package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import org.eclipse.xtext.validation.Check

/**
 * Contains validation rules to check if reserved key words in SKilL are used as an identifier
 * @author Armin Hühneburg
 */
class ReservedSKilLKeywordValidator extends AbstractSKilLValidator {

	/**
	 * checks if the type name is a reserved key word in SKilL.
	 * @param declaration: the type to be checked.
	 */
	@Check
	def checkTypeNameReservedSkillKeyword(Declaration declaration) {
		var name = declaration.name.toLowerCase;
		if (name.equals("skillid") || name.equals("api") || name.equals("internal")) {
			warning("Usage of skillid, api or internal is discouraged", declaration,
				SKilLPackage.Literals.DECLARATION__NAME)
		}
	}

	/**
	 * checks if the field name is a reserved key word in SKilL.
	 * @param field: the field to be checked.
	 */
	@Check
	def checkFieldNameReservedSkillKeyword(Field field) {
		var name = field.fieldcontent.name.toLowerCase
		if (name.equals("skillid") || name.equals("api") || name.equals("internal")) {
			warning("Usage of skillid, api or internal is discouraged", field.fieldcontent,
				SKilLPackage.Literals.FIELDCONTENT__NAME)
		}
	}

}