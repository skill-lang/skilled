package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

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

import de.unistuttgart.iste.ps.skilled.sir.ClassType;
import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.Hint;
import de.unistuttgart.iste.ps.skilled.sir.Identifier;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.Type;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import de.unistuttgart.iste.ps.skilled.tools.SIRCache;

/**
 * This class creates the SKilL-Tool-Overview which enables the user to create
 * and organize {@link Tool tools} within a SKilLEd-Project. It can be used to
 * add, remove and rename tools, add and remove types/fields/hints.
 * 
 * 
 * @author Ken Singer
 * @author Nico Russam
 * @author Marco Link
 * @author Armin Hüneburg
 * @author Leslie Tso
 * @author Timm Felden
 * 
 * @TODO iterate = update? dann müsste man die parameter weitgehend entfernen
 *       und die methoden umbennen
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

    // State of the View
    private IProject activeProject = null;
    private SkillFile skillFile = null;

    private Tool activeTool = null;
    private ClassType selectedType = null;
    private FieldLike selectedField = null;

    private Menu menu;
    private boolean doIndexing = true;

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
     * refreshs the data shown in the {@link ToolView toolview} and sets the
     * {@link CTabFolder tooltab} as the selected tab of the {@link CTabFolder
     * tabfolder}.
     * 
     * @category GUI
     */
    public void refresh() {
        if (!parent.isDisposed()) {
            clearAll();
            toolTabItem.dispose();
            if (typeTabItem != null)
                typeTabItem.dispose();
            if (fieldTabItem != null)
                fieldTabItem.dispose();
            buildToolContextMenu(buildToollist());
            tabFolder.setSelection(toolTabItem);
        }
    }

    /**
     * reload the typelist after adding a {@link Type type} or {@link Hint
     * typehinthint} to a tool and sets the {@link CTabFolder typetab} as the
     * selected tab of the {@link CTabFolder tabfolder}.
     * 
     * @category GUI
     */
    void reloadTypelist() {
        refresh();
        buildTypeTree();
        tabFolder.setSelection(typeTabItem);
    }

    /**
     * Reload the fieldlist after adding a {@link FieldLike field} or
     * {@link Hint fieldhint} to a {@link Tree fieldtab} and sets the
     * {@link CTabFolder toolitem} as the selected tab of the {@link CTabFolder
     * tabfolder}.
     * 
     * @category GUI
     * 
     */
    void reloadFieldList() {
        reloadTypelist();
        buildFieldTree();
        tabFolder.setSelection(fieldTabItem);
    }

    /**
     * clears the lists <code>allToolList</code>, <code>allTypeList</code>,
     * <code>typeListOfActualTool</code> and
     * <code>typeHintListOfActualTool</code>. sets <code>activeProject<\code> to
     * null.
     * 
     * @category Data Handling
     */
    private void clearAll() {
        activeProject = null;
    }

    /**
     * Ensures that the .sir file used by this dialog is connected to
     * <code>{@link IProject activeProject}</code>.
     * 
     * @category Data Handling
     */
    boolean ensureActiveProjectandSIR() {
        try {
            IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().getActiveEditor().getEditorInput();

            activeProject = file.getFile().getProject();
            if (null == activeProject)
                return false;
        } catch (NullPointerException e) {
            // some of the components is not yet available
            return false;
        }

        skillFile = SIRCache.ensureFile(activeProject);
        return true;
    }

    /**
     * Builds up the {@link CTabItem tooltab} inside the {@link ToolView
     * toolview} and a {@link List list} of all {@link Tools tools}.
     * 
     * @return a {@link List} containing all the tools in the .skills file of
     *         the <code>{@link IProject activeProject}</code>
     * @category Data Handling
     */
    private List buildToollist() {
        boolean hasProject = ensureActiveProjectandSIR();

        if (tabFolder.isDisposed())
            tabFolder = new CTabFolder(parent, SWT.BORDER);

        List toolViewList = new List(tabFolder, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

        if (toolTabItem.isDisposed())
            toolTabItem = new CTabItem(tabFolder, 0, 0);

        if (hasProject)
            toolTabItem.setText("Tools - " + activeProject.getName());
        else
            toolTabItem.setText("Tools");

        toolTabItem.setControl(toolViewList);
        toolTabItem.setData(toolViewList);

        if (hasProject)
            for (Tool t : skillFile.Tools())
                toolViewList.add((t).getName());

        ToolViewListener tvl = new ToolViewListener(this);
        tvl.initToolListListener(toolViewList);

        return toolViewList;

    }

    /**
     * Builds up the <code>{@link Tree typeTree}</code>, containing all types
     * with their specific {@link Hint hints}.
     * 
     * @param activeTool
     *            - the selected {@link Tool}
     * @category GUI
     */
    void buildTypeTree() {
        Tree typeTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.SINGLE);

        if (typeTabItem == null || typeTabItem.isDisposed())
            typeTabItem = new CTabItem(tabFolder, 0, 1);

        typeTabItem.setText("Types - " + activeTool.getName());

        if (skillFile != null)
            updateTypes(typeTree);

        typeTabItem.setControl(typeTree);
        typeTabItem.setData(typeTree);

        TypeTreeListener tvl = new TypeTreeListener(this);
        tvl.initTypeTreeListener(typeTree);
    }

    /**
     * iterate over all the {@link Type types} and their {@link Hint hints} in
     * the <code>{@link IProject activeProject}</code>.
     * 
     * @param typeTree
     *            - a {@link Tree} to hold the types and their hints
     */
    private void updateTypes(Tree typeTree) {
        // add all types to the tree
        for (UserdefinedType type : skillFile.UserdefinedTypes()) {
            TreeItem typeTreeItem = new TreeItem(typeTree, 0);
            typeTreeItem.setText(nameToText(type.getName()));
            typeTreeItem.setData(type);

            // boolean selected = false;
            // if (null != activeTool &&
            // activeTool.getSelectedUserTypes().contains(type)) {
            // typeTreeItem.setChecked(selected = true);
            // }
            // TODO iterateTypeHints(typeTreeItem, type, selected);
        }
    }

    private String nameToText(Identifier name) {
        StringBuilder sb = new StringBuilder();
        for (String n : name.getParts())
            sb.append(n);
        return sb.toString();
    }

    /**
     * iterate over all {@link Hint typehints}.
     * 
     * @param typeTreeItem
     *            - {@link TreeItem}
     * @param type
     *            - the original {@link Type}
     * @param tooltype
     *            - the {@link Tool tool} specific type or <code>null</code> if
     *            the type not in tool
     */
    private void iterateTypeHints(TreeItem typeTreeItem, UserdefinedType type, boolean selected) {
        // add all typeHints to the Tree
        for (Hint hint : type.getHints()) {
            TreeItem typeHintItem = new TreeItem(typeTreeItem, 0);
            typeHintItem.setText(hint.getName());
            typeHintItem.setChecked(false);
            typeTreeItem.setExpanded(true);
            typeHintItem.setData(hint);

            // set all toolspecific typeHints as checked
            if (!selected)
                continue;

            // TODO use customizations instead and make restrictions checkable
            // as well

            // typeHintListOfActualTool = tooltype.getHints();
            // for (Hint toolhint : typeHintListOfActualTool) {
            // if (hint.getName().equals(toolhint.getName())) {
            // typeHintItem.setChecked(true);
            // break;
            // }
            // }
        }
    }

    /**
     * Build the <code>{@link Tree fieldTree}</code>, listing all
     * {@link FieldLike fields} with their specific {@link Hint hints} .
     * 
     * @category GUI
     */
    void buildFieldTree() {
        Tree fieldTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);

        if (fieldTabItem == null || fieldTabItem.isDisposed())
            fieldTabItem = new CTabItem(tabFolder, 0, 2);
        fieldTabItem.setText("Fields - " + nameToText(selectedType.getName()));

        if (skillFile != null && selectedType != null)
            // add all fields to the tree
            iterateFieldsAndFieldHints(fieldTree, selectedType);

        fieldTabItem.setControl(fieldTree);
        fieldTabItem.setData(fieldTree);

        FieldTreeListener ftl = new FieldTreeListener(this);
        ftl.initFieldTreeListener(fieldTree);
    }

    /**
     * iterate over all the {@link FieldLike fields} and their {@link Hint
     * hints}.
     * 
     * @param fieldTree
     *            - {@link Tree}
     * @param tooltype
     *            - the tool specific {@link Type} or <code>null</code> if not
     *            in tool
     */
    private void iterateFieldsAndFieldHints(Tree fieldTree, Type tooltype) {
        for (FieldLike field : selectedType.getFields()) {
            TreeItem fieldTreeItem = new TreeItem(fieldTree, 0);
            fieldTreeItem.setText(nameToText(field.getName()));
            fieldTreeItem.setChecked(false);
            fieldTreeItem.setData(field);
            fieldTreeItem.setExpanded(true);

            // check all the fields used by the actual tool
            for (HashMap<String, FieldLike> v : activeTool.getSelectedFields().values()) {
                if (v.values().contains(field)) {
                    fieldTreeItem.setChecked(true);
                    break;
                }
            }
            iterateFieldHints(field, null, fieldTreeItem);
        }
    }

    /**
     * iterate over all the {@link Hint hints} of a {@link FieldLike field}.
     * 
     * @param field
     *            - the original {@link FieldLike}
     * @param toolField
     *            - the tool specific {@link FieldLike} or <code>null</code> if
     *            not in tool
     * @param fieldTreeItem
     *            - {@link TreeItem}
     */
    private static void iterateFieldHints(FieldLike field, FieldLike toolField, TreeItem fieldTreeItem) {
        for (Hint hint : field.getHints()) {
            TreeItem fieldHintItem = new TreeItem(fieldTreeItem, 0);
            fieldHintItem.setText(hint.getName());
            fieldHintItem.setChecked(false);
            fieldTreeItem.setExpanded(true);
            fieldHintItem.setData(hint);

            // check all the hints used by the tool
            if (toolField == null)
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
     * Creates the {@link ContextMenuToolView contextmenu} for the
     * {@link ToolView toolview}.
     * 
     * @param toollist
     *            - {@link List} containing all the tools
     * @category GUI
     */
    private void buildToolContextMenu(List toollist) {
        if (menu != null)
            menu.dispose();
        menu = new Menu(toollist);
        ContextMenuToolView cmtv = new ContextMenuToolView(this);
        cmtv.initContextMenu(toollist, menu);
    }

    /**
     * recursivly deletes a {@link File directory}.
     * 
     * @param {@link
     *            File directory} - the directory to delete
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
     * Show a message window with the passed string.
     * 
     * @param message
     *            - {@link String Message} which should be displayed.
     * @category Dialog
     */
    void showMessage(String message) {
        MessageDialog.openInformation(shell, "Tool View", message);
    }

    /**
     * Return the selected .
     * 
     * @category Getter
     * @return the selected {@link FieldLike field}.
     */
    FieldLike getSelectedField() {
        return selectedField;
    }

    /**
     * Set the selected {@link FieldLike field}.
     * 
     * @category Setter
     * @param selectedField
     *            the selected {@link FieldLike field}.
     */
    void setSelectedField(FieldLike selectedField) {
        this.selectedField = selectedField;
        // TODO update???
    }

    /**
     * Get the selected {@link Type type}.
     * 
     * @category Getter
     * @return the selected {@link Type type}
     */
    ClassType getSelectedType() {
        return selectedType;
    }

    /**
     * Set the selected {@link Type type}.
     * 
     * @category Setter
     * @param selectedType
     *            the selected {@link Type type}.
     */
    void setSelectedType(ClassType selectedType) {
        this.selectedType = selectedType;
        // TODO update???
    }

    /**
     * Get the active {@link Tool tool}.
     * 
     * @category Getter
     * @return the active {@link Tool tool}.
     */
    Tool getActiveTool() {
        return activeTool;
    }

    /**
     * Set the active {@link Tool tool}.
     * 
     * @category Setter
     * @param activeTool
     *            the active {@link Tool tool}.
     */
    void setActiveTool(Tool activeTool) {
        this.activeTool = activeTool;
    }

    /**
     * Get the active {@link IProject project}.
     * 
     * @category Getter
     * @return the active {@link IProject project}.
     */
    IProject getActiveProject() {
        return activeProject;
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
     * returns if the {@link SkillFile} should be indexed
     * 
     * @return - {@link Boolean doIndexing}
     */
    public boolean isdoIndexing() {
        return doIndexing;
    }

    /**
     * sets if the {@link SkillFile} should be indexed
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
