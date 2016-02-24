package de.unistuttgart.iste.ps.skilled.ui.tools.export;

import java.awt.EventQueue;
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
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillFile.Mode;


/**
 * This class provides the dialog window for "Export Tools" which allows the user to export a certain tool to a location of
 * his or her choice
 * 
 * @author Leslie Tso
 *
 */
public class ExportTools {
    Display display;
    String fName = "";
    String fSaveLocation = "";
    String[] fToolName;
    File fCheckSave;

    ArrayList<File> fListofFiles = null;
    List<String> fToolNameList = null;
    ArrayList<Tool> fToolList = null;
    List<String> fToolPathList = null;
    IProject activeProject;
    String path;
    SkillFile skillfile;
    String fSaveName = "";

    private final ArrayList<Tool> allToolList = new ArrayList<Tool>();
    private final ArrayList<String> pathList = new ArrayList<String>();

    // Location of the workspace the user is using
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();

    /**
     * Creates dialog window
     * 
     */
    public void run() {
        Shell shell = new Shell(display);
        shell.setText("Export Tool");
        shell.layout(true, true);
        try {
            IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor().getEditorInput();
            activeProject = file.getFile().getProject();
            path = activeProject.getLocation().toOSString() + File.separator + ".skills";
            skillfile = SkillFile.open(path, Mode.ReadOnly);
        } catch (@SuppressWarnings("unused") Exception e) {
            ShowMessage("Could not read .skills-file!", "File Generation Error");
            return;
        }

        ToolUtil.indexing(activeProject);

        if (skillfile.Tools() != null) {
            skillfile.Tools().forEach(tool -> allToolList.add(tool));
            skillfile.Tools().forEach(t -> pathList.add(path));
        }

        if (allToolList.size() > 0) {
            fToolNameList = new ArrayList<String>();
            for (int i = 0; i < allToolList.size(); i++) {
                fToolNameList.add(allToolList.get(i).getName());

            }
        }

        if (pathList.size() > 0) {
            fToolPathList = new ArrayList<String>();
            for (int i = 0; i < pathList.size(); i++) {
                fToolPathList.add(pathList.get(i));
            }
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
            cSelectTool.setItems(fToolNameList.toArray(new String[fToolNameList.size()]));

        } else {
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
                    ShowMessage("Invalid export format!", "Invalid Export Format");
                    return;
                } else if (fCheckSave.exists()) {
                    fSaveName = tSaveLocation.getText().substring(tSaveLocation.getText().lastIndexOf(File.separator) + 1);
                    int overwrite = ShowMessageOption();
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
            ShowMessage("Problem with .skillt files generation!", "File Generation Error");
            return;

        }
        for (File file : listofFiles) {
            if (file.isFile() && file.getName().endsWith(".skill")) {
                checkFiles.add(file);
            } else if (file.isDirectory()) {
                listAllFiles(file.getAbsolutePath(), checkFiles);
            }
        }
    }

    /**
     * Combines all files found by method listFiles
     * 
     */
    public void combineFiles() {

        int index = fToolNameList.indexOf(fName);

        // File path of the .skills file of the tool selected from the dropdown
        // menu (i.e. C:\\...\Workspace\Project\.skills)
        String fToolFilePath = fToolPathList.get(index);

        // File path of the project of the tool (i.e. C:\\...\Workspace\Project)
        String fToolProjectPath = fToolFilePath.substring(0, fToolFilePath.lastIndexOf(File.separator));

        // Name of the project (i.e. Project)
        String fToolProjectName = fToolProjectPath.substring(fToolProjectPath.lastIndexOf(File.separator) + 1,
                fToolProjectPath.length());

        // Generate tool folder and files
        IProject project = workspace.getRoot().getProject(fToolProjectName);
        ToolUtil.generateTemporarySKilLFiles(fName, project);

        // File path of the tool folder in the .skillt folder (i.e.
        // C:\\...\Workspace\Project\.skillt\Tool
        String fToolFolder = fToolProjectPath + File.separator + ".skillt" + File.separator + fName;

        fListofFiles = new ArrayList<>();
        listAllFiles(fToolFolder, fListofFiles);

        String fText = "# Tool " + fName + "\n";

        for (File f : fListofFiles) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String line;

                while ((line = br.readLine()) != null) {
                    // Ignore all head comments
                    if (!line.startsWith("#")) {
                        fText += line + "\n";
                    }
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // Head comment that says which tool the merged files belong to
        try {
            FileWriter fw = new FileWriter(fCheckSave, true);
            fw.write(fText);
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @SuppressWarnings("static-method")
    private void ShowMessage(String string, String string2) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, string, string2, JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private int ShowMessageOption() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showOptionDialog(null, fSaveName + " already exists!" + " Do you want to overwrite it?",
                        "Existing File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            }
        });
        return JOptionPane.YES_NO_OPTION;
    }
}
