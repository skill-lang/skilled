package de.unistuttgart.iste.ps.skilled.util;

import java.util.ArrayList;


public class keywordCheckEscaping {
    public static String[] languages = { "C", "Scala", "Ada", "Java" }; // The Languages that will be checked

    /**
     * This Method checks if the entered keyword requires escaping.
     * 
     * @name The name that is checked.
     * @return List with the languages where escaping is required for the name.
     */
    public static ArrayList<String> requiresEscaping(String args) {
        ArrayList<String> returnList = new ArrayList<String>();
        for (String language : languages) {
            // String s = CommandLine.checkEscaping(language, null);
            returnList.add(language);
        }
        return returnList;
    }
}