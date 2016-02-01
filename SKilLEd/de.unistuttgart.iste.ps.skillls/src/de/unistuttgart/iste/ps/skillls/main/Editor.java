package de.unistuttgart.iste.ps.skillls.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;


/**
 * This class performs editing tasks on tools.
 *
 * @author Armin Hüneburg
 * @since 25.08.15.
 *
 */
public class Editor {
    private final String COMMAND_STRING;
    private de.unistuttgart.iste.ps.skillls.tools.api.SkillFile skillFile;

    /**
     * @param commandString
     *            String containing the commands that should be executed.
     */
    public Editor(String commandString) {
        this.COMMAND_STRING = commandString;
    }

    /**
     * @param skillFile
     *            The skillfile containing the configuration of the project.
     */
    public void setSkillFile(de.unistuttgart.iste.ps.skillls.tools.api.SkillFile skillFile) {
        this.skillFile = skillFile;
    }

    /**
     * Starts processing the command.
     */
    public void start() {
        String[] commands = COMMAND_STRING.split(";");
        for (String command : commands) {
            processCommand(command);
        }
        skillFile.close();
    }

    /**
     * Processes a single command.
     *
     * @param command
     *            The single command, containing subcommands.
     */
    private void processCommand(String command) {
        String[] subCommands = command.split(":");// TODO: subcommand vllt ändern
        if (subCommands.length <= 1) {
            return;
        }
        int index = 0;
        Tool tool = selectTool(subCommands[index]);
        index++;
        if (tool == null) {
            tool = newTool(subCommands[index]);
            index++;
        }
        if (index < subCommands.length) { // TODO: umkehren
            Command cmd = getCommand(subCommands[index]); // TODO: mehr checks für indizes
            index++;

            try {
                switch (cmd) {
                    case delete:
                        delete(tool);
                        break;

                    case rename:
                        rename(tool, subCommands, index);
                        break;

                    case addType:
                        addType(tool, subCommands, index);
                        break;

                    case removeType:
                        removeType(tool, subCommands, index);
                        break;

                    case addField:
                        addField(tool, subCommands, index);
                        break;

                    case removeField:
                        removeField(tool, subCommands, index);
                        break;

                    case addFieldHint:
                        addFieldHint(tool, subCommands, index);
                        break;

                    case removeFieldHint:
                        removeFieldHint(tool, subCommands, index);
                        break;

                    case addTypeHint:
                        addTypeHint(tool, subCommands, index);
                        break;

                    case removeTypeHint:
                        removeTypeHint(tool, subCommands, index);
                        break;

                    case setDefaults:
                        setDefaults(tool, subCommands, index);
                        break;
                }
            } catch (Error e) {
                ExceptionHandler.handle(e);
            }
        }
    }

