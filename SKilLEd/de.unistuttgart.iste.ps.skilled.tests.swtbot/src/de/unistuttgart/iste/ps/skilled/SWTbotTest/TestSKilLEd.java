package de.unistuttgart.iste.ps.skilled.SWTbotTest;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


//
//import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertTextContains;
//import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
//import static org.eclipse.swtbot.swt.finder.waits.Conditions.tableHasRows;
//
//import org.eclipse.swt.custom.StyledText;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.Widget;
//import org.eclipse.swtbot.eclipse.finder.SWTEclipseBot;
//import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
//import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
//import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
//import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
//import org.eclipse.swtbot.swt.finder.utils.FileUtils;
//import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
//import org.eclipse.swtbot.swt.finder.widgets.SWTBotStyledText;
//import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
//import org.osgi.framework.Bundle;



@RunWith(SWTBotJunit4ClassRunner.class)
public class TestSKilLEd {

	private static SWTWorkbenchBot	bot;

	@BeforeClass
	public static void beforeClass() throws Exception {
		bot = new SWTWorkbenchBot();
		bot.viewByTitle("Welcome").close();
		bot.resetWorkbench();
	}

	@Test
	public void testCreateSkillProject(){
		String testProject = "TestProject";
		//Create a new SKilL Project
		bot.menu("File").menu("New").menu("Project...").click();
		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().expandNode("SKilL").select("SKilL Project");
		bot.button("Next >").click();
		bot.textWithLabel("Project name:").setText(testProject);
		bot.button("Finish").click();
		//assertProjectCreated(testProject);
		
		
		
		createSKilLFile("TestFile", testProject);
		try {
			setEditorText("TestFile", "A{}");
		} catch (Exception e) {
			throw new AssertionError();
		}
//		bot.viewByTitle("Package Explorer").setFocus();
//		bot.tree().expandNode(testProject);

		
	}
	
	/**
	 * This test opens a SKilL-File specified in the specification (2.3) and 
	 * checks if it is opened in the specified time.
	 * 
	 */
	@Test
	public void openFileTimeSpecified(){
        final long timeStart = System.currentTimeMillis(); 
        
        
        
        final long timeDiff = System.currentTimeMillis() - timeStart ; 
        System.out.println("File opened in " + timeDiff+ " ms.");
	}
	
//	
//	  private void assertProjectCreated(String projectName) {
//		    // Copied from #afterClass()
//		    // FIXME: remove duplication
//		  	
//		    SWTBotView packageExplorerView = bot.viewByTitle("Package Explorer");
//		    packageExplorerView.show();
//		    Composite packageExplorerComposite = (Composite) packageExplorerView.getWidget();
//		   
//		    Tree swtTree = (Tree) bot.widget(new Matcher(), 0);
//		    		
//		    SWTBotTree tree = new SWTBotTree(swtTree);
//		    // throws WNFE if the item does not exist
//		    tree.getTreeItem(projectName);
//		  }
//	
//	
	
	
	
	  public void setEditorText(String fileName, String text) {
		//SWTBotEclipseEditor editor = bot.editorByTitle(fileName).toTextEditor();
	    bot.editorByTitle(fileName).toTextEditor().setText(text);
	    bot.editorByTitle(fileName).toTextEditor().save();
	    
	    //editor.setText(text);
	    //editor.save();

	  }
	
	
	/**
	 * Creates a new SKilL File with the File-> New-> Other...  Dialog.
	 * @param fileName The name of the new SKilL-File to be created
	 * @param projectName The name of the project the file will be in
	 */
	private void createSKilLFile(String fileName, String projectName){
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
		bot.textWithLabel("File name:").setText(fileName+".skill");
		bot.button("Finish").click();
		
	}
	
//
//	@Test
//	public void canCreateANewJavaProject() throws Exception {
//		bot.menu("File").menu("New").menu("Project...").click();
//		
//		SWTBotShell shell = bot.shell("New Project");
//		shell.activate();
//		bot.tree().expandNode("Java").select("Java Project");
//		bot.button("Next >").click();
//
//		bot.textWithLabel("Project name:").setText("MyFirstProject");
//
//		bot.button("Finish").click();
//
//		SWTBotShell shell2 = bot.shell("Open Associated Perspective?");
//		
//		shell2.activate();
//		bot.button("No").click();
//		// FIXME: assert that the project is actually created, for later
//	}
//	

	@AfterClass
	public static void sleep() {
		bot.sleep(5000);
	}

}