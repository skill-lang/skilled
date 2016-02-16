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
 * Finds all subtypes of a given declaration
 * @author Marco Link
 * @author Nikolay Fateev
 * @author Daniel Ryan Degutis
 */
public class SubtypesFinder {

	@Inject
	private ResourceDescriptionsProvider resourceDescriptionsProvider;

	@Inject
	private IContainer.Manager containerManager;

	/**
	 * Gets all subtypes of a given declaration
	 * @param typeDeclaration the type whose subtypes should be queried
	 * @return a set of all subtypes
	 */
	public Set<TypeDeclaration> getSubtypes(TypeDeclaration typeDeclaration) {

		Set<TypeDeclaration> subtypes = new HashSet<>();
		IResourceDescriptions resourceDescriptions = resourceDescriptionsProvider
				.getResourceDescriptions(typeDeclaration.eResource());
		IResourceDescription resourceDescription = resourceDescriptions
				.getResourceDescription(typeDeclaration.eResource().getURI());

		for (IContainer container : containerManager.getVisibleContainers(resourceDescription, resourceDescriptions)) {
			for (IEObjectDescription objectDescription : container
					.getExportedObjectsByType(SKilLPackage.Literals.TYPE_DECLARATION)) {

				TypeDeclaration potentialSubtype = getPotentialSubtype(typeDeclaration, objectDescription);

				if (isSubtype(potentialSubtype, typeDeclaration)) {
					subtypes.add(potentialSubtype);
				}
			}
		}
		return subtypes;
	}

	/**
	 * Gets a potential subtype of a given type
	 * @param typeDeclaration the type whose potential subtype should be queried
	 * @param od the object description
	 * @return a potential subtype
	 */
	private static TypeDeclaration getPotentialSubtype(TypeDeclaration typeDeclaration, IEObjectDescription od) {
		return (TypeDeclaration) typeDeclaration.eResource().getResourceSet().getEObject(od.getEObjectURI(), true);
	}

	/**
	 * Queries whether a type is a subtype of another
	 * @param potentialSubtype
	 * @param typeDeclaration 
	 * @return true if potential subtype is a subtype of the given type
	 */
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
