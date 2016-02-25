package de.unistuttgart.iste.ps.skilled.SWTbotTest.importTools;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.unistuttgart.iste.ps.skilled.SWTbotTest.exportTools.TestExportTools;


/**
 * @author Leslie Tso
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestImportTools {

    private static SWTWorkbenchBot bot;
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
    String workspacePath = workspaceDirectory.getAbsolutePath();

    TestExportTools testExportTools = new TestExportTools();

    @BeforeClass
    public static void beforeClass() throws Exception {
        bot = new SWTWorkbenchBot();
        bot.viewByTitle("Welcome").close();
        bot.resetWorkbench();
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project.
     * 
     */
    @Test
    public void testImportTools() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools");
        bot.button("Finish").click();
        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();

        // TODO - IMPORT LOCATION
        bot.comboBoxWithLabel("Select tool to import:").setSelection(IMPORTLOCATION);
        bot.textWithLabel("Import to Project:").setText(workspacePath + "/TestImportTools");
        bot.button("OK").click();
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project and merge duplicate types.
     * 
     */
    @Test
    public void testImportToolsAndMergeDuplicateTypes() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools");
        bot.button("Finish").click();
        testExportTools.createSKilLFile("testImport", "TestImportTools");
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
        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();

        // TODO - IMPORT LOCATION
        bot.comboBoxWithLabel("Select tool to import:").setSelection(IMPORTLOCATION);
        bot.textWithLabel("Import to Project:").setText(workspacePath + "/TestImportTools");
        bot.button("OK").click();
    }

}
