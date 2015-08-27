package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;


/**
 * Custom Semantic Highlighting Calculator for SKilL. It is needed for custom syntax coloring. Semantic Highlighting
 * 
 * @author Marco Link
 *
 */
public class SKilLSemanticHighlightingCalculator extends DefaultSemanticHighlightingCalculator {
    @Override
    public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {
        System.err.println("fjdsklfjdskljfk");
        if (resource == null || resource.getParseResult() == null)
            return;

        INode root = resource.getParseResult().getRootNode();
        for (INode node : root.getAsTreeIterable()) {
            if (node.getGrammarElement() instanceof CrossReference) {
                acceptor.addPosition(node.getOffset(), node.getLength(), SKilLHighlightingConfiguration.CROSS_REF);
            }
        }
    }
}
