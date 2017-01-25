package de.unistuttgart.iste.ps.skilled.ui.tools.imprt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

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

/**
 * This class provides the dialog window for "Import Binary File" which allows
 * the user to import a binary file as a new tool.
 * 
 * @author Leslie
 * @author Tobias
 */
public class ImportBinary {
    Display d;
    String fSaveLocation = "";
    String fSelectedBinary = "";
    String fBinaryName;
    String fToolFilePath;
    String fTempFileForCombining;
    String fImports = "";
    String fBody;
    String fImportRenamed = "";
    String fStartDirectoryForChecker = "";

    ArrayList<File> fListofFiles = new ArrayList<File>();

    // Location of the workspace the user is using
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();

    /**
     * Creates dialog window
     * 
     */
    public void run() {
        Shell shell = new Shell(d);
        shell.setText("Import Binary File");
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
        title.setText("Import Binary File");
        GridData gridDataTitle = new GridData();
        gridDataTitle.horizontalAlignment = SWT.CENTER;
        gridDataTitle.horizontalSpan = 5;
        title.setLayoutData(gridDataTitle);

        Label lSelectBinary = new Label(shell, SWT.NONE);
        lSelectBinary.setText("Select binary file to import:");
        GridData gridDataLabel = new GridData();
        gridDataLabel.horizontalAlignment = SWT.CENTER;
        gridDataLabel.horizontalSpan = 2;
        lSelectBinary.setLayoutData(gridDataLabel);

        Text tSelectBinary = new Text(shell, SWT.BORDER | SWT.SINGLE);
        tSelectBinary.setText(fSelectedBinary);
        GridData gridDataWidgets = new GridData();
        gridDataWidgets.horizontalAlignment = SWT.FILL;
        gridDataWidgets.grabExcessHorizontalSpace = true;
        gridDataWidgets.horizontalSpan = 2;
        tSelectBinary.setLayoutData(gridDataWidgets);
        tSelectBinary.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                Text textWidget = (Text) e.getSource();
                fSelectedBinary = textWidget.getText();
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
                String[] filterExtensions = { "*.sf" };
                saveDialog.setFilterExtensions(filterExtensions);
                String fSelectedPath = saveDialog.open();
                if (fSelectedPath != null) {
                    tSelectBinary.setText(fSelectedPath);
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
        lSaveLocation.setText("Import to:");
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
//        OK.addSelectionListener(new SelectionAdapter() {
//            @SuppressWarnings({ "restriction", "unchecked" })
//            @Override
//            public void widgetSelected(SelectionEvent event) {
//
//                String fCheckEmptyImportRename = fImportRenamed.replace("/s+", "");
//                // Use binary file name as imported file name if rename field is
//                // empty
//                if (fCheckEmptyImportRename.length() == 0) {
//                    fBinaryName = fSelectedBinary.substring(fSelectedBinary.lastIndexOf(File.separator) + 1,
//                            fSelectedBinary.indexOf(".sf"));
//                } else {
//                    fBinaryName = tImportRename.getText();
//                }
//
//                try {
//                    SkillFile skillFile = null;
//                    if (null == skillFile)
//                        throw new NoSuchMethodError("not implemented");
//                    Iterator<? extends Access<? extends SkillObject>> it = skillFile.allTypes().iterator();
//                    LinkedList<String> strings = new LinkedList<String>();
//                    while (it.hasNext()) {
//                        Access<? extends SkillObject> type = it.next();
//                        strings.add(type.name()
//                                + (!Strings.isNullOrEmpty(type.superName()) ? (" : " + type.superName()) : "") + " {");
//                        Iterator<? extends FieldDeclaration<?>> it2 = type.fields();
//                        while (it2.hasNext()) {
//                            FieldDeclaration<?> declaration = it2.next();
//                            LazyField<?, ?> lazyField = (LazyField<?, ?>) declaration;
//                            strings.add("  " + lazyField.toString() + ";");
//                        }
//                        strings.add("}");
//                        strings.add("");
//                    }
//                    FileWriter writer = new FileWriter("TemporaryFileBySKilLEd.skill", true);
//                    for (String s : strings) {
//                        writer.write(s + System.lineSeparator());
//                    }
//                    writer.close();
//                    fSelectedBinary = "TemporaryFileBySKilLEd.skill";
//
//                } catch (SkillException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                File fCheckImportLocation = new File(tSaveLocation.getText());
//                fToolFilePath = tSaveLocation.getText() + File.separator + fBinaryName + ".skill";
//                fTempFileForCombining = tSaveLocation.getText() + File.separator + "TempFileBySKilLEd.skill";
//                File fCheckDuplicate = new File(fToolFilePath);
//                String fRename = fSaveLocation + File.separator + fBinaryName + "_RenamedBySKilLEd.skill";
//                File fRenamedFile = new File(fRename);
//                fStartDirectoryForChecker = tSaveLocation.getText();
//                File makeDirectory = new File(fStartDirectoryForChecker);
//                if (!makeDirectory.exists()) {
//                    makeDirectory.mkdirs();
//                }
//
//                // error if save location is not a directory
//                if (!fCheckImportLocation.isDirectory()) {
//                    JOptionPane.showMessageDialog(null, "Import location is not a folder!", "Invalid Import Location",
//                            JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                // error if selected file to import does not end with .sf
//                else if (!tSelectBinary.getText().endsWith(".sf")) {
//                    JOptionPane.showMessageDialog(null, "Invalid file type!", "Invalid File Type",
//                            JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                // Check if import location is in a .tool folder in the
//                // workspace
//                else if (!tSaveLocation.getText().contains(".tools")
//                        && !tSaveLocation.getText().contains(workspaceDirectory.getAbsolutePath())) {
//                    JOptionPane.showMessageDialog(null,
//                            "Import location is not in the workspace or a child of the .tools folder!",
//                            "Invalid Import Location", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                // Check if file to import already exists
//                else if (fCheckDuplicate.exists()) {
//                    int overwrite = JOptionPane.showOptionDialog(null,
//                            fBinaryName + ".skill already exists!"
//                                    + " Do you want to merge them? (Clicking \"No\" will overwrite the new file with the existing file.)",
//                            "Existing File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
//                            null);
//                    // Deletes existing duplicate tool file if the user clicks
//                    // "no"
//                    if (overwrite == JOptionPane.NO_OPTION) {
//                        fCheckDuplicate.delete();
//                    }
//                    // Renames existing duplicate tool file so that it can be
//                    // merged with the imported file if the user clicks "yes"
//                    else if (overwrite == JOptionPane.YES_OPTION) {
//                        fCheckDuplicate.renameTo(fRenamedFile);
//                    }
//
//                }
//                // Create tool file
//                else {
//                    // Creates a file with the name of the tool if \.tools
//                    // folder is selected
//                    if (fSaveLocation.endsWith(".tools")) {
//                        fToolFilePath = fSaveLocation + File.separator + fBinaryName + File.separator + fBinaryName
//                                + ".skill";
//                        fTempFileForCombining = tSaveLocation.getText() + File.separator + fBinaryName + File.separator
//                                + "TempFileBySKilLEd.skill";
//                        fStartDirectoryForChecker = fSaveLocation + File.separator + fBinaryName;
//                        File makeDirectory2 = new File(fStartDirectoryForChecker);
//                        if (!makeDirectory2.exists()) {
//                            makeDirectory2.mkdirs();
//                        }
//                    } else if (fSaveLocation.endsWith(".tools" + File.separator)) {
//                        fToolFilePath = fSaveLocation + fBinaryName + File.separator + fBinaryName + ".skill";
//                        fTempFileForCombining = tSaveLocation.getText() + fBinaryName + File.separator
//                                + "TempFileBySKilLEd.skill";
//                        fStartDirectoryForChecker = fSaveLocation + fBinaryName;
//                        File makeDirectory2 = new File(fStartDirectoryForChecker);
//                        if (!makeDirectory2.exists()) {
//                            makeDirectory2.mkdirs();
//                        }
//                    }
//                }
//                // Converts string filepaths to paths
//                tSelectBinary.setText(new File("TemporaryFileBySKilLEd.skill").getAbsolutePath());
//                Path fSelectedToolPath = Paths.get(tSelectBinary.getText());
//                Path fImportLocationPath = Paths.get(fToolFilePath);
//                // Moves to-be-imported tool from original location to its
//                // import location
//                try {
//                    Files.move(fSelectedToolPath, fImportLocationPath, REPLACE_EXISTING);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    checkForDuplicateTypes(fToolFilePath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                combineFiles();
//                fSaveLocation = "";
//                fSelectedBinary = "";
//
//                /*
//                 * IWorkspace workspace = ResourcesPlugin.getWorkspace();
//                 * IWorkspaceRoot root = workspace.getRoot(); IProject project =
//                 * root.getProject("NewProject"); IFolder folder =
//                 * project.getFolder("NewFolder"); IFile file =
//                 * folder.getFile("hell.MyDsl"); if (!project.exists()) try {
//                 * project.create(null); } catch (CoreException e1) {
//                 * e1.printStackTrace(); } if (!project.isOpen()) try {
//                 * project.open(null); } catch (CoreException e1) {
//                 * e1.printStackTrace(); } if (!folder.exists()) try {
//                 * folder.create(IResource.NONE, true, null); } catch
//                 * (CoreException e1) { e1.printStackTrace(); } byte[] bytes =
//                 * "File contents".getBytes(); ByteArrayInputStream source = new
//                 * ByteArrayInputStream(bytes); try { file.create(source,
//                 * IResource.NONE, null);
//                 * 
//                 * } catch (CoreException e) { e.printStackTrace(); } IWorkbench
//                 * wb = PlatformUI.getWorkbench(); IWorkbenchWindow win =
//                 * wb.getActiveWorkbenchWindow(); IWorkbenchPage page =
//                 * win.getActivePage(); try { IDE.openEditor(page, file); }
//                 * catch (PartInitException e) { e.printStackTrace(); }
//                 */
//
//                // Get the active page.
//                IWorkbench wb = PlatformUI.getWorkbench();
//                IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
//                IWorkbenchPage page = win.getActivePage();
//                // Figure out the default editor for the file type based on
//                // extension.
//                IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor("a.skill");
//                if (desc == null) {
//                    // MyHLMUtils.popupError("Editor open error", "Unable to
//                    // find a suitable editor to open file.");
//                } else {
//                    Constructor<org.eclipse.core.internal.resources.File> constructor = (Constructor<org.eclipse.core.internal.resources.File>) org.eclipse.core.internal.resources.File.class
//                            .getDeclaredConstructors()[0];
//                    constructor.setAccessible(true);
//                    try {
//                        IWorkspaceRoot root = workspace.getRoot();
//                        IPath path = root.getLocation();
//                        String shortPath = fImportLocationPath.toString().split(path.lastSegment())[1];
//                        org.eclipse.core.internal.resources.File file = constructor.newInstance(
//                                new org.eclipse.core.runtime.Path(shortPath), ResourcesPlugin.getWorkspace());
//                        File rename = new File(fImportLocationPath.toString());
//                        File deleteLater = new File(fImportLocationPath.toString() + "asdf");
//                        rename.renameTo(deleteLater);
//                        IProject currentProject = null;
//                        for (IProject p : root.getProjects()) {
//                            if (fImportLocationPath.toString().contains(p.getName())) {
//                                currentProject = p;
//                            }
//                        }
//                        if (currentProject == null) {
//                            return;
//                        }
//                        currentProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
//                        file.create(new FileInputStream(fImportLocationPath.toString() + "asdf"), true,
//                                new NullProgressMonitor());
//                        deleteLater.delete();
//                        currentProject.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
//                        page.openEditor(new FileEditorInput(file), desc.getId());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                shell.dispose();
//
//            }
//        });

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
                fSelectedBinary = "";
            }

        });
    }

    public void checkForDuplicateTypes(String importLocation) throws IOException {
        Set<String> duplicateType = new HashSet<>();

        listFiles("", fListofFiles);

        if (fListofFiles != null) {
            for (File f : fListofFiles) {
                FileInputStream fis;
                try {

                    fis = new FileInputStream(f);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.contains("{")) {
                            if (!duplicateType.contains(line)) {
                                duplicateType.add(line);
                            }
                        }
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileInputStream fis = new FileInputStream(importLocation);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                if (counter < 2) {
                    if (duplicateType.contains(line)) {
                        counter += 1;

                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "At least 2 types in " + fBinaryName + ".skill already exist in Tool " + fBinaryName + "!",
                            "Duplicate Types", JOptionPane.ERROR_MESSAGE);
                    br.close();
                    return;
                }
            }
            br.close();
        }
    }

    /**
     * Finds all .skill files in the project folder and its subdirectories
     * 
     * @param directoryName
     * @param checkFiles
     */
    public static void listFiles(String directoryName, ArrayList<File> checkFiles) {
        String location = "";
        if (directoryName == "") {
            location = new File("TemporaryFileBySKilLEd.skill").getAbsolutePath()
                    .split("TemporaryFileBySKilLEd.skill")[0];
        } else {
            location = directoryName;
        }
        File fProjectDirectory = new File(location);
        File[] listofFiles = fProjectDirectory.listFiles();
        for (File file : listofFiles) {
            if (file.isFile() && file.getName().endsWith(".sf")) {
                checkFiles.add(file);
            } else if (file.isDirectory()) {
                // listFiles(file.getAbsolutePath(), checkFiles);
            }
        }
    }

    /**
     * Combines all files found by method listFiles
     * 
     */
    public void combineFiles() {

        listFiles("", fListofFiles);

        if (fListofFiles != null) {
            for (File f : fListofFiles) {
                FileInputStream fis;
                try {
                    FileWriter fw = new FileWriter(fTempFileForCombining, true);
                    fis = new FileInputStream(f);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String line;

                    while ((line = br.readLine()) != null) {
                        // Ignore all other head comments
                        if (!line.startsWith("#")) {
                            if (line.startsWith("include") || line.startsWith("with")) {
                                fImports += line + "\n";
                            } else {
                                fBody += line + "\n";
                            }
                        }
                    }
                    br.close();
                    // Head comment that says which tool the merged files are
                    fw.write("# Tool " + fBinaryName);
                    fw.write(fImports);
                    fw.write(fBody);
                    fw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
