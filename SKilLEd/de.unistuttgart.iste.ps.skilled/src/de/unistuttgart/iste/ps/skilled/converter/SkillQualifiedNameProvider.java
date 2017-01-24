package de.unistuttgart.iste.ps.skilled.converter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.skill.Declaration;
import de.unistuttgart.iste.ps.skilled.skill.Enuminstance;
import de.unistuttgart.iste.ps.skilled.skill.Fieldcontent;


/**
 * Computes the fully qualified name of an EObject, specifically for Declarations, Fields and Enuminstances. It will also
 * convert the name to make it equivalent for the SKilL context.
 * 
 * @author Tobias Heck
 * @author Marco Link
 * @author Daniel Ryan Degutis
 */
public class SkillQualifiedNameProvider extends DefaultDeclarativeQualifiedNameProvider {

    @Inject
    SkillQualifiedNameConverter qualifiedNameConverter;

    /**
     * Computes the qualified name of a Declaration. Because of the uniqueness of a Declarationname, it only contains the
     * Declarationname which will get converted to compare for equality.
     * 
     * @param declaration
     *            - for which a qualified name should be computed.
     * @return - the qualifiedname of the declaration.
     */
    public static QualifiedName qualifiedName(Declaration declaration) {
        return QualifiedName.create(SkillQualifiedNameConverter.makeEquivalent(declaration.getName()));
    }

    /**
     * Computes the qualified name of a Field. It will contain its Declaration in which it is located and its name. Both the
     * Declarationname and the Fieldname will get converted to compare for equality.
     * 
     * @param field
     *            - for which a qualified name should be computed.
     * @return - the qualifiedname of the field.
     */
    static QualifiedName qualifiedName(Fieldcontent field) {
        EObject eObj = field.eContainer();
        while (!Declaration.class.isInstance(eObj)) {
            eObj = eObj.eContainer();
        }
        if (eObj == null) {
            return QualifiedName.create(SkillQualifiedNameConverter.makeEquivalent(field.getName()));
        }
        return QualifiedName.create(SkillQualifiedNameConverter.makeEquivalent(((Declaration) eObj).getName()),
                SkillQualifiedNameConverter.makeEquivalent(field.getName()));
    }

    /**
     * Computes the qualified name of a Enuminstance. It will contain its Enumtype in which it is located and its name. Both
     * the Enumtypename and the Enuminstance will get converted to compare for equality.
     * 
     * @param enuminstance
     *            - for which a qualified name should be computed.
     * @return - the qualifiedname of the enuminstance.
     */
    static QualifiedName qualifiedName(Enuminstance enuminstance) {
        EObject eObj = enuminstance.eContainer();
        while (!Declaration.class.isInstance(eObj)) {
            eObj = eObj.eContainer();
        }
        if (eObj == null) {
            return QualifiedName.create(SkillQualifiedNameConverter.makeEquivalent(enuminstance.getName()));
        }
        return QualifiedName.create(SkillQualifiedNameConverter.makeEquivalent(((Declaration) eObj).getName()),
                SkillQualifiedNameConverter.makeEquivalent(enuminstance.getName()));
    }

}
