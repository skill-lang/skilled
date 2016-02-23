package de.unistuttgart.iste.ps.skillls.test;

import de.unistuttgart.iste.ps.skillls.main.BreakageException;
import de.unistuttgart.iste.ps.skillls.main.CleanUpAssistant;
import de.unistuttgart.iste.ps.skillls.main.Indexing;
import de.unistuttgart.iste.ps.skillls.main.MainClass;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;


/**
 * @author Armin Hüneburg
 * @since 03.09.15.
 * 
 *        Tests functionalities that are unique to the main class.
 */
public class MainClassTest {
    private static final String skillFilePath = "resources/.skills";
    private static ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    private static ByteArrayOutputStream errStream = new ByteArrayOutputStream();
    private static PrintStream out = new PrintStream(outStream);
    private static PrintStream err = new PrintStream(errStream);
    private static PrintStream origOut;
    private static PrintStream origErr;

    /**
     * Sets the output and error stream of system.
     */
    @BeforeClass
    public static void setUp() {
        origOut = System.out;
        origErr = System.err;
        System.setErr(err);
        System.setOut(out);
        try {
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.createFile(Paths.get(skillFilePath));
        } catch (IOException e) {
            throw new RuntimeException();
        }
        String[] args = new String[] { "-e", "resources",
                "&n:twoTypeTool:2:Bathtub;twoTypeTool:2:Window;&n:oneTypeTool:2:Bathtub;oneTypeTool:2:Position;oneTypeTool:4:Chair:pos;&n:noTypeTool;" };
        MainClass.main(args);
    }

    @AfterClass
    public static void afterClass() {
        deleteDirectory(new File("resources" + File.separator + ".skills"));
    }

    @After
    public void cleanup() {
        CleanUpAssistant.renewInstance();
    }

    /**
     * Tests if the help text is correct.
     */
    @SuppressWarnings("static-method")
    @Test
    public void testHelp() {
        String[] args = new String[] { "-h" };
        MainClass.main(args);
        String text = "SYNOPSIS\n" + "       skillls\n" + "       [-a]  [--all]\n"
                + "       [-g  GENERATOR]  [--generator  GENERATOR]\n" + "       [-l  LANGUAGE] [--lang LANGUAGE]\n"
                + "       [-ls] [--list]\n" + "       [-o OUTPUT] [--output OUTPUT]\n" + "       [-x EXEC] [--exec EXEC]\n"
                + "       [-m MODULE] [--module MODULE]\n" + "       [-p PATH] [--path PATH]\n" + "       [TOOLS...]\n\n"
                + "DESCRIPTION\n" + "       SKilLls generates or lists tools with the given generator.\n"
                + "       It can also list all tools.\n\n" + "OPTIONS\n" + "       -a, --all\n"
                + "              When used with -ls/--list, lists all tools not regarding\n"
                + "              changes.  When used without -ls/--list, generates all\n"
                + "              tools not regarding changes.\n\n" + "       -g, --generator GENERATOR\n"
                + "              Needed for generating bindings. Path to the generator\n"
                + "              that is being used. Ignored with -ls/--list\n\n" + "       -l, --lang LANGUAGE\n"
                + "              Language the binding should be generated for.\n"
                + "              Ignored with -ls/--list.\n\n" + "       -o, --output OUTPUT\n"
                + "              The output directory for the binding.\n" + "              Ignored with -ls/--list.\n\n"
                + "       -x, --exec EXEC\n" + "              The execution environment for the generator,\n"
                + "              e.g. scala. Ignored with -ls/--list.\n\n" + "       -ls, --list\n"
                + "              Does not generate bindings but lists the tools.\n\n" + "       -m, --module MODULE\n"
                + "              The module the binding should be added to.\n" + "              Ignored with -ls/--list.\n\n"
                + "       -p, --path PATH\n" + "              The path the project is located at.\n\n" + "       TOOLS...\n"
                + "              The tools the options should be applied to.\n"
                + "              If no tools are given the options are applied to all\n"
                + "              available tools.\n\n\n" + "SIDE NOTE\n"
                + "       Single letter arguments can be combined. You can call SKilLls\n"
                + "       with following command:\n" + "       skillls -agl /path/to/generator Java /path/to/project\n"
                + "       This command generates all bindings for the tools of the project\n"
                + "       in Java. If l comes before g the language has to be given first.\n"
                + "       If no options are given the settings, stored in\n"
                + "       /path/to/project/.skills, are used.\n";
        String got = outStream.toString().trim();
        assertEquals(text.trim(), got);
        System.setErr(origErr);
        System.setOut(origOut);
    }

    @Before
    public void deleteGenStuff() {
        if (new File("generated").exists())
            deleteDirectory(new File("generated"));

        if (new File("resources" + File.separator + ".skillt").exists())
            deleteDirectory(new File("resources" + File.separator + ".skillt"));
    }

