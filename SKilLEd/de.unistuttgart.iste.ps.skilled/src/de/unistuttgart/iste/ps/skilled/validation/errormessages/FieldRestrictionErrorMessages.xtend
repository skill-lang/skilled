package de.unistuttgart.iste.ps.skilled.validation.errormessages

class FieldRestrictionErrorMessages {
	
	// All restriction warning and error messages
	public static val String Unknown_Restriction = "Unknown Restriction"
	public static val Restriction_On_Map = "Map-typed fields can't have restrictions."
	// NonNull restriction error messages
	public static val NonNull_Has_Args = "The Non Null restriction can't have arguments."
	public static val NonNull_Usage = "The Non Null restriction can only be used on strings and user-types."
	// NonNull restriction warning messages
	public static val NonNull_Already_Used = "The Non Null restriction is already used on this field."
	// Default restriction error messages
	public static val Default_Arg_Not_Singleton = "Restriction argument must be a singleton restricted sub-type."
	public static val Default_Arg_Not_Singleton_Or_Enum = "Restriction argument must be an enum or a singleton restricted type."
	public static val Default_Arg_Not_Integer = "Restriction argument must be an integer."
	public static val Default_Arg_Not_Float = "Restriction argument must be a float."
	public static val Default_Arg_Not_String = "Restriction argument must be a string."
	public static val Default_Not_One_Arg = "The Default restriction must have one argument."
	public static val Default_Usage = "The Default restriction can only be used on strings, integers, floats, annotations and user-types."
	// Default restriction warning messages
	public static val Default_Already_Used = "The Default restriction is already used on this field."
	// Range restriction error messages
	public static val Range_First_Arg_Not_Integer = "The first argument must be an integer."
	public static val Range_First_Arg_Not_Float = "The first argument must be an float."
	public static val Range_Second_Arg_Not_Integer = "The second argument must be an integer."
	public static val Range_Second_Arg_Not_Float = "The second argument must be an float."
	public static val Range_Third_Arg_Not_Lowercase_String = "The third argument must be a lowercase string."
	public static val Range_Fourth_Arg_Not_Lowercase_String = "The fourth argument must be a lowercase string."
	public static val Range_Not_2or4_Args = "The Range restriction must have two or four arguments."
	public static val Range_Usage = "The Range restriction can only be used on integers and floats."
	// Range restriction warning messages
	public static val Range_Multiple_Used = "The use of multiple Range restrictions is highly discouraged. The correctness of the intersection is not checked."
	// Max restriction error messages
	public static val Max_Arg_Not_Integer = "Restriction argument must be an integer."
	public static val Max_Arg_Not_Float = "Restriction argument must be a float."
	public static val Max_First_Arg_Not_Integer = "The first argument must be an integer."
	public static val Max_First_Arg_Not_Float = "The first argument must be an float."
	public static val Max_Second_Arg_Not_Lowercase_String = "The second argument must be a lowercase string."
	public static val Max_Not_1or2_Args = "The Max restriction must have one or two arguments."
	public static val Max_Usage = "The Max restriction can only be used on integers and floats."
	// Max restriction warning messages
	public static val Max_Multiple_Used = "The use of multiple Max restrictions is highly discouraged. The correctness of the intersection is not checked."
	// Min restriction error messages
	public static val Min_Arg_Not_Integer = "Restriction argument must be an integer."
	public static val Min_Arg_Not_Float = "Restriction argument must be a float."
	public static val Min_First_Arg_Not_Integer = "The first argument must be an integer."
	public static val Min_First_Arg_Not_Float = "The first argument must be an float."
	public static val Min_Second_Arg_Not_Lowercase_String = "The second argument must be a lowercase string."
	public static val Min_Not_1or2_Args = "The Min restriction must have one or two arguments."
	public static val Min_Usage = "The Min restriction can only be used on integers and floats."
	// Min restriction warning messages
	public static val Min_Multiple_Used = "The use of multiple Min restrictions is highly discouraged. The correctness of the intersection is not checked."
	// Coding restriction error messages
	public static val Coding_Arg_Not_String = "The Coding restriction argument value must be a string."
	public static val Coding_Not_One_Arg = "The Coding restriction must have one argument."
	// Coding restriction warning messages
	public static val Coding_Usage_Discouraged = "The use of the Coding restriction is discouraged."
	// ConstantLengthPointer restriction error messages
	public static val ConstantLengthPointer_Has_Args = "The Constant Length Pointer restriction can't have arguments."
	public static val ConstantLengthPointer_Usage = "The Constant Length Pointer restriction can only be used on strings, annotations and user-types."
	// ConstantLengthPointer restriction warning messages
	public static val ConstantLengthPointer_Already_Used = "The Constant Length Pointer restriction is already used on this field."
	// OneOf restriction error messages
	public static val OneOf_Arg_Not_Usertype = "Restriction argument must be a user-type."
	public static val OneOf_Not_One_Arg = "The One Of restriction requires at least one argument."
	public static val OneOf_Usage = "The One Of restriction can only be used on annotations and user-types."
	// OneOf restriction warning messages
	public static val OneOf_Already_Used = "The One Of restriction is already used on this field."
}