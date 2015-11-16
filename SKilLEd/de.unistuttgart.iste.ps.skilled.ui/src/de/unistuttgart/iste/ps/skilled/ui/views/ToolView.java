package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.ui.part.ViewPart;

import de.unistuttgart.iste.ps.skilled.ui.wizards.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.WizardOption;
import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.api.SkillFile.Mode;
import tools.Field;
import tools.Hint;
import tools.Tool;
import tools.Type;
import tools.api.SkillFile;


/**
 * Class to create the SKilL-Tool-Overview.
 * 
 * @author Ken & Nico
 *
 */
public class ToolView extends ViewPart {

    private String path = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
    private CTabFolder tabFolder;

    private ArrayList<Tool> allToolList = new ArrayList<Tool>();
    private ArrayList<Type> allTypeList = new ArrayList<Type>();
    private ArrayList<Field> fieldListOfActualType = new ArrayList<Field>();
    private ArrayList<Type> typeListOfActualTool = new ArrayList<Type>();
    private ArrayList<Field> fieldListOfActualTool = new ArrayList<Field>();
    private ArrayList<Hint> typeHintListOfActualTool = new ArrayList<Hint>();
    private ArrayList<Hint> fieldHintListOfActualTool = new ArrayList<Hint>();

    private CTabItem toolTabItem = null;
    private CTabItem typeTabItem = null;
    private CTabItem fieldTabItem = null;
    private SkillFile skillFile = null;
    private Tool activeTool = null;
    private Shell shell;

    @Override
    public void createPartControl(Composite parent) {
        System.out.println(path);
        tabFolder = new CTabFolder(parent, SWT.BORDER);
        tabFolder.setVisible(true);
        shell = parent.getShell();
        buildToolContextMenu(buildToollist(tabFolder));
    }

    @Override
    public void setFocus() {
        tabFolder.setFocus();
    }

    /**
     * Read the binary file of all tools.
     */
    private void readToolBinaryFile() {
        try {
            skillFile = SkillFile.open(path, Mode.Read);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Löschen
            System.err.println("Fehler beim laden der SKilL-File.");
        }
        skillFile.Tools().forEach(t -> allToolList.add(t));
        skillFile.Types().forEach(t -> allTypeList.add(t));
    }

