package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

public class Test {
	public void tt() {
		//TODO: Add saveall und Rename
		String commandId = "org.eclipse.ui.file.save";
		ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = service.getCommand(commandId);
		command.addExecutionListener(new IExecutionListener() {

			@Override
			public void notHandled(String arg0, NotHandledException arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postExecuteFailure(String arg0, ExecutionException arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postExecuteSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preExecute(String arg0, ExecutionEvent arg1) {
				// TODO Auto-generated method stub
				System.out.println("Save");
			}

		});
	}
}
