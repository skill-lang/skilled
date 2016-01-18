package de.unistuttgart.iste.ps.skilled.ui.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.File;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;


public class EditorUtil {

    EditorUtil2 eu = new EditorUtil2();

    public boolean openToolInEditor(Tool tool, IProject project) {
        // URI uri = URI.createURI(project.getLocation().toPortableString() + "/.skillt/" + tool.getName());
        // project.getFolder(URIUt)
        Set<File> toolFiles = getToolFiles(tool, project);
        for (File f : toolFiles) {
            String platformString = f.eResource().getURI().toPlatformString(true);
            IFile myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));

            IWorkbench wb = PlatformUI.getWorkbench();
            IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
            IWorkbenchPage page = win.getActivePage();

            IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(myFile.getName());
            try {
                page.openEditor(new FileEditorInput(myFile), desc.getId());
            } catch (PartInitException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean openTypeInEditor(Tool tool, Type type, IProject project) {

        URI toolFileURI = getToolFileURI(tool, type, project);
        File file = eu.getFileFromURI(toolFileURI);
        int i = 0;
        for (Declaration dec : file.getDeclarations()) {
            if (dec.getName().equals(type.getName())) {
                ICompositeNode ic = NodeModelUtils.getNode(dec);
                i = ic.getStartLine();
            }
        }

        IFile myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(toolFileURI.toPlatformString(false)));

        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
        IWorkbenchPage page = win.getActivePage();

        try {
            HashMap map = new HashMap();
            map.put(IMarker.LINE_NUMBER, i);
            IMarker marker = myFile.createMarker(IMarker.TEXT);
            marker.setAttributes(map);
            // page.openEditor(marker); //2.1 API

            IDE.openEditor(page, marker);
            marker.delete();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 3.0 API

        return true;
    }

    public URI getToolFileURI(Tool tool, Type type, IProject project) {
        String originalFilePath = type.getFile().getPath();
        URI originalFileUri = URI.createPlatformResourceURI(originalFilePath, true);
        // String[] segments = uri.segments();
        URI deresolvedOriginalFileUri = originalFileUri
                .deresolve(URI.createPlatformResourceURI(project.getLocation().toString(), true));
        String[] segments = deresolvedOriginalFileUri.segments();
        String path = "";
        if (segments[0].equals(project.getName())) {
            for (int i = 0; i < segments.length; i++) {
                if (i == 1) {
                    path += "/.skillt" + "/" + tool.getName();
                }
                if (i == 0) {
                    path += segments[i];
                } else {
                    path += "/" + segments[i];
                }
            }
        }

        return URI.createPlatformResourceURI(path, true);
    }

    public Set<File> getToolFiles(Tool tool, IProject project) {
        Set<File> toolFiles = new HashSet<File>();
        // URI uri = URI.createURI(project.getLocation().toPortableString() + "/.skillt/" + tool.getName());
        // project.getFolder(URIUt)
        Set<File> files = eu.getAll(project);
        for (File f : files) {
            String[] segments = f.eResource().getURI().segments();
            if (segments.length >= 5) {
                if (segments[3].equals(tool.getName())) {
                    toolFiles.add(f);
                }
            }
        }

        return toolFiles;

    }

}
