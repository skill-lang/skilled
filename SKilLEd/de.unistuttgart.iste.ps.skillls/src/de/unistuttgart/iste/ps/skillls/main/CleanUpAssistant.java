package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.tools.*;
import de.unistuttgart.iste.ps.skillls.tools.Generator;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;
import de.ust.skill.common.java.api.SkillFile.Mode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 18.02.16.
 *
 * @author Armin Hüneburg
 */
public class CleanUpAssistant {
	private SkillFile temporary;
	private final SkillFile skillFile;
	private final HashSet<Tool> brokenTools = new HashSet<>();

	/**
	 * Constructor. Sets the old skill file and creates a temporary one.
	 * 
	 * @param skillFile
	 *            the old skill file, that should be substituted
	 */
	public CleanUpAssistant(SkillFile skillFile) {
		this.skillFile = skillFile;
		SkillFile temp = null;
		try {
			temp = SkillFile.open(Files.createTempFile("temp", "sf"), Mode.Read, Mode.Write);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			temporary = temp;
		}
	}

	/**
	 *
	 * @return returns the temporary skill file
	 */
	public SkillFile getTemporary() {
		return temporary;
	}

	/**
	 * merges the original skill file and the temporary one.
	 */
	public void merge() {
		for (Tool tool : skillFile.Tools()) {
			transfer(tool);
		}
	}

	/**
	 * transfers a tool from the old skill file to the new one
	 * 
	 * @param tool
	 *            the tool that should be transferred.
	 */
	private void transfer(Tool tool) {
		Tool newTool = null;
		for (Tool t : temporary.Tools()) {
			if (t.getName().toLowerCase().equals(tool.getName().toLowerCase())) {
				newTool = t;
				break;
			}
		}
		if (newTool == null) {
			Generator generator;
			if (tool.getGenerator() == null) {
				generator = temporary.Generators().make("", "");
			} else {
				generator = temporary.Generators().make(tool.getGenerator().getExecEnv(),
						tool.getGenerator().getPath());
			}
			newTool = temporary.Tools().make(null, generator, tool.getLanguage(), tool.getModule(), tool.getName(),
					tool.getOutPath(), new ArrayList<>());
		}
		for (Type type : tool.getTypes()) {
			transferType(type, newTool);
		}
		List<String> containedTypes = newTool.getTypes().stream().map(Type::getName).collect(Collectors.toList());
		HashSet<String> containedExtends = new HashSet<>();
		for (Type type : newTool.getTypes()) {
			containedExtends.addAll(type.getExtends());
		}
		containedExtends.removeAll(containedTypes);
		List<Type> missing = containedExtends.stream().map(e -> findType(e)).collect(Collectors.toList());
		for (Type type : missing) {
			brokenTools.add(newTool);
			transferType(type, newTool);
		}

		HashSet<File> files = new HashSet<>();
		for (Type type : newTool.getTypes()) {
			files.add(type.getFile());
		}
		newTool.setFiles(new ArrayList<>(files));
	}

	/**
	 * transfers a type from the old skill file to the new one
	 * 
	 * @param type
	 *            type that should be transferred
	 * @param newTool
	 *            tool (owner is {@link CleanUpAssistant#getTemporary()}) the
	 *            new type should be added to.
	 */
	private void transferType(Type type, Tool newTool) {
		Type origType = findType(type.getName());
		if (origType == null) {
			brokenTools.add(newTool);
			return;
		}
		List<Hint> typeHints = type.getHints().stream()
				.filter(h -> origType.getHints().stream()
						.anyMatch(h1 -> h1.getName().toLowerCase().equals(h.getName().toLowerCase())))
				.collect(Collectors.toList());
		if (typeHints.size() != type.getHints().size()) {
			brokenTools.add(newTool);
		}
		List<String> typeRes = type.getRestrictions().stream().filter(
				r -> origType.getRestrictions().stream().anyMatch(r1 -> r.toLowerCase().equals(r1.toLowerCase())))
				.collect(Collectors.toList());
		if (typeRes.size() != type.getRestrictions().size()) {
			brokenTools.add(newTool);
		}

		File file = findFile(origType);

		ArrayList<Hint> newTypeHints = new ArrayList<>();
		for (Hint typeHint : typeHints) {
			newTypeHints.add(temporary.Hints().make(typeHint.getName(), null));
		}

		ArrayList<Field> fields = copyFields(origType, type, newTool);
		Type newType = temporary.Types().make(origType.getComment(), origType.getExtends(), fields, file,
				origType.getName(), origType, new ArrayList<>(typeRes), newTypeHints);
		for (Hint newTypeHint : newTypeHints) {
			newTypeHint.setParent(newType);
		}
		fields.parallelStream().forEach(f -> f.setType(newType));
		newTool.getTypes().add(newType);
	}

