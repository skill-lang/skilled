package de.unistuttgart.iste.ps.skillls.test;

import org.junit.*;

import de.unistuttgart.iste.ps.skillls.main.Edit;
import de.unistuttgart.iste.ps.skillls.main.MainClass;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

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
public class EditIncorrectInputTest {
    Edit edit;
    private static String skillFilePath = "resources" + File.separator + ".skills";
    private final String skillsPath = "testFiles" + File.separator + "IncorrectInput" + File.separator + "%s";

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
    public void testCreateNewTool() {
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
            assertTrue("no tool created.", sk.Tools().size() >= 1);
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to delete a non-existing tool.
     */
    @Test
    public void testDeleteToolNotExisting() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "DeleteToolNotExisting")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String command = "notTestTool:0;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write));
        } catch (IOException e) {
            fail();
        }
        try {
            edit.start();
        } catch (Throwable t) {
            if (!(t instanceof IllegalArgumentException)) {
                fail("wrong exception type");
            }
        }
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
    @Test//(expected = IllegalArgumentException.class)
    public void testAddTypeToNotExistingTool() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "AddTypeToNotExistingTool")), Paths.get(skillFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "notTestTool:2:Color;" };
        try {
            MainClass.main(args);
        } catch (Throwable t) {
            if (!(t instanceof IllegalArgumentException)) {
                fail("wrong exception type");
            }
        }
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if Color is in Tool
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
    public void testAddNotExistingType() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "AddNotExistingType")), Paths.get(skillFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:notColor;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if testTool has a type
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
    public void testRemoveNotExistingType() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "RemoveNotExistingType")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:3:notColor;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if some kind of error occurred
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
    public void testAddTypeWithoutExtension() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "AddTypeWithoutExtension")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:2:Color;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if color is in testTool
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
    public void testAddNotExistingField() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "AddNotExistingField")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:4:Color:notRed;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if no type has a field in testTool
            assertTrue("red not in Color in testTool", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool")
                    && t.getTypes().stream().allMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 0)));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to add a non-existing hint to a field.
     */
    @Test
    public void testAddNotExistingFieldHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "AddNotExistingFieldHint")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:6:Color:red:!ignore;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if color has a wrong hint
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
    public void testRemoveNotExistingFieldHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "RemoveNotExistingFieldHint")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:7:Color:red:!ignore;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if some error occurred
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
    @Test
    public void testAddNotExistingTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "AddNotExistingTypeHint")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:8:Color:!notSingleton;" };
        try {
            MainClass.main(args);
        } catch (AssertionError e) {
            assertTrue(true);
        }
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if color is not !notSingleton
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
    public void testRemoveNotExistingTypeHint() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "RemoveNotExistingTypeHint")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:9:Color:!singleton;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if some error occurred
            assertTrue("Color is not singleton in testTool", sk.Tools().stream()
                    .anyMatch(t -> t.getName().equals("testTool")
                            && t.getTypes().stream().anyMatch(ty -> ty.getName().equals("Color")
                                    && ty.getTypeHints().stream().noneMatch(h -> h.getName().equals("!singleton")))));
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Tries to remove a non-existing field.
     */
    @Test
    public void testRemoveNotExistingField() {
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get(String.format(skillsPath, "RemoveNotExistingField")), Paths.get(skillFilePath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:5:Color:notRed;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            // check if some error occurred.
            assertTrue("red still in Color in testTool. Failing ok, because not yet implemented in SKilL.",
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().stream()
                            .allMatch(ty -> ty.getName().equals("Color") && ty.getFields().size() == 0)));
        } catch (IOException e) {
            fail();
        }
    }
}
