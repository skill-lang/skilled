package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillFile.Mode;


/**
 * This class creates the SKilL-Tool-Overview which enables the user to create and organize {@link Tool tools} within a
 * SKilLEd-Project.
 * 
 * 
 * @author Ken Singer
 * @author Nico Russam
 * @author Marco Link
 * @author Armin Hüneburg
 * @author Leslie Tso
 * 
 * @category GUI
 */
public class ToolView extends ViewPart {
    // Actions
    private final FileChangeAction fileChangeAction = new FileChangeAction(this);
    // SWT
    private CTabFolder tabFolder;
    private CTabItem toolTabItem = null;
    private CTabItem typeTabItem = null;
    private CTabItem fieldTabItem = null;
    private Composite parent = null;
    private Shell shell;
    // Memory
    private SkillFile skillFile = null;
    private Tool activeTool = null;
    private Type selectedType = null;
    private Field selectedField = null;
    private IProject activeProject = null;
    private String path = "";
    private Menu menu;
    private boolean doIndexing = true;
    // Lists
    private final ArrayList<Tool> allToolList = new ArrayList<Tool>();
    private final ArrayList<Type> allTypeList = new ArrayList<Type>();
    private ArrayList<Type> typeListOfActualTool = new ArrayList<Type>();
    private ArrayList<Hint> typeHintListOfActualTool = new ArrayList<Hint>();

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        tabFolder = new CTabFolder(parent, SWT.BORDER);
        toolTabItem = new CTabItem(tabFolder, 0, 0);

        tabFolder.setVisible(true);
        fileChangeAction.save();
        fileChangeAction.saveAll();
        fileChangeAction.imp0rt();
        shell = parent.getShell();

        buildToolContextMenu(buildToollist());

        ToolViewButtonInitializer tvbi = new ToolViewButtonInitializer(this);
        tvbi.initialize();

        ToolViewListener tvl = new ToolViewListener(this);
        tvl.initPartListener(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());

