package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 */
class BooleanTypeFieldRestrictionsValidator extends AbstractFieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Booleantype
	}
	
	override void handleDefaultRestriction (Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		if (restriction.restrictionArguments.size != 1) {
			showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
			return
		}
		if (restriction.restrictionArguments.get(0).valueBoolean == null) {
			showError(FieldRestrictionErrorMessages.Default_Arg_Not_Boolean, restriction)
		}
	}

}