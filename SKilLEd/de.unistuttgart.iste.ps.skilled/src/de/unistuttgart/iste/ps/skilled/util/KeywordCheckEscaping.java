package de.unistuttgart.iste.ps.skilled.util;

import java.util.ArrayList;

import de.ust.skill.main.CommandLine;


/**
 * @author Daniel Ryan Degutis
 */
public class KeywordCheckEscaping {
    public static ArrayList<String> requiresEscaping(String args) {
        ArrayList<String> languagesWhereEscapingRequired = new ArrayList<>();
        for (String language : CheckAvailableLanguages.getAvailableLanguages()) {
            if (CommandLine.checkEscaping(language, new String[] { args }).equals("true")) {
                languagesWhereEscapingRequired.add(language);
            }
        }
        return languagesWhereEscapingRequired;
    }
}
