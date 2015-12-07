package de.unistuttgart.iste.ps.skilled.converter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.naming.IQualifiedNameConverter.DefaultImpl;
import org.eclipse.xtext.naming.QualifiedName;


/**
 * 
 * @author Marco Link
 *
 */
public class SKilLQualifiedNameConverter extends DefaultImpl {

    @Override
    public QualifiedName toQualifiedName(String qualifiedNameAsString) {

        QualifiedName qualifiedName = super.toQualifiedName(qualifiedNameAsString);
        List<String> segsConverted = new LinkedList<String>();

        // If there is only one segment, it is an normal ID and therefore it will be converted with the typeValueConverter.
        if (qualifiedName.getSegments().size() <= 1) {
            return qualifiedName;
        }

        // If not get the segments and convert it.
        for (String s : qualifiedName.getSegments()) {
            segsConverted.add((makeEquivalent(s)));
        }

        return QualifiedName.create(segsConverted);
    }

    // make names case-insensitive and ignore single underscores
    public String makeEquivalent(String string) {
        string = string.toLowerCase();
        int index = 0;
        while (string.charAt(index) == '_') {
            index++;
            if (index == string.length())
                return string;
        }
        boolean wasUnderscore = true;
        while (index < string.length()) {
            if (string.charAt(index) != '_') {
                index++;
                wasUnderscore = false;
                continue;
            }
            if (wasUnderscore == false) {
                wasUnderscore = true;
                string = string.substring(0, index) + string.substring(index + 1);
                continue;
            }
            index++;
        }
        if (!string.matches("_*")) {
            while (string.charAt(string.length() - 1) == '_')
                string = string.substring(0, string.length() - 1);
        }
        return string;
    }
}
