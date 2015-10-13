package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractLexerBasedConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.util.Strings;


/**
 * 
 * @author Marco Link
 *
 */
public class HEXINTValueConverter extends AbstractLexerBasedConverter<Long> {

    @Override
    public String toString(Long value) {
        return value.toString();
    }

    @Override
    public Long toValue(String string, INode node) {
        if (Strings.isEmpty(string))
            throw new ValueConverterException("Couldn't convert empty string to an hexint value.", node, null);
        try {
            return new Long(Long.valueOf(string.replaceFirst("0x", ""), 16));
        } catch (NumberFormatException e) {
            throw new ValueConverterException("Couldn't convert '" + string + "' to an hexint value.", node, e);
        }
    }

}
