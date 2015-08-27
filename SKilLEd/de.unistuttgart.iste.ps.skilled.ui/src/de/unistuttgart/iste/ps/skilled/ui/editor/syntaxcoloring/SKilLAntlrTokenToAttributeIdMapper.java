package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultAntlrTokenToAttributeIdMapper;


/**
 * Custom Token to AttributeID Mapper for SKilL. It is needed for custom syntax coloring. Lexical Highlighting
 * 
 * @author Marco Link
 *
 */
public class SKilLAntlrTokenToAttributeIdMapper extends DefaultAntlrTokenToAttributeIdMapper {

    @Override
    protected String calculateId(String tokenName, int tokenType) {
        if ("RULE_HEADCOMMENT".equals(tokenName)) {
            return "headcomment";
        }
        return super.calculateId(tokenName, tokenType);
    }
}
