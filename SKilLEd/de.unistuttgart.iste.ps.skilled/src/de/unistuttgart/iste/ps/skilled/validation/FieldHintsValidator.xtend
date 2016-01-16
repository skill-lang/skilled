package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Hint
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import org.eclipse.xtext.validation.Check
import java.util.List
import de.unistuttgart.iste.ps.skilled.sKilL.HintArgument

/** 
 * Validates field hints and their arguments. <br>
 * <br>
 * Supported hints: Owner, Provider, Remove Unknown Restrictions, Constant Mutator, <br>
 * Mixin, Flat, Unique, Pure, Distributed, OnDemand, Monotone, ReadOnly, Ignore, Hide, Pragma <br>
 * <br>
 * The error/warning messages give context to the conditions. <br>
 * <br>
 * Guide to hint arguments (parameters): <br>
 * hintArgument.valueType 	- a declaration (typedef, enum, interface, usertype) type argument <br>
 * hintArgument.valueString - a string type argument <br>
 * hintArgument.valueLong 	- a integer type argument <br>
 * hintArgument.valueDouble	- a float type argument <br>
 * 
 * 
 * @author Nikolay Fateev
 */
class FieldHintsValidator extends AbstractSKilLValidator {
	
	private final static String[] restrictionNames = #[ "unique", "singleton", "monotone", "abstract", "default", "nonnull", "range", "coding", "constantlengthpointer", "oneof", "unknown" ]
	private final static String newline = System.getProperty("line.separator");

	// All hint warning and error messages
	private final static String Unknown_Hint = "Unknown Hint"
	// Owner hint error messages
	private final static String Owner_Usage = "The Owner hint can only be used on strings and user-types."
	// Owner hint warning messages
	private final static String Owner_Already_Used = "The Owner hint is already used on this field."
	// Provider hint error messages
	private final static String Provider_Not_One_Arg = "The Provider hint must have one argument."
	private final static String Provider_Arg_Not_Type = "The Provider hint argument must be a tool."
	private final static String Provider_Already_Used = "The Provider hint is already used on this field."
	// Range hint error messages
	private final static String RemoveUnknownRestrictions_Usage = "The argument(s) are not correct."+newline+"Use no arguments to remove all restrictions."+newline+
	"Use \"unknown\" to remove only the restrictions unknown to the generated binding."+newline+"Otherwise the following restrictions are supported: "+
	"\"unique\", \"singleton\", \"monotone\", \"abstract\", \"default\", \"nonnull\", \"range\", \"coding\", \"constantlengthpointer\", \"oneof\" "
	private final static String RemoveUnknownRestrictions_Multiple_Used = "The Remove Unknown Restrictions hint is already used on this field."
	// Max hint error messages
	private final static String Max_Arg_Not_Integer = "Hint argument must be an integer."
	private final static String Max_Arg_Not_Float = "Hint argument must be a float."
	private final static String Max_First_Arg_Not_Integer = "The first argument must be an integer."
	private final static String Max_First_Arg_Not_Float = "The first argument must be an float."
	private final static String Max_Second_Arg_Not_Lowercase_String = "The second argument must be a lowercase string."
	private final static String Max_Not_1or2_Args = "The Max hint must have one or two arguments."
	private final static String Max_Usage = "The Max hint can only be used on integers and floats."
	// Max hint warning messages
	private final static String Max_Multiple_Used = "The use of multiple Max hints is highly discouraged. The correctness of the intersection is not checked."
	// Min hint error messages
	private final static String Min_Arg_Not_Integer = "Hint argument must be an integer."
	private final static String Min_Arg_Not_Float = "Hint argument must be a float."
	private final static String Min_First_Arg_Not_Integer = "The first argument must be an integer."
	private final static String Min_First_Arg_Not_Float = "The first argument must be an float."
	private final static String Min_Second_Arg_Not_Lowercase_String = "The second argument must be a lowercase string."
	private final static String Min_Not_1or2_Args = "The Min hint must have one or two arguments."
	private final static String Min_Usage = "The Min hint can only be used on integers and floats."
	// Min hint warning messages
	private final static String Min_Multiple_Used = "The use of multiple Min hints is highly discouraged. The correctness of the intersection is not checked."
	// Coding hint error messages
	private final static String Coding_Arg_Not_String = "The Coding hint argument value must be a string."
	private final static String Coding_Not_One_Arg = "The Coding hint must have one argument."
	// Coding hint warning messages
	private final static String Coding_Usage_Discouraged = "The use of the Coding hint is discouraged."
	// ConstantLengthPointer hint error messages
	private final static String ConstantLengthPointer_Has_Args = "The Constant Length Pointer hint can't have arguments."
	private final static String ConstantLengthPointer_Usage = "The Constant Length Pointer hint can only be used on strings, annotations and user-types."
	// ConstantLengthPointer hint warning messages
	private final static String ConstantLengthPointer_Already_Used = "The Constant Length Pointer hint is already used on this field."
	// OneOf hint error messages
	private final static String OneOf_Arg_Not_Usertype = "Hint argument must be a user-type."
	private final static String OneOf_Not_One_Arg = "The One Of hint requires at least one argument."
	private final static String OneOf_Usage = "The One Of hint can only be used on annotations and user-types."
	// OneOf hint warning messages
	private final static String OneOf_Already_Used = "The One Of hint is already used on this field."


