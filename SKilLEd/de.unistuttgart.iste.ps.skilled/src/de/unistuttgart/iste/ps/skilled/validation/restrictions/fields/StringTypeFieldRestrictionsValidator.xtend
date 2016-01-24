package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 */
class StringTypeFieldRestrictions extends FieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Stringtype
	}

	override void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasNonNullUsed) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.NonNull_Has_Args, restriction)
		} else if (wasNonNullUsed) {
			showWarning(FieldRestrictionErrorMessages.NonNull_Already_Used, restriction)
		}
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		if (!wasDefaultUsed) {
			if (restriction.restrictionArguments.size() == 1) {
				val restrictionArgument = restriction.restrictionArguments.get(0)
				if (restrictionArgument.valueString == null) {
					if (restrictionArgument.valueType != null) {
						val restrictionArgumentType = (restrictionArgument.valueType).type
						if (restrictionArgumentType instanceof Typedef) {
							if (!(restrictionArgumentType.fieldtype instanceof Stringtype)) {
								showError(FieldRestrictionErrorMessages.Default_Arg_Not_String, restriction)
							}
						} else {
							showError(FieldRestrictionErrorMessages.Default_Arg_Not_String, restriction)
						}
					} else {
						showError(FieldRestrictionErrorMessages.Default_Arg_Not_String, restriction)
					}
				}
			} else {
				showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
			}
		} else {
			showError(FieldRestrictionErrorMessages.Default_Already_Used, restriction)
		}
	}

	override void handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction,
		boolean wasConstantLenghtPointerUsed) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.ConstantLengthPointer_Has_Args, restriction)
		} else if (wasConstantLenghtPointerUsed) {
			showWarning(FieldRestrictionErrorMessages.ConstantLengthPointer_Already_Used, restriction)
		}
	}

}