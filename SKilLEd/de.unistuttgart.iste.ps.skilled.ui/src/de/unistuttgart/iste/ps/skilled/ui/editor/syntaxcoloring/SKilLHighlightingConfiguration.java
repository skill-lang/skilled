package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;


/**
 * Custom Highlichting Configuration for SKilL. It is needed for custom syntax coloring. This will create custom entries in
 * the eclipse settings for syntax coloring with its default settings.
 * 
 * @author Marco Link
 *
 */
public class SKilLHighlightingConfiguration implements IHighlightingConfiguration {

    public static final String HEADCOMMENT_ID = "headcomment";
    public static final String KEYWORD_ID = "keyword";
    public static final String PUNCTUATION_ID = "punctuation";
    public static final String COMMENT_ID = "comment";
    public static final String STRING_ID = "string";
    public static final String NUMBER_ID = "number";
    public static final String DEFAULT_ID = "default";
    public static final String USERTYPE_ID = "usertype";
    public static final String INTERFACE_ID = "interface";
    public static final String ENUM_ID = "enum";
    public static final String TYPEDEF_ID = "typedef";
    public static final String BUILDINTYPE_ID = "buildintype";
    public static final String DATA_ID = "data";
    public static final String CONSTANT_ID = "constant";
    public static final String VIEW_ID = "view";
    public static final String HINT_ID = "hint";
    public static final String RESTRICTION_ID = "restriction";
    public static final String COMPOUND_ID = "compound";

    /**
     * Registers the default styles for the semantic highlighting stage.
     * 
     * @param acceptor
     *            the acceptor is used to announce the various default styles. It is never <code>null</code>.
     * @see IHighlightingConfigurationAcceptor#acceptDefaultHighlighting(String, String,
     *      org.eclipse.xtext.ui.editor.utils.TextStyle)
     */
    @Override
    public void configure(IHighlightingConfigurationAcceptor acceptor) {
        acceptor.acceptDefaultHighlighting(HEADCOMMENT_ID, "Headcomment", headcommentTextStyle());
        acceptor.acceptDefaultHighlighting(KEYWORD_ID, "Keyword", keywordTextStyle());
        acceptor.acceptDefaultHighlighting(PUNCTUATION_ID, "Punctuation character", punctuationTextStyle());
        acceptor.acceptDefaultHighlighting(COMMENT_ID, "Comment", commentTextStyle());
        acceptor.acceptDefaultHighlighting(STRING_ID, "String", stringTextStyle());
        acceptor.acceptDefaultHighlighting(NUMBER_ID, "Number", numberTextStyle());
        acceptor.acceptDefaultHighlighting(DEFAULT_ID, "Default", defaultTextStyle());
        acceptor.acceptDefaultHighlighting(USERTYPE_ID, "Usertype", usertypeTextStyle());
        acceptor.acceptDefaultHighlighting(INTERFACE_ID, "Interface", interfaceTextStyle());
        acceptor.acceptDefaultHighlighting(ENUM_ID, "Enumeration", enumTextStyle());
        acceptor.acceptDefaultHighlighting(TYPEDEF_ID, "Typedef", typedefTextStyle());
        acceptor.acceptDefaultHighlighting(BUILDINTYPE_ID, "Build In Type", buildintypeTextStyle());
        acceptor.acceptDefaultHighlighting(DATA_ID, "Data Field", dataFieldTextStyle());
        acceptor.acceptDefaultHighlighting(CONSTANT_ID, "Constant Field", constantFieldTextStyle());
        acceptor.acceptDefaultHighlighting(VIEW_ID, "View Field", viewFieldTextStyle());
        acceptor.acceptDefaultHighlighting(HINT_ID, "Hint", hintTextStyle());
        acceptor.acceptDefaultHighlighting(RESTRICTION_ID, "Restriction", restrictionTextStyle());
        acceptor.acceptDefaultHighlighting(COMPOUND_ID, "Compound Type", compoundFieldTextStyle());
    }

    /**
     * Default syntax coloring setting.
     * 
     * @return
     */
    public static TextStyle defaultTextStyle() {
        TextStyle textStyle = new TextStyle();
        return textStyle;
    }

    /**
     * Default syntax coloring setting for numbers.
     * 
     * @return
     */
    public static TextStyle numberTextStyle() {
        TextStyle textStyle = defaultTextStyle().copy();
        textStyle.setColor(new RGB(125, 125, 125));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for strings.
     * 
     * @return
     */
    public static TextStyle stringTextStyle() {
        TextStyle textStyle = defaultTextStyle().copy();
        textStyle.setColor(new RGB(42, 0, 255));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for multiline comments.
     * 
     * @return
     */
    public static TextStyle commentTextStyle() {
        TextStyle textStyle = defaultTextStyle().copy();
        textStyle.setColor(new RGB(63, 127, 95));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for keywords.
     * 
     * @return
     */
    public static TextStyle keywordTextStyle() {
        TextStyle textStyle = defaultTextStyle().copy();
        textStyle.setColor(new RGB(149, 0, 85));
        textStyle.setStyle(SWT.BOLD);
        return textStyle;
    }

    /**
     * Default syntax coloring setting for punctuation.
     * 
     * @return
     */
    public static TextStyle punctuationTextStyle() {
        TextStyle textStyle = defaultTextStyle().copy();
        return textStyle;
    }

    /**
     * Default syntax coloring setting for usertypes.
     * 
     * @return
     */
    public static TextStyle usertypeTextStyle() {
        TextStyle textStyle = new TextStyle();
        return textStyle;
    }

    /**
     * Default syntax coloring setting for interfaces.
     * 
     * @return
     */
    public static TextStyle interfaceTextStyle() {
        TextStyle textStyle = new TextStyle();
        return textStyle;
    }

    /**
     * Default syntax coloring setting for enums.
     * 
     * @return
     */
    public static TextStyle enumTextStyle() {
        TextStyle textStyle = new TextStyle();
        return textStyle;
    }

    /**
     * Default syntax coloring setting for typedefs.
     * 
     * @return
     */
    public static TextStyle typedefTextStyle() {
        TextStyle textStyle = new TextStyle();
        return textStyle;
    }

    /**
     * Default syntax coloring setting for headcomments.
     * 
     * @return
     */
    public static TextStyle headcommentTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setColor(new RGB(63, 95, 191));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for build in types.
     * 
     * @return
     */
    public static TextStyle buildintypeTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.BOLD);
        textStyle.setColor(new RGB(149, 0, 85));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for data fields.
     * 
     * @return
     */
    public static TextStyle dataFieldTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setColor(new RGB(106, 62, 62));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for constant fields.
     * 
     * @return
     */
    public static TextStyle constantFieldTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC | SWT.BOLD);
        textStyle.setColor(new RGB(0, 0, 192));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for view fields.
     * 
     * @return
     */
    public static TextStyle viewFieldTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setColor(new RGB(106, 62, 62));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for compound types fields.
     * 
     * @return
     */
    public static TextStyle compoundFieldTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setColor(new RGB(106, 62, 62));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for hints.
     * 
     * @return
     */
    public static TextStyle hintTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setColor(new RGB(127, 159, 191));
        return textStyle;
    }

    /**
     * Default syntax coloring setting for restrictions.
     * 
     * @return
     */
    public static TextStyle restrictionTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setColor(new RGB(100, 100, 100));
        return textStyle;
    }
}