	/**
	 * tries to find a file in the {@link CleanUpAssistant#getTemporary()}
	 * 
	 * @param type
	 *            Type the file should contain
	 * @return returns the new
	 *         {@link de.unistuttgart.iste.ps.skillls.tools.File} object
	 */
	private File findFile(Type type) {
		for (File file : temporary.Files()) {
			if (file.getPath().equals(type.getFile().getPath())) {
				return file;
			}
		}
		return temporary.Files().make(type.getFile().getDependencies(), type.getFile().getHeader(),
				type.getFile().getMd5(), type.getFile().getPath(), type.getFile().getTimestamp());
	}

	/**
	 * copies the fields of a type to a new {@link Type} object. This is in
	 * respect to a Tool.
	 * 
	 * @param origType
	 *            the original type, that should be tranferred
	 * @param type
	 *            the old tool type from the old skillfile
	 * @param newTool
	 *            the new tool, belonging to
	 *            {@link CleanUpAssistant#getTemporary()}
	 * @return returns a list of fields, that are in the new tool.
	 */
	private ArrayList<Field> copyFields(Type origType, Type type, Tool newTool) {
		ArrayList<Field> fields = new ArrayList<>();

		for (Field field : type.getFields()) {
			Field origField = findField(origType, field);
			if (origField == null) {
				brokenTools.add(newTool);
				continue;
			}
			List<String> fieldRes = field.getRestrictions().stream().filter(
					r -> origField.getRestrictions().stream().anyMatch(r1 -> r.toLowerCase().equals(r1.toLowerCase())))
					.collect(Collectors.toList());
			List<Hint> fieldHints = field.getHints().stream()
					.filter(h -> origField.getHints().stream()
							.anyMatch(h1 -> h.getName().toLowerCase().equals(h1.getName().toLowerCase())))
					.collect(Collectors.toList());

			ArrayList<Hint> newFieldHints = new ArrayList<>();
			for (Hint fieldHint : fieldHints) {
				newFieldHints.add(temporary.Hints().make(fieldHint.getName(), null));
			}

			Field newField = temporary.Fields().make(origField.getComment(), origField.getName(), origField,
					new ArrayList<>(fieldRes), null, newFieldHints);
			fields.add(newField);

			for (Hint newFieldHint : newFieldHints) {
				newFieldHint.setParent(newField);
			}
		}

		return fields;
	}

	/**
	 * tries to find a field equivalent in the origType
	 * 
	 * @param origType
	 *            the type that should contain the field
	 * @param field
	 *            the field whose equivalent should be found
	 * @return the field, if it was found, else null
	 */
	private Field findField(Type origType, Field field) {
		for (Field field1 : origType.getFields()) {
			if (field1.getOrig() == null && normalize(field1.getName()).equals(normalize(field.getName()))) {
				return field1;
			}
		}
		return null;
	}

	/**
	 * Tries to find a type in the {@link #getTemporary()}
	 * 
	 * @param name
	 *            the name of the type
	 * @return returns the type, if found, else null
	 */
	private Type findType(String name) {
		for (Type type1 : temporary.Types()) {
			if (type1.getOrig() == null && normalize(type1.getName()).equals(normalize(name))) {
				return type1;
			}
		}
		return null;
	}

	/**
	 * normalizes a name in the skill context single _ in the middle of a word
	 * and multiple _ in the end are ignored capitalization is ignored
	 * 
	 * @param name
	 *            the name, that should be normalized
	 * @return the normalized name
	 */
	private static String normalize(String name) {
		String converted = name.toLowerCase();
		int index = 0;
		while (converted.charAt(index) == '_') {
			index++;
			if (index == converted.length())
				return converted;
		}
		boolean wasUnderscore = true;
		while (index < converted.length()) {
			if (converted.charAt(index) != '_') {
				index++;
				wasUnderscore = false;
				continue;
			}
			if (!wasUnderscore) {
				wasUnderscore = true;
				converted = converted.substring(0, index) + converted.substring(index + 1);
				continue;
			}
			index++;
		}
		if (!converted.matches("_*")) {
			while (converted.charAt(converted.length() - 1) == '_')
				converted = converted.substring(0, converted.length() - 1);
		}
		return converted;
	}

	/**
	 * sets the current skillfile to the temporary one.
	 */
	public void cleanUp() {
		Path tempPath = temporary.currentPath();
		Path oldPath = skillFile.currentPath();
		try {
			temporary.changePath(oldPath);
			temporary.close();
			temporary = SkillFile.open(oldPath, Mode.Read, Mode.Write);
			Files.deleteIfExists(tempPath);
			MainClass.setNewSkillFile(temporary);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * analyzes, whether at least one tool was broken, or not.
	 * 
	 * @throws BreakageException
	 *             thrown, when at least one tool is broken.
	 */
	public void analyseBreakage() {
		if (brokenTools.isEmpty()) {
			return;
		}
		throw new BreakageException(brokenTools);
	}
}
