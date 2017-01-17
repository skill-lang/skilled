package de.unistuttgart.iste.ps.skilled.tools;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import de.ust.skill.common.java.api.SkillException;

/**
 * Holds open sir files for each project.
 * Files are opened in write mode and flushed on change.
 * 
 * @author Timm Felden
 */
public enum SIRCache {
    instance;

    private HashMap<IProject, SkillFile> sirFiles = new HashMap<>();

    public static SkillFile ensureFile(IProject p) {
        SkillFile rval = instance.sirFiles.get(p);
        if (null == rval) {
            try {
                instance.sirFiles.put(p, rval = SkillFile.open(p.getFile(".sir").getLocationURI().toURL().getFile()));
            } catch (SkillException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return rval;
    }

    public static IProject getProject(EObject f) {
        try {
            Resource resource = f.eResource();
            String platformString = resource.getURI().toPlatformString(false);
            return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString)).getProject();
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
