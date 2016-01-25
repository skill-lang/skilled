package de.unistuttgart.iste.ps.skillls.test;

import de.unistuttgart.iste.ps.skillls.main.Editor;
import de.unistuttgart.iste.ps.skillls.main.MainClass;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;


/**
 * @author Armin Hüneburg
 * @since 03.09.15.
 */
public class EditorCorrectInputTest {
    Editor editor;
    private static String skillFilePath = "resources/.skills";

    /**
     * Deletes the .skills file.
     */
    @AfterClass
    public static void tearDown() {
        if (Files.exists(Paths.get(skillFilePath))) {
            try {
                Files.delete(Paths.get(skillFilePath));
            } catch (IOException e) {
                throw new RuntimeException();
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
        } catch (IOException e) {
            fail();
        }
        editor.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Create,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("More than 0 Tools.", 0, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if creating a new Tool works.
     */
    @Test
    public void testCreateNewTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "CreateNewTool.sf"), Paths.get(skillFilePath));
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
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if just giving a tool name throws an error.
     */
    @Test
    public void testGetTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "GetTool.sf"), Paths.get(skillFilePath));
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
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a normal type.
     */
    @Test
    public void testAddTypeWithoutExtension() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddTypeWithoutExtension.sf"), Paths.get(skillFilePath));
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
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a type.
     */
    @Test
    public void testRemoveTypeWithoutExtension() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveTypeWithoutExtension.sf"), Paths.get(skillFilePath));
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
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can rename a tool.
     */
    @Test
    public void testRename() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "Rename.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:1:blargh;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertTrue("testTool still exists or blargh does not",
                    // "testTool" is deleted and the other tools are called "blargh" and "TypeTool"
                    sk.Tools().stream().allMatch(t -> !t.getName().equals("testTool") && t.getName().equals("blargh")
                            || t.getName().endsWith("TypeTool")));
            sk.close();
            args[2] = "blargh:1:testTool";
            MainClass.main(args);
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can concat multiple commands into one string.
     */
    @Test
    public void testConcatStuff() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "ConcatStuff.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources",
                "&n:twoTypeTool:2:Bathtub;twoTypeTool:2:Window;&n:oneTypeTool:2:Bathtub;&n:noTypeTool;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("not 3 tools", sk.Tools().size() == 4);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSetDefaults() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "SetDefaults.sf"), Paths.get(skillFilePath));
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
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a type which extends another type and that type is automatically added.
     */
    @Test
    public void testAddTypeWithExtension() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddTypeWithExtension.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:TransparentColor;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color or TransparentColor not in Tool",
                    // find "testTool" and check amount of types.
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 2));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a field.
     */
    @Test
    public void testAddField() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddField.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:4:Color:red;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // find "testTool" and check if "color" is in it.
            assertTrue("red not in Color in testTool", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool")
                    && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 1)));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a hint to a field.
     */
    @Test
    public void testAddFieldHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddFieldHint.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:6:Color:red:!nonNull;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // find "testTool" and check if "Color" is in it and check if it contains "red" and check if it is "!nonNull"
            assertTrue("red not nonNull in Color in testTool",
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                            .anyMatch(ty -> ty.getName().equals("Color")
                                    && ty.getFields().stream().anyMatch(f -> f.getName().endsWith("red")
                                    && f.getFieldHints().stream().anyMatch(h -> h.getName().equals("!nonNull"))))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a hint from a field.
     */
    @Test
    public void testRemoveFieldHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveFieldHint.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:7:Color:red:!nonNull;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // find "testTool" and find "color" and check if it is still "!nonNull"
            assertTrue("red is nonNull in Color in testTool. Failing ok, not yet implemented in SKilL.",
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                            .anyMatch(ty -> ty.getName().endsWith("Color")
                                    && ty.getFields().stream().anyMatch(f -> f.getName().endsWith("red")
                                    && f.getFieldHints().stream().noneMatch(h -> h.getName().equals("!nonNull"))))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a hint to a type.
     */
    @Test
    public void testAddTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddTypeHint.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:8:Color:!unique;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if "Color" is in "testTool"
            assertTrue("Color is not unique in testTool",
                    sk.Tools().stream()
                            .anyMatch(t -> t.getName().equals("testTool")
                                    && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color")
                                    && ty.getTypeHints().stream().anyMatch(h -> h.getName().equals("!unique")))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a hint from a type.
     */
    @Test
    public void testRemoveTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveTypeHint.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:9:Color:!singleton;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if "Color" in "testTool" is still "!singleton"
            assertTrue("Color is not singleton in testTool", sk.Tools().stream()
                    .anyMatch(t -> t.getName().equals("testTool")
                            && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color")
                            && ty.getTypeHints().stream().noneMatch(h -> h.getName().equals("!singleton")))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if adding an enum adds the enum values automatically.
     */
    @Test
    public void testAddingEnum() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingEnum.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:Picture" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // Check if "Picture" contains all enum values
            assertTrue("does not contain all enum values", sk.Tools().stream()
                    .anyMatch(t -> t.getName().equals("testTool")
                            && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("enum Picture")
                                    && ty.getFields().stream().anyMatch(f -> f.getName().equals("Photo,Painting")))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a field.
     */
    @Test
    public void testRemoveField() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemoveField.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:5:Color:red;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if "Color" in "testTool" still contains any fields.
            assertTrue("red still in Color in testTool. Failing ok, because not yet implemented in SKilL.",
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                            .anyMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 0)));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testAddingTypedef() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingTypedef.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:MainRefridgerator" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if MainRefridgerator is indexed.
            assertTrue("MainFridge not indexed",
                    sk.Types().stream().anyMatch(type -> type.getName().contains("MainRefridgerator")));
            // check if "testTool" contains Fridge and MainRefridgerator
            assertTrue("does not contain MainFridge or Fridge",
                    sk.Tools().stream().anyMatch(tool -> tool.getName().equals("testTool")
                            && tool.getTypes().stream().anyMatch(type -> type.getName().contains("MainRefridgerator"))));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testAddingInterface() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingInterface.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:I1" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if i1 is in "testTool"
            assertTrue("does not contain I1", sk.Tools().stream().anyMatch(tool -> tool.getName().equals("testTool")
                    && tool.getTypes().stream().anyMatch(type -> type.getName().toLowerCase().startsWith("interface i1"))));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testExtractingTestTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "ExtractingTestTool.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        StringBuilder builder = new StringBuilder();
        builder.append("lib");
        builder.append(File.separator);
        builder.append("skill_2.11-0.3.jar");
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
            // ignored
        }
        String[] args = new String[] { "-agloxmp", builder.toString(), "Java", "generated", "scala", "onetypetool",
                "resources", "testTool" };
        MainClass.main(args);
        // check if generated files exist
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src"
                + File.separator + "main" + File.separator + "java" + File.separator + "onetypetool")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @Test
    public void testExtractingTestToolNoCleanup() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "NoCoExtractingTestToolNoCleanup.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        StringBuilder builder = new StringBuilder();
        builder.append("lib");
        builder.append(File.separator);
        builder.append("skill_2.11-0.3.jar");
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
            // ignored
        }
        String[] args = new String[] { "--no-cleanup", "-agloxmp", builder.toString(), "Java", "generated", "scala",
                "onetypetool", "resources", "testTool" };
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

    @Test
    public void testAddingTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddingTypeHint.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] {"--edit", "resources", "oneTypeTool:2:PowerStrip;oneTypeTool:8:PowerStrip:!readOnly"};
        MainClass.main(args);
        SkillFile sk = null;
        try {
            sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if PowerStrip is in oneTypeTool and if it is !readOnly
            assertTrue("PowerStrip does not have hint", sk.Tools().stream().anyMatch(tool -> tool.getName().equals("oneTypeTool") &&
                    tool.getTypes().stream().anyMatch(type -> type.getName().equals("PowerStrip") &&
                            type.getTypeHints().stream().anyMatch(hint -> hint.getName().equals("!readOnly")))));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sk != null) {
                sk.close();
            }
        }
    }

    @Test
    public void testRemovingTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "RemovingTypeHint.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] {"--edit", "resources", "oneTypeTool:2:PowerStrip;oneTypeTool:8:PowerStrip:!readOnly"};
        MainClass.main(args);
        SkillFile sk = null;
        try {
            sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            Tool oneTypeTool = null;
            for (Tool t : sk.Tools()) {
                if (t.getName().equals("oneTypeTool")) {
                    oneTypeTool = t;
                }
            }
            if (oneTypeTool == null) {
                fail("Tool is null");
            }
            Type powerStrip = null;
            for (Type t : oneTypeTool.getTypes()) {
                if (t.getName().equals("PowerStrip")) {
                    powerStrip = t;
                }
            }
            if (powerStrip == null) {
                fail("Type is null");
            }
            Hint readOnly = null;
            for (Hint h : powerStrip.getTypeHints()) {
                if (h.getName().equals("!readOnly")) {
                    readOnly = h;
                }
            }
            if (readOnly == null) {
                fail("Hint is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sk != null) {
                sk.close();
            }
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
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "DeleteTool.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String command = "testTool:0;";
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
            // check if testTool is deleted
            assertTrue("testTool not deleted. Failing ok, because not yet implemented in SKilL.",
                    sk.Tools().stream().noneMatch(t -> t.getName().equals("testTool")));
        } catch (IOException e) {
            fail();
        }
    }
}
