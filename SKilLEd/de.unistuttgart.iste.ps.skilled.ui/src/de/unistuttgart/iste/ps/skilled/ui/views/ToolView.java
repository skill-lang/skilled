package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.export.SaveListofAllTools;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillFile.Mode;


/**
 * Class to create the SKilL-Tool-Overview.
 * 
 * @author Ken Singer
 * @author Nico Russam
 * @author Marco Link
 * @author Armin Hüneburg
 * @author Leslie Tso
 */
public class ToolView extends ViewPart {

    private Action createToolAction;
    private FileChangeAction fileChangeAction = new FileChangeAction(this);

    private String path = "";
    private CTabFolder tabFolder;

    private ArrayList<Tool> allToolList = new ArrayList<Tool>();
    private ArrayList<Type> allTypeList = new ArrayList<Type>();
    private ArrayList<Type> typeListOfActualTool = new ArrayList<Type>();
    private ArrayList<Hint> typeHintListOfActualTool = new ArrayList<Hint>();

    private ArrayList<String> pathList = new ArrayList<String>();

    private CTabItem toolTabItem = null;
    private CTabItem typeTabItem = null;
    private CTabItem fieldTabItem = null;
    private SkillFile skillFile = null;
    private Tool activeTool = null;
    private IProject activeProject = null;
    private Type selectedType = null;
    private Field selectedField = null;

    private Composite parent = null;
    private Shell shell;

    SaveListofAllTools fSave;

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        tabFolder = new CTabFolder(parent, SWT.BORDER);
        toolTabItem = new CTabItem(tabFolder, 0, 0);
        typeTabItem = new CTabItem(tabFolder, 0, 1);
        fieldTabItem = new CTabItem(tabFolder, 0, 2);

        tabFolder.setVisible(true);
        fileChangeAction.save();
        fileChangeAction.saveAll();
        fileChangeAction.imp0rt();
        shell = parent.getShell();

        buildToolContextMenu(buildToollist());
        setFocus();
        makeActions();
        contributeToActionBars();

