package de.unistuttgart.iste.ps.skilled.ui.contentassist

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.DefaultCompletionProposalPostProcessor
import org.eclipse.xtext.ui.editor.hover.html.XtextBrowserInformationControlInput

/**
 * TODO Kommentieren!!!!!
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

		for (proposal : proposals) {
			if (proposal instanceof ConfigurableCompletionProposal) {
				var Object additionalProposalInfo = proposal.getAdditionalProposalInfo(new NullProgressMonitor);
				if (additionalProposalInfo != null) {
					if (additionalProposalInfo instanceof XtextBrowserInformationControlInput) {
						if (additionalProposalInfo?.element instanceof Declaration) {
							var Declaration dec = additionalProposalInfo.element as Declaration;
							proposal.displayString = dec?.name;
							proposal.replacementString = dec?.name;
						}

						if (additionalProposalInfo?.element instanceof Fieldcontent) {
							var Fieldcontent content = additionalProposalInfo.element as Fieldcontent;
							var String replacementString = (content.eContainer.eContainer as Declaration).name + "." +
								content.name;
							var String displayString = content.name + " - " + replacementString;
							proposal.displayString = displayString;
							proposal.replacementString = replacementString;
						}

					}
				}
			}
		}
		return proposals;
	}
}