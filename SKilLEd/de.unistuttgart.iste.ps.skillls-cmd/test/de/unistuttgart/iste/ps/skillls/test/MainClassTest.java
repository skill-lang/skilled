package de.unistuttgart.iste.ps.skillls.test;

import de.unistuttgart.iste.ps.skillls.main.BreakageException;
import de.unistuttgart.iste.ps.skillls.main.Indexing;
import de.unistuttgart.iste.ps.skillls.main.MainClass;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

	@BeforeClass
	public static void beforeClass() {
		origOut = System.out;
		origErr = System.err;
	}

	/**
	 * Sets the output and error stream of system.
	 */
	@Before
	public void setUp() {
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
		String[] args = new String[]{"-e", "resources",
				"&n:twoTypeTool:2:Bathtub;twoTypeTool:2:Window;&n:oneTypeTool:2:Bathtub;oneTypeTool:2:Position;oneTypeTool:4:Chair:pos;&n:noTypeTool;"};
		MainClass.main(args);
	}

	@AfterClass
	public static void afterClass() {
		deleteDirectory(new File("resources" + File.separator + ".skills"));
	}

	/**
	 * Tests if the help text is correct.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testHelp() {
		String[] args = new String[]{"-h"};
		MainClass.main(args);
		String text = "SYNOPSIS\n" + "       skillls\n" + "       [-a]  [--all]\n"
				+ "       [-g  GENERATOR]  [--generator  GENERATOR]\n" + "       [-l  LANGUAGE] [--lang LANGUAGE]\n"
				+ "       [-ls] [--list]\n" + "       [-o OUTPUT] [--output OUTPUT]\n"
				+ "       [-x EXEC] [--exec EXEC]\n" + "       [-m MODULE] [--module MODULE]\n"
				+ "       [-p PATH] [--path PATH]\n" + "       [TOOLS...]\n\n" + "DESCRIPTION\n"
				+ "       SKilLls generates or lists tools with the given generator.\n"
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
				+ "              The module the binding should be added to.\n"
				+ "              Ignored with -ls/--list.\n\n" + "       -p, --path PATH\n"
				+ "              The path the project is located at.\n\n" + "       TOOLS...\n"
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
		String builder = "lib" + File.separator + "skill_2.11-0.3.jar";
		try {
			Files.createDirectory(Paths.get("generated"));
		} catch (IOException ignored) {
			// ignored
		}
		String[] args = new String[]{"-agloxmp", builder, "Java", "generated", "scala", "twotypetool", "resources",
				"twoTypeTool"};
		System.setErr(origErr);
		System.setOut(origOut);
		MainClass.main(args);
		System.setErr(err);
		System.setOut(out);
		assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator
				+ "src" + File.separator + "main" + File.separator + "java" + File.separator + "twotypetool")));
		assertTrue("Bathtub not generated",
				Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
						+ "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
						+ "Bathtub.java")));
		assertTrue("Window not generated",
				Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
						+ "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
						+ "Window.java")));
		assertTrue("skill.java.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.java.common.jar")));
		assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
	}

	@Test
	public void testGenerateTwoTypeToolWithLongParameters() {
		System.setOut(origOut);
		String builder = "lib" + File.separator + "skill_2.11-0.3.jar";
		try {
			Files.createDirectory(Paths.get("generated"));
		} catch (IOException ignored) {
			// ignored
		}
		String[] args = new String[]{"--all", "--generator", builder, "--lang", "Java", "--output", "generated",
				"--exec", "scala", "--module", "twotypetool", "--path", "resources", "twoTypeTool"};
		MainClass.start(Indexing.NO_INDEXING, args);
		assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator
				+ "src" + File.separator + "main" + File.separator + "java" + File.separator + "twotypetool")));
		assertTrue("Bathtub not generated",
				Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
						+ "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
						+ "Bathtub.java")));
		assertTrue("Window not generated",
				Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
						+ "main" + File.separator + "java" + File.separator + "twotypetool" + File.separator
						+ "Window.java")));
		assertTrue("skill.java.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.java.common.jar")));
		assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
	}

	@Test
	public void testGenerateTwoTypeWithTypeHint() {
		String[] args = new String[]{"--edit", "resources",
				"oneTypeTool:2:PowerStrip;oneTypeTool:8:PowerStrip:!readOnly"};
		MainClass.main(args);
		String builder = "lib" + File.separator + "skill_2.11-0.3.jar";
		try {
			Files.createDirectory(Paths.get("generated"));
		} catch (IOException ignored) {
			// ignored
		}
		args = new String[]{"-agloxmp", builder, "Java", "generated", "scala", "onetypetool", "resources",
				"oneTypeTool"};
		MainClass.main(args);
		assertTrue("not generated", Files.exists(Paths.get("generated" + File.separator + "java" + File.separator
				+ "src" + File.separator + "main" + File.separator + "java" + File.separator + "onetypetool")));
		assertTrue("skill.java.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.java.common.jar")));
		assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
	}

	@Test
	public void testGenerateAll() {
		String builder = "lib" + File.separator + "skill_2.11-0.3.jar";
		try {
			Files.createDirectory(Paths.get("generated"));
		} catch (IOException ignored) {
			// ignored
		}
		String[] args = new String[]{"--all", "--generator", builder, "--lang", "Java", "--output", "generated",
				"--exec", "scala", "--module", "this.is.a.test", "--path", "resources"};
		MainClass.main(args);
		assertTrue("not generated",
				Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
						+ "main" + File.separator + "java" + File.separator + "this" + File.separator + "is"
						+ File.separator + "a" + File.separator + "test")));
		assertTrue("Bathtub not generated",
				Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
						+ "main" + File.separator + "java" + File.separator + "this" + File.separator + "is"
						+ File.separator + "a" + File.separator + "test" + File.separator + "Bathtub.java")));
		assertTrue("Window not generated",
				Files.exists(Paths.get("generated" + File.separator + "java" + File.separator + "src" + File.separator
						+ "main" + File.separator + "java" + File.separator + "this" + File.separator + "is"
						+ File.separator + "a" + File.separator + "test" + File.separator + "Window.java")));
		assertTrue("skill.java.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.java.common.jar")));
		assertTrue("skill.jvm.common.jar missing", Files.exists(Paths.get("generated" + File.separator + "java"
				+ File.separator + "lib" + File.separator + "skill.jvm.common.jar")));
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

		String[] args = new String[]{"--all", "--list", "--path", "resources"};
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
		assertEquals("line not correct", "twoTypeTool", got[0]);
		assertEquals("line not correct", "  resources/Furniture.skill", got[1]);
		assertEquals("line not correct", "    Bathtub", got[2]);
		assertEquals("line not correct", "    Window", got[4]);
		assertEquals("line not correct", "oneTypeTool", got[6]);
		assertEquals("line not correct", "  resources/Furniture.skill", got[7]);
		assertEquals("line not correct", "    Bathtub", got[8]);
		assertEquals("line not correct", "    Chair", got[10]);
		assertEquals("line not correct", "      Position pos", got[11]);
		assertEquals("line not correct", "  resources/Utility.skill", got[13]);
		assertEquals("line not correct", "    Position", got[14]);
		assertEquals("line not correct", "noTypeTool", got[16]);
		assertTrue("No Output", got.length != 0);
	}

	@SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
	@Test
	public void testListShortParamAll() {
		outStream = new ByteArrayOutputStream();
		errStream = new ByteArrayOutputStream();
		out = new PrintStream(outStream);
		err = new PrintStream(errStream);

		System.setOut(out);
		System.setErr(err);

		MainClass.main(new String[]{"-e", "resources",
				"oneTypeTool:8:Chair:!singleton;oneTypeTool:4:Chair:color;oneTypeTool:6:Chair:color:!ignore"});
		String[] args = new String[]{"-alsp", "resources"};
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
		for (String s : got) {
			System.out.println(s);
		}
		assertEquals("line not correct", "twoTypeTool", got[0]);
		assertEquals("line not correct", "  resources/Furniture.skill", got[1]);
		assertEquals("line not correct", "    Bathtub", got[2]);
		assertEquals("line not correct", "    Window", got[4]);
		assertEquals("line not correct", "oneTypeTool", got[6]);
		if (got[7].contains("Furniture")) {
			assertEquals("line not correct", "  resources/Furniture.skill", got[7]);
			assertEquals("line not correct", "    Bathtub", got[8]);
			assertEquals("line not correct", "    !singleton", got[10]);
			assertEquals("line not correct", "    Chair", got[11]);
			assertEquals("line not correct", "      Position pos", got[12]);
			assertEquals("line not correct", "      !ignore", got[13]);
			assertEquals("line not correct", "      Color color", got[14]);
			assertEquals("line not correct", "  resources/Utility.skill", got[16]);
			assertEquals("line not correct", "    Position", got[17]);
		} else {
			assertEquals("line not correct", "  resources/Utility.skill", got[7]);
			assertEquals("line not correct", "    Position", got[8]);
			assertEquals("line not correct", "  resources/Furniture.skill", got[10]);
			assertEquals("line not correct", "    Bathtub", got[11]);
			assertEquals("line not correct", "    !singleton", got[13]);
			assertEquals("line not correct", "    Chair", got[14]);
			assertEquals("line not correct", "      Position pos", got[15]);
			assertEquals("line not correct", "      !ignore", got[16]);
			assertEquals("line not correct", "      Color color", got[17]);
		}
		assertEquals("line not correct", "noTypeTool", got[19]);
		assertTrue("No Output", got.length != 0);
	}

	@Test
	public void testBreakage() {
		System.setErr(origErr);
		System.setOut(origOut);
		try {
			Files.move(Paths.get("resources" + File.separator + "Furniture.skill"),
					Paths.get("testFiles" + File.separator + "Furniture.skill"));
			if (Files.exists(Paths.get(skillFilePath))) {
				Files.delete(Paths.get(skillFilePath));
			}
			Files.copy(Paths.get("testFiles" + File.separator + "CorrectInput" + File.separator + "AddField.sf"),
					Paths.get(skillFilePath));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		String[] args = new String[]{"-e", "resources", ""};
		boolean failed = false;
		try {
			MainClass.start(Indexing.JUST_INDEXING, args);
			failed = true;
		} catch (Throwable t) {
			String message = t.getMessage().replace("\n", "").replace("\r", "");
			boolean correctMessage = message.matches(
					"The following tools are broken: +- (oneTypeTool|twoTypeTool) +- (oneTypeTool|twoTypeTool)");
			assertTrue("Wrong message", correctMessage);
			assertTrue("not a BreakageException", t instanceof BreakageException);
			assertEquals("wrong number of tools", 2, ((BreakageException) t).getTools().length);
		}
		try {
			Files.move(Paths.get("testFiles" + File.separator + "Furniture.skill"),
					Paths.get("resources" + File.separator + "Furniture.skill"));
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

	@Test
	public void testReindexing() {
		System.setErr(origErr);
		System.setOut(origOut);

		boolean nullFound = false, failed = false;
		int types = -1;
		try {
			Files.deleteIfExists(Paths.get(skillFilePath));
			Files.createFile(Paths.get(skillFilePath));
			MainClass.start(Indexing.JUST_INDEXING, new String[]{"-e", "resources", ""});
		} catch (BreakageException e) {
			// ignored
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			SkillFile sk = SkillFile.open(Paths.get(skillFilePath),
					de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
			types = sk.Types().size();
		} catch (IOException e) {
			e.printStackTrace();
			failed = true;
		}

		MainClass.start(Indexing.JUST_INDEXING, new String[]{"-e", "resources", ""});

		System.setErr(err);
		System.setOut(out);

		try {
			SkillFile sk = SkillFile.open(Paths.get(skillFilePath),
					de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
			for (Tool tool : sk.Tools()) {
				for (Type type : tool.getTypes()) {
					nullFound |= type == null;
				}
			}
			assertFalse("Null found", nullFound);
			assertTrue("Too many Types " + sk.Types().size() + " > " + types, types >= sk.Types().size());
		} catch (IOException e) {
			e.printStackTrace();
			failed = true;
		}

		if (failed) {
			fail();
		}
	}

	@Test
	public void testReindexingTypeDoubling() {
		System.setErr(origErr);
		System.setOut(origOut);

		boolean nullFound = false, failed = false;
		int types = -1;
		try {
			Files.deleteIfExists(Paths.get(skillFilePath));
			Files.createFile(Paths.get(skillFilePath));
			MainClass.start(Indexing.JUST_INDEXING, new String[]{"-e", "resources", ""});
		} catch (BreakageException e) {
			// ignored
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			SkillFile sk = SkillFile.open(Paths.get(skillFilePath),
					de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
			types = sk.Types().size();
		} catch (IOException e) {
			e.printStackTrace();
			failed = true;
		}

		MainClass.start(Indexing.JUST_INDEXING, new String[]{"-e", "resources", ""});
		MainClass.start(Indexing.JUST_INDEXING, new String[]{"-e", "resources", ""});
		MainClass.start(Indexing.JUST_INDEXING, new String[]{"-e", "resources", ""});
		MainClass.start(Indexing.JUST_INDEXING, new String[]{"-e", "resources", ""});

		System.setErr(err);
		System.setOut(out);

		try {
			SkillFile sk = SkillFile.open(Paths.get(skillFilePath),
					de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
			for (Tool tool : sk.Tools()) {
				for (Type type : tool.getTypes()) {
					nullFound |= type == null;
				}
			}
			assertFalse("Null found", nullFound);
			assertTrue("Too many Types " + sk.Types().size() + " > " + types, types >= sk.Types().size());
		} catch (IOException e) {
			e.printStackTrace();
			failed = true;
		}

		if (failed) {
			fail();
		}
	}

	@Test
	public void testTypeHintDisappearance() {
		System.setOut(origOut);

		MainClass.main(new String[]{"-e", "resources",
				"&n:testTool:2:PowerStrip;testTool:8:PowerStrip:!readOnly;testTool:9:PowerStrip:!readOnly"});
		try {
			int amountHints = 0;
			SkillFile sk = SkillFile.open(Paths.get(skillFilePath),
					de.ust.skill.common.java.api.SkillFile.Mode.ReadOnly);
			for (Type type : sk.Types()) {
				if (!type.getName().equals("PowerStrip") || type.getOrig() != null) {
					continue;
				}
				amountHints = type.getHints().size();
			}
			assertEquals("PowerStrip has wrong amount of Hints", 1, amountHints);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.setOut(out);
	}
}
