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

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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
    public void runFromMenu() {
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
            IPath shortPath = new Path(path.toString().substring(basePath.toString().length()));
            String projectName = shortPath.segment(0);
            LinkedList<java.io.File> fileList = new LinkedList<java.io.File>();
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
                if (!file.getPath().endsWith(".skill"))
                    continue;
                if (file.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                        String line;
                        while ((line = br.readLine()) != null) {
                            // Skips all lines starting with "!"
                            if (!line.trim().startsWith("!")) {
                                fCurrentContents += line + "\n";
                            }
                        }
                        br.close();
                    } catch (IOException e) {
                        StringBuilder sb = new StringBuilder("Error: ");
                        sb.append(e.getMessage());
                        sb.append("\n");
                        for (StackTraceElement ste : e.getStackTrace()) {
                            sb.append(ste.toString());
                            sb.append("\n");
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
                try {
                    FileWriter fw = new FileWriter(file, false);
                    fw.write(fCurrentContents);
                    fw.close();
                    fCurrentContents = "";
                } catch (IOException e) {
                    StringBuilder sb = new StringBuilder("Error: ");
                    sb.append(e.getMessage());
                    sb.append("\n");
                    for (StackTraceElement ste : e.getStackTrace()) {
                        sb.append(ste.toString());
                        sb.append("\n");
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
            // Error message if there no editor is open.
        } else {
            JOptionPane.showMessageDialog(null, "No open file!", "Invalid File!", JOptionPane.ERROR_MESSAGE);
        }
    }

}