    /**
     * Sets the defaults for the binding generation.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void setDefaults(Tool tool, String[] subCommands, int globalIndex) {
        int index = globalIndex;
        String execEnv = subCommands[index];
        index++;
        String path = subCommands[index];
        index++;
        tool.setGenerator(skillFile.Generators().make(execEnv, path));
        tool.setLanguage(subCommands[index]);
        index++;
        tool.setModule(subCommands[index]);
        index++;
        tool.setOutPath(subCommands[index]);
    }

    /**
     * Removes a hint from a type in a tool.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void removeTypeHint(Tool tool, String[] subCommands, int globalIndex) {
        int index = globalIndex;
        String typeName = subCommands[index];
        index++;
        String hintName = subCommands[index];

        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().equals(typeName)) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }

        for (Hint hint : skillFile.Hints()) {
            if (hint.getName().equals(hintName) && hint.getParent() instanceof Type) {
                Type parent = (Type) hint.getParent();
                if (parent.getName().equals(typeName)) {
                    parent.getTypeHints().remove(hint);
                    skillFile.delete(hint);
                    break;
                }
            }
        }
    }

    /**
     * Adds a hint to a type.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void addTypeHint(Tool tool, String[] subCommands, int globalIndex) {
        int index = globalIndex;
        String typeName = subCommands[index];
        index++;
        String hintName = subCommands[index];

        Type toolType = null;
        Type skType = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().equals(typeName)) {
                toolType = t;
                break;
            }
        }
        if (toolType == null) {
            throw new Error("Type not in Tool");
        }
        for (Type t : skillFile.Types()) {
            if (t.getName().equals(typeName)
                    && skillFile.Tools().stream().noneMatch(tool1 -> tool1.getTypes().contains(t))) {
                skType = t;
                break;
            }
        }
        if (skType == null) {
            return;
        }

        Hint hint = null;
        for (Hint h : skType.getTypeHints()) {
            if (h.getName().equals(hintName)) {
                hint = h;
                break;
            }
        }

        if (hint == null) {
            throw new Error("Hint not found");
        }

        if (toolType.getTypeHints().stream().noneMatch(h -> h.getName().equals(hintName))) {
            toolType.getTypeHints().add(skillFile.Hints().make(hint.getName(), toolType));
        }

    }

    /**
     * Removes a hint from a field.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void removeFieldHint(Tool tool, String[] subCommands, int globalIndex) {
        int index = globalIndex;
        String typeName = subCommands[index];
        index++;
        String fieldName = subCommands[index];
        index++;
        String hintName = subCommands[index];

        Type type = null; // TODO: auslagern auch andere stellen und klone; nicht mit strings in den methoden, sondern
                          // vorlagern
        for (Type t : tool.getTypes()) {
            if (t.getName().equals(typeName)) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }

        Field field = null;
        for (Field f : type.getFields()) {
            String[] splits = f.getName().trim().split(" ");
            if (splits[splits.length - 1].equals(fieldName)) {
                field = f;
                break;
            }
        }
        if (field == null) {
            return;
        }

        Hint hint = null;
        for (Hint h : field.getFieldHints()) {
            if (h.getName().equals(hintName)) {
                hint = h;
                break;
            }
        }
        // exit if arguemnt error: hint not found
        if (hint == null) {
            throw new Error("Hint not found");
        }
        field.getFieldHints().remove(hint);
        skillFile.delete(hint);

    }

    /**
     * Adds a hint to a field.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void addFieldHint(Tool tool, String[] subCommands, int globalIndex) {
        int index = globalIndex;
        String typeName = subCommands[index];
        index++;
        String fieldName = subCommands[index];
        index++;
        String hintName = subCommands[index];

        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().equals(typeName)) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }

        Field field = null;
        for (Field f : type.getFields()) {
            String[] splits = f.getName().trim().split(" ");
            if (splits[splits.length - 1].equals(fieldName)) {
                field = f;
                break;
            }
        }
        if (field == null) {
            return;
        }

        boolean found = false;
        for (Hint hint : skillFile.Hints()) {
            if (hint.getName().equals(hintName) && hint.getParent() instanceof Field) {
                Field parent = (Field) hint.getParent();
                Type grandParent = field.getType();
                String[] nameSplits = parent.getName().split(" ");
                if (nameSplits[nameSplits.length - 1].equals(fieldName) && grandParent.getName().equals(typeName)) {
                    found = true;
                    break;
                }
            }
        }
        if (found) {
            field.getFieldHints().add(skillFile.Hints().make(hintName, field));
        }
    }

    /**
     * Removes a field from a tool.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void removeField(Tool tool, String[] subCommands, int globalIndex) {
        int index = globalIndex;
        String typeName = subCommands[index];
        index++;
        String fieldName = subCommands[index];

        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().equals(typeName)) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }

        Field field = null;
        for (Field f : type.getFields()) {
            String[] splits = f.getName().split(" ");
            if (splits[splits.length - 1].equals(fieldName)) {
                field = f;
                break;
            }
        }
        if (field == null) {
            return;
        }
        type.getFields().remove(field);
        skillFile.delete(field);
        removeFieldContent(field);
    }

    /**
     * Adds a field to a type.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void addField(Tool tool, String[] subCommands, int globalIndex) {
        int index = globalIndex;
        String typeName = subCommands[index];
        index++;
        String fieldName = subCommands[index];

        Type type = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().equals(typeName)) {
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }

        for (Field f : type.getFields()) {
            String[] splits = f.getName().split(" ");
            if (splits[1].equals(fieldName)) {
                return;
            }
        }

        for (Field f : skillFile.Fields()) {
            String[] splits = f.getName().split(" ");
            if (splits.length > 1) {
                if (splits[1].equals(fieldName)) {
                    type.getFields()
                            .add(skillFile.Fields().make("", new ArrayList<>(), f.getName(), new ArrayList<>(), type));
                }
            }
        }
    }

    /**
     * Removes a type from a tool.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param globalIndex
     *            The position the subcommand starts in the array.
     */
    private void removeType(Tool tool, String[] subCommands, int globalIndex) {
        String typeName = subCommands[globalIndex];
        Type type = null;
        int i = 0;
        for (Type t : tool.getTypes()) {
            if (typeName.equals(t.getName())) {
                tool.getTypes().remove(i);
                type = t;
                break;
            }
            i++;
        }
        if (type == null) {
            return;
        }
        de.unistuttgart.iste.ps.skillls.tools.File file = null;
        for (de.unistuttgart.iste.ps.skillls.tools.File f : tool.getFiles()) {
            if (f.equals(type.getFile())) {
                file = f;
                tool.getFiles().remove(f);
                break;
            }
        }
        if (file == null) {
            return;
        }
        skillFile.delete(type);
        removeTypeContent(type);
        for (Type t : tool.getTypes()) {
            if (t.getFile().equals(file)) {
                tool.getFiles().add(file);
                break;
            }
        }
    }

