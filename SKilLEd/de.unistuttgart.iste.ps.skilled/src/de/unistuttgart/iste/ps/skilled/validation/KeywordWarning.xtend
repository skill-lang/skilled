package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.skill.Field
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.tools.SIRCache
import de.unistuttgart.iste.ps.skilled.tools.ToolInfo
import de.unistuttgart.iste.ps.skilled.util.KeywordCheckEscaping
import org.eclipse.xtext.validation.Check

/**
 * This Class checks if a name is a keyword that requires escaping in the target 
 * language. If so, a warning is displayed.
 * 
 * @author Jan Berberich, Timm Felden	
 */
class KeywordWarning extends AbstractSKilLComposedValidatorPart {

	/**
	 * Checks a typename for keywords.
	 * @dec The TypeDeclaration to check.
	 */
	@Check
	def KeywordWarning(TypeDeclaration dec) {
		val languages = ToolInfo.usedLanguages(SIRCache.getProject(dec));
		if (!languages.isEmpty) {
			val requiresEscaping = KeywordCheckEscaping.requiresEscaping(dec.name).filter([ String s |
				languages.contains(s)
			]);
			if (!requiresEscaping.isNullOrEmpty) {
				warning(errorMessage(requiresEscaping), dec, SkillPackage.Literals.DECLARATION__NAME)
			}
		}
	}

	/**
	 * Checks a fieldname for keywords.
	 * @param field The Field to check.
	 */
	@Check
	def void fieldCheck(Field field) {
		val languages = ToolInfo.usedLanguages(SIRCache.getProject(field));
		if (!languages.isEmpty) {
			val requiresEscaping = KeywordCheckEscaping.requiresEscaping(field.fieldcontent.name).filter([ String s |
				languages.contains(s)
			]);
			if (!requiresEscaping.isNullOrEmpty) {
				warning(errorMessage(requiresEscaping), field.fieldcontent, SkillPackage.Literals.FIELDCONTENT__NAME)
			}
		}
	}

	/**
	 * This method produces the error message.
	 * @param requiresEscaping The languages where escaping is required for the Keyword
	 * @return The error message
	 * 
	 */
	private def String errorMessage(Iterable<String> requiresEscaping) {
		var String warning = "Name requires escaping in "
		var boolean needComma = false
		var int i = 1;
		for (String language : requiresEscaping) {
			if (needComma) {
				if (i == requiresEscaping.size) {
					warning = warning + " and " + language
				} else {
					warning = warning + ", " + language
				}
			} else {
				warning = warning + language
				needComma = true
			}
			i++
		}
		return warning + "."
	}

}
