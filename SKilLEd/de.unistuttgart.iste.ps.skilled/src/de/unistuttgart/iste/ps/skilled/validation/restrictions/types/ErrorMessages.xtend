package de.unistuttgart.iste.ps.skilled.validation.restrictions.types

class ErrorMessages {

	// All restriction warning and error messages
	protected static val Unknown_Restriction = "Unknown Restriction"
	protected static val Restriction_On_Map = "Map-typed fields can't have restrictions."
	// Unique restriction error messages
	protected static val Unique_Has_Args = "The Unique restriction can't have arguments."
	protected static val Unique_Usage = "The Unique restriction can only be used on types that do not have sub- or super-types."
	// Unique restriction warning messages
	protected static val Unique_Already_Used = "The Unique restriction is already used on this type."
	// Singleton restriction error messages
	protected static val Singleton_Has_Args = "The Singleton restriction can't have arguments."
	protected static val Singleton_Usage = "The Singleton restriction can only be used on types that do not have sub-types."
	// Singleton restriction warning messages
	protected static val Singleton_Already_Used = "The Singleton restriction is already used on this type."
	// Monotone restriction error messages
	protected static val Monotone_Has_Args = "The Monotone restriction can't have arguments."
	protected static val Monotone_Usage = "The Monotone restriction can only be used on base-types"
	// Monotone restriction warning messages
	protected static val Monotone_Already_Used = "The Monotone restriction is already used on this type."
	// Abstract restriction error messages
	protected static val Abstract_Has_Args = "The Abstract restriction can't have arguments."
	// Abstract restriction warning messages
	protected static val Abstract_Already_Used = "The Abstract restriction is already used on this type."
	// Default restriction error messages
	protected static val Default_Arg_Not_Singleton = "Restriction argument must be a singleton restricted sub-type."
	protected static val Default_Not_One_Arg = "The Default restriction must have one argument."
	// Default restriction warning messages
	protected static val Default_Already_Used = "The Default restriction is already used on this field."
}