/*
 * generated by Xtext
 */
package de.unistuttgart.iste.ps.skilled;

import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;

import de.unistuttgart.iste.ps.skilled.converter.SKilLQualifiedNameConverter;
import de.unistuttgart.iste.ps.skilled.converter.SKilLQualifiedNameProvider;
import de.unistuttgart.iste.ps.skilled.converter.SKilLTerminalConverters;


/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class SKilLRuntimeModule extends de.unistuttgart.iste.ps.skilled.AbstractSKilLRuntimeModule {
    @Override
    public Class<? extends IValueConverterService> bindIValueConverterService() {
        return SKilLTerminalConverters.class;
    }

    @Override
    public Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
        return SKilLQualifiedNameProvider.class;
    }

    public static Class<? extends IQualifiedNameConverter> bindIQualifiedNameConverter() {
        return SKilLQualifiedNameConverter.class;
    }
}
