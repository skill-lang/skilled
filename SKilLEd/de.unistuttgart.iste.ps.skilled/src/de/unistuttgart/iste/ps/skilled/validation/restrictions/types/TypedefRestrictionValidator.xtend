package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype
import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.util.SubtypesFinder
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages
import de.unistuttgart.iste.ps.skilled.validation.errormessages.TypeRestrictionsErrorMessages
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.AbstractFieldRestrictionsValidator

/**
 * @author Nikolay Fateev
 * @author Moritz Platzer
 * @author Tobias Heck
 * @author Daniel Ryan Degutis
 */
class TypedefRestrictionValidator extends AbstractTypeRestrictionsValidator {

	@Inject
	private SubtypesFinder subtypesFinder
	
	override handleActivationCondition(Declaration declaration) {
		return declaration instanceof Typedef
	}

	override handleOneOfRestriction(Restriction restriction, Declaration declaration) {
		if ((declaration as Typedef).fieldtype instanceof Annotationtype) {
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
		} else {
			showError(FieldRestrictionErrorMessages.OneOf_Usage, restriction)
		}
	}

	override handleAbstractRestriction(Restriction restriction, Declaration declaration) {
		val typedef = declaration as Typedef
		if (!(typedef.fieldtype instanceof Maptype)) {
			if (restriction.restrictionArguments.size != 0) {
				showError(TypeRestrictionsErrorMessages.Abstract_Has_Args, restriction)
			} else if (wasAbstractUsed) {
				showWarning(TypeRestrictionsErrorMessages.Abstract_Already_Used, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Restriction_On_Map, restriction)
		}
	}

	override handleMonotoneRestriction(Restriction restriction, Declaration declaration) {
		if (restriction.restrictionArguments.size == 0) {
			if (underlyingUsertype != null) {
				if (underlyingUsertype.supertypes.size != 0) {
					showError(TypeRestrictionsErrorMessages.Monotone_Usage, restriction)
					return
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Monotone_Usage, restriction)
				return
			}
			if (wasMonotoneUsed) {
				showWarning(TypeRestrictionsErrorMessages.Monotone_Already_Used, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Monotone_Has_Args, restriction)
		}
	}

	override handleSingletonRestriction(Restriction restriction, Declaration declaration) {
		if (restriction.restrictionArguments.size == 0) {
			if (underlyingUsertype != null) {
				if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) {
					showError(TypeRestrictionsErrorMessages.Singleton_Usage, restriction)
					return
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Singleton_Usage, restriction)
				return
			}
			if (wasSingletonUsed) {
				showWarning(TypeRestrictionsErrorMessages.Singleton_Already_Used, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Singleton_Has_Args, restriction)
		}
	}

	override handleUniqueRestriction(Restriction restriction, Declaration declaration) {
		if (restriction.restrictionArguments.size == 0) {
			if (underlyingUsertype != null) {
				if (underlyingUsertype.supertypes.size != 0) {
					showError(TypeRestrictionsErrorMessages.Unique_Usage, restriction)
					return
				} else if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) {
					showError(TypeRestrictionsErrorMessages.Unique_Usage, restriction)
					return
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Unique_Usage, restriction)
				return
			}
			if (wasUniqueUsed) {
				showWarning(TypeRestrictionsErrorMessages.Unique_Already_Used, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Unique_Has_Args, restriction)
		}
	}

	override handleDefaultRestriction(Restriction restriction, Declaration declaration) {
		if (!wasDefaultUsed) {
			if (restriction.restrictionArguments.size() == 1) {
				if (underlyingUsertype != null) {
					val restrictionArgument = restriction.restrictionArguments.get(0)
					if (restrictionArgument.valueType == null) {
						showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
					} else {
						val restrictionArgumentType = (restrictionArgument.valueType).type
						if (restrictionArgumentType instanceof Usertype) {
							if (!isUsertypeValidDefaultArgument(restrictionArgumentType, underlyingUsertype)) {
								showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
							}
						} else if (restrictionArgumentType instanceof Typedef) {
							if (!isTypedefValidDefaultArgument(restrictionArgumentType, underlyingUsertype)) {
								showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
							}
						} else {
							showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
						}
					}
				} else {
					if ((declaration as Typedef).fieldtype instanceof Booleantype) {
						if (restriction.restrictionArguments.get(0).valueBoolean == null) {
							showError(FieldRestrictionErrorMessages.Default_Arg_Not_Boolean, restriction)
						}
						return
					}
					if ((declaration as Typedef).fieldtype instanceof Stringtype) {
						if (restriction.restrictionArguments.get(0).valueString == null) {
							showError(FieldRestrictionErrorMessages.Default_Arg_Not_String, restriction)
						}
						return
					}
					if ((declaration as Typedef).fieldtype instanceof Floattype) {
						if (restriction.restrictionArguments.get(0).valueDouble == null) {
							showError(FieldRestrictionErrorMessages.Default_Arg_Not_Float, restriction)
							return
						}
						var double maxValue = 0
						switch ((restriction.restrictionArguments.get(0).valueType as Floattype).type) {
							case F32: maxValue = Float.MAX_VALUE
							default: maxValue = Double.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueDouble < -maxValue ||
							restriction.restrictionArguments.get(0).valueDouble > maxValue) {
							showError(FieldRestrictionErrorMessages.Default_Boundary, restriction)
							return
						}
						return
					}
					if ((declaration as Typedef).fieldtype instanceof Integertype) {
						if (restriction.restrictionArguments.get(0).valueLong == null) {
							showError(FieldRestrictionErrorMessages.Default_Arg_Not_Integer, restriction)
							return
						}
						var long maxValue = 0
						switch ((restriction.restrictionArguments.get(0).valueType as Integertype).type) {
							case I8: maxValue = Byte.MAX_VALUE
							case I16: maxValue = Short.MAX_VALUE
							case I32: maxValue = Integer.MAX_VALUE
							case I64: maxValue = Long.MAX_VALUE
							default: maxValue = Long.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueLong < -maxValue - 1 ||
							restriction.restrictionArguments.get(0).valueLong > maxValue) {
							showError(FieldRestrictionErrorMessages.Default_Boundary, restriction)
							return
						}
						return
					}
					showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Default_Not_One_Arg, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Default_Already_Used, restriction)
		}
	}

	override handleRangeRestriction(Restriction restriction, Declaration declaration) {
		if ((declaration as Typedef).fieldtype instanceof Integertype) {
			if (restriction.restrictionArguments.size() == 2) {
				if (restriction.restrictionArguments.get(0).valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
					return
				}
				if (restriction.restrictionArguments.get(1).valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
					return
				}
				if (restriction.restrictionArguments.get(0).valueLong >
					restriction.restrictionArguments.get(1).valueLong) {
					showError(FieldRestrictionErrorMessages.Range_Arguments_Switched, restriction)
				} else {
					var long maxValue = 0
					switch (((declaration as Typedef).fieldtype as Integertype).type) {
						case I8: maxValue = Byte.MAX_VALUE
						case I16: maxValue = Short.MAX_VALUE
						case I32: maxValue = Integer.MAX_VALUE
						case I64: maxValue = Long.MAX_VALUE
						default: maxValue = Long.MAX_VALUE
					}
					if (restriction.restrictionArguments.get(1).valueLong < -maxValue - 1 ||
						restriction.restrictionArguments.get(0).valueLong > maxValue) {
						showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
					} else if (restriction.restrictionArguments.get(0).valueLong < -maxValue - 1 ||
						restriction.restrictionArguments.get(1).valueLong > maxValue) {
						showWarning(FieldRestrictionErrorMessages.Range_Partly_Outside, restriction)
					}
				}
			} else if (restriction.restrictionArguments.size() == 4) {
				if (restriction.restrictionArguments.get(0).valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
					return
				}
				if (restriction.restrictionArguments.get(1).valueLong == null) {
					showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
					return
				}
				if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
					restriction.restrictionArguments.get(2).valueString)) {
					showError(FieldRestrictionErrorMessages.Range_Third_Arg_Not_Lowercase_String, restriction)
					return
				}
				if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
					restriction.restrictionArguments.get(3).valueString)) {
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
				if (restriction.restrictionArguments.get(0).valueLong >
					restriction.restrictionArguments.get(1).valueLong) {
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
					switch (((declaration as Typedef).fieldtype as Integertype).type) {
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
			} else if ((declaration as Typedef).fieldtype instanceof Floattype) {
				if (restriction.restrictionArguments.size() == 2) {
					if (restriction.restrictionArguments.get(0).valueDouble == null) {
						showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
						return
					}
					if (restriction.restrictionArguments.get(1).valueDouble == null) {
						showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
						return
					}
					if (restriction.restrictionArguments.get(0).valueDouble.doubleValue >
						restriction.restrictionArguments.get(1).valueDouble.doubleValue) {
						showError(FieldRestrictionErrorMessages.Range_Arguments_Switched, restriction)
					} else {
						var double maxValue = 0
						switch (((declaration as Typedef).fieldtype as Floattype).type) {
							case F32: maxValue = Float.MAX_VALUE
							default: maxValue = Double.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(1).valueDouble.doubleValue < -maxValue ||
							restriction.restrictionArguments.get(0).valueDouble.doubleValue > maxValue) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
						} else if (restriction.restrictionArguments.get(0).valueDouble.doubleValue < -maxValue ||
							restriction.restrictionArguments.get(1).valueDouble.doubleValue > maxValue) {
							showWarning(FieldRestrictionErrorMessages.Range_Partly_Outside, restriction)
						}
					}

				} else if (restriction.restrictionArguments.size() == 4) {
					if (restriction.restrictionArguments.get(0).valueDouble == null) {
						showError(FieldRestrictionErrorMessages.Range_First_Arg_Not_Integer, restriction)
						return
					}
					if (restriction.restrictionArguments.get(1).valueDouble == null) {
						showError(FieldRestrictionErrorMessages.Range_Second_Arg_Not_Integer, restriction)
						return
					}
					if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
						restriction.restrictionArguments.get(2).valueString)) {
						showError(FieldRestrictionErrorMessages.Range_Third_Arg_Not_Lowercase_String, restriction)
						return
					}
					if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
						restriction.restrictionArguments.get(3).valueString)) {
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
					if (restriction.restrictionArguments.get(0).valueDouble.doubleValue >
						restriction.restrictionArguments.get(1).valueDouble.doubleValue) {
						showError(FieldRestrictionErrorMessages.Range_Arguments_Switched, restriction)
						return
					}
					if (restriction.restrictionArguments.get(0).valueDouble.doubleValue ==
						restriction.restrictionArguments.get(1).valueDouble.doubleValue &&
						(restriction.restrictionArguments.get(2).valueString.toLowerCase.contains("ex") ||
							restriction.restrictionArguments.get(3).valueString.toLowerCase.contains("ex"))) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						var double maxValue = 0
						switch (((declaration as Typedef).fieldtype as Floattype).type) {
							case F32: maxValue = Float.MAX_VALUE
							default: maxValue = Double.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(1).valueDouble.doubleValue < -maxValue ||
							restriction.restrictionArguments.get(0).valueDouble.doubleValue > maxValue) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue < -maxValue ||
							restriction.restrictionArguments.get(1).valueDouble.doubleValue > maxValue) {
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
				} else {
					showError(FieldRestrictionErrorMessages.Range_Usage, restriction)
				}
			}

