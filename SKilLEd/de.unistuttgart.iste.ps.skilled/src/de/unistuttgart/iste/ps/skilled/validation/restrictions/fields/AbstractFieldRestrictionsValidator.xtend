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
	
	var protected wasNonNullUsed = false
	var protected wasDefaultUsed = false
	var protected wasMaxUsed = false
	var protected wasMinUsed = false
	var protected wasConstantLenghtPointerUsed = false
	var protected wasOneOfUsed = false
	var protected wasRangeUsed = false

	@Check
	def validateFieldRestrictions(Field field) {
		val Fieldtype fieldtype = field.fieldcontent.fieldtype

		wasNonNullUsed = false
		wasDefaultUsed = false
		wasMaxUsed = false
		wasMinUsed = false
		wasConstantLenghtPointerUsed = false
		wasOneOfUsed = false
		wasRangeUsed = false

		if (handleActivationCondition(fieldtype)) {
			for (restriction : field.restrictions) {
				switch (restriction.restrictionName.toLowerCase) {
					case 'nonnull': {
						handleNonNullRestriction(fieldtype, restriction)
						wasNonNullUsed = true
					}
					case 'default': {
						handleDefaultRestriction(fieldtype, restriction)
						wasDefaultUsed = true
					}
					case 'range': {
						handleRangeRestriction(fieldtype, restriction)
						wasRangeUsed = true
					}
					case 'max': {
						handleMaxRestriction(fieldtype, restriction)
						wasMaxUsed = true
					}
					case 'min': {
						handleMinRestriction(fieldtype, restriction)
						wasMinUsed = true
					}
					case 'coding': {
						handleCodingRestriction(fieldtype, restriction)
					}
					case 'constantlengthpointer': {
						handleConstantLengthPointerRestriction(fieldtype, restriction)
						wasConstantLenghtPointerUsed = true
					}
					case 'oneof': {
						handleOneOfRestriction(fieldtype, restriction)
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

	def void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.NonNull_Usage, restriction)
	}

	def void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Default_Usage, restriction)
	}

	def void handleRangeRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Range_Usage, restriction)
	}

	def void handleMaxRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.MinMax_Usage, restriction)
	}

	def void handleMinRestriction(Fieldtype fieldtype, Restriction restriction) {
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

	def void handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.ConstantLengthPointer_Usage, restriction)
	}

	def void handleOneOfRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.OneOf_Usage, restriction)
	}

	def void showError(String message, Restriction restriction) {
		error(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def void showWarning(String message, Restriction restriction) {
		warning(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def public static boolean isStringNullOrLowercase(String string) {
		return string != null && string.equals(string.toLowerCase())
	}

}