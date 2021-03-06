package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.skill.Constant
import de.unistuttgart.iste.ps.skilled.skill.Integertype
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import org.eclipse.xtext.validation.Check

/**
 * Contains validation rules for constant fields
 * @author Marco Link
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 */
class ConstantValidator extends AbstractSKilLComposedValidatorPart {
  public static val INVALID_CONSTANT_TYPE = 'invalidConstantType'

  /**
   * Raises as error if a constant is used on a non integer field
   */
  @Check
  def checkConstantHasAnInteger(Constant constant) {
    if (!(constant.fieldtype instanceof Integertype)) {
      error('Only an Integer can be constant.', constant, SkillPackage.Literals.CONSTANT__CONSTANT_NAME,
        INVALID_CONSTANT_TYPE)
    }
  }

}