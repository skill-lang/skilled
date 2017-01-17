package de.unistuttgart.iste.ps.skilled.tools;

import java.util.HashSet;

import org.eclipse.core.resources.IProject;

import de.unistuttgart.iste.ps.skilled.sir.BuildInformation;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import de.unistuttgart.iste.ps.skilled.sir.internal.ToolAccess;
import de.unistuttgart.iste.ps.skilled.skill.File;

/**
 * Holds information for a project's tools.
 * 
 * @author Timm Felden
 */
public class ToolInfo {

    public static ToolAccess getTools(File f) {
        IProject p = SIRCache.getProject(f);
        SkillFile sf = SIRCache.ensureFile(p);
        return sf.Tools();
    }

    public static ToolAccess getTools(IProject p) {
        SkillFile sf = SIRCache.ensureFile(p);
        return sf.Tools();
    }

    public static HashSet<String> usedLanguages(IProject p) {
        HashSet<String> rval = new HashSet<>();
        for (Tool t : getTools(p)) {
            for (BuildInformation b : t.getBuildTargets()) {
                rval.add(b.getLanguage());
            }
        }
        return rval;
    }
}
