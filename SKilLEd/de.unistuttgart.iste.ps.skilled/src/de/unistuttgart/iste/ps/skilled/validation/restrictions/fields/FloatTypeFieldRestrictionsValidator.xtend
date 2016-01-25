package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 */
class FloatTypeFieldRestrictionsValidator extends NumericFieldRestrictionValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Floattype
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		if (!wasDefaultUsed && isDefaultArgNotFloat(restriction)) {
			showError(FieldRestrictionErrorMessages.Default_Arg_Not_Float, restriction)
		}
	}

	def private boolean isDefaultArgNotFloat(Restriction r) {
		return r.restrictionArguments.size() == 1 && r.restrictionArguments.get(0).valueDouble == null
	}
	
	override Number getRestrictionArgumentNumeric(Restriction restriction, int index) {
		return restriction.restrictionArguments.get(index).valueDouble
	}

}