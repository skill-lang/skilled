package de.unistuttgart.iste.ps.skilled.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference;


/**
 * @author Marco Link
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 */
public class SubtypesFinder {

    @Inject
    private ResourceDescriptionsProvider resourceDescriptionsProvider;

    @Inject
    private IContainer.Manager containerManager;

    public Set<TypeDeclaration> getSubtypes(TypeDeclaration typeDeclaration) {

        Set<TypeDeclaration> subtypes = new HashSet<>();
        IResourceDescriptions rDs = resourceDescriptionsProvider.getResourceDescriptions(typeDeclaration.eResource());
        IResourceDescription rD = rDs.getResourceDescription(typeDeclaration.eResource().getURI());

        for (IContainer c : containerManager.getVisibleContainers(rD, rDs)) {
            for (IEObjectDescription od : c.getExportedObjectsByType(SKilLPackage.Literals.TYPE_DECLARATION)) {

                TypeDeclaration potentialSubtype = getPotentialSubtype(typeDeclaration, od);

                if (isSubtype(potentialSubtype, typeDeclaration)) {
                    subtypes.add(potentialSubtype);
                }
            }
        }
        return subtypes;
    }

    private static TypeDeclaration getPotentialSubtype(TypeDeclaration typeDeclaration, IEObjectDescription od) {
        return (TypeDeclaration) typeDeclaration.eResource().getResourceSet().getEObject(od.getEObjectURI(), true);
    }

    private static boolean isSubtype(TypeDeclaration potentialSubtype, TypeDeclaration typeDeclaration) {
        if (!potentialSubtype.equals(typeDeclaration)) {
            for (TypeDeclarationReference supertyperef : potentialSubtype.getSupertypes()) {
                if (supertyperef.getType().equals(typeDeclaration)) {
                    return true;
                }
            }
        }
        return false;
    }

}
