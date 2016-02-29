package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.tools.File;
import de.unistuttgart.iste.ps.skillls.tools.Tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

/**
 * Created on 16.02.16.
 *
 * @author Armin Hüneburg
 *
 *         Class for generating the binding of a tool.
 */
public class Generator implements Runnable {
	private final Tool tool;
	private final String path;
	private String mainFile;
	private final boolean completeCleanup;
	private final String command;
	private final java.io.File parent;

	/**
	 * Constructor. Sets the variables the instance needs.
	 * 
	 * @param tool
	 *            the tool this instance should generate
	 * @param project
	 *            the path that contains the project.
	 * @param completeCleanup
	 *            variable deciding whether only the main or all temporary files
	 *            should be deleted.
	 * @param command
	 *            the command to be executed.
	 * @param workingDirectory
	 *            the working directory.
	 */
	public Generator(Tool tool, String project, boolean completeCleanup, String command,
			java.io.File workingDirectory) {
		this.tool = tool;
		this.path = project + java.io.File.separator + ".skillt" + java.io.File.separator + tool.getName();
		this.completeCleanup = completeCleanup;
		this.command = command;
		this.parent = workingDirectory;
	}

	/**
	 * Cleans the files that were produced during the generation
	 * 
	 * @throws IOException
	 *             throws same exception as
	 *             {@link java.nio.file.Files#delete(Path)}
	 */
	private void cleanup() throws IOException {
		Files.delete(Paths.get(mainFile));
		if (completeCleanup) {
			deleteDirectory(Paths.get(path));
		}
	}

	/**
	 * Deletes a directory recursively.
	 * 
	 * @param path
	 *            The file that should be deleted.
	 */
	private void deleteDirectory(Path path) {
		if (Files.isDirectory(path)) {
			try {
				Files.list(path).forEach(this::deleteDirectory);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * runs the generator to generate the skill binding
	 * 
	 * @throws IOException
	 *             {@link Runtime#exec(String, String[], java.io.File)} or
	 *             {@link BufferedReader#readLine()}
	 * @throws InterruptedException
	 *             throws exception at the same time as
	 *             {@link Process#waitFor()}
	 */
	private void runGenerator() throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(String.format(command, mainFile), null, parent.getParentFile());
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String line;
		boolean knownError = false;
		while ((line = errorReader.readLine()) != null) {
			// skip output of not being able to copy deps
			if (line.contains("java.nio.file.NoSuchFileException: deps/skill.jvm.common.jar") || knownError) {
				knownError = true;
			} else {
				System.out.println(line);
			}
		}
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

	/**
	 * Creates the file that includes every other file.
	 * 
	 * @throws IOException
	 *             throws exception at the same time as
	 *             {@link Files#write(Path, byte[], OpenOption...)}
	 */
	private void makeMain() throws IOException {
		mainFile = path + java.io.File.separator + ".." + java.io.File.separator + tool.getName() + "-Mainfile.skill";
		StringBuilder builder = new StringBuilder();
		for (File file : tool.getFiles()) {
			builder.append("include \"");
			builder.append(tool.getName());
			builder.append('/');
			builder.append(file.getPath().substring(file.getPath().indexOf(java.io.File.separatorChar) + 1));
			builder.append('"');
			builder.append(System.getProperty("line.separator"));
		}
		Files.write(Paths.get(mainFile), builder.toString().getBytes());
	}

	/**
	 * Creates the directories for the temporary files
	 * 
	 * @throws IOException
	 *             throws exception at the same time as
	 *             {@link Files#createDirectories(Path, FileAttribute[])}
	 */
	private void makeStructure() throws IOException {
		createDirectories(Paths.get(path));
	}

	/**
	 * Creates the complete path for a directory and the directory itself.
	 * 
	 * @param path
	 *            the path to the directory, that should be created.
	 * @throws IOException
	 *             thrown when access was denied.
	 */
	private static void createDirectories(Path path) throws IOException {
		if (!(Files.exists(path) && Files.isDirectory(path))) {
			createDirectories(path.getParent());
			Files.createDirectory(path);
		} else if (!Files.exists(path)) {
			Files.createDirectory(path);
		}
	}

	/**
	 * Runs the generation as an extra thread (if executed with
	 * {@link Thread#start()}
	 */
	@Override
	public void run() {
		try {
			makeStructure();
			makeMain();
			runGenerator();
			cleanup();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
