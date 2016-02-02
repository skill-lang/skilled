package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 */
abstract class AbstractNumericFieldRestrictionValidator extends AbstractFieldRestrictionsValidator {

	override void handleRangeRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasRangeUsed) {

		if (restriction.restrictionArguments.size() == 2) {
			if (getRestrictionArgumentNumeric(restriction, 0) == null) {
				showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 1) == null) {
				showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
				return
			}
		} else if (restriction.restrictionArguments.size() == 4) {
			if (getRestrictionArgumentNumeric(restriction, 0) == null) {
				showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 1) == null) {
				showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
				return
			}
			if (!isStringNullOrLowercase(restriction.restrictionArguments.get(2).valueString)) {
				showError(FieldRestrictionErrorMessages.Range_Third_Arg_Not_Lowercase_String, restriction)
				return
			}
			if (!isStringNullOrLowercase(restriction.restrictionArguments.get(3).valueString)) {
				showError(FieldRestrictionErrorMessages.Range_Fourth_Arg_Not_Lowercase_String, restriction)
				return
			}
		} else {
			showError(FieldRestrictionErrorMessages.Range_Not_2or4_Args, restriction)
			return
		}

		if (wasRangeUsed) {
			showWarning(FieldRestrictionErrorMessages.Range_Multiple_Used, restriction)
		}
	}

	override void handleMaxRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasMaxUsed) {
		handleMinMaxRestriction(restriction, wasMaxUsed)
	}

	override void handleMinRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasMinUsed) {
		handleMinMaxRestriction(restriction, wasMinUsed)
	}

	def void handleMinMaxRestriction(Restriction restriction, boolean wasMinMaxUsed) {
		var errorFound = false

		if (restriction.restrictionArguments.size() == 1) {
			if (getRestrictionArgumentNumeric(restriction, 0) == null) {
				showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Integer, restriction)
				errorFound = true
			}
		} else if (restriction.restrictionArguments.size() == 2) {
			if (getRestrictionArgumentNumeric(restriction, 0) == null) {
				showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Integer, restriction)
				errorFound = true
			}
			if (!isStringNullOrLowercase(restriction.restrictionArguments.get(1).valueString)) {
				showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Not_Lowercase_String, restriction)
				errorFound = true
			}
		} else {
			showError(FieldRestrictionErrorMessages.MinMax_Not_1or2_Args, restriction)
			errorFound = true
		}

		if (!errorFound && wasMinMaxUsed) {
			showWarning(FieldRestrictionErrorMessages.MinMax_Multiple_Used, restriction)
		}
	}

	def Number getRestrictionArgumentNumeric(Restriction restriction, int index) {
		return restriction.restrictionArguments.get(index).valueLong
	}

}