package de.unistuttgart.iste.ps.skilled.ui.refactoring.combinefiles;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;


/**
 * This class merges the .skill files selected in the SKilLCombineRefactoringDialog and deletes the original .skill files.
 * 
 * @author Leslie Tso
 * @author Tobias Heck
 */

public class SKilLCombineRefactoring {

    int numberSelected;
    String[] fCombines;
    String fCombinedSave;
    File[] files;

    String fComment = "";
    String fHead = "";
    String fBody = "";
    String fPreCombinedLocation;
    int aFileHasHeadCommentCheck = 0;
    String[] fPreCombinedWithFolderPath;
    String[] fPreCombinedWithoutProjectFolderPath;
    String[] fPreCombinedWithoutProjectFolderPathAndForwardSlash;
    File workspaceDirectory;

    int fWorkspaceLength;

    public SKilLCombineRefactoring() {
    }

    public static String getName() {
        return "Combine .skill files";
    }

    public void setNumberSelected(int number) {
        numberSelected = number;
    }

    public void setCombine(String[] text) {
        fCombines = new String[numberSelected];
        for (int i = 0; i < numberSelected; i++) {
            fCombines[i] = text[i];
        }
    }

    public void setCombinedSave(String text) {
        fCombinedSave = text;
    }

    public void setWorkSpaceDirectory(File file) {
        workspaceDirectory = file;
    }

    // get file paths of to be combined files and set file path of combined
    // file.
    public void startCombine() {
        files = new File[numberSelected];
        fPreCombinedWithFolderPath = new String[numberSelected];
        fPreCombinedWithoutProjectFolderPath = new String[numberSelected];
        fPreCombinedWithoutProjectFolderPathAndForwardSlash = new String[numberSelected];

        fWorkspaceLength = workspaceDirectory.getAbsolutePath().length();

        for (int i = 0; i < numberSelected; i++) {
            files[i] = new File(fCombines[i]);

            fPreCombinedWithFolderPath[i] = fCombines[i].substring(fWorkspaceLength + 1);
            fPreCombinedWithoutProjectFolderPath[i] = fPreCombinedWithFolderPath[i]
                    .substring(fPreCombinedWithFolderPath[i].indexOf(File.separator) + 1);
            fPreCombinedWithoutProjectFolderPathAndForwardSlash[i] = fPreCombinedWithoutProjectFolderPath[i].replace("\\",
                    "/");
        }
        organize(files);
    }

    /**
     * Splits includes and withs, head comments and body for all files
     * 
     * @param files
     *            The pre-combined files
     */
    public void organize(File[] files) {
        Set<String> duplicateComment = new HashSet<>();

        // Path of combined file here
        File combinedFile = new File(fCombinedSave);

        int count = -1;
        for (File f : files) {
            FileInputStream fis;
            int thisFileHasHeadCommentCheck = 0;
            count++;
            String include = "include";
            try {
                fis = new FileInputStream(f);
                BufferedReader br1 = new BufferedReader(new InputStreamReader(fis));
                String line;
                loop: while ((line = br1.readLine()) != null) {
                    // write everything that is not include, with or head
                    // comment to fBody
                    if (!line.startsWith("include") && !line.startsWith("with") && !line.startsWith("#")) {
                        fBody += line + System.lineSeparator();
                    }
                    // All includes and withs saved in fHead
                    else if (line.startsWith("include") || line.startsWith("with")) {
                        if (line.startsWith("w"))
                            include = "with";
                        // Deletes withs and includes that require the use of
                        // the pre-combined files
                        line = normalizePath(line, fPreCombinedWithoutProjectFolderPathAndForwardSlash[count]);
                        for (String path : fPreCombinedWithoutProjectFolderPathAndForwardSlash) {
                            if (path.equals(line))
                                continue loop;
                        }
                        String newFilePath = fCombinedSave.substring(fWorkspaceLength + 1)
                                .substring(fCombinedSave.substring(fWorkspaceLength + 1).indexOf(File.separator) + 1)
                                .replace("\\", "/");
                        int firstDifference;
                        for (firstDifference = 0; firstDifference < newFilePath.length()
                                && firstDifference < line.length(); firstDifference++) {
                            if (newFilePath.charAt(firstDifference) != line.charAt(firstDifference))
                                break;
                        }
                        line = line.substring(firstDifference);
                        int goUp = newFilePath.substring(firstDifference).length()
                                - newFilePath.substring(firstDifference).replace("/", "").length();
                        for (int i = 0; i < goUp; i++) {
                            line = "../" + line;
                        }
                        if (!fHead.contains(line))
                            fHead += include + " \"" + line + "\"" + System.lineSeparator();
                    }
                    // All head comments save in fComment
                    else if (line.startsWith("#")) {
                        String commentChecker = line.substring(1);
                        commentChecker.replaceAll("\\s+", "");
                        if (!commentChecker.isEmpty()) {
                            // Checks for duplicate head comments
                            if (!duplicateComment.contains(line)) {
                                // There is a head comment in THIS file
                                thisFileHasHeadCommentCheck = 1;
                                // One of the pre-combined files has a head
                                // comment
                                aFileHasHeadCommentCheck = 1;
                                fComment += line + System.lineSeparator();
                                duplicateComment.add(line);
                            }
                        }
                    }
                }
                // Adds # after the head comment(s) of a file
                if (thisFileHasHeadCommentCheck == 1) {
                    fComment += "#" + System.lineSeparator();
                    thisFileHasHeadCommentCheck = 0;
                }
                br1.close();
                fBody += System.lineSeparator();
            } catch (IOException e) {
                StringBuilder sb = new StringBuilder("Error: ");
                sb.append(e.getMessage());
                sb.append(System.lineSeparator());
                for (StackTraceElement ste : e.getStackTrace()) {
                    sb.append(ste.toString());
                    sb.append(System.lineSeparator());
                }
                JTextArea jta = new JTextArea(sb.toString());
                JScrollPane jsp = new JScrollPane(jta) {
                    /**
                     * 
                     */
                    private static final long serialVersionUID = -7067159314180395545L;

                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(400, 300);
                    }
                };
                JOptionPane.showMessageDialog(null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
            }

        }

        if (aFileHasHeadCommentCheck == 1) {
            // Deletes last # in comment.txt
            fComment = fComment.substring(0, fComment.length() - 2);
        }

        combine(combinedFile);

    }

