package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.validation.restrictions.types.TypeRestrictionsValidator
import org.eclipse.xtext.validation.ComposedChecks
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.FieldRestrictionsValidator

/**
 * The base validator, that calls all other validators
 */
@ComposedChecks(validators=#[InheritenceValidator, FieldRestrictionsValidator, DuplicatedTypenameValidation,
	KeywordWarning, ASCIICharValidator, ViewValidator, ScopingValidator, TypeRestrictionsValidator, ImportValidator,
	FieldHintsValidator, ConstantValidator, NestingValidator])
class SKilLValidator extends AbstractSKilLValidator {
}
