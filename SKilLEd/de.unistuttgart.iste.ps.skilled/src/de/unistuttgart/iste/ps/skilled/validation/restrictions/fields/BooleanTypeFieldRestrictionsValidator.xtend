package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Tobias Heck
 */
class BooleanTypeFieldRestrictionsValidator extends AbstractFieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Booleantype
	}
	
	override void handleDefaultRestriction (Fieldtype fieldtype, Restriction restriction) {
		if (restriction.restrictionArguments.size != 1) {
			showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
			return
		}
		//For some reason restriction arguments always have a valueBoolean != null
		//that's why we need to check all other values
		var boolean isBoolean = true
		val argument = restriction.restrictionArguments.get(0)
		if (argument.valueDouble != null) isBoolean = false
		if (argument.valueLong != null) isBoolean = false
		if (argument.valueString != null) isBoolean = false
		if (argument.valueType != null) isBoolean = false
		if (!isBoolean) {
			showError(FieldRestrictionErrorMessages.Default_Arg_Not_Boolean, restriction)
		}
	}

}