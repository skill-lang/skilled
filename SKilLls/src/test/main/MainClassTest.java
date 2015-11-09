package main;

import org.junit.*;
import org.junit.runners.MethodSorters;
import sun.applet.Main;
import tools.*;

import java.io.*;
import java.io.File;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Armin Hüneburg
 * @since 03.09.15.
 * 
 * Tests functionalities that are unique to the main class.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainClassTest {
    private static String skillFilePath = "resources/.skills";
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
        String[] args = new String[] { "-e", "resources", "&n:twoTypeTool:2:Bathtub;twoTypeTool:2:Window;&n:oneTypeTool:2:Bathtub;oneTypeTool:2:Position;oneTypeTool:4:Chair:pos;&n:noTypeTool;"};
        MainClass.main(args);
    }

    @AfterClass
    public static void cleanup() {
        deleteDirectory(new File("resources" + File.separator + ".skills"));
    }
    
    /**
     * Tests if the help text is correct.
     */
    @SuppressWarnings("static-method")
	@Test
    public void test0Help() {
    	String[] args = new String[] { "-h" };
    	MainClass.main(args);
    	String text = "SYNOPSIS\n"
                + "       skillls\n"
                + "       [-a]  [--all]\n"
                + "       [-g  GENERATOR]  [--generator  GENERATOR]\n"
                + "       [-l  LANGUAGE] [--lang LANGUAGE]\n"
                + "       [-ls] [--list]\n"
                + "       [-o OUTPUT] [--output OUTPUT]\n"
                + "       [-x EXEC] [--exec EXEC]\n"
                + "       [-m MODULE] [--module MODULE]\n"
                + "       [-p PATH] [--path PATH]\n"
                + "       [TOOLS...]\n\n"
                + "DESCRIPTION\n"
                + "       SKilLls generates or lists tools with the given generator.\n"
                + "       It can also list all tools.\n\n"
                + "OPTIONS\n"
                + "       -a, --all\n"
                + "              When used with -ls/--list, lists all tools not regarding\n"
                + "              changes.  When used without -ls/--list, generates all\n"
                + "              tools not regarding changes.\n\n"
                + "       -g, --generator GENERATOR\n"
                + "              Needed for generating bindings. Path to the generator\n"
                + "              that is being used. Ignored with -ls/--list\n\n"
                + "       -l, --lang LANGUAGE\n"
                + "              Language the binding should be generated for.\n"
                + "              Ignored with -ls/--list.\n\n"
                + "       -o, --output OUTPUT\n"
                + "              The output directory for the binding.\n"
                + "              Ignored with -ls/--list.\n\n"
                + "       -x, --exec EXEC\n"
                + "              The execution environment for the generator,\n"
                + "              e.g. scala. Ignored with -ls/--list.\n\n"
                + "       -ls, --list\n"
                + "              Does not generate bindings but lists the tools.\n\n"
                + "       -m, --module MODULE\n"
                + "              The module the binding should be added to.\n"
                + "              Ignored with -ls/--list.\n\n"
                + "       -p, --path PATH\n"
                + "              The path the project is located at.\n\n"
                + "       TOOLS...\n"
                + "              The tools the options should be applied to.\n"
                + "              If no tools are given the options are applied to all\n"
                + "              available tools.\n\n\n"
                + "SIDE NOTE\n"
                + "       Single letter arguments can be combined. You can call SKilLls\n"
                + "       with following command:\n"
                + "       skillls -agl /path/to/generator Java /path/to/project\n"
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
    public void test1GenerateTwoTypeTool() {
        StringBuilder builder = new StringBuilder();
        builder.append("lib");
        builder.append(File.separator);
        builder.append("skill_2.11-0.3.jar");
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
        }
        String[] args = new String[] { "-agloxmp", builder.toString(), "Java", "generated",
                "scala", "twotypetool", "resources", "twoTypeTool" };
        MainClass.main(args);
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "twotypetool")));
        assertTrue("Bathtub not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator + "Bathtub.java")));
        assertTrue("Window not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator + "Window.java")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @Test
    public void test2GenerateTwoTypeWithTypeFromOtherFile() {
        StringBuilder builder = new StringBuilder();
        builder.append("lib");
        builder.append(File.separator);
        builder.append("skill_2.11-0.3.jar");
        try {
            Files.createDirectory(Paths.get("generated"));
        } catch (IOException ignored) {
        }
        String[] args = new String[] { "-agloxmp", builder.toString(), "Java", "generated",
                "scala", "onetypetool", "resources", "oneTypeTool" };
        MainClass.main(args);
        assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "onetypetool")));
        assertTrue("skill.java.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.java.common.jar")));
        assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
    }

    @Test
    public void test3ListingOfAlteredTools() {
        outStream = new ByteArrayOutputStream();
        errStream = new ByteArrayOutputStream();
        out = new PrintStream(outStream);
        err = new PrintStream(errStream);

        System.setOut(out);
        System.setErr(err);

        // Add random type so that the file is changed.
        SecureRandom random = new SecureRandom();
        String type = "a" + new BigInteger(130, random).toString(32);

        try {
            Files.write(Paths.get("resources" + File.separator + "Furniture.skill"), (type + " {}").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            fail();
        }

        String[] args = new String[] { "-lsp", "resources", "twoTypeTool" };
        MainClass.main(args);

        String[] got = outStream.toString().trim().split("\n");
        System.setOut(origOut);
        System.setErr(origErr);
        assertTrue("First line does not contain file.", got[0].endsWith("resources" + File.separator + "Furniture.skill"));
        assertTrue("SecondLine is not a type", got[1].trim().equals("Bathtub"));
        assertTrue("SecondLine is not a type", got[2].trim().isEmpty());
        assertTrue("SecondLine is not a type", got[3].trim().equals("Window"));
        int i = 4;
        String line;
        while (i < got.length && (line = got[i]) != null) {
            assertTrue("more output", line.trim().isEmpty());
        }

        try {
            RandomAccessFile raf = new RandomAccessFile("resources" + File.separator + "Furniture.skill", "rw");
            FileChannel channel = raf.getChannel();
            channel = channel.truncate(channel.size() - type.length() - 3);
            channel.close();
        } catch (IOException e) {
            fail();
        }

    }

    private static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }
}