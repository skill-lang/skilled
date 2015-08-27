/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring.SKilLAntlrTokenToAttributeIdMapper;
import de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring.SKilLHighlightingConfiguration;
import de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring.SKilLSemanticHighlightingCalculator;


/**
 * Use this class to register components to be used within the IDE.
 */
public class SKilLUiModule extends de.unistuttgart.iste.ps.skilled.ui.AbstractSKilLUiModule {
    public SKilLUiModule(AbstractUIPlugin plugin) {
        super(plugin);
    }

    // Includes the custom settings for syntax coloring.
    public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
        return SKilLHighlightingConfiguration.class;
    }

    // Includes the custom settings for syntax coloring.
    public Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
        return SKilLAntlrTokenToAttributeIdMapper.class;
    }

    public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
        return SKilLSemanticHighlightingCalculator.class;
    }
}
