package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import de.unistuttgart.iste.ps.skilled.sKilL.Basetype;
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInTypeReference;
import de.unistuttgart.iste.ps.skilled.sKilL.Constant;
import de.unistuttgart.iste.ps.skilled.sKilL.Data;
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Hint;
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype;
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;

/**
 * Custom Semantic Highlighting Calculator for SKilL. It is needed for custom
 * syntax coloring. Semantic Highlighting
 * 
 * @author Marco Link
 *
 */
public class SKilLSemanticHighlightingCalculator implements ISemanticHighlightingCalculator {
	@Override
	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {

		if (resource == null || resource.getParseResult() == null)
			return;

		INode root = resource.getParseResult().getRootNode();
		for (INode node : root.getAsTreeIterable()) {
			EObject grammarElement = node.getGrammarElement();
			EObject semanticElement = node.getSemanticElement();
			if ((grammarElement instanceof CrossReference) && (semanticElement instanceof DeclarationReference)) {
				DeclarationReference dr = (DeclarationReference) node.getSemanticElement();
				if (dr.getType() instanceof Usertype) {
					acceptor.addPosition(node.getOffset(), node.getLength(),
							SKilLHighlightingConfiguration.USERTYPE_ID);
				} else if (dr.getType() instanceof Interfacetype) {
					acceptor.addPosition(node.getOffset(), node.getLength(),
							SKilLHighlightingConfiguration.INTERFACE_ID);
				} else if (dr.getType() instanceof Typedef) {
					acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.TYPEDEF_ID);
				}
			} else if (semanticElement instanceof BuildInTypeReference) {
				acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.BUILDINTYPE_ID);
			} else if (semanticElement instanceof Restriction) {
				for (INode resNode : node.getAsTreeIterable()) {
					if (resNode.getText().equals("@")) {
						acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
								SKilLHighlightingConfiguration.RESTRICTION_ID);
					} else if (resNode.getGrammarElement() instanceof RuleCall) {
						RuleCall rc = (RuleCall) resNode.getGrammarElement();
						AbstractRule r = rc.getRule();
						EObject c = resNode.getGrammarElement().eContainer();
						if ((r.getName().equals("ID")) && (c instanceof Assignment)
								&& ((Assignment) c).getFeature().equals("restrictionName")) {
							acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
									SKilLHighlightingConfiguration.RESTRICTION_ID);
						}
					}
				}
			} else if (semanticElement instanceof Hint) {
				for (INode resNode : node.getAsTreeIterable()) {
					if (resNode.getText().equals("!")) {
						acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
								SKilLHighlightingConfiguration.HINT_ID);
					} else if (resNode.getGrammarElement() instanceof RuleCall) {
						RuleCall rc = (RuleCall) resNode.getGrammarElement();
						AbstractRule r = rc.getRule();
						EObject c = resNode.getGrammarElement().eContainer();
						if ((r.getName().equals("ID")) && (c instanceof Assignment)
								&& ((Assignment) c).getFeature().equals("hintName")) {
							acceptor.addPosition(resNode.getOffset(), resNode.getLength(),
									SKilLHighlightingConfiguration.HINT_ID);
						}
					}
				}
			} else if (grammarElement instanceof RuleCall) {
				RuleCall rc = (RuleCall) grammarElement;
				AbstractRule r = rc.getRule();
				EObject c = grammarElement.eContainer();
				// Custom coloring for typenames.
				if ((r.getName().equals("ID")) && (c instanceof Assignment)
						&& ((Assignment) c).getFeature().equals("name")) {
					// Coloring for usertypes.
					if (node.getSemanticElement() instanceof Usertype) {
						acceptor.addPosition(node.getOffset(), node.getLength(),
								SKilLHighlightingConfiguration.USERTYPE_ID);

					}
					// Coloring for interfaces.
					else if (node.getSemanticElement() instanceof Interfacetype) {
						acceptor.addPosition(node.getOffset(), node.getLength(),
								SKilLHighlightingConfiguration.INTERFACE_ID);

					}
					// Coloring for enums.
					else if (node.getSemanticElement() instanceof Enumtype) {
						acceptor.addPosition(node.getOffset(), node.getLength(),
								SKilLHighlightingConfiguration.ENUM_ID);
					}

					else if (node.getSemanticElement() instanceof Typedef) {
						acceptor.addPosition(node.getOffset(), node.getLength(),
								SKilLHighlightingConfiguration.TYPEDEF_ID);
					} else if (node.getSemanticElement() instanceof Data) {
						Data d = (Data) node.getSemanticElement();
						if (d.getFieldtype() instanceof Basetype) {
							acceptor.addPosition(node.getOffset(), node.getLength(),
									SKilLHighlightingConfiguration.DATA_ID);
						} else {
							acceptor.addPosition(node.getOffset(), node.getLength(),
									SKilLHighlightingConfiguration.COMPOUND_ID);
						}
					} else if (node.getSemanticElement() instanceof Constant) {
						acceptor.addPosition(node.getOffset(), node.getLength(),
								SKilLHighlightingConfiguration.CONSTANT_ID);
					}
				} else if ((r.getName().equals("ID")) && (c instanceof Assignment)
						&& ((Assignment) c).getFeature().equals("instances")) {
					acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.ENUM_ID);
				}
			}
		}
	}
}