    @Test
    public void testGenerateTwoTypeTool() {
        String builder = "lib" +
                File.separator +
                "skill_2.11-0.3.jar";
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
            // ignored
        }
        String[] args = new String[] { "-agloxmp", builder, "Java", "generated", "scala", "twotypetool",
                "resources", "twoTypeTool" };
        System.setErr(origErr);
        System.setOut(origOut);
        MainClass.main(args);
        System.setErr(err);
        System.setOut(out);
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src"
                + File.separator + "main" + File.separator + "java" + File.separator + "twotypetool")));
        assertTrue("Bathtub not generated",
                Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
                        + "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
                        + "Bathtub.java")));
        assertTrue("Window not generated",
                Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
                        + "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
                        + "Window.java")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @Test
    public void testGenerateTwoTypeToolWithLongParameters() {
        String builder = "lib" +
                File.separator +
                "skill_2.11-0.3.jar";
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
            // ignored
        }
        String[] args = new String[] { "--all", "--generator", builder, "--lang", "Java", "--output", "generated",
                "--exec", "scala", "--module", "twotypetool", "--path", "resources", "twoTypeTool" };
        MainClass.start(Indexing.NO_INDEXING, args);
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src"
                + File.separator + "main" + File.separator + "java" + File.separator + "twotypetool")));
        assertTrue("Bathtub not generated",
                Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
                        + "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
                        + "Bathtub.java")));
        assertTrue("Window not generated",
                Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
                        + "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
                        + "Window.java")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @Test
    public void testGenerateTwoTypeWithTypeHint() {
        String[] args = new String[] {"--edit", "resources", "oneTypeTool:2:PowerStrip;oneTypeTool:8:PowerStrip:!readOnly"};
        MainClass.main(args);
        String builder = "lib" +
                File.separator +
                "skill_2.11-0.3.jar";
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
            // ignored
        }
        args = new String[] { "-agloxmp", builder, "Java", "generated", "scala", "onetypetool",
                "resources", "oneTypeTool" };
        MainClass.main(args);
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src"
                + File.separator + "main" + File.separator + "java" + File.separator + "onetypetool")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @Test
    public void testGenerateAll() {
        String builder = "lib" +
                File.separator +
                "skill_2.11-0.3.jar";
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
            // ignored
        }
        String[] args = new String[] { "--all", "--generator", builder, "--lang", "Java", "--output", "generated",
                "--exec", "scala", "--module", "this.is.a.test", "--path", "resources" };
        MainClass.main(args);
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src"
                + File.separator + "main" + File.separator + "java" + File.separator + "this" + File.separator + "is" +
                File.separator + "a" + File.separator + "test")));
        assertTrue("Bathtub not generated",
                Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
                        + "main" + File.separator + "java" + File.separator + "this" + File.separator + "is" +
                        File.separator + "a" + File.separator + "test" + File.separator + "Bathtub.java")));
        assertTrue("Window not generated",
                Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
                        + "main" + File.separator + "java" + File.separator + "this" + File.separator + "is" +
                        File.separator + "a" + File.separator + "test" + File.separator + "Window.java")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get(
                "generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
    @Test
    public void testListLongParamAll() {
        outStream = new ByteArrayOutputStream();
        errStream = new ByteArrayOutputStream();
        out = new PrintStream(outStream);
        err = new PrintStream(errStream);

        System.setOut(out);
        System.setErr(err);

        String[] args = new String[] { "--list", "--path", "resources" };
        try {
            MainClass.main(args);
        } catch (Error e) {
            if (e.getMessage().equals("TODO")) {
                fail("OK, not implemented");
            } else {
                fail(e.getMessage());
            }
        }

        String[] got = outStream.toString().trim().split("\n");
        System.setOut(origOut);
        System.setErr(origErr);
        assertTrue("No Output", got.length != 0);
    }

    @Test
    public void testBreakage() {
        CleanUpAssistant.renewInstance();
        System.setErr(origErr);
        System.setOut(origOut);
        try {
            Files.move(Paths.get("resources" + File.separator + "Furniture.skill"), Paths.get("testFiles" + File.separator + "Furniture.skill"));
            if (Files.exists(Paths.get(skillFilePath))) {
                Files.delete(Paths.get(skillFilePath));
            }
            Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddField.sf"), Paths.get(skillFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        String[] args = new String[] { "-e", "resources", "" };
        boolean failed = false;
        try {
            MainClass.start(Indexing.JUST_INDEXING, args);
            failed = true;
        } catch (Throwable t) {
            assertTrue("not a BreakageException", t instanceof BreakageException);
        }
        try {
            Files.move(Paths.get("testFiles" + File.separator + "Furniture.skill"), Paths.get("resources" + File.separator + "Furniture.skill"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setErr(err);
        System.setOut(out);
        if (failed) {
            fail("exception not thrown");
        }
    }

    public static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File f : children) {
                    deleteDirectory(f);
                }
            }
        }
        file.delete();
    }
}
