package de.unistuttgart.iste.ps.skilled.ui.tools;

import org.eclipse.core.resources.IProject;

import de.unistuttgart.iste.ps.skillls.main.MainClass;


public class ToolUtil {

    public static boolean createTool(String name, IProject project) {
        String[] arguments = { "-e", project.getLocation().toPortableString(), "&n:" + name };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean generateTemporarySKilLFiles(String toolname, IProject project, boolean cleanup) {

        if (!cleanup) {
            String[] arguments = { "--no-cleanup", "-aplomx", project.getLocation().toPortableString(), "Java",
                    project.getLocation().toString() + ".skillt", "scala" };
            try {
                MainClass.main(arguments);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        String[] arguments = { "-aplomx", project.getLocation().toPortableString(), "Java",
                project.getLocation().toString() + ".skillt", "scala" };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean addTypeToTool(String toolName, IProject project, String typeName) {
        return false;
    }

    public static boolean removeTypeFromTool() {
        return false;
    }

    public static boolean addField() {
        return false;
    }
}
