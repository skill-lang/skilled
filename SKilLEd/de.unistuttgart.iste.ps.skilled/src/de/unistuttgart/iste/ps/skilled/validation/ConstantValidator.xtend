package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Constant
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import org.eclipse.xtext.validation.Check

/**
 * @author Marco Link
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 */
class ConstantValidator extends AbstractSKilLValidator {
		public static val INVALID_CONSTANT_TYPE = 'invalidConstantType'
		
	@Check
	def checkConstantHasAnInteger(Constant constant) {
		if (!(constant.fieldtype instanceof Integertype)) {
			error('Only an Integer can be constant.',
				constant, SKilLPackage.Literals.CONSTANT__CONSTANT_NAME, INVALID_CONSTANT_TYPE)
		}
	}
	
}