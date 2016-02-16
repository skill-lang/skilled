package de.unistuttgart.iste.ps.skilled.ui.contentassist

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.ui.contentassist.TerminalsProposalProvider
import org.eclipse.xtext.RuleCall
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor
import org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher
import org.eclipse.xtext.GrammarUtil
import org.eclipse.xtext.util.Strings
import org.eclipse.xtext.Assignment
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal

class SKilLTerminalsProposalProvider extends TerminalsProposalProvider {
	
	override public void complete_ID(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (doCreateIdProposals) {
			val PrefixMatcher newMatcher = new PrefixMatcher {
				override public boolean isCandidateMatchingPrefix(String name, String prefix) {
					var String strippedName = name
					if (name.startsWith("^") && !prefix.startsWith("^")) {
						strippedName = name.substring(1)
					}
					return context.getMatcher.isCandidateMatchingPrefix(strippedName, prefix)
				}
			};
			val ContentAssistContext myContext = context.copy.setMatcher(newMatcher).toContext
			var String feature = ruleCall.getAssignedFeature
			var String proposalText = null
			if (feature != null) {
				proposalText = feature
			} else {
				proposalText = Strings.toFirstUpper(ruleCall.getRule.getName.toLowerCase)
			}
			var String displayText = proposalText
			if (feature != null)
				displayText = proposalText + " - " + ruleCall.getRule.getName
			proposalText = getValueConverter.toString(proposalText, ruleCall.getRule.getName)
			val ICompletionProposal proposal = createCompletionProposal(proposalText, displayText, null, myContext)
			if (proposal instanceof ConfigurableCompletionProposal) {
				val ConfigurableCompletionProposal configurable = proposal
				configurable.setSelectionStart(configurable.getReplacementOffset)
				configurable.setSelectionLength(proposalText.length)
				configurable.setAutoInsertable(true)
				configurable.setSimpleLinkedMode(myContext.getViewer, '\t', ' ')
			}
			acceptor.accept(proposal)
		}
		
		
	}
	
	def private String getAssignedFeature(RuleCall call) {
		val Assignment assignment = GrammarUtil.containingAssignment(call)
		if (assignment != null) {
			var String result = assignment.getFeature
			if (result.equals(result.toLowerCase))
				result = Strings.toFirstUpper(result)
			return result
		}
		return null
	}
	
	override public void complete_STRING(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (doCreateStringProposals) {
			val String feature = ruleCall.getAssignedFeature
			createStringProposal(context, acceptor, feature, ruleCall)
		}
	}
	
	def private void createStringProposal(ContentAssistContext context, ICompletionProposalAcceptor acceptor,
			String feature, RuleCall ruleCall) {
		var String proposalText = null
		if (feature != null) {
			proposalText = feature
		} else {
			proposalText = Strings.toFirstUpper(ruleCall.getRule.getName.toLowerCase)
		}
		proposalText = getValueConverter.toString(proposalText, ruleCall.getRule.getName)
		var String displayText = proposalText
		if (feature != null)
			displayText = displayText + " - " + ruleCall.getRule.getName
		val ICompletionProposal proposal = createCompletionProposal(proposalText, displayText, null, context)
		if (proposal instanceof ConfigurableCompletionProposal) {
			val ConfigurableCompletionProposal configurable = proposal
			configurable.setSelectionStart(configurable.getReplacementOffset + 1)
			configurable.setSelectionLength(proposalText.length - 2)
			configurable.setAutoInsertable(true)
			configurable.setSimpleLinkedMode(context.getViewer, proposalText.charAt(0), '\t');
			
		}
		acceptor.accept(proposal)
	}
}