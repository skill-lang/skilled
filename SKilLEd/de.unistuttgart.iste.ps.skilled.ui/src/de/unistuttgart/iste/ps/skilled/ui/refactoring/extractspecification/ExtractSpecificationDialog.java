package de.unistuttgart.iste.ps.skilled.ui.refactoring.extractspecification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.Extension;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.ui.quickfix.SKilLQuickfixProvider;


/**
 * This class creates the Dialog window where the user can select the types <br>
 * to be extracted and the name and the save location of the new file.
 * 
 * @author Leslie Tso
 * @author Tobias Heck
 *
 */
public class ExtractSpecificationDialog {

    @Inject
    @Extension
    static ValidationTestHelper helper = new ValidationTestHelper();

    static List<TypeDeclaration> declarations;
    static TypeDeclaration[] checkedDeclarations = null;
    Display d;
    File saveLocationFile = null;
    String saveLocation = "";
    Text saveLocationText;
    String folderPath;
    String fileName;
    static IPath currentFilePath;
    static IXtextDocument xtextDocument = null;
    static boolean hasErrors = false;
    static IWorkbenchPage page = null;
    static de.unistuttgart.iste.ps.skilled.sKilL.File file = null;

    /**
     * Creates dialog window
     * 
     */
    public void run() {
        hasErrors = false;
        if (!getDeclarations() || hasErrors)
            return;
        Shell shell = new Shell(d);
        shell.setText("Extract User Types");
        shell.layout(true, true);
        createContents(shell);
        final Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        shell.setSize(newSize);
        shell.open();
    }

    private static boolean getDeclarations() {
        if (EditorUtils.getActiveXtextEditor() == null || EditorUtils.getActiveXtextEditor().getDocument() == null)
            return false;
        xtextDocument = EditorUtils.getActiveXtextEditor().getDocument();
        page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null || page.getActiveEditor() == null)
            return false;
        currentFilePath = ((FileEditorInput) page.getActiveEditor().getEditorInput()).getPath();

        xtextDocument.readOnly(new IUnitOfWork<Void, XtextResource>() {

            @Override
            public java.lang.Void exec(XtextResource state) throws Exception {
                // only allow extraction if file has no errors
                List<Issue> issues = helper.validate(state);
                for (Issue i : issues) {
                    if (i.getSeverity().toString().equals("ERROR")) {
                        JOptionPane.showMessageDialog(null, "The file must not contain errors!");
                        hasErrors = true;
                        return null;
                    }
                }

                file = (de.unistuttgart.iste.ps.skilled.sKilL.File) state.getContents().get(0);
                EList<Declaration> declarationList = file.getDeclarations();

                // only add user types and interfaces
                declarations = new LinkedList<TypeDeclaration>();
                for (Declaration d : declarationList) {
                    if (d instanceof TypeDeclaration) {
                        declarations.add((TypeDeclaration) d);
                    }
                }

                return null;
            }

        });
        return true;
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
        label1.setText("Select user types to extract:");
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
        tree.setContentProvider(new UsertypeTreeContentProvider());
        tree.setLabelProvider(new UsertypeTreeLabelProvider());

        // Empty tree if no default input
        tree.setInput("something");
        tree.expandAll();

