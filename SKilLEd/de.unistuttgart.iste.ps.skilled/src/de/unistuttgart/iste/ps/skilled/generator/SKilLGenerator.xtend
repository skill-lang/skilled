/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.generator

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import de.unistuttgart.iste.ps.skilled.util.SkillInstallation
import org.eclipse.core.resources.ProjectScope
import org.eclipse.core.runtime.preferences.IEclipsePreferences
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class SKilLGenerator implements IGenerator {
	@Inject SKilLServices ss;

	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		val String project = ss.getProject(resource).location.toFile.absolutePath;
		val ProjectScope scope = new ProjectScope(ss.getProject(resource))
		val IEclipsePreferences prefs = scope.getNode("de.unistuttgart.iste.ps.skilled.skill")
		var String language = prefs.get("languageselection", "")
		language = Character.toUpperCase(language.charAt(0)) + language.substring(1)
		var String all = ""
		if (prefs.get("generateoption", null).startsWith("Generate all")) {
			all = "--all"
		}
		val String genPath = prefs.get("generatorpath", "")
		val String exec = prefs.get("executionenvironment", "").toLowerCase
		val String output = prefs.get("targetprojectpath", "")
		val String module = prefs.get("modulename", "")
		var String[] args
		if (all.empty) {
			args = #["--path", project, "--lang", language, "--generator", genPath, "--exec", exec, "--output", output,
				"--module", module];
		} else {
			args = #[all, "--path", project, "--lang", language, "--generator", genPath, "--exec", exec, "--output",
				output, "--module", module];
		}
		SkillInstallation.run(args)
	}
}
