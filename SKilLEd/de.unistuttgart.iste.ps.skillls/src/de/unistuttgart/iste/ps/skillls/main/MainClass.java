package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.grammar.SKilLLexer;
import de.unistuttgart.iste.ps.skillls.grammar.SKilLParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.ParseTree;
import de.unistuttgart.iste.ps.skillls.tools.*;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    /**
     * Entry Point. Generates flags for the generator execution.
     *
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        if (args[0].equals("-e") || args[0].equals("--edit")) {
            Edit editor = new Edit(args[2]);
            File file = new File(args[1]);
            File sfFile = new File(file.getAbsolutePath() + File.separator + ".skills");
            if (!sfFile.exists()) {
                try {
                    // noinspection ResultOfMethodCallIgnored
                    sfFile.createNewFile();
                } catch (IOException e1) {
                    ExceptionHandler.handle(e1);
                }
            }
            SkillFile skillFile;
            if (System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH).contains("win") && !sfFile.exists()) {
                try {
                    sfFile.createNewFile();
                } catch (IOException e) {
                    ExceptionHandler.handle(e);
                    return;
                }
            }
            try {
                skillFile = SkillFile.open(sfFile.getAbsolutePath(), de.ust.skill.common.java.api.SkillFile.Mode.Read,
                        de.ust.skill.common.java.api.SkillFile.Mode.Write);
            } catch (IOException e1) {
                ExceptionHandler.handle(e1);
                return;
            }
            indexFiles(file, skillFile);
            editor.setSkillFile(skillFile);
            editor.start();
        } else {
            HashMap<String, ArgumentEvaluation> evaluations = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                // decide what type of argument was passed. Long form (led by --) or short form (led by -) or help.
                if (args[i].equals("--help") || args[i].equals("-h")) {
                    printHelp();
                    return;
                } else if (args[i].startsWith("--")) {
                    ArgumentEvaluation e = doubleDashArg(args, i);
                    if (e.getArgument() != null) {
                        if (!e.getArgument().equals("list")) {
                            evaluations.put(e.getName(), e);
                        }
                    }
                    i = e.getIndex();
                } else if (args[i].startsWith("-")) {
                    ArgumentEvaluation[] es = singleDashArg(args, i);
                    for (ArgumentEvaluation e : es) {
                        if (e.getArgument() != null) {
                            evaluations.put(e.getName(), e);
                        }
                        i = i < e.getIndex() ? e.getIndex() : i;
                    }
                } else {
                    ArgumentEvaluation e = noDashArg(args, i);
                    if (e.getArgument() != null) {
                        evaluations.put(e.getName(), e);
                    }
                    i = e.getIndex();
                }
            }
            SkillFile sf;
            try {
                sf = SkillFile.open(evaluations.get("path").getArgument() + File.separator + ".skills",
                        de.ust.skill.common.java.api.SkillFile.Mode.Write, de.ust.skill.common.java.api.SkillFile.Mode.Read);
            } catch (IOException e) {
                ExceptionHandler.handle(e);
                return;
            }
            if (evaluations.containsKey("list")) {
                list(sf, evaluations);
                return;
            }
            buildDependencies(sf);
            String path = evaluations.get("path").getArgument();
            evaluations.remove("path");
            indexFiles(new File(path), sf);
            Set<String> set = evaluations.keySet();
            // set the environment of the generation.
            if (set.contains("generator")) {
                generator = sf.Generators().make(evaluations.get("exec").getArgument(),
                        evaluations.get("generator").getArgument());
                set.remove("exec");
                set.remove("generator");
            }
            if (set.contains("lang")) {
                language = evaluations.get("lang").getArgument();
                set.remove("lang");
            }

            if (set.contains("module")) {
                module = evaluations.get("module").getArgument();
                set.remove("module");
            }

            if (set.contains("output")) {
                output = new File(evaluations.get("output").getArgument());
                set.remove("output");
            }

            ArrayList<String> tools = new ArrayList<>();
            if (set.isEmpty()) {
                tools.addAll(sf.Tools().stream().map(t -> t.getName()).collect(Collectors.toList()));
            } else {
                tools.addAll(set.stream().map(s -> evaluations.get(s).getArgument()).collect(Collectors.toList()));
            }

            try {
                generate(new File(path), tools, sf);
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    /**
     * Prints a tool with its corresponding types and their fields on the stdout.
     * @param sf skillfile containing the tools and their types.
     * @param evaluations the evaluations of the arguments that were given on the command line.
     */
    private static void list(SkillFile sf, HashMap<String, ArgumentEvaluation> evaluations) {
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
            for (de.unistuttgart.iste.ps.skillls.tools.File file : tool.getFiles()) {
                File f = new File(file.getPath());
                String hash = hash(f);
                // test if file has changed
                if (fileFlag == FileFlag.All || !file.getMd5().equals(hash)
                        || ("" + f.lastModified()).equals(file.getTimestamp())) {
                    // print file name
                    System.out.println(file.getPath());
                    tool.getTypes().stream().filter(t -> t.getFile().getPath().equals(file.getPath())).forEach(type -> {
                        // print hints
                        for (Hint hint : type.getTypeHints()) {
                            System.out.println(hint.getName());
                        }
                        // print type
                        System.out.println("  " + type.getName());
                        // print fields
                        for (Field field : type.getFields()) {
                            for (Hint hint : field.getFieldHints()) {
                                // print field hints
                                System.out.println("    " + hint.getName());
                            }
                            System.out.println("    " + field.getName());
                        }
                        System.out.println();
                    });

                }
            }
        }
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
            switch (c) {
                case 'g':
                    // test if l was typed
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "generator"));
                    break;

                case 'a':
                    // test if l was typed
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    fileFlag = FileFlag.All;
                    list.add(new ArgumentEvaluation(index, null, "all"));
                    break;

                case 'l':
                    break;

                case 'x':
                    // test if l was typed
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "exec"));
                    break;

                case 'p':
                    // test if l was typed
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "path"));
                    break;

                case 'o':
                    // test if l was typed
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "output"));
                    break;

                case 'm':
                    // test if l was typed
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "module"));
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
            last = c;
        }
        // noinspection ToArrayCallWithZeroLengthArrayArgument
        return list.toArray(new ArgumentEvaluation[0]);
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
            case "--generator":
                index++;
                return new ArgumentEvaluation(index, args[index], "generator");

            case "--all":
                fileFlag = FileFlag.All;
                return new ArgumentEvaluation(index, null, "all");

            case "--lang":
                index++;
                return new ArgumentEvaluation(index, args[index], "lang");

            case "--exec":
                index++;
                return new ArgumentEvaluation(index, args[index], "exec");

            case "--path":
                index++;
                return new ArgumentEvaluation(index, args[index], "path");

            case "--output":
                index++;
                return new ArgumentEvaluation(index, args[index], "output");

            case "--module":
                index++;
                return new ArgumentEvaluation(index, args[index], "module");

            case "--list":
                return new ArgumentEvaluation(index, null, "list");

            case "--no-cleanup":
                cleanUp = false;
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

        for (String t : tools) {
            for (Tool tool : skillFile.Tools()) {
                if (t.equals(tool.getName())) {
                    ArrayList<File> files = new ArrayList<>();
                    for (de.unistuttgart.iste.ps.skillls.tools.File f : tool.getFiles()) {
                        File file = new File(f.getPath());
                        String hash = hash(new File(file.getAbsolutePath()));
                        // check if modified or all files are considered
                        if (!f.getMd5().equals(hash) || file.lastModified() != Long.parseLong(f.getTimestamp())
                                || fileFlag == FileFlag.All) {
                            f.setMd5(hash);
                            f.setTimestamp(String.valueOf(file.lastModified()));
                            files.add(file);
                        }
                    }
                    if (files.size() != 0) {
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
        runGeneration(toolToFile, new File(project.getAbsolutePath() + File.separator + ".skillt"));
    }

    /**
     * Method for accumulating Thread for Binding generation per tool. Also runs them.
     *
     * @param toolToFile
     *            Map for mapping tools to their corresponding temporary files.
     * @param tempDir
     *            Directory which contains the tool specific files.
     */
    private static void runGeneration(HashMap<Tool, ArrayList<File>> toolToFile,
            File tempDir) {
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
            commands.addAll(toolToFile.get(t).stream()
                    .map(f -> new Thread(new GenerationThread(String.format(builder.toString(), f.getAbsolutePath()),
                            new File(generator == null ? t.getGenerator().getPath() : generator.getPath()))))
                    .collect(Collectors.toList()));
        }
        commands.forEach(java.lang.Thread::start);
        commands.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                ExceptionHandler.handle(e);
            }
        });
        if (cleanUp) {
            cleanUp(tempDir);
        }
    }

    /**
     * Method for creating the temporary file for the tool.
     *
     * @param tool
     *            The tool the file should be generated for.
     * @param file
     *            The file that should be optimized for the tool.
     * @return The file object of the temporary file.
     * @throws IOException
     *             Thrown if there is a problem with creating temporary files.
     */
    private static File createToolFile(File project, Tool tool, File file) throws IOException {
        File tempDir = new File(project.getAbsolutePath() + File.separator + ".skillt");
        tempDir = new File(tempDir, tool.getName());
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        Path relativizedPath = Paths.get(project.getAbsolutePath()).relativize(Paths.get(file.getAbsolutePath()));

        File newFile = new File(tempDir, relativizedPath.toString());

        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
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
            @SuppressWarnings({ "unused" })
            ParseTree tree = parser.file();
        }
        return newFile;
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
     * Deletes the directory with the temporary files.
     *
     * @param projectRoot
     *            The root directory of the project.
     */
    private static void cleanUp(File projectRoot) {
        // noinspection SpellCheckingInspection
        File f = new File(projectRoot.getAbsolutePath());
        deleteDir(f);
    }

    /**
     * Deletes all children and the directory itself.
     *
     * @param dir
     *            The directory to delete.
     * @return true if successful, false if not.
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty or this is a file so delete it
        return dir.delete();
    }

    /**
     * Indexes the files. Furthermore indexes also the types, fields and hints.
     *
     * @param project
     *            The project the tools belong to.
     * @param skillFile
     *            The skill file containing the definitions for the tools.
     */
    private static void indexFiles(File project, SkillFile skillFile) {
        if (project != null && project.listFiles() != null) {
            // noinspection ConstantConditions
            for (File child : project.listFiles()) {
                if (child.isDirectory()) {
                    indexFiles(child, skillFile);
                } else if (child.getAbsolutePath().endsWith(".skill")) {
                	de.unistuttgart.iste.ps.skillls.tools.File f = null;
                    if (getStream(child, skillFile).count() != 0) {
                        f = getStream(child, skillFile).findFirst().get();
                    }
                    String hash = hash(child);
                    if (f == null) {
                        // noinspection UnusedAssignment
                        f = skillFile.Files().make(new ArrayList<>(), "", hash, child.getPath(),
                                child.lastModified() + "");
                        indexTypes(child, skillFile);
                    } else if (Paths.get(child.getPath()).relativize(Paths.get(f.getPath())).toString().equals("")
                            && (!f.getMd5().equals(hash) || !f.getTimestamp().equals("" + child.lastModified()))) {
                        indexTypes(child, skillFile);
                        f.setTimestamp("" + child.lastModified());
                        f.setMd5(hash);
                    } else if (Paths.get(child.getPath()).relativize(Paths.get(f.getPath())).toString().equals("")) {
                        indexTypes(child, skillFile);
                        f.setTimestamp("" + child.lastModified());
                        f.setMd5(hash);
                    }
                }
            }
        }
    }

    /**
     * Adds the dependencies of a file to the file.
     * @param skillFile the skillfile containing the files.
     */
    static void buildDependencies(SkillFile skillFile) {
        for (Tool tool : skillFile.Tools()) {
            for (de.unistuttgart.iste.ps.skillls.tools.File file : tool.getFiles()) {
                ArrayList<Type> types = tool.getTypes().stream()
                        .filter(type -> type.getFile().getPath().equals(file.getPath()))
                        .collect(Collectors.toCollection(ArrayList::new));
                for (Type type : types) {
                    ArrayList<String> deps = new ArrayList<>();
                    for (String extension : type.getExtends()) {
                        tool.getTypes().stream().filter(t -> t.getName().equals(extension))
                                .filter(t -> !t.getFile().getPath().equals(type.getFile().getPath())
                                        && !deps.contains(t.getFile().getPath()))
                                .forEach(t -> deps.add(t.getFile().getPath()));
                    }
                    for (Field field : type.getFields()) {
                        for (Type t : skillFile.Types()) {
                            String name = field.getName();
                            if (name.startsWith("auto")) {
                                name = name.substring(5);
                            }
                            int index = name.indexOf(' ');
                            if (index == -1) {
                                index = name.length();
                            }
                            if (t.getName().startsWith(name.substring(0, index))) {
                                if (!t.getFile().getPath().equals(file.getPath()) && !deps.contains(t.getFile().getPath())) {
                                    deps.add(t.getFile().getPath());
                                }
                            }
                        }
                    }
                    deps.stream().filter(dep -> !file.getDependencies().contains(dep))
                            .forEach(dep -> file.getDependencies().add(dep));
                }
            }
        }
    }

    /**
     * returns a stream of tools.File with identical path.
     * @param file the original file.
     * @param skillFile the skillfile containing the files.
     * @return a stream of the tool.Files.
     */
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
     *            The skill file containing the definitions for the tools.
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
     * @param file The file that should be hashed.
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
     * Class for handling program arguments.
     */
    private static class ArgumentEvaluation {
        private final int INDEX;
        private final String ARGUMENT;
        private final String NAME;

        /**
         * Constructor. Only way to set the attributes.
         *
         * @param index
         *            the last index this argument is concerned with.
         * @param argument
         *            if the argument has a second parameter, this is it.
         * @param name
         *            the name of the argument.
         */
        public ArgumentEvaluation(int index, String argument, String name) {
            this.INDEX = index;
            this.ARGUMENT = argument;
            this.NAME = name;
        }

        /**
         * @return Returns the index.
         */
        public int getIndex() {
            return INDEX;
        }

        /**
         * @return returns the parameter to the argument.
         */
        public String getArgument() {
            return ARGUMENT;
        }

        /**
         * @return returns the name of the argument.
         */
        public String getName() {
            return NAME;
        }
    }
}
