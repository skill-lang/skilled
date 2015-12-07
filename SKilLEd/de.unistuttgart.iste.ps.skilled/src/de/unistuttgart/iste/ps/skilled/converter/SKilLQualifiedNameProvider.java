package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Enuminstance;
import de.unistuttgart.iste.ps.skilled.sKilL.Fieldcontent;


/**
 * 
 * @author Tobias Heck
 *
 */
public class SKilLQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {

    @Inject
    SKilLQualifiedNameConverter qualifiedNameConverter;

    // get normalized fieldcontent name
    static QualifiedName qualifiedName(Fieldcontent field) {
        EObject e = field.eContainer();
        Declaration d = null;
        while (!Declaration.class.isInstance(e)) {
            e = e.eContainer();
        }
        if (e == null) {
            QualifiedName q = QualifiedName.create(SKilLQualifiedNameConverter.makeEquivalent(field.getName()));
            return q;
        }
        d = (Declaration) e;
        QualifiedName q = QualifiedName.create(d.getName(), SKilLQualifiedNameConverter.makeEquivalent(field.getName()));
        return q;
    }

    // get normalized enuminstance name
    static QualifiedName qualifiedName(Enuminstance enuminstance) {
        EObject e = enuminstance.eContainer();
        Declaration d = null;
        while (!Declaration.class.isInstance(e)) {
            e = e.eContainer();
        }
        if (e == null) {
            QualifiedName q = QualifiedName.create(SKilLQualifiedNameConverter.makeEquivalent(enuminstance.getName()));
            return q;
        }
        d = (Declaration) e;
        QualifiedName q = QualifiedName.create(d.getName(),
                SKilLQualifiedNameConverter.makeEquivalent(enuminstance.getName()));
        return q;
    }
}
