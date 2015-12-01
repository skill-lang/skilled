package de.unistuttgart.iste.ps.skilled.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import de.unistuttgart.iste.ps.skilled.ui.internal.SKilLActivator;

//sets default preferences and retrieves preferences from memory
public class SKilLPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {		
		IPreferenceStore store = SKilLActivator.getInstance().getPreferenceStore();
		//store.setDefault(SKilLConstants.IS_TOOL, false);
		store.setDefault(SKilLConstants.GENERATOR, false);
		store.setDefault(SKilLConstants.GENERATOR_PATH, "");
		store.setDefault(SKilLConstants.LANGUAGE, false);
		store.setDefault(SKilLConstants.LANGUAGE_PATH, 1);
		store.setDefault(SKilLConstants.OUTPUT, false);
		store.setDefault(SKilLConstants.OUTPUT_PATH, "");
		store.setDefault(SKilLConstants.EXECUTION_ENVIRONMENT, false);
		store.setDefault(SKilLConstants.EXECUTION_ENVIRONMENT_PATH, 1);
		store.setDefault(SKilLConstants.MODULE, false);
		store.setDefault(SKilLConstants.MODULE_PATH, "");
	}
	
	protected IPreferenceStore getPreferenceStore() {
		return SKilLActivator.getInstance().getPreferenceStore();
	}
}