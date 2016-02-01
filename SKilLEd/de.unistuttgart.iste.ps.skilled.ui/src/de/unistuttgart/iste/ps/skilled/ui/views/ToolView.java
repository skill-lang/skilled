package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.unistuttgart.iste.ps.skilled.ui.tools.EditorUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.export.SaveListofAllTools;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;
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
    private FileChangeAction fileChangeAction = new FileChangeAction();

    private String path = "";
    private CTabFolder tabFolder;

    private ArrayList<Tool> allToolList = new ArrayList<Tool>();
    private ArrayList<Type> allTypeList = new ArrayList<Type>();
    private ArrayList<Field> fieldListOfActualType = new ArrayList<Field>();
    private ArrayList<Type> typeListOfActualTool = new ArrayList<Type>();
    private ArrayList<Field> fieldListOfActualTool = new ArrayList<Field>();
    private ArrayList<Hint> typeHintListOfActualTool = new ArrayList<Hint>();
    private ArrayList<Hint> fieldHintListOfActualTool = new ArrayList<Hint>();

    private ArrayList<String> pathList = new ArrayList<String>();

    private CTabItem toolTabItem = null;
    private CTabItem typeTabItem = null;
    private CTabItem fieldTabItem = null;
    private SkillFile skillFile = null;
    private Tool activeTool = null;
    private IProject activeProject = null;
    private Type selectedType = null;
    private Shell shell;

    SaveListofAllTools fSave;

    // private static ToolView toolviewI = null;
    // private ToolView() {}
    // public static ToolView getToolViewInstance() {
    // if (null == toolviewI) {
    // toolviewI = new ToolView();
    // }
    // return toolviewI;
    // }

    @Override
    public void createPartControl(Composite parent) {
        tabFolder = new CTabFolder(parent, SWT.BORDER);
        tabFolder.setVisible(true);
        fileChangeAction.save();
        fileChangeAction.saveAll();
        fileChangeAction.rename();

        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(new IPartListener() {

            @Override
            public void partOpened(IWorkbenchPart part) {
                refresh();
            }

            @Override
            public void partDeactivated(IWorkbenchPart part) {
                // not used

            }

            @Override
            public void partClosed(IWorkbenchPart part) {
                refresh();
            }

            @Override
            public void partBroughtToTop(IWorkbenchPart part) {
                refresh();
            }

            @Override
            public void partActivated(IWorkbenchPart part) {
                setFocus();
            }
        });
        shell = parent.getShell();
        buildToolContextMenu(buildToollist(tabFolder));
        setFocus();
        makeActions();
        contributeToActionBars();
    }

    @Override
    public void setFocus() {
        tabFolder.setFocus();
    }

    public void refresh() {
        try {
            clearLists();
            if (typeTabItem != null)
                typeTabItem.dispose();
            if (toolTabItem != null)
                toolTabItem.dispose();
            if (fieldTabItem != null)
                fieldTabItem.dispose();
            buildToolContextMenu(buildToollist(tabFolder));
            setFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * reload the typelist after adding a type or hint to a tool
     */
    public void reloadTypelist() {
        refresh();
        buildTypeTree(activeTool);
    }

    /**
     * reload the fieldlist after adding a field or hint to a tooltype
     * 
     * @param type
     * @param isChecked
     */
    public void reloadFieldList(Type type, boolean isChecked) {
        reloadTypelist();
        buildFieldTree(type, isChecked);
    }

    public void clearLists() {
        allToolList.clear();
        pathList.clear();
        allTypeList.clear();
        typeListOfActualTool = null;
        fieldListOfActualTool = null;
        typeHintListOfActualTool = null;
        fieldHintListOfActualTool = null;
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
            return;
        }

        if (skillFile.Tools() != null) {
            skillFile.Tools().forEach(t -> allToolList.add(t));

            skillFile.Tools().forEach(t -> pathList.add(path));
            // fExport.setPaths(pathList);
        }

        if (skillFile.Types() != null) {
            skillFile.Types().forEach(t -> allTypeList.add(t));

        }

    }

    /**
     * Build up the tooltab, listing all tools.
     * 
     * @param parent
     *            - {@link Composite}
     * @return {@link List}
     */
    private List buildToollist(Composite parent) {
        readToolBinaryFile();
        List toolViewList = new List(parent, SWT.SINGLE);
        if (toolTabItem == null || toolTabItem.isDisposed()) {
            toolTabItem = new CTabItem(tabFolder, 0, 0);
            try {
                toolTabItem.setText("Tools - " + ((IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().getActiveEditor().getEditorInput()).getFile().getProject().getName());
            } catch (NullPointerException e) {
                toolTabItem.setText("Tools");
            }
            toolTabItem.setControl(toolViewList);
        }

        if (null != skillFile)
            allToolList.forEach(t -> toolViewList.add(t.getName()));

        // Listener to get the chosen tool and build the right typetree
        toolViewList.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (toolViewList.getSelectionCount() != 0) {
                    activeTool = allToolList.get(toolViewList.getSelectionIndex());
                    buildTypeTree(activeTool);
                    if (null != fieldTabItem)
                        fieldTabItem.dispose();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                // no default
            }
        });

        toolViewList.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // not used
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // not used
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                // TODO Open all the tempfiles used by the tool or the file in
                // which the actual type/field is
                EditorUtil eu = new EditorUtil();
                eu.openToolInEditor(activeTool, activeProject);
            }
        });

        fSave = new SaveListofAllTools();
        fSave.setListofAllTools(allToolList);
        fSave.setPathofAllTools(pathList);
        return toolViewList;

    }

    /**
     * Build up the typetree, listing all types with their specific hints.
     * 
     * @param tool
     *            - {@link Tool}
     */
    private void buildTypeTree(Tool tool) {
        Tree typeTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        typeListOfActualTool = tool.getTypes();
        TreeItem typeHintItem;

        if (null == typeTabItem || typeTabItem.isDisposed()) {
            typeTabItem = new CTabItem(tabFolder, 0, 1);
            typeTabItem.setShowClose(true);
        }

        typeTabItem.setText("Types - " + tool.getName());

        if (null != skillFile) {
            ArrayList<Type> typelist = (ArrayList<Type>) allTypeList.stream()
                    .filter(t -> tool.getTypes().contains(t) || (tool.getTypes().stream()
                            .noneMatch(ty -> ty.getName().equals(t.getName()) && ty.getFile() == t.getFile())
                            && skillFile.Tools().stream().noneMatch(to -> to.getTypes().contains(t))))
                    .collect(Collectors.toList());

            // add all types to the tree
            for (Type type : typelist) {
                TreeItem typeTreeItem = new TreeItem(typeTree, 0);
                typeTreeItem.setText(type.getName());
                typeTreeItem.setData(type);
                Type tooltype = null;

                // set all the toolspecific types as checked
                if (null != typeListOfActualTool) {
                    for (Type t : typeListOfActualTool) {
                        if (t.getName().equals(type.getName())) {
                            typeTreeItem.setChecked(true);
                            tooltype = t;
                            break;
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
        initTypeTreeListener(typeTree);
    }

    /**
     * inits the listeners for the typetree
     * 
     * @param typeTree
     */
    private void initTypeTreeListener(Tree typeTree) {
        typeTree.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    selectedType = (Type) (((TreeItem) e.item).getData());
                } catch (ClassCastException ee) {
                    //
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                try {
                    selectedType = (Type) (((TreeItem) e.item).getData());
                } catch (ClassCastException ee) {
                    //
                }

            }
        });

        typeTree.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // not used
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                typeTree.addSelectionListener(new SelectionListener() {
                    // listener to get the chosen type and build the proper
                    // fieldtree.
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        try {
                            buildFieldTree((Type) ((TreeItem) arg0.item).getData(), ((TreeItem) arg0.item).getChecked());
                        } catch (ClassCastException e) {
                            // hint was selected
                        }
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                // TODO Open all the tempfiles used by the tool or the file in
                // which the actual type/field is
                ToolUtil.generateTemporarySKilLFiles(activeTool.getName(), activeProject);
                EditorUtil eu = new EditorUtil();
                eu.openTypeInEditor(activeTool, selectedType, activeProject);
            }
        });

        // Listener for the checkboxes
        typeTree.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    Type type = null;
                    Hint hint = null;

                    if (event.item.getData() instanceof Type)
                        type = (Type) ((TreeItem) event.item).getData();
                    else if (event.item.getData() instanceof Hint)
                        hint = (Hint) ((TreeItem) event.item).getData();

                    if (null != type) {
                        System.out.println(type.getName());

                        if (((TreeItem) event.item).getChecked()) {
                            System.out.println("type added");
                            System.out.println(ToolUtil.addTypeToTool(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(type.getName())));
                            reloadTypelist();
                        } else {
                            System.out.println("type remove");
                            ToolUtil.removeTypeFromTool(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(type.getName()));
                            reloadTypelist();
                        }
                    }

                    if (hint != null) {
                        System.out.println();
                        if (((TreeItem) event.item).getChecked()) {
                            System.out.println(hint.getName() + "added");
                            final String typeName = ((Type) hint.getParent()).getName();
                            if (activeTool.getTypes().stream().noneMatch(t -> t.getName().equals(typeName)))
                                ToolUtil.addTypeToTool(activeTool.getName(), activeProject,
                                        ((Type) hint.getParent()).getName());
                            ToolUtil.addTypeHint(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(((Type) hint.getParent()).getName()), hint.getName());
                            reloadTypelist();
                        } else {
                            System.out.println(hint.getName() + "removed");
                            ToolUtil.removeTypeHint(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(((Type) hint.getParent()).getName()), hint.getName());
                            reloadTypelist();
                        }
                    }
                }
            }
        });
    }

    /**
     * Build the fieldtree, listing all fields with their specific hints.
     * 
     * @param tooltype
     *            - {@link Type}
     */
    private void buildFieldTree(Type tooltype, boolean typeIsChecked) {
        Tree fieldTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        fieldListOfActualType = tooltype.getFields();

        Type type = new Type();
        for (Type t : allTypeList) {
            if (tooltype.getName().equals(t.getName())) {
                type = t;
                break;
            }
        }

        if (null == fieldTabItem || fieldTabItem.isDisposed()) {
            fieldTabItem = new CTabItem(tabFolder, 0, 2);
            fieldTabItem.setShowClose(true);
        }

        fieldTabItem.setText("Fields - " + tooltype.getName());

        if (null != skillFile) {
            // add all fields to the tree
            for (Field field : type.getFields()) {
                TreeItem fieldTreeItem = new TreeItem(fieldTree, 0);
                fieldTreeItem.setText(field.getName());
                fieldTreeItem.setChecked(false);
                fieldTreeItem.setData(field);
                Field toolField = null;

                if (typeIsChecked != false) {
                    // check all the fields used by the actual tool
                    if (null != fieldListOfActualType) {
                        for (Field f : fieldListOfActualType) {
                            if (field.getName().equals(f.getName())) {
                                fieldTreeItem.setChecked(true);
                                toolField = f;
                                break;
                            }
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

                    if (typeIsChecked != false)
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
        initFieldTreeListener(fieldTree, tooltype);
    }

    /**
     * inits all the fieldlisteners
     * 
     * @param fieldTree
     * @param tooltype
     */
    private void initFieldTreeListener(Tree fieldTree, Type tooltype) {

        // listener for the checkboxes in the fieldtree
        fieldTree.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    Field field = null;
                    Hint hint = null;

                    if (event.item.getData() instanceof Field)
                        field = (Field) ((TreeItem) event.item).getData();
                    else if (event.item.getData() instanceof Hint)
                        hint = (Hint) ((TreeItem) event.item).getData();

                    if (null != field) {

                        if (((TreeItem) event.item).getChecked()) {
                            System.out.println(field.getName() + "added");
                            final String fieldName = field.getType().getName();
                            if (activeTool.getTypes().stream().noneMatch(t -> t.getName().equals(fieldName)))
                                ToolUtil.addTypeToTool(activeTool.getName(), activeProject, field.getType().getName());
                            ToolUtil.addField(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(field.getType().getName()),
                                    ToolUtil.getActualName(field.getName()));
                            reloadFieldList(tooltype, true);
                        } else {
                            System.out.println(field.getName() + "remove");
                            ToolUtil.removeField(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(field.getType().getName()),
                                    ToolUtil.getActualName(field.getName()));
                            reloadFieldList(tooltype, false);
                        }
                    }

                    if (hint != null) {

                        if (((TreeItem) event.item).getChecked()) {
                            System.out.println(hint.getName() + " added");
                            final String hintParentName = ((Field) hint.getParent()).getName();
                            final String typeName = ((Field) hint.getParent()).getType().getName();

                            if (activeTool.getTypes().stream().noneMatch(t -> t.getName().equals(typeName)))
                                ToolUtil.addTypeToTool(activeTool.getName(), activeProject, typeName);

                            if (((Field) hint.getParent()).getType().getFields().stream()
                                    .noneMatch(f -> f.getName().equals(hintParentName)))
                                ToolUtil.addTypeToTool(activeTool.getName(), activeProject,
                                        ((Field) hint.getParent()).getType().getName());
                            ToolUtil.addFieldHint(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(((Field) hint.getParent()).getType().getName()),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getName()), hint.getName());
                            reloadFieldList(tooltype, true);
                        } else {
                            System.out.println("hint removed");
                            ToolUtil.removeFieldHint(activeTool.getName(), activeProject,
                                    ToolUtil.getActualName(((Field) hint.getParent()).getType().getName()),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getName()), hint.getName());
                            reloadFieldList(tooltype, true);
                        }
                    }
                }
            }
        });

    }

    Menu menu;

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
        toollist.setMenu(menu);
        menu.addMenuListener(new MenuAdapter() {
            @Override
            public void menuShown(MenuEvent e) {
                int selected = toollist.getSelectionIndex();

                if (selected < 0 || selected >= toollist.getItemCount())
                    return;

                for (MenuItem mI : menu.getItems())
                    mI.dispose();

                // Create contextmenu for 'Clone Tool'.
                MenuItem cloneToolItem = new MenuItem(menu, SWT.NONE);
                cloneToolItem.setText("Clone Tool");
                cloneToolItem.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        ToolUtil.cloneTool(activeProject, activeTool, skillFile);
                        refresh();
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });

                // Create contextmenu for 'Delete Tool'.
                // Delete is currently not implemented in skill (08.10.2015).
                MenuItem deleteToolItem = new MenuItem(menu, SWT.NONE);
                deleteToolItem.setText("Delete Tool");
                deleteToolItem.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        ToolUtil.cleanUpAfterDeletion(activeProject, activeTool);
                        ToolUtil.removeTool(activeProject, activeTool.getName());
                        File toDelete = new File(activeProject.getLocationURI().getPath().toString() + File.separator
                                + ".skillt" + File.separator + activeTool.getName());
                        deleteDirectoryRecursivly(toDelete);
                        refresh();
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });

                // Create contextmenu for 'Rename Tool'.
                MenuItem renameToolItem = new MenuItem(menu, SWT.NONE);
                renameToolItem.setText("Rename Tool");
                renameToolItem.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        SKilLToolWizard newWizard = new SKilLToolWizard(WizardOption.RENAME, toollist.getItem(selected));
                        WizardDialog wizardDialog = new WizardDialog(shell, newWizard);

                        if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
                            System.out.println("Ok pressed");
                            ToolUtil.renameTool(activeTool.getName(), newWizard.getToolNewName(), activeProject);
                            refresh();

                        } else
                            System.out.println("Cancel pressed");
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });
            }
        });
    }

    /**
     * recursivly deletes a directory
     * 
     * @param directoryToDelete
     *            - the directory to delete
     * @throws IOException
     */
    private void deleteDirectoryRecursivly(File directoryToDelete) {
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
                    showMessage("EVERYTHING BROKEN AAAAAAAAAAAAAAAAAAAA!!!!!!!!!!!A");
                } else {
                    ToolUtil.generateTemporarySKilLFiles(newToolName, activeProject);
                }
                refresh();
            }
        }
    }

    /**
     * Method to print the values of all lists at the Console.
     */
    private void printAllLists() {

        System.out.println("AllToolsList---------------");
        for (Tool tool : allToolList) {
            System.out.println(tool.getName());
        }
        System.out.println("AllTypeList----------------");
        for (Type tool : allTypeList) {
            System.out.println(tool.getName());
        }
        System.out.println("FieldListOfActualType------");
        for (Field tool : fieldListOfActualType) {
            System.out.println(tool.getName());
        }
        System.out.println("TypeListOfActualTool-------");
        for (Type tool : typeListOfActualTool) {
            System.out.println(tool.getName());
        }
        System.out.println("FieldListOfActualTool------");
        for (Field tool : fieldListOfActualTool) {
            System.out.println(tool.getName());
        }
        System.out.println("TypeHintListOfAcutalTool---");
        for (Hint tool : typeHintListOfActualTool) {
            System.out.println(tool.getName());
        }
        System.out.println("fieldHintListOfActualTool--");
        for (Hint tool : fieldHintListOfActualTool) {
            System.out.println(tool.getName());
        }
    }

    public ArrayList<Tool> getListofTools() {
        return allToolList;
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(shell, "Tool View", message);
    }

}
