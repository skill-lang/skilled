package de.unistuttgart.iste.ps.skillls.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.unistuttgart.iste.ps.skillls.editor.Editor;

import de.unistuttgart.iste.ps.skillls.tools.Generator;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

/**
 * This class starts the application and runs the generator if necessary.
 *
 * @author Armin Hüneburg
 * @since 18.08.15.
 */
public class MainClass {
	private Generator generator;
	private String language;
	private FileFlag fileFlag = FileFlag.Changed;
	private String module;
	private File output;
	private boolean cleanUp = true;
	private String path;
	private SkillFile skillFile;

	/**
	 * Entry point. Sets the exception handler to not rethrow exceptions as
	 * errors.
	 *
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {
		ExceptionHandler.setRethrow(false);
		start(Indexing.NORMAL, args);
	}

	/**
	 * Generates flags for the generator execution.
	 *
	 * @param indexing
	 *            Determines whether files should be indexed or not
	 * @param args
	 *            The command line arguments.
	 */
	public static void start(Indexing indexing, String[] args) {
		CleanUpAssistantWrapper cleanUpAssistantWrapper = new CleanUpAssistantWrapper();
		MainClass mainClass = new MainClass();
		switch (args[0]) {
			case "-e" :
			case "--edit" :
				mainClass.startEditor(indexing, args, cleanUpAssistantWrapper);
				break;

			case "-h" :
			case "--help" :
				MainUtil.printHelp();
				break;

			default :
				mainClass.prepareGeneration(indexing, args, cleanUpAssistantWrapper);
		}
		if (cleanUpAssistantWrapper.get() != null && indexing != Indexing.NO_INDEXING) {
			cleanUpAssistantWrapper.get().analyseBreakage();
		}
	}

