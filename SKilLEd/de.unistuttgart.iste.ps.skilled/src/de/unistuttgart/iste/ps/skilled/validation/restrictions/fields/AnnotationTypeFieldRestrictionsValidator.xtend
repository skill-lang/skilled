package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 */
class AnnotationTypeFieldRestrictionsValidator extends FieldRestrictionsValidator {

	override handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Annotationtype
	}

	override handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		if (wasDefaultUsed) {
			showError(FieldRestrictionErrorMessages.Default_Already_Used, restriction)
			return
		}
		if (restriction.restrictionArguments.size() == 1) {
			val restrictionArgument = restriction.restrictionArguments.get(0)
			if (restrictionArgument.valueType == null) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, restriction)
			} else {
				val restrictionArgumentType = (restrictionArgument.valueType).type
				if (restrictionArgumentType instanceof Typedef) {
					val restrictionArgumentRestrictions = restrictionArgumentType.restrictions
					var isRestrictionArgumentSingletonRestricted = false
					for (typedefRestriction : restrictionArgumentRestrictions) {
						if ("singleton".equals(typedefRestriction.restrictionName.toLowerCase)) {
							isRestrictionArgumentSingletonRestricted = true
						}
					}
					if (!isRestrictionArgumentSingletonRestricted) {
						showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, restriction)
					}
				} else if (restrictionArgumentType instanceof Usertype) {
					val restrictionArgumentRestrictions = restrictionArgumentType.restrictions
					var isRestrictionArgumentSingletonRestricted = false
					for (typedefRestriction : restrictionArgumentRestrictions) {
						if ("singleton".equals(typedefRestriction.restrictionName.toLowerCase)) {
							isRestrictionArgumentSingletonRestricted = true
						}
					}
					if (!isRestrictionArgumentSingletonRestricted) {
						showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, restriction)
					}
				} else if (restrictionArgumentType instanceof Interfacetype) {
					showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, restriction)
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
		}
	}

	override handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction,
		boolean wasConstantLenghtPointerUsed) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.ConstantLengthPointer_Has_Args, restriction)
		} else if (wasConstantLenghtPointerUsed) {
			showWarning(FieldRestrictionErrorMessages.ConstantLengthPointer_Already_Used, restriction)
		}
	}

	override handleOneOfRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasOneOfUsed) {
		if (wasOneOfUsed) {
			showError(FieldRestrictionErrorMessages.OneOf_Already_Used, restriction)
			return
		}
		
		if (restriction.restrictionArguments.size() >= 1) {
			for (restrictionArgument : restriction.restrictionArguments) {
				if (restrictionArgument.valueType == null) {
					showError(FieldRestrictionErrorMessages.OneOf_Arg_Not_Usertype, restriction)
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.OneOf_Not_One_Arg, restriction)
		}
	}

}