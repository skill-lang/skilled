package de.unistuttgart.iste.ps.skilled.util;

import java.util.HashMap;

import de.ust.skill.main.CommandLine;

/**
 * Wrapps calls to skill to make them available in xtend.
 * 
 * @author Timm Felden
 *
 */
public class SkillInstallation {
    private static SkillInstallation instance = new SkillInstallation();

    private SkillInstallation() {
    }

    private String[] generatorNames;

    public static String[] allGeneratorNames() {
        if (null == instance.generatorNames) {
            instance.generatorNames = CommandLine.allGeneratorNames();
        }
        return instance.generatorNames;
    }

    /**
     * runs the current installation and returns the resulting lines as array of
     * strings
     * 
     * @return output on success and empty array on any kind of unexpected
     *         failure
     */
    public static void run(String... command) {
        CommandLine.main(command);
    }

    private HashMap<String, HashMap<String, Boolean>> escapeCache = new HashMap<>();

    public static boolean requiresEscaping(String language, String name) {
        HashMap<String, Boolean> cache = instance.escapeCache.get(language);
        if (null == cache) {
            instance.escapeCache.put(language, cache = new HashMap<>());
        }
        Boolean result = cache.get(name);
        if (null == result) {
            cache.put(name, result = CommandLine.checkEscaping(language, new String[] { name }).equals("true"));
        }
        return result;
    }
}
