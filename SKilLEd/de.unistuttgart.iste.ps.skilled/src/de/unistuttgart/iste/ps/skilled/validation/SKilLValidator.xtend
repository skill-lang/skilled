package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.AnnotationTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.BooleanTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.ContainerTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.FloatTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.IntegerTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.MapTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.StringTypeFieldRestrictions
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.UserTypeFieldRestrictions
import de.unistuttgart.iste.ps.skilled.validation.restrictions.types.TypedefRestrictionValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.types.UserTypeRestrictionValidator
import org.eclipse.xtext.validation.ComposedChecks

/**
 * The base validator, that calls all other validators
 */
@ComposedChecks(validators=#[InheritenceValidator, UserTypeFieldRestrictions, StringTypeFieldRestrictions,
	IntegerTypeFieldRestrictionsValidator, FloatTypeFieldRestrictionsValidator,
	AnnotationTypeFieldRestrictionsValidator, BooleanTypeFieldRestrictionsValidator,
	ContainerTypeFieldRestrictionsValidator, MapTypeFieldRestrictionsValidator, DuplicatedTypenameValidation,
	KeywordWarning, ASCIICharValidator, ViewValidator, ScopingValidator, UserTypeRestrictionValidator,
	TypedefRestrictionValidator, ImportURIValidator, ImportValidator, FieldHintsValidator, ConstantValidator,
	NestingValidator])
	class SKilLValidator extends AbstractSKilLValidator {
	}
