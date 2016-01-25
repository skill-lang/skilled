package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 */
class IntegerTypeFieldRestrictionsValidator extends NumericFieldRestrictionValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Integertype
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		if (!wasDefaultUsed && isDefaultArgNotInteger(restriction)) {
			showError(FieldRestrictionErrorMessages.Default_Arg_Not_Integer, restriction)
		}
	}

	def private boolean isDefaultArgNotInteger(Restriction r) {
		return r.restrictionArguments.size() == 1 && r.restrictionArguments.get(0).valueLong == null
	}

}