    /**
     * Removes the fields and hints from a type
     * @param type type that is deleted
     */
    private void removeTypeContent(Type type) {
        for (Hint hint : type.getTypeHints()) {
            skillFile.delete(hint);
        }
        for (Field f : type.getFields()) {
            skillFile.delete(f);
            removeFieldContent(f);
        }
    }

    /**
     * Removes the hints from a field
     * @param field field that is deleted
     */
    private void removeFieldContent(Field field) {
        for (Hint h : field.getFieldHints()) {
            skillFile.delete(h);
        }
    }

    /**
     * Adds a type to a tool.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param index
     *            The position the subcommand starts in the array.
     */
    private void addType(Tool tool, String[] subCommands, int index) {
        String typeName = subCommands[index];
        for (Type t : skillFile.Types()) {
            // check if type or enum or interface or typed with the name exists
            if (typeName.equals(t.getName()) || t.getName().toLowerCase().equals("enum " + typeName.toLowerCase())
                    || t.getName().toLowerCase().equals("interface " + typeName.toLowerCase())
                    || t.getName().toLowerCase().startsWith("typedef " + typeName.toLowerCase())) {
                Type type = null;
                // type is not in tool
                if (tool.getTypes().stream().noneMatch(ty -> ty.getName().equals(typeName))) {
                    type = skillFile.Types().make(t.getComment(), new ArrayList<>(), new ArrayList<>(), t.getFile(),
                            t.getName(), new ArrayList<>(), new ArrayList<>());
                }
                // type is enum
                if (t.getFields().stream().anyMatch(f -> f.getName().matches("\\S+")) && type != null) {
                    Field field = t.getFields().stream().filter(f -> f.getName().matches("\\S+")).findFirst().get();
                    type.getFields().add(skillFile.Fields().make(field.getComment(), new ArrayList<>(), field.getName(),
                            new ArrayList<>(), type));
                }
                if (type == null || t.getFile() == null) {
                    System.out.println("null");
                    return;
                }
                tool.getTypes().add(type);
                if (type.getName().toLowerCase().startsWith("typedef ")) {
                    addGroundType(type, tool);
                }
                addExtensions(tool, t);
                for (de.unistuttgart.iste.ps.skillls.tools.File f : skillFile.Files()) {
                    if (f.getPath().equals(t.getFile().getPath())) {
                        boolean found = false;
                        for (de.unistuttgart.iste.ps.skillls.tools.File fi : tool.getFiles()) {
                            if (fi.getPath().equals(f.getPath())) {
                                found = true;
                            }
                        }
                        if (!found) {
                            tool.getFiles().add(skillFile.Files().make(new ArrayList<>(), f.getHeader(), f.getMd5(),
                                    f.getPath(), f.getTimestamp()));
                        }
                        return;
                    }
                }
            }
        }
        throw new Error("Type not found");
    }

