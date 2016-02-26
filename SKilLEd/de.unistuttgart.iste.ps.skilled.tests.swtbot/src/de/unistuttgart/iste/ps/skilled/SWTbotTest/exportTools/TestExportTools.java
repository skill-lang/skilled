package de.unistuttgart.iste.ps.skilled.SWTbotTest.exportTools;

import java.io.File;

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
 * @author Leslie Tso
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestExportTools {

    private static SWTWorkbenchBot bot;
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
    String workspacePath = workspaceDirectory.getAbsolutePath();

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
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, create a tool for
     * the project and export it to a specific location. Then create another file and tool and export it to the same location
     * with the same name and overwrite.
     * 
     */
    @Test
    public void testExportToolsAndOverwrite() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        bot.menu("SKilLEd").menu("Tool View").click();
        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestExportTools"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestExportTools2");
        bot.button("Finish").click();

        // Create SKIlL-File
        createSKilLFile("testExport2", "TestExportTools2");
        // Editor for "testExport.skill" as active window
        bot.editorByTitle("testExport2.skill").show();
        // Populate "testExport.skill"
        bot.styledText().setText("# Test Export\nA {\n   !ignore\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();
        // Toolview window as active window
        bot.viewByTitle("ToolView").show();
        // Create tool "testExport"
        bot.toolbarButton("Create Tool").click();
        bot.textWithLabel("Put a value here.").typeText("testExport2");
        bot.checkBox("Add all Types to Tool:").click();
        bot.button("Finish").click();
        // Run Export Tools
        bot.menu("SKilLEd").menu("Export Tools").click();
        bot.comboBoxWithLabel("Select tool to export:").setSelection("testExport2");
        bot.textWithLabel("Export Location:").setText(workspacePath + File.separator + "testExportOverwrite.skill");
        bot.button("OK").click();

        // Create second SKilL-File
        createSKilLFile("testExport3", "TestExportTools2");
        // Editor for "testExport2.skill" as active window
        bot.editorByTitle("testExport3.skill").show();
        // Populate "testExport2.skill"
        bot.styledText().setText("# Test Export\nA2 {\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();
        // Toolview window as active window
        bot.viewByTitle("ToolView").show();
        // Create tool "testExport2"
        bot.toolbarButton("Create Tool").click();
        bot.textWithLabel("Put a value here.").typeText("testExport3");
        bot.checkBox("Add all Types to Tool:").click();
        bot.button("Finish").click();
        // Run Export Tools
        bot.menu("SKilLEd").menu("Export Tools").click();
        bot.comboBoxWithLabel("Select tool to export:").setSelection("testExport3");
        bot.textWithLabel("Export Location:").setText(workspacePath + File.separator + "testExportOverwrite.skill");
        bot.button("OK").click();
        try {
            bot.wait();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bot.shell("Existing File").isActive();
        bot.button("OK");
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
        // Create skill-file "testExport" in project "TestExportTools"
        bot.menu("File").menu("New").menu("SKilL file").click();
        bot.textWithLabel("&File name:").setText(name + ".skill");
        bot.button("Finish").click();
    }

    @AfterClass
    public static void sleep() {
        bot.sleep(5000);
    }

}
