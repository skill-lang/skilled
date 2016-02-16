package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.View
import java.util.ArrayList
import java.util.List
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.EValidatorRegistrar

/**
 * @author Jan Berberich
 * 
 * This Validator checks if there is a typename that also appeared in the supertypes of a type
 * and then gives an error.
 * 
 */
class DuplicatedTypenameValidation extends AbstractDeclarativeValidator {
	
	private val List<String> fieldNames = new ArrayList
	private val List<TypeDeclaration> declarations = new ArrayList
	
	private TypeDeclaration validate
	
	public static val FIELDNAME_ALREADY_EXISTS = "alreadyExists"

	override register(EValidatorRegistrar registar) {}

	@Check
	def searchDuplicatedDeclaration(TypeDeclaration declaration) {
		validate = declaration
		fieldNames.clear
		declarations.clear
		for (field : declaration.fields) {
			fieldNames.add(field.fieldcontent.name)
		}
		declarations.add(declaration)
		for (declarationReference : declaration.supertypes) {
			searchSupertypes(declarationReference.type)
		}
	}

	/**
	 * Searches supertypes of declaration that are not in declarations and contain a field with
	 * @param declaration The TypeDeclaration
	 */
	def void searchSupertypes(TypeDeclaration declaration) {
		if (!declarations.contains(declaration)) {
			declarations.add(declaration)
			for (field : declaration.fields) {
				if (fieldNames.contains(field.fieldcontent.name)) {
					error(field.fieldcontent.name)
				}
			}
			for (declarationReference : declaration.supertypes) {
				searchSupertypes(declarationReference.type)
			}
		}
	}

	/**
	 * Gives an Error for the duplicated field if it is not a view
	 * @param name The name of the field
	 */
	def void error(String name) {
		for (field : validate.fields) {
			if (field.fieldcontent.name.equals(name) &&  !(field.fieldcontent instanceof View)) {
				error("Error: Fieldname already exists in a supertype!", field.fieldcontent,
					SKilLPackage.Literals.FIELDCONTENT__NAME, FIELDNAME_ALREADY_EXISTS, field.fieldcontent.name)
			}
		}
	}

}