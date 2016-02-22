package de.unistuttgart.iste.ps.skilled.SWTbotTest;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader;

/**
 * @author Jan Berberich
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestSKilLEd {

	private static SWTWorkbenchBot bot;
	private String workspacePath = null;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.resetWorkbench();
	}

	/**
	 * Basic test: Create a new SKilL-Project and a SKilL-File in the project and write some content in it
	 * 
	 */
	@Test
	public void testCreateSkillProject() {
		String testProject = "TestProject";
		// getWorkspacePath();
		// Create a new SKilL Project
		bot.menu("File").menu("New").menu("Project...").click();
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("SKilL").select("SKilL Project");
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(testProject);
		bot.button("Finish").click();
		createSKilLFile("TestFile", testProject);
		bot.viewByTitle("Project Explorer").show();
		try {
			bot.tree().getTreeItem(testProject).expand();
			bot.tree().getTreeItem("TestProject").getNode("TestFile.skill");
		} catch (WidgetNotFoundException e) {
			throw new AssertionError(); // Project and/or File not existing->
										// Error creating them
		}
		bot.tree().getTreeItem(testProject).getNode("TestFile.skill").select();
		// Write some Text in the file
		bot.editorByTitle("TestFile.skill").show();
		bot.styledText().setText("A");
		bot.styledText().setText("A{ i8 a	}");
		bot.styledText().setText("A{i8 a; }");
		bot.styledText().setText("A{i8 a; \n	i16 b	}");
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
		File workspace = new File(workspacePath);
		if (workspace.exists() && workspace.isDirectory()) {
			for (File f : workspace.listFiles()) {
				System.out.println("File: " + f.getName());
			}
		}
		try {
			setEditorText("TestFile", "A{}");
		} catch (Exception e) {
			throw new AssertionError();
		}
	}

	/**
	 * This test opens a SKilL-File specified in the specification (2.3) and
	 * checks if it is opened in the specified time.
	 * 
	 */
	@Test
	public void openFileTimeSpecified() {
		//First generate a File with the specified Filecontent
		//String fileContent = FileLoader.loadFile("TestFileSpecification");
		//System.out.println("Content: "+fileContent);
		
		final long timeStart = System.currentTimeMillis();

		final long timeDiff = System.currentTimeMillis() - timeStart;
		System.out.println("File opened in " + timeDiff + " ms.");
	}

	public void setEditorText(String fileName, String text) {
		// SWTBotEclipseEditor editor =
		// bot.editorByTitle(fileName).toTextEditor();
		bot.editorByTitle(fileName).toTextEditor().setText(text);
		bot.editorByTitle(fileName).toTextEditor().save();

		// editor.setText(text);
		// editor.save();

	}

	private String getWorkspacePath() {
		if (workspacePath == null) {
			bot.menu("File").menu("Switch Workspace").menu("Other...").click();
			SWTBotShell shell = bot.shell("Workspace Launcher");
			shell.activate();

			workspacePath = bot.browserWithLabel("Workspace:").getText();

			System.out.println("Label: " + workspacePath);
			bot.button("Cancel").click();
		}
		return workspacePath;
	}

	/**
	 * Creates a new SKilL File with the File-> New-> Other... Dialog.
	 * 
	 * @param fileName
	 *            The name of the new SKilL-File to be created
	 * @param projectName
	 *            The name of the project the file will be in
	 */
	private void createSKilLFile(String fileName, String projectName) {
		bot.menu("File").menu("New").menu("Other...").click();
		SWTBotShell shell = bot.shell("New");
		shell.activate();
		bot.tree().expandNode("SKilL").select("SKilL file");
		bot.button("Next >").click();
		bot.button("Browse...").click();
		SWTBotShell shell2 = bot.shell("Folder Selection");
		shell2.activate();
		bot.tree().select(projectName);

		bot.button("OK").click();
		bot.textWithLabel("File name:").setText(fileName + ".skill");
		bot.button("Finish").click();

	}

	@AfterClass
	public static void sleep() {
		bot.sleep(5000);
	}

}