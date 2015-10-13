package main;

import tools.Field;
import tools.Hint;
import tools.Tool;
import tools.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Armin Hüneburg
 * @since 25.08.15.
 */
@SuppressWarnings("unused")
public class Edit {
    private final String COMMANDSTRING;
    private tools.api.SkillFile skillFile;

    /**
     * @param commandString String containing the commands that should be executed.
     */
    public Edit(String commandString) {
        this.COMMANDSTRING = commandString;
    }

    /**
     * @param skillFile The skillfile containing the configuration of the project.
     */
    public void setSkillFile(tools.api.SkillFile skillFile) {
        this.skillFile = skillFile;
    }

    /**
     * Starts processing the command.
     */
    public void start() {
        String[] commands = COMMANDSTRING.split(";");
        for (String command : commands) {
            processCommand(command);
        }
        skillFile.close();
    }

    /**
     * Processes a single command.
     * @param command The single command, containing subcommands.
     */
    private void processCommand(String command) {
        String[] subCommands = command.split(":");
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
        if (index < subCommands.length) {
            Command cmd = getCommand(subCommands[index]);
            index++;

            switch (cmd) {
                case delete:
                    delete(tool, subCommands, index);
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
        }
    }

    /**
     * Sets the defaults for the binding generation.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private void setDefaults(Tool tool, String[] subCommands, int index) {
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
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private void removeTypeHint(Tool tool, String[] subCommands, int index) {
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
            if (f.getName().equals(fieldName)) {
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
        if (hint == null) {
            return;
        }

        field.getFieldHints().remove(hint);
        hint.delete();
    }

    /**
     * Adds a hint to a type.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private void addTypeHint(Tool tool, String[] subCommands, int index) {
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

        Hint hint = null;
        for (Hint h : type.getTypeHints()) {
            if (h.getName().equals(hintName)) {
                hint = h;
                break;
            }
        }
        if (hint != null) {
            return;
        }

        type.getTypeHints().add(skillFile.Hints().make(hintName, type));
    }

    /**
     * Removes a hint from a field.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private void removeFieldHint(Tool tool, String[] subCommands, int index) {
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
            if (f.getName().equals(fieldName)) {
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
        if (hint == null) {
            return;
        }

        field.getFieldHints().remove(hint);
        hint.delete();
    }

    /**
     * Adds a hint to a field.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private void addFieldHint(Tool tool, String[] subCommands, int index) {
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
            if (f.getName().equals(fieldName)) {
                field = f;
                break;
            }
        }
        if (field == null) {
            return;
        }

        field.getFieldHints().add(skillFile.Hints().make(fieldName, field));
    }

    /**
     * Removes a field from a tool.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private void removeField(Tool tool, String[] subCommands, int index) {
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
            if (f.getName().equals(fieldName)) {
                field = f;
                break;
            }
        }
        if (field == null) {
            return;
        }

        type.getFields().remove(field);
        field.delete();
    }

    /**
     * Adds a field to a type.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("AssignmentToMethodParameter")
    private void addField(Tool tool, String[] subCommands, int index) {
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
            if (f.getName().equals(fieldName)) {
                return;
            }
        }
        Field f = skillFile.Fields().make(new ArrayList<>(), fieldName, type);
        type.getFields().add(f);
    }

    /**
     * Removes a type from a tool.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    private void removeType(Tool tool, String[] subCommands, int index) {
        String typeName = subCommands[index];
        Type type = null;
        for (Type t : skillFile.Types()) {
            if (typeName.equals(t.getName())) {
                tool.getTypes().remove(t);
                type = t;
                break;
            }
        }
        if (type == null) {
            return;
        }
        tools.File file = null;
        for (tools.File f : tool.getFiles()) {
            if (f.equals(type.getFile())) {
                file = f;
                tool.getFiles().remove(f);
                break;
            }
        }
        if (file == null) {
            return;
        }
        for (Type t : tool.getTypes()) {
            if (t.getFile().equals(file)) {
                tool.getFiles().add(file);
                break;
            }
        }
    }

    /**
     * Adds a type to a tool.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    private void addType(Tool tool, String[] subCommands, int index) {
        String typeName = subCommands[index];
        for (Type t : skillFile.Types()) {
            if (typeName.equals(t.getName())) {
                if (!tool.getTypes().contains(t)) {
                    tool.getTypes().add(t);
                }
                for (tools.File f : skillFile.Files()) {
                    if (f.equals(t.getFile())) {
                        if (!tool.getFiles().contains(f)) {
                            tool.getFiles().add(f);
                        }
                        return;
                    }
                }
            }
        }
    }

    /**
     * Renames a tool.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    private void rename(Tool tool, String[] subCommands, int index) {
        tool.setName(subCommands[index]);
    }

    /**
     * Deletes a tool.
     * @param tool The tool the settings belong to.
     * @param subCommands Array with the single subcommands.
     * @param index The position the subcommand starts in the array.
     */
    @SuppressWarnings("UnusedParameters")
    private void delete(Tool tool, String[] subCommands, int index) {
        tool.delete();
    }

    /**
     *
     * @param subCommand The command that has to be matched.
     * @return returns the corresponding enum value.
     */
    private Command getCommand(String subCommand) {
        return Command.values()[Integer.parseInt(subCommand)];
    }

    /**
     * Creates a new tool.
     * @param subCommand the name of the tool.
     * @return Returns the tool.
     */
    private Tool newTool(String subCommand) {
        return skillFile.Tools().make(new ArrayList<>(), skillFile.Generators().make("", ""), "", "", subCommand, "", new ArrayList<>());
    }

    /**
     * Searches for a tool.
     * @param subCommand the name of the tool or &n for creating a new tool.
     * @return returns the tool or null if the tool was not found or a new tool should be created.
     */
    private Tool selectTool(String subCommand) {
        if (subCommand.equals("&n")) {
            return null;
        } else {
            for (Tool t : skillFile.Tools()) {
                if (t.getName().equals(subCommand)) {
                    return t;
                }
            }
        }
        return null;
    }
}