        setFocus();
    }

    @Override
    public void setFocus() {
        tabFolder.setSelection(toolTabItem);
    }

    /**
     * refreshs the data shown in the {@link ToolView toolview}
     * 
     * @category Data Handling
     */
    public void refresh() {
        if (!parent.isDisposed()) {
            clearAll();
            toolTabItem.dispose();
            if (null != typeTabItem)
                typeTabItem.dispose();
            if (null != fieldTabItem)
                fieldTabItem.dispose();
            buildToolContextMenu(buildToollist());
            tabFolder.setSelection(toolTabItem);
        }
    }

    /**
     * reload the typelist after adding a {@link Type type} or {@link Hint hint}to a tool
     * 
     * @category Data Handling
     */
    void reloadTypelist() {
        refresh();
        activeTool = skillFile.Tools().stream()
                .filter(tool -> tool.getName().toLowerCase().equals(activeTool.getName().toLowerCase())).findFirst().get();
        buildTypeTree();
        tabFolder.setSelection(typeTabItem);
    }

    /**
     * Reload the fieldlist after adding a {@link Field field} or {@link Hint hint} to a {@link Tree tooltype}
     * 
     * @category Data Handling
     * @category GUI
     * @param type
     * 
     */
    void reloadFieldList() {
        reloadTypelist();
        selectedType = skillFile.Types().stream()
                .filter(type -> type.getName().toLowerCase().equals(selectedType.getName().toLowerCase())).findFirst().get();
        buildFieldTree();
        tabFolder.setSelection(fieldTabItem);
    }

    /**
     * clears the lists <code>allToolList</code>, <code>allTypeList</code>, <code>typeListOfActualTool</code> and
     * <code>typeHintListOfActualTool</code>. sets <code>activeProject<\code> to null .
     * 
     * @category Data Handling
     */
    private void clearAll() {
        activeProject = null;
        allToolList.clear();
        allTypeList.clear();
        typeListOfActualTool.clear();
        typeHintListOfActualTool.clear();
    }

    /**
     * Read the .skills file of the <code>activeProject</code>
     * 
     * @category Data Handling
     */
    void readToolBinaryFile() {
        try {
            IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor().getEditorInput();
            activeProject = file.getFile().getProject();

            if (doIndexing) {
                ToolUtil.indexing(activeProject);
                doIndexing = false;
            }

            path = activeProject.getLocation().toOSString() + File.separator + ".skills";
            skillFile = SkillFile.open(path, Mode.ReadOnly);
        } catch (@SuppressWarnings("unused") Exception e) {
            // skillfile does not exist or no active project
            return;
        }

        if (skillFile.Tools() != null)
            skillFile.Tools().forEach(tool -> allToolList.add(tool));

        if (skillFile.Types() != null)
            skillFile.Types().stream().filter(t -> t.getOrig() == null).forEach(t -> allTypeList.add(t));
    }

    /**
     * Build up the tooltab inside the toolview an a list of all tools.
     * 
     * @param parent
     *            - {@link Composite}
     * @return a {@link List} containing all the tools in the .skills file of the <code>activeProject</code>
     * @category Data Handling
     */
    private List buildToollist() {
        readToolBinaryFile();

        if (tabFolder.isDisposed())
            tabFolder = new CTabFolder(parent, SWT.BORDER);

        List toolViewList = new List(tabFolder, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

        if (toolTabItem.isDisposed())
            toolTabItem = new CTabItem(tabFolder, 0, 0);

        try {
            toolTabItem.setText("Tools - " + activeProject.getName());
        } catch (@SuppressWarnings("unused") NullPointerException e) {
            toolTabItem.setText("Tools");
        }
        toolTabItem.setControl(toolViewList);
        toolTabItem.setData(toolViewList);

        if (null != skillFile)
            allToolList.forEach(t -> toolViewList.add((t).getName()));

        ToolViewListener tvl = new ToolViewListener(this);
        tvl.initToolListListener(toolViewList);

        return toolViewList;

    }

    /**
     * Builds up the <code>typeTree</code>, containing all types with their specific hints.
     * 
     * @param activeTool
     *            - the selected {@link Tool}
     * @category GUI
     */
    void buildTypeTree() {
        Tree typeTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.SINGLE);
        typeListOfActualTool = activeTool.getTypes().stream().filter(t -> t != null)
                .collect(Collectors.toCollection(ArrayList<Type>::new));

        if (null == typeTabItem || typeTabItem.isDisposed())
            typeTabItem = new CTabItem(tabFolder, 0, 1);

        typeTabItem.setText("Types - " + activeTool.getName());

        if (null != skillFile)
            // add all types to the tree
            iterateTypesAndHints(typeTree);

        typeTabItem.setControl(typeTree);
        typeTabItem.setData(typeTree);

        TypeTreeListener tvl = new TypeTreeListener(this);
        tvl.initTypeTreeListener(typeTree);
    }

    /**
     * iterate over all the types and their hints
     * 
     * @param typeTree
     *            - {@link Tree}
     */
    private void iterateTypesAndHints(Tree typeTree) {
        for (Type type : allTypeList) {
            TreeItem typeTreeItem = new TreeItem(typeTree, 0);
            typeTreeItem.setText(type.getName().replaceAll(" %s", ""));
            typeTreeItem.setData(type);
            Type tooltype = null;

            // set all the toolspecific types as checked
            if (null != typeListOfActualTool) {
                for (Type t : typeListOfActualTool) {
                    if (t.getName().equals(type.getName())) {
                        typeTreeItem.setChecked(true);
                        tooltype = type;
                        break;
                    }
                }
            }
            interateTypeHints(typeTreeItem, type, tooltype);
        }
    }

    /**
     * iterate over all typehints
     * 
     * @param typeTreeItem
     *            - {@link TreeItem}
     * @param type
     *            - the original {@link Type}
     * @param tooltype
     *            - the tool specific {@link Type} or <code>null</code> if not in tool
     */
    private void interateTypeHints(TreeItem typeTreeItem, Type type, Type tooltype) {
        // add all typeHints to the Tree
        for (Hint hint : type.getHints()) {
            TreeItem typeHintItem = new TreeItem(typeTreeItem, 0);
            typeHintItem.setText(hint.getName());
            typeHintItem.setChecked(false);
            typeTreeItem.setExpanded(true);
            typeHintItem.setData(hint);

            // set all toolspecific typeHints as checked
            if (null == tooltype || null == tooltype.getHints())
                continue;
            typeHintListOfActualTool = tooltype.getHints();
            for (Hint toolhint : typeHintListOfActualTool) {
                if (hint.getName().equals(toolhint.getName())) {
                    typeHintItem.setChecked(true);
                    break;
                }
            }
        }
    }

    /**
     * Build the <code>fieldTree</code>, listing all fields with their specific hints.
     * 
     * @category GUI
     */
    void buildFieldTree() {
        Tree fieldTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        Type tooltype;

        if (null == fieldTabItem || fieldTabItem.isDisposed())
            fieldTabItem = new CTabItem(tabFolder, 0, 2);
        try {
            tooltype = typeListOfActualTool.stream().filter(t -> selectedType.getName().equals(t.getName())).findFirst()
                    .get();
        } catch (@SuppressWarnings("unused") NoSuchElementException e) {
            tooltype = null;
        }

        fieldTabItem.setText("Fields - " + selectedType.getName().replaceAll(" %s", ""));

        if (null != skillFile && null != selectedType)
            // add all fields to the tree
            iterateFieldsAndFieldHints(fieldTree, tooltype);

        fieldTabItem.setControl(fieldTree);
        fieldTabItem.setData(fieldTree);

        FieldTreeListener ftl = new FieldTreeListener(this);
        ftl.initFieldTreeListener(fieldTree);
    }

    /**
     * iterate over all the fields and their hints
     * 
     * @param fieldTree
     *            - {@link Tree}
     * @param tooltype
     *            - the tool specific {@link Type} or <code>null</code> if not in tool
     */
    private void iterateFieldsAndFieldHints(Tree fieldTree, Type tooltype) {
        for (Field field : selectedType.getFields()) {
            TreeItem fieldTreeItem = new TreeItem(fieldTree, 0);
            fieldTreeItem.setText(field.getName());
            fieldTreeItem.setChecked(false);
            fieldTreeItem.setData(field);
            fieldTreeItem.setExpanded(true);
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
            iterateFieldHints(field, toolField, fieldTreeItem);
        }
    }

    /**
     * iterate over all the hints of a field
     * 
     * @param field
     *            - the original {@link Field}
     * @param toolField
     *            - the tool specific {@link Field} or <code>null</code> if not in tool
     * @param fieldTreeItem
     *            - {@link TreeItem}
     */
    private static void iterateFieldHints(Field field, Field toolField, TreeItem fieldTreeItem) {

        for (Hint hint : field.getHints()) {
            TreeItem fieldHintItem = new TreeItem(fieldTreeItem, 0);
            fieldHintItem.setText(hint.getName());
            fieldHintItem.setChecked(false);
            fieldTreeItem.setExpanded(true);
            fieldHintItem.setData(hint);

            // check all the hints used by the tool
            if (null == toolField)
                continue;
            for (Hint h : toolField.getHints()) {
                if (hint.getName().equals(h.getName())) {
                    fieldHintItem.setChecked(true);
                    break;
                }
            }
        }
    }

    /**
     * Creates the contextmenu for the toolview.
     * 
     * @param toollist
     *            - {@link List} containing all the tools
     * @category GUI
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
     *            - the {@link File directory} to delete
     * @category Data Handling
     */
    void deleteDirectoryRecursivly(File directoryToDelete) {
        if (directoryToDelete.isDirectory())
            for (File toDelete : directoryToDelete.listFiles())
                deleteDirectoryRecursivly(toDelete);

        try {
            Files.deleteIfExists(directoryToDelete.toPath());
        } catch (IOException e) {
            showMessage(e.getMessage());
        }
    }

    /**
     * Show a message with the parameter string.
     * 
     * @param message
     *            - {@link String Message} which should be printed.
     * @category Dialog
     */
    void showMessage(String message) {
        MessageDialog.openInformation(shell, "Tool View", message);
    }

    /**
     * Return the selected field.
     * 
     * @category Getter
     * @return the selected {@link Field field}.
     */
    Field getSelectedField() {
        return selectedField;
    }

    /**
     * Set the selected field.
     * 
     * @category Setter
     * @param selectedField
     *            the selected {@link Field field}.
     */
    void setSelectedField(Field selectedField) {
        this.selectedField = selectedField;
    }

    /**
     * Get the selected type.
     * 
     * @category Getter
     * @return the selected {@link Type type}
     */
    Type getSelectedType() {
        return selectedType;
    }

    /**
     * Set the selected type.
     * 
     * @category Setter
     * @param selectedType
     *            the selected {@link Type type}.
     */
    void setSelectedType(Type selectedType) {
        this.selectedType = selectedType;
    }

    /**
     * Get the active tool.
     * 
     * @category Getter
     * @return the active {@link Tool tool}.
     */
    Tool getActiveTool() {
        return activeTool;
    }

    /**
     * Set the active tool.
     * 
     * @category Setter
     * @param activeTool
     *            the active {@link Tool tool}.
     */
    void setActiveTool(Tool activeTool) {
        this.activeTool = activeTool;
    }

    /**
     * Get the active project.
     * 
     * @category Getter
     * @return the active {@link IProject project}.
     */
    IProject getActiveProject() {
        return activeProject;
    }

    /**
     * Get a list of all tools.
     * 
     * @category Getter
     * @return {@link ArrayList ArrayList} from type tool with all tools.
     */
    ArrayList<Tool> getAllToolList() {
        return allToolList;
    }

    /**
     * Get the shell.
     * 
     * @category Getter
     * @return {@link Shell shell}
     */
    public Shell getShell() {
        return shell;
    }

    /**
     * Get skill file.
     * 
     * @category Getter
     * @return {@link SkillFile skillfile}
     */
    public SkillFile getSkillFile() {
        return skillFile;
    }

    /**
     * returns the {@link Boolean doIndexing} field
     * 
     * @return - {@link Boolean doIndexing}
     */
    public boolean isdoIndexing() {
        return doIndexing;
    }

    /**
     * sets the {@link Boolean doIndexing} field
     * 
     * @param doIndexing
     *            - {@link Boolean}
     */
    public void setdoIndexing(boolean doIndexing) {
        this.doIndexing = doIndexing;
    }

    /**
     * returns the {@link CTabFolder tabfolder} of the toolview
     * 
     * @return te {@link CTabFolder tabfolder} of the toolview
     */
    public CTabFolder getTabFolder() {
        return tabFolder;
    }

    /**
     * sets the {@link CTabFolder tabfolder} of the toolview
     * 
     * @param tabFolder
     *            - {@link CTabFolder}
     */
    public void setTabFolder(CTabFolder tabFolder) {
        this.tabFolder = tabFolder;
    }
}