        tree.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                Object[] checked = tree.getCheckedElements();
                checkedDeclarations = new TypeDeclaration[checked.length];
                for (int i = 0; i < checked.length; i++) {
                    checkedDeclarations[i] = (TypeDeclaration) checked[i];
                }
            }
        });

        // Empty line
        Label labele = new Label(shell, SWT.NONE);
        labele.setLayoutData(gridData);

        Label labelLocation = new Label(shell, SWT.NONE);
        labelLocation.setText("Save extracted specification as:");
        labelLocation.setLayoutData(new GridData());

        saveLocationText = new Text(shell, SWT.BORDER | SWT.SINGLE);
        saveLocationText.setText(saveLocation);
        GridData gridData4 = new GridData();
        gridData4.horizontalAlignment = SWT.FILL;
        gridData4.grabExcessHorizontalSpace = true;
        gridData4.horizontalSpan = 2;
        saveLocationText.setLayoutData(gridData4);
        saveLocationText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                Text textWidget = (Text) e.getSource();
                saveLocation = textWidget.getText();
            }
        });

        // Allows user to choose the save location.
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
                    saveLocation = savePath;
                    saveLocationText.setText(saveLocation);
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
        // correct and initiates the extraction.
        Button OK = new Button(shell, SWT.PUSH);
        GridData gridDataButtons = new GridData();
        gridDataButtons.horizontalAlignment = SWT.FILL;
        gridDataButtons.verticalAlignment = SWT.FILL;
        OK.setLayoutData(gridDataButtons);
        OK.setText("OK");
        OK.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                String location = saveLocationText.getText();
                if (location.contains(File.separator)) {
                    folderPath = location.substring(0, location.lastIndexOf(File.separator));
                    fileName = location.substring(location.lastIndexOf(File.separator) + 1);
                } else {
                    JOptionPane.showMessageDialog(null, "Save location and name missing!", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                File workspaceDirectory = workspace.getRoot().getLocation().toFile();

                File fExistL = new File(folderPath);
                File fExistC = new File(saveLocationText.getText());

                // Error message if no user types are selected
                if (checkedDeclarations.length == 0) {
                    JOptionPane.showMessageDialog(null, "No User Types are selected.", "Select at least one User Type",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Error message if combined file is not a .skill file.
                else if (!fileName.endsWith(".skill")) {
                    JOptionPane.showMessageDialog(null, "Save name missing.", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Error message if input field is empty
                else if (saveLocationText.getText() == null || saveLocationText.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Save name and location missing!", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Error message if save directory does not exist
                else if (!fExistL.exists() || !fExistL.isDirectory()) {
                    JOptionPane.showMessageDialog(null, "Selected location does not exist!", "Invalid Save Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Error if user tries to save outside the workspace
                else if (!folderPath.contains(workspaceDirectory.getAbsolutePath())) {
                    JOptionPane.showMessageDialog(null, "Save location is not in the workspace!", "Invalid Save Location",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Warning message if the combined file already exists. User has
                // the option to overwrite existing file.
                else if (fExistC.exists()) {
                    int overwrite = JOptionPane.showOptionDialog(null,
                            fileName + " already exists!" + " Do you want to overwrite it?", "Existing File",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (overwrite == JOptionPane.YES_OPTION) {
                        fExistC.delete();
                        executeExtraction(fExistC);

                    }
                } else {
                    executeExtraction(fExistC);
                }

            }

            private void executeExtraction(File file) {
                // remove declarations from old file
                page.saveAllEditors(false);
                // get file content
                try {
                    FileInputStream fis = new FileInputStream(currentFilePath.toFile());
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String line;
                    String fileContent = "";
                    List<String> headComments = new LinkedList<String>();
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("#")) headComments.add(line);
                        fileContent += line + System.lineSeparator();
                    }
                    br.close();
                    // delete types that were checked previously
                    String[] declarations = fileContent.split("}");
                    List<String> newDeclarations = new LinkedList<String>();
                    for (String s : declarations) {
                        for (TypeDeclaration decl : checkedDeclarations) {
                            if (s.toLowerCase().replace(" ", "").contains(decl.getName().toLowerCase() + "{") ||
                                    s.toLowerCase().replace(" ", "").contains(decl.getName().toLowerCase() + ":") ||
                                    s.toLowerCase().replace(" ", "").contains(decl.getName().toLowerCase() + "with") ||
                                    s.toLowerCase().replace(" ", "").contains(decl.getName().toLowerCase() +
                                    "extends")) {
                                s = "";
                            }
                            newDeclarations.add(s);
                        }
                    }
                    // overwrite old file
                    FileWriter fw = new FileWriter(currentFilePath.toFile(), false);
                    for (String s : headComments) {
                        fw.write(s);
                    }
                    for (String s : newDeclarations) {
                        fw.write(s);
                        if (s.contains("{"))
                            fw.write("}");
                    }
                    fw.close();
                    // refresh workspace
                    IWorkspaceRoot root  = ResourcesPlugin.getWorkspace().getRoot();
                    IPath basePath = root.getLocation();
                    String projectName = currentFilePath.segment(basePath.segmentCount());
                    root.getProject(projectName).refreshLocal(IResource.DEPTH_INFINITE,
                            new NullProgressMonitor());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CoreException e) {
                    e.printStackTrace();
                }
                // create new file
                ExtractSpecification.run(checkedDeclarations, file);
                // organize imports
                new SKilLQuickfixProvider().organizeImports(ExtractSpecificationDialog.file);
                // Reset save location
                saveLocation = "";
                saveLocationText.setText(saveLocation);
                shell.dispose();
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
                saveLocation = "";
                saveLocationText.setText(saveLocation);
                shell.dispose();
            }
        });
    }

    // Selects items to put in the tree
    class UsertypeTreeContentProvider implements ITreeContentProvider {

        // because there are only roots, no object has children
        @Override
        public Object[] getChildren(Object arg0) {
            return null;
        }

        // because there are only roots, no object has a parent
        @Override
        public Object getParent(Object arg0) {
            return null;
        }

        // because there are only roots, no object has children
        @Override
        public boolean hasChildren(Object arg0) {
            return false;
        }

        // get root elements of the tree
        @Override
        public Object[] getElements(Object arg0) {

            return ExtractSpecificationDialog.declarations.toArray();
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
    class UsertypeTreeLabelProvider implements ILabelProvider {

        private List<ILabelProviderListener> listeners;

        // Image for nodes
        private Image image;

        // @SuppressWarnings("unused")
        public UsertypeTreeLabelProvider() {
            // Create the list to hold the listeners
            listeners = new ArrayList<ILabelProviderListener>();
        }

        // image
        @Override
        public Image getImage(Object arg0) {
            return image;
        }

        @Override
        public String getText(Object arg0) {
            // Get the name of the user type
            String text = ((TypeDeclaration) arg0).getName();

            return text;
        }

        @Override
        public void addListener(ILabelProviderListener arg0) {
            listeners.add(arg0);
        }

        // Dispose images
        @Override
        public void dispose() {
            if (image != null)
                image.dispose();
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
}
