package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.skill.Declaration
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Hint
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import de.unistuttgart.iste.ps.skilled.skill.Usertype
import org.eclipse.xtext.validation.Check

/** 
 * Validates type hints and their arguments. <br>
 * <br>
 * Supported hints: Owner, Provider, Remove Unknown Restrictions, Constant Mutator, <br>
 * Mixin, Flat, Unique, Pure, Distributed, OnDemand, Monotone, ReadOnly, Ignore, Hide, Pragma. <br>
 * <br>
 * The error/warning messages give context to the conditions. <br>
 * <br>
 * Guide to hint arguments (parameters): <br>
 * hintArgument.valueType 	- a declaration (typedef, enum, interface, usertype) type argument <br>
 * hintArgument.valueString - a string type argument <br>
 * hintArgument.valueLong 	- a integer type argument <br>
 * hintArgument.valueDouble	- a float type argument <br>
 * 
 * @author Nikolay Fateev
 */
class TypeHintsValidator extends AbstractSKilLComposedValidatorPart {

  private final static String[] removeUnknownRestrictions_Hint_Args = #["unique", "singleton", "monotone", "abstract",
    "default", "nonnull", "range", "coding", "constantlengthpointer", "oneof", "unknown"]

  // An environment independent newline
  private final static String newline = System.getProperty("line.separator");

  // All hints warnings and error messages
  private final static String Unknown_Hint = "Unknown Hint."
  private final static String Hint_Usage = "Hints can be only be used on user-types."
  // Owner hint error messages
  private final static String Owner_No_Args = "The Owner hint must have at least one argument."
  private final static String Owner_Usage = "The Owner hint can only be used on base-types."
  private final static String Owner_Already_Used = "The Owner hint is already used on this field."
  // Provider hint error messages
  private final static String Provider_No_Args = "The Provider hint must have at least one argument."
  private final static String Provider_Already_Used = "The Provider hint is already used on this field."
  // Remove Unknown Restrictions hint error messages
  private final static String RemoveUnknownRestrictions_Usage = "The argument(s) are not correct." + newline +
    "Use no arguments to remove all restrictions." + newline +
    "Use \"unknown\" to remove only the restrictions unknown to the generated binding." + newline +
    "Supported restrictions that can be removed: " +
    "\"unique\", \"singleton\", \"monotone\", \"abstract\", \"default\", \"nonNull\", \"range\", \"coding\", \"constantLengthPointer\", \"oneOf\"."
	private final static String RemoveUnknownRestrictions_Multiple_Used = "The Remove Unknown Restrictions hint is already used on this field."
    // Constant Mutator hint error messages
    private final static String ConstantMutator_Usage = "The Constant Mutator hint can only be used on constants."
    // Mixin hint warning messages
    private final static String Mixin_Multiple_Used = "The Mixin hint is already used on this field."
    // Flat hint error messages
    private final static String Flat_Usage = "The Flat hint can only be used on type declarations with one field."
    private final static String Flat_Usage_2 = "The Flat hint can only be used on type declarations with no supertypes or interfaces."
    // Flat hint warning messages
    private final static String Flat_Multiple_Used = "The Flat hint is already used on this field."
    // Unique hint warning messages
    private final static String Unique_Multiple_Used = "The Unique hint is already used on this field."
    // Pure hint warning messages
    private final static String Pure_Multiple_Used = "The Pure hint is already used on this field."
    // Distributed hint error messages
    private final static String Distributed_Usage = "The Distributed hint can only be used on field declarations."
    // On Demand hint error messages
    private final static String OnDemand_Usage = "The On Demand hint can only be used on field declarations."
    // Monotone hint error messages
    private final static String Monotone_Usage = "The Monotone hint can only be used on base-types."
    // Monotone hint warning messages
    private final static String Monotone_Multiple_Used = "The Monotone hint is already used on this field."
    // Read Only hint error messages
    private final static String ReadOnly_Usage = "The Read Only hint can only be used on base-types."
    // Read Only hint warning messages
    private final static String ReadOnly_Multiple_Used = "The Read Only hint is already used on this field."
    // Ignore hint warning messages
    private final static String Ignore_Multiple_Used = "The Ignore hint is already used on this field."
    // Hide hint warning messages
    private final static String Hide_Usage = "The Hide hint can only be used on field declarations."
    // Pragma hint error messages
    private final static String Pragma_Args_Not_Types = "The Pragma hint arguments must be types." + newline +
      "Syntax: !pragma <typeID> or !pragma <typeID>(<typeID>, ...)"
    private final static String Pragma_Not_One_Arg = "The Pragma hint must have at least one argument."
    private final static String Pragma_Multiple_Used = "The Pragma hint is already used on this field."

    @Check
    def validateTypeHints(Declaration declaration) {
      if (declaration instanceof Usertype) {
        validateUsertype(declaration)
      } else if (declaration instanceof Typedef) {
        validateTypedef(declaration)
      }
    }

    def validateUsertype(Usertype usertype) {
      var wasOwnerUsed = false
      var wasProviderUsed = false
      var wasRemoveUnknownRestrictionUsed = false
      var wasMixinUsed = false
      var wasFlatUsed = false
      var wasUniqueUsed = false
      var wasPureUsed = false
      var wasMonotoneUsed = false
      var wasReadOnlyUsed = false
      var wasIgnoreUsed = false
      var wasPragmaUsed = false
      for (hint : usertype.hints) {
        switch (hint.hintName.toLowerCase) {
          case 'owner': {
            if (!wasOwnerUsed) {
              validateOwnerHint(hint, usertype)
              wasOwnerUsed = true
            } else {
              showError(Owner_Already_Used, hint)
            }
          }
          case 'provider': {
            if (!wasProviderUsed) {
              validateProviderHint(hint)
              wasProviderUsed = true
            } else {
              showError(Provider_Already_Used, hint)
            }
          }
          case 'removeunknownrestrictions': {
            if (!wasRemoveUnknownRestrictionUsed) {
              validateRemoveUnknownRestrictioHint(hint)
              wasRemoveUnknownRestrictionUsed = true
            } else {
              showError(RemoveUnknownRestrictions_Multiple_Used, hint)
            }
          }
          case 'constantmutator': {
            showError(ConstantMutator_Usage, hint)
          }
          case 'mixin': {
            if (!wasMixinUsed) {
              wasMixinUsed = true
            } else {
              showWarning(Mixin_Multiple_Used, hint)
            }
          }
          case 'flat': {
            if (!wasFlatUsed) {
              validateFlatHint(hint, usertype)
              wasFlatUsed = true
            } else {
              showWarning(Flat_Multiple_Used, hint)
            }
          }
          case 'unique': {
            if (!wasUniqueUsed) {
              wasUniqueUsed = true
            } else {
              showWarning(Unique_Multiple_Used, hint)
            }
          }
          case 'pure': {
            if (!wasPureUsed) {
              wasPureUsed = true
            } else {
              showWarning(Pure_Multiple_Used, hint)
            }
          }
          case 'distributed': {
            showError(Distributed_Usage, hint)
          }
          case 'ondemand': {
            showError(OnDemand_Usage, hint)
          }
          case 'monotone': {
            if (!wasMonotoneUsed) {
              validateMonotoneHint(hint, usertype)
              wasMonotoneUsed = true
            } else {
              showWarning(Monotone_Multiple_Used, hint)
            }
          }
          case 'readonly': {
            if (!wasReadOnlyUsed) {
              validateReadonlyHint(hint, usertype)
              wasReadOnlyUsed = true
            } else {
              showWarning(ReadOnly_Multiple_Used, hint)
            }
          }
          case 'ignore': {
            if (!wasIgnoreUsed) {
              wasIgnoreUsed = true
            } else {
              showWarning(Ignore_Multiple_Used, hint)
            }
          }
          case 'hide': {
            showError(Hide_Usage, hint)
          }
          case 'pragma': {
            if (!wasPragmaUsed) {
              validatePragmaHint(hint)
              wasPragmaUsed = true
            } else {
              showError(Pragma_Multiple_Used, hint)
            }
          }
          default: {
            showError(Unknown_Hint, hint)
          }
        }
      }
    }

    def validateTypedef(Typedef typedef) {
      val underlyingUsertype = returnTypedefDeclaration(typedef)
      var wasOwnerUsed = false
      var wasProviderUsed = false
      var wasRemoveUnknownRestrictionUsed = false
      var wasMixinUsed = false
      var wasFlatUsed = false
      var wasUniqueUsed = false
      var wasPureUsed = false
      var wasMonotoneUsed = false
      var wasReadOnlyUsed = false
      var wasIgnoreUsed = false
      var wasPragmaUsed = false
      if (underlyingUsertype != null) {
        wasOwnerUsed = isUserTypeOwnerHinted(underlyingUsertype)
        wasProviderUsed = isUserTypeProviderHinted(underlyingUsertype)
        wasRemoveUnknownRestrictionUsed = isUserTypeRemoveUnknownRestrictionsHinted(underlyingUsertype)
        wasMixinUsed = isUserTypeMixinHinted(underlyingUsertype)
        wasFlatUsed = isUserTypeFlatHinted(underlyingUsertype)
        wasUniqueUsed = isUserTypeUniqueHinted(underlyingUsertype)
        wasPureUsed = isUserTypePureHinted(underlyingUsertype)
        wasMonotoneUsed = isUserTypeMonotoneHinted(underlyingUsertype)
        wasReadOnlyUsed = isUserTypeReadOnlyHinted(underlyingUsertype)
        wasIgnoreUsed = isUserTypeIgnoreHinted(underlyingUsertype)
        wasPragmaUsed = isUserTypePragmaHinted(underlyingUsertype)
      }
      for (hint : typedef.hints) {
        if (underlyingUsertype != null) {
          switch (hint.hintName) {
            case 'owner': {
              if (!wasOwnerUsed) {
                validateOwnerHint(hint, underlyingUsertype)
                wasOwnerUsed = true
              } else {
                showError(Owner_Already_Used, hint)
              }
            }
            case 'provider': {
              if (!wasProviderUsed) {
                validateProviderHint(hint)
                wasProviderUsed = true
              } else {
                showError(Provider_Already_Used, hint)
              }
            }
            case 'removeunknownrestrictions': {
              if (!wasRemoveUnknownRestrictionUsed) {
                validateRemoveUnknownRestrictioHint(hint)
                wasRemoveUnknownRestrictionUsed = true
              } else {
                showError(RemoveUnknownRestrictions_Multiple_Used, hint)
              }
            }
            case 'constantmutator': {
              showError(ConstantMutator_Usage, hint)
            }
            case 'mixin': {
              if (!wasMixinUsed) {
                wasMixinUsed = true
              } else {
                showWarning(Mixin_Multiple_Used, hint)
              }
            }
            case 'flat': {
              if (!wasFlatUsed) {
                validateFlatHint(hint, underlyingUsertype)
                wasFlatUsed = true
              } else {
                showWarning(Flat_Multiple_Used, hint)
              }
            }
            case 'unique': {
              if (!wasUniqueUsed) {
                wasUniqueUsed = true
              } else {
                showWarning(Unique_Multiple_Used, hint)
              }
            }
            case 'pure': {
              if (!wasPureUsed) {
                wasPureUsed = true
              } else {
                showWarning(Pure_Multiple_Used, hint)
              }
            }
            case 'distributed': {
              showError(Distributed_Usage, hint)
            }
            case 'ondemand': {
              showError(OnDemand_Usage, hint)
            }
            case 'monotone': {
              if (!wasMonotoneUsed) {
                validateMonotoneHint(hint, underlyingUsertype)
                wasMonotoneUsed = true
              } else {
                showWarning(Monotone_Multiple_Used, hint)
              }
            }
            case 'readonly': {
              if (!wasReadOnlyUsed) {
                validateReadonlyHint(hint, underlyingUsertype)
                wasReadOnlyUsed = true
              } else {
                showWarning(ReadOnly_Multiple_Used, hint)
              }
            }
            case 'ignore': {
              if (!wasIgnoreUsed) {
                wasIgnoreUsed = true
              } else {
                showWarning(Ignore_Multiple_Used, hint)
              }
            }
            case 'hide': {
              showError(Hide_Usage, hint)
            }
            case 'pragma': {
              if (!wasPragmaUsed) {
                validatePragmaHint(hint)
                wasPragmaUsed = true
              } else {
                showError(Pragma_Multiple_Used, hint)
              }
            }
            default: {
              showError(Unknown_Hint, hint)
            }
          }
        } else { // If the base of the typedef is not a user-type
          showError(Hint_Usage, hint)
        }
      }
    }

    def validateOwnerHint(Hint hint, Usertype usertype) {
      if (usertype.supertypes.size == 0) {
        if (hint.hintArguments.size == 0) {
          showError(Owner_No_Args, hint)
        }
      } else {
        showError(Owner_Usage, hint)
      }
    }

    def validateProviderHint(Hint hint) {
      if (hint.hintArguments.size == 0) {
        showError(Provider_No_Args, hint)
      }
    }

    /**
     *  Currently it is legal to have "unknown" + other restrictions names at the same time
     */
    def validateRemoveUnknownRestrictioHint(Hint hint) {
      var areArgumentsCorrect = true
      for (hintArgument : hint.hintArguments) {
        if (hintArgument.valueString != null) {
          if (!removeUnknownRestrictions_Hint_Args.contains(hintArgument.valueString.toLowerCase)) {
            areArgumentsCorrect = false
          }
        } else {
          areArgumentsCorrect = false
        }
      }
      if (!areArgumentsCorrect) {
        showError(RemoveUnknownRestrictions_Usage, hint)
      }
    }

    def validateFlatHint(Hint hint, Usertype usertype) {
      if (usertype.fields.size != 1) {
        showError(Flat_Usage, hint)
      } else if (usertype.supertypes.size > 0) {
        showError(Flat_Usage_2, hint)
      }
    }

    def validateMonotoneHint(Hint hint, Usertype usertype) {
      if (usertype.supertypes.size != 0) {
        showError(Monotone_Usage, hint)
      }
    }

    def validateReadonlyHint(Hint hint, Usertype usertype) {
      if (usertype.supertypes.size != 0) {
        showError(ReadOnly_Usage, hint)
      }
    }

    def validatePragmaHint(Hint hint) {
      if (hint.hintArguments.size != 0) {
        for (hintArgument : hint.hintArguments) {
          if (hintArgument.valueType == null) {
            showError(Pragma_Args_Not_Types, hint)
          }
        }
      } else {
        showError(Pragma_Not_One_Arg, hint)
      }
    }

    def boolean isUserTypeOwnerHinted(Usertype usertype) {
      for (restriction : usertype.hints) {
        if ("owner".equals(restriction.hintName)) {
          return true
        }
      }
      return false
    }

    def Usertype returnTypedefDeclaration(Typedef typedef) {
      if (typedef.fieldtype instanceof DeclarationReference) {
        val typedefType = (typedef.fieldtype as DeclarationReference).type
        if (typedefType instanceof Usertype) {
          return typedefType
        } else {
          return null
        }
      } else
        return null
    }

    def boolean isUserTypeProviderHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("provider".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypeRemoveUnknownRestrictionsHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("removeunknownrestrictions".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypeMixinHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("mixin".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypeFlatHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("flat".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypeUniqueHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("unique".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypePureHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("pure".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypeMonotoneHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("monotone".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypeReadOnlyHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("readonly".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypeIgnoreHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("ignore".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def boolean isUserTypePragmaHinted(Usertype usertype) {
      for (hint : usertype.hints) {
        if ("pragma".equals(hint.hintName)) {
          return true
        }
      }
      return false
    }

    def showError(String message, Hint hint) {
      error(message, hint, SkillPackage.Literals.HINT__HINT_NAME)
    }

    def showWarning(String message, Hint hint) {
      warning(message, hint, SkillPackage.Literals.HINT__HINT_NAME)
    }
  }