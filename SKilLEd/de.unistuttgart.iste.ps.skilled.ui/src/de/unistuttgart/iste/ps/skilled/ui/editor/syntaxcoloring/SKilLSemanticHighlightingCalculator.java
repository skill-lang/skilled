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
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInTypeReference;
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference;
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;


/**
 * Custom Semantic Highlighting Calculator for SKilL. It is needed for custom syntax coloring. Semantic Highlighting
 * SOOOOOOOOOOOOOO BUUUUUNT
 * 
 * @author Marco Link
 *
 */
public class SKilLSemanticHighlightingCalculator extends DefaultSemanticHighlightingCalculator {
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
                    acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.USERTYPE_ID);
                } else if (dr.getType() instanceof Interfacetype) {
                    acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.INTERFACE_ID);
                } else if (dr.getType() instanceof Typedef) {
                    acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.TYPEDEF_ID);
                }
            } else if (node.getSemanticElement() instanceof BuildInTypeReference) {
                acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.BUILDINTYPE_ID);
            } else if (grammarElement instanceof RuleCall) {
                RuleCall rc = (RuleCall) grammarElement;
                AbstractRule r = rc.getRule();
                EObject c = grammarElement.eContainer();
                // Custom coloring for typenames.
                if ((r.getName().equals("ID")) && (c instanceof Assignment)
                        && ((Assignment) c).getFeature().equals("name")) {
                    // Coloring for usertypes.
                    if (node.getSemanticElement() instanceof Usertype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.USERTYPE_ID);

                    }
                    // Coloring for interfaces.
                    else if (node.getSemanticElement() instanceof Interfacetype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(),
                                SKilLHighlightingConfiguration.INTERFACE_ID);

                    }
                    // Coloring for enums.
                    else if (node.getSemanticElement() instanceof Enumtype) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.ENUM_ID);
                    }

                    else if (node.getSemanticElement() instanceof Typedef) {
                        acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.TYPEDEF_ID);
                    }
                }
            }
        }
    }
}
