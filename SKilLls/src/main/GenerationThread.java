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
    private final String COMMAND;
    private final File JVMLIB;
    private final File JAVALIB;
    private final File TARGET;

    /**
     * @param command   The COMMAND to be executed.
     * @param generator Path to the generator
     * @param target    Output path
     */
    public GenerationThread(String command, File generator, File target) {
        this.COMMAND = command;
        this.JVMLIB = new File(generator.getParentFile().getAbsolutePath() + File.separator + "deps" + File.separator + "skill.jvm.common.jar");
        this.JAVALIB = new File(generator.getParentFile().getAbsolutePath() + File.separator + "deps" + File.separator + "skill.java.common.jar");
        this.TARGET = target;
    }

    /**
     * The method that is executed with Thread.start().
     */
    @Override
    public void run() {
        Process p;
        try {
            p = Runtime.getRuntime().exec(COMMAND);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals("-FAILED-")) {
                    System.out.println(line);
                } else {
                    Files.copy(JVMLIB.toPath(), new File(TARGET.getAbsolutePath() + File.separator + "java/lib/skill.jvm.common.jar".replace('/', File.separatorChar)).toPath());
                    Files.copy(JAVALIB.toPath(), new File(TARGET.getAbsolutePath() + File.separator + "java/lib/skill.java.common.jar".replace('/', File.separatorChar)).toPath());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