	/**
	 * Prepares everything for the generation of bindings and temporary files
	 * 
	 * @param indexing
	 *            whether indexing should happen, or not and whether other stuff
	 *            should happen, or not
	 * @param args
	 *            the arguments given on the command line
	 * @param cleanUpAssistantWrapper
	 *            the wrapper that saves the {@link CleanUpAssistant} instance
	 */
	private void prepareGeneration(Indexing indexing, String[] args, CleanUpAssistantWrapper cleanUpAssistantWrapper) {
		HashMap<String, ArgumentEvaluation> evaluations = new HashMap<>();

		for (int i = 0; i < args.length; i++) {
			ArgumentEvaluation[] evals = evaluateArgument(args, i);
			if (evals != null) {
				Arrays.stream(evals).forEach(e -> evaluations.put(e.getName(), e));
			}
			for (ArgumentEvaluation e : evaluations.values()) {
				i = i > e.getIndex() ? i : e.getIndex();
			}
		}

		setFlags(evaluations);

		if (evaluations.containsKey("list")) {
			list(evaluations.get("path").getArgument() + File.separator + ".skills", evaluations);
			return;
		}
		try {
			skillFile = SkillFile.open(evaluations.get("path").getArgument() + File.separator + ".skills",
					SkillFile.Mode.Write, SkillFile.Mode.Read);
		} catch (IOException e) {
			ExceptionHandler.handle(e);
			return;
		}
		buildDependencies(skillFile);
		ArrayList<String> tools = new ArrayList<>();
		// set the environment of the generation.
		parseArguments(evaluations, tools);
		index(indexing, cleanUpAssistantWrapper, new File(path));
		if (indexing == Indexing.JUST_INDEXING) {
			return;
		}

		try {
			generate(new File(path), tools, skillFile);
		} catch (IOException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Sets the flags for the generation
	 * 
	 * @param evaluations
	 *            the evaluations of the command line arguments
	 */
	private void setFlags(HashMap<String, ArgumentEvaluation> evaluations) {
		if (evaluations.containsKey("all")) {
			fileFlag = FileFlag.All;
		}

		if (evaluations.containsKey("cleanup")) {
			cleanUp = false;
		}
	}

	/**
	 * Creates the corresponding objects for the skill specification in the
	 * project directory
	 * 
	 * @param indexing
	 *            whether indexing should happen and whether the original intent
	 *            should happen
	 * @param cleanUpAssistantWrapper
	 *            wrapper for saving the {@link CleanUpAssistant} instance
	 * @param project
	 *            the directory containing the .skills file and the skill
	 *            specification
	 */
	private void index(Indexing indexing, CleanUpAssistantWrapper cleanUpAssistantWrapper, File project) {
		cleanUpAssistantWrapper.set(new CleanUpAssistant(skillFile, this));
		indexFiles(project, cleanUpAssistantWrapper.get().getTemporary(), indexing);
		if (indexing != Indexing.NO_INDEXING) {
			cleanUpAssistantWrapper.get().merge();
			cleanUpAssistantWrapper.get().cleanUp();
		}
	}

	/**
	 * Parses the arguments that were saved in a {@link HashMap}
	 * 
	 * @param evaluations
	 *            the preprocessed evaluations of the arguments
	 * @param tools
	 *            out param, tools that should be generated
	 */
	private void parseArguments(HashMap<String, ArgumentEvaluation> evaluations, ArrayList<String> tools) {
		for (String key : evaluations.keySet()) {
			switch (key) {
				case "path" :
					path = evaluations.get("path").getArgument();
					break;

				case "generator" :
					generator = skillFile.Generators().make(evaluations.get("exec").getArgument(),
							evaluations.get("generator").getArgument());
					break;

				case "lang" :
					language = evaluations.get("lang").getArgument();
					break;

				case "module" :
					module = evaluations.get("module").getArgument();
					break;

				case "output" :
					output = new File(evaluations.get("output").getArgument());
					break;

				default :
					tools.add(evaluations.get(key).getArgument());

				case "all" :
				case "cleanup" :
				case "list" :
				case "exec" :
					break;
			}
		}
	}

	/**
	 * Prepares the start of the editor
	 * 
	 * @param indexing
	 *            Whether indexing should happen and whether the editor should
	 *            be run
	 * @param args
	 *            the command line arguments
	 * @param cleanUpAssistantWrapper
	 *            the wrapper for the {@link CleanUpAssistant} instance
	 */
	private void startEditor(Indexing indexing, String[] args, CleanUpAssistantWrapper cleanUpAssistantWrapper) {
		Editor editor = new Editor(args[2]);
		File projectDirectory = new File(args[1]);
		File sfFile = new File(projectDirectory.getAbsolutePath() + File.separator + ".skills");
		if (!sfFile.exists()) {
			try {
				// noinspection ResultOfMethodCallIgnored
				sfFile.createNewFile();
			} catch (IOException e1) {
				ExceptionHandler.handle(e1);
			}
		}
		try {
			skillFile = SkillFile.open(sfFile.getAbsolutePath(), SkillFile.Mode.Read, SkillFile.Mode.Write);
		} catch (IOException e1) {
			ExceptionHandler.handle(e1);
			return;
		}
		index(indexing, cleanUpAssistantWrapper, projectDirectory);
		if (indexing == Indexing.JUST_INDEXING) {
			skillFile.close();
			return;
		}
		editor.setSkillFile(skillFile);
		editor.start();
	}

	/**
	 * Method for parsing all arguments.
	 * 
	 * @param args
	 *            the arguments to parse
	 * @param index
	 *            the starting index
	 * @return Returns the array of evaluations of the arguments.
	 */
	private ArgumentEvaluation[] evaluateArgument(String[] args, int index) {
		// decide what type of argument was passed. Long form (led by --) or
		// short form (led by -).
		if (args[index].startsWith("--")) {
			ArgumentEvaluation e = MainUtil.doubleDashArg(args, index);
			return new ArgumentEvaluation[]{e};
		} else if (args[index].startsWith("-")) {
			return singleDashArg(args, index);
		} else {
			ArgumentEvaluation e = MainUtil.noDashArg(args, index);
			return new ArgumentEvaluation[]{e};
		}
	}

	/**
	 * Prints a tool with its corresponding types and their fields on the
	 * stdout.
	 * 
	 * @param sfPath
	 *            skillfile path containing the tools and their types.
	 * @param evaluations
	 *            the evaluations of the arguments that were given on the
	 *            command line.
	 */
	private void list(String sfPath, HashMap<String, ArgumentEvaluation> evaluations) {
		SkillFile sf;
		try {
			sf = SkillFile.open(sfPath, de.ust.skill.common.java.api.SkillFile.Mode.Read);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		ArrayList<Tool> tools = new ArrayList<>();
		for (String key : evaluations.keySet()) {
			int index;
			try {
				index = Integer.parseInt(key);
			} catch (NullPointerException | NumberFormatException ignored) {
				index = -1;
			}
			if (index > -1) {
				for (Tool tool : sf.Tools()) {
					if (tool.getName().equals(evaluations.get(key).getArgument())) {
						tools.add(tool);
						break;
					}
				}
			}
		}
		if (tools.isEmpty()) {
			tools.addAll(sf.Tools());
		}
		for (Tool tool : tools) {
			System.out.println(tool.getName());
			for (de.unistuttgart.iste.ps.skillls.tools.File file : tool.getFiles()) {
				listFileContent(tool, file);
			}
		}
	}

	/**
	 * Prints the content of a tool
	 * 
	 * @param tool
	 *            the tool that should be printed
	 * @param file
	 *            the sf-file containing the tool
	 */
	private void listFileContent(Tool tool, de.unistuttgart.iste.ps.skillls.tools.File file) {
		File f = new File(file.getPath());
		// test if file has changed
		if (fileFlag == FileFlag.All || !file.getMd5().equals(MainUtil.hash(f))
				|| ("" + f.lastModified()).equals(file.getTimestamp())) {
			// print file name
			System.out.println("  " + file.getPath());
			tool.getTypes().stream().filter(t -> t.getFile().getPath().equals(file.getPath()))
					.forEach(MainUtil::listTypeContent);
		}
	}

	/**
	 * Evaluates arguments which have one leading dash.
	 *
	 * @param args
	 *            The array containing the arguments.
	 * @param globalIndex
	 *            The current position in {@code args}.
	 * @return Returns an object with the unambiguous text what action has to
	 *         perform and the new index.
	 */
	private ArgumentEvaluation[] singleDashArg(String[] args, int globalIndex) {
		char last = ' ';
		int index = globalIndex;
		ArrayList<ArgumentEvaluation> list = new ArrayList<>();
		for (char c : args[index].substring(1).toCharArray()) {
			index = MainUtil.parseCharParams(args, last, index, list, c);
			last = c;
		}
		return list.toArray(new ArgumentEvaluation[list.size()]);
	}

	/**
	 * Work flow for generating bindings for tools.
	 *
	 * @param project
	 *            The directory the project is stored in.
	 * @param tools
	 *            The tools that should be built.
	 * @param skillFile
	 *            The skillfile which was loaded.
	 * @throws IOException
	 *             Thrown if there is a problem with the creation of temporary
	 *             files.
	 */
	private void generate(File project, ArrayList<String> tools, SkillFile skillFile) throws IOException {
		HashMap<Tool, ArrayList<File>> toolToFile = new HashMap<>();

		if (tools.size() == 0) {
			skillFile.Tools().forEach(t -> tools.add(t.getName()));
		}

		for (String t : tools) {
			for (Tool tool : skillFile.Tools()) {
				if (t.equals(tool.getName()) || fileFlag == FileFlag.All) {
					ArrayList<File> files = new ArrayList<>();
					for (de.unistuttgart.iste.ps.skillls.tools.File f : tool.getFiles()) {
						File file = new File(f.getPath());
						String hash = MainUtil.hash(new File(file.getAbsolutePath()));
						// check if modified or all files are considered
						if (!f.getMd5().equals(hash) || file.lastModified() != Long.parseLong(f.getTimestamp())) {
							f.setMd5(hash);
							f.setTimestamp(String.valueOf(file.lastModified()));
							files.add(file);
						}
					}
					if (files.size() != 0 || fileFlag == FileFlag.All) {
						files.clear();
						for (de.unistuttgart.iste.ps.skillls.tools.File f : tool.getFiles()) {
							File file = new File(f.getPath());
							File fi = MainUtil.createToolFile(project, tool, file);
							if (fi != null) {
								files.add(fi);
							}
						}
						toolToFile.put(tool, files);
					}
					break;
				}
			}
		}
		runGeneration(toolToFile, project.getAbsoluteFile());
	}

	/**
	 * Method for accumulating Thread for Binding generation per tool. Also runs
	 * them.
	 *
	 * @param toolToFile
	 *            Map for mapping tools to their corresponding temporary files.
	 * @param project
	 *            The directory containing the project.
	 */
	private void runGeneration(HashMap<Tool, ArrayList<File>> toolToFile, java.io.File project) {
		ArrayList<Thread> commands = new ArrayList<>();
		for (de.unistuttgart.iste.ps.skillls.tools.Tool t : toolToFile.keySet()) {
			StringBuilder builder = new StringBuilder();
			builder.append(generator == null ? t.getGenerator().getExecEnv() : generator.getExecEnv());
			builder.append(' ');
			builder.append(generator == null ? t.getGenerator().getPath() : new File(generator.getPath()).getName());
			builder.append(' ');
			builder.append("-L ");
			builder.append(language == null ? t.getLanguage() : language);
			builder.append(' ');
			builder.append("-p ");
			builder.append(module == null ? t.getModule() : module);
			builder.append(' ');
			// noinspection SuspiciousMethodCalls
			builder.append(" %s ");
			builder.append(output.getAbsolutePath());
			// noinspection Convert2streamapi
			commands.add(new Thread(new de.unistuttgart.iste.ps.skillls.main.Generator(t, project.getAbsolutePath(),
					cleanUp, builder.toString(),
					new File(generator == null ? t.getGenerator().getPath() : generator.getPath()))));
		}
		commands.forEach(java.lang.Thread::start);
		commands.forEach((thread) -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				ExceptionHandler.handle(e);
			}
		});
		Path skilltPath = Paths.get(project.getAbsolutePath() + File.separator + ".skillt");
		try {
			Files.deleteIfExists(skilltPath);
		} catch (IOException ignored) {
			// ignored
		}
	}

	/**
	 * Indexes the files. Furthermore indexes also the types, fields and hints.
	 *
	 * @param project
	 *            The project the tools belong to.
	 * @param skillFile
	 *            The skill file containing the definitions for the tools.
	 * @param indexing
	 *            Determines whether files should be indexed or not
	 */
	private void indexFiles(File project, SkillFile skillFile, Indexing indexing) {
		if (indexing == Indexing.NO_INDEXING) {
			return;
		}
		if (project != null && project.listFiles() != null) {
			// noinspection ConstantConditions
			for (File child : project.listFiles()) {
				if (child.isDirectory() && !child.getName().equals(".skillt")) {
					indexFiles(child, skillFile, indexing);
				} else if (child.getAbsolutePath().endsWith(".skill")) {
					// noinspection UnusedAssignment
					skillFile.Files().make(new ArrayList<>(), "", MainUtil.hash(child), child.getPath(),
							child.lastModified() + "");
					MainUtil.indexTypes(child, skillFile);
				}
			}
		}
	}

	/**
	 * Adds the dependencies of a file to the file.
	 *
	 * @param skillFile
	 *            the skillfile containing the files.
	 */
	private void buildDependencies(SkillFile skillFile) {
		ArrayList<Thread> builder = new ArrayList<>();
		for (Tool tool : skillFile.Tools()) {
			builder.add(new Thread(new DependencyBuilder(tool, skillFile)));
			builder.get(builder.size() - 1).start();
		}
		for (Thread t : builder) {
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * File for opening a new skillFile
	 * 
	 * @param skillFile
	 *            the new SkillFile
	 */
	public void setNewSkillFile(SkillFile skillFile) {
		this.skillFile = skillFile;
	}
}
