package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.skill.Annotationtype
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Fieldtype
import de.unistuttgart.iste.ps.skilled.skill.Interfacetype
import de.unistuttgart.iste.ps.skilled.skill.Restriction
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.skill.Usertype
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages
import org.eclipse.emf.common.util.EList

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 * @author Tobias Heck
 */
class AnnotationTypeFieldRestrictionsValidator extends AbstractFieldRestrictionsValidator {


	override handleActivationCondition(Fieldtype fieldtype) {
		if(fieldtype instanceof Annotationtype) return true
		if (fieldtype instanceof DeclarationReference) {
			if (fieldtype.type instanceof Typedef) {
				if ((fieldtype.type as Typedef).fieldtype instanceof Annotationtype) {
					for (Restriction r : (fieldtype.type as Typedef).restrictions) {
						switch (r.restrictionName.toLowerCase) {
							case 'nonnull': wasNonNullUsed = true
							case 'default': wasDefaultUsed = true
							case 'constantlengthpointer': wasConstantLenghtPointerUsed = true
							case 'oneof': wasOneOfUsed = true
						}
					}
					return true
				}
			}
		}
		return false
	}

	override handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (wasDefaultUsed) {
			showError(FieldRestrictionErrorMessages.Default_Already_Used, restriction)
			return
		}

		if (restriction.restrictionArguments.size() == 1) {
			val restrictionArgument = restriction.restrictionArguments.get(0)
			if (restrictionArgument.valueType == null) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, restriction)
			} else {
				val restrictionArgumentType = (restrictionArgument.valueType).type
				if (restrictionArgumentType instanceof Typedef) {
					handleDefaultRestrictionTypedef(restrictionArgumentType.restrictions, restriction)
				} else if (restrictionArgumentType instanceof Usertype) {
					handleDefaultRestrictionTypedef(restrictionArgumentType.restrictions, restriction)
				} else if (restrictionArgumentType instanceof Interfacetype) {
					showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, restriction)
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
		}
	}

	def private handleDefaultRestrictionTypedef(EList<Restriction> rs, Restriction r) {
		for (typedefRestriction : rs) {
			if ("singleton".equals(typedefRestriction.restrictionName.toLowerCase)) {
				return
			}
		}
		showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, r)
	}

	override handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.ConstantLengthPointer_Has_Args, restriction)
		} else if (wasConstantLenghtPointerUsed) {
			showWarning(FieldRestrictionErrorMessages.ConstantLengthPointer_Already_Used, restriction)
		}
	}

	override handleOneOfRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (wasOneOfUsed) {
			showError(FieldRestrictionErrorMessages.OneOf_Already_Used, restriction)
			return
		}

		if (restriction.restrictionArguments.size() >= 1) {
			for (restrictionArgument : restriction.restrictionArguments) {
				if (restrictionArgument.valueType == null) {
					showError(FieldRestrictionErrorMessages.OneOf_Arg_Not_Usertype, restriction)
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.OneOf_Not_One_Arg, restriction)
		}
	}

}