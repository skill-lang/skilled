package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.grammar.SKilLLexer;
import de.unistuttgart.iste.ps.skillls.grammar.SKilLParser;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Utility Class for the MainClass
 *
 * Created on 29.02.16.
 *
 * @author Armin Hüneburg
 */
public class MainUtil {
	/**
	 * Prints the type content
	 *
	 * @param type
	 *            the type whose content should be printed.
	 */
	public static void listTypeContent(Type type) {
		// print hints
		for (Hint hint : type.getHints()) {
			System.out.println("    " + hint.getName());
		}
		// print type
		System.out.println("    " + type.getName());
		// print fields
		for (Field field : type.getFields()) {
			for (Hint hint : field.getHints()) {
				// print field hints
				System.out.println("      " + hint.getName());
			}
			System.out.println("      " + field.getName());
		}
		System.out.println();
	}

	/**
	 * Evaluates arguments which don't have a leading dash.
	 *
	 * @param args
	 *            The array containing the arguments.
	 * @param index
	 *            The current position in {@code args}.
	 * @return Returns an object with the unambiguous text what action has to
	 *         perform and the new index.
	 */
	public static ArgumentEvaluation noDashArg(String[] args, int index) {
		return new ArgumentEvaluation(index, args[index], Integer.toString(index));
	}

	/**
	 * parses a character that is interpreted as argument
	 *
	 * @param args
	 *            the arguments of the command line
	 * @param last
	 *            the last character that was read
	 * @param globalIndex
	 *            the index of the last argument parsed
	 * @param list
	 *            the list of argument evaluations
	 * @param c
	 *            the current character
	 * @return returns the index of the last argument parsed
	 */
	public static int parseCharParams(String[] args, char last, int globalIndex, ArrayList<ArgumentEvaluation> list,
			char c) {
		int index = globalIndex;
		switch (c) {
			case 'g' :
				// test if l was typed
				index = wasLastCharL(index, list, last, args[index + 1]);
				index = addEvaluation(index, args, "generator", list);
				break;

			case 'a' :
				// test if l was typed
				index = wasLastCharL(index, list, last, args[index + 1]);
				list.add(new ArgumentEvaluation(index, null, "all"));
				break;

			case 'l' :
				break;

			case 'x' :
				// test if l was typed
				index = wasLastCharL(index, list, last, args[index + 1]);
				index = addEvaluation(index, args, "exec", list);
				break;

			case 'p' :
				// test if l was typed
				index = wasLastCharL(index, list, last, args[index + 1]);
				index = addEvaluation(index, args, "path", list);
				break;

			case 'o' :
				// test if l was typed
				index = wasLastCharL(index, list, last, args[index + 1]);
				index = addEvaluation(index, args, "output", list);
				break;

			case 'm' :
				// test if l was typed
				index = wasLastCharL(index, list, last, args[index + 1]);
				index = addEvaluation(index, args, "module", list);
				break;

			case 's' :
				// test if l was typed, if yes it is list else invalid
				if (last == 'l') {
					list.add(new ArgumentEvaluation(index, args[index], "list"));
					break;
				}
				throw new IllegalArgumentException();

			default :
				throw new IllegalArgumentException();
		}
		return index;
	}

	/**
	 * Creates an {@link ArgumentEvaluation} object from a command line argument
	 *
	 * @param index
	 *            the index of the last parsed argument
	 * @param args
	 *            the command line arguments
	 * @param key
	 *            the key, that should be used in the evaluation
	 * @param evaluations
	 *            the list, the evaluation should be added to
	 * @return the index of the last parsed argument
	 */
	public static int addEvaluation(int index, String[] args, String key, ArrayList<ArgumentEvaluation> evaluations) {
		evaluations.add(new ArgumentEvaluation(index, args[index + 1], key));
		return index + 1;
	}

	/**
	 * Checks whether the last char was an L and sets the language argument if
	 * it was.
	 *
	 * @param globalIndex
	 *            the index of the current argument
	 * @param list
	 *            the list of argument evaluations
	 * @param c
	 *            the char which was the one before the current
	 * @param arg
	 *            the argument at globalIndex
	 * @return new globalIndex
	 */
	public static int wasLastCharL(int globalIndex, ArrayList<ArgumentEvaluation> list, char c, String arg) {
		int index = globalIndex;
		if (c == 'l') {
			index++;
			list.add(new ArgumentEvaluation(index, arg, "lang"));
		}
		return index;
	}

