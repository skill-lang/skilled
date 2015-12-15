package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.util.SubtypesFinder
import org.eclipse.xtext.validation.Check

/**  
 * 
 * @author Nikolay Fateev
 */
class TypeRestrictionsValidator extends AbstractSKilLValidator {
	
	// All restriction warning and error messages
	private final static String Unknown_Restriction = "Unknown Restriction"
	// Unique restriction error messages
	private final static String Unique_Has_Args = "The Unique restriction can't have arguments."
	private final static String Unique_Usage = "The Unique restriction can only be used on types that do not have sub- or super-types."
	// Unique restriction warning messages
	private final static String Unique_Already_Used = "The Unique restriction is already used on this type."
	// Singleton restriction error messages
	private final static String Singleton_Has_Args = "The Singleton restriction can't have arguments."
	private final static String Singleton_Usage = "The Singleton restriction can only be used on types that do not have super-types."
	// Singleton restriction warning messages
	private final static String Singleton_Already_Used = "The Singleton restriction is already used on this type."
	// Monotone restriction error messages
	private final static String Monotone_Has_Args = "The Monotone restriction can't have arguments."
	private final static String Monotone_Usage = "The Monotone restriction can only be used on base-types"
	// Monotone restriction warning messages
	private final static String Monotone_Already_Used = "The Monotone restriction is already used on this type."
	// Abstract restriction error messages
	private final static String Abstract_Has_Args = "The Abstract restriction can't have arguments."
	// Abstract restriction warning messages
	private final static String Abstract_Already_Used = "The Abstract restriction is already used on this type."
	// Default restriction error messages
	private final static String Default_Arg_Not_Singleton = "Restriction argument must be a singleton restricted sub-type."
	private final static String Default_Not_One_Arg = "The Default restriction must have one argument."
	// Default restriction warning messages
	private final static String Default_Already_Used = "The Default restriction is already used on this field."
	

	@Check
	def validateTypeRestrions(Declaration declaration) {
		if (declaration instanceof Usertype) {
			validateUsertype(declaration);
		} else if (declaration instanceof Typedef) {
			validateTypedef(declaration);
		}
	}
	
	def validateUsertype(Usertype usertype) {
		var wasUniqueUsed = false
		var wasSingletonUsed = false
		var wasMonotoneUsed = false
		var wasAbstractUsed = false
		var wasDefaultUsed = false
		for (restriction : usertype.restrictions) {
			switch (restriction.restrictionName) {
				case 'unique': {
					if (!wasUniqueUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (usertype.supertypes.size != 0) {						 // Check for supertypes
								showError(Unique_Usage, restriction)
							} else if (SubtypesFinder.getSubtypes(usertype).size != 0) { // Check for subtypes
								showError(Unique_Usage, restriction)
							}
							wasUniqueUsed = true
						} else {
							showError(Unique_Has_Args, restriction)
						}
					} else {
						showWarning(Unique_Already_Used, restriction)
					}		
				} 
				case 'singleton': {
					if (!wasSingletonUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (SubtypesFinder.getSubtypes(usertype).size != 0) { 		// Check for subtypes
								showError(Singleton_Usage, restriction)
							}
							wasSingletonUsed = true
						} else {
							showError(Singleton_Has_Args, restriction)
						}
					} else {
						showWarning(Singleton_Already_Used, restriction)
					}		
				} 
				case 'monotone': {
					if (!wasMonotoneUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (usertype.supertypes.size != 0) {						// Check for supertypes
								showError(Monotone_Usage, restriction)
							}
							wasMonotoneUsed = true
						} else {
							showError(Monotone_Has_Args, restriction)
						}
					} else {
						showWarning(Monotone_Already_Used, restriction)
					}		
				}
				case 'abstract': {
					if (restriction.restrictionArguments.size != 0) {
						showError(Abstract_Has_Args, restriction)
					} else if (wasAbstractUsed) {
						showWarning(Abstract_Already_Used, restriction)
					}
					wasAbstractUsed = true;
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							val restrictionArgument = restriction.restrictionArguments.get(0)
							if (restrictionArgument.valueType == null) {			// The argument is not a Usertype or Typedef or Enum
								showError(Default_Arg_Not_Singleton, restriction)
							} else {
								val restrictionArgumentType = (restrictionArgument.valueType).type
								if (restrictionArgumentType instanceof Usertype) {
									val restrictionArgumentSupertypes = restrictionArgumentType.supertypes
									var isRestrictionArgumentSubTypeOfThisType = false
									for (supertype : restrictionArgumentSupertypes) {
										if (usertype.name.equals(supertype.type.name)) {
											isRestrictionArgumentSubTypeOfThisType = true
										}
									}
									var isRestrictionArgumentTypeSingletonRestricted = false
									if (isRestrictionArgumentSubTypeOfThisType) {
										val restrictionArgumentRestrictions = restrictionArgumentType.restrictions
										for (restrictionArgumentRestriction : restrictionArgumentRestrictions) {
											if ("singleton".equals(restrictionArgumentRestriction.restrictionName)) {
												isRestrictionArgumentTypeSingletonRestricted = true
											}
										}
									}
									if (!isRestrictionArgumentSubTypeOfThisType ||
										!isRestrictionArgumentTypeSingletonRestricted) {
										showError(Default_Arg_Not_Singleton, restriction)
									}
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
				default: {
					showError(Unknown_Restriction, restriction)
				}
			}
		}
	}
	
