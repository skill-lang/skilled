package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import de.unistuttgart.iste.ps.skilled.ui.tools.EditorUtil;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;


/**
 * This class is used to initialize listeners for the toolview
 * 
 * @author Ken Singer
 *
 */
public class ToolViewListener {
    private ToolView toolview;

    private EditorUtil editorUtil = new EditorUtil();

    public ToolViewListener(ToolView toolView) {
        this.toolview = toolView;
    }

    /**
     * initialize the listener for the toollist used in the toolview
     * 
     * @param toolViewList
     */
    public void initToolListListener(List toolViewList) {
        // selection listener to set the active tool
        toolViewList.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                if (toolViewList.getSelectionCount() != 0) {
                    toolview.setActiveTool(toolview.getAllToolList().get(toolViewList.getSelectionIndex()));
                    toolview.buildTypeTree();
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {
                // no default
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
                ToolUtil.generateTemporarySKilLFiles(toolview.getActiveTool().getName(), toolview.getActiveProject());
                editorUtil.openToolInEditor(toolview.getActiveTool(), toolview.getActiveProject());
            }
        });
    }

    /**
     * Listener to refresh the ToolView
     * 
     * @param part
     *            - {@link IPartService}
     */
    public void initPartListener(IPartService part) {
        part.addPartListener(new IPartListener() {

            @Override
            public void partOpened(IWorkbenchPart part) {
                IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().getActiveEditor().getEditorInput();
                IProject newActiveProject = file.getFile().getProject();

                if (!toolview.getActiveProject().equals(newActiveProject))
                    toolview.refresh();
            }

            @Override
            public void partDeactivated(IWorkbenchPart part) {
                // not used

            }

            @Override
            public void partClosed(IWorkbenchPart part) {
                IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().getActiveEditor().getEditorInput();
                IProject newActiveProject = file.getFile().getProject();

                if (!toolview.getActiveProject().equals(newActiveProject))
                    toolview.refresh();
            }

            @Override
            public void partBroughtToTop(IWorkbenchPart part) {
                IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().getActiveEditor().getEditorInput();
                IProject newActiveProject = file.getFile().getProject();

                if (!toolview.getActiveProject().equals(newActiveProject))
                    toolview.refresh();
            }

            @Override
            public void partActivated(IWorkbenchPart part) {
                // not used
            }
        });
    }
}
