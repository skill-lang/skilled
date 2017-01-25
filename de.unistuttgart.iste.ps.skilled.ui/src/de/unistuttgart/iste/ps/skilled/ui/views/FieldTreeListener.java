package de.unistuttgart.iste.ps.skilled.ui.views;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.unistuttgart.iste.ps.skilled.sir.ClassType;
import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.Hint;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.ui.tools.EditorUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;

/**
 * This class is used to initialize the Listeners for the {@link Tree fieldTree}
 * used in the {@link ToolView toolview}
 * 
 * @author Ken Singer
 * @author Timm Felden
 * 
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
        fieldTree.addListener(SWT.Selection,
                e -> toolview.setSelectedField((FieldLike) (((TreeItem) e.item).getData())));

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
                    ToolUtil.generateTemporarySKilLFiles(toolview.getActiveTool().getName(),
                            toolview.getActiveProject());
                    editorUtil.openFieldInEditor(toolview.getActiveTool(), toolview.getSelectedField(),
                            toolview.getActiveProject());
                }
            }
        });

        // listener for the check boxes in the field tree
        fieldTree.addListener(SWT.Selection, event -> {
            if (event.detail == SWT.CHECK) {
                final TreeItem treeItem = (TreeItem) event.item;

                // determine whether selection is a field or a hint
                if (event.item.getData() instanceof FieldLike) {
                    final Tool tool = toolview.getActiveTool();
                    final FieldLike field = (FieldLike) treeItem.getData();

                    // if the user checks the checkbox, add the selected
                    // field, all its hints and the parenttype
                    if (treeItem.getChecked()) {
                        ClassType type = toolview.getSelectedType();
                        ToolUtil.addFieldToTool(field, type, tool);

                        toolview.getSkillFile().flush();
                        toolview.refreshTools();

                    } else {
                        ClassType type = toolview.getSelectedType();
                        HashMap<UserdefinedType, HashMap<String, FieldLike>> selected = tool.getSelectedFields();
                        HashMap<String, FieldLike> fs = selected.get(type);
                        fs.remove(field.getName().getSkillname());

                        toolview.getSkillFile().flush();
                        toolview.refreshTools();
                    }
                } else if (event.item.getData() instanceof Hint) {
                    final Hint hint = (Hint) treeItem.getData();
                    throw new NoSuchMethodError();
                    // final String hintParentName =
                    // ToolUtil.getActualName(((Field)
                    // hint.getParent()).getName());
                    // final String typeName =
                    // ToolUtil.getActualName(((Field)
                    // hint.getParent()).getType().getName());
                    // // if the user checks the checkbox add the selected
                    // // hint, the parentfield and the parenttype of the
                    // // field
                    // if (((TreeItem) event.item).getChecked()) {
                    //
                    // if (toolview.getActiveTool().getTypes().stream()
                    // .noneMatch(t -> t.getName().equals(typeName))) {
                    // ToolUtil.addTypeToTool(toolview.getActiveTool().getName(),
                    // toolview.getActiveProject(),
                    // typeName);
                    // toolview.reloadFieldList();
                    // }
                    // if
                    // (toolview.getActiveTool().getTypes().stream().filter(t
                    // -> t.getName().equals(typeName))
                    // .findFirst().get().getFields().stream()
                    // .noneMatch(f -> f.getName().equals(hintParentName)))
                    // {
                    // ToolUtil.addField(toolview.getActiveTool().getName(),
                    // toolview.getActiveProject(),
                    // typeName, hintParentName);
                    // toolview.reloadFieldList();
                    // }
                    // ToolUtil.addFieldHint(toolview.getActiveTool().getName(),
                    // toolview.getActiveProject(),
                    // typeName, hintParentName, hint.getName());
                    // toolview.reloadFieldList();
                    // } else {
                    // // if the user unchecks the checkbox, remove the
                    // // selected hint
                    // ToolUtil.removeFieldHint(toolview.getActiveTool().getName(),
                    // toolview.getActiveProject(),
                    // typeName, hintParentName, hint.getName());
                    // toolview.reloadFieldList();
                    // }
                }
            }
        });
    }
}
