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
                System.out.println("save notHandled");
            }

            @Override
            public void postExecuteFailure(String arg0, ExecutionException arg1) {
                System.out.println("save ExecuteFailure");
            }

            @Override
            public void postExecuteSuccess(String arg0, Object arg1) {
                System.out.println("save ExecuteSuccess");
                indexFile();
            }

            @Override
            public void preExecute(String arg0, ExecutionEvent arg1) {
                System.out.println("save preExecute");
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
                System.out.println("saveAll nothandled");
            }

            @Override
            public void postExecuteFailure(String arg0, ExecutionException arg1) {
                System.out.println("saveAll ExecuteFailure");
            }

            @Override
            public void postExecuteSuccess(String arg0, Object arg1) {
                System.out.println("saveAll ExecuteSuccess");
                indexFile();
            }

            @Override
            public void preExecute(String arg0, ExecutionEvent arg1) {
                System.out.println("saveAll preExecute");
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
                System.out.println("import nothandled");
            }

            @Override
            public void postExecuteFailure(String arg0, ExecutionException arg1) {
                System.out.println("import ExecuteFailure");
            }

            @Override
            public void postExecuteSuccess(String arg0, Object arg1) {
                System.out.println("import ExecuteSuccess");
                indexFile();
            }

            @Override
            public void preExecute(String arg0, ExecutionEvent arg1) {
                System.out.println("import preExecute");
            }

        });
    }

    private boolean indexFile() {
        boolean back = false;
        if (toolview != null && toolview.getActiveProject() != null) {
            try {
                ToolUtil.deleteReportToolErrors(toolview.getActiveProject());
                back = ToolUtil.indexing(toolview.getActiveProject());
            } catch (CoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            toolview.setdoIndexing(true);
            toolview.refresh();
        }
        return back;
    }
}
