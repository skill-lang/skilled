package de.unistuttgart.iste.ps.skillls.editor;

import de.unistuttgart.iste.ps.skillls.main.Command;
import de.unistuttgart.iste.ps.skillls.main.ExceptionHandler;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Hint;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
		String[] subCommands = command.split(":");// TODO: subcommand vllt
		// ändern
		if (subCommands.length <= 1) {
			return;
		}
		int index = 0;
		Tool tool = EditorUtil.selectTool(subCommands[index], skillFile);
		index++;
		if (tool == null) {
			tool = newTool(subCommands[index]);
			index++;
		}
		if (index >= subCommands.length) {
			return;
		}
		Command cmd = EditorUtil.getCommand(subCommands[index]);
		index++;

		try {
			chooseAction(subCommands, index, tool, cmd);
		} catch (Error e) {
			e.printStackTrace();
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Chooses which method is executed
	 * 
	 * @param subCommands
	 *            the array, containing all steps of the command
	 * @param index
	 *            the current index in the subcommand
	 * @param tool
	 *            the tool that is modified
	 * @param command
	 *            the command that has to be executed
	 */
	private void chooseAction(String[] subCommands, int index, Tool tool, Command command) {
		switch (command) {
			case delete :
				delete(tool);
				break;

			case rename :
				rename(tool, subCommands, index);
				break;

			case addType :
				addType(tool, subCommands, index);
				break;

			case removeType :
				removeType(tool, subCommands, index);
				break;

			case addField :
				addField(tool, subCommands, index);
				break;

			case removeField :
				removeField(tool, subCommands, index);
				break;

			case addFieldHint :
				addFieldHint(tool, subCommands, index);
				break;

			case removeFieldHint :
				removeFieldHint(tool, subCommands, index);
				break;

			case addTypeHint :
				addTypeHint(tool, subCommands, index);
				break;

			case removeTypeHint :
				removeTypeHint(tool, subCommands, index);
				break;

			case setDefaults :
				setDefaults(tool, subCommands, index);
				break;
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

		Type type = EditorUtil.findType(tool.getTypes(), typeName);
		if (type == null) {
			return;
		}

		for (Hint hint : type.getHints()) {
			if (hint.getName().equals(hintName)) {
				type.getHints().remove(hint);
				skillFile.delete(hint);
				break;
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

		Type toolType = EditorUtil.findType(tool.getTypes(), typeName);
		if (toolType == null) {
			throw new Error("Type not in Tool");
		}

		Type skType = toolType.getOrig();

		if (skType == null) {
			return;
		}

		Hint hint = null;
		for (Hint h : skType.getHints()) {
			if (h.getName().equals(hintName)) {
				hint = h;
				break;
			}
		}

		if (hint == null) {
			throw new Error("Hint not found");
		}

		if (toolType.getHints().stream().noneMatch(h -> h.getName().equals(hintName))) {
			toolType.getHints().add(skillFile.Hints().make(hint.getName(), toolType));
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

		Type type = EditorUtil.findType(tool.getTypes(), typeName);
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
		for (Hint h : field.getHints()) {
			if (h.getName().equals(hintName)) {
				hint = h;
				break;
			}
		}
		// exit if arguemnt error: hint not found
		if (hint == null) {
			throw new Error("Hint not found");
		}
		field.getHints().remove(hint);
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

		Type type = EditorUtil.findType(tool.getTypes(), typeName);
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
			throw new Error(String.format("Field not found (%s)", fieldName));
		}

		for (Hint hint : field.getOrig().getHints()) {
			if (hint.getName().toLowerCase().equals(hintName.toLowerCase())) {
				field.getHints().add(skillFile.Hints().make(hintName, field));
			}
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

		Type type = EditorUtil.findType(tool.getTypes(), typeName);
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

		Type type = EditorUtil.findType(tool.getTypes(), typeName);
		if (type == null) {
			return;
		}

		for (Field f : type.getFields()) {
			String[] splits = f.getName().split(" ");
			if (splits[splits.length - 1].equals(fieldName)) {
				return;
			}
		}

		for (Field f : type.getOrig().getFields()) {
			String[] splits = f.getName().split(" ");
			if (splits[splits.length - 1].equals(fieldName)) {
				type.getFields()
						.add(skillFile.Fields().make("", f.getName(), f, new ArrayList<>(), type, new ArrayList<>()));
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
		Type type = EditorUtil.findType(tool.getTypes(), typeName);
		if (type == null) {
			return;
		}

		tool.getTypes().remove(type);

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
		removeTypeContent(type, skillFile);
		for (Type t : tool.getTypes()) {
			if (t.getFile().equals(file)) {
				tool.getFiles().add(file);
				break;
			}
		}
	}

	/**
	 * Removes the fields and hints from a type
	 *
	 * @param type
	 *            type that is deleted
	 * @param skillFile
	 *            skillfile containing the objects
	 */
	private void removeTypeContent(Type type, SkillFile skillFile) {
		if (type == null) {
			return;
		}
		for (Hint hint : type.getHints()) {
			skillFile.delete(hint);
		}
		for (Field f : type.getFields()) {
			skillFile.delete(f);
			removeFieldContent(f);
		}
	}

	/**
	 * Removes the hints from a field
	 *
	 * @param field
	 *            field that is deleted
	 */
	private void removeFieldContent(Field field) {
		for (Hint h : field.getHints()) {
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

		for (Type origType : skillFile.Types().stream().filter(ty -> ty.getOrig() == null)
				.collect(Collectors.toList())) {
			// check if type or enum or interface or typed with the name exists
			if (typeName.equals(origType.getName())
					|| origType.getName().toLowerCase().equals("enum " + typeName.toLowerCase())
					|| origType.getName().toLowerCase().equals("interface " + typeName.toLowerCase())
					|| origType.getName().toLowerCase().startsWith("typedef " + typeName.toLowerCase() + " ")) {
				Type type = null;
				// type is not in tool
				if (tool.getTypes().stream().noneMatch(ty -> ty != null && ty.getName().equals(typeName))) {
					// noinspection unchecked
					type = skillFile.Types().make(origType.getComment(),
							(ArrayList<String>) origType.getExtends().clone(), new ArrayList<>(), origType.getFile(),
							origType.getName(), origType, new ArrayList<>(), new ArrayList<>());
				}
				// type is enum
				if (origType.getFields().stream().anyMatch(f -> f.getName().matches("\\S+")) && type != null) {
					Field field = origType.getFields().stream().filter(f -> f.getName().matches("\\S+")).findFirst()
							.get();
					type.getFields().add(skillFile.Fields().make(field.getComment(), field.getName(), field,
							new ArrayList<>(), type, new ArrayList<>()));
				}
				if (type == null || origType.getFile() == null) {
					return;
				}
				tool.getTypes().add(type);
				if (type.getName().toLowerCase().startsWith("typedef ")) {
					EditorUtil.addGroundType(type, tool, skillFile);
				}
				EditorUtil.addExtensions(tool, origType, skillFile);
				for (de.unistuttgart.iste.ps.skillls.tools.File f : skillFile.Files()) {
					if (f.getPath().equals(origType.getFile().getPath())) {
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
		tool.getTypes().forEach(t -> removeTypeContent(t, skillFile));
	}

	/**
	 * Creates a new tool.
	 *
	 * @param subCommand
	 *            the name of the tool.
	 * @return Returns the tool.
	 */
	private Tool newTool(String subCommand) {
		for (Tool t : skillFile.Tools()) {
			if (t.getName().equals(subCommand)) {
				return t;
			}
		}
		return skillFile.Tools().make(new ArrayList<>(), skillFile.Generators().make("", ""), "", "", subCommand, "",
				new ArrayList<>());
	}
}
