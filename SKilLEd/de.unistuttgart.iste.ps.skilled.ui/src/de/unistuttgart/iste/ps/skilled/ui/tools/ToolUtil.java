package de.unistuttgart.iste.ps.skilled.ui.tools;

import org.eclipse.core.resources.IProject;

import de.unistuttgart.iste.ps.skillls.main.MainClass;


/**
 * Class for executing SKilLls commands.
 * 
 * @author Armin Hüneburg
 * @author Marco Link
 */
public final class ToolUtil {

    /**
     * Tries to create a new Tool.
     * 
     * @param name
     *            name of the tool
     * @param project
     *            the project the tool should be created in
     * @return true if creation was successful
     */
    public static boolean createTool(String name, IProject project) {
        String[] arguments = { "-e", project.getLocation().toPortableString(), "&n:" + name };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Tries to set the default Settings for a tool.
     * 
     * @param toolName
     *            name of the tool
     * @param project
     *            the project the tool should be created in
     * @param execEnv
     *            the name of the executable to execute the generator with
     * @param generator
     *            the location of the generator
     * @param language
     *            the language the binding of the tool should be generated in
     * @param module
     *            the module of the binding of the tool
     * @param outPath
     *            the location the binding of the tool will be generated in
     * @return true if setting was successful
     */
    public static boolean setDefaults(String toolName, IProject project, String execEnv, String generator, String language,
            String module, String outPath) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                toolName + ":10:" + execEnv + ":" + generator + ":" + language + ":" + module + ":" + outPath };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to generate the temporary files in the $PROJECT_PATH/.skillt directory
     * 
     * @param toolname
     *            the name of the tool whose temporary files should be created
     * @param project
     *            the project the tool should be created in
     * @return true if creation was successful
     */
    public static boolean generateTemporarySKilLFiles(String toolname, IProject project) {
        String[] arguments = { "--no-cleanup", "-aplomx", project.getLocation().toPortableString(), "Java",
                project.getLocation().toString() + ".skillt", "scala", toolname };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Tries to add a new Tool to the project
     * 
     * @param toolName
     *            the name of the tool
     * @param project
     *            the project containing tool
     * @param typeName
     *            the name of the type to be added
     * @return true if adding was successful
     */
    public static boolean addTypeToTool(String toolName, IProject project, String typeName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(), toolName + ":2:" + typeName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to remove a type from a tool
     * 
     * @param toolName
     *            name of the tool the type belongs to
     * @param project
     *            the project containing tool
     * @param typeName
     *            the type to be removed
     * @return true if removing was successful
     */
    public static boolean removeTypeFromTool(String toolName, IProject project, String typeName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(), toolName + ":3:" + typeName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to add a field to a type to a tool
     * 
     * @param toolName
     *            name of the tool the field-containing type belongs to
     * @param project
     *            the project containing tool
     * @param typeName
     *            name of the type containing the field
     * @param fieldName
     *            the name of the field that should be added
     * @return true if adding was successful
     */
    public static boolean addField(String toolName, IProject project, String typeName, String fieldName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                toolName + ":4:" + typeName + ":" + fieldName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to remove a field from a tool
     * 
     * @param toolName
     *            the name of the tool
     * @param project
     *            the project containing tool
     * @param typeName
     *            the type name of the type containing the field
     * @param fieldName
     *            the field to be removed
     * @return true if removing was successful
     */
    public static boolean removeField(String toolName, IProject project, String typeName, String fieldName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                toolName + ":5:" + typeName + ":" + fieldName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to add a hint to a type in a tool
     * 
     * @param toolName
     *            the tool containing the type
     * @param project
     *            the project containing tool
     * @param typeName
     *            the type containing the hint
     * @param hintName
     *            the name of the hint to be added
     * @return true if adding was successful
     */
    public static boolean addTypeHint(String toolName, IProject project, String typeName, String hintName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                toolName + ":8:" + typeName + ":" + hintName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to remove a hint from a type in a tool
     * 
     * @param toolName
     *            the tool containing the type
     * @param project
     *            the project containing tool
     * @param typeName
     *            the type containing the hint
     * @param hintName
     *            the name of the hint to be removed
     * @return true if removing was successful
     */
    public static boolean removeTypeHint(String toolName, IProject project, String typeName, String hintName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                toolName + ":9:" + typeName + ":" + hintName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to add a hint to a field in a type in a tool
     * 
     * @param toolName
     *            the tool containing the field
     * @param project
     *            the project containing tool
     * @param typeName
     *            the type containing the field
     * @param fieldName
     *            the field containing the hint
     * @param hintName
     *            the name of the hint to be added
     * @return true if adding was successful
     */
    public static boolean addFieldHint(String toolName, IProject project, String typeName, String fieldName,
            String hintName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                toolName + ":6:" + typeName + ":" + fieldName + ":" + hintName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to remove a hint from a field in a type in a tool
     * 
     * @param toolName
     *            the name of the tool containing the hint
     * @param project
     *            the project containing tool
     * @param typeName
     *            the type containing the field
     * @param fieldName
     *            the field containing the hint
     * @param hintName
     *            the hint to be removed
     * @return True if removing was successful
     */
    public static boolean removeFieldHint(String toolName, IProject project, String typeName, String fieldName,
            String hintName) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                toolName + ":7:" + typeName + ":" + fieldName + ":" + hintName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Tries to rename a tool
     * 
     * @param oldToolName
     *            the original name of the tool
     * @param newToolName
     *            the new name of the tool
     * @param project
     *            the project containing tool
     * @return true if renaming was successful
     */
    public static boolean renameTool(String oldToolName, String newToolName, IProject project) {
        String[] arguments = new String[] { "-e", project.getLocation().toPortableString(),
                oldToolName + ":1:" + newToolName };
        try {
            MainClass.main(arguments);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
