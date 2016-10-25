package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Fieldtype
import de.unistuttgart.iste.ps.skilled.skill.Floattype
import de.unistuttgart.iste.ps.skilled.skill.Restriction
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Tobias Heck
 */
class FloatTypeFieldRestrictionsValidator extends AbstractNumericFieldRestrictionValidator {

	override void handleRangeRestriction(Fieldtype fieldtype2, Restriction restriction) {
		var Fieldtype fieldtype = null
		if (fieldtype2 instanceof DeclarationReference) {
			fieldtype = (fieldtype2.type as Typedef).fieldtype
		} else {
			fieldtype = fieldtype2
		}

		if (restriction.restrictionArguments.size() == 2) {
			if (getRestrictionArgumentNumeric(restriction, 0) == null) {
				showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Float, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 1) == null) {
				showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Float, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 0).doubleValue >
				getRestrictionArgumentNumeric(restriction, 1).doubleValue) {
				showError(FieldRestrictionErrorMessages.Range_Arguments_Switched, restriction)
			} else {
				var double maxValue = 0
				switch ((fieldtype as Floattype).type) {
					case F32: maxValue = Float.MAX_VALUE
					default: maxValue = Double.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 1).doubleValue < -maxValue ||
					getRestrictionArgumentNumeric(restriction, 0).doubleValue > maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
				} else if (getRestrictionArgumentNumeric(restriction, 0).doubleValue < -maxValue ||
					getRestrictionArgumentNumeric(restriction, 1).doubleValue > maxValue) {
					showWarning(FieldRestrictionErrorMessages.Range_Partly_Outside, restriction)
				}
			}

		} else if (restriction.restrictionArguments.size() == 4) {
			if (getRestrictionArgumentNumeric(restriction, 0) == null) {
				showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Float, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 1) == null) {
				showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Float, restriction)
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
			if (getRestrictionArgumentNumeric(restriction, 0).doubleValue >
				getRestrictionArgumentNumeric(restriction, 1).doubleValue) {
				showError(FieldRestrictionErrorMessages.Range_Arguments_Switched, restriction)
				return
			}
			if (getRestrictionArgumentNumeric(restriction, 0).doubleValue ==
				getRestrictionArgumentNumeric(restriction, 1).doubleValue &&
				(restriction.restrictionArguments.get(2).valueString.toLowerCase.contains("ex") ||
					restriction.restrictionArguments.get(3).valueString.toLowerCase.contains("ex"))) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				var double maxValue = 0
				switch ((fieldtype as Floattype).type) {
					case F32: maxValue = Float.MAX_VALUE
					default: maxValue = Double.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 1).doubleValue < -maxValue ||
					getRestrictionArgumentNumeric(restriction, 0).doubleValue > maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue < -maxValue ||
					getRestrictionArgumentNumeric(restriction, 1).doubleValue > maxValue) {
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
			if(fieldtype instanceof Floattype) return true
			if (fieldtype instanceof DeclarationReference) {
				if (fieldtype.type instanceof Typedef) {
					if ((fieldtype.type as Typedef).fieldtype instanceof Floattype) {
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
			if (isDefaultArgNotFloat(restriction)) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Float, restriction)
				return
			}
			var double maxValue = 0
			switch ((fieldtype as Floattype).type) {
				case F32: maxValue = Float.MAX_VALUE
				default: maxValue = Double.MAX_VALUE
			}
			if (getRestrictionArgumentNumeric(restriction, 0).doubleValue < -maxValue ||
				getRestrictionArgumentNumeric(restriction, 0).doubleValue > maxValue) {
				showError(FieldRestrictionErrorMessages.Default_Boundary, restriction)
				return
			}
		}

		def private boolean isDefaultArgNotFloat(Restriction r) {
			return r.restrictionArguments.size() == 1 && r.restrictionArguments.get(0).valueDouble == null
		}

		override Number getRestrictionArgumentNumeric(Restriction restriction, int index) {
			return restriction.restrictionArguments.get(index).valueDouble
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
					showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Float, restriction)
					return
				}
				var double maxValue = 0
				switch ((fieldtype as Floattype).type) {
					case F32: maxValue = Float.MAX_VALUE
					default: maxValue = Double.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue < -maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue > maxValue) {
					showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
					return
				}

			} else if (restriction.restrictionArguments.size() == 2) {
				if (getRestrictionArgumentNumeric(restriction, 0) == null) {
					showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Float, restriction)
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
				var double maxValue = 0
				switch ((fieldtype as Floattype).type) {
					case F32: maxValue = Float.MAX_VALUE
					default: maxValue = Double.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue < -maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue > maxValue) {
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
					showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Float, restriction)
					return
				}
				var double maxValue = 0
				switch ((fieldtype as Floattype).type) {
					case F32: maxValue = Float.MAX_VALUE
					default: maxValue = Double.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue > maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue < -maxValue) {
					showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
					return
				}

			} else if (restriction.restrictionArguments.size() == 2) {
				if (getRestrictionArgumentNumeric(restriction, 0) == null) {
					showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Float, restriction)
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
				var double maxValue = 0
				switch ((fieldtype as Floattype).type) {
					case F32: maxValue = Float.MAX_VALUE
					default: maxValue = Double.MAX_VALUE
				}
				if (getRestrictionArgumentNumeric(restriction, 0).doubleValue > maxValue) {
					showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					return
				} else if (getRestrictionArgumentNumeric(restriction, 0).doubleValue < -maxValue) {
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