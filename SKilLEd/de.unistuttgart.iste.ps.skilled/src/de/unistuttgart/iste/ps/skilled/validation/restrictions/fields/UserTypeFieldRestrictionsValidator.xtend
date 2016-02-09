package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 * @author Tobias Heck
 */
class UserTypeFieldRestrictions extends AbstractFieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		if (fieldtype instanceof DeclarationReference) {
			if (fieldtype.type instanceof Typedef) {
				if ((fieldtype.type as Typedef).fieldtype instanceof Integertype)
					return false
				if ((fieldtype.type as Typedef).fieldtype instanceof Floattype)
					return false
			}
			return true
		}
		return false
	}

	override void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasNonNullUsed) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.NonNull_Has_Args, restriction)
		} else if (wasNonNullUsed) {
			showWarning(FieldRestrictionErrorMessages.NonNull_Already_Used, restriction)
		}
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		if (wasDefaultUsed) {
			showError(FieldRestrictionErrorMessages.Default_Already_Used, restriction)
			return
		}

		if (restriction.restrictionArguments.size() == 1) {
			if (restriction.restrictionArguments.get(0).valueType == null) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
			} else {
				handleIsRestrictionArgumentUsertype(fieldtype, restriction)
			}
		} else {
			showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
		}
	}

	def private void handleIsRestrictionArgumentUsertype(Fieldtype fieldtype, Restriction restriction) {
		val restrictionArgumentType = (restriction.restrictionArguments.get(0).valueType).type

		if (restrictionArgumentType instanceof Usertype) {
			if (!isRestrictionArgumentSubTypeOfField(restrictionArgumentType, fieldtype)) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
			} else if (!isRestrictionArgumentTypeSingletonRestricted(restrictionArgumentType)) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
			}
		} else if (restrictionArgumentType instanceof Enumtype) {
			// TODO
			return
		} else {
			showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
		}
	}

	def private boolean isRestrictionArgumentSubTypeOfField(Usertype ut, Fieldtype fieldtype) {
		val fieldTypeName = (fieldtype as DeclarationReference).type.name

		for (supertype : ut.supertypes) {
			if (fieldTypeName.equals(supertype.type.name)) {
				return true
			}
		}
		return false
	}

	def private boolean isRestrictionArgumentTypeSingletonRestricted(Usertype ut) {
		for (restrictionArgumentRestriction : ut.restrictions) {
			if ("singleton".equals(restrictionArgumentRestriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
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