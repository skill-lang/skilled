package main;

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

    /**
     * Entry Point. Generates flags for the generator execution.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        FileFlag changeFlag = FileFlag.Changed;
        // HintFlag hintFlag = HintFlag.No;
        String lang = "";
        String pack = "";
        String exec = "";
        Edit edit = null;
        File gen = null;
        ArrayList<String> ts = new ArrayList<>();
        File project = null;
        File output = null;
        boolean generate = true;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                switch (arg) {
                    case "--all":
                        changeFlag = FileFlag.All;
                        break;

                    case "--hints":
                        // hintFlag = HintFlag.Yes;
                        break;

                    case "--lang":
                        lang = args[i + 1];
                        i++;
                        break;

                    case "--generator":
                        gen = new File(args[i + 1]);
                        i++;
                        break;

                    case "--ls":
                        generate = false;
                        break;

                    case "--help":
                        printHelp();
                        return;

                    default:
                        break;
                }
            } else if (arg.startsWith("-")) {
                for (char c : arg.toCharArray()) {
                    switch (c) {
                        case 'a':
                            changeFlag = FileFlag.All;
                            break;

                        case 'e':
                            generate = false;
                            edit = new Edit();
                            break;

                        case 'h':
                            // hintFlag = HintFlag.Yes;
                            break;

                        case 'l':
                            lang = args[i + 1];
                            i++;
                            break;

                        case 'g':
                            gen = new File(args[i + 1]);
                            i++;
                            break;

                        default:
                            break;
                    }
                }
            } else {
                if (exec.isEmpty()) {
                    exec = arg;
                } else if (project == null) {
                    project = new File(arg);
                } else if (output == null) {
                    output = new File(arg);
                } else if (i != args.length - 1) {
                    ts.add(arg);
                } else {
                    pack = arg;
                }
            }
        }
        if (generate) {
            try {
                generate(project, output, pack, ts, lang, exec, gen, changeFlag);
            } catch (ProjectException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (edit != null) {
            project = new File(exec);
            edit.setProject(project);
            try {
                tools.api.SkillFile state = tools.api.SkillFile.open(project.getPath() + File.separator + "tools");
                edit.setFileAccess(state.Files());
                edit.setSkillFile(state);
                edit.setToolAccess(state.Tools());
                edit.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
     * @param project
     *            The directory the project is stored in.
     * @param output
     *            The directory the output is stored in.
     * @param pack
     *            The package the binding should be placed in.
     * @param ts
     *            The tools being affected.
     * @param language
     *            The language the binding will be generated in.
     * @param exec
     *            The program executing the generator, e.g. scala.
     * @param generator
     *            The generator used for the generation.
     * @param changeFlag
     *            The flag for the whether to consider only one file or all.
     * @throws ProjectException
     *             Thrown if project is not a directory or does not contain a tools.sf file. Also if output is not a
     *             directory.
     * @throws IOException
     *             Thrown if there is a problem with the creation of temporary files.
     */
    private static void generate(File project, File output, String pack, ArrayList<String> ts, String language, String exec,
            File generator, FileFlag changeFlag) throws ProjectException, IOException {
        HashSet<tools.Tool> toolsToBuild = new HashSet<>();
        HashMap<String, ArrayList<File>> toolToFile = new HashMap<>();
        if (!project.isDirectory()) {
            throw new ProjectException(project.getPath() + " is not a directory.");
        }

        if (!output.isDirectory()) {
            throw new ProjectException(project.getPath() + " is not a directory.");
        }

        File toolRoot = new File(project.getPath() + File.separator + "tools");
        if (!toolRoot.exists()) {
            throw new ProjectException(project.getPath() + " does not contain tools");
        }

        if (language.isEmpty() || !generator.exists()) {
            throw new IllegalArgumentException("Language (--lang or -l [language]) and Generator (--generator or -g "
                    + "[generator]) are required parameters for generation");
        }

        tools.api.SkillFile sf;
        try {
            sf = tools.api.SkillFile.open(toolRoot);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        tools.internal.ToolAccess ta = sf.Tools();

        MessageDigest md5;
        MessageDigest sha1;
        try {
            md5 = MessageDigest.getInstance("MD5");
            sha1 = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }

        for (tools.Tool t : ta) {
            if (!ts.contains(t.getName())) {
                continue;
            }

            for (tools.File f : t.getFiles()) {
                File toolFile = createToolFile(project, t, f);

                if (toolToFile.get(t.getName()) == null) {
                    toolToFile.put(t.getName(), new ArrayList<>());
                }

                toolToFile.get(t.getName()).add(toolFile);

                DigestInputStream md5Dis;
                DigestInputStream sha1Dis;
                byte[] buffer = new byte[4096];
                try (InputStream is = Files.newInputStream(Paths.get(toolFile.getPath()))) {
                    md5Dis = new DigestInputStream(is, md5);
                    sha1Dis = new DigestInputStream(is, sha1);
                    while (md5Dis.read(buffer, 0, buffer.length) != -1) {
                        // noinspection ResultOfMethodCallIgnored
                        sha1Dis.read(buffer, 0, buffer.length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                String md5String = encodeHex(md5Dis.getMessageDigest().digest());
                String sha1String = encodeHex(sha1Dis.getMessageDigest().digest());
                if (!(f.getMd5().equals(md5String) && f.getSha().equals(sha1String)) || changeFlag == FileFlag.All) {
                    f.setMd5(md5String);
                    f.setSha(sha1String);
                    toolsToBuild.add(t);
                }
            }
        }
        runGeneration(toolsToBuild, toolToFile, language, exec, generator, output, pack);
    }

    /**
     * Method for accumulating Thread for Binding generation per tool. Also runs them.
     *
     * @param toolsToBuild
     *            Tools that need to be built.
     * @param toolToFile
     *            Map for mapping tools to their corresponding temporary files.
     * @param language
     *            The language the binding should be in.
     * @param exec
     *            The execution environment for the generator, e.g. scala.
     * @param generator
     *            The generator used for the bindings.
     * @param output
     *            The output directory.
     * @param pack
     *            The package the bindings should be placed in.
     */
    private static void runGeneration(HashSet<tools.Tool> toolsToBuild, HashMap<String, ArrayList<File>> toolToFile,
            String language, String exec, File generator, File output, String pack) {
        ArrayList<Thread> commands = new ArrayList<>();
        for (tools.Tool t : toolsToBuild) {
            StringBuilder builder = new StringBuilder();
            builder.append(exec);
            builder.append(' ');
            builder.append(generator.getAbsolutePath());
            builder.append(' ');
            builder.append("-L ");
            builder.append(language);
            builder.append(' ');
            builder.append("-p ");
            builder.append(pack);
            builder.append(' ');
            for (File f : toolToFile.get(t.getName())) {
                builder.append(f.getAbsolutePath());
                builder.append(' ');
            }
            builder.append(output.getAbsolutePath());
            commands.add(new Thread(new GenerationThread(builder.toString(), generator, output)));
        }
        commands.forEach(java.lang.Thread::start);
        commands.forEach((thread) -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        cleanUp(toolToFile);
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
    private static File createToolFile(File project, tools.Tool tool, tools.File file) throws IOException {
        boolean tempFound = false;
        int counter = 0;
        File f = null;
        while (!tempFound) {
            f = new File(project.getAbsolutePath() + File.separator + counter);
            f = new File(f, tool.getName());
            if (!f.exists()) {
                f.mkdirs();
                tempFound = true;
            } else {
                counter++;
            }
        }
        String name = file.getPath();
        name = name.substring(name.lastIndexOf(File.separator, name.length()));
        @SuppressWarnings("null")
        File temp = new File(f.getAbsolutePath() + File.separator + name);
        temp.createNewFile();
        Files.copy(Paths.get(file.getPath()), new FileOutputStream(temp.getAbsolutePath()));
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
     * @param toolToFiles
     *            HashMap containing the Tools and the Files.
     */
    public static void cleanUp(HashMap<String, ArrayList<File>> toolToFiles) {
        File f = toolToFiles.get(toolToFiles.keySet().toArray()[0]).get(0);
        f = f.getParentFile();
        f = f.getParentFile();
        deleteDir(f);
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty or this is a file so delete it
        return dir.delete();
    }
}
