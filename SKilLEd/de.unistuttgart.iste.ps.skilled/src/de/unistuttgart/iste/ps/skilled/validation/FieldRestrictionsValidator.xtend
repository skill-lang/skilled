package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype
import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype

/** 
 * Validates field restrictions and their arguments. <br>
 * <br>
 * Supported restrictions: NonNull, Default, Range (Min, Max), Coding, ConstantLengthPointer, OneOf <br>
 * <br>
 * The error/warning messages give context to the conditions. <br>
 * <br>
 * Guide to restriction arguments (parameters): <br>
 * restrictionArgument.valueType 	- a declaration (typedef, enum, interface, usertype) type argument <br>
 * restrictionArgument.valueString 	- a string type argument <br>
 * restrictionArgument.valueLong 	- a integer type argument <br>
 * restrictionArgument.valueDouble	- a float type argument <br>
 * 
 * 
 * @author Nikolay Fateev
 */
class FieldRestrictionsValidator extends AbstractSKilLValidator {

	// All restriction warning and error messages
	private final static String Unknown_Restriction = "Unknown Restriction"
	private final static String Restriction_On_Map = "Map-typed fields can't have restrictions."
	// NonNull restriction error messages
	private final static String NonNull_Has_Args = "The Non Null restriction can't have arguments."
	private final static String NonNull_Usage = "The Non Null restriction can only be used on strings and user-types."
	// NonNull restriction warning messages
	private final static String NonNull_Already_Used = "The Non Null restriction is already used on this field."
	// Default restriction error messages
	private final static String Default_Arg_Not_Singleton = "Restriction argument must be a singleton restricted sub-type."
	private final static String Default_Arg_Not_Singleton_Or_Enum = "Restriction argument must be an enum or a singleton restricted type."
	private final static String Default_Arg_Not_Integer = "Restriction argument must be an integer."
	private final static String Default_Arg_Not_Float = "Restriction argument must be a float."
	private final static String Default_Arg_Not_String = "Restriction argument must be a string."
	private final static String Default_Not_One_Arg = "The Default restriction must have one argument."
	private final static String Default_Usage = "The Default restriction can only be used on strings, integers, floats, annotations and user-types."
	// Default restriction warning messages
	private final static String Default_Already_Used = "The Default restriction is already used on this field."
	// Range restriction error messages
	private final static String Range_First_Arg_Not_Integer = "The first argument must be an integer."
	private final static String Range_First_Arg_Not_Float = "The first argument must be an float."
	private final static String Range_Second_Arg_Not_Integer = "The second argument must be an integer."
	private final static String Range_Second_Arg_Not_Float = "The second argument must be an float."
	private final static String Range_Third_Arg_Not_Lowercase_String = "The third argument must be a lowercase string."
	private final static String Range_Fourth_Arg_Not_Lowercase_String = "The fourth argument must be a lowercase string."
	private final static String Range_Not_2or4_Args = "The Range restriction must have two or four arguments."
	private final static String Range_Usage = "The Range restriction can only be used on integers and floats."
	// Range restriction warning messages
	private final static String Range_Multiple_Used = "The use of multiple Range restrictions is highly discouraged. The correctness of the intersection is not checked."
	// Max restriction error messages
	private final static String Max_Arg_Not_Integer = "Restriction argument must be an integer."
	private final static String Max_Arg_Not_Float = "Restriction argument must be a float."
	private final static String Max_First_Arg_Not_Integer = "The first argument must be an integer."
	private final static String Max_First_Arg_Not_Float = "The first argument must be an float."
	private final static String Max_Second_Arg_Not_Lowercase_String = "The second argument must be a lowercase string."
	private final static String Max_Not_1or2_Args = "The Max restriction must have one or two arguments."
	private final static String Max_Usage = "The Max restriction can only be used on integers and floats."
	// Max restriction warning messages
	private final static String Max_Multiple_Used = "The use of multiple Max restrictions is highly discouraged. The correctness of the intersection is not checked."
	// Min restriction error messages
	private final static String Min_Arg_Not_Integer = "Restriction argument must be an integer."
	private final static String Min_Arg_Not_Float = "Restriction argument must be a float."
	private final static String Min_First_Arg_Not_Integer = "The first argument must be an integer."
	private final static String Min_First_Arg_Not_Float = "The first argument must be an float."
	private final static String Min_Second_Arg_Not_Lowercase_String = "The second argument must be a lowercase string."
	private final static String Min_Not_1or2_Args = "The Min restriction must have one or two arguments."
	private final static String Min_Usage = "The Min restriction can only be used on integers and floats."
	// Min restriction warning messages
	private final static String Min_Multiple_Used = "The use of multiple Min restrictions is highly discouraged. The correctness of the intersection is not checked."
	// Coding restriction error messages
	private final static String Coding_Arg_Not_String = "The Coding restriction argument value must be a string."
	private final static String Coding_Not_One_Arg = "The Coding restriction must have one argument."
	// Coding restriction warning messages
	private final static String Coding_Usage_Discouraged = "The use of the Coding restriction is discouraged."
	// ConstantLengthPointer restriction error messages
	private final static String ConstantLengthPointer_Has_Args = "The Constant Length Pointer restriction can't have arguments."
	private final static String ConstantLengthPointer_Usage = "The Constant Length Pointer restriction can only be used on strings, annotations and user-types."
	// ConstantLengthPointer restriction warning messages
	private final static String ConstantLengthPointer_Already_Used = "The Constant Length Pointer restriction is already used on this field."
	// OneOf restriction error messages
	private final static String OneOf_Arg_Not_Usertype = "Restriction argument must be a user-type."
	private final static String OneOf_Not_One_Arg = "The One Of restriction requires at least one argument."
	private final static String OneOf_Usage = "The One Of restriction can only be used on annotations and user-types."
	// OneOf restriction warning messages
	private final static String OneOf_Already_Used = "The One Of restriction is already used on this field."

