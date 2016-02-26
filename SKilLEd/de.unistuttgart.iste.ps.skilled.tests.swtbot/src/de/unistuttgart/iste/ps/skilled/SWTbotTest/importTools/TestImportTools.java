package de.unistuttgart.iste.ps.skilled.SWTbotTest.importTools;

import static org.junit.Assert.assertTrue;

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
public class TestImportTools {

    private static SWTWorkbenchBot bot;
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
    String workspacePath = workspaceDirectory.getAbsolutePath();

    String classProjectPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(5,
            getClass().getProtectionDomain().getCodeSource().getLocation().toString().length() - 1);
    String resourcePath = classProjectPath + File.separator + "resources" + File.separator + "FileToBeImported.skill";

    @BeforeClass
    public static void beforeClass() throws Exception {
        bot = new SWTWorkbenchBot();
        bot.viewByTitle("Welcome").close();
        bot.resetWorkbench();
    }

    /**
     * Create a new SKilL-Project. Afterwards, import a tool to the project.
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
        bot.textWithLabel("Select tool to import:").setText(resourcePath);
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools").expand();
        bot.tree().getTreeItem("TestImportTools").getNode("FileToBeImported.skill").doubleClick();
        bot.editorByTitle("FileToBeImported.skill").show();
        bot.viewByTitle("ToolView").show();
        bot.menu("File").menu("Refresh").click();
        bot.viewByTitle("ToolView").show();
        // Select Tool "FileToBeImported" to check if it exists
        bot.list().select("FileToBeImported");
        bot.editorByTitle("FileToBeImported.skill").show();
        bot.editorByTitle("FileToBeImported.skill").close();

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
        createSKilLFile("testImport2", "TestImportTools2");
        // Editor for "testImport2.skill" as active window
        bot.editorByTitle("testImport2.skill").show();
        // Populate "testImport2.skill"
        bot.styledText().setText("# Test Import\nA {\n  !ignore\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();

        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();
        bot.textWithLabel("Select tool to import:").setText(resourcePath);
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools2");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools2").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools2").expand();
        bot.tree().getTreeItem("TestImportTools2").getNode("FileToBeImported.skill").doubleClick();
        bot.editorByTitle("FileToBeImported.skill").show();
        bot.viewByTitle("ToolView").show();
        bot.menu("File").menu("Refresh").click();
        bot.viewByTitle("ToolView").show();
        // Select Tool "FileToBeImported" to check if it exists
        bot.list().select("FileToBeImported");
        bot.editorByTitle("FileToBeImported.skill").show();
        bot.editorByTitle("FileToBeImported.skill").close();

        assertTrue(true);
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project. As the number of duplicate types > 1, an error dialog should appear and the import is canceled.
     * 
     */
    // @Test
    // public void testImportToolsWithMoreThanOneDuplicateType() {
    // SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
    //
    // bot.menu("Window").menu("Show View").menu("Project Explorer").click();
    // // Project Explorer as active window
    // bot.viewByTitle("Project Explorer").show();
    // // Create new project "TestImportTools3"
    // bot.menu("File").menu("New").menu("SKilL Project").click();
    // bot.textWithLabel("&Project name:").setText("TestImportTools3");
    // bot.button("Finish").click();
    // createSKilLFile("testImport3", "TestImportTools3");
    // // Editor for "testImport3.skill" as active window
    // bot.editorByTitle("testImport3.skill").show();
    // // Populate "testImport3.skill"
    // bot.styledText().setText("# Test Import\nA {\n i8 Test;\n}\n\nB {\n string something;\n}");
    // bot.menu("File").menu("Save").click();
    //
    // // Run Import Tools
    // bot.menu("SKilLEd").menu("Import Tools").click();
    // bot.textWithLabel("Select tool to import:").setText(resourcePath);
    // bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools3");
    // bot.button("OK").click();
    // // bot.sleep(5000);
    // // bot.shell("Too Many Duplicate Types");
    // // bot.button("OK").click();
    // }

    /**
     * Create a new SKilL-Project. Afterwards, import a tool to the project under a different name.
     * 
     */
    @Test
    public void testRenamedImportTool() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";
        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools4"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools4");
        bot.button("Finish").click();
        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();

        bot.textWithLabel("Select tool to import:").setText(resourcePath);
        bot.textWithLabel("Rename imported file to:\n(If blank, the name of the exported file will be used)")
                .setText("renamedFileToBeImported");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools4");
        bot.button("OK").click();
        bot.sleep(5000);
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
