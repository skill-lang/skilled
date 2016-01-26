package de.unistuttgart.iste.ps.skilled.ui.quickfix

import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification
import org.eclipse.xtext.ui.editor.quickfix.IssueResolution
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.xtext.validation.Issue

/**
 * @author Jan Berberich
 * 
 * Class to set a new Name for a Field or a TypeDeclaration that is used in the fixes for nonASCIICharWarnings.
 *  
 */
class SetName {
	
	var IssueResolutionAcceptor acceptor
	var Issue issue;

	new(Issue issue, IssueResolutionAcceptor acceptor) {
		this.issue = issue;
		this.acceptor = acceptor;
	}

	/**
	 * Change the name of a the Field or the TypeDeclaration that was set by the Issue.
	 * 
	 * @param newName The new Name of the Field or TypeDeclaration.
	 */
	def changeName(String newName) {
		if (newName != null) {
			// Define new issueResolvation for the acceptor
			acceptor.accept(issue, "Change name", "changing name", "upcase.png", new ISemanticModification() {
				override void apply(EObject element, IModificationContext context) {
					if (element instanceof TypeDeclaration) {
						element.name = newName
					}
					if (element instanceof Fieldcontent) {
						element.name = " " + newName
					}
				}
			});
			// Search the IssueResolvation that was defined and apply it
			for (IssueResolution i : acceptor.issueResolutions) {
				if (i.description.equals("changing name")) {
					i.apply
				}
			}
		}
	}
	
}