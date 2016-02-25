package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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


/**
 * This class is used to initialize the Listeners for the {@link Tree fieldTree} used in the {@link ToolView toolview}
 * 
 * @author Ken Singer
 * @category GUI
 */
public class FieldTreeListener {

    private ToolView toolview;

    private EditorUtil editorUtil = new EditorUtil();

    public FieldTreeListener(ToolView toolview) {
        this.toolview = toolview;
    }

    /**
     * initialize the Listeners for the given {@link Tree fieldTree}
     * 
     * @param fieldTree
     *            - {@link Tree}
     */
    public void initFieldTreeListener(Tree fieldTree) {
        fieldTree.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    toolview.setSelectedField((Field) (((TreeItem) e.item).getData()));
                } catch (@SuppressWarnings("unused") ClassCastException ex) {
                    return;
                    // item was a hint
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
                if (toolview.getActiveProject() != null && toolview.getActiveTool() != null
                        && toolview.getSelectedField() != null) {
                    ToolUtil.generateTemporarySKilLFiles(toolview.getActiveTool().getName(), toolview.getActiveProject());
                    editorUtil.openFieldInEditor(toolview.getActiveTool(), toolview.getSelectedField(),
                            toolview.getActiveProject());
                }
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
                            final String fieldName = field.getType().getName();
                            if (toolview.getActiveTool().getTypes().stream().noneMatch(t -> t.getName().equals(fieldName)))
                                ToolUtil.addTypeToTool(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                        field.getType().getName());
                            ToolUtil.addField(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(field.getType().getName()),
                                    ToolUtil.getActualName(field.getName()));
                            ToolUtil.addAllFieldHints(toolview.getActiveProject(), toolview.getActiveTool(), field.getType(),
                                    field);
                            toolview.reloadFieldList();
                        } else {
                            // if the user checks the checkbox add the selected field and all its hints
                            ToolUtil.removeAllFieldHints(toolview.getActiveProject(), toolview.getActiveTool(),
                                    field.getType(), field);
                            ToolUtil.removeField(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(field.getType().getName()),
                                    ToolUtil.getActualName(field.getName()));
                            toolview.reloadFieldList();
                        }
                    }

                    if (hint != null) {
                        // if the user checks the checkbox add the selected hint
                        if (((TreeItem) event.item).getChecked()) {
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
                            toolview.reloadFieldList();
                        } else {
                            // if the user unchecks the checkbox remove the selected hint
                            ToolUtil.removeFieldHint(toolview.getActiveTool().getName(), toolview.getActiveProject(),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getType().getName()),
                                    ToolUtil.getActualName(((Field) hint.getParent()).getName()), hint.getName());
                            toolview.reloadFieldList();
                        }
                    }
                }
            }
        });

        // enables a refresh on hitting the F5-key
        fieldTree.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.keyCode == SWT.F5)
                    toolview.refresh();
            }
        });
    }
}
