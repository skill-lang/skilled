package de.unistuttgart.iste.ps.skillls.main;

import java.util.ArrayList;
import java.util.stream.Collectors;

import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.File;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile;

/**
 * Created on 25.01.16.
 *
 * Class for adding the dependencies of a type to a tool
 *
 * @author Armin Hüneburg
 */
public class DependencyBuilder implements Runnable {
	private final Tool tool;
	private final SkillFile skillFile;

	/**
	 * Constructor, setting object fields.
	 * 
	 * @param tool
	 *            tool containing types, that may have dependencies that are not
	 *            met
	 * @param skillFile
	 *            skillfile containing the tool and types.
	 */
	public DependencyBuilder(Tool tool, SkillFile skillFile) {
		this.tool = tool;
		this.skillFile = skillFile;
	}

	/**
	 * Method for searching and adding missing dependencies.
	 */
	@Override
	public void run() {
		tool.getFiles().forEach(this::analyzeFile);
	}

	/**
	 * Looks for missing dependency for a file
	 * 
	 * @param file
	 *            file that should be checked, if dependencies are missing
	 */
	private void analyzeFile(File file) {
		ArrayList<Type> types = tool.getTypes().stream()
				.filter(type -> type != null && type.getFile().getPath().equals(file.getPath()))
				.collect(Collectors.toCollection(ArrayList::new));
		types.forEach(this::analyzeType);
	}

	/**
	 * Looks for missing dependencies for a type
	 * 
	 * @param type
	 *            type that should be checked, if dependencies are missing
	 */
	private void analyzeType(Type type) {
		File file = type.getFile();
		ArrayList<String> deps = new ArrayList<>();
		for (String extension : type.getExtends()) {
			tool.getTypes().stream().filter(t -> t.getName().equals(extension))
					.filter(t -> !t.getFile().getPath().equals(type.getFile().getPath())
							&& !deps.contains(t.getFile().getPath()))
					.forEach(t -> deps.add(t.getFile().getPath()));
		}
		for (Field field : type.getFields()) {
			analyzeField(deps, field);
		}
		deps.stream().filter(dep -> !file.getDependencies().contains(dep))
				.forEach(dep -> file.getDependencies().add(dep));
	}

	/**
	 * Looks for missing dependencies for a field
	 * 
	 * @param deps
	 *            the dependencies that are needed
	 * @param field
	 *            the field that should be checked
	 */
	private void analyzeField(ArrayList<String> deps, Field field) {
		File file = field.getType().getFile();
		for (Type t : skillFile.Types()) {
			String name = field.getName();
			if (name.startsWith("auto")) {
				name = name.substring(5);
			}
			int index = name.indexOf(' ');
			if (index == -1) {
				index = name.length();
			}
			if (t.getName().startsWith(name.substring(0, index))) {
				if (!t.getFile().getPath().equals(file.getPath()) && !deps.contains(t.getFile().getPath())) {
					deps.add(t.getFile().getPath());
				}
			}
		}
	}
}