			override handleMinRestriction(Restriction restriction, Declaration declaration) {
				if ((declaration as Typedef).fieldtype instanceof Integertype) {
					if (restriction.restrictionArguments.size() == 1) {
						if (restriction.restrictionArguments.get(0).valueLong == null) {
							showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Integer, restriction)
							return
						}
						var long maxValue = 0
						switch (((declaration as Typedef).fieldtype as Integertype).type) {
							case I8: maxValue = Byte.MAX_VALUE
							case I16: maxValue = Short.MAX_VALUE
							case I32: maxValue = Integer.MAX_VALUE
							case I64: maxValue = Long.MAX_VALUE
							default: maxValue = Long.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueLong.longValue > maxValue) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						if (restriction.restrictionArguments.get(0).valueLong.longValue < -maxValue) {
							showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
							return
						}

					} else if (restriction.restrictionArguments.size() == 2) {
						if (restriction.restrictionArguments.get(0).valueLong == null) {
							showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Integer, restriction)
							return
						}
						if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
							restriction.restrictionArguments.get(1).valueString)) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Not_Lowercase_String, restriction)
							return
						}
						if (!restriction.restrictionArguments.get(1).valueString.toLowerCase.matches(
							"(\\s)*(inclusive|exclusive)(\\s)*")) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Wrong_String, restriction)
							return
						}
						var long maxValue = 0
						switch (((declaration as Typedef).fieldtype as Integertype).type) {
							case I8: maxValue = Byte.MAX_VALUE
							case I16: maxValue = Short.MAX_VALUE
							case I32: maxValue = Integer.MAX_VALUE
							case I64: maxValue = Long.MAX_VALUE
							default: maxValue = Long.MAX_VALUE
						}
						var notAsHigh = 0
						if(restriction.restrictionArguments.get(1).valueString.toLowerCase.contains("ex")) notAsHigh = 1
						if (restriction.restrictionArguments.get(0).valueLong.longValue > maxValue - notAsHigh) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						if (restriction.restrictionArguments.get(0).valueLong.longValue < -maxValue - notAsHigh) {
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
				} else if ((declaration as Typedef).fieldtype instanceof Floattype) {
					if (restriction.restrictionArguments.size() == 1) {
						if (restriction.restrictionArguments.get(0).valueDouble == null) {
							showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Float, restriction)
							return
						}
						var double maxValue = 0
						switch (((declaration as Typedef).fieldtype as Floattype).type) {
							case F32: maxValue = Float.MAX_VALUE
							default: maxValue = Double.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue > maxValue) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue < -maxValue) {
							showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
							return
						}

					} else if (restriction.restrictionArguments.size() == 2) {
						if (restriction.restrictionArguments.get(0).valueDouble == null) {
							showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Float, restriction)
							return
						}
						if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
							restriction.restrictionArguments.get(1).valueString)) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Not_Lowercase_String, restriction)
							return
						}
						if (!restriction.restrictionArguments.get(1).valueString.toLowerCase.matches(
							"(\\s)*(inclusive|exclusive)(\\s)*")) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Wrong_String, restriction)
							return
						}
						var double maxValue = 0
						switch (((declaration as Typedef).fieldtype as Floattype).type) {
							case F32: maxValue = Float.MAX_VALUE
							default: maxValue = Double.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue > maxValue) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						} else if (restriction.restrictionArguments.get(0).valueDouble.doubleValue < -maxValue) {
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
				} else {
					showError(FieldRestrictionErrorMessages.MinMax_Usage, restriction)
				}
			}

			override handleMaxRestriction(Restriction restriction, Declaration declaration) {
				if ((declaration as Typedef).fieldtype instanceof Integertype) {
					if (restriction.restrictionArguments.size() == 1) {
						if (restriction.restrictionArguments.get(0).valueLong == null) {
							showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Integer, restriction)
							return
						}
						var long maxValue = 0
						switch (((declaration as Typedef).fieldtype as Integertype).type) {
							case I8: maxValue = Byte.MAX_VALUE
							case I16: maxValue = Short.MAX_VALUE
							case I32: maxValue = Integer.MAX_VALUE
							case I64: maxValue = Long.MAX_VALUE
							default: maxValue = Long.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueLong.longValue < -maxValue - 1) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						} else if (restriction.restrictionArguments.get(0).valueLong.longValue > maxValue - 1) {
							showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
							return
						}
					} else if (restriction.restrictionArguments.size() == 2) {
						if (restriction.restrictionArguments.get(0).valueLong == null) {
							showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Integer, restriction)
							return
						}
						if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
							restriction.restrictionArguments.get(1).valueString)) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Not_Lowercase_String, restriction)
							return
						}
						if (!restriction.restrictionArguments.get(1).valueString.toLowerCase.matches(
							"(\\s)*(inclusive|exclusive)(\\s)*")) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Wrong_String, restriction)
							return
						}
						var long maxValue = 0
						switch (((declaration as Typedef).fieldtype as Integertype).type) {
							case I8: maxValue = Byte.MAX_VALUE
							case I16: maxValue = Short.MAX_VALUE
							case I32: maxValue = Integer.MAX_VALUE
							case I64: maxValue = Long.MAX_VALUE
							default: maxValue = Long.MAX_VALUE
						}
						var notAsLow = 0
						if(restriction.restrictionArguments.get(1).valueString.toLowerCase.contains("ex")) notAsLow = 1
						if (restriction.restrictionArguments.get(0).valueLong.longValue < -maxValue - 1 + notAsLow) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						if (restriction.restrictionArguments.get(0).valueLong.longValue > maxValue + notAsLow - 1) {
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
				} else if ((declaration as Typedef).fieldtype instanceof Floattype) {
					if (restriction.restrictionArguments.size() == 1) {
						if (restriction.restrictionArguments.get(0).valueDouble == null) {
							showError(FieldRestrictionErrorMessages.MinMax_Arg_Not_Float, restriction)
							return
						}
						var double maxValue = 0
						switch (((declaration as Typedef).fieldtype as Floattype).type) {
							case F32: maxValue = Float.MAX_VALUE
							default: maxValue = Double.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue < -maxValue) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue > maxValue) {
							showWarning(FieldRestrictionErrorMessages.MinMax_Partly_Outside, restriction)
							return
						}

					} else if (restriction.restrictionArguments.size() == 2) {
						if (restriction.restrictionArguments.get(0).valueDouble == null) {
							showError(FieldRestrictionErrorMessages.MinMax_First_Arg_Not_Float, restriction)
							return
						}
						if (!AbstractFieldRestrictionsValidator.isStringNullOrLowercase(
							restriction.restrictionArguments.get(1).valueString)) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Not_Lowercase_String, restriction)
							return
						}
						if (!restriction.restrictionArguments.get(1).valueString.toLowerCase.matches(
							"(\\s)*(inclusive|exclusive)(\\s)*")) {
							showError(FieldRestrictionErrorMessages.MinMax_Second_Arg_Wrong_String, restriction)
							return
						}
						var double maxValue = 0
						switch (((declaration as Typedef).fieldtype as Floattype).type) {
							case F32: maxValue = Float.MAX_VALUE
							default: maxValue = Double.MAX_VALUE
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue < -maxValue) {
							showError(FieldRestrictionErrorMessages.Range_Outside, restriction)
							return
						}
						if (restriction.restrictionArguments.get(0).valueDouble.doubleValue > maxValue) {
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
				} else {
					showError(FieldRestrictionErrorMessages.MinMax_Usage, restriction)
				}
			}

		}