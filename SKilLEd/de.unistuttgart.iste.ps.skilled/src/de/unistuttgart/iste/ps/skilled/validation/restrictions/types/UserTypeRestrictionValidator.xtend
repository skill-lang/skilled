package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.util.SubtypesFinder
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction

/**
 * @author Nikolay Fateev
 * @author Moritz Platzer
 */
class UserTypeRestrictionValidator extends TypeRestrictionsValidator {

	@Inject
	private SubtypesFinder subtypesFinder

	@Check
	def validateUsertype(Declaration declaration) {
		if (declaration instanceof Usertype) {
			var wasUniqueUsed = false
			var wasSingletonUsed = false
			var wasMonotoneUsed = false
			var wasAbstractUsed = false
			var wasDefaultUsed = false
			for (restriction : declaration.restrictions) {
				switch (restriction.restrictionName.toLowerCase) {
					case 'unique': {
						handleUniqueRestriction(restriction, wasUniqueUsed, declaration)
						wasUniqueUsed = true
					}
					case 'singleton': {
						handleSingletonRestriction(restriction, wasSingletonUsed, declaration)
						wasSingletonUsed = true
					}
					case 'monotone': {
						handleMonotoneRestriction(restriction, wasMonotoneUsed, declaration)
						wasMonotoneUsed = true
					}
					case 'abstract': {
						handleAbstractRestriction(restriction, wasMonotoneUsed)
						wasAbstractUsed = true
					}
					case 'default': {
						handleDefaultRestriction(restriction, wasDefaultUsed, declaration)
						wasDefaultUsed = true
					}
					default: {
						showError(TypeRestrictionsErrorMessages.Unknown_Restriction, restriction)
					}
				}
			}
		}
	}

	def handleAbstractRestriction(Restriction restriction, boolean wasAbstractUsed) {
		if (restriction.restrictionArguments.size != 0) {
			showError(TypeRestrictionsErrorMessages.Abstract_Has_Args, restriction)
		} else if (wasAbstractUsed) {
			showWarning(TypeRestrictionsErrorMessages.Abstract_Already_Used, restriction)
		}
	}

	def handleMonotoneRestriction(Restriction restriction, boolean wasMonotoneUsed, Usertype declaration) {
		if (!wasMonotoneUsed) {
			if (restriction.restrictionArguments.size == 0) {
				if (declaration.supertypes.size != 0) { // Check for supertypes
					showError(TypeRestrictionsErrorMessages.Monotone_Usage, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Monotone_Has_Args, restriction)
			}
		} else {
			showWarning(TypeRestrictionsErrorMessages.Monotone_Already_Used, restriction)
		}
	}

	def handleDefaultRestriction(Restriction restriction, boolean wasDefaultUsed, Usertype declaration) {
		if (!wasDefaultUsed) {
			if (restriction.restrictionArguments.size() == 1) {
				val restrictionArgument = restriction.restrictionArguments.get(0)
				if (restrictionArgument.valueType == null) { // The argument is not a Usertype/Typedef/Enum/Interface
					showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
				} else {
					val restrictionArgumentType = (restrictionArgument.valueType).type
					if (restrictionArgumentType instanceof Usertype) {
						if (!isUsertypeValidDefaultArgument(restrictionArgumentType, declaration)) {
							showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
						}
					} else if (restrictionArgumentType instanceof Typedef) {
						if (!isTypedefValidDefaultArgument(restrictionArgumentType, declaration)) {
							showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
						}
					} else {
						showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
					}
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Default_Not_One_Arg, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Default_Already_Used, restriction)
		}
	}

	def handleSingletonRestriction(Restriction restriction, boolean wasSingletonUsed, Usertype declaration) {
		if (!wasSingletonUsed) {
			if (restriction.restrictionArguments.size == 0) {
				if (subtypesFinder.getSubtypes(declaration).size != 0) {
					showError(TypeRestrictionsErrorMessages.Singleton_Usage, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Singleton_Has_Args, restriction)
			}
		} else {
			showWarning(TypeRestrictionsErrorMessages.Singleton_Already_Used, restriction)
		}
	}

	def handleUniqueRestriction(Restriction restriction, boolean wasUniqueUsed, Usertype declaration) {
		if (!wasUniqueUsed) {
			if (restriction.restrictionArguments.size == 0) {
				if ((declaration.supertypes.size != 0) || (subtypesFinder.getSubtypes(declaration).size != 0)) {
					showError(TypeRestrictionsErrorMessages.Unique_Usage, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Unique_Has_Args, restriction)
			}
		} else {
			showWarning(TypeRestrictionsErrorMessages.Unique_Already_Used, restriction)
		}
	}

}