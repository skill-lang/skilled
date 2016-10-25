package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.skill.View
import java.util.HashSet
import java.util.List
import java.util.Set
import org.eclipse.xtext.validation.Check

/**
 * @author Timm Felden
 * 
 * This Validator checks if there is a typename that also appeared in the supertypes of a type
 * and then gives an error.
 * 
 */
class DuplicatedFieldDefinitionValidation extends AbstractSKilLComposedValidatorPart {

	private TypeDeclaration validate
	// @note: we cannot assume, that the types have no cycles at this point
	private Set<TypeDeclaration> seen = new HashSet

	public static val FIELDNAME_ALREADY_EXISTS = "alreadyExists"

	@Check
	def searchDuplicatedDeclaration(TypeDeclaration declaration) {
		// reset state
		validate = declaration
		seen.clear

		// search for field names in super declarations
		val names = declaration.fields.filter[x|!(x.fieldcontent instanceof View)].map[x|x.fieldcontent.name].toList

		for (declarationReference : declaration.supertypes) {
			searchInSupertypes(declarationReference.type, names)
		}
	}

	/**
	 * Searches supertypes of declaration that are not in declarations and contain a field with
	 * @param declaration The TypeDeclaration
	 */
	private def void searchInSupertypes(TypeDeclaration declaration, List<String> names) {
		if (!seen.contains(declaration)) {
			seen.add(declaration)

			for (field : declaration.fields) {
				if (names.contains(field.fieldcontent.name)) {
					error(declaration, field.fieldcontent.name)
				}
			}
			for (declarationReference : declaration.supertypes) {
				searchInSupertypes(declarationReference.type, names)
			}
		}
	}

	/**
	 * Gives an Error for the duplicated field if it is not a view
	 * @param name The name of the field
	 */
	private def void error(TypeDeclaration target, String name) {
		val field = validate.fields.findFirst[x|x.fieldcontent.name.equals(name)]

		error('''Field «validate.name».«name» already defined in super type «target.name».''', field.fieldcontent,
			SkillPackage.Literals.FIELDCONTENT__NAME, FIELDNAME_ALREADY_EXISTS, field.fieldcontent.name)
	}

}
