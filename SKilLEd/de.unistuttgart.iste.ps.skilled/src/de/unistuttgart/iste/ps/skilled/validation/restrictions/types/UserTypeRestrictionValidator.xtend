package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.skill.Declaration
import de.unistuttgart.iste.ps.skilled.skill.Restriction
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.skill.Usertype
import de.unistuttgart.iste.ps.skilled.util.SubtypesFinder
import de.unistuttgart.iste.ps.skilled.validation.errormessages.TypeRestrictionsErrorMessages

/**
 * @author Nikolay Fateev
 * @author Moritz Platzer
 * @author Daniel Ryan Degutis
 */
class UserTypeRestrictionValidator extends AbstractTypeRestrictionsValidator {

	@Inject
	private SubtypesFinder subtypesFinder
	
	override handleActivationCondition(Declaration declaration) {
		return declaration instanceof Usertype
	}

	override handleAbstractRestriction(Restriction restriction, Declaration declaration) {
		if (restriction.restrictionArguments.size != 0) {
			showError(TypeRestrictionsErrorMessages.Abstract_Has_Args, restriction)
		} else if (wasAbstractUsed) {
			showWarning(TypeRestrictionsErrorMessages.Abstract_Already_Used, restriction)
		}
	}

	override handleMonotoneRestriction(Restriction restriction, Declaration declaration) {
		if (!wasMonotoneUsed) {
			if (restriction.restrictionArguments.size == 0) {
				if ((declaration as Usertype).supertypes.size != 0) {
					showError(TypeRestrictionsErrorMessages.Monotone_Usage, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Monotone_Has_Args, restriction)
			}
		} else {
			showWarning(TypeRestrictionsErrorMessages.Monotone_Already_Used, restriction)
		}
	}

	override handleDefaultRestriction(Restriction restriction, Declaration declaration) {
		if (!wasDefaultUsed) {
			if (restriction.restrictionArguments.size() == 1) {
				val restrictionArgument = restriction.restrictionArguments.get(0)
				if (restrictionArgument.valueType == null) {
					showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
				} else {
					val restrictionArgumentType = (restrictionArgument.valueType).type
					if (restrictionArgumentType instanceof Usertype) {
						if (!isUsertypeValidDefaultArgument(restrictionArgumentType, declaration as Usertype)) {
							showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
						}
					} else if (restrictionArgumentType instanceof Typedef) {
						if (!isTypedefValidDefaultArgument(restrictionArgumentType, declaration as Usertype)) {
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

	override handleSingletonRestriction(Restriction restriction, Declaration declaration) {
		if (!wasSingletonUsed) {
			if (restriction.restrictionArguments.size == 0) {
				if (subtypesFinder.getSubtypes(declaration as Usertype).size != 0) {
					showError(TypeRestrictionsErrorMessages.Singleton_Usage, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Singleton_Has_Args, restriction)
			}
		} else {
			showWarning(TypeRestrictionsErrorMessages.Singleton_Already_Used, restriction)
		}
	}

	override handleUniqueRestriction(Restriction restriction, Declaration declaration) {
		if (!wasUniqueUsed) {
			if (restriction.restrictionArguments.size == 0) {
				if (((declaration as Usertype).supertypes.size != 0) ||
					(subtypesFinder.getSubtypes(declaration as Usertype).size != 0)) {
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