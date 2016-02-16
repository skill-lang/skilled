package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.unistuttgart.iste.ps.skilled.ui.tools.EditorUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Type;


/**
 * This class is used to initialize the Listeners for the typetree used in the toolview
 * 
 * @author Ken Singer
 * @category GUI
 */
public class TypeTreeListener {

    private ToolView toolview;

    private EditorUtil editorUtil = new EditorUtil();

    public TypeTreeListener(ToolView toolview) {
        this.toolview = toolview;
    }

    /**
     * initialize the Listeners for the given typetree
     * 
     * @param typeTree
     */
    public void initTypeTreeListener(Tree typeTree) {
        // add a mouse listener to the typetree
        typeTree.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // not used
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // on mouseclick select type at selection index and build the fieldtree
                typeTree.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        try {
                            toolview.setSelectedType((Type) (((TreeItem) e.item).getData()));
                            toolview.buildFieldTree();
                        } catch (ClassCastException ex) {
                            ex.getMessage();
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
                // open the temporary file of the selected type
                ToolUtil.generateTemporarySKilLFiles(toolview.getActiveTool().getName(), toolview.getActiveProject());
                System.out.println(toolview.getActiveTool() == null);
                System.out.println(toolview.getSelectedType() == null);
                System.out.println(toolview.getActiveProject() == null);
                editorUtil.openTypeInEditor(toolview.getActiveTool(), toolview.getSelectedType(),
                        toolview.getActiveProject());
            }
        });

        // Listener for the checkboxes
        typeTree.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    Type type = null;
                    Hint hint = null;

                    // determine whether selection is a type or a hint
                    if (event.item.getData() instanceof Type)
                        type = (Type) ((TreeItem) event.item).getData();
                    else if (event.item.getData() instanceof Hint)
                        hint = (Hint) ((TreeItem) event.item).getData();

                    if (null != type) {
                        if (((TreeItem) event.item).getChecked()) {
                            // if the user checks the checkbox add the selected type and all its fields and hints
                            ToolUtil.addTypeToTool(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(type.getName()));
                            ToolUtil.addAllTypeHints(toolview.getActiveProject(), toolview.getActiveTool(), type);
                            ToolUtil.addAllFields(toolview.getActiveProject(), toolview.getActiveTool(), type);
                            toolview.reloadTypelist();
                        } else {
                            // if the user unchecks the checkbox remove the selected type and all its fields and hints
                            ToolUtil.removeAllFields(toolview.getActiveProject(), toolview.getActiveTool(), type);
                            ToolUtil.removeAllTypeHints(toolview.getActiveProject(), toolview.getActiveTool(), type);
                            ToolUtil.removeTypeAndAllSubtypes(toolview.getActiveProject(), toolview.getActiveTool(), type);
                            toolview.reloadTypelist();
                        }
                    }

                    if (hint != null) {
                        // if the user checks the checkbox add the selected hint
                        if (((TreeItem) event.item).getChecked()) {
                            final String typeName = ((Type) hint.getParent()).getName();
                            if (toolview.getActiveTool().getTypes().stream().noneMatch(t -> t.getName().equals(typeName)))
                                ToolUtil.addTypeToTool(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                        ((Type) hint.getParent()).getName());
                            ToolUtil.addTypeHint(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(((Type) hint.getParent()).getName()), hint.getName());
                            toolview.reloadTypelist();
                        } else {
                            // if the user unchecks the checkbox remove the selected hint
                            ToolUtil.removeTypeHint(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(((Type) hint.getParent()).getName()), hint.getName());
                            toolview.reloadTypelist();
                        }
                    }
                }
            }
        });
    }
}
