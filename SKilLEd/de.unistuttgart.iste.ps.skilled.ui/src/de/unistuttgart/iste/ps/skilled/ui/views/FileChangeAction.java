package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

public class FileChangeAction {
	public void save() {
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
				Process p = null;
				try {
					p = Runtime.getRuntime().exec("java -jar skillls.jar -e $PATH_TO_PROJECT $PARAMS", null, new File("lib"));
					p.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try (InputStreamReader reader = new InputStreamReader(p.getInputStream())) {
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Save");
			}

		});
	}
	
	public void saveAll() {
		String commandId = "org.eclipse.ui.file.saveAll";
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
				System.out.println("SaveAll");
			}

		});
	}
	
	public void rename() {
		String commandId = "org.eclipse.ui.file.import";
		ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = service.getCommand(commandId);
		command.addExecutionListener(new IExecutionListener() {

			@Override
			public void notHandled(String arg0, NotHandledException arg1) {
				// TODO Auto-generated method stub
				System.out.println("import1");
			}

			@Override
			public void postExecuteFailure(String arg0, ExecutionException arg1) {
				// TODO Auto-generated method stub
				System.out.println("import2");
			}

			@Override
			public void postExecuteSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("import3");
			}

			@Override
			public void preExecute(String arg0, ExecutionEvent arg1) {
				// TODO Auto-generated method stub
				System.out.println("import4");
			}

		});
	}
}
