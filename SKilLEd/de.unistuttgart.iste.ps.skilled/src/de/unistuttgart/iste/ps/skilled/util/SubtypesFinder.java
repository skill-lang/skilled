package de.unistuttgart.iste.ps.skilled.util;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration;
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference;


/**
 * Class for finding subtypes of a TypeDeclaration.
 * 
 * @author Marco Link
 *
 */
public class SubtypesFinder {

    @Inject
    private static ResourceDescriptionsProvider resourceDescriptionsProvider;

    @Inject
    private static IContainer.Manager containerManager;

    /**
     * This Method computes the subtypes from the given TypeDeclaration (Usertype or Interfacetype) and returns a set with
     * the subtypes.
     * 
     * @param typeDeclaration
     * @return a set with TypeDeclaration or an empty set, when there are no subtypes.
     */
    public static Set<TypeDeclaration> getSubtypes(TypeDeclaration typeDeclaration) {
        Set<TypeDeclaration> subtypes = new HashSet<TypeDeclaration>();

        // Get all visible resources.
        IResourceDescriptions resourceDescriptions = resourceDescriptionsProvider
                .getResourceDescriptions(typeDeclaration.eResource());
        IResourceDescription resourceDescription = resourceDescriptions
                .getResourceDescription(typeDeclaration.eResource().getURI());

        for (IContainer c : containerManager.getVisibleContainers(resourceDescription, resourceDescriptions)) {
            for (IEObjectDescription od : c.getExportedObjectsByType(SKilLPackage.Literals.TYPE_DECLARATION)) {

                // All found resources have to be cast to an TypeDeclaration object.
                TypeDeclaration potentialSubtype = (TypeDeclaration) typeDeclaration.eResource().getResourceSet()
                        .getEObject(od.getEObjectURI(), true);

                if (!potentialSubtype.equals(typeDeclaration)) {
                    for (TypeDeclarationReference supertyperef : potentialSubtype.getSupertypes()) {
                        if (supertyperef.getType().equals(typeDeclaration)) {
                            subtypes.add(potentialSubtype);
                        }
                    }
                }
            }
        }
        return subtypes;
    }
}
