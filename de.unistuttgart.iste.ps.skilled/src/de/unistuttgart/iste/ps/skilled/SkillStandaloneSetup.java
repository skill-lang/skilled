/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class SkillStandaloneSetup extends SkillStandaloneSetupGenerated{

	public static void doSetup() {
		new SkillStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
