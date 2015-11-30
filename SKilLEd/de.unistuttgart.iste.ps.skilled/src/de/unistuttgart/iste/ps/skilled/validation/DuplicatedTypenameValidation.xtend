package de.unistuttgart.iste.ps.skilled.validation

import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import java.util.List
import java.util.ArrayList
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import org.eclipse.xtext.validation.EValidatorRegistrar
import de.unistuttgart.iste.ps.skilled.sKilL.View

/**
 * @author Jan Berberich
 * 
 * This Validator checks if there is a typename that also appeared in the supertypes of a type
 * and then gives an error.
 * 
 */
class DuplicatedTypenameValidation extends AbstractDeclarativeValidator {
	private var List<String> fieldNames // List with all FieldNames in validate
	private var List<TypeDeclaration> declarations // declarations of Types where fieldNames were already looked at
	private TypeDeclaration validate // The declaration that is checked
	public static var FIELDNAME_ALREADY_EXISTS = "alreadyExists"

	override register(EValidatorRegistrar registar) {}

	@Check
	def searchDuplicatedDeclaration(TypeDeclaration dec) {
		validate = dec
		fieldNames = new ArrayList<String>
		declarations = new ArrayList<TypeDeclaration>
		for (Field f : dec.fields) {
			fieldNames.add(f.fieldcontent.name)
		}
		declarations.add(dec)
		for(TypeDeclarationReference d : dec.supertypes){
			searchSupertypes(d.type)
		}
	}

	/**
	 * Search dec and gives an error if there is a fieldname in a that is also fieldname in validate, 
	 * calls searchSupertypes for all supertypes of dec that are not in declarations
	 * @param dec The TypeDeclaration
	 */
	def void searchSupertypes(TypeDeclaration dec) {
		if (!declarations.contains(dec)) {
			declarations.add(dec)
			for (Field f : dec.fields) {
				if (fieldNames.contains(f.fieldcontent.name)) {
					error(f.fieldcontent.name)
				}
			}
			for (TypeDeclarationReference d : dec.supertypes) {
				searchSupertypes(d.type)
			}
		}
	}

	/**
	 * Gives an Error for the duplicated field if it is not a view
	 * @param name The name of the field
	 */
	def void error(String name) {
		for (Field f : validate.fields) {
			if (f.fieldcontent.name.equals(name)) {
				if(!(f.fieldcontent instanceof View)){
					error("Error: Fieldname already exists in a supertype!", f.fieldcontent,
					SKilLPackage.Literals.FIELDCONTENT__NAME, FIELDNAME_ALREADY_EXISTS, f.fieldcontent.name)
				}
			}
		}
	}
}