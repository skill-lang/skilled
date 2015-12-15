package de.unistuttgart.iste.ps.skilled.validation
import de.ust.skill.main.CommandLine
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import org.eclipse.xtext.validation.EValidatorRegistrar
import java.util.ArrayList
import de.unistuttgart.iste.ps.skilled.util.keywordCheckEscaping;

/**
 * This Class checks if a Keyword requires escaping in the target 
 * Language and, if it is required, displays a warning.
 * @author Jan Berberich	
 */
class KeywordWarning extends AbstractDeclarativeValidator{
	
	override register(EValidatorRegistrar registar){	
	}	
	
	/**
	 * Check Method for the KeywordWarning.
	 * 
	 */
	@Check
	def KeywordWarning(TypeDeclaration dec){ 
		var ArrayList<String> requiresEscaping = keywordCheckEscaping.requiresEscaping(dec.name);
		// requiresEscaping(dec.name)
		if(!requiresEscaping.isNullOrEmpty){
			var String warning = "Name requires escaping in "
			var boolean needComma = false
			var int i=1;
			for(String targetLanguage : requiresEscaping){
				if(needComma){
					if(i == requiresEscaping.size){
						warning = warning +" and " + targetLanguage
					}else{
						warning = warning +", " + targetLanguage
					}
				}else{
					warning = warning + targetLanguage
					needComma = true
				}
				i++
			}
			warning(warning + ".", dec, SKilLPackage.Literals.DECLARATION__NAME)
		}
	}	
}