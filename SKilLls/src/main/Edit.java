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
class Edit {
    private final String commandString;
    private tools.api.SkillFile sk;

    /**
     * @param commandString String containing the commands that should be executed.
     */
    public Edit(String commandString) {
        this.commandString = commandString;
    }

    /**
     * @param sk The skillfile containing the configuration of the project.
     */
    public void setSkillFile(tools.api.SkillFile sk) {
        this.sk = sk;
    }

    public void start() {
        String[] commands = commandString.split(";");
        for (String command : commands) {
            processCommand(command);
        }
    }

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
        Command cmd = getCommand(subCommands[index]);
        index++;

        Method m;
        try {
            m = this.getClass().getMethod(cmd.name(), Tool.class, String[].class, int.class);
        } catch (NoSuchMethodException e) {
            ExceptionHandler.handle(e);
            return;
        }
        try {
            m.invoke(this, tool, subCommands, index);
        } catch (IllegalAccessException | InvocationTargetException e) {
            ExceptionHandler.handle(e);
        }
    }

    private void setDefaults(Tool tool, String[] subCommands, int index) {
        String execEnv = subCommands[index];
        index++;
        String path = subCommands[index];
        index++;
        tool.setGenerator(sk.Generators().make(execEnv, path));
        tool.setLanguage(subCommands[index]);
        index++;
        tool.setModule(subCommands[index]);
        index++;
        tool.setOutPath(subCommands[index]);
    }

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

        type.getTypeHints().add(sk.Hints().make(hintName, type));
    }

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

        field.getFieldHints().add(sk.Hints().make(fieldName, field));
    }

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
        Field f = sk.Fields().make(new ArrayList<>(), fieldName, type);
        type.getFields().add(f);
    }

    private void removeType(Tool tool, String[] subCommands, int index) {
        String typeName = subCommands[index];
        Type type = null;
        for (Type t : sk.Types()) {
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
            if (f.getPath().equals(type.getFilePath())) {
                file = f;
                tool.getFiles().remove(f);
                break;
            }
        }
        if (file == null) {
            return;
        }
        for (Type t : tool.getTypes()) {
            if (t.getFilePath().equals(file.getPath())) {
                tool.getFiles().add(file);
                break;
            }
        }
    }

    private void addType(Tool tool, String[] subCommands, int index) {
        String typeName = subCommands[index];
        for (Type t : sk.Types()) {
            if (typeName.equals(t.getName())) {
                tool.getTypes().add(t);
                for (tools.File f : tool.getFiles()) {
                    if (f.getPath().equals(t.getFilePath())) {
                        tool.getFiles().add(f);
                        return;
                    }
                }
            }
        }
    }

    private void rename(Tool tool, String[] subCommands, int index) {
        tool.setName(subCommands[index]);
    }

    @SuppressWarnings("UnusedParameters")
    private void delete(Tool tool, String[] subCommands, int index) {
        tool.delete();
    }

    private Command getCommand(String subCommand) {
        return Command.values()[Integer.parseInt(subCommand)];
    }

    private Tool newTool(String subCommand) {
        return sk.Tools().make(new ArrayList<>(), sk.Generators().make(), "", "", subCommand, "", new ArrayList<>());
    }

    private Tool selectTool(String subCommand) {
        if (subCommand.equals("&n")) {
            return null;
        } else {
            for (Tool t : sk.Tools()) {
                if (t.getName().equals(subCommand)) {
                    return t;
                }
            }
        }
        return null;
    }
}
