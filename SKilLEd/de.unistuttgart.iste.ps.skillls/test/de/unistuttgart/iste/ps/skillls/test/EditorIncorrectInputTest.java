package de.unistuttgart.iste.ps.skillls.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unistuttgart.iste.ps.skillls.main.Editor;
import de.unistuttgart.iste.ps.skillls.main.MainClass;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;


/**
 * Created on 01.10.15.
 *
 * @author Armin Hüneburg
 */
public class EditorIncorrectInputTest {
    private Editor editor;
    private static final String skillFilePath = "resources" + File.separator + ".skills";
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
        } catch (@SuppressWarnings("unused") IOException e) {
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
            } catch (@SuppressWarnings("unused") IOException e) {
                throw new RuntimeException();
            }
        }
    }

    @SuppressWarnings("static-method")
    @Before
    public void before() {
        MainClassTest.deleteDirectory(new File("resources" + File.separator + ".skillt"));
        if (Files.exists(Paths.get(skillFilePath))) {
            try {
                Files.delete(Paths.get(skillFilePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.createFile(Paths.get(skillFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new Tool as preparation for the other tests.
     */
    @Test
    public void testCreateNewTool() {
        String command = "&n:testTool;";
        editor = new Editor(command);
        int orig;
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            orig = sk.Tools().size();
            editor.setSkillFile(sk);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
            orig = -1;
        }
        editor.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            for (Tool tool : sk.Tools()) {
                System.out.println(tool.getName());
            }
            assertEquals("no tool created.", sk.Tools().size(), orig + 1);
        } catch (@SuppressWarnings("unused") IOException e) {
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
        editor = new Editor(command);
        int orig;
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            orig = sk.Tools().size();
            editor.setSkillFile(sk);
        } catch (@SuppressWarnings("unused") IOException e) {
            orig = -1;
            fail();
        }
        try {
            editor.start();
        } catch (Throwable t) {
            if (!(t instanceof IllegalArgumentException)) {
                fail("wrong exception type");
            }
        }
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read,
                    de.ust.skill.common.java.api.SkillFile.Mode.Write);
            assertEquals("testTool not deleted. Failing ok, because not yet implemented in SKilL.", orig, sk.Tools().size());
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }

    /**
     * Tries to add a type to a non-existing tool.
     */
    @Test // (expected = IllegalArgumentException.class)
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
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 0));
        } catch (@SuppressWarnings("unused") IOException e) {
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
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 0));
        } catch (@SuppressWarnings("unused") IOException e) {
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
                    sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool") && t.getTypes().size() == 0));
        } catch (@SuppressWarnings("unused") IOException e) {
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
        } catch (@SuppressWarnings("unused") IOException e) {
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
            boolean nonRed = false;

            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testName")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Field field : type.getFields()) {
                        if (field.getName().equals("notRed")) {
                            nonRed = true;
                        }
                    }
                }
            }
            assertFalse("red not in Color in testTool", nonRed);
        } catch (@SuppressWarnings("unused") IOException e) {
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
            boolean wrongHint = false;

            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Hint hint : type.getHints()) {
                        if (hint.getName().equals("!ignore")) {
                            wrongHint = true;
                        }
                    }
                }
            }

            // check if color has a wrong hint
            assertFalse("red has a wrong hint", wrongHint);
        } catch (@SuppressWarnings("unused") IOException e) {
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
            SkillFile sk = SkillFile.open(Paths.get(skillFilePath));
            for (Tool tool : sk.Tools()) {
                System.out.println(tool.getName());
                for (Type type : tool.getTypes()) {
                    System.out.println("  " + type.getName());
                    for (Field field : type.getFields()) {
                        for (Hint hint : field.getHints()) {
                            System.out.println("    " + hint.getName());
                        }
                        System.out.println("    " + field.getName());
                    }
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        String[] args = new String[] { "-e", "resources", "testTool:7:Color:red:!ignore;" };
        MainClass.main(args);
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            boolean nonNull = false;
            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Hint hint : type.getHints()) {
                        if (hint.getName().equals("!nonNull")) {
                            nonNull = true;
                        }
                    }
                }
            }

            // check if some error occurred
            assertFalse("red is nonNull in Color in testTool.", nonNull);
        } catch (@SuppressWarnings("unused") IOException e) {
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
        } catch (@SuppressWarnings("unused") Error e) {
            assertTrue(true);
        }
        try {
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            for (Tool tool : sk.Tools()) {
                System.out.println(tool.getName());
                for (Type type : tool.getTypes()) {
                    for (Hint hint : type.getHints()) {
                        System.out.println("  " + hint.getName());
                    }
                    System.out.println("  " + type.getName());
                }
                System.out.println();
            }

            boolean colorIsSingleton = false;

            // check if color is not !notSingleton
            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Hint hint : type.getHints()) {
                        if (hint.getName().equals("!notSingleton")) {
                            colorIsSingleton = true;
                        }
                    }
                }
            }
            assertFalse("Color is not singleton in testTool", colorIsSingleton);
        } catch (@SuppressWarnings("unused") IOException e) {
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
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
            // check if some error occurred
            boolean singleton = false;

            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Hint hint : type.getHints()) {
                        if (hint.getName().equals("!singleton")) {
                            singleton = true;
                            break;
                        }
                    }
                }
            }
            assertFalse("Color is not singleton in testTool", singleton);
        } catch (@SuppressWarnings("unused") IOException e) {
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
            SkillFile sk = SkillFile.open(skillFilePath, de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);

            for (Tool tool : sk.Tools()) {
                System.out.println(tool.getName());
                for (Type type : tool.getTypes()) {
                    for (Hint hint : type.getHints()) {
                        System.out.println("  " + hint.getName());
                    }
                    System.out.println("  " + type.getName());
                }
                System.out.println();
            }

            // check if some error occurred.
            boolean notRed = false;

            for (Tool tool : sk.Tools()) {
                if (!tool.getName().equals("testTool")) {
                    continue;
                }
                for (Type type : tool.getTypes()) {
                    if (!type.getName().equals("Color")) {
                        continue;
                    }
                    for (Field field : type.getFields()) {
                        if (field.getName().contains("notRed")) {
                            notRed = true;
                            break;
                        }
                    }
                    if (notRed) {
                        break;
                    }
                }
                if (notRed) {
                    break;
                }
            }
            assertFalse("red still in Color in testTool.", notRed);
        } catch (@SuppressWarnings("unused") IOException e) {
            fail();
        }
    }
}
