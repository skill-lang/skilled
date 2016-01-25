package de.unistuttgart.iste.ps.skilled.ui.quickfix

import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification
import org.eclipse.xtext.validation.Issue
import org.eclipse.xtext.ui.editor.quickfix.IssueResolution

class setName {
	var TypeDeclaration declara;
	var IModificationContext context;
	var IssueResolutionAcceptor acceptor
	var Issue issue;
	new(TypeDeclaration td, IModificationContext context, Issue issue, IssueResolutionAcceptor acceptor) {
		this.context = context;
		declara = td;
		this.issue = issue;
		this.acceptor = acceptor;
	}
	def setName(String name){
		if(name!= null){
			acceptor.accept(issue, "Change name", "changing name", "upcase.png", new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					var TypeDeclaration td = element as TypeDeclaration
					td.name= name
				}	
			});
			var int count = 0;
			for(IssueResolution i :acceptor.issueResolutions){
				count++;
				println("fix: "+ count+ "Description: "+ i.description)
				if(i.description.equals("changing name")){
					i.apply
				}
			}		
		}
	}
}