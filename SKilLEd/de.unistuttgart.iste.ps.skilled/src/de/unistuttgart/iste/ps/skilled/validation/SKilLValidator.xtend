/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype
import de.unistuttgart.iste.ps.skilled.sKilL.Basetype
import de.unistuttgart.iste.ps.skilled.sKilL.Constant
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Listtype
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Settype
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.ComposedChecks

/**
 * This class contains custom validation rules. 
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 * 
 * @author Marco Link
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 * 
 */
@ComposedChecks(validators =#[InheritenceValidator, FieldRestrictionsValidator, DuplicatedTypenameValidation, 
KeywordWarning, ASCIICharValidator, ViewValidator, ScopingValidator, TypeRestrictionsValidator, ImportValidator])
class SKilLValidator extends AbstractSKilLValidator {

	public static val INVALID_CONSTANT_TYPE = 'invalidConstantType'
	public static val INVALID_NESTED_TYPEDEF = 'invalidNestedTypedef'

	@Check
	def checkConstantHasAnInteger(Constant constant) {
		if (!(constant.fieldtype instanceof Integertype)) {
			error('Only an Integer can be constant.',
				constant, SKilLPackage.Literals.CONSTANT__CONSTANT_NAME, INVALID_CONSTANT_TYPE)
		}
	}

	def checkInvalidNestedTypes(DeclarationReference dr) {
		if(dr.type instanceof Typedef) {
			val td = dr.type as Typedef
			if (!(td.fieldtype instanceof Basetype)) {
					error('It is forbidden to nest containers inside of other containers.',
						dr, SKilLPackage.Literals.DECLARATION_REFERENCE__TYPE, INVALID_NESTED_TYPEDEF)
			}
		}
	}
	
	@Check 
	def checkInvalidNestedTypedef(Listtype listtype) {
		if (listtype.basetype instanceof DeclarationReference) {
			checkInvalidNestedTypes(listtype.basetype as DeclarationReference)
		}
	}
	
	@Check
	def checkInvalidNestedTypedef(Arraytype arraytype) {
		if (arraytype.basetype instanceof DeclarationReference) {
			checkInvalidNestedTypes(arraytype.basetype as DeclarationReference)
		}
	}
	
	@Check
	def checkInvalidNestedTypedef(Maptype maptype) {
		for (Basetype b : maptype.basetypes) {
			if (b instanceof DeclarationReference) {
				checkInvalidNestedTypes(b)
			}
		}
	}
	
	@Check
	def checkInvalidNestedTypedef(Settype settype) {
		if (settype.basetype instanceof DeclarationReference) {
			checkInvalidNestedTypes(settype.basetype as DeclarationReference)
		}
	}
}
