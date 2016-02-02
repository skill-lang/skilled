package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.validation.AbstractSKilLValidator
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages
import org.eclipse.xtext.validation.Check

/** 
 * All of this classes' subclasses inherit the field restriction validation algorithm
 * and replace the default behaviour when appropriate
 * 
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 * @author Tobias Heck
 */
abstract class AbstractFieldRestrictionsValidator extends AbstractSKilLValidator {

	@Check
	def validateFieldRestrictions(Field field) {
		val Fieldtype fieldtype = field.fieldcontent.fieldtype

		var wasNonNullUsed = false
		var wasDefaultUsed = false
		var wasMaxUsed = false
		var wasMinUsed = false
		var wasConstantLenghtPointerUsed = false
		var wasOneOfUsed = false

		if (handleActivationCondition(fieldtype)) {
			for (restriction : field.restrictions) {
				switch (restriction.restrictionName.toLowerCase) {
					case 'nonnull': {
						handleNonNullRestriction(fieldtype, restriction, wasNonNullUsed)
						wasNonNullUsed = true
					}
					case 'default': {
						handleDefaultRestriction(fieldtype, restriction, wasDefaultUsed)
						wasDefaultUsed = true
					}
					case 'range': {
						handleRangeRestriction(fieldtype, restriction, false)
					}
					case 'max': {
						handleMaxRestriction(fieldtype, restriction, wasMaxUsed)
						wasMaxUsed = true
					}
					case 'min': {
						handleMinRestriction(fieldtype, restriction, wasMinUsed)
						wasMinUsed = true
					}
					case 'coding': {
						handleCodingRestriction(fieldtype, restriction)
					}
					case 'constantlengthpointer': {
						handleConstantLengthPointerRestriction(fieldtype, restriction, wasConstantLenghtPointerUsed)
						wasConstantLenghtPointerUsed = true
					}
					case 'oneof': {
						handleOneOfRestriction(fieldtype, restriction, wasOneOfUsed)
						wasOneOfUsed = true
					}
					default: {
						showError(FieldRestrictionErrorMessages.Unknown_Restriction, restriction)
					}
				}
			}
		}
	}

	def abstract boolean handleActivationCondition(Fieldtype fieldtype)

	def void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasNonNullUsed) {
		showError(FieldRestrictionErrorMessages.NonNull_Usage, restriction)
	}

	def void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		showError(FieldRestrictionErrorMessages.Default_Usage, restriction)
	}

	def void handleRangeRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasRangeUsed) {
		showError(FieldRestrictionErrorMessages.Range_Usage, restriction)
	}

	def void handleMaxRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasMaxUsed) {
		showError(FieldRestrictionErrorMessages.MinMax_Usage, restriction)
	}

	def void handleMinRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasMinUsed) {
		showError(FieldRestrictionErrorMessages.MinMax_Usage, restriction)
	}

	def void handleCodingRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (restriction.restrictionArguments.size() == 1) {
			val argument = restriction.restrictionArguments.get(0)
			if (argument.valueString == null) {
				showError(FieldRestrictionErrorMessages.Coding_Arg_Not_String, restriction)
			} else {
				showWarning(FieldRestrictionErrorMessages.Coding_Usage_Discouraged, restriction)
			}
		} else {
			showError(FieldRestrictionErrorMessages.Coding_Not_One_Arg, restriction)
		}
	}

	def void handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction,
		boolean wasConstantLenghtPointerUsed) {
		showError(FieldRestrictionErrorMessages.ConstantLengthPointer_Usage, restriction)
	}

	def void handleOneOfRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasOneOfUsed) {
		showError(FieldRestrictionErrorMessages.OneOf_Usage, restriction)
	}

	def void showError(String message, Restriction restriction) {
		error(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def void showWarning(String message, Restriction restriction) {
		warning(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def boolean isStringNullOrLowercase(String string) {
		return string != null && string.equals(string.toLowerCase())
	}

}