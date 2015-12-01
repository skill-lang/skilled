package de.unistuttgart.iste.ps.skilled.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillException;

/**
 * Opens the .skills file in the given path.
 * @author Armin Hüneburg
 *
 */
public class SkillFileOpener {
    private static String path = null;
    private static SkillFile file = null;
    private static boolean pathChanged = false;
    
    /**
     * Sets the path and tries to open the .sf-file at that location.
     * @param path Path to the sf-file
     */
    public static void setPath(String path) {
        if (path == null) {
            return;
        }
        if (SkillFileOpener.path == null || !SkillFileOpener.path.equals(path)) {
            SkillFileOpener.path = path;
            pathChanged = true;
        }
    }

    /**
     * Returns the sf-file at the provided path.
     * @return null if no path is given or there was an error opening the file, else the file.
     */
    public static SkillFile getFile() {
        if (pathChanged) {
            try {
                file = de.unistuttgart.iste.ps.skillls.tools.api.SkillFile.open(new File(path), SkillFile.Mode.Read);
            } catch (SkillException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }
}
