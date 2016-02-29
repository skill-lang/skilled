package de.unistuttgart.iste.ps.skillls.main;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;

import de.unistuttgart.iste.ps.skillls.grammar.SKilLLexer;
import de.unistuttgart.iste.ps.skillls.grammar.SKilLParser;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Generator;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;


/**
 * This class starts the application and runs the generator if necessary.
 *
 * @author Armin Hüneburg
 * @since 18.08.15.
 */
public class MainClass {
    private static Generator generator;
    private static String language;
    private static FileFlag fileFlag = FileFlag.Changed;
    private static String module;
    private static File output;
    private static boolean cleanUp = true;
    private static String path;
    private static SkillFile skillFile;

    /**
     * Entry point. Sets the exception handler to not rethrow exceptions as errors.
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
        switch (args[0]) {
            case "-e":
            case "--edit":
                startEditor(indexing, args, cleanUpAssistantWrapper);
                break;

            case "-h":
            case "--help":
                printHelp();
                break;

            default:
                prepareGeneration(indexing, args, cleanUpAssistantWrapper);
        }
        if (cleanUpAssistantWrapper.get() != null && indexing != Indexing.NO_INDEXING) {
            cleanUpAssistantWrapper.get().analyseBreakage();
        }
    }

    /**
     * Prepares everything for the generation of bindings and temporary files
     * 
     * @param indexing
     *            whether indexing should happen, or not and whether other stuff should happen, or not
     * @param args
     *            the arguments given on the command line
     * @param cleanUpAssistantWrapper
     *            the wrapper that saves the {@link CleanUpAssistant} instance
     */
    private static void prepareGeneration(Indexing indexing, String[] args,
            CleanUpAssistantWrapper cleanUpAssistantWrapper) {
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
    private static void setFlags(HashMap<String, ArgumentEvaluation> evaluations) {
        if (evaluations.containsKey("all")) {
            fileFlag = FileFlag.All;
        }

        if (evaluations.containsKey("cleanup")) {
            cleanUp = false;
        }
    }

    /**
     * Creates the corresponding objects for the skill specification in the project directory
     * 
     * @param indexing
     *            whether indexing should happen and whether the original intent should happen
     * @param cleanUpAssistantWrapper
     *            wrapper for saving the {@link CleanUpAssistant} instance
     * @param project
     *            the directory containing the .skills file and the skill specification
     */
    private static void index(Indexing indexing, CleanUpAssistantWrapper cleanUpAssistantWrapper, File project) {
        cleanUpAssistantWrapper.set(new CleanUpAssistant(skillFile));
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
    private static void parseArguments(HashMap<String, ArgumentEvaluation> evaluations, ArrayList<String> tools) {
        for (String key : evaluations.keySet()) {
            switch (key) {
                case "path":
                    path = evaluations.get("path").getArgument();
                    break;

                case "generator":
                    generator = skillFile.Generators().make(evaluations.get("exec").getArgument(),
                            evaluations.get("generator").getArgument());
                    break;

                case "lang":
                    language = evaluations.get("lang").getArgument();
                    break;

                case "module":
                    module = evaluations.get("module").getArgument();
                    break;

                case "output":
                    output = new File(evaluations.get("output").getArgument());
                    break;

                default:
                    tools.add(evaluations.get(key).getArgument());

                case "all":
                case "cleanup":
                case "list":
                case "exec":
                    break;
            }
        }
    }

    /**
     * Prepares the start of the editor
     * 
     * @param indexing
     *            Whether indexing should happen and whether the editor should be run
     * @param args
     *            the command line arguments
     * @param cleanUpAssistantWrapper
     *            the wrapper for the {@link CleanUpAssistant} instance
     */
    private static void startEditor(Indexing indexing, String[] args, CleanUpAssistantWrapper cleanUpAssistantWrapper) {
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
    private static ArgumentEvaluation[] evaluateArgument(String[] args, int index) {
        // decide what type of argument was passed. Long form (led by --) or
        // short form (led by -).
        if (args[index].startsWith("--")) {
            ArgumentEvaluation e = doubleDashArg(args, index);
            return new ArgumentEvaluation[] { e };
        } else if (args[index].startsWith("-")) {
            return singleDashArg(args, index);
        } else {
            ArgumentEvaluation e = noDashArg(args, index);
            return new ArgumentEvaluation[] { e };
        }
    }

    /**
     * Prints a tool with its corresponding types and their fields on the stdout.
     * 
     * @param sfPath
     *            skillfile path containing the tools and their types.
     * @param evaluations
     *            the evaluations of the arguments that were given on the command line.
     */
    private static void list(String sfPath, HashMap<String, ArgumentEvaluation> evaluations) {
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
            } catch (@SuppressWarnings("unused") NullPointerException | NumberFormatException ignored) {
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
    private static void listFileContent(Tool tool, de.unistuttgart.iste.ps.skillls.tools.File file) {
        File f = new File(file.getPath());
        // test if file has changed
        if (fileFlag == FileFlag.All || !file.getMd5().equals(hash(f))
                || ("" + f.lastModified()).equals(file.getTimestamp())) {
            // print file name
            System.out.println("  " + file.getPath());
            tool.getTypes().stream().filter(t -> t.getFile().getPath().equals(file.getPath()))
                    .forEach(MainClass::listTypeContent);
        }
    }

    /**
     * Prints the type content
     * 
     * @param type
     *            the type whose content should be printed.
     */
    private static void listTypeContent(Type type) {
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
     * @return Returns an object with the unambiguous text what action has to perform and the new index.
     */
    private static ArgumentEvaluation noDashArg(String[] args, int index) {
        return new ArgumentEvaluation(index, args[index], Integer.toString(index));
    }

    /**
     * Evaluates arguments which have one leading dash.
     *
     * @param args
     *            The array containing the arguments.
     * @param globalIndex
     *            The current position in {@code args}.
     * @return Returns an object with the unambiguous text what action has to perform and the new index.
     */
    private static ArgumentEvaluation[] singleDashArg(String[] args, int globalIndex) {
        char last = ' ';
        int index = globalIndex;
        ArrayList<ArgumentEvaluation> list = new ArrayList<>();
        for (char c : args[index].substring(1).toCharArray()) {
            index = parseCharParams(args, last, index, list, c);
            last = c;
        }
        return list.toArray(new ArgumentEvaluation[list.size()]);
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
    private static int parseCharParams(String[] args, char last, int globalIndex, ArrayList<ArgumentEvaluation> list,
            char c) {
        int index = globalIndex;
        switch (c) {
            case 'g':
                // test if l was typed
                index = wasLastCharL(index, list, last, args[index + 1]);
                index = addEvaluation(index, args, "generator", list);
                break;

            case 'a':
                // test if l was typed
                index = wasLastCharL(index, list, last, args[index + 1]);
                list.add(new ArgumentEvaluation(index, null, "all"));
                break;

            case 'l':
                break;

            case 'x':
                // test if l was typed
                index = wasLastCharL(index, list, last, args[index + 1]);
                index = addEvaluation(index, args, "exec", list);
                break;

            case 'p':
                // test if l was typed
                index = wasLastCharL(index, list, last, args[index + 1]);
                index = addEvaluation(index, args, "path", list);
                break;

            case 'o':
                // test if l was typed
                index = wasLastCharL(index, list, last, args[index + 1]);
                index = addEvaluation(index, args, "output", list);
                break;

            case 'm':
                // test if l was typed
                index = wasLastCharL(index, list, last, args[index + 1]);
                index = addEvaluation(index, args, "module", list);
                break;

            case 's':
                // test if l was typed, if yes it is list else invalid
                if (last == 'l') {
                    list.add(new ArgumentEvaluation(index, args[index], "list"));
                    break;
                }
                throw new IllegalArgumentException();

            default:
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
    private static int addEvaluation(int index, String[] args, String key, ArrayList<ArgumentEvaluation> evaluations) {
        evaluations.add(new ArgumentEvaluation(index, args[index + 1], key));
        return index + 1;
    }

    /**
     * Checks whether the last char was an L and sets the language argument if it was.
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
    private static int wasLastCharL(int globalIndex, ArrayList<ArgumentEvaluation> list, char c, String arg) {
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
     * @return Returns an object with the unambiguous text what action has to perform and the new index.
     */
    private static ArgumentEvaluation doubleDashArg(String[] args, int globalIndex) {
        int index = globalIndex;
        switch (args[index]) {
            case "--all":
                return new ArgumentEvaluation(index, null, "all");

            case "--lang":
            case "--generator":
            case "--exec":
            case "--path":
            case "--output":
            case "--module":
                index++;
                return new ArgumentEvaluation(index, args[index], args[index - 1].substring(2));

            case "--list":
                return new ArgumentEvaluation(index, null, "list");

            case "--no-cleanup":
                return new ArgumentEvaluation(index, null, "cleanup");

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Prints the message saved in {@link Constants#HELPTEXT} to the command line.
     */
    private static void printHelp() {
        System.out.println(Constants.HELPTEXT);
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
     *             Thrown if there is a problem with the creation of temporary files.
     */
    private static void generate(File project, ArrayList<String> tools, SkillFile skillFile) throws IOException {
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
                        String hash = hash(new File(file.getAbsolutePath()));
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
                            File fi = createToolFile(project, tool, file);
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
     * Method for accumulating Thread for Binding generation per tool. Also runs them.
     *
     * @param toolToFile
     *            Map for mapping tools to their corresponding temporary files.
     * @param project
     *            The directory containing the project.
     */
    private static void runGeneration(HashMap<Tool, ArrayList<File>> toolToFile, java.io.File project) {
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
            commands.add(new Thread(new de.unistuttgart.iste.ps.skillls.main.Generator(t, project.getAbsolutePath(), cleanUp,
                    builder.toString(), new File(generator == null ? t.getGenerator().getPath() : generator.getPath()))));
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
        } catch (@SuppressWarnings("unused") IOException ignored) {
            // ignored
        }
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
    private static File createToolFile(File project, Tool tool, File file) throws IOException {
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
    private static void createDirectory(File file) {
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
    private static String encodeHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
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
    private static void indexFiles(File project, SkillFile skillFile, Indexing indexing) {
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
                    skillFile.Files().make(new ArrayList<>(), "", hash(child), child.getPath(), child.lastModified() + "");
                    indexTypes(child, skillFile);
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
    private static void buildDependencies(SkillFile skillFile) {
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
     * returns a stream of tools.File with identical path.
     * 
     * @param file
     *            the original file.
     * @param skillFile
     *            the skillfile containing the files.
     * @return a stream of the tool.Files.
     */
    @SuppressWarnings("unused")
    private static Stream<de.unistuttgart.iste.ps.skillls.tools.File> getStream(File file, SkillFile skillFile) {
        ArrayList<de.unistuttgart.iste.ps.skillls.tools.File> files = new ArrayList<>();
        for (de.unistuttgart.iste.ps.skillls.tools.File f : skillFile.Files()) {
            Path p1 = Paths.get(file.getAbsolutePath());
            Path p2 = Paths.get(new File(f.getPath()).getAbsolutePath());
            if (p1.relativize(p2).toString().equals("")) {
                files.add(f);
            }
        }
        return files.stream();
    }

    /**
     * Indexes the types of a file.
     *
     * @param file
     *            The file containing the types.
     * @param skillFile
     *            SkillFile containing the types in the end.
     */
    private static void indexTypes(File file, SkillFile skillFile) {
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
        @SuppressWarnings({ "unused" })
        ParseTree tree = parser.file();
    }

    /**
     * Creates a MD5 hash of a file and transforms it into a hexadecimal String.
     * 
     * @param file
     *            The file that should be hashed.
     * @return The MD5 hash of the given file as hexadecimal string.
     */
    private static String hash(File file) {
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

    /**
     * File for opening a new skillFile
     * 
     * @param skillFile
     *            the new SkillFile
     */
    public static void setNewSkillFile(SkillFile skillFile) {
        MainClass.skillFile = skillFile;
    }
}
