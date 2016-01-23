package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Basetype
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Listtype
import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Settype


/**
 * @author Marco Link
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 */
class NestingValidator extends AbstractSKilLValidator{
	
	public static val INVALID_NESTED_TYPEDEF = 'invalidNestedTypedef'

	@Check
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
		for (b : maptype.basetypes) {
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