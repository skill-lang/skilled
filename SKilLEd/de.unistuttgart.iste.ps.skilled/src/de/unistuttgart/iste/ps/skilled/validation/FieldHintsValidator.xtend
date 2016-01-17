package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Constant
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Hint
import de.unistuttgart.iste.ps.skilled.sKilL.HintArgument
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import java.util.List
import org.eclipse.xtext.validation.Check

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
	
	
	private final static String[] removeUnknownRestrictions_Hint_Args = #["unique", "singleton", "monotone", "abstract",
		"default", "nonnull", "range", "coding", "constantlengthpointer", "oneof", "unknown"]

	// An environment independent newline
	private final static String newline = System.getProperty("line.separator");

	// All hint warning and error messages
	private final static String Unknown_Hint = "Unknown Hint"
	// Owner hint error messages
	private final static String Owner_Usage = "The Owner hint can only be used on base-type declarations."
	// Owner hint warning messages
	private final static String Owner_Already_Used = "The Owner hint is already used on this field."
	// Provider hint error messages
	private final static String Provider_Not_One_Arg = "The Provider hint must have one argument."
	private final static String Provider_Arg_Not_Type = "The Provider hint argument must be a tool."
	private final static String Provider_Already_Used = "The Provider hint is already used on this field."
	// Remove Unknown Restrictions hint error messages
	private final static String RemoveUnknownRestrictions_Usage = "The argument(s) are not correct." + newline +
		"Use no arguments to remove all restrictions." + newline +
		"Use \"unknown\" to remove only the restrictions unknown to the generated binding." + newline +
		"Supported restrictions that can be removed: " +
		"\"unique\", \"singleton\", \"monotone\", \"abstract\", \"default\", \"nonNull\", \"range\", \"coding\", \"constantLengthPointer\", \"oneOf\"."
	private final static String RemoveUnknownRestrictions_Multiple_Used = "The Remove Unknown Restrictions hint is already used on this field."
	// Constant Mutator hint error messages
	private final static String ConstantMutator_First_Arg_Not_Integer = "The first argument must be an integer."
	private final static String ConstantMutator_Second_Arg_Not_Integer = "The second argument must be an integer."
	private final static String ConstantMutator_Not_2_Args = "The Constant Mutator hint must have two arguments."
	private final static String ConstantMutator_Usage = "The Constant Mutator hint can only be used on constants."
	private final static String ConstantMutator_Second_Arg_Smaller_Than_First = "The second argument must be bigger or equal to the first one."
	private final static String ConstantMutator_Multiple_Used = "The Constant Mutator hint is already used on this field."
	// Mixin hint error messages
	private final static String Mixin_Usage = "The Mixin hint can only be used on type declarations."
	// Mixin hint warning messages
	private final static String Mixin_Multiple_Used = "The Mixin hint is already used on this field."
	// Flat hint error messages
	private final static String Flat_Usage = "The Flat hint can only be used on type declarations."
	// Flat hint warning messages
	private final static String Flat_Multiple_Used = "The Flat hint is already used on this field."
	// Unique hint error messages
	private final static String Unique_Usage = "The Unique hint can only be used on type declarations."
	// Unique hint warning messages
	private final static String Unique_Multiple_Used = "The Unique hint is already used on this field."
	// Pure hint error messages
	private final static String Pure_Usage = "The Pure hint can only be used type declarations."
	// Pure hint warning messages
	private final static String Pure_Multiple_Used = "The Pure hint is already used on this field."
	// Distributed hint warning messages
	private final static String Distributed_Multiple_Used = "The Distributed hint is already used on this field."
	// On Demand hint warning messages
	private final static String OnDemand_Multiple_Used = "The On Demand hint is already used on this field."
	// Monotone hint error messages
	private final static String Monotone_Usage = "The Monotone hint can only be used on base-type declarations."
	// Monotone hint warning messages
	private final static String Monotone_Multiple_Used = "The Monotone hint is already used on this field."
	// Read Only hint error messages
	private final static String ReadOnly_Usage = "The Read Only hint can only be used on base-type declarations."
	// Read Only hint warning messages
	private final static String ReadOnly_Multiple_Used = "The Read Only hint is already used on this field."
	// Ignore hint warning messages
	private final static String Ignore_Multiple_Used = "The Ignore hint is already used on this field."
	// Hide hint warning messages
	private final static String Hide_Multiple_Used = "The Hide hint is already used on this field."
	// Pragma hint warning messages
	private final static String Pragma_Multiple_Used = "The Pragma hint is already used on this field."
		
	@Check
	def validateFieldHints(Field field) {
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
		var wasHideUsed = false
		var wasPragmaUsed = false
		for (hint : field.hints) {
			switch (hint.hintName) {
				case 'owner': {
					if (!wasOwnerUsed) {
						showError(Owner_Usage, hint)
						wasOwnerUsed = true
					} else {
						showWarning(Owner_Already_Used, hint)
					}					
				}
				case 'provider': {
					if (!wasProviderUsed) {
						if (hint.hintArguments.size == 1) {	// Can there be many args?
							if (hint.hintArguments.get(0).valueType == null) {	// How to check for tools names?
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
						if (!areRemoveUnknownRestrictionArgumentsCorrect(hint.hintArguments)) {		// Combination of "unknown" + something else valid?
							showError(RemoveUnknownRestrictions_Usage, hint)
						}
						wasRemoveUnknownRestrictionUsed = true
					} else {
						showError(RemoveUnknownRestrictions_Multiple_Used, hint)
					}
				}
				case 'constantmutator': {
					if (!wasConstantMutatorUsed) {
						if (field.fieldcontent instanceof Constant) {
							var hintArguments = hint.hintArguments
							if (hintArguments.size == 2) {
								if (hintArguments.get(0).valueLong == null) {
									showError(ConstantMutator_First_Arg_Not_Integer, hint)
								} else if (hintArguments.get(1).valueLong == null) {
									showError(ConstantMutator_Second_Arg_Not_Integer, hint)
								} else {
									if (hintArguments.get(1).valueLong < hintArguments.get(0).valueLong) {
										showError(ConstantMutator_Second_Arg_Smaller_Than_First, hint)
									}
								}
							} else {
								showError(ConstantMutator_Not_2_Args, hint)
							}
						} else {
							showError(ConstantMutator_Usage, hint)
						}
						wasConstantMutatorUsed = true
					} else {
						showError(ConstantMutator_Multiple_Used, hint)
					}
				}
				case 'mixin': {
					if (!wasMixinUsed) {
						showError(Mixin_Usage, hint)	// Is legal on field? 
						wasMixinUsed = true
					} else {
						showWarning(Mixin_Multiple_Used, hint) // If above legal,  Warning or error?
					}
				}
				case 'flat': {
					if (!wasFlatUsed) {
						showError(Flat_Usage, hint)
						wasFlatUsed = true
					} else {
						showWarning(Flat_Multiple_Used, hint)
					}
				}
				case 'unique': {
					if (!wasUniqueUsed) {
						showError(Unique_Usage, hint)
						wasUniqueUsed = true
					} else {
						showWarning(Unique_Multiple_Used, hint)
					}
				}
				case 'pure': {
					if (!wasPureUsed) {
						showError(Pure_Usage, hint)
						wasPureUsed = true
					} else {
						showWarning(Pure_Multiple_Used, hint)
					}
				}
				case 'distributed': {
					if (!wasDistributedUsed) {
						wasDistributedUsed = true
					} else {
						showWarning(Distributed_Multiple_Used, hint) // Warning or error?
					}
				}
				case 'ondemand': {
					if (!wasOnDemandUsed) {
						wasOnDemandUsed = true
					} else {
						showWarning(OnDemand_Multiple_Used, hint) // Warning or error?
					}
				}
				case 'monotone': {
					if (!wasMonotoneUsed) {
						showError(Monotone_Usage, hint)
						wasMonotoneUsed = true
					} else {
						showWarning(Monotone_Multiple_Used, hint)
					}
				}
				case 'readonly': {
					if (!wasReadOnlyUsed) {
						showError(ReadOnly_Usage, hint)
						wasReadOnlyUsed = true
					} else {
						showWarning(ReadOnly_Multiple_Used, hint)
					}
				}
				case 'ignore': {
					if (!wasIgnoreUsed) {
						wasIgnoreUsed = true
					} else {
						showWarning(Ignore_Multiple_Used, hint) // Warning or error?
					}
				}
				case 'hide': {
					if (!wasHideUsed) {
						wasHideUsed = true
					} else {
						showWarning(Hide_Multiple_Used, hint) // Warning or error?
					}
				}
				case 'pragma': {
					if (!wasPragmaUsed) {
						wasPragmaUsed = true // Legal args?
					} else {
						showWarning(Pragma_Multiple_Used, hint) // Warning or error?
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
				if (!removeUnknownRestrictions_Hint_Args.contains(hintArgument.valueString.toLowerCase)) {
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