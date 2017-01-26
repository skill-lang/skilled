package de.unistuttgart.iste.ps.skilled.ui.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.xml.crypto.NoSuchMechanismException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import de.unistuttgart.iste.ps.skilled.sir.BuiltinType;
import de.unistuttgart.iste.ps.skilled.sir.ClassType;
import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.GroundType;
import de.unistuttgart.iste.ps.skilled.sir.Identifier;
import de.unistuttgart.iste.ps.skilled.sir.InterfaceType;
import de.unistuttgart.iste.ps.skilled.sir.MapType;
import de.unistuttgart.iste.ps.skilled.sir.SingleBaseTypeContainer;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.Type;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import de.unistuttgart.iste.ps.skilled.sir.internal.ToolAccess.ToolBuilder;
import de.unistuttgart.iste.ps.skilled.tools.SIRCache;
import de.unistuttgart.iste.ps.skilled.tools.SIRHelper;

/**
 * This class encapsulates common modifications of tools.
 * 
 * @author Armin Hüneburg
 * @author Marco Link
 * @author Ken Singer
 * @author Timm Felden
 */
public final class ToolUtil {
    static String addendum = "";

    /**
     * Create a tool and ensure that it is persisted.
     * 
     * @param name
     *            name of the tool
     * @param project
     *            the project the tool should be created in
     * @return true if creation was successful
     */
    public static Tool createNewTool(IProject project, String name, boolean addTypes) {
        SkillFile sf = SIRCache.ensureFile(project);

        ToolBuilder t = sf.Tools().build();
        t.name(name);
        if (addTypes) {
            HashSet<UserdefinedType> types = new HashSet<UserdefinedType>();
            HashMap<UserdefinedType, HashMap<String, FieldLike>> selectedFields = new HashMap<>();
            for (UserdefinedType type : sf.UserdefinedTypes()) {
                types.add(type);
                HashMap<String, FieldLike> fields = new HashMap<>();
                for (FieldLike f : SIRHelper.fieldsOf(type)) {
                    fields.put(f.getName().skillName(), f);
                }
                selectedFields.put(type, fields);
            }
            t.selectedUserTypes(types);
            t.selectedFields(selectedFields);
        }
        Tool rval = t.make();
        sf.flush();
        return rval;

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
     * Ensure that a type is selected in a tool. This will also select base
     * types and super types if required.
     */
    public static void addTypeToTool(Type type, Tool tool) {
        // catch null for simple recursive adding
        if (null == type)
            return;

        if (type instanceof UserdefinedType) {
            HashSet<UserdefinedType> types = tool.getSelectedUserTypes();
            if (types.contains(type))
                return;
            else {
                // add type and follow super types
                types.add((UserdefinedType) type);
                if (type instanceof ClassType) {
                    addTypeToTool(((ClassType) type).getSuper(), tool);
                    for (InterfaceType i : ((ClassType) type).getInterfaces()) {
                        addTypeToTool(i, tool);
                    }
                } else if (type instanceof InterfaceType) {
                    addTypeToTool(((InterfaceType) type).getSuper(), tool);
                    for (InterfaceType i : ((InterfaceType) type).getInterfaces()) {
                        addTypeToTool(i, tool);
                    }
                }
            }
        } else if (type instanceof SingleBaseTypeContainer) {
            addTypeToTool(((SingleBaseTypeContainer) type).getBase().self(), tool);
        } else if (type instanceof MapType) {
            for (GroundType t : ((MapType) type).getBase())
                addTypeToTool(t.self(), tool);
        } else if (type instanceof BuiltinType) {
            // no action required
        } else
            throw new Error("failed to select Type " + type);
    }

    /**
     * Remove a type from a tool. This will also remove all its subtypes and all
     * fields that require any of the removed types.
     */
    public static void removeTypeFromTool(UserdefinedType type, Tool tool) {
        HashMap<UserdefinedType, HashSet<UserdefinedType>> subs = calculateSubtypes(tool.getSelectedUserTypes());
        HashSet<UserdefinedType> removed = new HashSet<>();
        removeRecursive(type, removed, subs);

        // remove types
        tool.getSelectedUserTypes().removeAll(removed);
        // remove fields of removed types
        for (UserdefinedType t : removed)
            tool.getSelectedFields().remove(t);

        // remove fields using removed types
        for (HashMap<String, FieldLike> fs : tool.getSelectedFields().values()) {
            for (Entry<String, FieldLike> p : fs.entrySet()) {
                for (UserdefinedType t : removed)
                    if (usesType(p.getValue().getType(), t))
                        fs.remove(p.getKey());
            }
        }
    }

    private static boolean usesType(Type type, UserdefinedType t) {
        if (type == t) {
            return true;
        } else if (type instanceof SingleBaseTypeContainer) {
            return usesType(((SingleBaseTypeContainer) type).getBase().self(), t);
        } else if (type instanceof MapType) {
            for (GroundType inner : ((MapType) type).getBase())
                if (usesType(inner.self(), t))
                    return true;
        }
        return false;
    }

    private static void removeRecursive(UserdefinedType type, HashSet<UserdefinedType> removed,
            HashMap<UserdefinedType, HashSet<UserdefinedType>> subs) {
        if (removed.contains(type))
            return;

        removed.add(type);
        HashSet<UserdefinedType> subList = subs.get(type);
        if (null != subList)
            for (UserdefinedType sub : subList)
                removeRecursive(sub, removed, subs);
    }

    /**
     * Establish the subtypes relation.
     */
    private static HashMap<UserdefinedType, HashSet<UserdefinedType>> calculateSubtypes(
            HashSet<UserdefinedType> selectedUserTypes) {
        HashMap<UserdefinedType, HashSet<UserdefinedType>> rval = new HashMap<>();
        for (UserdefinedType t : selectedUserTypes) {
            for (UserdefinedType s : SIRHelper.superTypes(t)) {
                HashSet<UserdefinedType> hs = rval.get(s);
                if (null == hs) {
                    rval.put(s, hs = new HashSet<>());
                }
                hs.add(t);
            }
        }

        return rval;
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
    public static void addFieldToTool(FieldLike field, UserdefinedType type, Tool tool) {
        HashMap<UserdefinedType, HashMap<String, FieldLike>> selected = tool.getSelectedFields();
        HashMap<String, FieldLike> fs = selected.get(type);
        if (null == fs) {
            fs = new HashMap<>();
            selected.put(type, fs);
        }
        fs.put(field.getName().getSkillname(), field);

        addTypeToTool(type, tool);
        addTypeToTool(field.getType(), tool);
    }

    /**
     * Remove a field from a tool
     */
    public static void removeFieldFromTool(FieldLike field, UserdefinedType type, Tool tool) {
        HashMap<UserdefinedType, HashMap<String, FieldLike>> selected = tool.getSelectedFields();
        HashMap<String, FieldLike> fs = selected.get(type);
        if (null == fs) {
            // if no fields are present, we do not need to add them now
            return;
        }
        fs.remove(field.getName().getSkillname());
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
