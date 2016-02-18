package de.unistuttgart.iste.ps.skilled.ui.quickfix.organize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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


public class SKilLOrganizeImportsHandler {

    private SKilLServices services = new SKilLServices();
    private SKilLImportOrganizer sKilLImportOrganizer;

    public SKilLOrganizeImportsHandler(DependencyGraph dg) {
        sKilLImportOrganizer = new SKilLImportOrganizer(dg);
    }

    private static String A(Set<File> files, URI mainFileURI) {
        if (files == null || mainFileURI == null) {
            return null;
        }
        List<String> includes = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            if (!f.eResource().getURI().path().equals(mainFileURI.path())) {
                includes.add(EcoreUtil2.getNormalizedResourceURI(f).deresolve(mainFileURI).path());
            }
        }
        for (String include : includes) {
            sb.append("include " + "\"" + include + "\"" + "\n");
        }
        return sb.toString();
    }

    public void organizeImportsWholeProject(File file) {
        IProject project = services.getProject(file);
        File main = services.getMainFile(file);
        Set<File> files = services.getAll(file);
        StringBuilder sb = new StringBuilder();

        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart activeEditor = page.getActiveEditor();
        IFile activeFile = ((IFileEditorInput) activeEditor.getEditorInput()).getFile();

        if (main == null) {
            String mainFileName = project.getName() + "-all" + ".skill";
            IFile mainFile = project.getFile(mainFileName);
            URI mainFileURI = URI.createPlatformResourceURI(project.getName() + "/" + mainFileName, true);
            sb.append("#@Main" + "\n\n");
            sb.append(A(files, mainFileURI));

            byte[] bytes = sb.toString().getBytes();
            InputStream source = new ByteArrayInputStream(bytes);
            try {
                mainFile.delete(true, new NullProgressMonitor());
            } catch (CoreException e1) {
                e1.printStackTrace();
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
        sb.append(A(files, EcoreUtil2.getURI(main)));
        for (File f2 : files) {
            if (EcoreUtil2.equals(f2, main)) {

                BLUB(f2, sb.toString());
                continue;
            }
            final String organizedImportSection = "include " + "\""
                    + main.eResource().getURI().deresolve(f2.eResource().getURI()).path() + "\"" + "\n";
            BLUB(f2, organizedImportSection);
        }

        try {
            IDE.openEditor(page, activeFile, true);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    public void b(File file) {
        BLUB(file, null);
    }

    private void BLUB(File file, final String ups) {
        IFile iFile = services.getIFile(file);

        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
        XtextEditor editor = null;

        IEditorReference[] references = page.getEditorReferences();

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
                    for (EObject o : xtextResource.getContents()) {
                        if (o instanceof File) {
                            type = (File) o;
                        }
                    }
                    TextRegion importRegion = SKilLImportOrganizer.getIncludeImportRegion(type);
                    if (importRegion != null) {
                        if (ups != null) {
                            return Tuples.create(new Region(importRegion.getOffset(), importRegion.getLength()), ups);
                        }

                        final String organizedImportSection = sKilLImportOrganizer.getOrganizedImportSection(type);
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
                    String importSection = result.getSecond();
                    int offset = region.getOffset();
                    int length = region.getLength();
                    String string = document.get(offset, length);
                    if (!string.equals(importSection)) {
                        document.replace(offset, length, importSection);

                        workbench.saveAllEditors(false);
                        boolean wasOpened = false;
                        for (IEditorReference ref : references) {
                            if (ref.getEditor(true).equals(editor)) {
                                wasOpened = true;
                            }
                        }

                        if (!wasOpened) {
                            page.closeEditor(editor, true);
                        }

                    }
                } catch (BadLocationException e) {
                    // Ignore.
                }
            }
        }
    }
}
