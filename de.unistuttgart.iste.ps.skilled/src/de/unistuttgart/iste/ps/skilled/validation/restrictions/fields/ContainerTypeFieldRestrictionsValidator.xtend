package de.unistuttgart.iste.ps.skilled.validation.restrictions.fields

import de.unistuttgart.iste.ps.skilled.skill.Arraytype
import de.unistuttgart.iste.ps.skilled.skill.Fieldtype
import de.unistuttgart.iste.ps.skilled.skill.Listtype
import de.unistuttgart.iste.ps.skilled.skill.Settype

/**
 * @author Daniel Ryan Degutis
 */
class ContainerTypeFieldRestrictionsValidator extends AbstractFieldRestrictionsValidator {

	override boolean handleActivationCondition(Fieldtype fieldtype) {
		return fieldtype instanceof Settype || fieldtype instanceof Arraytype || fieldtype instanceof Listtype
	}

}