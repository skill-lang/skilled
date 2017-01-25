package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

import de.unistuttgart.iste.ps.skilled.skill.Declaration
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Restriction
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.skill.Usertype
import de.unistuttgart.iste.ps.skilled.validation.AbstractSkillValidator
import de.unistuttgart.iste.ps.skilled.validation.errormessages.TypeRestrictionsErrorMessages
import org.eclipse.emf.common.util.EList
import org.eclipse.xtext.validation.Check

/** 
 * @author Nikolay Fateev
 * @author Moritz Platzer
 * @author Daniel Ryan Degutis
 */
class AbstractTypeRestrictionsValidator extends AbstractSkillValidator {

	var protected Usertype underlyingUsertype = null
	var protected wasUniqueUsed = false
	var protected wasSingletonUsed = false
	var protected wasMonotoneUsed = false
	var protected wasAbstractUsed = false
	var protected wasDefaultUsed = false
	var protected wasRangeUsed = false
	var protected protewasMinUsed = false
	var protected wasMinUsed = false
	var protected wasMaxUsed = false
	var protected wasOneOfUsed = false

	@Check
	def validate(Declaration declaration) {
		prepare()
		if (handleActivationCondition(declaration)) {
		for (restriction : getRestrictions(declaration)) {
			switch (restriction.restrictionName.toLowerCase) {
				case 'unique': {
					handleUniqueRestriction(restriction, declaration)
					wasUniqueUsed = true
				}
				case 'singleton': {
					handleSingletonRestriction(restriction, declaration)
					wasSingletonUsed = true
				}
				case 'monotone': {
					handleMonotoneRestriction(restriction, declaration)
					wasMonotoneUsed = true
				}
				case 'abstract': {
					handleAbstractRestriction(restriction, declaration)
					wasAbstractUsed = true
				}
				case 'default': {
					handleDefaultRestriction(restriction, declaration)
					wasDefaultUsed = true
				}
				case 'range': {
					handleRangeRestriction(restriction, declaration)
					wasRangeUsed = true
				}
				case 'min': {
					handleMinRestriction(restriction, declaration)
					wasMinUsed = true
				}
				case 'max': {
					handleMaxRestriction(restriction, declaration)
					wasMaxUsed = true
				}
				case 'oneof': {
					handleOneOfRestriction(restriction, declaration)
				}
				default: {
					showError(TypeRestrictionsErrorMessages.Unknown_Restriction, restriction)
				}
			}
		}
		}
	}

	def private prepare() {
		underlyingUsertype = null
		wasUniqueUsed = false
		wasSingletonUsed = false
		wasMonotoneUsed = false
		wasAbstractUsed = false
		wasDefaultUsed = false
		wasRangeUsed = false
		wasMinUsed = false
		wasMinUsed = false
		wasMaxUsed = false
		wasOneOfUsed = false
	}

	def EList<Restriction> getRestrictions(Declaration declaration) {
		if (declaration instanceof Usertype) {
			return declaration.restrictions
		}
		if (declaration instanceof Typedef) {
			underlyingUsertype = returnTypedefDeclaration(declaration)
			if (underlyingUsertype != null) {
				wasUniqueUsed = isUserTypeRestricted(underlyingUsertype, "unique")
				wasSingletonUsed = isUserTypeRestricted(underlyingUsertype, "singleton")
				wasMonotoneUsed = isUserTypeRestricted(underlyingUsertype, "monotone")
				wasAbstractUsed = isUserTypeRestricted(underlyingUsertype, "abstract")
				wasDefaultUsed = isUserTypeRestricted(underlyingUsertype, "default")
				wasRangeUsed = isUserTypeRestricted(underlyingUsertype, "range")
				wasMinUsed = isUserTypeRestricted(underlyingUsertype, "min")
				wasMaxUsed = isUserTypeRestricted(underlyingUsertype, "max")
				wasOneOfUsed = isUserTypeRestricted(underlyingUsertype, "oneof")
			}
			return declaration.restrictions
		}
		return null;
	}
	
	def boolean handleActivationCondition(Declaration declaration) {
		return false
	}

	def void handleUniqueRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleSingletonRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleMonotoneRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleAbstractRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleDefaultRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleRangeRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleMinRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleMaxRestriction(Restriction restriction, Declaration declaration) {
		return
	}

	def void handleOneOfRestriction(Restriction restriction, Declaration declaration) {
		return
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

	def void showError(String message, Restriction restriction) {
		error(message, restriction, SkillPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}

	def void showWarning(String message, Restriction restriction) {
		warning(message, restriction, SkillPackage.Literals.RESTRICTION__RESTRICTION_NAME)
	}
}