package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

/**
 * Class running a command line command in an extra thread.
 *
 * @author Armin Hüneburg
 * @since 25.08.15.
 */
class GenerationThread implements Runnable {
    private final String command;
    private final File jvmLib;
    private final File javaLib;
    private final File target;

    /**
     * @param command   The command to be executed.
     * @param generator Path to the generator
     * @param target    Output path
     */
    public GenerationThread(String command, File generator, File target) {
        this.command = command;
        jvmLib = new File(generator.getParentFile().getAbsolutePath() + File.separator + "deps" + File.separator + "skill.jvm.common.jar");
        javaLib = new File(generator.getParentFile().getAbsolutePath() + File.separator + "deps" + File.separator + "skill.java.common.jar");
        this.target = target;
    }

    /**
     * The method that is executed with Thread.start().
     */
    @Override
    public void run() {
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("-FAILED-")) {
                    System.out.println(line);
                } else {
                    Files.copy(jvmLib.toPath(), new File(target.getAbsolutePath() + File.separator + "java/lib/skill.jvm.common.jar".replace('/', File.separatorChar)).toPath());
                    Files.copy(javaLib.toPath(), new File(target.getAbsolutePath() + File.separator + "java/lib/skill.java.common.jar".replace('/', File.separatorChar)).toPath());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
