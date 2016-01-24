package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 */
class NumericTypeFieldRestrictions extends FieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Integertype || fieldtype instanceof Floattype
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasDefaultUsed) {
		if (!wasDefaultUsed) {
			if (restriction.restrictionArguments.size() == 1) {
				val restrictionArgument = restriction.restrictionArguments.get(0)
				if (fieldtype instanceof Integertype) {
					if (restrictionArgument.valueLong == null) {
						showError(FieldRestrictionErrorMessages.Default_Arg_Not_Integer, restriction)
					} else if (restrictionArgument.valueType != null) {
						val restrictionArgumentType = (restrictionArgument.valueType).type
						if (restrictionArgumentType instanceof Typedef) {
						}
					}
				} else if (fieldtype instanceof Floattype) {
					if (restrictionArgument.valueDouble == null) {
						showError(FieldRestrictionErrorMessages.Default_Arg_Not_Float, restriction)
					}
				}
			}
		}
	}

	override void handleRangeRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasRangeUsed) {
		var errorFound = false
		var restrictionArguments = restriction.restrictionArguments
		if (restrictionArguments.size() == 2) {
			val argument1 = restrictionArguments.get(0)
			val argument2 = restrictionArguments.get(1)
			if (fieldtype instanceof Integertype) {
				if (argument1.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
					errorFound = true
				} else if (argument2.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
					errorFound = true
				}
			} else if (fieldtype instanceof Floattype) {
				if (argument1.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Float, restriction)
					errorFound = true
				} else if (argument2.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Float, restriction)
					errorFound = true
				}
			}
		} else if (restrictionArguments.size() == 4) {
			val argument1 = restrictionArguments.get(0)
			val argument2 = restrictionArguments.get(1)
			val argument3 = restrictionArguments.get(2)
			val argument4 = restrictionArguments.get(3)
			if (fieldtype instanceof Integertype) {
				if (argument1.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
					errorFound = true
				} else if (argument2.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
					errorFound = true
				} else if (!isStringNullOrLowercase(argument3.valueString)) {
					showError(FieldRestrictionErrorMessages.Range_Third_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				} else if (!isStringNullOrLowercase(argument4.valueString)) {
					showError(FieldRestrictionErrorMessages.Range_Fourth_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				}
			} else if (fieldtype instanceof Floattype) {
				if (argument1.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Float, restriction)
					errorFound = true
				} else if (argument2.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Float, restriction)
					errorFound = true
				} else if (argument3.valueString == null) {
					showError(FieldRestrictionErrorMessages.Range_Third_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				} else if (argument4.valueString == null) {
					showError(FieldRestrictionErrorMessages.Range_Fourth_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.Range_Not_2or4_Args, restriction)
			errorFound = true
		}
		if (!errorFound && wasRangeUsed) {
			showWarning(FieldRestrictionErrorMessages.Range_Multiple_Used, restriction)
		}
	}

	override void handleMaxRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasMaxUsed) {
		var errorFound = false
		var restrictionArguments = restriction.restrictionArguments
		if (restrictionArguments.size() == 1) {
			val argument1 = restrictionArguments.get(0)
			if (fieldtype instanceof Integertype) {
				if (argument1.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Max_Arg_Not_Integer, restriction)
					errorFound = true
				}
			} else if (fieldtype instanceof Floattype) {
				if (argument1.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Max_Arg_Not_Float, restriction)
					errorFound = true
				}
			}
		} else if (restrictionArguments.size() == 2) {
			val argument1 = restrictionArguments.get(0)
			val argument2 = restrictionArguments.get(1)
			if (fieldtype instanceof Integertype) {
				if (argument1.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Max_First_Arg_Not_Integer, restriction)
					errorFound = true
				} else if (!isStringNullOrLowercase(argument2.valueString)) {
					showError(FieldRestrictionErrorMessages.Max_Second_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				}
			} else if (fieldtype instanceof Floattype) {
				if (argument1.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Max_First_Arg_Not_Float, restriction)
					errorFound = true
				} else if (!isStringNullOrLowercase(argument2.valueString)) {
					showError(FieldRestrictionErrorMessages.Max_Second_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.Max_Not_1or2_Args, restriction)
			errorFound = true
		}
		if (!errorFound && wasMaxUsed) {
			showWarning(FieldRestrictionErrorMessages.Max_Multiple_Used, restriction)
		}
	}

	override void handleMinRestriction(Fieldtype fieldtype, Restriction restriction, boolean wasMinUsed) {
		var errorFound = false
		val restrictionArguments = restriction.restrictionArguments
		if (restrictionArguments.size() == 1) {
			val argument1 = restrictionArguments.get(0)
			if (fieldtype instanceof Integertype) {
				if (argument1.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Min_Arg_Not_Integer, restriction)
					errorFound = true
				}
			} else if (fieldtype instanceof Floattype) {
				if (argument1.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Min_Arg_Not_Float, restriction)
					errorFound = true
				}
			}
		} else if (restrictionArguments.size() == 2) {
			val argument1 = restrictionArguments.get(0)
			val argument2 = restrictionArguments.get(1)
			if (fieldtype instanceof Integertype) {
				if (argument1.valueLong == null) {
					showError(FieldRestrictionErrorMessages.Min_First_Arg_Not_Integer, restriction)
					errorFound = true
				} else if (!isStringNullOrLowercase(argument2.valueString)) {
					showError(FieldRestrictionErrorMessages.Min_Second_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				}
			} else if (fieldtype instanceof Floattype) {
				if (argument1.valueDouble == null) {
					showError(FieldRestrictionErrorMessages.Min_First_Arg_Not_Float, restriction)
					errorFound = true
				} else if (!isStringNullOrLowercase(argument2.valueString)) {
					showError(FieldRestrictionErrorMessages.Min_Second_Arg_Not_Lowercase_String, restriction)
					errorFound = true
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.Min_Not_1or2_Args, restriction)
			errorFound = true
		}
		if (!errorFound && wasMinUsed) {
			showWarning(FieldRestrictionErrorMessages.Min_Multiple_Used, restriction)
		}
	}

}