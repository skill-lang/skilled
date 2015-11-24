package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Listtype
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.RestrictionArgument
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Settype
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype
import de.unistuttgart.iste.ps.skilled.sKilL.BuiltInType
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype

/** 
 * This class contains the Validation rules for Cyclic Types. 
 * @author Nikolay Fateev
 */
class FieldRestrictionsValidator extends AbstractSKilLValidator {

	/*
	 * If field type is instance of DeclarationReference, it means that this is a user-type.
	 * The error message gives context to the if()s.
	 * 
	 */
	@Check
	def checkFieldRestrictions(Field field) {
		val fieldType = field.getFieldcontent().getFieldtype();
		var nonNullUsed = false;
		var defaultUsed = false;
//		var rangeUsed = false;
//		var maxUsed = false;
//		var minUsed = false;
//		var codingUsed = false;
		var constantLenghtPointerUsed = false;
//		var oneOfUsed = false;
		for (Restriction restriction : field.getRestrictions()) {
			if (fieldType instanceof Maptype) {
				error("Map-typed fields can't have restrictions.", restriction,
					SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
			} else {
				switch (restriction.restrictionName) {
					case 'nonnull': {
						var errorFound = false;
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype) {
							error("The Non Null restriction can only be used on strings and user-types.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							errorFound = true;
						} else if (fieldType instanceof BuiltInType) {
							if (fieldType instanceof Stringtype) {
								if (restriction.getRestrictionArguments().size() != 0) {
									error("The Non Null restriction can't have arguments.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
									errorFound = true;
								}
							} else {
								error("The Non Null restriction can only be used on strings and user-types.",
									restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								errorFound = true;
							}
						} else if (fieldType instanceof DeclarationReference) {
							if (restriction.getRestrictionArguments().size() != 0) {
								error("The Non Null restriction can't have arguments.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								errorFound = true;
							}
						}
						if (!errorFound && nonNullUsed) {
							warning("The Non Null restriction is already used on this field.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						}
						nonNullUsed = true;
					}
					case 'default': {
						if (!defaultUsed) {
							if (fieldType instanceof Settype || fieldType instanceof Listtype || fieldType instanceof Arraytype) {
								error("The Default restriction can only be used on strings, integers, floats, annotations and user-types.",
									restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							} else if (fieldType instanceof BuiltInType) {
								if (!(fieldType instanceof Booleantype)) {
									if (restriction.getRestrictionArguments().size() == 1) {
										val argument1 = restriction.getRestrictionArguments().get(0);
										if (fieldType instanceof Integertype) {
											if (argument1.valueLong == null) {
												error("Restriction argument must be an integer.", restriction,
													SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
											}
										} else if (fieldType instanceof Floattype) {
											if (argument1.valueDouble == null) {
												error("Restriction argument must be a float.", restriction,
													SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
											}
										} else if (fieldType instanceof Stringtype) {
											if (argument1.valueString == null) {
												error("Restriction argument must be a string.", restriction,
													SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
											}
										} 
										else if (fieldType instanceof Annotationtype) {
											if (argument1.valueType == null) {
												error("Restriction argument must be an enum or a singleton restricted type.", restriction,
													SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
											} else {
												val restrictionArgumentType = (argument1.valueType).type;
												if (restrictionArgumentType instanceof Interfacetype) {
													error("Restriction argument must be an enum or a singleton restricted type.", restriction,
														SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
												} else if (restrictionArgumentType instanceof Typedef) {
													val restrictions = restrictionArgumentType.restrictions;
													var isRestrictionArgumentSingletonRestricted = false;
													for (typedefRestriction : restrictions) {
														if ("singleton".equals(typedefRestriction.restrictionName)) {
															isRestrictionArgumentSingletonRestricted = true;
														}
													}
													if (!isRestrictionArgumentSingletonRestricted) {
														error("Restriction argument must be an enum or a singleton restricted type.", restriction,
															SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
													}
												} else if (restrictionArgumentType instanceof Usertype) {		//Yes, the logic here duplicates the typedef block.
													val restrictions = restrictionArgumentType.restrictions;
													var singletonFound = false;
													for (typedefRestriction : restrictions) {
														if ("singleton".equals(typedefRestriction.restrictionName)) {
															singletonFound = true;
														}
													}
													if (!singletonFound) {
														error("Restriction argument must be an enum or a singleton restricted type.", restriction,
															SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
													}
												}
											}
										}
									} else {
										error("The Default restriction requires one argument.", restriction,
											SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
									}
								} else {
									error("The Default restriction can only be used on strings, integers, floats, annotations and user-types.",
										restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								}
							} else if (fieldType instanceof DeclarationReference) {
								if (restriction.getRestrictionArguments().size() == 1) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									if (argument1.valueType == null) {
										error("Restriction argument must be a singleton restricted sub-type.", restriction,
											SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
									} else {
										val restrictionArgumentType = (argument1.valueType).type;
										if (restrictionArgumentType instanceof Interfacetype || restrictionArgumentType instanceof Enumtype || 
											restrictionArgumentType instanceof Typedef) {
											error("Restriction argument must be a singleton restricted sub-type.", restriction,
												SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (restrictionArgumentType instanceof Usertype) {
											val restrictionArgumentSupertypes = restrictionArgumentType.supertypes;
											var isRestrictionArgumentSubTypeOfField = false;
											for (supertype : restrictionArgumentSupertypes) {
												if ((fieldType.type.name).equals(supertype.type.name)) {
													isRestrictionArgumentSubTypeOfField = true;
												}
											}
											var isRestrictionArgumentTypeSingletonRestricted = false;
											if (isRestrictionArgumentSubTypeOfField) {
												val restrictions = restrictionArgumentType.restrictions;
												for (typedefRestriction : restrictions) {
													if ("singleton".equals(typedefRestriction.restrictionName)) {
														isRestrictionArgumentTypeSingletonRestricted = true;
													}
												}
											}
											if (!isRestrictionArgumentSubTypeOfField || !isRestrictionArgumentTypeSingletonRestricted) {
												error("Restriction argument must be a singleton restricted sub-type.", restriction,
													SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
											}
										}
									}
								} else {
									error("The Default restriction requires one argument.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								}
							}
							defaultUsed = true;				
						} else {
							error("The Default restriction is already used on this field.",
								restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						}	
					}
					case 'range': {
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype || fieldType instanceof DeclarationReference) {
							error("The Range restriction can only be used on integers and floats.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						} else if (fieldType instanceof BuiltInType) {
							if ((fieldType instanceof Integertype) || (fieldType instanceof Floattype)) {
								if (restriction.getRestrictionArguments().size() == 2) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									val argument2 = restriction.getRestrictionArguments().get(1);
									if (fieldType instanceof Integertype) {
										if (argument1.valueLong == null) {
											error("Restriction argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueLong == null) {
											error("The second argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldType instanceof Floattype) {
										if (argument1.valueDouble == null) {
											error("The first argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueDouble == null) {
											error("The second argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else if (restriction.getRestrictionArguments().size() == 3) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									val argument2 = restriction.getRestrictionArguments().get(1);
									val argument3 = restriction.getRestrictionArguments().get(2);
									if (fieldType instanceof Integertype) {
										if (argument1.valueLong == null) {
											error("The first argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueLong == null) {
											error("The second argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument3.valueString == null) {
											error("The third argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldType instanceof Floattype) {
										if (argument1.valueDouble == null) {
											error("The first argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueDouble == null) {
											error("The second argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}  else if (argument3.valueString == null) {
											error("The third argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else {
									error("The Range restriction must have two or three arguments.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								}
							} else {
								error("The Range restriction can only be used on integers and floats.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							}
						}
					}
					case 'max': {
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype || fieldType instanceof DeclarationReference) {
							error("The Max restriction can only be used on integers and floats.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						} else if (fieldType instanceof BuiltInType) {
							if ((fieldType instanceof Integertype) || (fieldType instanceof Floattype)) {
								if (restriction.getRestrictionArguments().size() == 1) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									if (fieldType instanceof Integertype) {
										if (argument1.valueLong == null) {
											error("Restriction argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldType instanceof Floattype) {
										if (argument1.valueDouble == null) {
											error("Restriction argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else if (restriction.getRestrictionArguments().size() == 2) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									val argument2 = restriction.getRestrictionArguments().get(1);
									if (fieldType instanceof Integertype) {
										if (argument1.valueLong == null) {
											error("The first argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueString == null) {
											error("The second argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldType instanceof Floattype) {
										if (argument1.valueDouble == null) {
											error("The first argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueString == null) {
											error("The second argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else {
									error("The Max restriction must have one or two arguments.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								}
							} else {
								error("The Max restriction can only be used on integers and floats.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							}
						}
					}
					case 'min': {
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype || fieldType instanceof DeclarationReference) {
							error("The Min restriction can only be used on integers and floats.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						} else if (fieldType instanceof BuiltInType) {
							if ((fieldType instanceof Integertype) || (fieldType instanceof Floattype)) {
								if (restriction.getRestrictionArguments().size() == 1) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									if (fieldType instanceof Integertype) {
										if (argument1.valueLong == null) {
											error("Restriction argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldType instanceof Floattype) {
										if (argument1.valueDouble == null) {
											error("Restriction argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else if (restriction.getRestrictionArguments().size() == 2) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									val argument2 = restriction.getRestrictionArguments().get(1);
									if (fieldType instanceof Integertype) {
										if (argument1.valueLong == null) {
											error("The first argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueString == null) {
											error("The second argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldType instanceof Floattype) {
										if (argument1.valueDouble == null) {
											error("The first argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueString == null) {
											error("The second argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else {
									error("The Min restriction must have one or two arguments.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								}
							} else {
								error("The Min restriction can only be used on integers and floats.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							}
						}
					}
					case 'coding': {
						if (restriction.getRestrictionArguments().size() == 1) {
							val argument = restriction.getRestrictionArguments().get(0);
							if (argument.valueString == null) {
								error("The Coding restriction argument value must be a string.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							}
						} else {
							error("The Coding restriction must have one argument.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						}
					}
					case 'constantlenghtpointer': {
						var errorFound = false;
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype) {
							error("The Constant Length Pointer restriction can only be used on strings, annotations and user-types.",
								restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							errorFound = true;
						} else if (fieldType instanceof BuiltInType) {
							if ((fieldType instanceof Stringtype) || (fieldType instanceof Annotationtype)) {
								if (restriction.getRestrictionArguments().size() != 0) {
									error("The Constant Length Pointer restriction can't have arguments.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
									errorFound = true;
								}
							} else {
								error("The Constant Length Pointer restriction can only be used on strings, annotations and user-types.",
									restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								errorFound = true;
							}
						} else if (fieldType instanceof DeclarationReference) {
							if (restriction.getRestrictionArguments().size() != 0) {
								error("The Constant Length Pointer restriction can't have arguments.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								errorFound = true;
							}
						}
						if (!errorFound && constantLenghtPointerUsed) {
							warning("The Constant Length Pointer restriction is already used on this field.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						}
						constantLenghtPointerUsed = true;
					}
					case 'oneof': {
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype) {
							error("The One Of restriction can only be used on annotations and user-types.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						} else if (fieldType instanceof BuiltInType) {
							if (fieldType instanceof Annotationtype) {
								if (restriction.getRestrictionArguments().size() >= 1) {
									for (RestrictionArgument argument : restriction.getRestrictionArguments()) {
										if (argument.valueType == null) {
											error("Restriction argument must be a user-type.", restriction,
												SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else {
									error("The One Of restriction requires at least one argument.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								}
							} else {
								error("The One Of restriction can only be used on annotations and user-types.",
									restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							}
						} else if (fieldType instanceof DeclarationReference) {
							if (restriction.getRestrictionArguments().size() >= 1) {
								for (RestrictionArgument argument : restriction.getRestrictionArguments()) {
									if (argument.valueType == null) {
										error("Restriction argument must be a user-type.", restriction,
											SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
									}
								}
							} else {
								error("The One Of restriction requires at least one argument.", restriction,
									SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							}
						}
					}
					default: {
						error("Unknown Restriction", restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
					}
				}
			}
		}
	}
}