    /**
     * writes to the binary file
     * 
     * @return true if write is successful, false otherwise.
     */
    private boolean writeToolBinaryFile() {
        try {
            skillFile.flush();
        } catch (SkillException e) {
            System.err.println("Windows-Error occured!");
            e.printStackTrace();
            return false;
        }
        return true;
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
        List toolViewList = new List(parent, 1);
        if (toolTabItem == null) {
            toolTabItem = new CTabItem(tabFolder, 0, 0);
            toolTabItem.setText("Tools");
            toolTabItem.setControl(toolViewList);
        }

        buildToolContextMenu(toolViewList);

        if (null != skillFile)
            allToolList.forEach(t -> toolViewList.add(t.getName()));

        // Listener to get the chosen tool and build the right typetree
        toolViewList.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (toolViewList.getSelectionCount() != 0) {
                    activeTool = allToolList.get(toolViewList.getSelectionIndex());
                    buildTypTree(activeTool);
                    if (null != fieldTabItem)
                        fieldTabItem.dispose();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                // no default
            }
        });

        return toolViewList;
    }

    /**
     * Build up the typetree, listing all types with their specific hints.
     * 
     * @param tool
     *            - {@link Tool}
     */
    @SuppressWarnings("unchecked")
    private void buildTypTree(Tool tool) {
        Tree typeTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        typeListOfActualTool = (ArrayList<Type>) tool.getTypes().clone();
        TreeItem typeHintItem;

        if (null == typeTabItem)
            typeTabItem = new CTabItem(tabFolder, 0, 1);

        typeTabItem.setText("Types - " + tool.getName());

        if (null != skillFile) {// TODO != allTypeFile?

            // add all types to the tree
            for (Type type : allTypeList) {
                TreeItem typeTreeItem = new TreeItem(typeTree, 0);
                typeTreeItem.setData(type);
                typeTreeItem.setText(type.getName());
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
                // Auslagern?
                // add all typeHints to the Tree
                ArrayList<Hint> allTypeHintList = (ArrayList<Hint>) type.getTypeHints().clone();
                for (Hint hint : allTypeHintList) {
                    typeHintItem = new TreeItem(typeTreeItem, 0);
                    typeHintItem.setData(hint);
                    typeHintItem.setText(hint.getName());

                    // set all toolspecific typeHints as checked
                    if (null != tooltype && null != typeHintListOfActualTool) {
                        typeHintListOfActualTool = tooltype.getTypeHints();
                        for (Hint toolhint : typeHintListOfActualTool) {
                            if (hint.getName().equals(toolhint.getName())) {
                                typeHintItem.setChecked(true);
                                typeHintItem.setExpanded(true);
                                break;
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
                        buildFieldTree((Type) ((TreeItem) arg0.item).getData(), ((TreeItem) arg0.item).getChecked());
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
                System.out.println("Mouse Double Click");
            }
        });

        // Listener for the checkboxes
        typeTree.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    Type type = (Type) ((TreeItem) event.item).getData();
                    // TODO: Remove Syso
                    System.out.println(type.getName());
                    if (null == typeListOfActualTool)
                        typeListOfActualTool = new ArrayList<Type>();

                    // Check or uncheck the specific item in the TreeView.
                    if (((TreeItem) event.item).getChecked()) {
                        // TODO: Remove Syso
                        System.out.println("type added");
                        typeListOfActualTool.add(type);
                        type.getFields().forEach(f -> fieldListOfActualTool.add(f));
                        type.getTypeHints().forEach(h -> typeHintListOfActualTool.add(h));
                        buildFieldTree(type, true);
                    } else {
                        // TODO: Remove Syso
                        System.out.println("type remove");
                        typeListOfActualTool.remove(type);
                        type.getFields().forEach(f -> fieldListOfActualTool.remove(f));
                        type.getTypeHints().forEach(h -> typeHintListOfActualTool.remove(h));
                        tabFolder.getItem(2).getControl().setEnabled(false);
                    }
                    activeTool.setTypes(typeListOfActualTool);
                    writeToolBinaryFile();
                }
            }
        });
    }

    /**
     * Build the fieldtree, listing all fields with their specific hints.
     * 
     * @param type
     *            - {@link Type}
     */
    @SuppressWarnings("unchecked")
    private void buildFieldTree(Type type, boolean typeIsChecked) {
        Tree fieldTree = new Tree(tabFolder, SWT.MULTI | SWT.CHECK | SWT.FULL_SELECTION);
        fieldListOfActualType = (ArrayList<Field>) type.getFields().clone();
        Type tooltype = null;

        if (null != activeTool.getTypes())
            for (Type t : activeTool.getTypes()) {
                if (type.getName().equals(t.getName())) {
                    tooltype = t;
                    fieldListOfActualTool = (ArrayList<Field>) tooltype.getFields().clone();
                    break;
                }
            }

        if (null == fieldTabItem || fieldTabItem.isDisposed())
            fieldTabItem = new CTabItem(tabFolder, 0, 2);

        fieldTabItem.setText("Fields - " + type.getName());

        if (null != skillFile) {
            // add all fields to the tree
            for (Field field : fieldListOfActualType) {
                TreeItem fieldTreeItem = new TreeItem(fieldTree, 0);
                fieldTreeItem.setData(field);
                fieldTreeItem.setText(field.getName());
                Field toolField = null;

                // check all the fields used by the actual tool
                if (null != fieldListOfActualTool) {
                    for (Field f : fieldListOfActualTool) {
                        if (field.getName().equals(f.getName())) {
                            fieldTreeItem.setChecked(true);
                            toolField = f;
                            break;
                        }
                    }
                }

                // add all the fieldhints to the tree
                ArrayList<Hint> allFieldHintList = (ArrayList<Hint>) field.getFieldHints().clone();
                for (Hint hint : allFieldHintList) {
                    TreeItem fieldHintItem = new TreeItem(fieldTreeItem, 0);
                    fieldHintItem.setData(hint);
                    fieldHintItem.setText(hint.getName());

                    // check all the hints used by the tool
                    if (null != toolField && null != fieldHintListOfActualTool) {
                        fieldHintListOfActualTool = toolField.getFieldHints();
                        for (Hint h : fieldHintListOfActualTool) {
                            if (hint.getName().equals(h.getName())) {
                                fieldHintItem.setChecked(true);
                                fieldTreeItem.setExpanded(true);
                                break;
                            }
                        }
                    }
                }
            }
        }

        fieldTabItem.setControl(fieldTree);

        if (!typeIsChecked)
            tabFolder.getItem(2).getControl().setEnabled(false);

        initFieldTreeListener(fieldTree, tooltype);
    }

    /**
     * inits all the fieldlisteners
     * 
     * @param fieldTree
     * @param type
     */
    private void initFieldTreeListener(Tree fieldTree, Type type) {

        // listener for the checkboxes in the fieldtree
        fieldTree.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    Field field = null;
                    Hint hint = null;

                    if (event.item.getData() instanceof Field)
                        field = (Field) ((TreeItem) event.item).getData();
                    else
                        hint = (Hint) ((TreeItem) event.item).getData();

                    if (null != field) {
                        System.out.println(field.getName());
                        if (null == fieldListOfActualTool)
                            fieldListOfActualTool = new ArrayList<Field>();

                        if (((TreeItem) event.item).getChecked()) {
                            System.out.println("field added");
                            fieldListOfActualTool.add(field);
                            field.getFieldHints().forEach(h -> fieldHintListOfActualTool.add(h));
                            buildFieldTree(type, true);
                            writeToolBinaryFile();
                        } else {
                            System.out.println("field remove");
                            field.getFieldHints().forEach(h -> fieldHintListOfActualTool.remove(h));
                            fieldListOfActualTool.remove(field);
                            buildFieldTree(type, true);
                            writeToolBinaryFile();
                        }
                    }

                    if (hint != null) {
                        // TODO
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

                // Create contextmenu for 'Creaste Tool'.
                MenuItem createToolItem = new MenuItem(menu, SWT.NONE);
                createToolItem.setText("Create Tool");
                createToolItem.addSelectionListener(new SelectionListener() {
                    // TODO: Wizard Test
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        WizardDialog wizardDialog = new WizardDialog(shell, new SKilLToolWizard());

                        if (wizardDialog.open() == org.eclipse.jface.window.Window.OK)
                            System.out.println("Ok pressed");
                        else
                            System.out.println("Cancel pressed");
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });

                // Create contextmenu for 'Clone Tool'.
                MenuItem cloneToolItem = new MenuItem(menu, SWT.NONE);
                cloneToolItem.setText("Clone Tool");
                cloneToolItem.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        // Create new tool with same content like the tool to
                        // copy from.
                        Tool t = skillFile.Tools().make(activeTool.getFiles(), activeTool.getGenerator(),
                                activeTool.getLanguage(), activeTool.getModule(), activeTool.getName() + " - Copy",
                                activeTool.getOutPath(), activeTool.getTypes());
                        // add new item to toollist and allToolList under the
                        // item to be cloned.
                        toollist.add(t.getName(), selected + 1);
                        allToolList.add(selected + 1, t);
                        writeToolBinaryFile();
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
                        // Delete the selected tool from the toollist,
                        // skillFile.Tools() and allToolList.
                        toollist.remove(selected);
                        try {
                            skillFile.Tools().remove(activeTool);
                        } catch (Exception e) {
                            System.out.println("Tool gelöscht");
                            e.printStackTrace();
                        }
                        allToolList.remove(activeTool);
                        writeToolBinaryFile();
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
                            activeTool.setName(newWizard.getToolNewName());
                            toollist.remove(selected);
                            toollist.add(activeTool.getName(), selected);
                            skillFile.Tools().forEach(t -> {
                                if (t.equals(activeTool))
                                    t.setName(activeTool.getName());
                                // break the loop after find first equals?
                            });
                            typeTabItem.setText("Types - " + activeTool.getName());
                            writeToolBinaryFile();

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
}
