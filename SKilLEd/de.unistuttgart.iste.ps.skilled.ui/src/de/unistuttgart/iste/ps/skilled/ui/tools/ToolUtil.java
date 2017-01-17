package de.unistuttgart.iste.ps.skilled.ui.tools;

import javax.xml.crypto.NoSuchMechanismException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.Identifier;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.Type;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;

/**
 * Class for executing SKilLls commands.
 * 
 * @author Armin Hüneburg
 * @author Marco Link
 * @author Ken Singer
 */
public final class ToolUtil {
    static String addendum = "";

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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(), "&n:" + name };

        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
    public static boolean setDefaults(String toolName, IProject project, String execEnv, String generator,
            String language, String module, String outPath) {
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    toolName + ":10:" + execEnv + ":" + generator + ":" + language + ":" + module + ":" + outPath };

        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
            return false;
        }
    }

    /**
     * Tries to generate the temporary files in the $PROJECT_PATH/.skillt
     * directory
     * 
     * @param toolname
     *            the name of the tool whose temporary files should be created
     * @param project
     *            the project the tool should be created in
     * @return true if creation was successful
     */
    public static boolean generateTemporarySKilLFiles(String toolname, IProject project) {
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "--no-cleanup", "-aplomx", project.getLocation().toPortableString(), "Java",
                    project.getLocation().toString(), "scala", toolname };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(), toolName + ":2:" + typeName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(), toolName + ":3:" + typeName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    toolName + ":4:" + typeName + ":" + fieldName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    toolName + ":5:" + typeName + ":" + fieldName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    toolName + ":8:" + typeName + ":" + hintName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    toolName + ":9:" + typeName + ":" + hintName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    toolName + ":6:" + typeName + ":" + fieldName + ":" + hintName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    toolName + ":7:" + typeName + ":" + fieldName + ":" + hintName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
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
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(),
                    oldToolName + ":1:" + newToolName };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
            return false;
        }
    }

    /**
     * Tries to delete a tool
     * 
     * @param project
     *            the project containing the tool.
     * @param name
     *            the name of the tool to be deleted.
     * @return true if deletion was successful
     */
    public static boolean removeTool(IProject project, String name) {
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(), name + ":0" };
        try {
            // TODO MainClass.start(Indexing.NO_INDEXING, arguments);
            return true;
        } catch (@SuppressWarnings("unused") Throwable t) {
            return false;
        }
    }

    /**
     * Tries to clone a Tool.
     * 
     * @param project
     *            The project containing the tool.
     * @param tool
     *            The tool that should be cloned.
     * @param sk
     *            The skillFile object containing the type definitions.
     * @return true if cloning was successful.
     */
    public static boolean cloneTool(IProject project, Tool tool, String newToolName, SkillFile sk) {
        if (project == null)
            return false;

        throw new NoSuchMethodError("TODO");

        // boolean value = true;
        // final String newName = newToolName;
        // addendum = "";
        // int counter = 0;
        // while (sk.Tools().stream().anyMatch(t -> t.getName().equals(newName +
        // addendum))) {
        // addendum = String.valueOf(++counter);
        // }
        // String newNameForTool = newName + addendum;
        // value &= createTool(newNameForTool, project);
        // for (Type type : tool.getTypes()) {
        // value &= addTypeToTool(newNameForTool, project,
        // getActualName(type.getName()));
        // for (Hint h : type.getHints()) {
        // value &= addTypeHint(newNameForTool, project,
        // getActualName(type.getName()), h.getName());
        // }
        // for (Field f : type.getFields()) {
        // value &= addField(newNameForTool, project,
        // getActualName(type.getName()), getActualName(f.getName()));
        // for (Hint h : f.getHints()) {
        // value &= addFieldHint(newNameForTool, project,
        // getActualName(type.getName()),
        // getActualName(f.getName()), h.getName());
        // }
        // }
        // }
        // return value && setDefaults(newNameForTool, project,
        // tool.getGenerator().getExecEnv(),
        // tool.getGenerator().getPath(), tool.getLanguage(), tool.getModule(),
        // tool.getOutPath());
    }

    /**
     * returns the pure name of a type or field
     */
    public static String getActualName(Identifier identifier) {
        return identifier.getSkillname();
    }

    /**
     * delete all the tool-specific objects out of the .skillls file, when a
     * tool is deleted
     * 
     * @param project
     *            - the project, where the tool originates in
     * @param tool
     *            - the tool, which is deleted
     */
    public static void cleanUpAfterDeletion(IProject project, Tool tool) {
        for (UserdefinedType toDelete : tool.getSelectedUserTypes()) {
            removeAllTypeHints(project, tool, toDelete);
            removeAllFields(project, tool, toDelete);
            removeTypeFromTool(tool.getName(), project, getActualName(toDelete.getName()));
        }
    }

    /**
     * add all typehints of a type to a tool
     * 
     * @param project-
     *            the project, where the tool originates in
     * @param tool-
     *            the tool, where the hints should be added to
     * @param type
     *            - the type containing the hints
     */
    public static void addAllTypeHints(IProject project, Tool tool, UserdefinedType type) {
        throw new NoSuchMethodError();
        // for (Hint toAdd : type.getHints())
        // addTypeHint(tool.getName(), project, getActualName(type.getName()),
        // toAdd.getName());
    }

    /**
     * remove all hints of a type from a tool
     * 
     * @param project
     *            - the project, where the tool originates in
     * @param tool
     *            - the tool, which contains the typeHints
     * @param type
     *            - the type to delete
     */
    public static boolean removeAllTypeHints(IProject project, Tool tool, UserdefinedType type) {
        throw new NoSuchMethodError();
        // boolean failure = false;
        // for (Hint toDelete : type.getHints()) {
        // if (!removeTypeHint(tool.getName(), project,
        // getActualName(type.getName()),
        // getActualName(toDelete.getName())))
        // failure = true;
        // }
        // return failure;
    }

    /**
     * add all field of a type to a tool
     * 
     * @param project-
     *            the project, where the tool originates in
     * @param tool
     *            - the tool, where the fields should be added to
     * @param type
     *            - the type containing the fields
     */
    public static void addAllFields(IProject project, Tool tool, UserdefinedType type) {
        throw new NoSuchMethodError();
        // for (Field toAdd : type.getFields()) {
        // if (toAdd != null) {
        // addField(tool.getName(), project, getActualName(type.getName()),
        // getActualName(toAdd.getName()));
        // addAllFieldHints(project, tool, type, toAdd);
        // }
        // }
    }

    /**
     * remove all fields of a type from a tool
     * 
     * @param project
     *            - the project, where the tool originates in
     * @param tool
     *            - the tool, which contains the fields
     * @param type
     *            - the type to delete
     */
    public static void removeAllFields(IProject project, Tool tool, UserdefinedType type) {
        throw new NoSuchMethodError();
        // for (Field toDelete : type.getFields()) {
        // removeAllFieldHints(project, tool, type, toDelete);
        // removeField(tool.getName(), project, getActualName(type.getName()),
        // getActualName(toDelete.getName()));
        // }
    }

    /**
     * add all fieldhints of a field to a tool
     * 
     * @param project-
     *            the project, where the tool originates in
     * @param tool
     *            - the tool, where the field should be added to
     * @param type
     *            - the type, containing the field
     * @param field
     *            - the field containing the fieldhints
     */
    public static void addAllFieldHints(IProject project, Tool tool, UserdefinedType type, FieldLike field) {
        throw new NoSuchMechanismException();
        // for (Hint toAdd : field.getHints())
        // ToolUtil.addFieldHint(tool.getName(), project,
        // getActualName(type.getName()),
        // getActualName(field.getName()), toAdd.getName());
    }

    /**
     * remove all fieldhints of a field from a tool
     * 
     * @param project
     *            - the project, where the tool originates in
     * @param tool
     *            - the tool, which contains the fieldhints
     * @param type
     *            - the type , which contains the fields
     * @param field
     *            - the field to delete
     */
    public static boolean removeAllFieldHints(IProject project, Tool tool, Type type, FieldLike field) {
        throw new NoSuchMechanismException();
        // boolean failure = false;
        // for (Hint toDelete : field.getHints()) {
        // if (!removeFieldHint(tool.getName(), project,
        // getActualName(type.getName()), getActualName(field.getName()),
        // getActualName(toDelete.getName())))
        // failure = true;
        //
        // }
        // return failure;
    }

    /**
     * removes a type and all the types that inherit from it.
     * 
     * @param project
     *            - the project, where the tool originates in
     * @param tool
     *            - the tool, which contains the type
     * @param type
     *            - the type to delete
     */
    public static void removeTypeAndAllSubtypes(IProject project, Tool tool, UserdefinedType type) {
        throw new NoSuchMethodError();
        // for (UserdefinedType ty : tool.getSelectedUserTypes()) {
        // if (ty.getExtends().size() > 0 &&
        // ty.getExtends().contains(type.getName())) {
        // removeTypeAndAllSubtypes(project, tool, ty);
        // }
        // ToolUtil.removeTypeFromTool(tool.getName(), project, type.getName());
        // }

    }

    /**
     * adds every type, field and hint in the read {@link SkillFile} to a tool.
     * 
     * @param skillFile
     *            - read in skillfile
     * @param project
     *            - the currently active project
     * @param tool
     *            - the tool, which should contain everything
     */
    public static void addAllToTool(SkillFile skillFile, IProject project, Tool tool) {
        for (UserdefinedType type : skillFile.UserdefinedTypes()) {
            ToolUtil.addTypeToTool(tool.getName(), project, getActualName(type.getName()));
            ToolUtil.addAllTypeHints(project, tool, type);
            ToolUtil.addAllFields(project, tool, type);
        }
    }

    /**
     * indexes the {@link SkillFile} of the currently active {@link IProject}
     * 
     * @param project
     * @return
     * @throws CoreException
     */
    public static boolean indexing(IProject project) throws CoreException {
        String[] arguments = null;
        if (project != null)
            arguments = new String[] { "-e", project.getLocation().toPortableString(), "" };

        // TODO MainClass.start(Indexing.JUST_INDEXING, arguments);
        return true;
    }
}
