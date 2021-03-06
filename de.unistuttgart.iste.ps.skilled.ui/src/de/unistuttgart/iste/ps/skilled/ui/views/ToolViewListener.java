package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import de.unistuttgart.iste.ps.skilled.ui.tools.EditorUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;

/**
 * This class is used to initialize listeners for the {@link ToolView toolview}
 * 
 * @author Ken Singer
 * @category GUI
 */
public class ToolViewListener {
    final private ToolView toolview;

    final private EditorUtil editorUtil = new EditorUtil();

    public ToolViewListener(ToolView toolView) {
        this.toolview = toolView;
    }

    /**
     * initialize the listener for the {@link List toollist} used in the
     * {@link ToolView toolview}
     * 
     * @param toolViewList
     */
    public void initToolListListener(List toolViewList) {
        // keep selected tool updated
        toolViewList.addListener(SWT.Selection, e -> {
            if (toolViewList.getSelectionCount() != 0 && toolViewList.getItemCount() > 0) {
                toolview.setActiveTool(toolview.getSkillFile().Tools().getByID(1 + toolViewList.getSelectionIndex()));
            }
        });

        // open all temporary skillfiles of the tool
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
                if (toolview.getActiveProject() != null && toolview.getActiveTool() != null) {
                    ToolUtil.generateTemporarySKilLFiles(toolview.getActiveTool().getName(),
                            toolview.getActiveProject());
                    editorUtil.openToolInEditor(toolview.getActiveTool(), toolview.getActiveProject());
                }
            }
        });
    }

    /**
     * Listener to refresh the {@link ToolView toolview}
     * 
     * @param part
     *            - {@link IPartService}
     */
    public void initPartListener(IPartService part) {
        part.addPartListener(new IPartListener() {

            @Override
            public void partOpened(IWorkbenchPart part) {
                doRefreshOnProjectChange();
            }

            @Override
            public void partDeactivated(IWorkbenchPart part) {
                doRefreshOnProjectChange();
            }

            @Override
            public void partClosed(IWorkbenchPart part) {
                // toolview.setdoIndexing(true);
                toolview.refresh();
            }

            @Override
            public void partBroughtToTop(IWorkbenchPart part) {
                doRefreshOnProjectChange();
            }

            @Override
            public void partActivated(IWorkbenchPart part) {
                doRefreshOnProjectChange();
            }

            /**
             * calls {@link ToolView#refresh()} if the project has changed
             */
            private void doRefreshOnProjectChange() {
                if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor() != null) {
                    IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                            .getActivePage().getActiveEditor().getEditorInput();
                    IProject newActiveProject = file.getFile().getProject();

                    if (toolview.getActiveProject() == null
                            || !toolview.getActiveProject().getName().equals(newActiveProject.getName())) {
                        // toolview.setdoIndexing(true);
                        toolview.refresh();
                    }
                }
            }

        });
    }
}