    private static String normalizePath(String line, String path) {
        String[] lines = line.split("\"");
        if (lines.length < 2)
            return "";
        int lastIndex = path.lastIndexOf("/");
        if (lastIndex > -1) {
            lines[0] = path.substring(0, lastIndex);
        } else {
            lines[0] = "";
        }
        while (lines[1].startsWith("../")) {
            lines[1] = lines[1].substring(3);
            lastIndex = lines[0].lastIndexOf("/");
            if (lastIndex > -1) {
                lines[0] = lines[0].substring(0, lastIndex);
            } else {
                lines[0] = "";
            }
        }
        lines[0] = lines[0] + "/" + lines[1];
        if (lines[0].startsWith("/"))
            lines[0] = lines[0].substring(1);
        return lines[0];
    }

    /**
     * Writes fComment, fHead and fBody into a single file
     * 
     * @param combinedFile
     *            Creates a file with the combined content of the pre-combined files
     */

    public void combine(File combinedFile) {
        try {
            FileWriter fw = new FileWriter(combinedFile, true);

            if (fComment != null) {
                fw.write(fComment);
                if (!fComment.equals("")) fw.write(System.lineSeparator());
            }
            if (fHead != null) {
                fw.write(fHead);
                if (!fHead.equals("")) fw.write(System.lineSeparator());
            }
            if (fBody != null) {
                fw.write(fBody);
            }
            fw.close();

            // Empty strings to remove append errors
            fComment = "";
            fHead = "";
            fBody = "";
            aFileHasHeadCommentCheck = 0;

            // Deletes the original pre-combined files
            for (int i = 0; i < numberSelected; i++) {
                files[i].delete();
            }

            // refresh workspace
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            String projectName = fPreCombinedWithFolderPath[0].split("\\" + File.separator)[0];
            root.getProject(projectName).refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

            // open new file
            String shortPath = combinedFile.getAbsolutePath().split(root.getLocation().lastSegment())[1];
            IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor("a.skill");
            Constructor<org.eclipse.core.internal.resources.File> constructor = (Constructor<org.eclipse.core.internal.resources.File>) org.eclipse.core.internal.resources.File.class
                    .getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            org.eclipse.core.internal.resources.File file = constructor
                    .newInstance(new org.eclipse.core.runtime.Path(shortPath), ResourcesPlugin.getWorkspace());
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(file), desc.getId());
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder("Error: ");
            sb.append(e.getMessage());
            sb.append(System.lineSeparator());
            for (StackTraceElement ste : e.getStackTrace()) {
                sb.append(ste.toString());
                sb.append(System.lineSeparator());
            }
            JTextArea jta = new JTextArea(sb.toString());
            JScrollPane jsp = new JScrollPane(jta) {

                private static final long serialVersionUID = 1L;

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(400, 300);
                }
            };
            JOptionPane.showMessageDialog(null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (CoreException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
