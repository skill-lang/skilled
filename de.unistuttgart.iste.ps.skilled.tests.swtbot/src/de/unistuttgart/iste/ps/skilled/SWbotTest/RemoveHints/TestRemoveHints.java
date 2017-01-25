package de.unistuttgart.iste.ps.skilled.SWbotTest.RemoveHints;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * This class executes SWTbot tests for the "Remove Hints" and "Remove Hints from Project" function.
 * 
 * @author Leslie Tso
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestRemoveHints {

    private static SWTWorkbenchBot bot;
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
    String workspacePath = workspaceDirectory.getAbsolutePath();

    private String fileAfterRemoveHints;
    private String fileAfterRemoveHintsChecker;
    private String fileAfterRemoveHintsProject1;
    private String fileAfterRemoveHintsProject1Checker;
    private String fileAfterRemoveHintsProject2;
    private String fileAfterRemoveHintsProject2Checker;

    /**
     * This class is responsible for creating the SWTWorkbenchBot() for every test and closing the "Welcome" window that
     * shows up the first time the IDE is started
     * 
     * @throws Exception
     */
    @BeforeClass
    public static void beforeClass() throws Exception {
        bot = new SWTWorkbenchBot();
        bot.viewByTitle("Welcome").close();
        bot.resetWorkbench();
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, remove all hints
     * from the file.
     * 
     */
    @Test
    public void testRemoveHints() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        // bot.menu("SKilLEd").menu("Tool View").click();
        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestRemoveHints"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestRemoveHints");
        bot.button("Finish").click();
        createSKilLFile("testRemoveHints", "TestRemoveHints");
        // Editor for "testRemoveHints.skill" as active window
        bot.editorByTitle("testRemoveHints.skill").show();
        // Populate "testRemoveHints.skill"
        bot.styledText().setText(
                "# Test Remove Hints\n/** A horrifying mutated creature */\n!flat\n!unique\n!pure\n!monotone\nMonster {\n   /*\n   * A Monster has a fixed set of fangs but they can\n   * rot due to radiation\n   */\n   const i8 fangs = 14;\n    /*\n   * Nobody cares about a monsters background\n   */\n   !ignore\n   string backgroundStory;\n}");
        bot.menu("File").menu("Save").click();

        // Run Remove Hints
        bot.menu("SKilLEd").menu("Remove all hints in this file").click();

        // Convert files to strings
        try {
            fileAfterRemoveHints = new String(Files.readAllBytes(Paths
                    .get(workspacePath + File.separator + "TestRemoveHints" + File.separator + "testRemoveHints.skill")));
            fileAfterRemoveHintsChecker = new String(
                    Files.readAllBytes(Paths.get("resources" + File.separator + "testRemoveHintsChecker.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Remove all whitespaces so that they do not affect the comparision
        fileAfterRemoveHints = fileAfterRemoveHints.replaceAll("\\s+", "");
        fileAfterRemoveHintsChecker = fileAfterRemoveHintsChecker.replaceAll("\\s+", "");

        // Check if the contents of the files after hint removal are correct
        assertTrue(fileAfterRemoveHints.equals(fileAfterRemoveHintsChecker));

    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, remove all hints
     * from the file.
     * 
     */
    @Test
    public void testRemoveHintsFromProject() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        // bot.menu("SKilLEd").menu("Tool View").click();
        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestRemoveHintsProject"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestRemoveHintsProject");
        bot.button("Finish").click();
        createSKilLFile("testRemoveHintsProject1", "TestRemoveHintsProject");
        // Editor for "testRemoveHintsProject1.skill" as active window
        bot.editorByTitle("testRemoveHintsProject1.skill").show();
        // Populate "testRemoveHintsProject1.skill"
        bot.styledText().setText(
                "# Test Remove Hints Project 1\n!flat\n!unique\n!pure\n!monotone\nMonster {\n   !ignore\n   string backgroundStory;\n}");
        bot.menu("File").menu("Save").click();
        createSKilLFile("testRemoveHintsProject2", "TestRemoveHintsProject");
        // Editor for "testRemoveHintsProject2.skill" as active window
        bot.editorByTitle("testRemoveHintsProject2.skill").show();
        // Populate "testRemoveHintsProject2.skill"
        bot.styledText().setText(
                "# Test Remove Hints Project 2\n!flat\n!unique\n!pure\n!monotone\nMonster {\n   !ignore\n   string backgroundStory;\n}");
        bot.menu("File").menu("Save").click();

        // Run Remove Hints
        bot.menu("SKilLEd").menu("Remove all hints in this project").click();

        // Convert files to strings
        try {
            fileAfterRemoveHintsProject1 = new String(Files.readAllBytes(Paths.get(workspacePath + File.separator
                    + "TestRemoveHintsProject" + File.separator + "testRemoveHintsProject1.skill")));
            fileAfterRemoveHintsProject1Checker = new String(
                    Files.readAllBytes(Paths.get("resources" + File.separator + "testRemoveHintsProject1Checker.skill")));
            fileAfterRemoveHintsProject2 = new String(Files.readAllBytes(Paths.get(workspacePath + File.separator
                    + "TestRemoveHintsProject" + File.separator + "testRemoveHintsProject2.skill")));
            fileAfterRemoveHintsProject2Checker = new String(
                    Files.readAllBytes(Paths.get("resources" + File.separator + "testRemoveHintsProject2Checker.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Remove all whitespaces so that they do not affect the comparision
        fileAfterRemoveHintsProject1 = fileAfterRemoveHintsProject1.replaceAll("\\s+", "");
        fileAfterRemoveHintsProject1Checker = fileAfterRemoveHintsProject1Checker.replaceAll("\\s+", "");
        fileAfterRemoveHintsProject2 = fileAfterRemoveHintsProject2.replaceAll("\\s+", "");
        fileAfterRemoveHintsProject2Checker = fileAfterRemoveHintsProject2Checker.replaceAll("\\s+", "");

        // Check if the contents of the files after hint removal are correct
        assertTrue(fileAfterRemoveHintsProject1.equals(fileAfterRemoveHintsProject1Checker));
        assertTrue(fileAfterRemoveHintsProject2.equals(fileAfterRemoveHintsProject2Checker));
    }

    /**
     * Creates a new SKilL File with the File-> New-> SKilL File Dialog.
     * 
     * @param name
     *            The name of the new SKilL-File to be created
     * @param project
     *            The name of the project the file will be in
     */
    @SuppressWarnings("static-method")
    public void createSKilLFile(String name, String project) {
        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem(project).select();
        bot.menu("File").menu("New").menu("SKilL file").click();
        bot.textWithLabel("&File name:").setText(name + ".skill");
        bot.button("Finish").click();
    }

    /**
     * This class releases the resources used in beforeClass()
     */
    @AfterClass
    public static void sleep() {
        bot.sleep(5000);
    }

}
