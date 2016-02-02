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
 * @author Tobias Heck
 */
class TypedefRestrictionValidator extends AbstractTypeRestrictionsValidator {

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
			var wasRangeUsed = false
			var wasMinUsed = false
			var wasMaxUsed = false

			if (underlyingUsertype != null) {
				wasUniqueUsed = isUserTypeRestricted(underlyingUsertype, "unique")
				wasSingletonUsed = isUserTypeRestricted(underlyingUsertype, "singleton")
				wasMonotoneUsed = isUserTypeRestricted(underlyingUsertype, "monotone")
				wasAbstractUsed = isUserTypeRestricted(underlyingUsertype, "abstract")
				wasDefaultUsed = isUserTypeRestricted(underlyingUsertype, "default")
				wasRangeUsed = isUserTypeRestricted(underlyingUsertype, "range")
				wasMinUsed = isUserTypeRestricted(underlyingUsertype, "min")
				wasMaxUsed = isUserTypeRestricted(underlyingUsertype, "max")
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
					case 'range': {
						handleRangeRestriction(restriction, underlyingUsertype, wasRangeUsed)
						wasRangeUsed = true
					}
					case 'min': {
						handleMinRestriction(restriction, underlyingUsertype, wasMinUsed)
						wasMinUsed = true
					}
					case 'max': {
						handleMaxRestriction(restriction, underlyingUsertype, wasMaxUsed)
						wasMaxUsed = true
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

	def handleSingletonRestriction(Restriction restriction, Usertype underlyingUsertype, boolean wasSingletonUsed) {
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

	def handleUniqueRestriction(Restriction restriction, Usertype underlyingUsertype, boolean wasUniqueUsed) {
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

	def handleDefaultRestriction(Restriction restriction, Usertype underlyingUsertype, boolean wasDefaultUsed) {
		//TODO default bool, string, int, float
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
					showError(TypeRestrictionsErrorMessages.Default_Arg_Not_Singleton, restriction)
				}
			} else {
				showError(TypeRestrictionsErrorMessages.Default_Not_One_Arg, restriction)
			}
		} else {
			showError(TypeRestrictionsErrorMessages.Default_Already_Used, restriction)
		}
	}
	
	def handleRangeRestriction(Restriction restriction, Usertype underlyingUsertape, boolean wasRangeUsed) {
		if (restriction.restrictionArguments.size() != 2) {
			
		}
	}
	
	def handleMinRestriction(Restriction restriction, Usertype underlyingUsertape, boolean wasMinUsed) {
		
	}
	
	def handleMaxRestriction(Restriction restriction, Usertype underlyingUsertape, boolean wasMaxUsed) {
		
	}
	
}