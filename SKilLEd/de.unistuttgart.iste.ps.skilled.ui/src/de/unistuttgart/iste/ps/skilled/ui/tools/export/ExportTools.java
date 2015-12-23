package de.unistuttgart.iste.ps.skilled.ui.tools.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skillls.tools.Tool;

/**
 * This class provides the dialog window for "Export Tools" which allows the
 * user to export a certain tool to a location of his or her choice
 * 
 * @author Leslie
 *
 */
public class ExportTools {
	Display d;
	String fName = "";
	String fSaveLocation = "";
	String[] fToolName;
	String[] fToolPath;
	// String[] fToolLanguage;
	// Generator[] fToolGenerator;
	// String[] fToolModule;
	File fCheckSave;

	ArrayList<File> fListofFiles = null;

	// Location of the workspace the user is using
	IWorkspace workspace = ResourcesPlugin.getWorkspace();
	File workspaceDirectory = workspace.getRoot().getLocation().toFile();

	// Gets the names of the tools from
	// de.unistuttgart.iste.ps.skilled.ui.views.Toolview.java
	public void setListofAllTools(ArrayList<Tool> allToolList) {
		// TODO
		for (int i = 0; i < allToolList.size(); i++) {
			fToolName[i] = allToolList.get(i).getName();
			// fToolPath[i] = allToolList.get(i).getOutPath();
			// fToolLanguage[i] = allToolList.get(i).getLanguage();
			// ArrayList<Type>[] fToolTypes = (ArrayList<Type>[]) new
			// ArrayList[allToolList.get(i).getTypes().size()];
			// fToolTypes[i].addAll(allToolList.get(i).getTypes());
			// ArrayList<de.unistuttgart.iste.ps.skillls.tools.File>[]
			// fToolFiles =
			// (ArrayList<de.unistuttgart.iste.ps.skillls.tools.File>[]) new
			// ArrayList[allToolList
			// .get(i).getTypes().size()];
			// fToolFiles[i].addAll(allToolList.get(i).getFiles());
			// fToolGenerator[i] = allToolList.get(i).getGenerator();
			// fToolModule[i] = allToolList.get(i).getModule();

		}
	}

	// Gets the file paths of the tools from
	// de.unistuttgart.iste.ps.skilled.ui.views.Toolview.java
	public void setPaths(ArrayList<String> pathList) {
		for (int i = 0; i < pathList.size(); i++) {
			fToolPath[i] = pathList.get(i);
		}
	}

	/**
	 * Creates dialog window
	 * 
	 */
	public void run() {
		Shell shell = new Shell(d);
		shell.setText("Export Tool");
		shell.layout(true, true);
		createContents(shell);
		final Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		shell.setSize(newSize);
		shell.open();
	}

