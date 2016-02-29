package de.unistuttgart.iste.ps.skilled.ui.quickfix.organize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Region;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.XtextDocument;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.TextRegion;
import org.eclipse.xtext.util.Tuples;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import de.unistuttgart.iste.ps.skilled.formatting2.SKilLImportOrganizer;
import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skilled.util.SKilLServices;
import de.unistuttgart.iste.ps.skilled.util.DependencyGraph.DependencyGraph;


/**
 * Handles the Organize-Imports-Quickfix
 * 
 * @author Marco Link
 *
 */
public class SKilLOrganizeImportsHandler {

    private SKilLServices services = new SKilLServices();
    private SKilLImportOrganizer skillImportOrganizer;

    public SKilLOrganizeImportsHandler(DependencyGraph dependencyGraph) {
        skillImportOrganizer = new SKilLImportOrganizer(dependencyGraph);
    }

    /**
     * Creates the includes for the main file, basically a index of all files in the project.
     * 
     * @param files
     *            The Set of all files
     * @param mainFileURI
     *            The URI of the main file
     * @return the String containing all includes of the main file
     */
    private static String createMainFileIncludes(Set<File> files, URI mainFileURI) {
        if (files == null || mainFileURI == null) {
            return null;
        }
        List<String> includes = new ArrayList<>();
        StringBuilder includeBuilder = new StringBuilder();
        Iterator<File> fileIterator = files.iterator();
        while (fileIterator.hasNext()) {
            File file = fileIterator.next();
            if (!file.eResource().getURI().path().equals(mainFileURI.path())) {
                includes.add(EcoreUtil2.getNormalizedResourceURI(file).deresolve(mainFileURI).path());
            }
        }
        Collections.sort(includes);
        for (String include : includes) {
            includeBuilder.append("include " + "\"" + include + "\"" + "\n");
        }
        return includeBuilder.toString();
    }

    /**
     * Organizes the imports for a whole project.
     * 
     * @param file
     */
    public void organizeImportsWholeProject(File file) {
        IProject project = services.getProject(file);
        File main = services.getMainFile(file);
        Set<File> files = services.getAll(file, true);
        StringBuilder sb = new StringBuilder();

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        IFile activeFile = ((IFileEditorInput) activeEditor.getEditorInput()).getFile();

        if (main == null) {
            String mainFileName = project.getName() + "-all" + ".skill";
            IFile mainFile = project.getFile(mainFileName);
            URI mainFileURI = URI.createPlatformResourceURI(project.getName() + "/" + mainFileName, true);
            sb.append("# @Main" + "\n\n");
            sb.append(createMainFileIncludes(files, mainFileURI));

            byte[] bytes = sb.toString().getBytes();
            InputStream source = new ByteArrayInputStream(bytes);
            try {
                mainFile.delete(true, new NullProgressMonitor());
            } catch (CoreException e) {
                e.printStackTrace();
            }
            if (!mainFile.exists()) {
                try {
                    mainFile.create(source, true, new NullProgressMonitor());
                } catch (CoreException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        source.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        main = services.getMainFile(file);
        sb.append(createMainFileIncludes(files, EcoreUtil2.getURI(main)));
        Iterator<File> fileIterator = files.iterator();
        while (fileIterator.hasNext()) {
            File f2 = fileIterator.next();
            if (EcoreUtil2.equals(f2, main)) {
                writeInclude(f2, sb.toString());
                continue;
            }
            final String organizedImportSection = "include " + "\""
                    + main.eResource().getURI().deresolve(f2.eResource().getURI()).path() + "\"" + "\n";
            writeInclude(f2, organizedImportSection);
        }

        try {
            IDE.openEditor(page, activeFile, true);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the new include in a file, closing it afterwards if it was not open to begin with.
     * 
     * @param file
     *            The File the include should be written in
     * @param include
     *            The include
     */
    private void writeInclude(File file, final String include) {
        IFile iFile = services.getIFile(file);

        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        XtextEditor editor = null;

        List<IEditorReference> references = new ArrayList<>(Arrays.asList(page.getEditorReferences()));

        try {
            editor = (XtextEditor) IDE.openEditor(page, iFile, true);
        } catch (PartInitException e) {
            e.printStackTrace();
        }

        if (editor != null) {
            XtextDocument document = (XtextDocument) editor.getDocument();
            Pair<Region, String> result = document.readOnly(new IUnitOfWork<Pair<Region, String>, XtextResource>() {
                @Override
                public Pair<Region, String> exec(XtextResource xtextResource) throws Exception {
                    File type = null;
                    Iterator<EObject> objectIterator = xtextResource.getContents().iterator();
                    while (objectIterator.hasNext()) {
                        EObject fileObject = objectIterator.next();
                        if (fileObject instanceof File) {
                            type = (File) fileObject;
                        }
                    }
                    TextRegion importRegion = SKilLImportOrganizer.getIncludeImportRegion(type);
                    if (importRegion != null) {
                        if (include != null) {
                            return Tuples.create(new Region(importRegion.getOffset(), importRegion.getLength()), include);
                        }

                        final String organizedImportSection = skillImportOrganizer.getOrganizedImportSection(type);
                        if (organizedImportSection != null) {
                            return Tuples.create(new Region(importRegion.getOffset(), importRegion.getLength()),
                                    organizedImportSection);
                        }
                    }
                    return null;
                }
            });
            if (result != null) {
                try {
                    Region region = result.getFirst();
                    int offset = region.getOffset();
                    int length = region.getLength();
                    String importSection = result.getSecond();
                    String string = document.get(offset, length);
                    if (!string.equals(importSection)) {
                        document.replace(offset, length, importSection);

                        workbench.saveAllEditors(false);
                        boolean wasOpened = false;
                        Iterator<IEditorReference> referenceIterator = references.iterator();
                        while (referenceIterator.hasNext()) {
                            IEditorReference ref = referenceIterator.next();
                            if (ref.getEditor(true).equals(editor)) {
                                wasOpened = true;
                            }
                        }

                        // close all not previously opened editors
                        if (!wasOpened) {
                            page.closeEditor(editor, true);
                        }

                    }
                } catch (@SuppressWarnings("unused") BadLocationException e) {
                    // Ignore.
                }
            }
        }
    }
}
