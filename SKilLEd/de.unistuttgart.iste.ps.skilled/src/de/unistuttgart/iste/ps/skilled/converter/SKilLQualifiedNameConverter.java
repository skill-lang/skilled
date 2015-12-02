package de.unistuttgart.iste.ps.skilled.converter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.util.Strings;


/**
 * 
 * @author Marco Link
 *
 */
public class SKilLQualifiedNameConverter implements IQualifiedNameConverter {

    @Override
    public String toString(QualifiedName qualifiedName) {
        if (qualifiedName == null)
            throw new IllegalArgumentException("Qualified name cannot be null");
        return qualifiedName.toString(getDelimiter());
    }

    @Override
    public QualifiedName toQualifiedName(String qualifiedNameAsString) {

        if (qualifiedNameAsString == null)
            throw new IllegalArgumentException("Qualified name cannot be null");
        if (qualifiedNameAsString.equals(""))
            throw new IllegalArgumentException("Qualified name cannot be empty");
        if (Strings.isEmpty(getDelimiter()))
            return QualifiedName.create(qualifiedNameAsString);
        List<String> segs = getDelimiter().length() == 1 ? Strings.split(qualifiedNameAsString, getDelimiter().charAt(0))
                : Strings.split(qualifiedNameAsString, getDelimiter());
        List<String> segsConverted = new LinkedList<String>();
        for (String s : segs) {
            segsConverted.add((makeEquivalent(s)));
        }
        return QualifiedName.create(segsConverted);
    }

    public String getDelimiter() {
        return ".";
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
