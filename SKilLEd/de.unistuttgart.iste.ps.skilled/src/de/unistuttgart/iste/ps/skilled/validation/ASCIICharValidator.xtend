package de.unistuttgart.iste.ps.skilled.validation

import org.eclipse.xtext.validation.EValidatorRegistrar
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.util.CheckASCII;

/**
 * This class checks if a name has non-ASCII-Characters in it 
 * and gives a warning if this is the case.
 * 
 * @author Jan Berberich
 */
class ASCIICharValidator extends AbstractDeclarativeValidator {
	override register(EValidatorRegistrar registar) {}

	public val static String DECLARATION_HAS_NONASCII_CHARS = "declarationNonASCII"
	public val static String FIELD_HAS_NONASCII_CHARS = "fieldNonASCII"
	
	

	@Check
	/**
	 * Checks if a Declaration has non-ASCII-Chars in the name.
	 * @param dec The Declaration to be checked.
	 */
	def void declarationCheck(Declaration dec) {
		if (!CheckASCII.isPureAscii(dec.name)) {
			warning("Warning: Declaration contains non-ASCII-Chars in the name.", dec,
				SKilLPackage.Literals.DECLARATION.getEStructuralFeature(1), DECLARATION_HAS_NONASCII_CHARS, dec.name)
		}
	}

	@Check
	/**
	 * Checks if a Field has non-ASCII-Chars in the name.
	 * @param f The Field to be checked
	 */
	def void fieldCheck(Field f) {
		if (!CheckASCII.isPureAscii(f.fieldcontent.name)) {
			warning("Warning: Field contains non-ASCII-Chars in the name.", f.fieldcontent,
				SKilLPackage.Literals.FIELDCONTENT__NAME, FIELD_HAS_NONASCII_CHARS, f.fieldcontent.name)
		}
	}
}