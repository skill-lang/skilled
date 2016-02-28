package de.unistuttgart.iste.ps.skilled.converter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.naming.IQualifiedNameConverter.DefaultImpl;
import org.eclipse.xtext.naming.QualifiedName;


/**
 * Converts QualifiedNames to strings and back. It is customized for SKilL so the special conversion will be considered.
 * 
 * @author Marco Link
 * @author Tobias Heck
 */
public class SKilLQualifiedNameConverter extends DefaultImpl {

    @Override
    public QualifiedName toQualifiedName(String qualifiedNameAsString) {
        List<String> segsConverted = new LinkedList<>();

        for (String s : super.toQualifiedName(qualifiedNameAsString).getSegments()) {
            segsConverted.add((makeEquivalent(s)));
        }

        return QualifiedName.create(segsConverted);
    }

    /**
     * Make names case-insensitive,ignore single underscores and all underscores at the end
     * 
     * @param string
     *            - The String which will be converted.
     */
    public static String makeEquivalent(String string) {
        String converted = string.toLowerCase();
        int index = 0;
        while (converted.charAt(index) == '_') {
            index++;
            if (index == converted.length())
                return converted;
        }
        boolean wasUnderscore = true;
        while (index < converted.length()) {
            if (converted.charAt(index) != '_') {
                index++;
                wasUnderscore = false;
                continue;
            }
            if (wasUnderscore == false) {
                wasUnderscore = true;
                converted = converted.substring(0, index) + converted.substring(index + 1);
                continue;
            }
            index++;
        }
        if (!converted.matches("_*")) {
            while (converted.charAt(converted.length() - 1) == '_')
                converted = converted.substring(0, converted.length() - 1);
        }
        return converted;
    }
}
