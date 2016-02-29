package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Constant
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Hint
import de.unistuttgart.iste.ps.skilled.sKilL.HintArgument
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldHintsErrorMessages
import java.util.List
import org.eclipse.xtext.validation.Check

/** 
 * @author Nikolay Fateev
 * @author Tobias Heck
 * @author Daniel Ryan Degutis
 */
class FieldHintsValidator extends AbstractSKilLComposedValidatorPart {

  public final static String[] removeUnknownRestrictions_Hint_Args = #["unique", "singleton", "monotone", "abstract",
    "default", "nonnull", "range", "coding", "constantlengthpointer", "oneof", "unknown"]

  @Check
  def validateFieldHints(Field field) {
    var wasRemoveUnknownRestrictionUsed = false
    var wasConstantMutatorUsed = false
    var wasDistributedUsed = false
    var wasOnDemandUsed = false
    var wasIgnoreUsed = false
    var wasHideUsed = false
    var wasPragmaUsed = false

    for (hint : field.hints) {
      switch (hint.hintName.toLowerCase) {
        case 'removeunknownrestrictions': {
          handleRemoveUnknownRestrictionsHint(hint, wasRemoveUnknownRestrictionUsed)
          wasRemoveUnknownRestrictionUsed = true
        }
        case 'constantmutator': {
          handleConstantMutatorHint(field, hint, wasConstantMutatorUsed)
          wasConstantMutatorUsed = true
        }
        case 'mixin': {
          showError(FieldHintsErrorMessages.Mixin_Usage, hint)
        }
        case 'flat': {
          showError(FieldHintsErrorMessages.Flat_Usage, hint)
        }
        case 'unique': {
          showError(FieldHintsErrorMessages.Unique_Usage, hint)
        }
        case 'pure': {
          showError(FieldHintsErrorMessages.Pure_Usage, hint)
        }
        case 'distributed': {
          if (wasDistributedUsed) {
            showWarning(FieldHintsErrorMessages.Distributed_Multiple_Used, hint)
          }
          wasDistributedUsed = true
        }
        case 'ondemand': {
          if (wasOnDemandUsed) {
            showWarning(FieldHintsErrorMessages.OnDemand_Multiple_Used, hint)
          }
          wasOnDemandUsed = true
        }
        case 'monotone': {
          showError(FieldHintsErrorMessages.Monotone_Usage, hint)
        }
        case 'readonly': {
          showError(FieldHintsErrorMessages.ReadOnly_Usage, hint)
        }
        case 'ignore': {
          if (wasIgnoreUsed) {
            showWarning(FieldHintsErrorMessages.Ignore_Multiple_Used, hint)
          }
          wasIgnoreUsed = true
        }
        case 'hide': {
          if (wasHideUsed) {
            showWarning(FieldHintsErrorMessages.Hide_Multiple_Used, hint)
          }
          wasHideUsed = true
        }
        case 'pragma': {
          handlePragmaHint(hint, wasPragmaUsed)
          wasPragmaUsed = true
        }
        default: {
          showError(FieldHintsErrorMessages.Unknown_Hint, hint)
        }
      }
    }
  }

  def void handleRemoveUnknownRestrictionsHint(Hint hint, boolean wasRemoveUnknownRestrictionUsed) {
    if (!wasRemoveUnknownRestrictionUsed) {
      if (!areRemoveUnknownRestrictionArgumentsCorrect(hint.hintArguments)) {
        showError(FieldHintsErrorMessages.RemoveUnknownRestrictions_Usage, hint)
      }
    } else {
      showError(FieldHintsErrorMessages.RemoveUnknownRestrictions_Multiple_Used, hint)
    }
  }

  def void handleConstantMutatorHint(Field field, Hint hint, boolean wasConstantMutatorUsed) {
    if (!wasConstantMutatorUsed) {
      if (field.fieldcontent instanceof Constant) {
        var hintArguments = hint.hintArguments
        if (hintArguments.size == 2) {
          if (hintArguments.get(0).valueLong == null) {
            showError(FieldHintsErrorMessages.ConstantMutator_First_Arg_Not_Integer, hint)
          } else if (hintArguments.get(1).valueLong == null) {
            showError(FieldHintsErrorMessages.ConstantMutator_Second_Arg_Not_Integer, hint)
          } else {
            if (hintArguments.get(1).valueLong < hintArguments.get(0).valueLong) {
              showError(FieldHintsErrorMessages.ConstantMutator_Second_Arg_Smaller_Than_First, hint)
            }
          }
        } else {
          showError(FieldHintsErrorMessages.ConstantMutator_Not_2_Args, hint)
        }
      } else {
        showError(FieldHintsErrorMessages.ConstantMutator_Usage, hint)
      }
    } else {
      showError(FieldHintsErrorMessages.ConstantMutator_Multiple_Used, hint)
    }
  }

  def void handlePragmaHint(Hint hint, boolean wasPragmaUsed) {
    if (!wasPragmaUsed) {
      if (hint.hintArguments.size != 0) {
        for (hintArgument : hint.hintArguments) {
          if (hintArgument.valueType == null) {
            showError(FieldHintsErrorMessages.Pragma_Args_Not_Types, hint)
          }
        }
      } else {
        showError(FieldHintsErrorMessages.Pragma_Not_One_Arg, hint)
      }
    } else {
      showError(FieldHintsErrorMessages.Pragma_Multiple_Used, hint)
    }
  }

  /**
   *  Currently it is legal to have "unknown" + other restrictions names at the same time
   */
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

  def void showError(String message, Hint hint) {
    error(message, hint, SKilLPackage.Literals.HINT__HINT_NAME)
  }

  def void showWarning(String message, Hint hint) {
    warning(message, hint, SKilLPackage.Literals.HINT__HINT_NAME)
  }

}