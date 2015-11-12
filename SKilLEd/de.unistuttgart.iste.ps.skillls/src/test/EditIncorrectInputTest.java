package test;

import main.Edit;
import main.MainClass;

import org.junit.*;
import org.junit.runners.MethodSorters;
import tools.api.SkillFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;


/**
 * Created on 01.10.15.
 *
 * @author Armin Hüneburg
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditIncorrectInputTest {
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

    @Before
    public void before() {
        MainClassTest.deleteDirectory(new File("resources" + File.separator + ".skillt"));
    }

    /**
     * Creates a new Tool as preparation for the other tests.
     */
    @Test
    public void test20CreateNewTool() {
        String command = "&n:testTool;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("no tool created.", 1, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to delete a non-existing tool.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test21DeleteToolnotExisting() {
        String command = "notTestTool:0;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("testTool not deleted. Failing ok, because not yet implemented in SKilL.", 1, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to add a type to a non-existing tool.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test40AddTypeToNotExistingTool() {
        String[] args = new String[] { "-e", "resources", "notTestTool:2:Color;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color in Tool",
                    sk.Tools().stream().allMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 0));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to add a non-existing type to a tool.
     */
    @Test
    public void test41AddNotExistingType() {
        String[] args = new String[] { "-e", "resources", "testTool:2:notColor;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color in Tool",
                    sk.Tools().stream().allMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 0));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to remove a non-existing type.
     */
    @Test
    public void test41RemoveNotExistingType() {
        String[] args = new String[] { "-e", "resources", "testTool:3:notColor;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color in Tool",
                    sk.Tools().stream().allMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 0));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Adds a type as preparation for the other tests.
     */
    @Test
    public void test42AddTypeWithoutExtension() {
        String[] args = new String[] { "-e", "resources", "testTool:2:Color;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color not in Tool",
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 1));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to add a non-existing field.
     */
    @Test
    public void test60AddNotExistingField() {
        String[] args = new String[] { "-e", "resources", "testTool:4:Color:notRed;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red not in Color in testTool", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool")
                    && t.getTypes().stream().allMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 0)));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to remove a non-existing field.
     */
    @Test
    public void test91RemoveNotExistingField() {
        String[] args = new String[] { "-e", "resources", "testTool:5:Color:notRed;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red still in Color in testTool. Failing ok, because not yet implemented in SKilL.",
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                            .allMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 0)));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to add a non-existing hint to a field.
     */
    @Test
    public void test70AddNotExistingFieldHint() {
        String[] args = new String[] { "-e", "resources", "testTool:6:Color:red:!ignore;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red has a wrong hint",
                    sk.Tools().stream()
                            .anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                                    .anyMatch(ty -> ty.getName().equals("Color") && ty.getFields().stream()
                                            .allMatch(f -> f.getName().endsWith("red") && f.getFieldHints().size() == 0))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to remove a non-existing hint from a field.
     */
    @Test
    public void test71RemoveNotExistingFieldHint() {
        String[] args = new String[] { "-e", "resources", "testTool:7:Color:red:!ignore;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("red is nonNull in Color in testTool. Failing ok, not yet implemented in SKilL.",
                    sk.Tools().stream()
                            .anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                                    .anyMatch(ty -> ty.getName().endsWith("Color") && ty.getFields().stream()
                                            .allMatch(f -> f.getName().endsWith("red") && f.getFieldHints().size() == 0))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to add a non-existing hint to a type.
     */
    public void test80AddNotExistingTypeHint() {
        String[] args = new String[] { "-e", "resources", "testTool:8:Color:!notSingleton;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color is not singleton in testTool", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool")
                    && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color") && ty.getTypeHints().size() == 0)));
        } catch (IOException e) {
            fail("IO Exception");
        }
    }

    /**
     * Tries to remove a non-existing hint from a type.
     */
    @Test
    public void test81RemoveNotExistingTypeHint() {
        String[] args = new String[] { "-e", "resources", "testTool:9:Color:!singleton;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            assertTrue("Color is not singleton in testTool", sk.Tools().stream()
                    .anyMatch(t -> t.getName().equals("testTool")
                            && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color")
                                    && ty.getTypeHints().stream().noneMatch(h -> h.getName().equals("!singleton")))));
        } catch (IOException e) {
            fail();
        }
    }
}
