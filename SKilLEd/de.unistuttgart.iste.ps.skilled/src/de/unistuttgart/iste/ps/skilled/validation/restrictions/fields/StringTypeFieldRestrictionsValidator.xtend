package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.skill.Fieldtype
import de.unistuttgart.iste.ps.skilled.skill.Restriction
import de.unistuttgart.iste.ps.skilled.skill.Stringtype
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 */
class StringTypeFieldRestrictions extends AbstractFieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Stringtype
	}

	override void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.NonNull_Has_Args, restriction)
		} else if (wasNonNullUsed) {
			showWarning(FieldRestrictionErrorMessages.NonNull_Already_Used, restriction)
		}
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (wasDefaultUsed) {
			showError(FieldRestrictionErrorMessages.Default_Already_Used, restriction)
			return
		}

		if (restriction.restrictionArguments.size() == 1) {
			val restrictionArgument = restriction.restrictionArguments.get(0)
			
			if (restrictionArgument.valueString != null) {
				return
			}
			
			if (restrictionArgument.valueType != null) {
				val restrictionArgumentType = (restrictionArgument.valueType).type
				if (restrictionArgumentType instanceof Typedef) {
					if ((restrictionArgumentType.fieldtype instanceof Stringtype)) {
						return
					}
				}
			}
		}
		showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
	}

	override void handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.ConstantLengthPointer_Has_Args, restriction)
		} else if (wasConstantLenghtPointerUsed) {
			showWarning(FieldRestrictionErrorMessages.ConstantLengthPointer_Already_Used, restriction)
		}
	}

}