package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Enuminstance;
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent;


/**
 * @author Tobias Heck
 * @author Marco Link
 * @author Daniel Ryan Degutis
 */
public class SKilLQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {

    @Inject
    SKilLQualifiedNameConverter qualifiedNameConverter;

    public static QualifiedName qualifiedName(Declaration declaration) {
        return QualifiedName.create(SKilLQualifiedNameConverter.makeEquivalent(declaration.getName()));
    }

    static QualifiedName qualifiedName(Fieldcontent field) {
        EObject eObj = field.eContainer();
        while (!Declaration.class.isInstance(eObj)) {
            eObj = eObj.eContainer();
        }
        if (eObj == null) {
            return QualifiedName.create(SKilLQualifiedNameConverter.makeEquivalent(field.getName()));
        }
        return QualifiedName.create(SKilLQualifiedNameConverter.makeEquivalent(((Declaration) eObj).getName()),
                SKilLQualifiedNameConverter.makeEquivalent(field.getName()));
    }

    static QualifiedName qualifiedName(Enuminstance enuminstance) {
        EObject eObj = enuminstance.eContainer();
        while (!Declaration.class.isInstance(eObj)) {
            eObj = eObj.eContainer();
        }
        if (eObj == null) {
            return QualifiedName.create(SKilLQualifiedNameConverter.makeEquivalent(enuminstance.getName()));
        }
        return QualifiedName.create(SKilLQualifiedNameConverter.makeEquivalent(((Declaration) eObj).getName()),
                SKilLQualifiedNameConverter.makeEquivalent(enuminstance.getName()));
    }

}
