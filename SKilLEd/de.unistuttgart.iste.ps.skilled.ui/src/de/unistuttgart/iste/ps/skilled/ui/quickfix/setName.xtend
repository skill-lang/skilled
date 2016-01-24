package de.unistuttgart.iste.ps.skilled.ui.quickfix

import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification
import org.eclipse.xtext.validation.Issue

class setName {
	var TypeDeclaration declara;
	var IModificationContext context;
	new(TypeDeclaration td, IModificationContext context) {
		this.context = context;
		declara = td;
	}
	def setName(String name){
		if(name!= null){
			var ISemanticModification sem = new ISemanticModification() {
			override void apply(EObject element, IModificationContext context) {
				var TypeDeclaration e =element as TypeDeclaration;				
				e.name = name;
				println ("Name set to "+ e.name+ "!" )
			}
			}
			sem.apply(declara, context);
		}
		println("Name now: "+declara.name)	
	}
}