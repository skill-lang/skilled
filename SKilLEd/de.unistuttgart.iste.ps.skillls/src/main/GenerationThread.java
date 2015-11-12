package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Class running a command line command in an extra thread.
 *
 * @author Armin Hüneburg
 * @since 25.08.15.
 */
class GenerationThread implements Runnable {
    private final String COMMAND;
    private final File PARENT;

    /**
     * @param command
     *            Command to be executed
     * @param generator
     *            Path to the generator
     */
    public GenerationThread(String command, File generator) {
        this.COMMAND = command;
        generator.getName();
        this.PARENT = generator.getParentFile().getAbsoluteFile();
    }

    /**
     * The method that is executed with Thread.start().
     */
    @Override
    public void run() {
        try {
            Process p = Runtime.getRuntime().exec(COMMAND, null, PARENT);
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
                System.out.println(line);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