	/**
	 * Creates contents of the dialog window
	 * 
	 * @param shell
	 *            The window in which the content is created in
	 */
	public void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(4, true));

		Label title = new Label(shell, SWT.NONE);
		title.setText("Export Tool");
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.CENTER;
		gridData.horizontalSpan = 4;
		title.setLayoutData(gridData);

		Label lSelectTool = new Label(shell, SWT.NONE);
		lSelectTool.setText("Select tool to export:");
		GridData gridDataLabel = new GridData();
		gridDataLabel.horizontalAlignment = SWT.CENTER;
		gridDataLabel.horizontalSpan = 2;
		lSelectTool.setLayoutData(gridDataLabel);

		// Dropdown menu with a list of all tools
		Combo cSelectTool = new Combo(shell, SWT.READ_ONLY);
		GridData gridDataWidgets = new GridData();
		gridDataWidgets.horizontalAlignment = SWT.FILL;
		gridDataWidgets.grabExcessHorizontalSpace = true;
		gridDataWidgets.horizontalSpan = 2;
		cSelectTool.setLayoutData(gridDataWidgets);
		cSelectTool.setItems(fToolName);
		// Default combofield value (blank)
		cSelectTool.select(0);
		cSelectTool.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO
				fName = cSelectTool.getText();
			}
		});

		// Label lNameOfToolFile = new Label(shell, SWT.NONE);
		// lNameOfToolFile.setText("Name of tool file:");
		// lNameOfToolFile.setLayoutData(gridDataLabel);
		//
		// Text tNameOfToolFile = new Text(shell, SWT.BORDER | SWT.SINGLE);
		// tNameOfToolFile.setText(fName);
		// tNameOfToolFile.setLayoutData(gridDataWidgets);
		//
		// tNameOfToolFile.addModifyListener(new ModifyListener() {
		//
		// @Override
		// public void modifyText(ModifyEvent e) {
		// Text textWidget = (Text) e.getSource();
		// fName = textWidget.getText();
		// }
		// });

		// Fills grid cell with empty block
		Label lEmptyBlock = new Label(shell, SWT.NONE);
		lEmptyBlock.setLayoutData(new GridData());

		Label lSaveLocation = new Label(shell, SWT.NONE);
		lSaveLocation.setText("Export Location:");
		lSaveLocation.setLayoutData(new GridData());

		Text tSaveLocation = new Text(shell, SWT.BORDER | SWT.SINGLE);
		tSaveLocation.setText(fSaveLocation);
		tSaveLocation.setLayoutData(gridDataWidgets);
		tSaveLocation.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textWidget = (Text) e.getSource();
				fSaveLocation = textWidget.getText();
			}
		});

		Button bSaveLocation = new Button(shell, SWT.PUSH);
		bSaveLocation.setText("Browse");
		GridData gridDataButtons = new GridData();
		gridDataButtons.horizontalAlignment = SWT.FILL;
		gridDataButtons.verticalAlignment = SWT.FILL;
		bSaveLocation.setLayoutData(gridDataButtons);
		bSaveLocation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
				saveDialog.setFilterPath(workspaceDirectory.getAbsolutePath());
				String[] filterExtensions = { "*.skill" };
				saveDialog.setFilterExtensions(filterExtensions);
				String savePath = saveDialog.open();
				if (savePath != null) {
					tSaveLocation.setText(savePath);
				}
			}
		});

		// Creates OK Button.
		Button OK = new Button(shell, SWT.PUSH);

		OK.setLayoutData(gridDataButtons);
		OK.setText("OK");
		OK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {

				fCheckSave = new File(fSaveLocation);
				if (!fSaveLocation.endsWith(".skill") || fCheckSave.isDirectory()) {
					JOptionPane.showMessageDialog(null, "Invalid export format!", "Invalid Export Format",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (fCheckSave.exists()) {
					int overwrite = JOptionPane.showOptionDialog(null,
							fName + " already exists!" + " Do you want to overwrite it?", "Existing File",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (overwrite == JOptionPane.YES_OPTION) {
						fCheckSave.delete();
						combineFiles();
						// Reset save location
						fSaveLocation = "";
						shell.dispose();
					}
				}
				// Create tool file
				else {
					combineFiles();
					shell.dispose();

				}

			}
		});

		// Cancel button. Closes dialog window.
		Button Cancel = new Button(shell, SWT.PUSH);
		Cancel.setLayoutData(gridDataButtons);
		Cancel.setText("Cancel");
		Cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.dispose();
				fName = "";
				fSaveLocation = "";
			}
		});
	}

	/**
	 * Finds all .skill files in the project folder and its subdirectories
	 * 
	 * @param directoryName
	 * @param checkFiles
	 */
	public void listFiles(String directoryName, ArrayList<File> checkFiles) {
		File fProjectDirectory = new File(directoryName);
		File[] listofFiles = fProjectDirectory.listFiles();
		for (File file : listofFiles) {
			if (file.isFile() && file.getName().endsWith(".skill")) {
				checkFiles.add(file);
			} else if (file.isDirectory()) {
				listFiles(file.getAbsolutePath(), checkFiles);
			}
		}
	}

	/**
	 * Combines all files found by method listFiles
	 * 
	 */
	public void combineFiles() {

		int fWorkspaceLength = workspaceDirectory.getAbsolutePath().length();

		System.out.println("fName is: " + fName);
		int index = Arrays.asList(fToolName).indexOf(fName);

		// File path of tool selected from the dropdown menu
		String fToolFilePath = fToolPath[index];
		System.out.println("fToolFilePath is: " + fToolFilePath);

		String fToolFilePathWithoutWorkspace = fToolFilePath.substring(fWorkspaceLength + 1);
		System.out.println("fToolFilePathWithoutWorkspace is: " + fToolFilePathWithoutWorkspace);

		String fToolFilePathWithoutProjectFolder = fToolFilePathWithoutWorkspace
				.substring(fToolFilePathWithoutWorkspace.indexOf(File.separator) + 1);
		System.out.println("fToolFilePathWithoutProjectFolder is: " + fToolFilePathWithoutProjectFolder);

		int fLengthofProject = fToolFilePathWithoutWorkspace.length() - fToolFilePathWithoutProjectFolder.length();

		String fProjectPath = fToolFilePath.substring(0, fToolFilePath.length() + fLengthofProject);
		System.out.println("fProjectPath is: " + fProjectPath);

		// i.e. C:\\...\ProjectRoot\.tools\toolname
		String fToolFolder = fProjectPath + File.separator + ".tools" + File.separator + fName;
		System.out.println("fToolFolder is: " + fToolFolder);

		// File fToolDirectory = new File(fToolFolder);
		// File[] fListofFiles = fToolDirectory.listFiles();

		listFiles(fToolFolder, fListofFiles);

		for (File f : fListofFiles) {
			FileInputStream fis;
			try {
				FileWriter fw = new FileWriter(fCheckSave, true);
				fis = new FileInputStream(f);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line;
				// Head comment that says which tool the merged files are
				fw.write("# Tool " + fName);
				while ((line = br.readLine()) != null) {
					// Ignore all other head comments
					if (!line.startsWith("#")) {
						fw.write(line);
					}
				}
				br.close();
				fw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
