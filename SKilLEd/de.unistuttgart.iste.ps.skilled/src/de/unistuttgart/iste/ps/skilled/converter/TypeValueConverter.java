package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractLexerBasedConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.util.Strings;


/**
 * 
 * @author Tobias Heck
 * 
 *         Removes underscores from type names, so that 'car' and 'c_a_r' are equivalent.
 *
 */
public class TypeValueConverter extends AbstractLexerBasedConverter<String> {

    @Override
    public String toValue(String string, INode node) throws ValueConverterException {
        if (Strings.isEmpty(string))
            throw new ValueConverterException("Couldn't convert empty string.", node, null);
        try {
            return SKilLQualifiedNameConverter.makeEquivalent(string);
        } catch (Exception e) {
            throw new ValueConverterException("Couldn't convert '" + string + "'.", node, e);
        }
    }
}
