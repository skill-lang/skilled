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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
 *
 */
public class SKilLRemoveHints {

	/** 
	 * Remove hints from the file in the active editor window.
	 */
	public void runFromMenu() {
		String fCurrentContents = "";
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		IEditorPart editor = page.getActiveEditor();
		IEditorInput input = editor.getEditorInput();
		// Finds path of the active file in the editor
		IPath path = ((FileEditorInput) input).getPath();
		NullProgressMonitor npm = new NullProgressMonitor();
		// Saves any un-saved changes
		editor.doSave(npm);
		File f = path.toFile();
		if (f.exists() && !npm.isCanceled()) {
			try {

				FileInputStream fis = new FileInputStream(f);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line;
				while ((line = br.readLine()) != null) {
					if (!line.startsWith("!")) {
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
			FileWriter fw = new FileWriter(f, false);
			if (fCurrentContents != null) {
				fw.write(fCurrentContents);
			}
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

	/**
	 * Removes hints from file selected in the package explorer
	 */
	public void runFromContextMenu() {
		String fCurrentContents = "";
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelection selection = window.getSelectionService().getSelection("org.eclipse.jdt.ui.PackageExplorer");
		IStructuredSelection sel = (IStructuredSelection) selection;
		Object element = sel.getFirstElement();
		// Checks if selected file is a .skill-file
		if (element.toString().endsWith(".skill")) {
			// Gets full path of the file
			IFile file = Platform.getAdapterManager().getAdapter(element, IFile.class);
			String location = file.getLocation().toOSString();
			File f = new File(location);
			if (f.exists() && location.endsWith(".skill")) {
				try {
					FileInputStream fis = new FileInputStream(f);
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					String line;
					while ((line = br.readLine()) != null) {
						if (!line.startsWith("!")) {
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
				JOptionPane.showMessageDialog(null, "Selected file is not a .skill-file!", "Invald File!",
						JOptionPane.ERROR_MESSAGE);
			}

			try {
				FileWriter fw = new FileWriter(f, false);
				if (fCurrentContents != null) {
					fw.write(fCurrentContents);
				}
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
		} else {
			JOptionPane.showMessageDialog(null, "Selected file is not a .skill-file!", "Invald File!",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
