package de.unistuttgart.iste.ps.skilled.SWTbotTest.importTools;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.keyboard.Keyboard;
import org.eclipse.swtbot.swt.finder.keyboard.KeyboardFactory;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * This class executes SWTbot tests for the "Import Tool" function.
 * 
 * @author Leslie Tso
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class TestImportTools {

    private static SWTWorkbenchBot bot;
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    File workspaceDirectory = workspace.getRoot().getLocation().toFile();
    String workspacePath = workspaceDirectory.getAbsolutePath();

    // String classProjectPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(5,
    // getClass().getProtectionDomain().getCodeSource().getLocation().toString().length() - 1);
    // String resourcePathGenericFileToBeImported = classProjectPath + File.separator + "resources" + File.separator
    // + "FileToBeImported.skill";
    // String resourcePath = classProjectPath + File.separator + "resources" + File.separator;

    String resourcePathGenericFileToBeImported = "resources" + File.separator + "FileToBeImported.skill";
    String resourcePath = "resources" + File.separator;

    Keyboard key = KeyboardFactory.getSWTKeyboard();
    String originalImportedFile;
    String FileInProjectAfterImport;
    private String newtestImport;
    private String newFileToBeImportedAfterImport;
    private String newtestImportChecker;
    private String newFileToBeImportedAfterImportChecker;

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
        bot.textWithLabel("Select tool to import:").setText(resourcePathGenericFileToBeImported);
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools").expand();
        bot.tree().getTreeItem("TestImportTools").getNode("FileToBeImported.skill").doubleClick();
        bot.editorByTitle("FileToBeImported.skill").show();

        bot.viewByTitle("ToolView").show();
        // Refresh ToolView window
        key.pressShortcut(Keystrokes.F5);
        // Select Tool "FileToBeImported" to check if it exists
        bot.list().select("FileToBeImported");
        bot.editorByTitle("FileToBeImported.skill").show();
        bot.editorByTitle("FileToBeImported.skill").close();

        try {
            originalImportedFile = new String(Files.readAllBytes(Paths.get(resourcePathGenericFileToBeImported)));
            FileInProjectAfterImport = new String(Files.readAllBytes(Paths
                    .get(workspacePath + File.separator + "TestImportTools" + File.separator + "FileToBeImported.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Remove all whitespaces so that they do not affect the comparision
        originalImportedFile = originalImportedFile.replaceAll("\\s+", "");
        FileInProjectAfterImport = FileInProjectAfterImport.replaceAll("\\s+", "");

        // Check if the contents of the imported file are correct
        assertTrue(originalImportedFile.equals(FileInProjectAfterImport));

    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project and merge duplicate user type.
     * 
     */
    @Test
    public void testImportToolsAndOneDuplicateUserType() {
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
        bot.textWithLabel("Select tool to import:").setText(resourcePathGenericFileToBeImported);
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools2");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools2").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools2").expand();
        bot.tree().getTreeItem("TestImportTools2").getNode("FileToBeImported.skill").doubleClick();
        bot.editorByTitle("FileToBeImported.skill").show();

        bot.viewByTitle("ToolView").show();
        // Refresh ToolView window
        key.pressShortcut(Keystrokes.F5);
        // Select Tool "FileToBeImported" to check if it exists
        bot.list().select("FileToBeImported");
        bot.editorByTitle("FileToBeImported.skill").show();
        bot.editorByTitle("FileToBeImported.skill").close();

        try {
            newtestImport = new String(Files.readAllBytes(
                    Paths.get(workspacePath + File.separator + "TestImportTools2" + File.separator + "testImport2.skill")));
            newtestImportChecker = new String(Files.readAllBytes(
                    Paths.get(resourcePath + "testImportToolsAndOneDuplicateUserType - new testImport2.skill")));

            newFileToBeImportedAfterImport = new String(Files.readAllBytes(Paths
                    .get(workspacePath + File.separator + "TestImportTools2" + File.separator + "FileToBeImported.skill")));
            newFileToBeImportedAfterImportChecker = new String(Files.readAllBytes(Paths
                    .get(resourcePath + "testImportToolsAndOneDuplicateUserType - FileToBeImported After Import.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        checkIfFileContentsAreTheSame();
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project and show that different kinds of types (i.e. user, enum, typedef, etc.) with the same name are not merged
     * together and do not count as duplicates.
     * 
     * While it is normally not allowed to have multiple types with the same name irrespective of type, we allow it in this
     * case to reduce the amount of redundant tests.
     * 
     */
    @Test
    public void testImportToolsWithDifferentTypesButSameName() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools3"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools3");
        bot.button("Finish").click();
        createSKilLFile("testImport3", "TestImportTools3");
        // Editor for "testImport3.skill" as active window
        bot.editorByTitle("testImport3.skill").show();
        // Populate "testImport3.skill"
        bot.styledText().setText("# Test Import\nA {\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();

        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();
        bot.textWithLabel("Select tool to import:").setText(
                resourcePath + "testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import.skill");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools3");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools3").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools3").expand();
        bot.tree().getTreeItem("TestImportTools3")
                .getNode("testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import.skill")
                .doubleClick();
        bot.editorByTitle("testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import.skill").show();

        bot.viewByTitle("ToolView").show();
        // Refresh ToolView window
        key.pressShortcut(Keystrokes.F5);
        // Select Tool "testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import" to check if it exists
        bot.list().select("testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import");
        bot.editorByTitle("testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import.skill").show();
        bot.editorByTitle("testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import.skill").close();

        try {
            newtestImport = new String(Files.readAllBytes(
                    Paths.get(workspacePath + File.separator + "TestImportTools3" + File.separator + "testImport3.skill")));
            newtestImportChecker = new String(Files.readAllBytes(
                    Paths.get(resourcePath + "testImportToolsWithDifferentTypesButSameName - new testImport3.skill")));

            newFileToBeImportedAfterImport = new String(
                    Files.readAllBytes(Paths.get(workspacePath + File.separator + "TestImportTools3" + File.separator
                            + "testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import.skill")));
            newFileToBeImportedAfterImportChecker = new String(Files.readAllBytes(Paths.get(
                    resourcePath + "testImportToolsWithDifferentTypesButSameName - FileToBeImported Before Import.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        checkIfFileContentsAreTheSame();
    }

    /**
     * Create a new SKilL-Project. Afterwards, import a tool to the project under a different name.
     * 
     */
    @Test
    public void testRenameImportTool() {
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

        bot.textWithLabel("Select tool to import:").setText(resourcePathGenericFileToBeImported);
        bot.textWithLabel("Rename imported file to:\n(If blank, the name of the imported file will be used)")
                .setText("renamedFileToBeImported");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools4");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools4").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools4").expand();
        bot.tree().getTreeItem("TestImportTools4").getNode("renamedFileToBeImported.skill").doubleClick();
        bot.editorByTitle("renamedFileToBeImported.skill").show();

        bot.viewByTitle("ToolView").show();
        // Refresh ToolView window
        key.pressShortcut(Keystrokes.F5);
        // Select Tool "FileToBeImported" to check if it exists
        bot.list().select("renamedFileToBeImported");
        bot.editorByTitle("renamedFileToBeImported.skill").show();
        bot.editorByTitle("renamedFileToBeImported.skill").close();

        try {
            originalImportedFile = new String(Files.readAllBytes(Paths.get(resourcePathGenericFileToBeImported)));
            FileInProjectAfterImport = new String(Files.readAllBytes(Paths.get(workspacePath + File.separator
                    + "TestImportTools4" + File.separator + "renamedFileToBeImported.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Remove all whitespaces so that they do not affect the comparision
        originalImportedFile = originalImportedFile.replaceAll("\\s+", "");
        FileInProjectAfterImport = FileInProjectAfterImport.replaceAll("\\s+", "");

        // Check if the contents of the imported file are correct
        assertTrue(originalImportedFile.equals(FileInProjectAfterImport));
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project and merge duplicate interface type.
     * 
     */
    @Test
    public void testImportToolsAndOneDuplicateInterfaceType() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools5"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools5");
        bot.button("Finish").click();
        createSKilLFile("testImport5", "TestImportTools5");
        // Editor for "testImport5.skill" as active window
        bot.editorByTitle("testImport5.skill").show();
        // Populate "testImport5.skill"
        bot.styledText().setText("# Test Import\ninterface A {\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();

        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();
        bot.textWithLabel("Select tool to import:").setText(
                resourcePath + "testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import.skill");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools5");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools5").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools5").expand();
        bot.tree().getTreeItem("TestImportTools5")
                .getNode("testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import.skill").doubleClick();
        bot.editorByTitle("testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import.skill").show();

        bot.viewByTitle("ToolView").show();
        // Refresh ToolView window
        key.pressShortcut(Keystrokes.F5);
        // Select Tool "testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import" to check if it exists
        bot.list().select("testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import");
        bot.editorByTitle("testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import.skill").show();
        bot.editorByTitle("testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import.skill").close();
        try {
            newtestImport = new String(Files.readAllBytes(
                    Paths.get(workspacePath + File.separator + "TestImportTools5" + File.separator + "testImport5.skill")));
            newtestImportChecker = new String(Files.readAllBytes(
                    Paths.get(resourcePath + "testImportToolsAndOneDuplicateInterfaceType - new testImport5.skill")));

            newFileToBeImportedAfterImport = new String(
                    Files.readAllBytes(Paths.get(workspacePath + File.separator + "TestImportTools5" + File.separator
                            + "testImportToolsAndOneDuplicateInterfaceType - FileToBeImported Before Import.skill")));
            newFileToBeImportedAfterImportChecker = new String(Files.readAllBytes(Paths.get(
                    resourcePath + "testImportToolsAndOneDuplicateInterfaceType - FileToBeImported After Import.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        checkIfFileContentsAreTheSame();
    }

    /**
     * Create a new SKilL-Project and a SKilL-File in the project and write some content in it. Afterwards, import a tool to
     * the project and merge duplicate enum type.
     * 
     */
    @Test
    public void testImportToolsAndOneDuplicateEnumType() {
        SWTBotPreferences.KEYBOARD_LAYOUT = "EN_US";

        bot.menu("Window").menu("Show View").menu("Project Explorer").click();
        // Project Explorer as active window
        bot.viewByTitle("Project Explorer").show();
        // Create new project "TestImportTools6"
        bot.menu("File").menu("New").menu("SKilL Project").click();
        bot.textWithLabel("&Project name:").setText("TestImportTools6");
        bot.button("Finish").click();
        createSKilLFile("testImport6", "TestImportTools6");
        // Editor for "testImport6.skill" as active window
        bot.editorByTitle("testImport6.skill").show();
        // Populate "testImport6.skill"
        bot.styledText().setText("# Test Import\nenum A {\n  test1, test2;\n   i8 Test;\n}");
        bot.menu("File").menu("Save").click();

        // Run Import Tools
        bot.menu("SKilLEd").menu("Import Tools").click();
        bot.textWithLabel("Select tool to import:")
                .setText(resourcePath + "testImportToolsAndOneDuplicateEnumType - FileToBeImported before Import.skill");
        bot.textWithLabel("Import to Project:").setText(workspacePath + File.separator + "TestImportTools6");
        bot.button("OK").click();

        bot.viewByTitle("Project Explorer").show();
        bot.tree().getTreeItem("TestImportTools6").select();
        bot.menu("File").menu("Refresh").click();
        bot.tree().getTreeItem("TestImportTools6").expand();
        bot.tree().getTreeItem("TestImportTools6")
                .getNode("testImportToolsAndOneDuplicateEnumType - FileToBeImported before Import.skill").doubleClick();
        bot.editorByTitle("testImportToolsAndOneDuplicateEnumType - FileToBeImported before Import.skill").show();

        bot.viewByTitle("ToolView").show();
        // Refresh ToolView window
        key.pressShortcut(Keystrokes.F5);
        // Select Tool "FileToBeImported" to check if it exists
        bot.list().select("testImportToolsAndOneDuplicateEnumType - FileToBeImported before Import");
        bot.editorByTitle("testImportToolsAndOneDuplicateEnumType - FileToBeImported before Import.skill").show();
        bot.editorByTitle("testImportToolsAndOneDuplicateEnumType - FileToBeImported before Import.skill").close();

        try {
            newtestImport = new String(Files.readAllBytes(
                    Paths.get(workspacePath + File.separator + "TestImportTools6" + File.separator + "testImport6.skill")));
            newtestImportChecker = new String(Files.readAllBytes(
                    Paths.get(resourcePath + "testImportToolsAndOneDuplicateEnumType - new testImport6.skill")));

            newFileToBeImportedAfterImport = new String(
                    Files.readAllBytes(Paths.get(workspacePath + File.separator + "TestImportTools6" + File.separator
                            + "testImportToolsAndOneDuplicateEnumType - FileToBeImported before Import.skill")));
            newFileToBeImportedAfterImportChecker = new String(Files.readAllBytes(Paths
                    .get(resourcePath + "testImportToolsAndOneDuplicateEnumType - FileToBeImported after Import.skill")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        checkIfFileContentsAreTheSame();
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
     * This method checks that the contents of the imported file and the files in the project are correct after merging.
     */
    public void checkIfFileContentsAreTheSame() {
        // Remove all whitespaces so that they do not affect the comparision
        newtestImport = newtestImport.replaceAll("\\s+", "");
        newtestImportChecker = newtestImportChecker.replaceAll("\\s+", "");
        newFileToBeImportedAfterImport = newFileToBeImportedAfterImport.replaceAll("\\s+", "");
        newFileToBeImportedAfterImportChecker = newFileToBeImportedAfterImportChecker.replaceAll("\\s+", "");

        // Check if the contents of the imported and merged files are correct
        assertTrue(newtestImport.equals(newtestImportChecker));
        assertTrue(newFileToBeImportedAfterImport.equals(newFileToBeImportedAfterImportChecker));

    }

    /**
     * This class releases the resources used in beforeClass()
     */
    @AfterClass
    public static void sleep() {
        bot.sleep(5000);
    }

}
