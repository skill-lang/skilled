package de.unistuttgart.iste.ps.skilled.ui.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;

public class SKilLConstants {
	
	public static final String QUALIFIER = "de.unistuttgart.iste.ps.skilled.ui";
	
	public static final String IS_TOOL = "istool";
	public static String getIsTool(){
		return getString(IS_TOOL);
	}
	
	public static final String LIST_TOOLS = "listtools";
	public static String getListTools(){
		return getString(LIST_TOOLS);
	}
	
	public static final String GENERATOR = "generator";
	public static String getGenerator(){
		return getString(GENERATOR);
	}
	
	public static final String GENERATOR_PATH = "generatorpath";
	public static String getGeneratorPath(){
		return getString(GENERATOR_PATH);
	}
	
	public static final String LANGUAGE = "language";
	public static String getLanguage(){
		return getString(LANGUAGE);
	}
	
	public static final String LANGUAGE_PATH = "languagepath";
	public static String getLanguagePath(){
		return getString(LANGUAGE_PATH);
	}
	
	public static final String OUTPUT = "output";
	public static String getOutput(){
		return getString(OUTPUT);
	}
	
	public static final String OUTPUT_PATH = "outputpath";
	public static String getOutputPath(){
		return getString(OUTPUT_PATH);
	}
	
	public static final String EXECUTION_ENVIRONMENT = "executionenvironment";
	public static String getExecutionEnvironment(){
		return getString(EXECUTION_ENVIRONMENT);
	}
	
	public static final String EXECUTION_ENVIRONMENT_PATH = "executionenvironmentpath";
	public static String getExecutionEnvironmentPath(){
		return getString(EXECUTION_ENVIRONMENT_PATH);
	}
	
	public static final String LIST_OR_GENERATE = "listorgenerate";
	public static String getListOrGenerate(){
		return getString(LIST_OR_GENERATE);
	}
	
	public static final String MODULE = "module";
	public static String getModule(){
		return getString(MODULE);
	}
	
	public static final String MODULE_PATH = "modulepath";
	public static String getModulePath(){
		return getString(MODULE_PATH);
	}
	public static String getString(String prefKey) {
		return getString(prefKey, (String) null);	
	}
	
	public static String getString(String prefKey, String defaultValue) {
		IPreferencesService prefs = Platform.getPreferencesService();
//		IScopeContext[] contexts = new IScopeContext[] { ConfigurationScope.INSTANCE }; 
		IScopeContext[] contexts = null; 
		return prefs.getString(QUALIFIER, prefKey, defaultValue, contexts);
	}
	
	public static Boolean getBoolean(String prefKey) {
		IPreferencesService prefs = Platform.getPreferencesService();
//		IScopeContext[] contexts = new IScopeContext[] { ConfigurationScope.INSTANCE }; 
		IScopeContext[] contexts = null; 
//		return prefs.getBoolean(QUALIFIER, prefKey, defaultValue, contexts);
		return prefs.getBoolean(QUALIFIER, prefKey, false, contexts);
	}

}
