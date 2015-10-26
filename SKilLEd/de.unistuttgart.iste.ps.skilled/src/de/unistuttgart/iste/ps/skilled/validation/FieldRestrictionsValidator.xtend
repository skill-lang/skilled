package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInType
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInTypeReference
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Listtype
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.RestrictionArgument
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Settype
import org.eclipse.xtext.validation.Check

/** 
 * This class contains the Validation rules for Cyclic Types. 
 * @author Nikolay Fateev
 */
class FieldRestrictionsValidator extends AbstractSKilLValidator {

	/*
	 * If field type is instance of DeclarationReference, it means that this is a user-type.
	 * Look at the error message - it gives context to the if()s.
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
					case 'nonNull': {
						var errorFound = false;
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype) {
							error("The Non Null restriction can only be used on strings and user-types.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							errorFound = true;
						} else if (fieldType instanceof BuildInTypeReference) {
							val fieldBuiltInType = (fieldType as BuildInTypeReference).getType();
							if (fieldBuiltInType == BuildInType.STRING) {
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
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype || fieldType instanceof DeclarationReference) {
							error("The Default restriction can only be used on strings, integers, floats, annotations and user-types.",
								restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						} else if (fieldType instanceof BuildInTypeReference) {
							val fieldBuiltInType = (fieldType as BuildInTypeReference).getType();
							if (fieldBuiltInType != BuildInType.BOOLEAN) {
								if (restriction.getRestrictionArguments().size() == 1) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									if (fieldBuiltInType == BuildInType.I8 || fieldBuiltInType == BuildInType.I16 ||
										fieldBuiltInType == BuildInType.I32 || fieldBuiltInType == BuildInType.I64 ||
										fieldBuiltInType == BuildInType.V64) {
										if (argument1.valueLong == null) {
											error("Restriction argument must be an integer.", restriction,
												SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldBuiltInType == BuildInType.F32 || fieldBuiltInType == BuildInType.F64) {
										if (argument1.valueDouble == null) {
											error("Restriction argument must be a float.", restriction,
												SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldBuiltInType == BuildInType.STRING) {
										if (argument1.valueString == null) {
											error("Restriction argument must be a string.", restriction,
												SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} 
//									else if (fieldBuiltInType == BuildInType.ANNOTATION) {
//										if (argument1.valueType == null) {
//											error("Restriction argument must be an enum or singleton restricted type.", restriction,
//												SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
//										} else {
//											val type = (argument1.valueType).type;
//											if (type instanceof Interfacetype) {
//												error("Restriction argument must be an enum or singleton restricted type.!!", restriction,
//													SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
//											} else if (type instanceof Typedef) {
//												val restrictions = (type as Typedef).restrictions;
//												var singletonFound = false;
//												for (typedefRestriction : restrictions) {
//													if ("singleton".equals(typedefRestriction.restrictionName)) {
//														singletonFound = true;
//													}
//												}
//												if (!singletonFound) {
//													error("Restriction argument must be an enum or singleton restricted type.!!", restriction,
//														SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
//												}
//											}
//										}
//									}
								} else {
									error("The Default restriction requires one argument.", restriction,
										SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
								}
							} else {
								error("The Default restriction can only be used on strings, integers, floats, annotations and user-types.",
									restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							}
						}
						defaultUsed = true;
					}
					case 'range': {
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype || fieldType instanceof DeclarationReference) {
							error("The Range restriction can only be used on integers and floats.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						} else if (fieldType instanceof BuildInTypeReference) {
							val fieldBuiltInType = (fieldType as BuildInTypeReference).getType();
							if (fieldBuiltInType != BuildInType.STRING && fieldBuiltInType != BuildInType.BOOLEAN &&
								fieldBuiltInType != BuildInType.ANNOTATION) {
								if (restriction.getRestrictionArguments().size() == 2) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									val argument2 = restriction.getRestrictionArguments().get(1);
									if (fieldBuiltInType == BuildInType.I8 || fieldBuiltInType == BuildInType.I16 ||
										fieldBuiltInType == BuildInType.I32 || fieldBuiltInType == BuildInType.I64 ||
										fieldBuiltInType == BuildInType.V64) {
										if (argument1.valueLong == null) {
											error("Restriction argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueLong == null) {
											error("The second argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldBuiltInType == BuildInType.F32 || fieldBuiltInType == BuildInType.F64) {
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
									if (fieldBuiltInType == BuildInType.I8 || fieldBuiltInType == BuildInType.I16 ||
										fieldBuiltInType == BuildInType.I32 || fieldBuiltInType == BuildInType.I64 ||
										fieldBuiltInType == BuildInType.V64) {
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
									} else if (fieldBuiltInType == BuildInType.F32 || fieldBuiltInType == BuildInType.F64) {
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
						} else if (fieldType instanceof BuildInTypeReference) {
							val fieldBuiltInType = (fieldType as BuildInTypeReference).getType();
							if (fieldBuiltInType != BuildInType.STRING && fieldBuiltInType != BuildInType.BOOLEAN &&
								fieldBuiltInType != BuildInType.ANNOTATION) {
								if (restriction.getRestrictionArguments().size() == 1) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									if (fieldBuiltInType == BuildInType.I8 || fieldBuiltInType == BuildInType.I16 ||
										fieldBuiltInType == BuildInType.I32 || fieldBuiltInType == BuildInType.I64 ||
										fieldBuiltInType == BuildInType.V64) {
										if (argument1.valueLong == null) {
											error("Restriction argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldBuiltInType == BuildInType.F32 || fieldBuiltInType == BuildInType.F64) {
										if (argument1.valueDouble == null) {
											error("Restriction argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else if (restriction.getRestrictionArguments().size() == 2) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									val argument2 = restriction.getRestrictionArguments().get(1);
									if (fieldBuiltInType == BuildInType.I8 || fieldBuiltInType == BuildInType.I16 ||
										fieldBuiltInType == BuildInType.I32 || fieldBuiltInType == BuildInType.I64 ||
										fieldBuiltInType == BuildInType.V64) {
										if (argument1.valueLong == null) {
											error("The first argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueString == null) {
											error("The second argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldBuiltInType == BuildInType.F32 || fieldBuiltInType == BuildInType.F64) {
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
						} else if (fieldType instanceof BuildInTypeReference) {
							val fieldBuiltInType = (fieldType as BuildInTypeReference).getType();
							if (fieldBuiltInType != BuildInType.STRING && fieldBuiltInType != BuildInType.BOOLEAN &&
								fieldBuiltInType != BuildInType.ANNOTATION) {
								if (restriction.getRestrictionArguments().size() == 1) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									if (fieldBuiltInType == BuildInType.I8 || fieldBuiltInType == BuildInType.I16 ||
										fieldBuiltInType == BuildInType.I32 || fieldBuiltInType == BuildInType.I64 ||
										fieldBuiltInType == BuildInType.V64) {
										if (argument1.valueLong == null) {
											error("Restriction argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldBuiltInType == BuildInType.F32 || fieldBuiltInType == BuildInType.F64) {
										if (argument1.valueDouble == null) {
											error("Restriction argument must be a float.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									}
								} else if (restriction.getRestrictionArguments().size() == 2) {
									val argument1 = restriction.getRestrictionArguments().get(0);
									val argument2 = restriction.getRestrictionArguments().get(1);
									if (fieldBuiltInType == BuildInType.I8 || fieldBuiltInType == BuildInType.I16 ||
										fieldBuiltInType == BuildInType.I32 || fieldBuiltInType == BuildInType.I64 ||
										fieldBuiltInType == BuildInType.V64) {
										if (argument1.valueLong == null) {
											error("The first argument must be an integer.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										} else if (argument2.valueString == null) {
											error("The second argument must be a string.",
												restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
										}
									} else if (fieldBuiltInType == BuildInType.F32 || fieldBuiltInType == BuildInType.F64) {
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
							error("The Coding restriction requires an argument.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						}
					}
					case 'constantLenghtPointer': {
						var errorFound = false;
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype) {
							error("The Constant Length Pointer restriction can only be used on strings, annotations and user-types.",
								restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
							errorFound = true;
						} else if (fieldType instanceof BuildInTypeReference) {
							val fieldBuiltInType = (fieldType as BuildInTypeReference).getType();
							if (fieldBuiltInType == BuildInType.STRING || fieldBuiltInType == BuildInType.ANNOTATION) {
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
					case 'oneOf': {
						if (fieldType instanceof Settype || fieldType instanceof Listtype ||
							fieldType instanceof Arraytype) {
							error("The One Of restriction can only be used on annotations and user-types.", restriction,
								SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
						} else if (fieldType instanceof BuildInTypeReference) {
							val fieldBuiltInType = (fieldType as BuildInTypeReference).getType();
							if (fieldBuiltInType == BuildInType.ANNOTATION) {
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