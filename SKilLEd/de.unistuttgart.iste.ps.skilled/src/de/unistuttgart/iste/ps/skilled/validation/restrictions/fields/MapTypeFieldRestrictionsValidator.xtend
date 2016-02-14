package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.sKilL.Fieldtype
import de.unistuttgart.iste.ps.skilled.sKilL.Maptype
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction
import de.unistuttgart.iste.ps.skilled.validation.errormessages.FieldRestrictionErrorMessages

/**
 * @author Daniel Ryan Degutis
 */
class MapTypeFieldRestrictionsValidator extends AbstractFieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Maptype
	}

	override void handleNonNullRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

	override void handleDefaultRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

	override void handleRangeRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

	override void handleMaxRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

	override void handleMinRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

	override void handleCodingRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

	override void handleConstantLengthPointerRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

	override void handleOneOfRestriction(Fieldtype fieldtype, Restriction restriction) {
		showError(FieldRestrictionErrorMessages.Restriction_On_Map, restriction)
	}

}