/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;
import org.eclipse.xtext.ui.editor.hover.IEObjectHoverProvider;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AbstractAntlrTokenToAttributeIdMapper;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.ui.resource.SimpleResourceSetProvider;

import de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring.SKilLAntlrTokenToAttributeIdMapper;
import de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring.SKilLHighlightingConfiguration;
import de.unistuttgart.iste.ps.skilled.ui.editor.syntaxcoloring.SKilLSemanticHighlightingCalculator;
import de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc.SKilLObjectDocumentationProvider;
import de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc.SKilLObjectHoverProvider;

/**
 * This class is used to register components to be used within the IDE.
 */
public class SKilLUiModule extends de.unistuttgart.iste.ps.skilled.ui.AbstractSKilLUiModule {
	public SKilLUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}
	
	/**
	 * Provides the Hover Provider for Mouseover Documentation
	 * @return	The SKilL Hover Provider
	 */
    public Class<? extends IEObjectHoverProvider> bindIEObjectHoverProvider() {
        return SKilLObjectHoverProvider.class;
    }
    
	/**
	 * Provides the Documentation Provider for Mouseover Documentation
	 * @return	The SKilL Documentation Provider
	 */
    public Class<? extends IEObjectDocumentationProvider> bindIEObjectDocumentationProviderr() {
        return SKilLObjectDocumentationProvider.class;
    }
    
	@Override
	public java.lang.Class<? extends org.eclipse.xtext.ui.resource.IResourceSetProvider> bindIResourceSetProvider() {
		return SimpleResourceSetProvider.class;
	};

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
