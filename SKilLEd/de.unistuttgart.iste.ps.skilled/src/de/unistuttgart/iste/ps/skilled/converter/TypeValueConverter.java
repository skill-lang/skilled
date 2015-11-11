package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractLexerBasedConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.util.Strings;

/**
 * 
 * @author Tobias Heck
 * 
 * Removes underscores from type names, so that 'car' and 'c_a_r' are equivalent.
 *
 */
public class TypeValueConverter extends AbstractLexerBasedConverter<String> {

	@Override
	public String toValue(String string, INode node) throws ValueConverterException {
        if (Strings.isEmpty(string))
            throw new ValueConverterException("Couldn't convert empty string.", node, null);
        try {
        	string = string.toLowerCase();
        	int index = 0;
        	while (string.charAt(index) == '_') {
        		index++;
        		if (index == string.length()) return string;
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
        			string = string.substring(0,index) + string.substring(index + 1);
        			continue;
        		}
        		index++;
        	}
        	if (!string.matches("_*")) {
        		while (string.charAt(string.length() - 1) == '_')
        			string = string.substring(0, string.length() - 1);
        	}
        	return string;
        } catch (Exception e) {
            throw new ValueConverterException("Couldn't convert '" + string + "'.", node, e);
        }
	}
}
