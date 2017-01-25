package de.unistuttgart.iste.ps.skilled.tools;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;

import de.unistuttgart.iste.ps.skilled.sir.BuildInformation;
import de.unistuttgart.iste.ps.skilled.sir.ClassType;
import de.unistuttgart.iste.ps.skilled.sir.EnumType;
import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.InterfaceType;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import de.unistuttgart.iste.ps.skilled.sir.internal.ToolAccess;
import de.unistuttgart.iste.ps.skilled.skill.File;

/**
 * Provides unified access to information stored in SIR files.
 * 
 * @author Timm Felden
 */
public class SIRHelper {

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

    public static ArrayList<FieldLike> fieldsOf(UserdefinedType t) {
        if (t instanceof ClassType) {
            return ((ClassType) t).getFields();
        } else if (t instanceof InterfaceType) {
            return ((InterfaceType) t).getFields();
        } else if (t instanceof EnumType) {
            return ((EnumType) t).getFields();
        } else {
            return new ArrayList<>();
        }
    }
}
