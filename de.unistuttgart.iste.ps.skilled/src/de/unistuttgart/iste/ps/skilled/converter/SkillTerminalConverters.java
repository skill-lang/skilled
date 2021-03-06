package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;

import com.google.inject.Inject;


/**
 * Extends the default converter to add conversion for hexadecimal values.
 * 
 * @author Marco Link
 * @author Tobias Heck
 */
public class SkillTerminalConverters extends DefaultTerminalConverters {

    @Inject
    private HEXINTValueConverter hexintValueConverter;

    @ValueConverter(rule = "HEXINT")
    public IValueConverter<Long> HEXINT() {
        return hexintValueConverter;
    }
}
