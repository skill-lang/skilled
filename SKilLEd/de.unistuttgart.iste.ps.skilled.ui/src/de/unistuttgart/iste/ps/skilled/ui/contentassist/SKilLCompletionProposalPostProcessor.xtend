package de.unistuttgart.iste.ps.skilled.ui.contentassist

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.DefaultCompletionProposalPostProcessor
import org.eclipse.xtext.ui.editor.hover.html.XtextBrowserInformationControlInput

/**
 * overwrites the postProcess method and changes displayed string and replacement string <br>
 * of the proposals if they propose a declaration or field reference
 * 
 * @author Tobias Heck
 * @author Marco Link
 */
class SKilLCompletionProposalPostProcessor extends DefaultCompletionProposalPostProcessor {

	/**
	 * This method changes the displayed and replacement string if the proposal is a declaration or field reference
	 */
	override public ICompletionProposal[] postProcess(ICompletionProposal[] proposals) {

		for (proposal : proposals) {
			if (proposal instanceof ConfigurableCompletionProposal) {
				var Object additionalProposalInfo = proposal.getAdditionalProposalInfo(new NullProgressMonitor);
				if (additionalProposalInfo != null) {
					if (additionalProposalInfo instanceof XtextBrowserInformationControlInput) {

						// change strings to match: [declaration name]
						if (additionalProposalInfo?.element instanceof Declaration) {
							var Declaration dec = additionalProposalInfo.element as Declaration;
							proposal.displayString = dec?.name;
							proposal.replacementString = dec?.name;
						}

						// change strings to match: [declaration name] some separator [field name]
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