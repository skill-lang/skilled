package de.unistuttgart.iste.ps.skilled.SWTbotTest.exportTools;

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
 * This class executes SWTbot tests for the "Export Tool" function.
 * 
 * @author Leslie Tso
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestExportTools {

    private static SWTWorkbenchBot bot;
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
    String workspacePath = workspaceDirectory.getAbsolutePath();
    String toolToExportText;
    String exportedToolText;

    String classProjectPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(5,
            getClass().getProtectionDomain().getCodeSource().getLocation().toString().length() - 1);
    String resourcePath = classProjectPath + File.separator + "resources" + File.separator + "CompareWithExportedTool.skill";

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
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, create a tool for
     * the project and export it to a specific location.
     * 
     */
    @Test
    public void testExportTools() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        // bot.menu("SKilLEd").menu("Tool View").click();
        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestExportTools"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestExportTools");
        bot.button("Finish").click();
        createSKilLFile("testExport", "TestExportTools");
        // Editor for "testExport.skill" as active window
        bot.editorByTitle("testExport.skill").show();
        // Populate "testExport.skill"
        bot.styledText().setText("# Test Export\nA {\n  !ignore\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();

        // Toolview window as active window
        bot.viewByTitle("ToolView").show();
        // Create tool "testExport"
        bot.toolbarButton("Create Tool").click();
        bot.textWithLabel("Put a value here.").typeText("testExport");
        bot.checkBox("Add all Types to Tool:").click();
        bot.button("Finish").click();
        // Run Export Tools
        bot.menu("SKilLEd").menu("Export Tools").click();
        bot.comboBoxWithLabel("Select tool to export:").setSelection("testExport");
        bot.textWithLabel("Export Location:").setText(workspacePath + File.separator + "testExport.skill");
        bot.button("OK").click();

        // Convert files to strings
        try {
            toolToExportText = new String(Files.readAllBytes(Paths.get(resourcePath)));
            exportedToolText = new String(
                    Files.readAllBytes(Paths.get(workspacePath + File.separator + "testExport.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Remove all whitespaces so that they do not affect the comparision
        toolToExportText = toolToExportText.replaceAll("\\s+", "");
        exportedToolText = exportedToolText.replaceAll("\\s+", "");

        // Check if the contents of the exported file are correct
        assertTrue(toolToExportText.equals(exportedToolText));

    }

    /**
     * Creates a new SKilL File with the File-> New-> Other... Dialog.
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
