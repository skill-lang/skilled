package de.unistuttgart.iste.ps.skilled.validation

import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import org.eclipse.xtext.validation.AbstractDeclarativeValidator
import org.eclipse.xtext.validation.EValidatorRegistrar
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.View

class ViewValidation extends AbstractDeclarativeValidator{
	override register(EValidatorRegistrar registar) {}
	
	@Check
	def validateViews(TypeDeclaration td){
		for (Field f : td.fields) {
			//Check if Field is a View
			if((f.fieldcontent instanceof View)){
				var View v = f.fieldcontent as View
				print(f.fieldcontent.name)
				print(v.fieldcontent.eContainer);
				print(v.fieldcontent.fieldcontent.name);
			}				
		}
	}
	
}