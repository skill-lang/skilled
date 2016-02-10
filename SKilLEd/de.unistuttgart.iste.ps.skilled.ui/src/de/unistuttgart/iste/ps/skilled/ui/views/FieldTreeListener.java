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
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Type;


/**
 * This class is used to initialize the Listeners for the fieldtree used in the toolview
 * 
 * @author Ken Singer
 *
 */
public class FieldTreeListener {

    private ToolView toolview;

    private EditorUtil editorUtil = new EditorUtil();

    public FieldTreeListener(ToolView toolview) {
        this.toolview = toolview;
    }

    /**
     * initialize the Listeners for the given fieldtree
     * 
     * @param fieldTree
     * @param tooltype
     */
    public void initFieldTreeListener(Tree fieldTree, Type tooltype) {
        fieldTree.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    if (((TreeItem) e.item).getData() instanceof Field) {
                        toolview.setSelectedField((Field) (((TreeItem) e.item).getData()));
                    }
                } catch (ClassCastException ee) {
                    //
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // no default

            }
        });

        fieldTree.addMouseListener(new MouseListener() {

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
                // open the temporary file of the selected type
                ToolUtil.generateTemporarySKilLFiles(toolview.getActiveTool().getName(), toolview.getActiveProject());
                editorUtil.openFieldInEditor(toolview.getActiveTool(), toolview.getSelectedField(),
                        toolview.getActiveProject());
            }
        });

        // listener for the checkboxes in the fieldtree
        fieldTree.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (event.detail == SWT.CHECK) {
                    Field field = null;
                    Hint hint = null;

                    // determine whether selection is a field or a hint
                    if (event.item.getData() instanceof Field)
                        field = (Field) ((TreeItem) event.item).getData();
                    else if (event.item.getData() instanceof Hint)
                        hint = (Hint) ((TreeItem) event.item).getData();

                    if (null != field) {
                        // if the user checks the checkbox add the selected field and all its hints
                        if (((TreeItem) event.item).getChecked()) {
                            System.out.println(field.getName() + "added");
                            final String fieldName = field.getType().getName();
                            if (toolview.getActiveTool().getTypes().stream().noneMatch(t -> t.getName().equals(fieldName)))
                                ToolUtil.addTypeToTool(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                        field.getType().getName());
                            ToolUtil.addField(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(field.getType().getName()),
                                    ToolUtil.getActualName(field.getName()));
                            ToolUtil.addAllFieldHints(toolview.getActiveProject(), toolview.getActiveTool(), tooltype,
                                    field);
                            toolview.setTypeIsInTool(true);
                            toolview.reloadFieldList(tooltype);
                        } else {
                            // if the user checks the checkbox add the selected field and all its hints
                            System.out.println(field.getName() + "remove");
                            ToolUtil.removeAllFieldHints(toolview.getActiveProject(), toolview.getActiveTool(),
                                    field.getType(), field);
                            ToolUtil.removeField(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(field.getType().getName()),
                                    ToolUtil.getActualName(field.getName()));
                            toolview.setTypeIsInTool(true);
                            toolview.reloadFieldList(tooltype);
                        }
                    }

                    if (hint != null) {
                        // if the user checks the checkbox add the selected hint
                        if (((TreeItem) event.item).getChecked()) {
                            System.out.println(hint.getName() + " added");
                            final String hintParentName = ((Field) hint.getParent()).getName();
                            final String typeName = ((Field) hint.getParent()).getType().getName();

                            if (toolview.getActiveTool().getTypes().stream().noneMatch(t -> t.getName().equals(typeName)))
                                ToolUtil.addTypeToTool(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                        typeName);

                            if (((Field) hint.getParent()).getType().getFields().stream()
                                    .noneMatch(f -> f.getName().equals(hintParentName)))
                                ToolUtil.addTypeToTool(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                        ((Field) hint.getParent()).getType().getName());
                            ToolUtil.addFieldHint(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getType().getName()),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getName()), hint.getName());
                            toolview.setTypeIsInTool(true);
                            toolview.reloadFieldList(tooltype);
                        } else {
                            // if the user unchecks the checkbox remove the selected hint
                            System.out.println("hint removed");
                            ToolUtil.removeFieldHint(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getType().getName()),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getName()), hint.getName());
                            toolview.setTypeIsInTool(true);
                            toolview.reloadFieldList(tooltype);
                        }
                    }
                }
            }
        });

    }
}
