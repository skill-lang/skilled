package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Enuminstance
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.util.SubtypesFinder
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages
import java.util.Set

/**
 * @author Daniel Ryan Degutis
 * @author Nikolay Fateev
 * @author Tobias Heck
 */
class UserTypeFieldRestrictions extends AbstractFieldRestrictionsValidator {
	
	@Inject
	private SubtypesFinder subtypes

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		if (fieldtype instanceof DeclarationReference) {
			if (fieldtype.type instanceof Typedef) {
				if ((fieldtype.type as Typedef).fieldtype instanceof Integertype)
					return false
				if ((fieldtype.type as Typedef).fieldtype instanceof Floattype)
					return false
				if ((fieldtype.type as Typedef).fieldtype instanceof Annotationtype) {
					return false
				}
			}
			return true
		}
		return false
	}

	override void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.NonNull_Has_Args, restriction)
		} else if (wasNonNullUsed) {
			showWarning(FieldRestrictionErrorMessages.NonNull_Already_Used, restriction)
		}
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (wasDefaultUsed) {
			showError(FieldRestrictionErrorMessages.Default_Already_Used, restriction)
			return
		}

		if (restriction.restrictionArguments.size() == 1) {
			if (restriction.restrictionArguments.get(0).valueType == null) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
			} else {
				handleIsRestrictionArgumentUsertype(fieldtype, restriction)
			}
		} else {
			showError(FieldRestrictionErrorMessages.Default_Not_One_Arg, restriction)
		}
	}

	def private void handleIsRestrictionArgumentUsertype(Fieldtype fieldtype, Restriction restriction) {
		val restrictionArgumentType = (restriction.restrictionArguments.get(0).valueType).type

		if (restrictionArgumentType instanceof Usertype) {
			if (!isRestrictionArgumentSubTypeOfField(restrictionArgumentType, fieldtype)) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
			} else if (!isRestrictionArgumentTypeSingletonRestricted(restrictionArgumentType)) {
				showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton, restriction)
			}
		} else if (restrictionArgumentType instanceof Enumtype) {
			if ((fieldtype as DeclarationReference).type instanceof Enumtype) {
				if (restrictionArgumentType == (fieldtype as DeclarationReference).type) {
					if (restriction.restrictionArguments.get(0).enumInstance == null ||
							restriction.restrictionArguments.get(0).enumInstance == "") {
						showWarning(FieldRestrictionErrorMessages.Default_Redundant, restriction)
					}
					for (Enuminstance i : restrictionArgumentType.instances) {
						if (i.name.toLowerCase.equals(restriction.restrictionArguments.get(0).enumInstance.toLowerCase))
						return
					}
					showError(FieldRestrictionErrorMessages.Default_Instance_Does_Not_Exist, restriction)
				} else {
					showError(FieldRestrictionErrorMessages.Default_Wrong_Enum, restriction)
				}
			} else {
				showError(FieldRestrictionErrorMessages.Default_Arg_Type_Mismatch, restriction)
			}
		} else {
			showError(FieldRestrictionErrorMessages.Default_Arg_Not_Singleton_Or_Enum, restriction)
		}
	}

	def private boolean isRestrictionArgumentSubTypeOfField(Usertype ut, Fieldtype fieldtype) {
		val fieldTypeName = (fieldtype as DeclarationReference).type.name

		for (supertype : ut.supertypes) {
			if (fieldTypeName.equals(supertype.type.name)) {
				return true
			}
		}
		return false
	}

	def private boolean isRestrictionArgumentTypeSingletonRestricted(Usertype ut) {
		for (restrictionArgumentRestriction : ut.restrictions) {
			if ("singleton".equals(restrictionArgumentRestriction.restrictionName.toLowerCase)) {
				return true
			}
		}
		return false
	}

	override void handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (restriction.restrictionArguments.size() != 0) {
			showError(FieldRestrictionErrorMessages.ConstantLengthPointer_Has_Args, restriction)
		} else if (wasConstantLenghtPointerUsed) {
			showWarning(FieldRestrictionErrorMessages.ConstantLengthPointer_Already_Used, restriction)
		}
	}

	override void handleOneOfRestriction(Fieldtype fieldtype, Restriction restriction) {
		if (wasOneOfUsed) {
			showError(FieldRestrictionErrorMessages.OneOf_Already_Used, restriction)
			return
		}
		
		var TypeDeclaration type = null
		
		if ((fieldtype as DeclarationReference).type instanceof TypeDeclaration) {
			type = (fieldtype as DeclarationReference).type as TypeDeclaration
		} else {
			showError(FieldRestrictionErrorMessages.OneOf_Usage, restriction)
			return
		}

		if (restriction.restrictionArguments.size() >= 1) {
			val Set<TypeDeclaration> subtype = subtypes.getSubtypes(type)
			for (restrictionArgument : restriction.restrictionArguments) {
				if (restrictionArgument.valueType == null) {
					showError(FieldRestrictionErrorMessages.OneOf_Arg_Not_Usertype, restriction)
					return
				}
				var boolean isSubtype = false
				for (TypeDeclaration decl : subtype) {
					if (decl == restrictionArgument.valueType.type || restrictionArgument.valueType.type == type) {
						isSubtype = true
					}
				}
				if (!isSubtype)  {
					showError(FieldRestrictionErrorMessages.OneOf_Arg_Not_Subtype, restriction)
					return
				}
				if (restrictionArgument.valueType.type == type) {
					showWarning(FieldRestrictionErrorMessages.OneOf_Arg_Is_Type, restriction)
				}
			}
		} else {
			showError(FieldRestrictionErrorMessages.OneOf_Not_One_Arg, restriction)
		}
	}
}