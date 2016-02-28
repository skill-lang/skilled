package de.unistuttgart.iste.ps.skilled.ui.refactoring.removeprojecthints;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;


/**
 * This class removes all hints from the project associated with the file currently active in the editor window.
 * 
 * @author Leslie
 * @author Tobias
 *
 */
public class SKilLRemoveProjectHints {

    /**
     * Remove hints from the file in the active editor window.
     */
    @SuppressWarnings("static-method")
    public void runFromMenu() {

        final String linebreak = System.lineSeparator();
        String fCurrentContents = "";
        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        page.saveAllEditors(false);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IPath basePath = root.getLocation();
        // Checks if there is an editor open
        if (editor != null) {
            IEditorInput input = editor.getEditorInput();
            // Finds path of the active file in the editor
            IPath path = ((FileEditorInput) input).getPath();
            String projectName = path.segment(basePath.segmentCount());
            LinkedList<java.io.File> fileList = new LinkedList<java.io.File>();

            // add all files in the project to a list
            fileList.add(new File(basePath.append(projectName).toString()));
            int index = 0;
            while (index < fileList.size()) {
                if (fileList.get(index).isDirectory()) {
                    java.io.File[] newFiles = fileList.get(index).listFiles();
                    if (newFiles != null)
                        fileList.addAll(Arrays.asList(newFiles));
                }
                index++;
            }
            for (File file : fileList) {
                if (!file.getName().endsWith(".skill"))
                    continue;
                if (file.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                        String line;
                        while ((line = br.readLine()) != null) {
                            // Skips all lines starting with "!"
                            if (!line.trim().startsWith("!")) {
                                fCurrentContents += line + linebreak;
                            }
                        }
                        br.close();
                    } catch (IOException e) {

                        // show error message in a notification window
                        StringBuilder sb = new StringBuilder("Error: ");
                        sb.append(e.getMessage());
                        sb.append(linebreak);
                        for (StackTraceElement ste : e.getStackTrace()) {
                            sb.append(ste.toString());
                            sb.append(linebreak);
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
                } else {
                    JOptionPane.showMessageDialog(null, "Please save the current file before removing hints!",
                            "File not saved!", JOptionPane.ERROR_MESSAGE);
                }

                // replace the file with a file where the hints have been removed
                try {
                    FileWriter fw = new FileWriter(file, false);
                    fw.write(fCurrentContents);
                    fw.close();
                    fCurrentContents = "";
                    root.getProject(projectName).refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                    page.saveAllEditors(false);
                } catch (IOException e) {

                    // show error message in a notification window
                    StringBuilder sb = new StringBuilder("Error: ");
                    sb.append(e.getMessage());
                    sb.append(linebreak);
                    for (StackTraceElement ste : e.getStackTrace()) {
                        sb.append(ste.toString());
                        sb.append(linebreak);
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
                } catch (CoreException e) {

                    // very unlikely that this will ever happen
                    e.printStackTrace();
                }
            }
            // Error message if there no editor is open.
        } else {
            JOptionPane.showMessageDialog(null, "No open file!", "Invalid File!", JOptionPane.ERROR_MESSAGE);
        }
    }

}
