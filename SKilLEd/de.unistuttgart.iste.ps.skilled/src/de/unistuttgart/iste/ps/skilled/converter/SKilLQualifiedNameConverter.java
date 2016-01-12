package de.unistuttgart.iste.ps.skilled.converter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.naming.IQualifiedNameConverter.DefaultImpl;
import org.eclipse.xtext.naming.QualifiedName;


/**
 * 
 * @author Marco Link Tobias Heck
 *
 */
public class SKilLQualifiedNameConverter extends DefaultImpl {

    @Override
    public QualifiedName toQualifiedName(String qualifiedNameAsString) {

        QualifiedName qualifiedName = super.toQualifiedName(qualifiedNameAsString);
        List<String> segsConverted = new LinkedList<String>();

        // Get the segments and convert it.
        for (String s : qualifiedName.getSegments()) {
            segsConverted.add((makeEquivalent(s)));
        }

        return QualifiedName.create(segsConverted);
    }

    /**
     * Make names case-insensitive,ignore single underscores and all underscores at the end
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
