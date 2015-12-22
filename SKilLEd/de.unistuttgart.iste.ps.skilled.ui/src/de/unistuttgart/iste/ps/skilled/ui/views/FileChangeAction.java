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

	// TODO: Je nachdem was Armin aussagt werden anstatt seine skillls.jar
	// aufzurufen direkt seine Methoden verwendet. Diese Methoden müssen
	// aufgerufen werden um die .skills Datei zu erzeugen.

	public void save() {
		String commandId = "org.eclipse.ui.file.save";
		ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
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
			}

			@Override
			public void preExecute(String arg0, ExecutionEvent arg1) {
				// Process p = null;
				// try {
				// p = Runtime.getRuntime().exec("java -jar skillls.jar -e
				// $PATH_TO_PROJECT $PARAMS", null,
				// new File("lib"));
				// p.waitFor();
				// } catch (IOException e) {
				// e.printStackTrace();
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// try (InputStreamReader reader = new
				// InputStreamReader(p.getInputStream())) {
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				String pathToProject = "C:\\Users\\nicoa\\Documents\\skilled-skilled.code2\\runtime-New_configuration\\Test";
				String parameter = "--help";
				System.out.println(new String[] { "-e", pathToProject, parameter });
				de.unistuttgart.iste.ps.skillls.main.MainClass.main(new String[] { "-e", pathToProject, parameter });
				System.out.println("save preExecute");
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
				System.out.println("saveAll nothandled");
			}

			@Override
			public void postExecuteFailure(String arg0, ExecutionException arg1) {
				System.out.println("saveAll ExecuteFailure");
			}

			@Override
			public void postExecuteSuccess(String arg0, Object arg1) {
				System.out.println("saveAll ExecuteSuccess");
			}

			@Override
			public void preExecute(String arg0, ExecutionEvent arg1) {
				System.out.println("saveAll preExecute");
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
				System.out.println("import nothandled");
			}

			@Override
			public void postExecuteFailure(String arg0, ExecutionException arg1) {
				System.out.println("import ExecuteFailure");
			}

			@Override
			public void postExecuteSuccess(String arg0, Object arg1) {
				System.out.println("import ExecuteSuccess");
			}

			@Override
			public void preExecute(String arg0, ExecutionEvent arg1) {
				System.out.println("import preExecute");
			}

		});
	}
}