	@Check
	def validateFieldRestrions(Field field) {
		val fieldType = field.fieldcontent.fieldtype
		if (fieldType instanceof DeclarationReference) { // User-type
			validateUserTypeField(field)
		} else if (fieldType instanceof Stringtype) {
			validateStringTypeField(field)
		} else if (fieldType instanceof Integertype || fieldType instanceof Floattype) {
			validateNumericTypeField(field)
		} else if (fieldType instanceof Annotationtype) {
			validateAnnotationTypeField(field)
		} else if (fieldType instanceof Booleantype) {
			validateBooleanTypeField(field)
		} else { // Maps, Sets, Arrays, Lists
			validateContainerTypeField(field)
		}
	}

	def validateUserTypeField(Field field) {
		val fieldTypeName = (field.fieldcontent.fieldtype as DeclarationReference).type.name
		var wasNonNullUsed = false
		var wasDefaultUsed = false
		var wasConstantLenghtPointerUsed = false
		var wasOneOfUsed = false
		for (restriction : field.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'nonnull': {
					if (restriction.restrictionArguments.size() != 0) {
						showError(NonNull_Has_Args, restriction)
					} else if (wasNonNullUsed) {
						showWarning(NonNull_Already_Used, restriction)
					}
					wasNonNullUsed = true
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							val restrictionArgument = restriction.restrictionArguments.get(0)
							if (restrictionArgument.valueType == null) {
								showError(Default_Arg_Not_Singleton, restriction)
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
									if (!isRestrictionArgumentSubTypeOfField ||
										!isRestrictionArgumentTypeSingletonRestricted) {
										showError(Default_Arg_Not_Singleton, restriction)
									}
								} else if (restrictionArgumentType instanceof Enumtype) {
								} else {
									showError(Default_Arg_Not_Singleton, restriction)
								}
							}
						} else {
							showError(Default_Not_One_Arg, restriction)
						}
						wasDefaultUsed = true
					} else {
						showError(Default_Already_Used, restriction)
					}
				}
				case 'range': {
					showError(Range_Usage, restriction)
				}
				case 'max': {
					showError(Max_Usage, restriction)
				}
				case 'min': {
					showError(Min_Usage, restriction)
				}
				case 'coding': {
					if (restriction.restrictionArguments.size() == 1) {
						val argument = restriction.restrictionArguments.get(0)
						if (argument.valueString == null) {
							showError(Coding_Arg_Not_String, restriction)
						} else {
							showWarning(Coding_Usage_Discouraged, restriction)
						}
					} else {
						showError(Coding_Not_One_Arg, restriction)
					}
				}
				case 'constantlengthpointer': {
					if (restriction.restrictionArguments.size() != 0) {
						showError(ConstantLengthPointer_Has_Args, restriction)
					} else if (wasConstantLenghtPointerUsed) {
						showWarning(ConstantLengthPointer_Already_Used, restriction)
					}
					wasConstantLenghtPointerUsed = true
				}
				case 'oneof': {
					if (!wasOneOfUsed) {
						if (restriction.restrictionArguments.size() >= 1) {
							for (restrictionArgument : restriction.restrictionArguments) {
								if (restrictionArgument.valueType == null) {
									showError(OneOf_Arg_Not_Usertype, restriction)
								}
							}
						} else {
							showError(OneOf_Not_One_Arg, restriction)
						}
						wasOneOfUsed = true
					} else {
						showError(OneOf_Already_Used, restriction)
					}
				}
				default: {
					showError(Unknown_Restriction, restriction)
				}
			}
		}
	}

	def validateStringTypeField(Field field) {
		var wasNonNullUsed = false
		var wasDefaultUsed = false
		var wasConstantLenghtPointerUsed = false
		for (restriction : field.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'nonnull': {
					if (restriction.restrictionArguments.size() != 0) {
						showError(NonNull_Has_Args, restriction)
					} else if (wasNonNullUsed) {
						showWarning(NonNull_Already_Used, restriction)
					}
					wasNonNullUsed = true
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							val restrictionArgument = restriction.restrictionArguments.get(0)
							if (restrictionArgument.valueString == null) {
								if (restrictionArgument.valueType != null) {
									val restrictionArgumentType = (restrictionArgument.valueType).type
									if (restrictionArgumentType instanceof Typedef) {
										if (!(restrictionArgumentType.fieldtype instanceof Stringtype)) {
											showError(Default_Arg_Not_String, restriction)
										}
									} else {
										showError(Default_Arg_Not_String, restriction)
									}
								} else {
									showError(Default_Arg_Not_String, restriction)
								}
							}
						} else {
							showError(Default_Not_One_Arg, restriction)
						}
						wasDefaultUsed = true
					} else {
						showError(Default_Already_Used, restriction)
					}
				}
				case 'range': {
					showError(Range_Usage, restriction)
				}
				case 'max': {
					showError(Max_Usage, restriction)
				}
				case 'min': {
					showError(Min_Usage, restriction)
				}
				case 'coding': {
					if (restriction.restrictionArguments.size() == 1) {
						val argument = restriction.restrictionArguments.get(0)
						if (argument.valueString == null) {
							showError(Coding_Arg_Not_String, restriction)
						} else {
							showWarning(Coding_Usage_Discouraged, restriction)
						}
					} else {
						showError(Coding_Not_One_Arg, restriction)
					}
				}
				case 'constantlengthpointer': {
					if (restriction.restrictionArguments.size() != 0) {
						showError(ConstantLengthPointer_Has_Args, restriction)
					} else if (wasConstantLenghtPointerUsed) {
						showWarning(ConstantLengthPointer_Already_Used, restriction)
					}
					wasConstantLenghtPointerUsed = true
				}
				case 'oneof': {
					showError(OneOf_Usage, restriction)
				}
				default: {
					showError(Unknown_Restriction, restriction)
				}
			}
		}
	}

	def validateNumericTypeField(Field field) {
		val fieldType = field.fieldcontent.fieldtype
		var wasDefaultUsed = false
		var rangeUsed = false
		var maxUsed = false
		var minUsed = false
		for (restriction : field.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'nonnull': {
					showError(NonNull_Usage, restriction)
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							val restrictionArgument = restriction.restrictionArguments.get(0)
							if (fieldType instanceof Integertype) {
								if (restrictionArgument.valueLong == null) {
									showError(Default_Arg_Not_Integer, restriction)
								} else if (restrictionArgument.valueType != null) {
									val restrictionArgumentType = (restrictionArgument.valueType).type
									if (restrictionArgumentType instanceof Typedef) {
									}
								}
							} else if (fieldType instanceof Floattype) {
								if (restrictionArgument.valueDouble == null) {
									showError(Default_Arg_Not_Float, restriction)
								}
							}
						}
					}
				}
				case 'range': {
					var errorFound = false
					var restrictionArguments = restriction.restrictionArguments
					if (restrictionArguments.size() == 2) {
						val argument1 = restrictionArguments.get(0)
						val argument2 = restrictionArguments.get(1)
						if (fieldType instanceof Integertype) {
							if (argument1.valueLong == null) {
								showError(Range_First_Arg_Not_Integer, restriction)
								errorFound = true
							} else if (argument2.valueLong == null) {
								showError(Range_Second_Arg_Not_Integer, restriction)
								errorFound = true
							}
						} else if (fieldType instanceof Floattype) {
							if (argument1.valueDouble == null) {
								showError(Range_First_Arg_Not_Float, restriction)
								errorFound = true
							} else if (argument2.valueDouble == null) {
								showError(Range_Second_Arg_Not_Float, restriction)
								errorFound = true
							}
						}
					} else if (restrictionArguments.size() == 4) {
						val argument1 = restrictionArguments.get(0)
						val argument2 = restrictionArguments.get(1)
						val argument3 = restrictionArguments.get(2)
						val argument4 = restrictionArguments.get(3)
						if (fieldType instanceof Integertype) {
							if (argument1.valueLong == null) {
								showError(Range_First_Arg_Not_Integer, restriction)
								errorFound = true
							} else if (argument2.valueLong == null) {
								showError(Range_Second_Arg_Not_Integer, restriction)
								errorFound = true
							} else if (!isStringNullOrLowercase(argument3.valueString)) {
								showError(FieldRestrictionsValidator.Range_Third_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							} else if (!isStringNullOrLowercase(argument4.valueString)) {
								showError(FieldRestrictionsValidator.Range_Fourth_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							}
						} else if (fieldType instanceof Floattype) {
							if (argument1.valueDouble == null) {
								showError(Range_First_Arg_Not_Float, restriction)
								errorFound = true
							} else if (argument2.valueDouble == null) {
								showError(Range_Second_Arg_Not_Float, restriction)
								errorFound = true
							} else if (argument3.valueString == null) {
								showError(FieldRestrictionsValidator.Range_Third_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							} else if (argument4.valueString == null) {
								showError(FieldRestrictionsValidator.Range_Fourth_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							}
						}
					} else {
						showError(Range_Not_2or4_Args, restriction)
						errorFound = true
					}
					if (!errorFound && rangeUsed) {
						showWarning(Range_Multiple_Used, restriction)
					}
					rangeUsed = true
				}
				case 'max': {
					var errorFound = false
					var restrictionArguments = restriction.restrictionArguments
					if (restrictionArguments.size() == 1) {
						val argument1 = restrictionArguments.get(0)
						if (fieldType instanceof Integertype) {
							if (argument1.valueLong == null) {
								showError(Max_Arg_Not_Integer, restriction)
								errorFound = true
							}
						} else if (fieldType instanceof Floattype) {
							if (argument1.valueDouble == null) {
								showError(Max_Arg_Not_Float, restriction)
								errorFound = true
							}
						}
					} else if (restrictionArguments.size() == 2) {
						val argument1 = restrictionArguments.get(0)
						val argument2 = restrictionArguments.get(1)
						if (fieldType instanceof Integertype) {
							if (argument1.valueLong == null) {
								showError(Max_First_Arg_Not_Integer, restriction)
								errorFound = true
							} else if (!isStringNullOrLowercase(argument2.valueString)) {
								showError(FieldRestrictionsValidator.Max_Second_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							}
						} else if (fieldType instanceof Floattype) {
							if (argument1.valueDouble == null) {
								showError(Max_First_Arg_Not_Float, restriction)
								errorFound = true
							} else if (!isStringNullOrLowercase(argument2.valueString)) {
								showError(FieldRestrictionsValidator.Max_Second_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							}
						}
					} else {
						showError(Max_Not_1or2_Args, restriction)
						errorFound = true
					}
					if (!errorFound && maxUsed) {
						showWarning(Max_Multiple_Used, restriction)
					}
					maxUsed = true
				}
				case 'min': {
					var errorFound = false
					val restrictionArguments = restriction.restrictionArguments
					if (restrictionArguments.size() == 1) {
						val argument1 = restrictionArguments.get(0)
						if (fieldType instanceof Integertype) {
							if (argument1.valueLong == null) {
								showError(Min_Arg_Not_Integer, restriction)
								errorFound = true
							}
						} else if (fieldType instanceof Floattype) {
							if (argument1.valueDouble == null) {
								showError(Min_Arg_Not_Float, restriction)
								errorFound = true
							}
						}
					} else if (restrictionArguments.size() == 2) {
						val argument1 = restrictionArguments.get(0)
						val argument2 = restrictionArguments.get(1)
						if (fieldType instanceof Integertype) {
							if (argument1.valueLong == null) {
								showError(Min_First_Arg_Not_Integer, restriction)
								errorFound = true
							} else if (!isStringNullOrLowercase(argument2.valueString)) {
								showError(FieldRestrictionsValidator.Min_Second_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							}
						} else if (fieldType instanceof Floattype) {
							if (argument1.valueDouble == null) {
								showError(Min_First_Arg_Not_Float, restriction)
								errorFound = true
							} else if (!isStringNullOrLowercase(argument2.valueString)) {
								showError(FieldRestrictionsValidator.Min_Second_Arg_Not_Lowercase_String, restriction)
								errorFound = true
							}
						}
					} else {
						showError(Min_Not_1or2_Args, restriction)
						errorFound = true
					}
					if (!errorFound && minUsed) {
						showWarning(Min_Multiple_Used, restriction)
					}
					minUsed = true
				}
				case 'coding': {
					if (restriction.restrictionArguments.size() == 1) {
						val argument = restriction.restrictionArguments.get(0)
						if (argument.valueString == null) {
							showError(Coding_Arg_Not_String, restriction)
						} else {
							showWarning(Coding_Usage_Discouraged, restriction)
						}
					} else {
						showError(Coding_Not_One_Arg, restriction)
					}
				}
				case 'constantlengthpointer': {
					showError(ConstantLengthPointer_Usage, restriction)
				}
				case 'oneof': {
					showError(OneOf_Usage, restriction)
				}
				default: {
					showError(Unknown_Restriction, restriction)
				}
			}
		}
	}

	def validateAnnotationTypeField(Field field) {
		var wasDefaultUsed = false
		var wasConstantLenghtPointerUsed = false
		var wasOneOfUsed = false
		for (restriction : field.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'nonnull': {
					showError(NonNull_Usage, restriction)
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							val restrictionArgument = restriction.restrictionArguments.get(0)
							if (restrictionArgument.valueType == null) {
								showError(Default_Arg_Not_Singleton_Or_Enum, restriction)
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
										showError(Default_Arg_Not_Singleton_Or_Enum, restriction)
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
										showError(Default_Arg_Not_Singleton_Or_Enum, restriction)
									}
								} else if (restrictionArgumentType instanceof Interfacetype) {
									showError(Default_Arg_Not_Singleton_Or_Enum, restriction)
								}
							}
						} else {
							showError(Default_Not_One_Arg, restriction)
						}
						wasDefaultUsed = true
					} else {
						showError(Default_Already_Used, restriction)
					}
				}
				case 'range': {
					showError(Range_Usage, restriction)
				}
				case 'max': {
					showError(Max_Usage, restriction)
				}
				case 'min': {
					showError(Min_Usage, restriction)
				}
				case 'coding': {
					if (restriction.restrictionArguments.size() == 1) {
						val argument = restriction.restrictionArguments.get(0)
						if (argument.valueString == null) {
							showError(Coding_Arg_Not_String, restriction)
						} else {
							showWarning(Coding_Usage_Discouraged, restriction)
						}
					} else {
						showError(Coding_Not_One_Arg, restriction)
					}
				}
				case 'constantlengthpointer': {
					if (restriction.restrictionArguments.size() != 0) {
						showError(ConstantLengthPointer_Has_Args, restriction)
					} else if (wasConstantLenghtPointerUsed) {
						showWarning(ConstantLengthPointer_Already_Used, restriction)
					}
					wasConstantLenghtPointerUsed = true
				}
				case 'oneof': {
					if (!wasOneOfUsed) {
						if (restriction.restrictionArguments.size() >= 1) {
							for (restrictionArgument : restriction.restrictionArguments) {
								if (restrictionArgument.valueType == null) {
									showError(OneOf_Arg_Not_Usertype, restriction)
								}
							}
						} else {
							showError(OneOf_Not_One_Arg, restriction)
						}
						wasOneOfUsed = true
					} else {
						showError(OneOf_Already_Used, restriction)
					}
				}
				default: {
					showError(Unknown_Restriction, restriction)
				}
			}
		}
	}

	def validateBooleanTypeField(Field field) {
		for (restriction : field.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'nonnull': {
					showError(NonNull_Usage, restriction)
				}
				case 'default': {
					showError(Default_Usage, restriction)
				}
				case 'range': {
					showError(Range_Usage, restriction)
				}
				case 'max': {
					showError(Max_Usage, restriction)
				}
				case 'min': {
					showError(Min_Usage, restriction)
				}
				case 'coding': {
					if (restriction.restrictionArguments.size() == 1) {
						val argument = restriction.restrictionArguments.get(0)
						if (argument.valueString == null) {
							showError(Coding_Arg_Not_String, restriction)
						} else {
							showWarning(Coding_Usage_Discouraged, restriction)
						}
					} else {
						showError(Coding_Not_One_Arg, restriction)
					}
				}
				case 'constantlengthpointer': {
					showError(ConstantLengthPointer_Usage, restriction)
				}
				case 'oneof': {
					showError(OneOf_Usage, restriction)
				}
				default: {
					showError(Unknown_Restriction, restriction)
				}
			}
		}
	}

	def validateContainerTypeField(Field field) {
		val fieldType = field.fieldcontent.fieldtype
		for (restriction : field.restrictions) {
			if (fieldType instanceof Maptype) {
				showError(Restriction_On_Map, restriction)
			} else {
				switch (restriction.restrictionName.toLowerCase) {
					case 'nonnull': {
						showError(NonNull_Usage, restriction)
					}
					case 'default': {
						showError(Default_Usage, restriction)
					}
					case 'range': {
						showError(Range_Usage, restriction)
					}
					case 'max': {
						showError(Max_Usage, restriction)
					}
					case 'min': {
						showError(Min_Usage, restriction)
					}
					case 'coding': {
						if (restriction.restrictionArguments.size() == 1) {
							val argument = restriction.restrictionArguments.get(0)
							if (argument.valueString == null) {
								showError(Coding_Arg_Not_String, restriction)
							} else {
								showWarning(Coding_Usage_Discouraged, restriction)
							}
						} else {
							showError(Coding_Not_One_Arg, restriction)
						}
					}
					case 'constantlengthpointer': {
						showError(ConstantLengthPointer_Usage, restriction)
					}
					case 'oneof': {
						showError(OneOf_Usage, restriction)
					}
					default: {
						showError(Unknown_Restriction, restriction)
					}
				}
			}
		}
	}

	def showError(String message, Restriction restriction) {
		error(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def showWarning(String message, Restriction restriction) {
		warning(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def boolean isStringNullOrLowercase(String string) {
		if (string == null || !string.equals(string.toLowerCase())) {
			return false;
		}
		return true;
	}
}