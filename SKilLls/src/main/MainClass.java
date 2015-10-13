package main;

import tools.Generator;
import tools.Tool;
import tools.api.SkillFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import Exceptions.ProjectException;


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
    private static final String helpText = "SYNOPSIS\n"
            + "       skillls [-e] [--edit] [-a] [--all] [-g GENERATOR] [--generator GENERATOR] [-l LANGUAGE] [--lang LANGUAGE] "
            + "[--ls] EXEC PATH OUTPUT [TOOLS...] PACKAGE\n" + "\n" + "DESCRIPTION\n"
            + "       SKilLls generates or lists tools with the given generator.  It can also list all tools.\n" + "\n"
            + "OPTIONS\n" + "       -e, --edit\n" + "               Starts the tool editor." + "\n" + "       -a, --all\n"
            + "              When used with --ls, lists all tools not regarding changes.  When used without --ls, "
            + "generates all tools not regarding changes.\n" + "\n" + "       -g, --generator=GENERATOR\n"
            + "              Needed for generating bindings. Path to the generator that is being used.\n" + "\n"
            + "       -l, --lang=LANGUAGE\n" + "              Language the binding should be generated for.\n" + "\n"
            + "       --ls   Lists the tools of the project.\n" + "\n"
            + "       EXEC   Execution environment for the generator. E.g. scala" + "\n"
            + "       PATH   The path the project is located at.\n" + "\n"
            + "       OUTPUT The path the binding is generated in.\n" + "\n" + "       TOOLS...\n"
            + "              The tools the options should be applied to. If no tools are given the options are "
            + "applied to all available tools.\n" + "\n" + "       PACKAGE The package the binding should be generated in."
            + "\n" + "\n" + "SIDE NODE\n"
            + "       Single letter arguments can be combined. you can call SKilLls with following command:\n"
            + "       skillls -agl /path/to/generator Java /path/to/project\n"
            + "       This command generates all bindings for the tools of the project in Java.\n"
            + "       If l comes before g the language has to be given first.";
    private static Generator generator;
    private static String language;
    private static FileFlag fileFlag = FileFlag.Changed;
    private static String module;
    private static File output;

    /**
     * Entry Point. Generates flags for the generator execution.
     *
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        if (args[0].equals("-e") || args[0].equals("--edit")) {
            Edit e = new Edit(args[2]);
            File file = new File(args[1]);
            File sfFile = new File(file.getAbsolutePath() + File.separator + ".skills");
            if (!sfFile.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    sfFile.createNewFile();
                } catch (IOException e1) {
                    ExceptionHandler.handle(e1);
                }
            }
            SkillFile sf;
            try {
                sf = SkillFile.open(sfFile.getAbsolutePath());
            } catch (IOException e1) {
                ExceptionHandler.handle(e1);
                return;
            }
            e.setSkillFile(sf);
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
                    ArgumentEvaluation e = singleDashArg(args, i);
                    if (e.getArgument() != null) {
                        evaluations.put(e.getName(), e);
                    }
                    i = e.getIndex();
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
                sf = SkillFile.open(evaluations.get("path").getArgument() + File.separator + ".skills");
            } catch (IOException e) {
                ExceptionHandler.handle(e);
                return;
            }
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
                e.printStackTrace();
            }
        }
    }

    private static ArgumentEvaluation noDashArg(String[] args, int index) {
        return new ArgumentEvaluation(index, args[index], Integer.toString(index));
    }

    private static ArgumentEvaluation singleDashArg(String[] args, int index) {
        for (char c : args[index].substring(1).toCharArray()) {
            switch (c) {
                case 'g':
                    index += 1;
                    return new ArgumentEvaluation(index, args[index], "generator");

                case 'a':
                    fileFlag = FileFlag.All;
                    return new ArgumentEvaluation(index, null, "all");

                case 'l':
                    index += 1;
                    return new ArgumentEvaluation(index, args[index], "lang");

                case 'x':
                    index += 1;
                    return new ArgumentEvaluation(index, args[index], "exec");

                case 'p':
                    index += 1;
                    return new ArgumentEvaluation(index, args[index], "path");

                case 'o':
                    index += 1;
                    return new ArgumentEvaluation(index, args[index], "output");

                case 'm':
                    index += 1;
                    return new ArgumentEvaluation(index, args[index], "module");
            }
        }
        return new ArgumentEvaluation(-1, null, null);
    }

    private static ArgumentEvaluation doubleDashArg(String[] args, int index) {
        switch (args[index]) {
            case "--generator":
                index += 1;
                return new ArgumentEvaluation(index, args[index], "generator");

            case "--all":
                fileFlag = FileFlag.All;
                return new ArgumentEvaluation(index, null, "all");

            case "--lang":
                index += 1;
                return new ArgumentEvaluation(index, args[index], "lang");

            case "--exec":
                index += 1;
                return new ArgumentEvaluation(index, args[index], "exec");

            case "--path":
                index += 1;
                return new ArgumentEvaluation(index, args[index], "path");

            case "--output":
                index += 1;
                return new ArgumentEvaluation(index, args[index], "output");

            case "--module":
                index += 1;
                return new ArgumentEvaluation(index, args[index], "module");
        }
        return new ArgumentEvaluation(-1, null, null);
    }

    /**
     * Prints the message saved in {@link #helpText} to the command line.
     */
    private static void printHelp() {
        System.out.println(helpText);
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
        MessageDigest sha1;
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
                            files.add(createToolFile(project, t, file));
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
                e.printStackTrace();
            }
        });
        cleanUp(tempDir);
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File createToolFile(File project, String tool, File file) throws IOException {
        File f = new File(project.getAbsolutePath() + File.separator + ".skillt");
        f = new File(f, tool);
        String name = file.getAbsolutePath();
        name = name.substring(project.getAbsolutePath().length());
        @SuppressWarnings("null")
        File temp = new File(f.getAbsolutePath() + File.separator + name);
        temp.createNewFile();
        try (FileOutputStream fs = new FileOutputStream(temp.getAbsolutePath())) {
            Files.copy(Paths.get(file.getAbsolutePath()), fs);
        }
        return temp;
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
     * Deletes the directory with the temporary files.
     *
     * @param projectRoot The root directory of the project.
     */
    private static void cleanUp(File projectRoot) {
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

    private static class ArgumentEvaluation {
        private final int index;
        private final String argument;
        private final String name;

        public ArgumentEvaluation(int index, String argument, String name) {
            this.index = index;
            this.argument = argument;
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public String getArgument() {
            return argument;
        }

        public String getName() {
            return name;
        }
    }
}
