package de.unistuttgart.iste.ps.skilled.validation

import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.EValidatorRegistrar

/**
 * This class should be extended by every Validator which is part of the composed checks.
 * It will prevent the multiple call of validator checks and the wrong use of composed checks.
 * 
 * @author Marco Link 
 */
class AbstractSKilLComposedValidatorPart extends AbstractDeclarativeValidator {
  override register(EValidatorRegistrar registar) {}
}