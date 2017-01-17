package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;


/**
 * Handle the ChangeActions of files in the editor.
 * 
 * @author Nico Rusam
 * @author Ken Singer
 * @category GUI
 *
 */
public class FileChangeAction {

    ToolView toolview = null;

    public FileChangeAction(ToolView tv) {
        this.toolview = tv;
    }

    /**
     * Handle save-action of active file in the editor.
     */
    public void save() {
        String commandId = "org.eclipse.ui.file.save";
        ICommandService service = PlatformUI.getWorkbench().getService(ICommandService.class);
        Command command = service.getCommand(commandId);
        command.addExecutionListener(new IExecutionListener() {

            @Override
            public void notHandled(String arg0, NotHandledException arg1) {
                // not used
            }

            @Override
            public void postExecuteFailure(String arg0, ExecutionException arg1) {
                // not used
            }

            @Override
            public void postExecuteSuccess(String arg0, Object arg1) {
                indexFile();
            }

            @Override
            public void preExecute(String arg0, ExecutionEvent arg1) {
                // not used
            }

        });
    }

    /**
     * Handle save-all-action of active file in the editor.
     */
    public void saveAll() {
        String commandId = "org.eclipse.ui.file.saveAll";
        ICommandService service = PlatformUI.getWorkbench().getService(ICommandService.class);
        Command command = service.getCommand(commandId);
        command.addExecutionListener(new IExecutionListener() {

            @Override
            public void notHandled(String arg0, NotHandledException arg1) {
                // not used
            }

            @Override
            public void postExecuteFailure(String arg0, ExecutionException arg1) {
                // not used
            }

            @Override
            public void postExecuteSuccess(String arg0, Object arg1) {
                indexFile();
            }

            @Override
            public void preExecute(String arg0, ExecutionEvent arg1) {
                // not used
            }

        });
    }

    /**
     * Handle import-action of active file in the editor.
     */
    public void imp0rt() {
        String commandId = "org.eclipse.ui.file.import";
        ICommandService service = PlatformUI.getWorkbench().getService(ICommandService.class);
        Command command = service.getCommand(commandId);
        command.addExecutionListener(new IExecutionListener() {

            @Override
            public void notHandled(String arg0, NotHandledException arg1) {
                // not used
            }

            @Override
            public void postExecuteFailure(String arg0, ExecutionException arg1) {
                // not used
            }

            @Override
            public void postExecuteSuccess(String arg0, Object arg1) {
                indexFile();
            }

            @Override
            public void preExecute(String arg0, ExecutionEvent arg1) {
                // not used
            }

        });
    }

    /**
     * indexes the file and refreshs the toolview
     * 
     * @return
     */
    private boolean indexFile() {
        boolean back = false;
        if (toolview != null && toolview.getActiveProject() != null) {
            try {
                // TODO? ToolUtil.deleteReportToolErrors(toolview.getActiveProject());
                back = ToolUtil.indexing(toolview.getActiveProject());
            } catch (CoreException e) {
                e.printStackTrace();
            }

            if (toolview.getTabFolder().getSelectionIndex() == 1)
                toolview.reloadTypelist();
            else if (toolview.getTabFolder().getSelectionIndex() == 2)
                toolview.reloadFieldList();
            else
                toolview.refresh();
        }
        return back;
    }
}
