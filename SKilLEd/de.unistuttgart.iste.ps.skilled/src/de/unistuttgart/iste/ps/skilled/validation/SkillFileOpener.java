package de.unistuttgart.iste.ps.skilled.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import de.unistuttgart.iste.ps.skilled.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillException;


public class SkillFileOpener {
    private static String path = null;
    private static SkillFile file = null;
    private static boolean pathChanged = false;
    
    public static void setPath(String path) {
        if (path == null) {
            return;
        }
        if (SkillFileOpener.path == null || !SkillFileOpener.path.equals(path)) {
            SkillFileOpener.path = path;
            pathChanged = true;
        }
    }

    public static SkillFile getFile() {
        if (pathChanged) {
            try {
                file = SkillFile.open(path, new SkillFile.Mode[] {SkillFile.Mode.Read});
            } catch (SkillException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }
}
