package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.tools.Tool
import de.unistuttgart.iste.ps.skilled.tools.Type
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.EValidatorRegistrar
import de.unistuttgart.iste.ps.skilled.tools.api.SkillFile
import org.eclipse.core.resources.ResourcesPlugin
import java.io.File
import org.eclipse.core.runtime.Path
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Field

class UnusedDeclarationValidator extends AbstractSKilLValidator {

	override register(EValidatorRegistrar registar){
		
	}	

	/**
	 * Tests if a type is being used in a tool and gives a warning if not.
	 * @param declaration: The type declaration that should be tested if it is used in a tool.
	 */
	@Check
	def checkType(Declaration declaration) {
		val platformString = declaration.eResource.URI.toPlatformString(true);
		val myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString))
		val project = myFile.project
		val file = project.getFile(File.separator + ".skills")
		var SkillFile skillFile = null
		try {
			SkillFileOpener.path = file.rawLocation.toString()
		} catch (Throwable e) {
			e.printStackTrace
		}
		skillFile = SkillFileOpener.file
		var name = declaration.name
		var found = false
		for (Tool tool : skillFile.Tools) {
			for (Type type : tool.types) {
				if(type.name.startsWith(name)) {
					found = true
				}
			}
		}
		if (!found) {
			warning("Type is not used in Tool", declaration, SKilLPackage.Literals.DECLARATION__NAME)
		}
	}
	
	/**
	 * Tests if a field is being used in a tool and gives a warning if not.
	 * @param field: The declared field that should be tested if it is used in a tool.
	 */
	@Check
	def checkField(Field field) {
		val platformString = field.eResource.URI.toPlatformString(true);
		val myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString))
		val project = myFile.project
		val file = project.getFile(File.separator + ".skills")
		var SkillFile skillFile = null
		try {
			SkillFileOpener.path = file.rawLocation.toString()
		} catch (Throwable e) {
			e.printStackTrace
		}
		skillFile = SkillFileOpener.file
		val content = field.fieldcontent
		val name = content.name
		var found = false
		for (Tool tool : skillFile.Tools) {
			for (Type type : tool.types) {
				for (de.unistuttgart.iste.ps.skilled.tools.Field f : type.fields) {
					if(f.name.toLowerCase.substring(f.name.lastIndexOf(' ')).equals(name)) {
					    found = true
				    }
				}
			}
		}
		if (!found) {
			warning("Field is not used in Tool", field.fieldcontent, SKilLPackage.Literals.FIELDCONTENT__NAME)
		}
	}

}