	def validateTypedef(Typedef typedef) {
		val typedefFieldType = typedef.fieldtype
		var wasUniqueUsed = false
		var wasSingletonUsed = false
		var wasMonotoneUsed = false
		var wasAbstractUsed = false
		var wasDefaultUsed = false
		for (restriction : typedef.restrictions) {
			switch (restriction.restrictionName) {
				case 'unique': {
					if (!wasUniqueUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (typedefFieldType instanceof Usertype) {
								if (typedefFieldType.supertypes.size != 0) { // Check for supertypes
									showError(Unique_Usage, restriction)
								} else if (SubtypesFinder.getSubtypes(typedefFieldType).size != 0) { // Check for subtypes
									showError(Unique_Usage, restriction)
								}
							} else {
								showError(Unique_Usage, restriction)
							}
							wasUniqueUsed = true
						} else {
							showError(Unique_Has_Args, restriction)
						}
					} else {
						showWarning(Unique_Already_Used, restriction)
					}		
				} 
				case 'singleton': {
					if (!wasSingletonUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (typedefFieldType instanceof Usertype) {
								if (SubtypesFinder.getSubtypes(typedefFieldType).size != 0) { // Check for subtypes
									showError(Singleton_Usage, restriction)
								}
							} else {
								showError(Singleton_Usage, restriction)
							}
							wasSingletonUsed = true
						} else {
							showError(Singleton_Has_Args, restriction)
						}						
					} else {
						showWarning(Singleton_Already_Used, restriction)
					}		
				} 
				case 'monotone': {
					if (!wasMonotoneUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (typedefFieldType instanceof Usertype) {
								if (typedefFieldType.supertypes.size != 0) { 				// Check for supertypes
									showError(Monotone_Usage, restriction)
								}				
							} else {
								showError(Monotone_Usage, restriction)
							}
							wasMonotoneUsed = true
						} else {	
							showError(Monotone_Has_Args, restriction)
						}
					} else {
						showWarning(Monotone_Already_Used, restriction)
					}		
				}
				case 'abstract': {
					if (restriction.restrictionArguments.size != 0) {
						showError(Abstract_Has_Args, restriction)
					} else if (wasAbstractUsed) {
						showWarning(Abstract_Already_Used, restriction)
					}
					wasAbstractUsed = true;
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							if (typedefFieldType instanceof Usertype) {
								val restrictionArgument = restriction.restrictionArguments.get(0)
								if (restrictionArgument.valueType == null) { // The argument is not a Usertype or Typedef or Enum
									showError(Default_Arg_Not_Singleton, restriction)
								} else {
									val restrictionArgumentType = (restrictionArgument.valueType).type
									if (restrictionArgumentType instanceof Usertype) {
										val restrictionArgumentSupertypes = restrictionArgumentType.supertypes
										var isRestrictionArgumentSubTypeOfThisType = false
										for (supertype : restrictionArgumentSupertypes) {
											if (typedefFieldType.name.equals(supertype.type.name)) {
												isRestrictionArgumentSubTypeOfThisType = true
											}
										}
										var isRestrictionArgumentTypeSingletonRestricted = false
										if (isRestrictionArgumentSubTypeOfThisType) {
											val restrictionArgumentRestrictions = restrictionArgumentType.restrictions
											for (restrictionArgumentRestriction : restrictionArgumentRestrictions) {
												if ("singleton".equals(
													restrictionArgumentRestriction.restrictionName)) {
													isRestrictionArgumentTypeSingletonRestricted = true
												}
											}
										}
										if (!isRestrictionArgumentSubTypeOfThisType ||
											!isRestrictionArgumentTypeSingletonRestricted) {
											showError(Default_Arg_Not_Singleton, restriction)
										}
									} else {
										showError(Default_Arg_Not_Singleton, restriction)
									}
								}
							} else {
								showError(Default_Arg_Not_Singleton, restriction)
							}											
						} else {
							showError(Default_Not_One_Arg, restriction)
						}
						wasDefaultUsed = true
					} else {
						showError(Default_Already_Used, restriction)
					}
				}
				default: {
					showError(Unknown_Restriction, restriction)
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
}