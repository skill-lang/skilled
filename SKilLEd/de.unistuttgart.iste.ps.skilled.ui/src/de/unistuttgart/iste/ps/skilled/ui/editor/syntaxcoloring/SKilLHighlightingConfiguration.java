package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

/**
 * Custom Highlichting Configuration for SKilL. It is needed for custom syntax
 * coloring. This will create custom entries in the eclipse settings for syntax
 * coloring with its default settings.
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
	public static final String HINT_ID = "hint";
	public static final String RESTRICTION_ID = "restriction";
	public static final String COMPOUND_ID = "compound";
	public static final String INVALID_TOKEN_ID = "invalidtoken";

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
		acceptor.acceptDefaultHighlighting(HINT_ID, "Hint", hintTextStyle());
		acceptor.acceptDefaultHighlighting(RESTRICTION_ID, "Restriction", restrictionTextStyle());
		acceptor.acceptDefaultHighlighting(COMPOUND_ID, "Compound Type", compoundFieldTextStyle());
		acceptor.acceptDefaultHighlighting(INVALID_TOKEN_ID, "Invalid Symbol", errorTextStyle());
	}

	public TextStyle defaultTextStyle() {
		TextStyle textStyle = new TextStyle();
		return textStyle;
	}

	public TextStyle numberTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(125, 125, 125));
		return textStyle;
	}

	public TextStyle stringTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(42, 0, 255));
		return textStyle;
	}

	public TextStyle commentTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(63, 127, 95));
		return textStyle;
	}

	public TextStyle keywordTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(127, 0, 85));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle punctuationTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		return textStyle;
	}

	public TextStyle usertypeTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(0, 0, 0));
		return textStyle;
	}

	public TextStyle interfaceTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(50, 63, 112));
		return textStyle;
	}

	public TextStyle enumTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(100, 70, 50));
		return textStyle;
	}

	public TextStyle typedefTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(0, 0, 0));
		return textStyle;
	}

	public TextStyle headcommentTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(241, 51, 127));
		return textStyle;
	}

	public TextStyle buildintypeTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(127, 0, 85));
		return textStyle;
	}

	public TextStyle dataFieldTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(127, 0, 85));
		return textStyle;
	}

	public TextStyle constantFieldTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(127, 0, 85));
		return textStyle;
	}

	public TextStyle compoundFieldTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(127, 0, 85));
		return textStyle;
	}

	public TextStyle hintTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(207, 207, 207));
		return textStyle;
	}

	public TextStyle restrictionTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setColor(new RGB(207, 207, 207));
		return textStyle;
	}

	public TextStyle errorTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		return textStyle;
	}

}
