package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.AnnotationTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.BooleanTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.ContainerTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.FloatTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.IntegerTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.MapTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.StringTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.fields.UserTypeFieldRestrictionsValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.types.TypedefRestrictionValidator
import de.unistuttgart.iste.ps.skilled.validation.restrictions.types.UserTypeRestrictionValidator
import org.eclipse.xtext.validation.ComposedChecks

/**
 * The base validator, that calls all other validators
 */
@ComposedChecks(validators=#[InheritenceValidator, UserTypeFieldRestrictionsValidator,
	StringTypeFieldRestrictionsValidator, IntegerTypeFieldRestrictionsValidator, FloatTypeFieldRestrictionsValidator,
	AnnotationTypeFieldRestrictionsValidator, BooleanTypeFieldRestrictionsValidator,
	ContainerTypeFieldRestrictionsValidator, MapTypeFieldRestrictionsValidator, DuplicatedFieldDefinitionValidation,
	KeywordWarning, ASCIICharValidator, ViewValidator, ScopingValidator, UserTypeRestrictionValidator,
	TypedefRestrictionValidator, ImportURIValidator, ImportValidator, FieldHintsValidator, ConstantValidator,
	NestingValidator, NamesAreUniqueValidator, TypeHintsValidator, ReservedSKilLKeywordValidator,
	UnusedDeclarationValidator])
	class SKilLValidator extends AbstractSKilLValidator {
	}
	