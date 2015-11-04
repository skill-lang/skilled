package main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tools.api.SkillFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author Armin Hüneburg
 * @since 03.09.15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditCorrectInputTest {
    Edit edit;
    private static String skillFilePath = "resources/.skills";

    /**
     * Deletes the old .skills file and creates a new one.
     */
    @BeforeClass
    public static void setUp() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.createFile(Paths.get(skillFilePath));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

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
    public void test1NoCommand() {
        String command = "";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Create, de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Create, de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("More than 0 Tools.", 0, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if creating a new Tool works.
     */
    @Test
    public void test20CreateNewTool() {
        String command = "&n:testTool;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read, de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read, de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("no tool created.", 1, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if deleting a tool works.
     */
    @Test
    public void test99DeleteTool() {
        String command = "testTool:0;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read, de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read, de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertTrue("testTool not deleted. Failing ok, because not yet implemented in SKilL.", sk.Tools().stream().noneMatch(t -> t.getName().equals("testTool")));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if just giving a tool name throws an error.
     */
    @Test
    public void test3GetTool() {
        String command = "testTool;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read, de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read, de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertTrue("testTool not found.", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool")));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a normal type.
     */
    @Test
    public void test40AddTypeWithoutExtension() {
        String[] args = new String[] { "-e", "resources", "testTool:2:Color;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color not in Tool", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 1));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a type.
     */
    @Test
    public void test41RemoveTypeWithoutExtension() {
        String[] args = new String[] { "-e", "resources", "testTool:3:Color;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color in Tool", sk.Tools().stream().noneMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 1));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a type which extends another type and that type is automatically added.
     */
    @Test
    public void test5AddTypeWithExtension() {
        String[] args = new String[] { "-e", "resources", "testTool:2:TransparentColor;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color or TransparentColor not in Tool", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 2));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a field.
     */
    @Test
    public void test60AddField() {
        String[] args = new String[] { "-e", "resources", "testTool:4:Color:red;"};
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red not in Color in testTool", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 1)));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a field.
     */
    @Test
    public void test91RemoveField() {
        String[] args = new String[] { "-e", "resources", "testTool:5:Color:red;"};
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red still in Color in testTool. Failing ok, because not yet implemented in SKilL.", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 0)));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a hint to a field.
     */
    @Test
    public void test70AddFieldHint() {
        String[] args = new String[] { "-e", "resources", "testTool:6:Color:red:!nonNull;"};
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red not nonNull in Color in testTool", sk.Tools().stream().anyMatch(t ->
                    t.getName().equals("testTool") && t.getTypes().stream().anyMatch(ty ->
                            ty.getName().equals("Color") && ty.getFields().stream().anyMatch(f ->
                                f.getName().endsWith("red") && f.getFieldHints().stream().anyMatch(h -> h.getName().equals("!nonNull"))))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a hint from a field.
     */
    @Test
    public void test71RemoveFieldHint() {
        String[] args = new String[] { "-e", "resources", "testTool:7:Color:red:!nonNull;"};
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red is nonNull in Color in testTool. Failing ok, not yet implemented in SKilL.",
                    sk.Tools().stream().anyMatch(t ->
                            t.getName().equals("testTool") && t.getTypes().stream().anyMatch(ty ->
                                    ty.getName().endsWith("Color") && ty.getFields().stream().anyMatch(f ->
                                            f.getName().endsWith("red") && f.getFieldHints().stream().noneMatch(h ->
                                                    h.getName().equals("!nonNull"))))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can add a hint to a type.
     */
    @Test
    public void test80AddTypeHint() {
        String[] args = new String[] { "-e", "resources", "testTool:8:Color:!unique;"};
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color is not unique in testTool", sk.Tools().stream().anyMatch(t ->
                    t.getName().equals("testTool") && t.getTypes().stream().anyMatch(ty ->
                            ty.getName().equals("Color") && ty.getTypeHints().stream().anyMatch(h ->
                                    h.getName().equals("!unique")))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can remove a hint from a type.
     */
    @Test
    public void test81RemoveTypeHint() {
        String[] args = new String[] { "-e", "resources", "testTool:9:Color:!singleton;"};
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color is not singleton in testTool", sk.Tools().stream().anyMatch(t ->
                    t.getName().equals("testTool") && t.getTypes().stream().anyMatch(ty ->
                            ty.getName().equals("Color") && ty.getTypeHints().stream().noneMatch(h ->
                                    h.getName().equals("!singleton")))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if you can rename a tool.
     */
    @Test
    public void test51Rename() {
        String[] args = new String[] { "-e", "resources", "testTool:1:blargh;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read, de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertTrue("testTool still exists or blargh does not", sk.Tools().stream().allMatch(t -> !t.getName().equals("testTool") && t.getName().equals("blargh") || t.getName().endsWith("TypeTool")));
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
    public void test52ConcatStuff() {
        String[] args = new String[] { "-e", "resources", "&n:twoTypeTool:2:Bathtub;twoTypeTool:2:Window;&n:oneTypeTool:2:Bathtub;&n:noTypeTool;"};
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("not 3 tools", sk.Tools().size() == 4);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void test53SetDefaults() {
        String[] args = new String[] { "-e", "resources", "testTool:10:scala:/home/armin/uni/SKilLEd/SKilL/target/scala-2.11/skill_2.11-0.3.jar:Java:testTool:/home/armin/test" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("no defaults set.", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") &&
                    t.getGenerator().getExecEnv().equals("scala") &&
                    t.getGenerator().getPath().equals("/home/armin/uni/SKilLEd/SKilL/target/scala-2.11/skill_2.11-0.3.jar") &&
                    t.getModule().equals("testTool") &&
                    t.getLanguage().equals("Java") &&
                    t.getOutPath().equals("/home/armin/test")));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tests if adding an enum adds the enum values automatically.
     */
    @Test
    public void test91AddingEnum() {
        String[] args = new String[] { "-e", "resources", "testTool:2:Picture" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("does not contain all enum values", sk.Tools().stream().anyMatch(t ->
                    t.getName().equals("testTool") && t.getTypes().stream().anyMatch(ty ->
                            ty.getName().equals("enum Picture") && ty.getFields().stream().anyMatch(f ->
                                    f.getName().equals("Photo,Painting")))));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void test92AddingTypedef() {
        String[] args = new String[] { "-e", "resources", "testTool:2:MainRefridgerator" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("MainFridge not indexed", sk.Types().stream().anyMatch(type ->
                    type.getName().contains("MainRefridgerator")));
            assertTrue("does not contain MainFridge or Fridge", sk.Tools().stream().anyMatch(tool ->
                    tool.getName().equals("testTool") &&
                            tool.getTypes().stream().anyMatch(type -> type.getName().contains("MainRefridgerator"))));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void test93AddingInterface() {
        String[] args = new String[] { "-e", "resources", "testTool:2:I1" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("does not contain I1", sk.Tools().stream().anyMatch(tool -> tool.getName().equals("testTool") &&
                    tool.getTypes().stream().anyMatch(type -> type.getName().toLowerCase().startsWith("interface i1"))));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void test94ExtractingTestTool() {
        StringBuilder builder = new StringBuilder();
        builder.append("lib");
        builder.append(File.separator);
        builder.append("skill_2.11-0.3.jar");
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
        }
        String[] args = new String[] { "-agloxmp", builder.toString(), "Java", "generated",
                "scala", "onetypetool", "resources", "testTool" };
        MainClass.main(args);
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "onetypetool")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }
}