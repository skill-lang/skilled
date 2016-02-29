package de.unistuttgart.iste.ps.skillls.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import de.unistuttgart.iste.ps.skillls.main.Editor;
import de.unistuttgart.iste.ps.skillls.main.Indexing;
import de.unistuttgart.iste.ps.skillls.main.MainClass;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;


/**
 * @author Armin Hüneburg
 * @since 03.09.15.
 */
public class EditorCorrectInputTest {
    private Editor editor;
    private static final String skillFilePath = "resources/.skills";

    /**
     * Deletes the .skills file.
     */
    @AfterClass
    public static void tearDown() {
        if (Files.exists(Paths.get(skillFilePath))) {
            try {
                Files.delete(Paths.get(skillFilePath));
            } catch (@SuppressWarnings("unused") IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @SuppressWarnings("static-method")
    @After
    public void cleanUp() {
        if (Files.exists(Paths.get("testFiles" + File.separator + "Furniture.skill"))) {
            try {
                Files.move(Paths.get("testFiles" + File.separator + "Furniture.skill"),
                        Paths.get("resources" + File.separator + "Furniture.skill"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tests if no errors occur if no command is given.
     */
    @Test
    public void testNoCommand() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.createFile(Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String command = "";
        editor = new Editor(command);
        try {
            editor.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Create,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
        editor.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Create,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("More than 0 Tools.", 0, sk.Tools().size());
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "CreateNewTool.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if creating a new Tool works.
     */
    @SuppressWarnings("unused")
    @Test
    public void testCreateNewTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "CreateNewTool.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String command = "&n:testTool;";
        editor = new Editor(command);
        try {
            editor.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        editor.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("no tool created.", 1, sk.Tools().size());
            assertEquals("tool name not testTool", "testTool", sk.Tools().stream().findAny().get().getName());
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "GetTool.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests if just giving a tool name throws an error.
     */
    @SuppressWarnings("unused")
    @Test
    public void testGetTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "GetTool.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String command = "testTool;";
        editor = new Editor(command);
        try {
            editor.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        editor.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            // find tool "testTool"
            assertTrue("testTool not found.", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool")));
            // Files.copy(Paths.get(skillFilePath), Paths
            // .get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "AddTypeWithoutExtension.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a normal type.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testAddTypeWithoutExtension() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(
                    Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddTypeWithoutExtension.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:Color;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color not in Tool",
                    // find "testTool" with one type
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 1));
            // Files.copy(Paths.get(skillFilePath), Paths.get(
            // "testFiles" + File.separator + "CorrectInput" + File.separator +
            // "RemoveTypeWithoutExtension.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a type.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testRemoveTypeWithoutExtension() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths
                    .get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveTypeWithoutExtension.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:3:Color;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color in Tool",
                    // there is no tool named "testTool" with one type.
                    sk.Tools().stream().noneMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 1));
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "Rename.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can rename a tool.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testRename() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "Rename.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:1:blargh;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertTrue("testTool still exists or blargh does not",
                    // "testTool" is deleted and the other tools are called
                    // "blargh" and "TypeTool"
                    sk.Tools().stream().allMatch(t -> !t.getName().equals("testTool") && t.getName().equals("blargh")
                            || t.getName().endsWith("TypeTool")));
            sk.close();
            args[2] = "blargh:1:testTool";
            MainClass.start(Indexing.NO_INDEXING, args);
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "ConcatStuff.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can concat multiple commands into one string.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testConcatStuff() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "ConcatStuff.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources",
                "&n:twoTypeTool:2:Bathtub;twoTypeTool:2:Window;&n:oneTypeTool:2:Bathtub;&n:noTypeTool;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            Tool testTool, twoType, oneType, noType;
            testTool = twoType = oneType = noType = null;
            for (Tool tool : sk.Tools()) {
                switch (tool.getName()) {
                    case "testTool":
                        testTool = tool;
                        break;

                    case "twoTypeTool":
                        twoType = tool;
                        break;

                    case "oneTypeTool":
                        oneType = tool;
                        break;

                    case "noTypeTool":
                        noType = tool;
                        break;

                    default:
                        break;
                }
            }
            assertEquals("not 4 tools", 4, sk.Tools().size());
            if (testTool != null) {
                assertEquals("testTool does not contain the right amount of Types", 0, testTool.getTypes().size());
            } else {
                fail("testTool is null");
            }
            if (twoType != null) {
                assertEquals("twoType does not contain the right amount of Types", 2, twoType.getTypes().size());
            } else {
                fail("twoType is null");
            }
            if (oneType != null) {
                assertEquals("oneType does not contain the right amount of Types", 1, oneType.getTypes().size());
            } else {
                fail("oneType is null");
            }
            if (noType != null) {
                assertEquals("noType does not contain the right amount of Types", 0, noType.getTypes().size());
            } else {
                fail("noType is null");
            }
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    @SuppressWarnings("static-method")
    @Test
    public void testSetDefaults() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "SetDefaults.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources",
                "testTool:10:scala:/home/armin/uni/SKilLEd/SKilL/target/scala-2.11/skill_2.11-0.3.jar:Java:testTool:/home/armin/test" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("no defaults set.",
                    sk.Tools().stream()
                            // find "testTool" and check the settings.
                            .anyMatch(t -> t.getName().equals("testTool") && t.getGenerator().getExecEnv().equals("scala")
                                    && t.getGenerator().getPath()
                                            .equals("/home/armin/uni/SKilLEd/SKilL/target/scala-2.11/skill_2.11-0.3.jar")
                            && t.getModule().equals("testTool") && t.getLanguage().equals("Java")
                            && t.getOutPath().equals("/home/armin/test")));
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a type which extends another type and that type is automatically added.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testAddTypeWithExtension() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddTypeWithExtension.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:TransparentColor;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            assertTrue("Color or TransparentColor not in Tool",
                    // find "testTool" and check amount of types.
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 2));
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "AddField.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a field.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testAddField() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddField.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:4:Color:red;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            for (Tool tool : sk.Tools()) {
                System.out.println(tool.getName());
                for (Type type : tool.getTypes()) {
                    System.out.println("  " + type.getName());
                    for (Field field : type.getFields()) {
                        System.out.println("    " + field.getName());
                    }
                }
            }
            // find "testTool" and check if "color" is in it.
            assertTrue("red not in Color in testTool",
                    sk.Tools().stream()
                            .anyMatch(t -> t.getName().equals("testTool")
                                    && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color")
                                            && ty.getFields().stream().anyMatch(f -> f.getName().equals("i16 red")))));
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a hint to a field.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testAddFieldHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddFieldHint.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:6:Color:red:!nonNull;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // find "testTool" and check if "Color" is in it and check if it
            // contains "red" and check if it is "!nonNull"
            boolean red = false;
            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Field field : type.getFields()) {
                        if (!field.getName().contains("red")) {
                            continue;
                        }
                        for (Hint hint : field.getHints()) {
                            if (hint.getName().equals("!nonNull")) {
                                red = true;
                            }
                        }
                    }
                }
            }
            assertTrue("red not nonNull in Color in testTool", red);
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "RemoveFieldHint.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a hint from a field.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testRemoveFieldHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveFieldHint.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:7:Color:red:!nonNull;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // find "testTool" and find "color" and check if it is still
            // "!nonNull"
            assertTrue("red is nonNull in Color in testTool. Failing ok, not yet implemented in SKilL.",
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                            .anyMatch(ty -> ty.getName().endsWith("Color")
                                    && ty.getFields().stream().anyMatch(f -> f.getName().endsWith("red")
                                            && f.getHints().stream().noneMatch(h -> h.getName().equals("!nonNull"))))));
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "AddTypeHint.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a hint to a type.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testAddTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddTypeHint.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:8:Color:!unique;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // check if "Color" is in "testTool"
            assertTrue("Color is not unique in testTool",
                    sk.Tools().stream()
                            .anyMatch(t -> t.getName().equals("testTool")
                                    && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color")
                                            && ty.getHints().stream().anyMatch(h -> h.getName().equals("!unique")))));
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a hint from a type.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testRemoveTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveTypeHint.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:9:Color:!singleton;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // check if "Color" in "testTool" is still "!singleton"
            boolean unique = false;

            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Hint hint : type.getHints()) {
                        if (hint.getName().equals("!unique")) {
                            unique = true;
                        }
                    }
                }
            }
            assertTrue("Color is not unique in testTool", unique);
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "AddingEnum.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if adding an enum adds the enum values automatically.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testAddingEnum() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingEnum.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:Picture" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // Check if "Picture" contains all enum values
            assertTrue("does not contain all enum values", sk.Tools().stream()
                    .anyMatch(t -> t.getName().equals("testTool")
                            && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("enum Picture")
                                    && ty.getFields().stream().anyMatch(f -> f.getName().equals("Photo,Painting")))));
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "RemoveField.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a field.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testRemoveField() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveField.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:5:Color:red;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            boolean redInColorInTestTool = false;

            // check if "Color" in "testTool" still contains any fields.
            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Field field : type.getFields()) {
                        if (field.getName().equals("i16 red")) {
                            redInColorInTestTool = true;
                        }
                    }
                }
            }

            assertFalse("red still in Color in testTool.", redInColorInTestTool);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    @SuppressWarnings("static-method")
    @Test
    public void testAddingTypedef() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingTypedef.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:MainRefridgerator" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // check if MainRefridgerator is indexed.
            assertTrue("MainFridge not indexed",
                    sk.Types().stream().anyMatch(type -> type.getName().contains("MainRefridgerator")));
            // check if "testTool" contains Fridge and MainRefridgerator
            assertTrue("does not contain MainFridge or Fridge",
                    sk.Tools().stream().anyMatch(tool -> tool.getName().equals("testTool")
                            && tool.getTypes().stream().anyMatch(type -> type.getName().contains("MainRefridgerator"))));
            // Files.copy(Paths.get(skillFilePath),
            // Paths.get("testFiles" + File.separator + "CorrectInput" +
            // File.separator + "AddingInterface.sf"),
            // StandardCopyOption.REPLACE_EXISTING);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    @SuppressWarnings("static-method")
    @Test
    public void testAddingInterface() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingInterface.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:I1" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // check if i1 is in "testTool"
            assertTrue("does not contain I1", sk.Tools().stream().anyMatch(tool -> tool.getName().equals("testTool")
                    && tool.getTypes().stream().anyMatch(type -> type.getName().toLowerCase().startsWith("interface i1"))));
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    @SuppressWarnings("static-method")
    @Test
    public void testExtractingTestTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "ExtractingTestTool.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String builder = "lib" + File.separator + "skill_2.11-0.3.jar";
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (@SuppressWarnings("unused") IOException ignored) {
            // ignored
        }
        String[] args = new String[] { "-agloxmp", builder, "Java", "generated", "scala", "onetypetool", "resources",
                "testTool" };
        MainClass.start(Indexing.NO_INDEXING, args);
        // check if generated files exist
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src"
                + File.separator + "main" + File.separator + "java" + File.separator + "onetypetool")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @SuppressWarnings("static-method")
    @Test
    public void testExtractingTestToolNoCleanup() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(
                    "testFiles" + File.separator + "CorrectInput" + File.separator + "NoCoExtractingTestToolNoCleanup.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String builder = "lib" + File.separator + "skill_2.11-0.3.jar";
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (@SuppressWarnings("unused") IOException ignored) {
            // ignored
        }
        String[] args = new String[] { "--no-cleanup", "-agloxmp", builder, "Java", "generated", "scala", "onetypetool",
                "resources", "testTool" };
        MainClass.main(args);
        // check if generated files exist
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src"
                + File.separator + "main" + File.separator + "java" + File.separator + "onetypetool")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
        assertTrue(".skillt does not exist", Files.exists(Paths.get("resources" + File.separator + ".skillt")));
    }

    @SuppressWarnings("static-method")
    @Test
    public void testAddingTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingTypeHint.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "--edit", "resources",
                "oneTypeTool:2:PowerStrip;oneTypeTool:8:PowerStrip:!readOnly" };
        MainClass.main(args);
        SkillFile sk;
        try {
            sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // check if PowerStrip is in oneTypeTool and if it is !readOnly
            assertTrue("PowerStrip does not have hint", sk.Tools().stream()
                    .anyMatch(tool -> tool.getName().equals("oneTypeTool")
                            && tool.getTypes().stream().anyMatch(type -> type != null && type.getName().equals("PowerStrip")
                                    && type.getHints().stream().anyMatch(hint -> hint.getName().equals("!readOnly")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests if deleting a tool works.
     */
    @Test
    public void testDeleteTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "DeleteTool.sf"),
                    Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String command = "testTool:0;";
        editor = new Editor(command);
        try {
            editor.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
        editor.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            // check if testTool is deleted
            assertTrue("testTool not deleted. Failing ok, because not yet implemented in SKilL.",
                    sk.Tools().stream().noneMatch(t -> t.getName().equals("testTool")));
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    @SuppressWarnings("static-method")
    @Test
    public void testViewIndexing() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainClass.start(Indexing.NORMAL,
                new String[] { "-e", "resources", "&n:testTool:2:ViewType;testTool:4:ViewType:myRequiredPower" });
        try {
            SkillFile sf = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            assertTrue("view not correctly indexed",
                    sf.Types().stream().anyMatch(t -> t.getName().equals("ViewType") && t.getFields().stream()
                            .anyMatch(f -> f.getName().equals("view Microwave.requiredPower as i16 myRequiredPower"))));
            assertTrue("view not correctly added to tool",
                    sf.Tools().stream().anyMatch(t -> t.getName().equals(
                            "testTool")
                    && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("ViewType") && ty.getFields().stream()
                            .anyMatch(f -> f.getName().equals("view Microwave.requiredPower as i16 myRequiredPower")))));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error while opening file");
        }
    }
}
