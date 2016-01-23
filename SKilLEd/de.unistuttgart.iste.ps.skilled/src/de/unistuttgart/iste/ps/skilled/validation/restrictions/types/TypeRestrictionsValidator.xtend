package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

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
import de.unistuttgart.iste.ps.skilled.validation.restrictions.types.ErrorMessages
import de.unistuttgart.iste.ps.skilled.validation.AbstractSKilLValidator

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
								showError(ErrorMessages.Unique_Usage, restriction)
							}
						} else {
							showError(ErrorMessages.Unique_Has_Args, restriction)
						}
						wasUniqueUsed = true
					} else {
						showWarning(ErrorMessages.Unique_Already_Used, restriction)
					}
				}
				case 'singleton': {
					if (!wasSingletonUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (subtypesFinder.getSubtypes(usertype).size != 0) { // Check for subtypes
								showError(ErrorMessages.Singleton_Usage, restriction)
							}
						} else {
							showError(ErrorMessages.Singleton_Has_Args, restriction)
						}
						wasSingletonUsed = true
					} else {
						showWarning(ErrorMessages.Singleton_Already_Used, restriction)
					}
				}
				case 'monotone': {
					if (!wasMonotoneUsed) {
						if (restriction.restrictionArguments.size == 0) {
							if (usertype.supertypes.size != 0) { // Check for supertypes
								showError(ErrorMessages.Monotone_Usage, restriction)
							}
						} else {
							showError(ErrorMessages.Monotone_Has_Args, restriction)
						}
						wasMonotoneUsed = true
					} else {
						showWarning(ErrorMessages.Monotone_Already_Used, restriction)
					}
				}
				case 'abstract': {
					if (restriction.restrictionArguments.size != 0) {
						showError(ErrorMessages.Abstract_Has_Args, restriction)
					} else if (wasAbstractUsed) {
						showWarning(ErrorMessages.Abstract_Already_Used, restriction)
					}
					wasAbstractUsed = true
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							val restrictionArgument = restriction.restrictionArguments.get(0)
							if (restrictionArgument.valueType == null) { // The argument is not a Usertype/Typedef/Enum/Interface
								showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
							} else {
								val restrictionArgumentType = (restrictionArgument.valueType).type
								if (restrictionArgumentType instanceof Usertype) {
									if (!isUsertypeValidDefaultArgument(restrictionArgumentType, usertype)) {
										showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
									}
								} else if (restrictionArgumentType instanceof Typedef) {
									if (!isTypedefValidDefaultArgument(restrictionArgumentType, usertype)) {
										showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
									}
								} else {
									showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
								}
							}
						} else {
							showError(ErrorMessages.Default_Not_One_Arg, restriction)
						}
						wasDefaultUsed = true
					} else {
						showError(ErrorMessages.Default_Already_Used, restriction)
					}
				}
				default: {
					showError(ErrorMessages.Unknown_Restriction, restriction)
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
			wasUniqueUsed = isUserTypeRestricted(underlyingUsertype, "unique")
			wasSingletonUsed = isUserTypeRestricted(underlyingUsertype, "singleton")
			wasMonotoneUsed = isUserTypeRestricted(underlyingUsertype, "monotone")
			wasAbstractUsed = isUserTypeRestricted(underlyingUsertype, "abstract")
			wasDefaultUsed = isUserTypeRestricted(underlyingUsertype, "default")
		}
		for (restriction : typedef.restrictions) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'unique': {
					if (restriction.restrictionArguments.size == 0) {
						var errorFound = false
						if (underlyingUsertype != null) {
							if (underlyingUsertype.supertypes.size != 0) { // Check for supertypes
								showError(ErrorMessages.Unique_Usage, restriction)
								errorFound = true
							} else if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) { // Check for subtypes
								showError(ErrorMessages.Unique_Usage, restriction)
								errorFound = true
							}
						} else {
							showError(ErrorMessages.Unique_Usage, restriction)
							errorFound = true
						}
						if (!errorFound && wasUniqueUsed) {
							showWarning(ErrorMessages.Unique_Already_Used, restriction)
						}
						wasUniqueUsed = true
					} else {
						showError(ErrorMessages.Unique_Has_Args, restriction)
					}
				}
				case 'singleton': {
					if (restriction.restrictionArguments.size == 0) {
						var errorFound = false
						if (underlyingUsertype != null) {
							if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) { // Check for subtypes
								showError(ErrorMessages.Singleton_Usage, restriction)
								errorFound = true
							}
						} else {
							showError(ErrorMessages.Singleton_Usage, restriction)
							errorFound = true
						}
						if (!errorFound && wasSingletonUsed) {
							showWarning(ErrorMessages.Singleton_Already_Used, restriction)
						}
						wasSingletonUsed = true
					} else {
						showError(ErrorMessages.Singleton_Has_Args, restriction)
					}
				}
				case 'monotone': {
					if (restriction.restrictionArguments.size == 0) {
						var errorFound = false
						if (underlyingUsertype != null) {
							if (underlyingUsertype.supertypes.size != 0) { // Check for supertypes
								showError(ErrorMessages.Monotone_Usage, restriction)
								errorFound = true
							}
						} else {
							showError(ErrorMessages.Monotone_Usage, restriction)
							errorFound = true
						}
						if (!errorFound && wasMonotoneUsed) {
							showWarning(ErrorMessages.Monotone_Already_Used, restriction)
						}
						wasMonotoneUsed = true
					} else {
						showError(ErrorMessages.Monotone_Has_Args, restriction)
					}
				}
				case 'abstract': {
					if (!(typedef.fieldtype instanceof Maptype)) {
						if (restriction.restrictionArguments.size != 0) {
							showError(ErrorMessages.Abstract_Has_Args, restriction)
						} else if (wasAbstractUsed) {
							showWarning(ErrorMessages.Abstract_Already_Used, restriction)
						}
						wasAbstractUsed = true
					} else {
						showError(ErrorMessages.Restriction_On_Map, restriction)
					}
				}
				case 'default': {
					if (!wasDefaultUsed) {
						if (restriction.restrictionArguments.size() == 1) {
							if (underlyingUsertype != null) {
								val restrictionArgument = restriction.restrictionArguments.get(0)
								if (restrictionArgument.valueType == null) { // The argument is not a Usertype/Typedef/Enum/Interface
									showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
								} else {
									val restrictionArgumentType = (restrictionArgument.valueType).type
									if (restrictionArgumentType instanceof Usertype) {
										if (!isUsertypeValidDefaultArgument(restrictionArgumentType,
											underlyingUsertype)) {
											showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
										}
									} else if (restrictionArgumentType instanceof Typedef) {
										if (!isTypedefValidDefaultArgument(restrictionArgumentType,
											underlyingUsertype)) {
											showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
										}
									} else {
										showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
									}
								}
							} else {
								showError(ErrorMessages.Default_Arg_Not_Singleton, restriction)
							}
						} else {
							showError(ErrorMessages.Default_Not_One_Arg, restriction)
						}
						wasDefaultUsed = true
					} else {
						showError(ErrorMessages.Default_Already_Used, restriction)
					}
				}
				default: {
					showError(ErrorMessages.Unknown_Restriction, restriction)
				}
			}
		}
	}

	def Usertype returnTypedefDeclaration(Typedef typedef) {
		if (typedef.fieldtype instanceof DeclarationReference) {
			val typedefType = (typedef.fieldtype as DeclarationReference).type
			if (typedefType instanceof Usertype) {
				return typedefType
			}
		}
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

	def boolean isUserTypeRestricted(Usertype usertype, String restrictionName) {
		for (restriction : usertype.restrictions) {
			if (restrictionName.equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}

	def boolean isUsertypeValidDefaultArgument(Usertype argumentUsertype, Usertype usertype) {
		return ( isSubtype(usertype, argumentUsertype) && isUserTypeRestricted(argumentUsertype, "singleton"))
	}

	def boolean isTypedefValidDefaultArgument(Typedef typedef, Usertype usertype) {
		val underlyingUsertype = returnTypedefDeclaration(typedef)
		if (underlyingUsertype == null) {
			return false
		}
		return isSubtype(usertype, underlyingUsertype) &&
			(isUserTypeRestricted(underlyingUsertype, "singleton") || isTypedefRestricted(typedef, "singleton"))
	}

	def boolean isTypedefRestricted(Typedef typedef, String restrictionName) {
		for (restriction : typedef.restrictions) {
			if (restrictionName.equals(restriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}

	def showError(String message, Restriction restriction) {
		error(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def showWarning(String message, Restriction restriction) {
		warning(message, restriction, SKilLPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}
}