package de.unistuttgart.iste.ps.skillls.editor;

import de.unistuttgart.iste.ps.skillls.main.Command;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility Class for the Editor
 *
 * Created on 29.02.16.
 *
 * @author Armin Hüneburg
 */
public class EditorUtil {
	/**
	 * Adds the groundtype of typedefs to a tool.
	 *
	 * @param type
	 *            typedef which has a groundtype.
	 * @param tool
	 *            tool the new types are added to.
	 * @param skillFile
	 *            skillfile containing all objects.
	 */
	public static void addGroundType(Type type, Tool tool, SkillFile skillFile) {
		String groundTypeName = type.getName().split(" ")[type.getName().split(" ").length - 1].toLowerCase();
		List<Type> candidates = skillFile.Types().stream()
				.filter(t -> t.getName().toLowerCase().contains(groundTypeName)).collect(Collectors.toList());
		for (Type candidate : candidates) {
			String candidateName = candidate.getName().toLowerCase();
			if (candidateName.startsWith("typedef " + groundTypeName)
					|| candidateName.startsWith("enum " + groundTypeName) || candidateName.equals(groundTypeName)
					|| candidateName.startsWith("interface " + groundTypeName)) {
				if (tool.getTypes().stream().filter(t -> t.getName().equals(candidate.getName())).count() > 0) {
					break;
				}
				// noinspection unchecked
				tool.getTypes().add(skillFile.Types().make(candidate.getComment(),
						(ArrayList<String>) candidate.getExtends().clone(), new ArrayList<>(), candidate.getFile(),
						candidate.getName(), candidate, candidate.getRestrictions(), new ArrayList<>()));
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
	 * @param skillFile
	 *            The skillfile containing all objects.
	 */
	public static void addExtensions(Tool tool, Type type, SkillFile skillFile) {
		Type toolType = null;
		for (Type t : tool.getTypes()) {
			if (t != null && t.getName().equals(type.getName())) {
				toolType = t;
				break;
			}
		}
		List<String> types = tool.getTypes().stream().filter(t -> t != null).map(Type::getName)
				.collect(Collectors.toList());
		for (int j = 0; j < type.getExtends().size(); j++) {
			String extension = type.getExtends().get(j);
			if (types.contains(extension)) {
				continue;
			}
			List<Type> exts = skillFile.Types().stream()
					.filter(t -> t.getName().equals(extension) && !types.contains(t.getName()))
					.collect(Collectors.toList());
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
				// noinspection unchecked
				tool.getTypes().add(skillFile.Types().make(t.getComment(), (ArrayList<String>) t.getExtends().clone(),
						new ArrayList<>(), t.getFile(), t.getName(), t, t.getRestrictions(), new ArrayList<>()));
				if (t.getExtends().size() > 0) {
					addExtensions(tool, t, skillFile);
				}
				if (toolType == null)
					return;
				toolType.getExtends().add(t.getName());
			}
		}
	}

	/**
	 * Searches for a tool.
	 *
	 * @param subCommand
	 *            the name of the tool or &amp;n for creating a new tool.
	 * @param skillFile
	 *            the skillfile containing all objects
	 * @return returns the tool or null if the tool was not found or a new tool
	 *         should be created.
	 */
	public static Tool selectTool(String subCommand, SkillFile skillFile) {
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

	/**
	 *
	 * @param subCommand
	 *            The command that has to be matched.
	 * @return returns the corresponding enum value.
	 */
	public static Command getCommand(String subCommand) {
		return Command.values()[Integer.parseInt(subCommand)];
	}

	/**
	 * Finds a type in a tool
	 *
	 * @param types
	 *            Collection containing all types to be searched in
	 * @param typeName
	 *            name of the type to be found
	 * @return returns the type object or null if not found
	 */
	public static Type findType(Collection<Type> types, String typeName) {
		for (Type t : types) {
			if (t.getName().equals(typeName)) {
				return t;
			}
		}
		return null;
	}
}