    /**
     * Adds the groundtype of typedefs to a tool.
     * 
     * @param type
     *            typedef which has a groundtype.
     * @param tool
     *            tool the new types are added to.
     */
    private void addGroundType(Type type, Tool tool) {
        String groundTypeName = type.getName().split(" ")[type.getName().split(" ").length - 1].toLowerCase();
        List<Type> candidates = skillFile.Types().stream().filter(t -> t.getName().toLowerCase().contains(groundTypeName))
                .collect(Collectors.toList());
        for (Type candidate : candidates) {
            String candidateName = candidate.getName().toLowerCase();
            if (candidateName.startsWith("typedef " + groundTypeName) || candidateName.startsWith("enum " + groundTypeName)
                    || candidateName.equals(groundTypeName) || candidateName.startsWith("interface " + groundTypeName)) {
                if (tool.getTypes().stream().filter(t -> t.getName().equals(candidate.getName())).count() > 0) {
                    break;
                }
                tool.getTypes().add(skillFile.Types().make(candidate.getComment(), candidate.getExtends(), new ArrayList<>(),
                        candidate.getFile(), candidate.getName(), candidate.getRestrictions(), new ArrayList<>()));
                break;
            }
        }
    }

    /**
     * Adds the types a new user type extends.
     *
     * @param tool
     *            The tool the new type is added to.
     * @param type
     *            The type that extends other types.
     */
    private void addExtensions(Tool tool, Type type) {
        Type toolType = null;
        for (Type t : tool.getTypes()) {
            if (t.getName().equals(type.getName())) {
                toolType = t;
                break;
            }
        }
        List<String> types = tool.getTypes().stream().map(Type::getName).collect(Collectors.toList());
        for (String extension : type.getExtends()) {
            if (types.contains(extension)) {
                continue;
            }
            List<Type> exts = skillFile.Types().stream()
                    .filter(t -> t.getName().equals(extension) && !types.contains(t.getName())).collect(Collectors.toList());
            for (int i = 0; i < exts.size(); i++) {
                Type t = exts.get(i);
                exts.remove(t);
                if (exts.stream().noneMatch(t1 -> t1.getName().equals(t.getName()))) {
                    exts.add(t);
                } else {
                    i--;
                }
            }
            for (Type t : exts) {
                if (tool.getTypes().stream().map(Type::getName).collect(Collectors.toList()).contains(t.getName())) {
                    continue;
                }
                tool.getTypes().add(skillFile.Types().make(t.getComment(), t.getExtends(), new ArrayList<>(), t.getFile(),
                        t.getName(), t.getRestrictions(), new ArrayList<>()));
                if (t.getExtends().size() > 0) {
                    addExtensions(tool, t);
                }
                if (toolType == null)
                    return;
                toolType.getExtends().add(t.getName());
            }
        }
    }

    /**
     * Renames a tool.
     *
     * @param tool
     *            The tool the settings belong to.
     * @param subCommands
     *            Array with the single subcommands.
     * @param index
     *            The position the subcommand starts in the array.
     */
    private static void rename(Tool tool, String[] subCommands, int index) {
        tool.setName(subCommands[index]);
    }

    /**
     * Deletes a tool.
     *
     * @param tool
     *            The tool the settings belong to.
     */
    private void delete(Tool tool) {
        skillFile.delete(tool);
        tool.getTypes().forEach(this::removeTypeContent);
    }

    /**
     *
     * @param subCommand
     *            The command that has to be matched.
     * @return returns the corresponding enum value.
     */
    private static Command getCommand(String subCommand) {
        return Command.values()[Integer.parseInt(subCommand)];
    }

    /**
     * Creates a new tool.
     *
     * @param subCommand
     *            the name of the tool.
     * @return Returns the tool.
     */
    private Tool newTool(String subCommand) {
        return skillFile.Tools().make(new ArrayList<>(), skillFile.Generators().make("", ""), "", "", subCommand, "",
                new ArrayList<>());
    }

    /**
     * Searches for a tool.
     *
     * @param subCommand
     *            the name of the tool or &n for creating a new tool.
     * @return returns the tool or null if the tool was not found or a new tool should be created.
     */
    private Tool selectTool(String subCommand) {
        if (subCommand.equals("&n")) {
            return null;
        }
        for (Tool t : skillFile.Tools()) {
            if (t.getName().equals(subCommand)) {
                return t;
            }
        }
        throw new IllegalArgumentException(String.format("The specified Tool (%s) was not found", subCommand));
    }
}
