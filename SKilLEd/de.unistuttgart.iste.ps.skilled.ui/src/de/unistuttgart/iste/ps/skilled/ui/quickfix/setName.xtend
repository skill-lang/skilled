package de.unistuttgart.iste.ps.skilled.ui.quickfix

import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification
import org.eclipse.xtext.validation.Issue
import javax.inject.Inject
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
			/*
			var ISemanticModification sem = new ISemanticModification() {
			override void apply(EObject element, IModificationContext context) {
				var TypeDeclaration e =element as TypeDeclaration;				
				e.name = name;
				print("Name set to "+ e.name+ "!" )
			}
			}
			sem.apply(declara, context);
		*/
			acceptor.accept(issue, "Change name", "changing name" + issue.data.get(0) + ".", "upcase.png", new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					var TypeDeclaration td = element as TypeDeclaration
					td.name= name
					println("Name set to "+ td.name+ "!" )
				}	
			});
		
			for(IssueResolution i :acceptor.issueResolutions){
				if(i.description.equals("changing name")){
					println("Apply fix")
					i.apply
				}
			}
			println("Name now: "+declara.name)			
		}
	}
}