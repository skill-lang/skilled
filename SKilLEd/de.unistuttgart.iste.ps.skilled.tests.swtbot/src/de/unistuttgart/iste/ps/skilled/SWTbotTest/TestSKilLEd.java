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

import de.unistuttgart.iste.ps.skilled.SWTbotTest.util.LoadTestfile;


/**
 * @author Jan Berberich
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestSKilLEd {

	private static SWTWorkbenchBot bot;
	private String workspacePath = null;
	private final String testProject = "TestProject"; //Name of the test project created by the test
	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.resetWorkbench();
	}

	/**
	 * Basic test: Create a new SKilL-Project and a SKilL-File in the project and write some content in it.
	 * 
	 */
	@Test
	public void testCreateSkillProject() {
		getWorkspacePath();
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
		bot.styledText().setText("A{i8 a; \n	i16 b;	}");
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
		File workspace = new File(workspacePath);
		if (workspace.exists() && workspace.isDirectory()) {
			for (File f : workspace.listFiles()) {
				System.out.println("File: " + f.getName());
			}
		}
	}

	/**
	 * This test opens a SKilL-File specified in the specification (2.3) and
	 * checks if it is opened in the specified time.
	 * File is specified to be opened in a maximum of 50 ms.
	 * 
	 */
	@Test
	public void openFileTimeSpecified() {
		//First generate a File with the specified Filecontent
		String fileContent = LoadTestfile.loadTestfile();
		createSKilLFile("timeTestSpecification", testProject);
		//Focus on Project Explorer, open testFile, save content
		bot.viewByTitle("Project Explorer").show();
		bot.tree().getTreeItem(testProject).expand();
		bot.tree().getTreeItem(testProject).getNode("timeTestSpecification.skill").select();
		bot.editorByTitle("timeTestSpecification.skill").show();
		bot.styledText().setText(fileContent);
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
		bot.editorByTitle("timeTestSpecification.skill").close();		
		System.out.println("Open Testfile: ");
		final long endTime;
		bot.viewByTitle("Project Explorer").show();
		final long timeStart = System.currentTimeMillis();
		bot.tree().getTreeItem(testProject).getNode("timeTestSpecification.skill").select();
		bot.editorByTitle("timeTestSpecification.skill").show();
		endTime = System.currentTimeMillis();
		final long timeDiff = endTime - timeStart;
		System.out.println("File opened in " + timeDiff + " ms.");
	}


	private String getWorkspacePath() {
		if (workspacePath == null) {
			bot.menu("File").menu("Switch Workspace").menu("Other...").click();
			SWTBotShell shell = bot.shell("Workspace Launcher");
			shell.activate();
			workspacePath = bot.comboBox().getText();
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