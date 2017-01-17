package de.unistuttgart.iste.ps.skilled.ui.tools;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.Type;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.skill.Declaration;
import de.unistuttgart.iste.ps.skilled.skill.Field;
import de.unistuttgart.iste.ps.skilled.skill.File;
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage;
import de.unistuttgart.iste.ps.skilled.util.SKilLServices;

/**
 * Useful methods for the ToolView to open the generated tool files.
 * 
 * @author Marco Link
 *
 */
public class EditorUtil {

    private SKilLServices services = new SKilLServices();

    /**
     * Opens all tool specific skill files of a tool in the editor.
     * 
     * @param tool
     *            which generated files should be opened.
     * @param project
     *            The project in which the tool is located.
     * @return true if everything went fine, otherwise false
     */
    public boolean openToolInEditor(Tool tool, IProject project) {
        try {
            refreshToolFolder(tool, project);
        } catch (CoreException e1) {
            e1.printStackTrace();
            return false;
        }
        Set<File> toolFiles = services.getToolFiles(tool.getName(), project);
        for (File f : toolFiles) {
            URI u = f.eResource().getURI();
            services.isToolFile(u);
            String platformString = f.eResource().getURI().toPlatformString(true);
            IFile myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));

            IWorkbench wb = PlatformUI.getWorkbench();
            IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
            IWorkbenchPage page = win.getActivePage();

            IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(myFile.getName());
            try {
                page.openEditor(new FileEditorInput(myFile), desc.getId());
            } catch (PartInitException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Opens the given type of a tool in the editor.
     * 
     * @param tool
     *            in which the type is contained.
     * @param type
     *            The type which should be opened.
     * @param project
     *            The project in which the tool is located.
     * @return true if everything went fine, otherwise false
     */
    public boolean openTypeInEditor(Tool tool, UserdefinedType type, IProject project) {
        try {
            refreshToolFolder(tool, project);
        } catch (CoreException e1) {
            e1.printStackTrace();
            return false;
        }
        URI toolFileURI = getToolFileURI(tool, type, project);
        File file = services.getFile(toolFileURI);
        if (file == null) {
            return false;
        }

        INode typeNameToMark = null;

        for (Declaration dec : file.getDeclarations()) {
            if (dec.getName().equals(ToolUtil.getActualName(type.getName()))) {
                List<INode> nodes = NodeModelUtils.findNodesForFeature(dec, SkillPackage.Literals.DECLARATION__NAME);
                if (nodes != null) {
                    if (nodes.size() == 1) {
                        typeNameToMark = nodes.get(0);
                    }
                }
                break;
            }
        }

        if (typeNameToMark == null) {
            return false;
        }

        IFile myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(toolFileURI.toPlatformString(false)));

        if (myFile == null) {
            return false;
        }

        return openFileAtSpecificLocation(myFile, typeNameToMark.getStartLine(), typeNameToMark.getOffset(),
                typeNameToMark.getTotalEndOffset());
    }

    /**
     * Opens the given field of a tool in the editor.
     * 
     * @param tool
     *            in which the field is contained.
     * @param field
     *            The field which should be opened.
     * @param project
     *            The project in which the tool is located.
     * @return true if everything went fine, otherwise false
     */
    public boolean openFieldInEditor(Tool tool, FieldLike field, IProject project) {
        try {
            refreshToolFolder(tool, project);
        } catch (CoreException e1) {
            e1.printStackTrace();
            return false;
        }
        URI toolFileURI = getToolFileURI(tool, field.getType(), project);
        File file = services.getFile(toolFileURI);
        if (file == null) {
            return false;
        }

        INode fieldToMark = null;

        Declaration declaration = null;

        for (Declaration dec : file.getDeclarations()) {
            throw new NoSuchMethodError("TBD");
            // if
            // (dec.getName().toLowerCase().equals(ToolUtil.getActualName(declaredIn(field)))))
            // {
            // declaration = dec;
            // break;
            // }
        }

        if (declaration == null) {
            return false;
        }

        for (Field f : services.getFields(declaration)) {
            if (f.getFieldcontent().getName().equals(ToolUtil.getActualName(field.getName()))) {
                List<INode> nodes = NodeModelUtils.findNodesForFeature(f.getFieldcontent(),
                        SkillPackage.Literals.FIELDCONTENT__NAME);
                if (nodes != null) {
                    if (nodes.size() == 1) {
                        fieldToMark = nodes.get(0);
                    }
                }
            }
        }

        if (fieldToMark == null) {
            return false;
        }

        IFile myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(toolFileURI.toPlatformString(false)));

        if (myFile == null) {
            return false;
        }

        return openFileAtSpecificLocation(myFile, fieldToMark.getStartLine(), fieldToMark.getOffset(),
                fieldToMark.getTotalEndOffset());
    }

    /**
     * Returns the location of the file that defines the given type for a given tool.
     * 
     * @note this method appears to be eroded and requires reimplementation and renaming 
     * 
     * @param tool
     *            The tool in which the needed file is located.
     * @param type
     *            The type which is located in the needed toolfile.
     * @param project
     *            The project in which the tool is located.
     * @return A URI of the needed toolfile.
     */
    public static URI getToolFileURI(Tool tool, Type type, IProject project) {
        throw new NoSuchMethodError();
        
//        URI originalFileUri = URI.createPlatformResourceURI(originalFilePath, true);
//        URI deresolvedOriginalFileUri = originalFileUri
//                .deresolve(URI.createPlatformResourceURI(project.getLocation().toString(), true));
//        String[] segments = deresolvedOriginalFileUri.segments();
//        String path = "";
//        if (segments[0].equals(project.getName())) {
//            for (int i = 0; i < segments.length; i++) {
//                if (i == 1) {
//                    path += "/.skillt" + "/" + tool.getName();
//                }
//                if (i == 0) {
//                    path += segments[i];
//                } else {
//                    path += "/" + segments[i];
//                }
//            }
//        }
//
//        return URI.createPlatformResourceURI(path, true);
    }

    /**
     * Helping method which opens the editor and also moves the cursor at a
     * specific location.
     * 
     * @param fileToOpen
     *            The file which should be opened.
     * @param startLineNumber
     *            The line number in which the cursor should be positioned.
     * @param charStart
     *            The offset of the beginning char.
     * @param charEnd
     *            The end offset of the last char.
     * @return true if everything went fine, otherwise false
     */
    public static boolean openFileAtSpecificLocation(IFile fileToOpen, int startLineNumber, int charStart,
            int charEnd) {
        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
        IWorkbenchPage page = win.getActivePage();

        if (fileToOpen == null) {
            return false;
        }

        try {
            IMarker marker = fileToOpen.createMarker(IMarker.TEXT);
            MarkerUtilities.setLineNumber(marker, startLineNumber);
            MarkerUtilities.setCharStart(marker, charStart);
            MarkerUtilities.setCharEnd(marker, charEnd);
            IDE.openEditor(page, marker);
            marker.delete();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Refresehes a specific tool folder.
     * 
     * @param tool
     *            The tool which folder should be refreshed.
     * @param project
     *            The project in which the tool is located.
     * @throws CoreException
     */
    public static void refreshToolFolder(Tool tool, IProject project) throws CoreException {
        IFolder folder = project.getFolder(".skillt/" + tool.getName());
        folder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
    }
}
