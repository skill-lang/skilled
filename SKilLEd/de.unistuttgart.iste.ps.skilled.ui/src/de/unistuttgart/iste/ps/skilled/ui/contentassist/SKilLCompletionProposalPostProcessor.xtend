package de.unistuttgart.iste.ps.skilled.ui.contentassist

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.DefaultCompletionProposalPostProcessor
import org.eclipse.xtext.ui.editor.hover.html.XtextBrowserInformationControlInput

/**
 * 
 * @author Tobias Heck
 * @author Marco Link
 */
class SKilLCompletionProposalPostProcessor extends DefaultCompletionProposalPostProcessor {

	override public ICompletionProposal[] postProcess(ICompletionProposal[] proposals) {
		if (proposals.length == 1) {
			if (proposals.get(0) instanceof ConfigurableCompletionProposal) {
				val ConfigurableCompletionProposal proposal = proposals.get(0) as ConfigurableCompletionProposal;
				if (proposal.isAutoInsertable() &&
					proposal.getReplaceContextLength() > proposal.getReplacementLength()) {
					proposal.setAutoInsertable(true);
				}
			}
		}

		for (ICompletionProposal p : proposals) {
			if (p instanceof ConfigurableCompletionProposal) {
				var Object additionalProposalInfo = p.getAdditionalProposalInfo(new NullProgressMonitor);
				if (additionalProposalInfo != null) {
					if (additionalProposalInfo instanceof XtextBrowserInformationControlInput) {
						if (additionalProposalInfo?.element instanceof Declaration) {
							var Declaration dec = additionalProposalInfo.element as Declaration;
							p.displayString = dec?.name;
							p.replacementString = dec?.name;
						}

						if (additionalProposalInfo?.element instanceof Fieldcontent) {
							var Fieldcontent abc = additionalProposalInfo.element as Fieldcontent;
							var String replacementString = (abc.eContainer.eContainer as Declaration).name + "." +
								abc.name;
							var String displayString = abc.name + " - " + replacementString;
							p.displayString = displayString;
							p.replacementString = replacementString;
						}

					}
				}
			}
		}
		return proposals;
	}
}