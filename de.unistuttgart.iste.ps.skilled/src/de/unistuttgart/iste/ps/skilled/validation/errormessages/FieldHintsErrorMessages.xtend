package de.unistuttgart.iste.ps.skilled.validation.errormessages

class FieldHintsErrorMessages {
	
	// An environment independent newline
	public static val String newline = System.getProperty("line.separator");
	
	// All hints warnings and error messages
	public static val String Unknown_Hint = "Unknown Hint."
	// Remove Unknown Restrictions hint error messages
	public static val String RemoveUnknownRestrictions_Usage = "The argument(s) are not correct." + newline +
		"Use no arguments to remove all restrictions." + newline +
		"Use \"unknown\" to remove only the restrictions unknown to the generated binding." + newline +
		"Supported restrictions that can be removed: " +
		"\"unique\", \"singleton\", \"monotone\", \"abstract\", \"default\", \"nonNull\", \"range\", \"coding\", \"constantLengthPointer\", \"oneOf\"."

	public static val String RemoveUnknownRestrictions_Multiple_Used = "The Remove Unknown Restrictions hint is already used on this field."

	// Constant Mutator hint error messages
	public static val String ConstantMutator_First_Arg_Not_Integer = "The first argument must be an integer."
	public static val String ConstantMutator_Second_Arg_Not_Integer = "The second argument must be an integer."
	public static val String ConstantMutator_Not_2_Args = "The Constant Mutator hint must have two arguments."
	public static val String ConstantMutator_Usage = "The Constant Mutator hint can only be used on constants."
	public static val String ConstantMutator_Second_Arg_Smaller_Than_First = "The second argument must be bigger or equal to the first one."
	public static val String ConstantMutator_Multiple_Used = "The Constant Mutator hint is already used on this field."
	// Mixin hint error messages
	public static val String Mixin_Usage = "The Mixin hint can only be used on type declarations."
	// Flat hint error messages
	public static val String Flat_Usage = "The Flat hint can only be used on type declarations."
	// Unique hint error messages
	public static val String Unique_Usage = "The Unique hint can only be used on type declarations."
	// Pure hint error messages
	public static val String Pure_Usage = "The Pure hint can only be used type declarations."
	// Distributed hint warning messages
	public static val String Distributed_Multiple_Used = "The Distributed hint is already used on this field."
	// On Demand hint warning messages
	public static val String OnDemand_Multiple_Used = "The On Demand hint is already used on this field."
	// Monotone hint error messages
	public static val String Monotone_Usage = "The Monotone hint can only be used on base-types."
	// Read Only hint error messages
	public static val String ReadOnly_Usage = "The Read Only hint can only be used on base-types."
	// Ignore hint warning messages
	public static val String Ignore_Multiple_Used = "The Ignore hint is already used on this field."
	// Hide hint warning messages
	public static val String Hide_Multiple_Used = "The Hide hint is already used on this field."
	// Pragma hint error messages
	public static val String Pragma_Args_Not_Types = "The Pragma hint arguments must be types." + newline +
		"Syntax: !pragma <typeID> or !pragma <typeID>(<typeID>, ...)"
	public static val String Pragma_Not_One_Arg = "The Pragma hint must have at least one argument."
	public static val String Pragma_Multiple_Used = "The Pragma hint is already used on this field."

}