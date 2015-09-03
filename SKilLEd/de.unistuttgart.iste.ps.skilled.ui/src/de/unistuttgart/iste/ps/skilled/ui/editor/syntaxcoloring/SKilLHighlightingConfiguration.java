package de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring;

import org.eclipse.swt.SWT;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;


/**
 * Custom Highlichting Configuration for SKilL. It is needed for custom syntax coloring. This will create custom entries in
 * the eclipse settings for syntax coloring with its default settings.
 * 
 * @author Marco Link
 *
 */
public class SKilLHighlightingConfiguration extends DefaultHighlightingConfiguration {

    public static final String HEADCOMMENT_ID = "headcomment";
    public static final String USERTYPE_ID = "usertype";
    public static final String INTERFACE_ID = "interface";
    public static final String ENUM_ID = "enum";
    public static final String TYPEDEF_ID = "typedef";
    public static final String BUILDINTYPE_ID = "buildintype";
    public static final String CROSS_REF = "crossreference";

    @Override
    public void configure(IHighlightingConfigurationAcceptor acceptor) {
        super.configure(acceptor);
        acceptor.acceptDefaultHighlighting(HEADCOMMENT_ID, "Headcomment", headcommentTextStyle());
        acceptor.acceptDefaultHighlighting(USERTYPE_ID, "Usertype", usertypeTextStyle());
        acceptor.acceptDefaultHighlighting(INTERFACE_ID, "Interface", interfaceTextStyle());
        acceptor.acceptDefaultHighlighting(ENUM_ID, "Enumeration", enumTextStyle());
        acceptor.acceptDefaultHighlighting(TYPEDEF_ID, "Typedef", typedefTextStyle());
        acceptor.acceptDefaultHighlighting(CROSS_REF, "Cross-References", crossReferenceTextStyle());
        acceptor.acceptDefaultHighlighting(BUILDINTYPE_ID, "Build In Type", buildintypeTextStyle());
    }

    public static TextStyle crossReferenceTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }

    public static TextStyle usertypeTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }

    public static TextStyle interfaceTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }

    public static TextStyle enumTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }
    
    public static TextStyle typedefTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }

    public static TextStyle headcommentTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }

    public static TextStyle buildintypeTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }
}
