package de.unistuttgart.iste.ps.skilled.validation
import de.ust.skill.main.CommandLine
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import org.eclipse.xtext.validation.EValidatorRegistrar
import java.io.BufferedReader
import java.util.ArrayList
import org.eclipse.xtext.findReferences.TargetURIs.Key
import java.util.Scanner

/**
 * This Class checks if a Keyword requires escaping in the target 
 * Language and, if it is required, displays a warning.
 * @author Jan Berberich	
 */
class KeywordWarning extends AbstractDeclarativeValidator{
		var String generatorPath
	//val private String[] languages = {"  ", "  "}
	val String[] languages = #["C", "Scala", "Ada", "Java"] //The Languages that will be checked
	
	override register(EValidatorRegistrar registar){	
	}	
	
	/**
	 * Check Method for the KeywordWarning.
	 * 
	 */
	@Check
	def KeywordWarning(TypeDeclaration dec){ 
		var ArrayList<String> requiresEscaping = requiresEscaping(dec.name)
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
	/**
	 * This Method checks if the entered keyword requires escaping.
	 *@name The name that is checked.
	 *@return List with the languages where escaping is required for the name.
	 */
	def ArrayList<String> requiresEscaping(String name){
		var ArrayList<String> returnList = new ArrayList<String>()
		for(String checkLanguage: languages){
			//var BLA = CommandLine.
			
			
			var String output = ""//Read generator output in this variable	
			/*var Process process = new ProcessBuilder(generatorPath, "-L" + checkLanguage + "--requiresEscaping " +name ).start();
			var Scanner s = new Scanner(process.inputStream).useDelimiter("\\A")
			while(s.hasNext){
				output = output + s.next
			}*/
			if(output.equals("true")){
				returnList.add(checkLanguage) //Generator gives output true->RequiresEscaping in checkLanguage
			}
		}
		return returnList
	}
}