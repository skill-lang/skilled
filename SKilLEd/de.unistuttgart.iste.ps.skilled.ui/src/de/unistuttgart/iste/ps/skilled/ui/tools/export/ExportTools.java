package de.unistuttgart.iste.ps.skilled.ui.tools.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IProject;
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

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skillls.tools.Tool;


/**
 * This class provides the dialog window for "Export Tools" which allows the user to export a certain tool to a location of
 * his or her choice
 * 
 * @author Leslie Tso
 *
 */
public class ExportTools {
    Display d;
    String fName = "";
    String fSaveLocation = "";
    String[] fToolName;
    File fCheckSave;

    ArrayList<File> fListofFiles = null;
    List<String> fToolNameList = null;
    SaveListofAllTools fSave;
    ArrayList<Tool> fToolList = null;
    List<String> fToolPathList = null;

    // Location of the workspace the user is using
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();

    /**
     * Creates dialog window
     * 
     */
    public void run() {
        Shell shell = new Shell(d);
        shell.setText("Export Tool");
        shell.layout(true, true);
        // Gets list of tool names and their paths from SaveListofAllTools.java
        fSave = SaveListofAllTools.getInstance();
        if (SaveListofAllTools.getToolNameList() != null && SaveListofAllTools.getToolPathList() != null) {
            fToolNameList = SaveListofAllTools.getToolNameList();
            fToolPathList = SaveListofAllTools.getToolPathList();
            System.out.println("fToolNameList.size() is " + fToolNameList.size());
        } else {
            System.out.println("run:fToolNameList is null");
        }

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
        gridDataLabel.horizontalSpan = 1;
        lSelectTool.setLayoutData(gridDataLabel);

        // Dropdown menu with a list of all tools
        Combo cSelectTool = new Combo(shell, SWT.READ_ONLY);
        GridData gridDataWidgets = new GridData();
        gridDataWidgets.horizontalAlignment = SWT.FILL;
        gridDataWidgets.grabExcessHorizontalSpace = true;
        gridDataWidgets.horizontalSpan = 3;
        cSelectTool.setLayoutData(gridDataWidgets);
        if (fToolNameList != null) {
            System.out.println("fToolNameList is not empty!");
            cSelectTool.setItems(fToolNameList.toArray(new String[fToolNameList.size()]));
        } else {
            System.out.println("fToolNameList is empty!");
            String emptyList[] = {};
            cSelectTool.setItems(emptyList);
        }
        fName = cSelectTool.getText();
        cSelectTool.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO
                fName = cSelectTool.getText();
            }
        });

        Label lSaveLocation = new Label(shell, SWT.NONE);
        lSaveLocation.setText("Export Location:");
        GridData gridDataLabel2 = new GridData();
        gridDataLabel2.horizontalAlignment = SWT.CENTER;
        gridDataLabel2.horizontalSpan = 1;
        lSaveLocation.setLayoutData(gridDataLabel2);

        // Export location here
        Text tSaveLocation = new Text(shell, SWT.BORDER | SWT.SINGLE);
        tSaveLocation.setText(fSaveLocation);
        GridData gridDataWidgetsSmall = new GridData();
        gridDataWidgetsSmall.horizontalAlignment = SWT.FILL;
        gridDataWidgetsSmall.grabExcessHorizontalSpace = true;
        gridDataWidgetsSmall.horizontalSpan = 2;
        tSaveLocation.setLayoutData(gridDataWidgetsSmall);
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
        gridDataButtons.horizontalAlignment = SWT.CENTER;
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

        // Fills grid cell with empty block
        Label lEmptyBlock = new Label(shell, SWT.NONE);
        lEmptyBlock.setLayoutData(new GridData());

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
                        // Reset text fields
                        fName = "";
                        fSaveLocation = "";
                        shell.dispose();
                    }
                }
                // Create tool file
                else {
                    combineFiles();
                    shell.dispose();
                    fName = "";
                    fSaveLocation = "";

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
    public void listAllFiles(String directoryName, ArrayList<File> checkFiles) {
        File fProjectDirectory = new File(directoryName);
        File[] listofFiles = fProjectDirectory.listFiles();
        if (null == listofFiles) {

            JOptionPane.showMessageDialog(null, "Problem with .skillt files generation!", "File Generation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;

        } else {
            System.out.println("listofFiles is: " + listofFiles.toString());
            for (File file : listofFiles) {
                if (file.isFile() && file.getName().endsWith(".skill")) {
                    checkFiles.add(file);
                } else if (file.isDirectory()) {
                    listAllFiles(file.getAbsolutePath(), checkFiles);
                }
            }
        }
    }

    /**
     * Combines all files found by method listFiles
     * 
     */
    public void combineFiles() {

        System.out.println("fName is: " + fName);
        int index = fToolNameList.indexOf(fName);
        System.out.println("Index is: " + index);

        // File path of the .skills file of the tool selected from the dropdown
        // menu (i.e. C:\\...\Workspace\Project\.skills)
        String fToolFilePath = fToolPathList.get(index);
        System.out.println("fToolFilePath is: " + fToolFilePath);

        // File path of the project of the tool (i.e. C:\\...\Workspace\Project)
        String fToolProjectPath = fToolFilePath.substring(0, fToolFilePath.lastIndexOf(File.separator));
        System.out.println("fToolProjectPath is: " + fToolProjectPath);

        // File path of the tool folder in the .skillt folder (i.e.
        // C:\\...\Workspace\Project\.skillt\Tool
        String fToolFolder = fToolProjectPath + File.separator + ".skillt" + File.separator + fName;
        System.out.println("fToolFolder is: " + fToolFolder);

        // Name of the project (i.e. Project)
        String fToolProjectName = fToolProjectPath.substring(fToolProjectPath.lastIndexOf(File.separator) + 1,
                fToolProjectPath.length());
        System.out.println("fToolProjectName is: " + fToolProjectName);

        // Generate tool folder and files
        IProject project = workspace.getRoot().getProject(fToolProjectName);
        ToolUtil.generateTemporarySKilLFiles(fName, project);
        System.out.println(".skillt Folder made");

        fListofFiles = new ArrayList<>();
        listAllFiles(fToolFolder, fListofFiles);

        for (File f : fListofFiles) {
            FileInputStream fis;
            String fText = "";
            try {
                FileWriter fw = new FileWriter(fCheckSave, true);
                fis = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line;

                while ((line = br.readLine()) != null) {
                    // Ignore all head comments
                    if (!line.startsWith("#")) {
                        fText += line + "\n";
                    }
                }

                // Head comment that says which tool the merged files belong to
                fw.write("# Tool " + fName + "\n");
                fw.write(fText);
                br.close();
                fw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
