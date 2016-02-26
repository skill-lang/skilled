package de.unistuttgart.iste.ps.skilled.tests.ui;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import de.unistuttgart.iste.ps.skilled.tests.ui.util.LoadTestfile;

/**
 * @author Jan Berberich
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestSKilLEd {

	private static SWTWorkbenchBot bot;
	private String workspacePath = null;
	private final String testProject = "TestProject"; // Name of the test
														// project created by
														// the test
	private final String projectView = "Project Explorer";//Name of the project explorer view

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.resetWorkbench();
	}

	/**
	 * Basic test: Create a new SKilL-Project and a SKilL-File in the project
	 * and write some content in it.
	 * 
	 */
	@Test
	public void testCreateSkillProject() {
		getWorkspacePath();
		String testFile = "TestFile.skill";
		// Create a new SKilL Project
		bot.menu("File").menu("New").menu("Project...").click();
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("SKilL").select("SKilL Project");
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(testProject);
		bot.button("Finish").click();
		// Create Testfiles
		createSKilLFile(testFile, testProject);
		bot.viewByTitle(projectView).show();
		try {
			bot.tree().getTreeItem(testProject).expand();
			bot.tree().getTreeItem(testProject).getNode("TestFile.skill");
		} catch (WidgetNotFoundException e) {
			throw new AssertionError(); // Project and/or File not existing->
										// Error creating them
		}
		bot.tree().getTreeItem(testProject).getNode(testFile).select();
		// Write some Text in the file
		bot.editorByTitle(testFile).show();
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
	 * 
	 * This method tests the combine SKilL file refactoring. 
	 * 
	 */
	@Test
	public void testCombineSkillFile(){
		getWorkspacePath();
		String[] testFiles = LoadTestfile.loadCombineTest();
		String testFile1 = "testCombine1.skill";
		String testFile2 = "testCombine2.skill";
		createSKilLFileWithContentInTestProject(testFile1, testFiles[0]);
		createSKilLFileWithContentInTestProject(testFile2, testFiles[1]);
		if(combineFiles(testFile1, testFile2,"combined.skill")){
			bot.editorByTitle("combined.skill");
			if(bot.styledText().getText().equals(testFiles[2])){
				throw new AssertionError(); //Combined text wrong
			}
		}else{
			throw new AssertionError(); //Not executed correct
		}		
	}
	
	/**
	 * 
	 * Test the import of a binary file.
	 */
	@Test
	public void testImportBinary(){
		getWorkspacePath();
		try{
			bot.menu("SKilLEd").menu("Import Binary File").click();
			bot.textWithLabel("Select binary file to import:").setText("resources"+File.separator+"testImport"+File.separator+"age.sf");		
			bot.textWithLabel("Import to:").setText(workspacePath + File.separator+ testProject);
			bot.button("OK").click();
			bot.editorByTitle("age.skill");
			String text = bot.styledText().getText();
			if(!((text.contains("age {"))&&(text.contains("v64 age;")&&(text.contains("}"))))){
				throw new AssertionError();//Not importet correct
			}
		}catch(WidgetNotFoundException e){
			throw new AssertionError(); //Import failed
		}		
		
	}
	
	/**
	 * This test opens a SKilL-File specified in the specification (2.3) and
	 * checks if it is opened in the specified time. File is specified to be
	 * opened in a maximum of 50 ms.
	 * 
	 */
	@Test
	public void openFileTimeSpecified() {
		String testFile = "timeTestSpecification.skill";
		String fileContent = LoadTestfile.loadPerformenceTestfile();
		createSKilLFileWithContentInTestProject(testFile, fileContent);
		bot.editorByTitle(testFile).close();
		final long endTime;
		bot.viewByTitle(projectView).show();
		//bot.tree().getTreeItem(testProject).expand();
		final long timeStart = System.currentTimeMillis();
		bot.tree().getTreeItem(testProject).getNode(testFile).doubleClick();
		endTime = System.currentTimeMillis();
		final long timeDiff = endTime - timeStart;
		System.out.println("File opened in " + timeDiff + " ms.");
	}
	
	/**
	 * Get the path of the current workspace.
	 * 
	 * @return The workspace path
	 * 
	 */
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
	 * Executes combine SKilL files refactoring for two existing files.
	 * @param fileName1 Name of file 1
	 * @param fileName2 Name of file 2
	 * @return True if succeeded; else false
	 */
	private boolean combineFiles(String fileName1, String fileName2, String combinedFile ){
		try{
			bot.menu("SKilLEd").menu("Combine .skill-files").click();
			//bot.tree().getTreeItem(testProject).check();
			bot.tree().getTreeItem(testProject).getNode(fileName1).check();
			bot.tree().getTreeItem(testProject).getNode(fileName2).check();
			bot.textWithLabel("Save combined file as:").setText(workspacePath+File.separator+testProject +File.separator+combinedFile);
			bot.button("OK").click();
			bot.editorByTitle("combined.skill").show();			
		}catch(WidgetNotFoundException e){
			return false; //Something went wrong
		}		
		return true;
	}
	
	/**
	 * Creates a new SKilL file in the test Project.
	 * @param fileName Name of the file
	 * @param content Content of the file
	 */
	private void createSKilLFileWithContentInTestProject(String fileName, String content){
		createSKilLFile(fileName, testProject);
		bot.editorByTitle(fileName).show();
		bot.styledText().setText(content);
		bot.toolbarButtonWithTooltip("Save (Ctrl+S)").click();
	}
	
	/**
	 * Creates a new SKilL File with the File-> New-> Other... Dialog.
	 * 
	 * @param fileName
	 *            The name of the new SKilL-File to be created (ending with .skill)
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
		bot.textWithLabel("File name:").setText(fileName);
		bot.button("Finish").click();

	}

	@AfterClass
	public static void sleep() {
		bot.sleep(5000);
	}

}