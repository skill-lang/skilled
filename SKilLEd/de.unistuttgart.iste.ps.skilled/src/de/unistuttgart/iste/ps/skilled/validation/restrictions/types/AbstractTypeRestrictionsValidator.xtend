package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.validation.AbstractSKilLValidator

/** 
 * @author Nikolay Fateev
 * @author Moritz Platzer
 */
class AbstractTypeRestrictionsValidator extends AbstractSKilLValidator {

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