        ToolViewListener tvl = new ToolViewListener(this);
        tvl.initPartListener(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());
    }

    @Override
    public void setFocus() {
        tabFolder.setFocus();
    }

    void refresh() {
        if (!parent.isDisposed()) {
            clearLists();
            toolTabItem.dispose();
            typeTabItem.dispose();
            fieldTabItem.dispose();
            buildToolContextMenu(buildToollist());
            tabFolder.setSelection(toolTabItem);
        }
    }

    /**
     * reload the typelist after adding a type or hint to a tool
     */
    void reloadTypelist() {
        refresh();
        for (Tool t : skillFile.Tools()) {
            if (t.getName().equals(activeTool.getName()))
                activeTool = t;
        }
        buildTypeTree();
        tabFolder.setSelection(typeTabItem);
    }

    /**
     * reload the fieldlist after adding a field or hint to a tooltype
     * 
     */
    void reloadFieldList() {
        reloadTypelist();
        buildFieldTree();
        tabFolder.setSelection(fieldTabItem);
    }

    private void clearLists() {
        allToolList.clear();
        pathList.clear();
        allTypeList.clear();
        typeListOfActualTool = null;
        typeHintListOfActualTool = null;
    }

    /**
     * Read the binary file of all tools.
     */
    private void readToolBinaryFile() {
        try {
            IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor().getEditorInput();
            activeProject = file.getFile().getProject();
            path = activeProject.getLocation().toOSString() + File.separator + ".skills";
            skillFile = SkillFile.open(path, Mode.ReadOnly);
        } catch (Exception e) {
            System.err.println("Did not read .skills-File");
            return;
        }

        if (skillFile.Tools() != null) {
            skillFile.Tools().forEach(tool -> allToolList.add(tool));
            skillFile.Tools().forEach(t -> pathList.add(path));
        }

        if (skillFile.Types() != null)
            for (Type type : skillFile.Types()) {
                if (allToolList.stream().anyMatch(tool -> tool.getTypes().contains(type)))
                    break;
                allTypeList.add(type);
            }

        // fExport.setPaths(pathList);
    }

    /**
     * Build up the tooltab, listing all tools.
     * 
     * @param parent
     *            - {@link Composite}
     * @return {@link List}
     */
    private List buildToollist() {
        readToolBinaryFile();

        if (tabFolder.isDisposed())
            tabFolder = new CTabFolder(parent, SWT.BORDER);

        List toolViewList = new List(tabFolder, SWT.SINGLE | SWT.V_SCROLL);

        if (toolTabItem.isDisposed())
            toolTabItem = new CTabItem(tabFolder, 0, 0);

        try {
            toolTabItem.setText("Tools - " + activeProject.getName());
        } catch (NullPointerException e) {
            toolTabItem.setText("Tools");
        }

        toolTabItem.setControl(toolViewList);

        if (null != skillFile)
            allToolList.forEach(t -> toolViewList.add(t.getName()));

        ToolViewListener tvl = new ToolViewListener(this);
        tvl.initToolListListener(toolViewList);

        fSave = new SaveListofAllTools();
        fSave.setListofAllTools(allToolList);
        fSave.setPathofAllTools(pathList);

        return toolViewList;

    }

    /**
     * Build up the typetree, listing all types with their specific hints.
     * 
     * @param activeTool
     *            - {@link Tool}
     */
    void buildTypeTree() {
        Tree typeTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.SINGLE);
        typeListOfActualTool = activeTool.getTypes();
        TreeItem typeHintItem;

        if (typeTabItem.isDisposed())
            typeTabItem = new CTabItem(tabFolder, 0, 1);

        typeTabItem.setText("Types - " + activeTool.getName());

        if (null != skillFile) {
            // add all types to the tree
            for (Type type : allTypeList) {
                TreeItem typeTreeItem = new TreeItem(typeTree, 0);
                typeTreeItem.setText(type.getName().replaceAll(" %s ", " "));
                typeTreeItem.setData(type);
                Type tooltype = null;

                // set all the toolspecific types as checked
                if (null != typeListOfActualTool) {
                    for (Type t : typeListOfActualTool) {
                        if (t.getName().equals(type.getName())) {
                            typeTreeItem.setChecked(true);
                            tooltype = type;
                        }
                    }
                }

                // add all typeHints to the Tree
                for (Hint hint : type.getTypeHints()) {
                    typeHintItem = new TreeItem(typeTreeItem, 0);
                    typeHintItem.setText(hint.getName());
                    typeHintItem.setChecked(false);
                    typeHintItem.setExpanded(true);
                    typeHintItem.setData(hint);

                    // set all toolspecific typeHints as checked
                    if (null != tooltype) {
                        typeHintListOfActualTool = tooltype.getTypeHints();
                        if (typeHintListOfActualTool != null) {
                            for (Hint toolhint : typeHintListOfActualTool) {
                                if (hint.getName().equals(toolhint.getName())) {
                                    typeHintItem.setChecked(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        typeTabItem.setControl(typeTree);
        TypeTreeListener tvl = new TypeTreeListener(this);
        tvl.initTypeTreeListener(typeTree);
    }

    /**
     * Build the fieldtree, listing all fields with their specific hints.
     * 
     * @param tooltype
     *            - {@link Type}
     */
    void buildFieldTree() {
        Tree fieldTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        Type tooltype;

        if (fieldTabItem.isDisposed())
            fieldTabItem = new CTabItem(tabFolder, 0, 2);

        try {
            tooltype = typeListOfActualTool.stream().filter(t -> selectedType.getName().equals(t.getName())).findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            tooltype = null;
        }

        fieldTabItem.setText("Fields - " + selectedType.getName());

        if (null != skillFile && selectedType != null) {
            // add all fields to the tree
            for (Field field : selectedType.getFields()) {
                TreeItem fieldTreeItem = new TreeItem(fieldTree, 0);
                fieldTreeItem.setText(field.getName());
                fieldTreeItem.setChecked(false);
                fieldTreeItem.setData(field);
                Field toolField = null;

                // check all the fields used by the actual tool
                if (null != tooltype) {
                    for (Field f : tooltype.getFields()) {
                        if (field.getName().equals(f.getName())) {
                            fieldTreeItem.setChecked(true);
                            toolField = f;
                            break;
                        }
                    }
                }

                // add all the fieldhints to the tree
                for (Hint hint : field.getFieldHints()) {
                    TreeItem fieldHintItem = new TreeItem(fieldTreeItem, 0);
                    fieldHintItem.setText(hint.getName());
                    fieldHintItem.setChecked(false);
                    fieldTreeItem.setExpanded(true);
                    fieldHintItem.setData(hint);

                    // check all the hints used by the tool
                    if (null != toolField) {
                        for (Hint h : toolField.getFieldHints()) {
                            if (hint.getName().equals(h.getName())) {
                                fieldHintItem.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            }
        }

        fieldTabItem.setControl(fieldTree);
        FieldTreeListener ftl = new FieldTreeListener(this);
        ftl.initFieldTreeListener(fieldTree, tooltype);
    }

    private Menu menu;

    /**
     * Creates the contextmenu for the toolview.
     * 
     * @param toollist
     *            - {@link List}
     */
    private void buildToolContextMenu(List toollist) {
        if (null != menu)
            menu.dispose();
        menu = new Menu(toollist);
        ContextMenuToolView cmtv = new ContextMenuToolView(this);
        cmtv.initContextMenu(toollist, menu);
    }

    /**
     * recursivly deletes a directory
     * 
     * @param directoryToDelete
     *            - the directory to delete
     * @throws IOException
     */
    static void deleteDirectoryRecursivly(File directoryToDelete) {
        if (directoryToDelete.isDirectory()) {
            try {
                for (File toDelete : directoryToDelete.listFiles())
                    Files.deleteIfExists(toDelete.toPath());
                Files.deleteIfExists(directoryToDelete.toPath());
            } catch (IOException e) {
                // TODO: handling
            }
        }
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(createToolAction);
    }

    private void makeActions() {
        createToolAction = new Action() {
            @Override
            public void run() {
                createToolDialog();
            }
        };
        createToolAction.setText("Create Tool");
        createToolAction.setToolTipText("Create Tool tooltip");
    }

    private void createToolDialog() {
        final SKilLToolWizard sKilLToolWizard = new SKilLToolWizard();
        WizardDialog wizardDialog = new WizardDialog(shell, sKilLToolWizard);
        if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
            String newToolName = sKilLToolWizard.getToolNewName();
            if (newToolName != null) {

                if (!ToolUtil.createTool(newToolName, activeProject)) {
                    showMessage("Could not create tool.");
                }
                refresh();
            }
        }
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(shell, "Tool View", message);
    }

    Field getSelectedField() {
        return selectedField;
    }

    void setSelectedField(Field selectedField) {
        this.selectedField = selectedField;
    }

    Type getSelectedType() {
        return selectedType;
    }

    void setSelectedType(Type selectedType) {
        this.selectedType = selectedType;
    }

    Tool getActiveTool() {
        return activeTool;
    }

    void setActiveTool(Tool activeTool) {
        this.activeTool = activeTool;
    }

    IProject getActiveProject() {
        return activeProject;
    }

    ArrayList<Tool> getAllToolList() {
        return allToolList;
    }

    CTabItem getFieldTabItem() {
        return fieldTabItem;
    }

    void setFieldTabItem(CTabItem fieldTabItem) {
        this.fieldTabItem = fieldTabItem;
    }

    public Shell getShell() {
        return shell;
    }

    public SkillFile getSkillFile() {
        return skillFile;
    }

}
