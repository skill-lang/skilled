package de.unistuttgart.iste.ps.skilled.ui.contentassist

import org.eclipse.xtext.ui.editor.contentassist.DefaultCompletionProposalPostProcessor
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal

class SKilLCompletionProposalPostProcessor extends DefaultCompletionProposalPostProcessor {
	
	override public ICompletionProposal[] postProcess(ICompletionProposal[] proposals) {
		if (proposals.length == 1) {
			if (proposals.get(0) instanceof ConfigurableCompletionProposal) {
				val ConfigurableCompletionProposal proposal = proposals.get(0) as ConfigurableCompletionProposal;
				if (proposal.isAutoInsertable() && proposal.getReplaceContextLength() > proposal.getReplacementLength()) {
					proposal.setAutoInsertable(true);
				}
			}
		}
		return proposals;
	}
}