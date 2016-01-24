package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype

/**
 * @author Daniel Ryan Degutis
 */
class BooleanTypeFieldRestrictionsValidator extends FieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Booleantype
	}

}