	/**
	 * Evaluates arguments which have two leading dashes.
	 *
	 * @param args
	 *            The array containing the arguments.
	 * @param globalIndex
	 *            The current position in {@code args}.
	 * @return Returns an object with the unambiguous text what action has to
	 *         perform and the new index.
	 */
	public static ArgumentEvaluation doubleDashArg(String[] args, int globalIndex) {
		int index = globalIndex;
		switch (args[index]) {
			case "--all" :
				return new ArgumentEvaluation(index, null, "all");

			case "--lang" :
			case "--generator" :
			case "--exec" :
			case "--path" :
			case "--output" :
			case "--module" :
				index++;
				return new ArgumentEvaluation(index, args[index], args[index - 1].substring(2));

			case "--list" :
				return new ArgumentEvaluation(index, null, "list");

			case "--no-cleanup" :
				return new ArgumentEvaluation(index, null, "cleanup");

			default :
				throw new IllegalArgumentException();
		}
	}

	/**
	 * Prints the message saved in {@link Constants#HELPTEXT} to the command
	 * line.
	 */
	public static void printHelp() {
		System.out.println(Constants.HELPTEXT);
	}

	/**
	 * Method for creating the temporary file for the tool.
	 *
	 * @param tool
	 *            The tool the file should be generated for.
	 * @param file
	 *            The file that should be optimized for the tool.
	 * @param project
	 *            The file containing the project.
	 * @return The file object of the temporary file.
	 * @throws IOException
	 *             Thrown if there is a problem with creating temporary files.
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static File createToolFile(File project, Tool tool, File file) throws IOException {
		File tempDir = new File(project.getAbsolutePath() + File.separator + ".skillt");
		tempDir = new File(tempDir, tool.getName());
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		Path relativizedPath = Paths.get(project.getAbsolutePath()).relativize(Paths.get(file.getAbsolutePath()))
				.normalize();

		File newFile = new File(tempDir, relativizedPath.toString());

		if (!newFile.getParentFile().exists()) {
			createDirectory(newFile.getParentFile());
		}
		newFile.createNewFile();
		try (FileOutputStream fs = new FileOutputStream(newFile.getAbsolutePath())) {
			Lexer lexer;
			try {
				lexer = new SKilLLexer(new ANTLRFileStream(file.getAbsolutePath()));
			} catch (IOException e) {
				ExceptionHandler.handle(e);
				return null;
			}
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			SKilLParser parser = new SKilLParser(tokens);
			parser.addParseListener(new SkillExtractListener(fs, file, tool));
			// Call has side effect: the .skill-file is parsed.
			parser.file();
		}
		return newFile;
	}

	/**
	 * Creates a directory and its parents if they don't exist
	 *
	 * @param file
	 *            the complete directory path
	 */
	public static void createDirectory(File file) {
		if (!file.getParentFile().exists()) {
			createDirectory(file.getParentFile());
		}
		// noinspection ResultOfMethodCallIgnored
		file.mkdir();
	}

	/**
	 * Encodes a byte array to a hex-String.
	 *
	 * @param digest
	 *            The array that should be encoded.
	 * @return The hex-String equivalent to the byte array.
	 */
	public static String encodeHex(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	/**
	 * Indexes the types of a file.
	 *
	 * @param file
	 *            The file containing the types.
	 * @param skillFile
	 *            SkillFile containing the types in the end.
	 */
	public static void indexTypes(File file, SkillFile skillFile) {
		Lexer lexer;
		try {
			lexer = new SKilLLexer(new ANTLRFileStream(file.getAbsolutePath()));
		} catch (IOException e) {
			ExceptionHandler.handle(e);
			return;
		}
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SKilLParser parser = new SKilLParser(tokens);
		parser.addParseListener(new SkillIndexListener(skillFile, file));
		// Call has side effect: the .skill-file is parsed.
		@SuppressWarnings({"unused"})
		ParseTree tree = parser.file();
	}

	/**
	 * Creates a MD5 hash of a file and transforms it into a hexadecimal String.
	 *
	 * @param file
	 *            The file that should be hashed.
	 * @return The MD5 hash of the given file as hexadecimal string.
	 */
	public static String hash(File file) {
		try (InputStream is = Files.newInputStream(Paths.get(file.getAbsolutePath()))) {
			MessageDigest md5;
			try {
				md5 = MessageDigest.getInstance("md5");
			} catch (NoSuchAlgorithmException e) {
				ExceptionHandler.handle(e);
				return "";
			}
			DigestInputStream dis = new DigestInputStream(is, md5);
			byte[] bytes = new byte[4096];
			while (dis.read(bytes, 0, bytes.length) != -1) {
				// digest file
			}
			bytes = md5.digest();
			return encodeHex(bytes);
		} catch (IOException e) {
			ExceptionHandler.handle(e);
			return "";
		}
	}
}
