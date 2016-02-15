package de.unistuttgart.iste.ps.skilled.ui.refactoring.removehints;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * This class removes hints from either the file in the active editor window
 * (action started from the main menu) or the file selected in the package
 * explorer (action started in the popup menu)
 * 
 * @author Leslie
 * @author Tobias
 *
 */
public class SKilLRemoveHints {
	
	/**
	 * Remove hints from the file in the active editor window.
	 */
	public void runFromMenu() {
		
	    final String linebreak = System.lineSeparator();
		String fCurrentContents = "";
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IPath basePath = root.getLocation();
        
		// Checks if there is an editor open
		if (editor != null) {
			IEditorInput input = editor.getEditorInput();
			// Finds path of the active file in the editor
			IPath path = ((FileEditorInput) input).getPath();
			String projectName = path.segment(basePath.segmentCount());
			NullProgressMonitor npm = new NullProgressMonitor();
			// Saves any un-saved changes
			editor.doSave(npm);
			File f = path.toFile();
			// Double checks that the file has not been moved or deleted in the meantime.
			if (f.exists()) {
				try {

					FileInputStream fis = new FileInputStream(f);
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

			try {
				FileWriter fw = new FileWriter(f, false);
				fw.write(fCurrentContents);
				fw.close();
				fCurrentContents = "";
				root.getProject(projectName).refreshLocal(IResource.DEPTH_INFINITE, npm);
			} catch (IOException e) {
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
                e.printStackTrace();
            }
		} 
		// Error message if there no editor is open.
		else {
			JOptionPane.showMessageDialog(null, "No open file!", "Invalid File!", JOptionPane.ERROR_MESSAGE);
		}
	}

}
