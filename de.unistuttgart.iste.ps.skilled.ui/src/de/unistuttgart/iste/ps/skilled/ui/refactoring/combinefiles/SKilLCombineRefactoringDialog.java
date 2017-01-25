package de.unistuttgart.iste.ps.skilled.ui.refactoring.combinefiles;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * This class creates the Dialog window where the user can select the .skill files to be combined and the name and the save
 * location of the combined document.
 * 
 * @author Leslie Tso
 *
 */
public class SKilLCombineRefactoringDialog {

    Display d;
    String saveText = "";

    int numberSelected;
    String[] fCombines;
    String fCombinedName;
    String fCombinedLocation;
    Text fCombinedSave;
    SKilLCombineRefactoring fRefactoring = new SKilLCombineRefactoring();

    File fExistL;
    File fExistC;

    /**
     * Creates dialog window
     * 
     */
    public void run() {
        Shell shell = new Shell(d);
        shell.setText("Combine .skill-Files");
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

        Label label1 = new Label(shell, SWT.NONE);
        label1.setText("Select files to combine:");
        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.CENTER;
        gridData.horizontalSpan = 4;
        label1.setLayoutData(gridData);

        // Create the tree viewer to display the file tree
        final CheckboxTreeViewer tree = new CheckboxTreeViewer(shell);
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = SWT.FILL;
        gridData2.verticalAlignment = SWT.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.horizontalSpan = 4;
        tree.getTree().setLayoutData(gridData2);
        tree.setContentProvider(new FileTreeContentProvider());
        tree.setLabelProvider(new FileTreeLabelProvider());

        // Empty tree if no default input
        tree.setInput("something");
        tree.expandAll();

        tree.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                if (event.getChecked() == true) {
                    // If folder is checked, check all children
                    tree.setSubtreeChecked(event.getElement(), true);
                } else {
                    // If folder is unchecked, uncheck all children
                    tree.setSubtreeChecked(event.getElement(), false);
                }
                Object[] treeItems = tree.getCheckedElements();

                numberSelected = treeItems.length;
                for (int j = 0; j < numberSelected; j++) {
                    // Removes directories as a tree element to be combined (if
                    // one checks a folder instead of a file)
                    if (!treeItems[j].toString().endsWith(".skill")) {
                        treeItems[j] = "a";
                        numberSelected = numberSelected - 1;
                    }
                }
                List<Object> removeFolders = new ArrayList<>();
                Collections.addAll(removeFolders, treeItems);
                removeFolders.removeAll(Arrays.asList("a"));
                // Creates new array with the directories taken out
                Object[] treeItemswithoutFolders = new Object[numberSelected];
                treeItemswithoutFolders = removeFolders.toArray(treeItems);

                fCombines = new String[numberSelected];
                // Get selected items
                for (int i = 0; i < numberSelected; i++) {
                    fCombines[i] = treeItemswithoutFolders[i].toString();
                }
            }
        });

        // Empty line
        Label labele = new Label(shell, SWT.NONE);
        labele.setLayoutData(gridData);

        Label labelLocation = new Label(shell, SWT.NONE);
        labelLocation.setText("Save combined file as:");
        labelLocation.setLayoutData(new GridData());

        fCombinedSave = new Text(shell, SWT.BORDER | SWT.SINGLE);
        fCombinedSave.setText(saveText);
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = SWT.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.horizontalSpan = 2;
        fCombinedSave.setLayoutData(gridData4);
        fCombinedSave.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                Text textWidget = (Text) e.getSource();
                saveText = textWidget.getText();
            }
        });

        // Allows user to choose the save location and name the combined file.
        Button save = new Button(shell, SWT.PUSH);
        save.setText("Browse");
        save.setLayoutData(new GridData(SWT.CENTER));
        save.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
                // Set initial path to workspace directory
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                File workspaceDirectory = workspace.getRoot().getLocation().toFile();
                saveDialog.setFilterPath(workspaceDirectory.getAbsolutePath());
                String[] filterExtensions = { "*.skill" };
                saveDialog.setFilterExtensions(filterExtensions);
                String savePath = saveDialog.open();
                if (savePath != null) {
                    saveText = savePath;
                    fCombinedSave.setText(saveText);
                }
            }
        });

        // Empty line
        Label labelel1 = new Label(shell, SWT.NONE);
        labelel1.setLayoutData(gridData);

        // Empty cell
        Label labele8 = new Label(shell, SWT.NONE);
        labele8.setLayoutData(new GridData());

        // Creates OK Button. When clicked, it checks whether the inputs are
        // correct and if yes, then sends the data to be
        // combined.
        Button OK = new Button(shell, SWT.PUSH);
        GridData gridDataButtons = new GridData();
        gridDataButtons.horizontalAlignment = SWT.FILL;
        gridDataButtons.verticalAlignment = SWT.FILL;
        OK.setLayoutData(gridDataButtons);
        OK.setText("OK");
        OK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                String location = fCombinedSave.getText();
                if (location.contains(File.separator)) {
                    fCombinedLocation = location.substring(0, location.lastIndexOf(File.separator));
                    fCombinedName = location.substring(location.lastIndexOf(File.separator) + 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Save location and name missing!", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                File workspaceDirectory = workspace.getRoot().getLocation().toFile();

                fExistL = new File(fCombinedLocation);
                fExistC = new File(fCombinedSave.getText());
                for (int i = 0; i < numberSelected; i++) {
                    // Checks if the combined file is one of the pre-combined
                    // files
                    if (fCombinedSave.getText().equals(fCombines[i])) {
                        JOptionPane.showMessageDialog(null, "Cannot set one of the pre-combined files as the combined file.",
                                "Overwrite Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Error message if less than 2 files are selected
                if (numberSelected < 2) {
                    JOptionPane.showMessageDialog(null, "Less than 2 files are selected.", "Select >2 files",
                            JOptionPane.ERROR_MESSAGE);
                }
                // Error message if combined file is not a .skill file.
                else if (!fCombinedName.endsWith(".skill")) {
                    JOptionPane.showMessageDialog(null, "Save name missing.", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                }
                // Error message if input field is empty
                else if (fCombinedSave.getText() == null || fCombinedSave.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Save name and location missing!", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                }
                // Error message if save directory does not exist
                else if (!fExistL.exists() || !fExistL.isDirectory()) {
                    JOptionPane.showMessageDialog(null, "Location of combined file does not exist!", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                }
                // Error if user tries to save outside the workspace
                else if (!fCombinedLocation.contains(workspaceDirectory.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(null, "Save location is not in the workspace!", "Invalid Save Location",
                            JOptionPane.ERROR_MESSAGE);
                }
                // Warning message if the combined file already exists. User has
                // the option to overwrite existing file.
                else if (fExistC.exists()) {
                    int overwrite = JOptionPane.showOptionDialog(null,
                            fCombinedName + " already exists!" + " Do you want to overwrite it?", "Existing File",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (overwrite == JOptionPane.YES_OPTION) {
                        fExistC.delete();
                        initializeRefactoring();
                        fRefactoring.startCombine();
                        // Reset save location
                        saveText = "";
                        fCombinedSave.setText(saveText);
                        shell.dispose();
                    }
                } else {
                    initializeRefactoring();
                    fRefactoring.startCombine();
                    // Reset save location
                    saveText = "";
                    fCombinedSave.setText(saveText);
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
                // Reset save location
                saveText = "";
                fCombinedSave.setText(saveText);
                shell.dispose();
            }
        });
    }

    // Selects items to put in the tree
    class FileTreeContentProvider implements ITreeContentProvider {

        // Gets all children with the file extension ".skill" or directories
        // that does not begin with "."
        @Override
        public Object[] getChildren(Object arg0) {
            return ((File) arg0).listFiles(new FileFilter() {

                @Override
                public boolean accept(File arg0) {
                    return (arg0.getName().endsWith(".skill") || arg0.isDirectory() && !arg0.getName().startsWith("."));
                }
            });
        }

        // Gets the parent of the children
        @Override
        public Object getParent(Object arg0) {
            return ((File) arg0).getParentFile();
        }

        // Checks if object has children
        @Override
        public boolean hasChildren(Object arg0) {
            Object[] obj = getChildren(arg0);
            return obj == null ? false : obj.length > 0;
        }

        // get root elements of the tree
        @Override
        public Object[] getElements(Object arg0) {

            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            File workspaceDirectory = workspace.getRoot().getLocation().toFile();
            return workspaceDirectory.listFiles(new FilenameFilter() {
                // Ignores all folders that start with "."
                @Override
                public boolean accept(File workspaceDirectory, String name) {
                    return !name.startsWith(".");
                }
            });
        }

        @Override
        public void dispose() {
            // Do nothing
        }

        @Override
        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
            // Do nothing
        }
    }

    // Provides labels for the tree
    class FileTreeLabelProvider implements ILabelProvider {

        private List<ILabelProviderListener> listeners;

        // Images for tree nodes
        private Image file;
        private Image dir;

        // @SuppressWarnings("unused")
        public FileTreeLabelProvider() {
            // Create the list to hold the listeners
            listeners = new ArrayList<ILabelProviderListener>();
        }

        // If node is a directory, return directory image; else return file
        // image
        @Override
        public Image getImage(Object arg0) {
            return ((File) arg0).isDirectory() ? dir : file;
        }

        @Override
        public String getText(Object arg0) {
            // Get the name of the file
            String text = ((File) arg0).getName();

            // If name is blank, get the path
            if (text.length() == 0) {
                text = ((File) arg0).getPath();
            }
            return text;
        }

        @Override
        public void addListener(ILabelProviderListener arg0) {
            listeners.add(arg0);
        }

        // Dispose images
        @Override
        public void dispose() {
            if (dir != null)
                dir.dispose();
            if (file != null)
                file.dispose();
        }

        @Override
        public boolean isLabelProperty(Object arg0, String arg1) {
            return false;
        }

        @Override
        public void removeListener(ILabelProviderListener arg0) {
            listeners.remove(arg0);
        }
    }

    // Sends information from the input fields to SKilLCombineRefactoring to
    // merge
    private void initializeRefactoring() {
        fRefactoring.setNumberSelected(numberSelected);
        fRefactoring.setCombine(fCombines);
        fRefactoring.setCombinedSave(fCombinedSave.getText());
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        File workspaceDirectory = workspace.getRoot().getLocation().toFile();
        fRefactoring.setWorkSpaceDirectory(workspaceDirectory);
    }
}
