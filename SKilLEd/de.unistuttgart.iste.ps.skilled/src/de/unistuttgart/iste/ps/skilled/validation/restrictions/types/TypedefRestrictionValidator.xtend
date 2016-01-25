package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.util.SubtypesFinder
import de.unistuttgart.iste.ps.skilled.validation.errormessages.TypeRestrictionsErrorMessages
import org.eclipse.xtext.validation.Check

/**
 * @author Nikolay Fateev
 * @author Moritz Platzer
 */
class TypedefRestrictionValidator extends TypeRestrictionsValidator {

	@Inject
	private SubtypesFinder subtypesFinder

	@Check
	def validateTypedef(Declaration declaration) {
		if (declaration instanceof Typedef) {
			var underlyingUsertype = returnTypedefDeclaration(declaration)
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

			for (restriction : declaration.restrictions) {
				switch (restriction.restrictionName.toLowerCase) {
					case 'unique': {
						handleUniqueRestriction(restriction, underlyingUsertype, wasUniqueUsed)
						wasUniqueUsed = true
					}
					case 'singleton': {
						handleSingletonRestriction(restriction, underlyingUsertype, wasSingletonUsed)
						wasSingletonUsed = true
					}
					case 'monotone': {
						handleMonotoneRestriction(restriction, underlyingUsertype, wasMonotoneUsed)
						wasMonotoneUsed = true
					}
					case 'abstract': {
						handleAbstractRestriction(restriction, underlyingUsertype, wasAbstractUsed, declaration)
						wasAbstractUsed = true
					}
					case 'default': {
						handleDefaultRestriction(restriction, underlyingUsertype, wasDefaultUsed)
						wasDefaultUsed = true
					}
					default: {
						showError(TypeRestrictionsErrorMessages.Unknown_Restriction, restriction)
					}
				}
			}
		}
	}

	def handleAbstractRestriction(Restriction restriction, Usertype usertype, boolean wasAbstractUsed,
		Declaration declaration) {
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

	def handleMonotoneRestriction(Restriction restriction, Usertype underlyingUsertype, boolean wasMonotoneUsed) {
		if (restriction.restrictionArguments.size == 0) {
			var errorFound = false
			if (underlyingUsertype != null) {
				if (underlyingUsertype.supertypes.size != 0) { // Check for supertypes
					showError(TypeRestrictionsErrorMessages.Monotone_Usage, restriction)
					errorFound = true
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Monotone_Usage, restriction)
				errorFound = true
			}
			if (!errorFound && wasMonotoneUsed) {
				showWarning(TypeRestrictionsErrorMessages.Monotone_Already_Used, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Monotone_Has_Args, restriction)
		}
	}

	def handleSingletonRestriction(Restriction restriction, Usertype underlyingUsertype, boolean wasSingletonUsed) {
		if (restriction.restrictionArguments.size == 0) {
			var errorFound = false
			if (underlyingUsertype != null) {
				if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) { // Check for subtypes
					showError(TypeRestrictionsErrorMessages.Singleton_Usage, restriction)
					errorFound = true
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Singleton_Usage, restriction)
				errorFound = true
			}
			if (!errorFound && wasSingletonUsed) {
				showWarning(TypeRestrictionsErrorMessages.Singleton_Already_Used, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Singleton_Has_Args, restriction)
		}
	}

	def handleUniqueRestriction(Restriction restriction, Usertype underlyingUsertype, boolean wasUniqueUsed) {
		if (restriction.restrictionArguments.size == 0) {
			var errorFound = false
			if (underlyingUsertype != null) {
				if (underlyingUsertype.supertypes.size != 0) { // Check for supertypes
					showError(TypeRestrictionsErrorMessages.Unique_Usage, restriction)
					errorFound = true
				} else if (subtypesFinder.getSubtypes(underlyingUsertype).size != 0) { // Check for subtypes
					showError(TypeRestrictionsErrorMessages.Unique_Usage, restriction)
					errorFound = true
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Unique_Usage, restriction)
				errorFound = true
			}
			if (!errorFound && wasUniqueUsed) {
				showWarning(TypeRestrictionsErrorMessages.Unique_Already_Used, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Unique_Has_Args, restriction)
		}
	}

	def handleDefaultRestriction(Restriction restriction, Usertype underlyingUsertype, boolean wasDefaultUsed) {
		if (!wasDefaultUsed) {
			if (restriction.restrictionArguments.size() == 1) {
				if (underlyingUsertype != null) {
					val restrictionArgument = restriction.restrictionArguments.get(0)
					if (restrictionArgument.valueType == null) { // The argument is not a Usertype/Typedef/Enum/Interface
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
					showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Default_Not_One_Arg, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Default_Already_Used, restriction)
		}
	}
}