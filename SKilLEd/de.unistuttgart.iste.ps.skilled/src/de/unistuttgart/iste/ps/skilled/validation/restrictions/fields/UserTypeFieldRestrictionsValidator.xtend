package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 */
class UserTypeFieldRestrictions extends FieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof DeclarationReference
	}

	override void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasNonNullUsed) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.NonNull_Has_Args, restriction)
		} else if (wasNonNullUsed) {
			showWarning(FieldRestrictionErrorMessages.NonNull_Already_Used, restriction)
		}
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		val fieldTypeName = (fieldtype as DeclarationReference).type.name
		if (!wasDefaultUsed) {
			if (restriction.restrictionArguments.size() == 1) {
				val restrictionArgument = restriction.restrictionArguments.get(0)
				if (restrictionArgument.valueType == null) {
					showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
				} else {
					val restrictionArgumentType = (restrictionArgument.valueType).type
					if (restrictionArgumentType instanceof Usertype) {
						val restrictionArgumentSupertypes = restrictionArgumentType.supertypes
						var isRestrictionArgumentSubTypeOfField = false
						for (supertype : restrictionArgumentSupertypes) {
							if (fieldTypeName.equals(supertype.type.name)) {
								isRestrictionArgumentSubTypeOfField = true
							}
						}
						var isRestrictionArgumentTypeSingletonRestricted = false
						if (isRestrictionArgumentSubTypeOfField) {
							val restrictionArgumentRestrictions = restrictionArgumentType.restrictions
							for (restrictionArgumentRestriction : restrictionArgumentRestrictions) {
								if ("singleton".equals(restrictionArgumentRestriction.restrictionName.toLowerCase)) {
									isRestrictionArgumentTypeSingletonRestricted = true
								}
							}
						}
						if (!isRestrictionArgumentSubTypeOfField || !isRestrictionArgumentTypeSingletonRestricted) {
							showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
						}
					} else if (restrictionArgumentType instanceof Enumtype) {
					} else {
						showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
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

	override void handleOneOfRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasOneOfUsed) {
		if (!wasOneOfUsed) {
			if (restriction.restrictionArguments.size() >= 1) {
				for (restrictionArgument : restriction.restrictionArguments) {
					if (restrictionArgument.valueType == null) {
						showError(FieldRestrictionErrorMessages.OneOf_Arg_Not_Usertype, restriction)
					}
				}
			} else {
				showError(FieldRestrictionErrorMessages.OneOf_Not_One_Arg, restriction)
			}
		} else {
			showError(FieldRestrictionErrorMessages.OneOf_Already_Used, restriction)
		}
	}
}