	@Check
	def validateFieldHints(Field field) {
		val fieldTypeName = (field.fieldcontent.fieldtype as DeclarationReference).type.name
		var wasOwnerUsed = false
		var wasProviderUsed = false
		var wasRemoveUnknownRestrictionUsed = false
		var wasConstantMutatorUsed = false
		var wasMixinUsed = false
		var wasFlatUsed = false
		var wasUniqueUsed = false
		var wasPureUsed = false
		var wasDistributedUsed = false
		var wasOnDemandUsed = false
		var wasMonotoneUsed = false
		var wasReadOnlyUsed = false
		var wasIgnoreUsed = false
		var wasPragmaUsed = false
		for (hint : field.hints) {
			switch (hint.hintName) {
				case 'owner': {
					if (!wasOwnerUsed) {
						//showError(Owner_Usage, hint)
						wasOwnerUsed = true
					} else {
						showWarning(Owner_Already_Used, hint)
					}					
				}
				case 'provider': {
					if (!wasProviderUsed) {
						if (hint.hintArguments.size == 1) {	// Or many?
							if (hint.hintArguments.get(0).valueType == null) {	// valueType?
								showError(Provider_Arg_Not_Type, hint)
							}
						} else {
							showError(Provider_Not_One_Arg, hint)
						}
						wasProviderUsed = true
					} else {
						showError(Provider_Already_Used, hint)
					}					
				}
				case 'removeunknownrestrictions': {
					if (!wasRemoveUnknownRestrictionUsed) {
						if (!areRemoveUnknownRestrictionArgumentsCorrect(hint.hintArguments)) {
							showError(RemoveUnknownRestrictions_Usage, hint)
						}
						wasRemoveUnknownRestrictionUsed = true
					} else {
						showError(RemoveUnknownRestrictions_Multiple_Used, hint)
					}
				}
				default: {
					showError(Unknown_Hint, hint)
				}
			}
		}
	}
	
	def boolean areRemoveUnknownRestrictionArgumentsCorrect(List<HintArgument> arguments) {
		for (hintArgument : arguments) {
			if (hintArgument.valueString != null) {
				if (!restrictionNames.contains(hintArgument.valueString.toLowerCase)) {
					return false
				}			
			} else {
				return false
			}	
		}
		return true
	}

	def showError(String message, Hint hint) {
		error(message, hint, SKilLPackage.Literals.HINT__HINT_NAME)
	}

	def showWarning(String message, Hint hint) {
		warning(message, hint, SKilLPackage.Literals.HINT__HINT_NAME)
	}
}