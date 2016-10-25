package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.skill.Arraytype
import de.unistuttgart.iste.ps.skilled.skill.Basetype
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Listtype
import de.unistuttgart.iste.ps.skilled.skill.Maptype
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.skill.Settype
import de.unistuttgart.iste.ps.skilled.skill.Typedef
import org.eclipse.xtext.validation.Check

/**
 * Checks wether a compound Type is hidden behind a Typedef and is then nested in another compound Type
 * @author Marco Link
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 */
class NestingValidator extends AbstractSKilLComposedValidatorPart {

  public static val INVALID_NESTED_TYPEDEF = 'invalidNestedTypedef'

  /**
   * Checks whether a typedef hides a compound type, raises an error if so
   */
  def checkInvalidNestedTypes(DeclarationReference dr) {
    if (dr.type instanceof Typedef) {
      val td = dr.type as Typedef
      if (!(td.fieldtype instanceof Basetype)) {
        error('It is forbidden to nest containers inside of other containers.', dr,
          SkillPackage.Literals.DECLARATION_REFERENCE__TYPE, INVALID_NESTED_TYPEDEF)
      }
    }
  }

  /**
   * Checks whether a typedef is illegally used to define a list
   */
  @Check
  def checkInvalidNestedTypedef(Listtype listtype) {
    if (listtype.basetype instanceof DeclarationReference) {
      checkInvalidNestedTypes(listtype.basetype as DeclarationReference)
    }
  }

  /**
   * Checks whether a typedef is illegally used to define an array
   */
  @Check
  def checkInvalidNestedTypedef(Arraytype arraytype) {
    if (arraytype.basetype instanceof DeclarationReference) {
      checkInvalidNestedTypes(arraytype.basetype as DeclarationReference)
    }
  }

  /**
   * Checks whether a typedef is illegally used to define a map
   */
  @Check
  def checkInvalidNestedTypedef(Maptype maptype) {
    for (keyValuePair : maptype.basetypes) {
      if (keyValuePair instanceof DeclarationReference) {
        keyValuePair.checkInvalidNestedTypes
      }
    }
  }

  /**
   * Checks whether a typedef is illegally used to define a set
   */
  @Check
  def checkInvalidNestedTypedef(Settype settype) {
    if (settype.basetype instanceof DeclarationReference) {
      checkInvalidNestedTypes(settype.basetype as DeclarationReference)
    }
  }
}