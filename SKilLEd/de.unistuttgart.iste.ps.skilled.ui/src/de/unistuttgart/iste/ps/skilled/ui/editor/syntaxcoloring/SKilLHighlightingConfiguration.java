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
    public static final String INTERFACE_ID = "interface";
    public final static String CROSS_REF = "CrossReference";

    @Override
    public void configure(IHighlightingConfigurationAcceptor acceptor) {
        super.configure(acceptor);
        acceptor.acceptDefaultHighlighting(HEADCOMMENT_ID, "Headcomment", headcommentTextStyle());
        acceptor.acceptDefaultHighlighting(INTERFACE_ID, "Interface", interfaceTextStyle());
        acceptor.acceptDefaultHighlighting(CROSS_REF, "Cross-References", crossReferenceTextStyle());
    }

    public TextStyle crossReferenceTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }

    public TextStyle interfaceTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }

    public TextStyle headcommentTextStyle() {
        TextStyle textStyle = new TextStyle();
        textStyle.setStyle(SWT.ITALIC);
        return textStyle;
    }
}
