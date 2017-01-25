package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Fieldtype
import de.unistuttgart.iste.ps.skilled.skill.Integertype
import de.unistuttgart.iste.ps.skilled.skill.Restriction
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Tobias Heck
 */
class IntegerTypeFieldRestrictionsValidator extends AbstractNumericFieldRestrictionValidator {

	override void handleRangeRestriction(Fieldtype fieldtype2, Restriction restriction) {
		var Fieldtype fieldtype = null
		if (fieldtype2 instanceof DeclarationReference) {
			fieldtype = (fieldtype2.type as Typedef).fieldtype
		} else {
			fieldtype = fieldtype2
		}
		if (restriction.restrictionArguments.size() == 2) {
			if (getRestrictionArgumentNumeric(restriction, 0) == null) {
				showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 1) == null) {
				showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 0).longValue >
				getRestrictionArgumentNumeric(restriction, 1).longValue) {
				showError(FieldRestrictionErrorMessages.Range_Arguments_Switched, restriction)
			} else {
				var long maxValue = 0
				switch ((fieldtype as Integertype).type) {
					case I8: maxValue = Byte.MAX_VALUE
					case I16: maxValue = Short.MAX_VALUE
					case I32: maxValue = Integer.MAX_VALUE
					case I64: maxValue = Long.MAX_VALUE
					default: maxValue = Long.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 1).longValue < -maxValue - 1 ||
					getRestrictionArgumentNumeric(restriction, 0).longValue > maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
				} else if (getRestrictionArgumentNumeric(restriction, 0).longValue < -maxValue - 1 ||
					getRestrictionArgumentNumeric(restriction, 1).longValue > maxValue) {
					showWarning(FieldRestrictionErrorMessages.Range_Partly_Outside, restriction)
				}
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
			if (!restriction.restrictionArguments.get(2).valueString.toLowerCase.matches(
				"(\\s)*(inclusive|exclusive)(\\s)*")) {
				showError(FieldRestrictionErrorMessages.Range_Third_Arg_Wrong_String, restriction)
				return
			}
			if (!restriction.restrictionArguments.get(3).valueString.toLowerCase.matches(
				"(\\s)*(inclusive|exclusive)(\\s)*")) {
				showError(FieldRestrictionErrorMessages.Range_Fourth_Arg_Wrong_String, restriction)
				return
			}
			if (restriction.restrictionArguments.get(0).valueLong > restriction.restrictionArguments.get(1).valueLong) {
				showError(FieldRestrictionErrorMessages.Range_Arguments_Switched, restriction)
				return
			}
			if (restriction.restrictionArguments.get(0).valueLong + 1 ==
				restriction.restrictionArguments.get(1).valueLong &&
				restriction.restrictionArguments.get(2).valueString.toLowerCase.contains("ex") &&
				restriction.restrictionArguments.get(3).valueString.toLowerCase.contains("ex")) {
				showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
				return
			}
			if (restriction.restrictionArguments.get(0).valueLong ==
				restriction.restrictionArguments.get(1).valueLong &&
				(restriction.restrictionArguments.get(2).valueString.toLowerCase.contains("ex") ||
					restriction.restrictionArguments.get(3).valueString.toLowerCase.contains("ex"))) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				var long maxValue = 0
				switch ((fieldtype as Integertype).type) {
					case I8: maxValue = Byte.MAX_VALUE
					case I16: maxValue = Short.MAX_VALUE
					case I32: maxValue = Integer.MAX_VALUE
					case I64: maxValue = Long.MAX_VALUE
					default: maxValue = Long.MAX_VALUE
				}
				var notAsLow = 0
				var notAsHigh = 0
				if(restriction.restrictionArguments.get(2).valueString.toLowerCase.contains("ex")) notAsLow = 1
				if(restriction.restrictionArguments.get(3).valueString.toLowerCase.contains("ex")) notAsHigh = 1
				if (restriction.restrictionArguments.get(1).valueLong < -maxValue - 1 + notAsHigh ||
					restriction.restrictionArguments.get(0).valueLong > maxValue - notAsLow) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (restriction.restrictionArguments.get(0).valueLong < -maxValue - 1 - notAsLow ||
					restriction.restrictionArguments.get(1).valueLong > maxValue + notAsHigh) {
					showWarning(FieldRestrictionErrorMessages.Range_Partly_Outside, restriction)
					return
				}
			} else {
				showError(FieldRestrictionErrorMessages.Range_Not_2or4_Args, restriction)
				return
			}
			if (wasRangeUsed || wasMinUsed || wasMaxUsed) {
				showWarning(FieldRestrictionErrorMessages.Range_Multiple_Used, restriction)
			}
		}

		override boolean handleActivationCondition(Fieldtype fieldtype) {
			if(fieldtype instanceof Integertype) return true
			if (fieldtype instanceof DeclarationReference) {
				if (fieldtype.type instanceof Typedef) {
					if ((fieldtype.type as Typedef).fieldtype instanceof Integertype) {
						for (Restriction r : (fieldtype.type as Typedef).restrictions) {
							switch (r.restrictionName.toLowerCase) {
								case 'nonnull': wasNonNullUsed = true
								case 'default': wasDefaultUsed = true
								case 'range': wasRangeUsed = true
								case 'max': wasMaxUsed = true
								case 'min': wasMinUsed = true
								case 'constantlengthpointer': wasConstantLenghtPointerUsed = true
								case 'oneof': wasOneOfUsed = true
							}
						}
					return true
					}
				}
			}
			return false
		}

		override void handleDefaultRestriction(Fieldtype fieldtype2, Restriction restriction) {
			var Fieldtype fieldtype = null
			if (fieldtype2 instanceof DeclarationReference) {
				fieldtype = (fieldtype2.type as Typedef).fieldtype
			} else {
				fieldtype = fieldtype2
			}
			if (wasDefaultUsed) {
				showError(FieldRestrictionErrorMessages.Default_Already_Used, restriction)
				return
			}
			if (isDefaultArgNotInteger(restriction)) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Integer, restriction)
				return
			}
			var long maxValue = 0
			switch ((fieldtype as Integertype).type) {
				case I8: maxValue = Byte.MAX_VALUE
				case I16: maxValue = Short.MAX_VALUE
				case I32: maxValue = Integer.MAX_VALUE
				case I64: maxValue = Long.MAX_VALUE
				default: maxValue = Long.MAX_VALUE
			}
			if (getRestrictionArgumentNumeric(restriction, 0).longValue < -maxValue - 1 ||
				getRestrictionArgumentNumeric(restriction, 0).longValue > maxValue) {
				showError(FieldRestrictionErrorMessages.Default_Boundary, restriction)
				return
			}
		}

		def private boolean isDefaultArgNotInteger(Restriction r) {
			return r.restrictionArguments.size() == 1 && r.restrictionArguments.get(0).valueLong == null
		}

		override void handleMaxRestriction(Fieldtype fieldtype2, Restriction restriction) {
			var Fieldtype fieldtype = null
			if (fieldtype2 instanceof DeclarationReference) {
				fieldtype = (fieldtype2.type as Typedef).fieldtype
			} else {
				fieldtype = fieldtype2
			}

			if (restriction.restrictionArguments.size() == 1) {
				if (getRestrictionArgumentNumeric(restriction, 0) == null) {
					showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Integer, restriction)
					return
				}
				var long maxValue = 0
				switch ((fieldtype as Integertype).type) {
					case I8: maxValue = Byte.MAX_VALUE
					case I16: maxValue = Short.MAX_VALUE
					case I32: maxValue = Integer.MAX_VALUE
					case I64: maxValue = Long.MAX_VALUE
					default: maxValue = Long.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 0).longValue < -maxValue - 1) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				} else if (getRestrictionArgumentNumeric(restriction, 0).longValue > maxValue - 1) {
					showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
					return
				}
			} else if (restriction.restrictionArguments.size() == 2) {
				if (getRestrictionArgumentNumeric(restriction, 0) == null) {
					showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Integer, restriction)
					return
				}
				if (!isStringNullOrLowercase(restriction.restrictionArguments.get(1).valueString)) {
					showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Not_Lowercase_String, restriction)
					return
				}
				if (!restriction.restrictionArguments.get(1).valueString.toLowerCase.matches(
					"(\\s)*(inclusive|exclusive)(\\s)*")) {
					showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Wrong_String, restriction)
					return
				}
				var long maxValue = 0
				switch ((fieldtype as Integertype).type) {
					case I8: maxValue = Byte.MAX_VALUE
					case I16: maxValue = Short.MAX_VALUE
					case I32: maxValue = Integer.MAX_VALUE
					case I64: maxValue = Long.MAX_VALUE
					default: maxValue = Long.MAX_VALUE
				}
				var notAsLow = 0
				if(restriction.restrictionArguments.get(1).valueString.toLowerCase.contains("ex")) notAsLow = 1
				if (getRestrictionArgumentNumeric(restriction, 0).longValue < -maxValue - 1 + notAsLow) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (getRestrictionArgumentNumeric(restriction, 0).longValue > maxValue + notAsLow - 1) {
					showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
					return
				}
			} else {
				showError(FieldRestrictionErrorMessages.MinMax_Not_1or2_Args, restriction)
				return
			}

			if (wasMaxUsed || wasMinUsed || wasRangeUsed) {
				showWarning(FieldRestrictionErrorMessages.MinMax_Multiple_Used, restriction)
				return
			}

		}

		override void handleMinRestriction(Fieldtype fieldtype2, Restriction restriction) {
			var Fieldtype fieldtype = null
			if (fieldtype2 instanceof DeclarationReference) {
				fieldtype = (fieldtype2.type as Typedef).fieldtype
			} else {
				fieldtype = fieldtype2
			}
			if (restriction.restrictionArguments.size() == 1) {
				if (getRestrictionArgumentNumeric(restriction, 0) == null) {
					showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Integer, restriction)
					return
				}
				var long maxValue = 0
				switch ((fieldtype as Integertype).type) {
					case I8: maxValue = Byte.MAX_VALUE
					case I16: maxValue = Short.MAX_VALUE
					case I32: maxValue = Integer.MAX_VALUE
					case I64: maxValue = Long.MAX_VALUE
					default: maxValue = Long.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 0).longValue > maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (getRestrictionArgumentNumeric(restriction, 0).longValue < -maxValue) {
					showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
					return
				}

			} else if (restriction.restrictionArguments.size() == 2) {
				if (getRestrictionArgumentNumeric(restriction, 0) == null) {
					showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Integer, restriction)
					return
				}
				if (!isStringNullOrLowercase(restriction.restrictionArguments.get(1).valueString)) {
					showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Not_Lowercase_String, restriction)
					return
				}
				if (!restriction.restrictionArguments.get(1).valueString.toLowerCase.matches(
					"(\\s)*(inclusive|exclusive)(\\s)*")) {
					showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Wrong_String, restriction)
					return
				}
				var long maxValue = 0
				switch ((fieldtype as Integertype).type) {
					case I8: maxValue = Byte.MAX_VALUE
					case I16: maxValue = Short.MAX_VALUE
					case I32: maxValue = Integer.MAX_VALUE
					case I64: maxValue = Long.MAX_VALUE
					default: maxValue = Long.MAX_VALUE
				}
				var notAsHigh = 0
				if(restriction.restrictionArguments.get(1).valueString.toLowerCase.contains("ex")) notAsHigh = 1
				if (getRestrictionArgumentNumeric(restriction, 0).longValue > maxValue - notAsHigh) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (getRestrictionArgumentNumeric(restriction, 0).longValue < -maxValue - notAsHigh) {
					showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
					return
				}

			} else {
				showError(FieldRestrictionErrorMessages.MinMax_Not_1or2_Args, restriction)
				return
			}

			if (wasMinUsed || wasMaxUsed || wasRangeUsed) {
				showWarning(FieldRestrictionErrorMessages.MinMax_Multiple_Used, restriction)
				return
			}
		}
	}