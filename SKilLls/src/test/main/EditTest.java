package main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tools.api.SkillFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * @author Armin Hüneburg
 * @since 03.09.15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditTest {
    Edit edit;
    private static String skillFilePath = "a.skills";

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

    @Test
    public void test1NoCommand() {
        String command = "";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath);
            assertEquals("More than 0 Tools.", 0, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void test2CreateNewTool() {
        String command = "&n:testTool;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath);
            assertEquals("no tool created.", 1, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void test3GetTool() {
        String command = "testTool;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath);
            assertTrue("testTool not found.", sk.Tools().stream().anyMatch(t -> t.getName().equals("testTool")));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void test4DeleteTool() {
        String command = "testTool:0;";
        edit = new Edit(command);
        try {
            edit.setSkillFile(SkillFile.open(skillFilePath));
        } catch (IOException e) {
            fail();
        }
        edit.start();
        try {
            SkillFile sk = SkillFile.open(skillFilePath);
            assertEquals("testTool not deleted. Failing ok, because not yet implemented in SKilL.", 0, sk.Tools().size());
        } catch (IOException e) {
            fail();
        }
    }
}