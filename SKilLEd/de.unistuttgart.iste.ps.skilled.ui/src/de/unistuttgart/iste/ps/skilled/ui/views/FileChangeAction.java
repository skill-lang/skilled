package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillException;
import de.ust.skill.common.java.api.SkillFile.Mode;

/**
 * Handle the ChangeActions of files in the editor.
 * 
 * @author Nico Rusam
 *
 */
public class FileChangeAction {

	ToolView tv = null;

	public FileChangeAction(ToolView tv) {
		this.tv = tv;
	}

	/**
	 * Handle save-action of active file in the editor.
	 */
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
				indexFile();
			}

			@Override
			public void preExecute(String arg0, ExecutionEvent arg1) {
				System.out.println("import preExecute");
			}

		});
	}

	private boolean indexFile() {
		if (tv != null && tv.getActiveProject() != null) {
			// if (ToolUtil.cleanup(tv.getActiveProject())) {
			// tv.refresh();
			// return true;
			// } else {
			// return false;
			// }
		}
		return false;
	}
}
