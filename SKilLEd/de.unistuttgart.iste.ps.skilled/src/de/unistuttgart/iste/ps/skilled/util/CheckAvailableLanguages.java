package de.unistuttgart.iste.ps.skilled.util;

import de.ust.skill.main.CommandLine;


/**
 * @author Daniel Ryan Degutis
 */
public class CheckAvailableLanguages {
    public static String[] getAvailableLanguages() {
        return CommandLine.getKnownGeneratorNames();
    }
}
