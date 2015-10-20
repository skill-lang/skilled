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
    private final File JVM_LIB;
    private final File JAVA_LIB;
    private final File TARGET;

    /**
     * @param command   The COMMAND to be executed.
     * @param generator Path to the generator
     * @param target    Output path
     */
    @SuppressWarnings("SpellCheckingInspection")
    public GenerationThread(String command, File generator, File target) {
        this.COMMAND = command;
        this.JVM_LIB = new File(generator.getParentFile().getAbsolutePath() + File.separator + "deps" + File.separator + "skill.jvm.common.jar");
        this.JAVA_LIB = new File(generator.getParentFile().getAbsolutePath() + File.separator + "deps" + File.separator + "skill.java.common.jar");
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
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            boolean knownError = false;
            while ((line = errorReader.readLine()) != null) {
                if (line.contains("java.nio.file.NoSuchFileException: deps/skill.jvm.common.jar") || knownError) {
                    knownError = true;
                } else {
                    System.out.println(line);
                }
            }
            while ((line = reader.readLine()) != null) {
                if (!line.equals("-FAILED-")) {
                    System.out.println(line);
                } else {
                    File jvmFile = new File(TARGET.getAbsolutePath() + File.separator + "java/lib/skill.jvm.common.jar".replace('/', File.separatorChar));
                    File javaFile = new File(TARGET.getAbsolutePath() + File.separator + "java/lib/skill.java.common.jar".replace('/', File.separatorChar));
                    if (jvmFile.exists()) {
                        jvmFile.delete();
                    }
                    if (javaFile.exists()) {
                        javaFile.delete();
                    }
                    Files.copy(JVM_LIB.toPath(), jvmFile.toPath());
                    Files.copy(JAVA_LIB.toPath(), javaFile.toPath());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
