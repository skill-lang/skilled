package de.unistuttgart.iste.ps.skilled.ui.tools.imprt;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillFile.Mode;


/**
 * This class provides the dialog window for "Export Tools" which allows the user to export a certain tool to a location of
 * his or her choice
 * 
 * @author Leslie
 * @author Ken Singer
 */
public class ImportTools {
    Display d;
    String fSaveLocation = "";
    String fSelectedTool = "";
    String fImportRenamed = "";

    String fToolName = "";

    ImportCombine fImportCombine = new ImportCombine();
    static String fProjectName = "";
    static String fSelectedToolPath = "";
    static String fSaveDestination;
    static String fFileName;

    String fDuplicateToolNameChecker = "";
    String fNameChecker = "";

    // Location of the workspace the user is using
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();

    /**
     * Creates dialog window
     * 
     */
    public void run() {
        Shell shell = new Shell(d);
        shell.setText("Import Tool");
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
        shell.setLayout(new GridLayout(5, false));

        Label title = new Label(shell, SWT.NONE);
        title.setFont(new Font(title.getDisplay(), new FontData(title.getFont().toString(), 12, SWT.BOLD)));
        title.setText("Import Tool");
        GridData gridDataTitle = new GridData();
        gridDataTitle.horizontalAlignment = SWT.CENTER;
        gridDataTitle.horizontalSpan = 5;
        title.setLayoutData(gridDataTitle);

        Label lSelectTool = new Label(shell, SWT.NONE);
        lSelectTool.setText("Select tool to import:");
        GridData gridDataLabel = new GridData();
        gridDataLabel.horizontalAlignment = SWT.CENTER;
        gridDataLabel.horizontalSpan = 2;
        lSelectTool.setLayoutData(gridDataLabel);

        Text tSelectTool = new Text(shell, SWT.BORDER | SWT.SINGLE);
        tSelectTool.setText(fSelectedTool);
        GridData gridDataWidgets = new GridData();
        gridDataWidgets.horizontalAlignment = SWT.FILL;
        gridDataWidgets.grabExcessHorizontalSpace = true;
        gridDataWidgets.horizontalSpan = 2;
        tSelectTool.setLayoutData(gridDataWidgets);
        tSelectTool.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                Text textWidget = (Text) e.getSource();
                fSelectedTool = textWidget.getText();
            }
        });

        Button bSelectTool = new Button(shell, SWT.PUSH);
        bSelectTool.setText("Browse");
        GridData gridDataButtons = new GridData();
        gridDataButtons.horizontalAlignment = SWT.CENTER;
        gridDataButtons.verticalAlignment = SWT.FILL;
        gridDataButtons.horizontalSpan = 1;
        bSelectTool.setLayoutData(gridDataButtons);
        bSelectTool.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog saveDialog = new FileDialog(shell);
                saveDialog.setFilterPath(workspaceDirectory.getAbsolutePath());
                String[] filterExtensions = { "*.skill" };
                saveDialog.setFilterExtensions(filterExtensions);
                String fSelectedPath = saveDialog.open();
                if (fSelectedPath != null) {
                    tSelectTool.setText(fSelectedPath);
                }
            }
        });

        Label lImportRename = new Label(shell, SWT.WRAP | SWT.CENTER | SWT.NONE);
        lImportRename.setText("Rename imported file to:\n(If blank, the name of the exported file will be used)");
        GridData gridDataLabel1 = new GridData();
        gridDataLabel1.horizontalAlignment = SWT.CENTER;
        gridDataLabel1.horizontalSpan = 2;
        lImportRename.setLayoutData(gridDataLabel1);

        Text tImportRename = new Text(shell, SWT.BORDER | SWT.SINGLE);
        tImportRename.setText(fImportRenamed);
        GridData gridDataWidgetsBig = new GridData();
        gridDataWidgetsBig.horizontalAlignment = SWT.FILL;
        gridDataWidgetsBig.grabExcessHorizontalSpace = true;
        gridDataWidgetsBig.horizontalSpan = 3;
        tImportRename.setLayoutData(gridDataWidgetsBig);
        tImportRename.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                Text textWidget = (Text) e.getSource();
                fImportRenamed = textWidget.getText();
            }
        });

        Label lSaveLocation = new Label(shell, SWT.NONE);
        lSaveLocation.setText("Import to Project:");
        GridData gridDataLabel2 = new GridData();
        gridDataLabel2.horizontalAlignment = SWT.CENTER;
        gridDataLabel2.horizontalSpan = 2;
        lSaveLocation.setLayoutData(gridDataLabel2);

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
        bSaveLocation.setLayoutData(gridDataButtons);
        bSaveLocation.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog directoryDialog = new DirectoryDialog(shell);
                directoryDialog.setFilterPath(workspaceDirectory.getAbsolutePath());
                String fSaveLocation = directoryDialog.open();
                if (fSaveLocation != null) {
                    tSaveLocation.setText(fSaveLocation);
                }
            }
        });

        Label emptyCell1 = new Label(shell, SWT.NONE);
        emptyCell1.setText("");
        emptyCell1.setLayoutData(new GridData());

        // Creates OK Button.
        Button OK = new Button(shell, SWT.PUSH);

        OK.setLayoutData(gridDataButtons);
        OK.setText("OK");
        OK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {

                File fCheckSelectTool = new File(tSelectTool.getText());
                File fCheckImportLocation = new File(tSaveLocation.getText());

                int fWorkspaceLength = workspaceDirectory.getAbsolutePath().length();

                String fCheckIfProjectSelected = tSaveLocation.getText().substring(fWorkspaceLength + 1);
                if (fCheckIfProjectSelected.endsWith(File.separator)) {
                    fCheckIfProjectSelected = fCheckIfProjectSelected.substring(0,
                            fCheckIfProjectSelected.lastIndexOf(File.separator) - 1);
                }
                int count = fCheckIfProjectSelected.length() - fCheckIfProjectSelected.replace(File.separator, "").length();

                String fToolName = tSelectTool.getText().substring(tSelectTool.getText().lastIndexOf(File.separator) + 1);

                if (tImportRename.getText() == null || tImportRename.getText() == "" || tImportRename.getText().isEmpty()) {
                    fNameChecker = fToolName.substring(0, fToolName.length() - 6);
                } else {
                    if (!tImportRename.getText().endsWith(".skill")) {
                        fNameChecker = tImportRename.getText();
                    } else {
                        fNameChecker = tImportRename.getText().substring(0, tImportRename.getText().length() - 6);
                    }
                }

                if (tSelectTool.getText() == null || tSelectTool.getText() == "") {
                    ShowMessage("Path for tool to import missing!", "No File to Import");
                    return;
                } else if (tSaveLocation.getText() == null || tSaveLocation.getText() == "") {
                    ShowMessage("Import location missing!", "Missing Import Location");
                    return;
                } else if (!fCheckSelectTool.isFile() || !tSelectTool.getText().endsWith(".skill")) {
                    ShowMessage("Invalid tool file!", "Invalid File Type");
                    return;
                } else if (!fCheckImportLocation.isDirectory()) {
                    ShowMessage("Import location is not in the workspace or a project folder!", "Invalid Import Location");
                    return;
                } else if (count > 0 || fCheckIfProjectSelected.length() == 0) {
                    ShowMessage("Import location is not a project folder!", "Invalid Import location");
                    return;
                }

                fFileName = fNameChecker;
                fProjectName = fCheckIfProjectSelected;
                fSelectedToolPath = tSelectTool.getText();
                fSaveDestination = tSaveLocation.getText();
                fImportCombine.start();
                shell.dispose();
                fSaveLocation = "";
                fSelectedTool = "";
                fFileName = "";
                fImportRenamed = "";

            }

        });

        Label emptyCell2 = new Label(shell, SWT.NONE);
        emptyCell2.setText("                                           ");
        emptyCell2.setLayoutData(new GridData());

        // Cancel button. Closes dialog window.
        Button Cancel = new Button(shell, SWT.PUSH);
        Cancel.setLayoutData(gridDataButtons);
        Cancel.setText("Cancel");
        Cancel.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                shell.dispose();
                fSaveLocation = "";
                fSelectedTool = "";
            }

        });
    }

    public static String getProjectName() {
        return fProjectName;
    }

    public static String getSelectedToolPath() {
        return fSelectedToolPath;
    }

    public static String getSaveDestination() {
        return fSaveDestination;
    }

    public static String getFileName() {
        return fFileName;
    }

    public static boolean addAllToNewTool(String ProjectPath, IProject project) {
        SkillFile sk;
        try {
            sk = SkillFile.open(ProjectPath + File.separator + ".skills", Mode.ReadOnly);
        } catch (@SuppressWarnings("unused") Exception e) {
            return false;
        }
        ToolUtil.addAllToTool(sk, project, sk.Tools().stream().filter(t -> t.getName().equals(fFileName)).findFirst().get());
        return true;
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
}
