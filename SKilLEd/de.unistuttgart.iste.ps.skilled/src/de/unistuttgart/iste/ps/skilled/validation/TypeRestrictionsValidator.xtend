package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.util.SubtypesFinder
import javax.inject.Inject
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype

/**  
 * Validates type restrictions and their arguments. <br>
 * <br>
 * Supported restrictions: Unique, Singleton, Monotone, Abstract, Default <br>
 * <br>
 * The error/warning messages give context to the conditions. <br>
 * <br>
 * Guide to restriction arguments (parameters): <br>
 * restrictionArgument.valueType 	- a declaration (typedef, enum, interface, usertype) type argument <br>
 * restrictionArgument.valueString 	- a string type argument <br>
 * restrictionArgument.valueLong 	- a integer type argument <br>
 * restrictionArgument.valueDouble	- a float type argument <br>
 * 
 * @author Nikolay Fateev
 */
class TypeRestrictionsValidator extends AbstractSKilLValidator {

	@Inject
	private SubtypesFinder subtypesFinder

	// All restriction warning and error messages
	private final static String Unknown_Restriction = "Unknown Restriction"
	private final static String Restriction_On_Map = "Map-typed fields can't have restrictions."
	// Unique restriction error messages
	private final static String Unique_Has_Args = "The Unique restriction can't have arguments."
	private final static String Unique_Usage = "The Unique restriction can only be used on types that do not have sub- or super-types."
	// Unique restriction warning messages
	private final static String Unique_Already_Used = "The Unique restriction is already used on this type."
	// Singleton restriction error messages
	private final static String Singleton_Has_Args = "The Singleton restriction can't have arguments."
	private final static String Singleton_Usage = "The Singleton restriction can only be used on types that do not have sub-types."
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
			validateUsertype(declaration)
		} else if (declaration instanceof Typedef) {
			validateTypedef(declaration)
		}
	}

	def validateUsertype(Usertype usertype) {
		var wasUniqueUsed = false
		var wasSingletonUsed = false
		var wasMonotoneUsed = false
		var wasAbstractUsed = false
		var wasDefaultUsed = false
		for (restriction : usertype.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'unique': {
					if (!wasUniqueUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if ((usertype.supertypes.size != 0) || // Check for supertypes
							(subtypesFinder.getSubtypes(usertype).size != 0)) { // Check for subtypes				 
								showError(Unique_Usage, restriction)
							}
						} else {
							showError(Unique_Has_Args, restriction)
						}
						wasUniqueUsed = true
					} else {
						showWarning(Unique_Already_Used, restriction)
					}
				}
				case 'singleton': {
					if (!wasSingletonUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (subtypesFinder.getSubtypes(usertype).size != 0) { // Check for subtypes
								showError(Singleton_Usage, restriction)
							}
						} else {
							showError(Singleton_Has_Args, restriction)
						}
						wasSingletonUsed = true
					} else {
						showWarning(Singleton_Already_Used, restriction)
					}
				}
				case 'monotone': {
					if (!wasMonotoneUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (usertype.supertypes.size != 0) { // Check for supertypes
								showError(Monotone_Usage, restriction)
							}
						} else {
							showError(Monotone_Has_Args, restriction)
						}
						wasMonotoneUsed = true
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
					wasAbstractUsed = true
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							val restrictionArgument = restriction.restrictionArguments.get(0)
							if (restrictionArgument.valueType == null) { // The argument is not a Usertype/Typedef/Enum/Interface
								showError(Default_Arg_Not_Singleton, restriction)
							} else {
								val restrictionArgumentType = (restrictionArgument.valueType).type
								if (restrictionArgumentType instanceof Usertype) {
									if (!isUsertypeValidDefaultArgument(restrictionArgumentType, usertype)) {
										showError(Default_Arg_Not_Singleton, restriction)
									}
								} else if (restrictionArgumentType instanceof Typedef) {							
									if (!isTypedefValidDefaultArgument(restrictionArgumentType, usertype)) {
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
		val underlyingUsertype = returnTypedefDeclaration(typedef)
		var wasUniqueUsed = false
		var wasSingletonUsed = false
		var wasMonotoneUsed = false
		var wasAbstractUsed = false
		var wasDefaultUsed = false
		if (underlyingUsertype != null) {
			wasUniqueUsed = isUserTypeUniqueRestricted(underlyingUsertype)
			wasSingletonUsed = isUserTypeSingletonRestricted(underlyingUsertype)
			wasMonotoneUsed = isUserTypeMonotoneRestricted(underlyingUsertype)
			wasAbstractUsed = isUserTypeAbstractRestricted(underlyingUsertype)
			wasDefaultUsed = isUserTypeDefaultRestricted(underlyingUsertype)
		}
		for (restriction : typedef.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'unique': {
					if (restriction.restrictionArguments.size == 0) {
						var errorFound = false
						if (underlyingUsertype != null) {
							if (underlyingUsertype.supertypes.size != 0) { // Check for supertypes
								showError(Unique_Usage, restriction)
								errorFound = true
							} else if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) { // Check for subtypes
								showError(Unique_Usage, restriction)
								errorFound = true
							}
						} else {
							showError(Unique_Usage, restriction)
							errorFound = true
						}
						if (!errorFound && wasUniqueUsed) {
							showWarning(Unique_Already_Used, restriction)
						}
						wasUniqueUsed = true
					} else {
						showError(Unique_Has_Args, restriction)
					}
				}
				case 'singleton': {
					if (restriction.restrictionArguments.size == 0) {
						var errorFound = false
						if (underlyingUsertype != null) {
							if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) { // Check for subtypes
								showError(Singleton_Usage, restriction)
								errorFound = true
							}
						} else {
							showError(Singleton_Usage, restriction)
							errorFound = true
						}
						if (!errorFound && wasSingletonUsed) {
							showWarning(Singleton_Already_Used, restriction)
						}
						wasSingletonUsed = true
					} else {
						showError(Singleton_Has_Args, restriction)
					}
				}
				case 'monotone': {
					if (restriction.restrictionArguments.size == 0) {
						var errorFound = false
						if (underlyingUsertype != null) {
							if (underlyingUsertype.supertypes.size != 0) { // Check for supertypes
								showError(Monotone_Usage, restriction)
								errorFound = true
							}
						} else {
							showError(Monotone_Usage, restriction)
							errorFound = true
						}
						if (!errorFound && wasMonotoneUsed) {
							showWarning(Monotone_Already_Used, restriction)
						}
						wasMonotoneUsed = true
					} else {
						showError(Monotone_Has_Args, restriction)
					}
				}
				case 'abstract': {
					if (!(typedef.fieldtype instanceof Maptype)) {
						if (restriction.restrictionArguments.size != 0) {
							showError(Abstract_Has_Args, restriction)
						} else if (wasAbstractUsed) {
							showWarning(Abstract_Already_Used, restriction)
						}
						wasAbstractUsed = true
					} else {
						showError(Restriction_On_Map, restriction)
					}	
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							if (underlyingUsertype != null) {
								val restrictionArgument = restriction.restrictionArguments.get(0)
								if (restrictionArgument.valueType == null) { // The argument is not a Usertype/Typedef/Enum/Interface
									showError(Default_Arg_Not_Singleton, restriction)
								} else {
									val restrictionArgumentType = (restrictionArgument.valueType).type
									if (restrictionArgumentType instanceof Usertype) {
										if (!isUsertypeValidDefaultArgument(restrictionArgumentType,
											underlyingUsertype)) {
											showError(Default_Arg_Not_Singleton, restriction)
										}
									} else if (restrictionArgumentType instanceof Typedef) {
										if (!isTypedefValidDefaultArgument(restrictionArgumentType,
											underlyingUsertype)) {
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

	def Usertype returnTypedefDeclaration(Typedef typedef) {
		if (typedef.fieldtype instanceof DeclarationReference) {
			val typedefType = (typedef.fieldtype as DeclarationReference).type
			if (typedefType instanceof Usertype) {
				return typedefType
			} else {
				return null
			}
		} else
			return null
	}
	
	def boolean isSubtype(Usertype basetype, Usertype subtype) {
		for (supertype : subtype.supertypes) {
			if (basetype.name.equals(supertype.type.name)) {
				return true
			}
		}
		return false
	}

	def boolean isUserTypeUniqueRestricted(Usertype usertype) {
		for (restriction : usertype.restrictions) {
			if ("unique".equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}
	
	def boolean isTypedefSingletonRestricted(Typedef typedef) {
		for (restriction : typedef.restrictions) {
			if ("singleton".equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}

	def boolean isUserTypeSingletonRestricted(Usertype usertype) {
		for (restriction : usertype.restrictions) {
			if ("singleton".equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}

	def boolean isUserTypeMonotoneRestricted(Usertype usertype) {
		for (restriction : usertype.restrictions) {
			if ("monotone".equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}

	def boolean isUserTypeAbstractRestricted(Usertype usertype) {
		for (restriction : usertype.restrictions) {
			if ("abstract".equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}

	def boolean isUserTypeDefaultRestricted(Usertype usertype) {
		for (restriction : usertype.restrictions) {
			if ("default".equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}
	
	def boolean isUsertypeValidDefaultArgument(Usertype argumentUsertype, Usertype usertype) {
		val isUsertypeSubtypeOfThisType = isSubtype(usertype, argumentUsertype)
		val isUsertypeSingletonRestricted = isUserTypeSingletonRestricted(argumentUsertype)
		if (!isUsertypeSubtypeOfThisType || !isUsertypeSingletonRestricted) {
			return false
		}
		return true
	}
	
	def boolean isTypedefValidDefaultArgument(Typedef typedef, Usertype usertype) {
		val underlyingUsertype = returnTypedefDeclaration(typedef)
		var isTypedefTypeSubtypeOfThisType = false
		var isTypedefTypeSingletonRestricted = false;
		var isTypedefSingletonRestricted = false;
		if (underlyingUsertype != null) {
			isTypedefTypeSubtypeOfThisType = isSubtype(usertype, underlyingUsertype)
			isTypedefTypeSingletonRestricted = isUserTypeSingletonRestricted(underlyingUsertype)
			isTypedefSingletonRestricted = isTypedefSingletonRestricted(typedef)
		}
		if (!isTypedefTypeSubtypeOfThisType || (!isTypedefTypeSingletonRestricted && !isTypedefSingletonRestricted)) {
			return false
		}
		return true
	}

	def showError(String message, Restriction restriction) {
		error(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def showWarning(String message, Restriction restriction) {
		warning(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}
}