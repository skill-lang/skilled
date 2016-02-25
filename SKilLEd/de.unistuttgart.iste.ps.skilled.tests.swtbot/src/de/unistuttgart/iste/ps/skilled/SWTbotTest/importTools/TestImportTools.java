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

        bot.comboBoxWithLabel("Select tool to import:")
                .setSelection("resources" + File.separator + "FileToBeImported.skill");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools");
        bot.button("OK").click();
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project and merge duplicate type.
     * 
     */
    @Test
    public void testImportToolsAndOneDuplicateType() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools2"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools2");
        bot.button("Finish").click();
        testExportTools.createSKilLFile("testImport2", "TestImportTools2");
        // Editor for "testImport2.skill" as active window
        bot.editorByTitle("testImport2.skill").show();
        // Populate "testImport2.skill"
        bot.styledText().setText("# Test Import\nA {\n  !ignore\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();

        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();
        bot.comboBoxWithLabel("Select tool to import:")
                .setSelection("resources" + File.separator + "FileToBeImported.skill");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools2");
        bot.button("OK").click();
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project. As the number of duplicate types > 1, an error dialog should appear and the import is canceled.
     * 
     */
    @Test
    public void testImportToolsWithMoreThanOneDuplicateType() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools3");
        bot.button("Finish").click();
        testExportTools.createSKilLFile("testImport3", "TestImportTools3");
        // Editor for "testExport.skill" as active window
        bot.editorByTitle("testImport3.skill").show();
        // Populate "testExport.skill"
        bot.styledText().setText("# Test Import\nA {\n   i8 Test;\n   i8 Test2;\n}");
        bot.menu("File").menu("Save").click();

        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();
        bot.comboBoxWithLabel("Select tool to import:")
                .setSelection("resources" + File.separator + "FileToBeImportedMultiple.skill");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools3");
        bot.button("OK").click();
    }

}
