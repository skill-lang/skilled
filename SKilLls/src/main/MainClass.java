package main;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tools.Generator;
import tools.Tool;
import tools.api.SkillFile;
import grammar.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This class starts the application and runs the generator if necessary.
 *
 * @author Armin Hüneburg
 * @since 18.08.15.
 */
public class MainClass {
    /**
     * Text for the help message. Called with "skillls --help".
     */
    @SuppressWarnings("SpellCheckingInspection")
    private static final String HELPTEXT = "SYNOPSIS\n"
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
    private static Generator generator;
    private static String language;
    private static FileFlag fileFlag = FileFlag.Changed;
    private static String module;
    private static File output;

    /**
     * Entry Point. Generates flags for the generator execution.
     *
     * @param args The command line arguments.
     */
    @SuppressWarnings("ConstantConditions")
    public static void main(String[] args) {
        if (args[0].equals("-e") || args[0].equals("--edit")) {
            Edit e = new Edit(args[2]);
            File file = new File(args[1]);
            File sfFile = new File(file.getAbsolutePath() + File.separator + "a.skills");
            if (!sfFile.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    sfFile.createNewFile();
                } catch (IOException e1) {
                    ExceptionHandler.handle(e1);
                }
            }
            SkillFile skillFile;
            try {
                skillFile = SkillFile.open(sfFile.getAbsolutePath());
            } catch (IOException e1) {
                ExceptionHandler.handle(e1);
                return;
            }
            e.setSkillFile(skillFile);
            e.start();
        } else {
            HashMap<String, ArgumentEvaluation> evaluations = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--help") || args[i].equals("-h")) {
                    printHelp();
                    return;
                } else if (args[i].startsWith("--")) {
                    ArgumentEvaluation e = doubleDashArg(args, i);
                    if (e.getArgument() != null) {
                        evaluations.put(e.getName(), e);
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
                sf = SkillFile.open(evaluations.get("path").getArgument() + File.separator + "a.skills");
            } catch (IOException e) {
                ExceptionHandler.handle(e);
                return;
            }
            indexFiles(new File(evaluations.get("path").getArgument()), sf);
            Set<String> set = evaluations.keySet();
            set.remove("path");
            if (set.contains("generator")) {
                generator = sf.Generators().make(evaluations.get("exec").getArgument(), evaluations.get("path").getArgument());
                set.remove("exec");
                set.remove("path");
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
                generate(new File(evaluations.get("path").getArgument()), tools, sf);
            } catch (IOException e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    /**
     * Evaluates arguments which don't have a leading dash.
     *
     * @param args  The array containing the arguments.
     * @param index The current position in {@code args}.
     * @return Returns an object with the unambiguous text what action has to perform and the new index.
     */
    private static ArgumentEvaluation noDashArg(String[] args, int index) {
        return new ArgumentEvaluation(index, args[index], Integer.toString(index));
    }

    /**
     * Evaluates arguments which have one leading dash.
     *
     * @param args  The array containing the arguments.
     * @param index The current position in {@code args}.
     * @return Returns an object with the unambiguous text what action has to perform and the new index.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private static ArgumentEvaluation[] singleDashArg(String[] args, int index) {
        char last = ' ';
        ArrayList<ArgumentEvaluation> list = new ArrayList<>();
        for (char c : args[index].substring(1).toCharArray()) {
            switch (c) {
                case 'g':
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "generator"));

                case 'a':
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    fileFlag = FileFlag.All;
                    list.add(new ArgumentEvaluation(index, null, "all"));

                case 'l':
                    break;

                case 'x':
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "exec"));

                case 'p':
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "path"));

                case 'o':
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "output"));

                case 'm':
                    if (last == 'l') {
                        index++;
                        list.add(new ArgumentEvaluation(index, args[index], "lang"));
                    }
                    index++;
                    list.add(new ArgumentEvaluation(index, args[index], "module"));

                case 's':
                    if (last == 'l') {
                        throw new NotImplementedException();
                        //list.add(new ArgumentEvaluation(index, args[index], "list"));
                    }
                    throw new IllegalArgumentException();

                default:
                    throw new IllegalArgumentException();
            }
            last = c;
        }
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return list.toArray(new ArgumentEvaluation[0]);
    }

    /**
     * Evaluates arguments which have two leading dashes.
     *
     * @param args  The array containing the arguments.
     * @param index The current position in {@code args}.
     * @return Returns an object with the unambiguous text what action has to perform and the new index.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private static ArgumentEvaluation doubleDashArg(String[] args, int index) {
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
                throw new NotImplementedException();

            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Prints the message saved in {@link #HELPTEXT} to the command line.
     */
    private static void printHelp() {
        System.out.println(HELPTEXT);
    }

    /**
     * Work flow for generating bindings for tools.
     *
     * @param project   The directory the project is stored in.
     * @param tools     The tools that should be built.
     * @param skillFile The skillfile which was loaded.
     * @throws IOException Thrown if there is a problem with the creation of temporary files.
     */
    private static void generate(File project, ArrayList<String> tools, SkillFile skillFile) throws IOException {
        HashSet<tools.Tool> toolsToBuild = new HashSet<>();
        HashMap<Tool, ArrayList<File>> toolToFile = new HashMap<>();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            ExceptionHandler.handle(e);
            return;
        }

        for (String t : tools) {
            for (Tool tool : skillFile.Tools()) {
                if (t.equals(tool.getName())) {
                    toolsToBuild.add(tool);
                    ArrayList<File> files = new ArrayList<>();
                    for (tools.File f : tool.getFiles()) {
                        File file = new File(f.getPath());
                        try (InputStream is = Files.newInputStream(Paths.get(file.getAbsolutePath()))) {
                            DigestInputStream dis = new DigestInputStream(is, md5);
                            byte[] bytes = new byte[4096];
                            while (dis.read(bytes, 0, bytes.length) != -1) {
                                // digest file
                            }
                            bytes = md5.digest();
                            if (!f.getMd5().equals(encodeHex(bytes)) || file.lastModified() != Long.parseLong(f.getTimestamp()) || fileFlag == FileFlag.All) {
                                f.setMd5(encodeHex(bytes));
                                f.setTimestamp(String.valueOf(file.lastModified()));
                                files.add(file);
                                break;
                            }
                        }
                    }
                    if (files.size() != 0) {
                        files.clear();
                        for (tools.File f : tool.getFiles()) {
                            File file = new File(f.getPath());
                            files.add(createToolFile(project, tool, file));
                        }
                        toolToFile.put(tool, files);
                    }
                    break;
                }
            }
        }

        runGeneration(toolsToBuild, toolToFile, new File(project.getAbsolutePath() + File.separator + ".skillt"));
    }

    /**
     * Method for accumulating Thread for Binding generation per tool. Also runs them.
     *
     * @param toolsToBuild Tools that need to be built.
     * @param toolToFile   Map for mapping tools to their corresponding temporary files.
     * @param tempDir      Directory which contains the tool specific files.
     */
    private static void runGeneration(HashSet<tools.Tool> toolsToBuild, HashMap<Tool, ArrayList<File>> toolToFile,
                                      File tempDir) {
        ArrayList<Thread> commands = new ArrayList<>();
        for (tools.Tool t : toolsToBuild) {
            StringBuilder builder = new StringBuilder();
            builder.append(generator == null ? t.getGenerator().getExecEnv() : generator.getExecEnv());
            builder.append(' ');
            builder.append(generator == null ? t.getGenerator().getPath() : generator.getPath());
            builder.append(' ');
            builder.append("-L ");
            builder.append(language == null ? t.getLanguage() : language);
            builder.append(' ');
            builder.append("-p ");
            builder.append(module == null ? t.getModule() : module);
            builder.append(' ');
            //noinspection SuspiciousMethodCalls
            for (File f : toolToFile.get(t.getName())) {
                builder.append(f.getAbsolutePath());
                builder.append(' ');
            }
            builder.append(output.getAbsolutePath());
            commands.add(new Thread(new GenerationThread(builder.toString(), new File(generator == null ? t.getGenerator().getPath() : generator.getPath()), output)));
        }
        commands.forEach(java.lang.Thread::start);
        commands.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                ExceptionHandler.handle(e);
            }
        });
        cleanUp(tempDir);
    }

    /**
     * Method for creating the temporary file for the tool.
     *
     * @param tool The tool the file should be generated for.
     * @param file The file that should be optimized for the tool.
     * @return The file object of the temporary file.
     * @throws IOException Thrown if there is a problem with creating temporary files.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File createToolFile(File project, Tool tool, File file) throws IOException {
        File f = new File(project.getAbsolutePath() + File.separator + ".skillt");
        f = new File(f, tool.getName());
        if (!f.exists()) {
            f.mkdirs();
        }
        String name = file.getAbsolutePath();
        name = name.substring(project.getAbsolutePath().length());
        if (name.startsWith(File.separator)) {
            name = name.substring(1);
        }
        @SuppressWarnings("null")
        File temp = new File(f.getAbsolutePath() + File.separator + name);
        temp.createNewFile();
        try (FileReader input = new FileReader(file); FileOutputStream fs = new FileOutputStream(temp.getAbsolutePath());
             BufferedReader reader = new BufferedReader(input)) {
            Files.copy(Paths.get(file.getAbsolutePath()), fs);
        }
        return temp;
    }

    /**
     * Encodes a byte array to a hex-String.
     *
     * @param digest The array that should be encoded.
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
     * Deletes the directory with the temporary files.
     *
     * @param projectRoot The root directory of the project.
     */
    private static void cleanUp(File projectRoot) {
        //noinspection SpellCheckingInspection
        File f = new File(projectRoot.getAbsolutePath() + File.separator + ".skillt");
        deleteDir(f);
    }

    /**
     * Deletes all children and the directory itself.
     *
     * @param dir The directory to delete.
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
     * Indexes the files.
     * Furthermore indexes also the types, fields and hints.
     *
     * @param project   The project the tools belong to.
     * @param skillFile The skill file containing the definitions for the tools.
     */
    private static void indexFiles(File project, SkillFile skillFile) {
        if (project != null && project.listFiles() != null) {
            //noinspection ConstantConditions
            for (File child : project.listFiles()) {
                if (child.isDirectory()) {
                    indexFiles(child, skillFile);
                } else if (child.getAbsolutePath().endsWith(".skill")) {
                    if (skillFile.Files().stream().noneMatch(f -> child.getAbsolutePath().endsWith(f.getPath()))) {
                        skillFile.Files().make("", child.getAbsolutePath(), "");
                        indexTypes(child, skillFile);
                    }
                }
            }
        }
    }

    /**
     * Indexes the types of a file.
     *
     * @param file      The file containing the types.
     * @param skillFile The skill file containing the definitions for the tools.
     */
    @SuppressWarnings("UnusedParameters")
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
        ParserRuleContext t = parser.file();
        ParseTreeWalker walker = new ParseTreeWalker();
        //walker.walk(new SkillIndexListener(skillFile, file.getPath()), t);
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
         * @param index    the last index this argument is concerned with.
         * @param argument if the argument has a second parameter, this is it.
         * @param name     the name of the argument.
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
