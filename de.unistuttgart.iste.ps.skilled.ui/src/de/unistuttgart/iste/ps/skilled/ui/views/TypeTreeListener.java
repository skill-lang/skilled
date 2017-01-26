package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.unistuttgart.iste.ps.skilled.sir.ClassType;
import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.Hint;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.Type;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.tools.SIRHelper;
import de.unistuttgart.iste.ps.skilled.ui.tools.EditorUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;

/**
 * This class is used to initialize the Listeners for the {@link Tree typetree}
 * used in the {@link ToolView toolview}
 * 
 * @author Ken Singer
 * @author Timm Felden
 * 
 * @category GUI
 */
public class TypeTreeListener {

    private ToolView toolview;

    private EditorUtil editorUtil = new EditorUtil();

    public TypeTreeListener(ToolView toolview) {
        this.toolview = toolview;
    }

    /**
     * initialize the Listeners for the given {@link Tree typetree}
     * 
     * @param typeTree
     *            - {@link Tree}
     */
    public void initTypeTreeListener(Tree typeTree) {
        typeTree.addListener(SWT.Selection, e -> toolview.setSelectedType((ClassType) (((TreeItem) e.item).getData())));

        // add a mouse listener to the typetree
        typeTree.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent arg0) {
                // not used
            }

            @Override
            public void mouseDown(MouseEvent arg0) {
                // unused
            }

            @Override
            public void mouseDoubleClick(MouseEvent arg0) {
                // open the temporary file of the selected type
                if (toolview.getActiveProject() != null && toolview.getActiveTool() != null
                        && toolview.getSelectedType() != null) {
                    ToolUtil.generateTemporarySKilLFiles(toolview.getActiveTool().getName(),
                            toolview.getActiveProject());
                    editorUtil.openTypeInEditor(toolview.getActiveTool(), toolview.getSelectedType(),
                            toolview.getActiveProject());
                }
            }
        });

        // Listener for the checkboxes
        typeTree.addListener(SWT.Selection, event -> {
            if (event.detail == SWT.CHECK) {
                // determine whether selection is a type or a hint
                if (event.item.getData() instanceof Type) {
                    final UserdefinedType type = (UserdefinedType) ((TreeItem) event.item).getData();
                    final Tool tool = toolview.getActiveTool();

                    if (((TreeItem) event.item).getChecked()) {
                        // if the user checks the checkbox, add the selected
                        // type and all its fields and hints
                        ToolUtil.addTypeToTool(type, tool);
                        // TODO
                        // ToolUtil.addAllTypeHints(toolview.getActiveProject(),
                        // toolview.getActiveTool(), type);

                        // add all fields by default
                        for (FieldLike f : SIRHelper.fieldsOf(type)) {
                            ToolUtil.addFieldToTool(f, type, tool);
                        }
                        toolview.getSkillFile().flush();
                        toolview.refreshTools();

                    } else {
                        // TODO
                        // ToolUtil.removeAllTypeHints(toolview.getActiveProject(),
                        // tool, type);
                        ToolUtil.removeTypeFromTool(type, tool);
                        toolview.getSkillFile().flush();
                        toolview.refreshTools();
                    }
                } else if (event.item.getData() instanceof Hint) {
                    Hint hint = (Hint) ((TreeItem) event.item).getData();
                    throw new NoSuchMethodError();
                    // final String typeName = ((Type)
                    // hint.getParent()).getName();
                    // // if the user checks the checkbox, add the selected
                    // // hint and its parenttype
                    // if (((TreeItem) event.item).getChecked()) {
                    // if
                    // (toolview.getActiveTool().getSelectedUserTypes().stream()
                    // .noneMatch(t -> t.getName().equals(typeName)))
                    // ToolUtil.addTypeToTool(toolview.getActiveTool().getName(),
                    // toolview.getActiveProject(),
                    // typeName);
                    // ToolUtil.addTypeHint(toolview.getActiveTool().getName(),
                    // toolview.getActiveProject(),
                    // typeName, hint.getName());
                    // toolview.reloadTypelist();
                    // } else {
                    // // if the user unchecks the checkbox, remove the
                    // // selected hint
                    // ToolUtil.removeTypeHint(toolview.getActiveTool().getName(),
                    // toolview.getActiveProject(),
                    // typeName, hint.getName());
                    // toolview.reloadTypelist();
                    // }
                }
            }
        });
    }
}
