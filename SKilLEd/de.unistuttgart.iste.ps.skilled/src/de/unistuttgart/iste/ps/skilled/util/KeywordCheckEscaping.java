package de.unistuttgart.iste.ps.skilled.util;

import java.util.ArrayList;

/**
 * Queries the SKilL-Generator to inquire ambigous identifiers that need
 * escaping in the Binding
 * 
 * @author Daniel Ryan Degutis
 */
public class KeywordCheckEscaping {
    public static ArrayList<String> requiresEscaping(String name) {
		ArrayList<String> languagesWhereEscapingRequired = new ArrayList<>();
		for (String language : SkillInstallation.allGeneratorNames()) {
			if (SkillInstallation.requiresEscaping(language, name)) {
				languagesWhereEscapingRequired.add(language);
			}
		}
		return languagesWhereEscapingRequired;
	}
}
