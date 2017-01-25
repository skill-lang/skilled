package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import java.util.regex.Pattern;

import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;


/**
 * Custom Token to AttributeID Mapper for SKilL. It is needed for custom syntax coloring. Lexical Highlighting
 * 
 * @author Marco Link
 *
 */
public class SKilLAntlrTokenToAttributeIdMapper extends AbstractAntlrTokenToAttributeIdMapper {

    private static final Pattern QUOTED = Pattern.compile("(?:^'([^']*)'$)|(?:^\"([^\"]*)\")$", Pattern.MULTILINE);

    private static final Pattern PUNCTUATION = Pattern.compile("\\p{Punct}*");

    /**
     * Checks whether the the given name of a token matches its lexical rule and returns the style ID.
     */
    @Override
    protected String calculateId(String tokenName, int tokenType) {
        if ("RULE_HEADCOMMENT".equals(tokenName)) {
            return SKilLHighlightingConfiguration.HEADCOMMENT_ID;
        }
        if (PUNCTUATION.matcher(tokenName).matches()) {
            return SKilLHighlightingConfiguration.PUNCTUATION_ID;
        }
        if (QUOTED.matcher(tokenName).matches()) {
            return SKilLHighlightingConfiguration.KEYWORD_ID;
        }
        if ("RULE_STRING".equals(tokenName)) {
            return SKilLHighlightingConfiguration.STRING_ID;
        }
        if ("RULE_INT".equals(tokenName) || "RULE_FLOAT".equals(tokenName)) {
            return SKilLHighlightingConfiguration.NUMBER_ID;
        }
        if ("RULE_ML_COMMENT".equals(tokenName)) {
            return SKilLHighlightingConfiguration.COMMENT_ID;
        }
        return SKilLHighlightingConfiguration.DEFAULT_ID;
    }
}
