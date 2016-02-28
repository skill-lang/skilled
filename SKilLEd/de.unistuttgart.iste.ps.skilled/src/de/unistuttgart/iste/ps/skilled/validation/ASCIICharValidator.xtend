package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.util.CheckASCII
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.EValidatorRegistrar

/**
 * This class contains the validation methods for non-ASCII names warnings.
 * @author Jan Berberich
 */
class ASCIICharValidator extends AbstractDeclarativeValidator {

	override register(EValidatorRegistrar registar) {}

	public static val DECLARATION_HAS_NONASCII_CHARS = "declarationNonASCII"
	public static val FIELD_HAS_NONASCII_CHARS = "fieldNonASCII"

	/**
	 * Checks if the name of the declaration has non-ASCII chars.
	 * @dec The declaration to check.
	 */
	@Check
	def void checkDeclarationIsPureASCII(Declaration dec) {
		if (!CheckASCII.isPureAscii(dec.name)) {
			warning("Warning: Declaration contains non-ASCII-Chars in the name.", dec,
				SKilLPackage.Literals.DECLARATION.getEStructuralFeature(1), DECLARATION_HAS_NONASCII_CHARS, dec.name)
		}
	}

	/**
	 * Check if the field has non-ASCII chars
	 * @f The field to check.
	 */
	@Check
	def void checkFieldIsPureASCII(Field f) {
		if (!CheckASCII.isPureAscii(f.fieldcontent.name)) {
			warning("Warning: Field contains non-ASCII-Chars in the name.", f.fieldcontent,
				SKilLPackage.Literals.FIELDCONTENT__NAME, FIELD_HAS_NONASCII_CHARS, f.fieldcontent.name)
		}
	}

}
