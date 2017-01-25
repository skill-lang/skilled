package de.unistuttgart.iste.ps.skilled.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.validation.NamesAreUniqueValidationHelper;

import com.google.inject.Inject;

import de.unistuttgart.iste.ps.skilled.converter.SkillQualifiedNameProvider;
import de.unistuttgart.iste.ps.skilled.skill.Declaration;
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage;
import de.unistuttgart.iste.ps.skilled.util.SKilLServices;


/**
 * Validator for checking the uniqueness of names in a project.
 * 
 * @author Marco Link
 *
 */
public class NamesAreUniqueValidator extends org.eclipse.xtext.validation.NamesAreUniqueValidator {

    @Inject
    private NamesAreUniqueValidationHelper helper;

    @Inject
    private IContainer.Manager containermanager;

    @Inject
    private ResourceDescriptionsProvider resourceDescriptionsProvider;

    @Inject
    private SKilLServices services;

    @Override
    @Check
    public void checkUniqueNamesInResourceOf(EObject eObject) {
        // Only need to customize the check for Declarations because they have
        // to be project wide unique.
        if (!(eObject instanceof Declaration)) {
            super.checkUniqueNamesInResourceOf(eObject);
        } else {
            if (doCheckUniqueTypeNames((Declaration) eObject)) {
                // Still have to check if in one file the declaration is unique.
                super.checkUniqueNamesInResourceOf(eObject);
            }
        }
    }

    /**
     * Checks whether the given name of a declaration is project wide unique.
     * 
     * @param declaration
     *            to check
     * @return true if the name is unique, else false
     */
    public boolean doCheckUniqueTypeNames(Declaration declaration) {
        Iterable<IEObjectDescription> descriptions = getAllDeclarationDescriptions(declaration);
        IEObjectDescription declarationDescription = EObjectDescription
                .create(SkillQualifiedNameProvider.qualifiedName(declaration), declaration);
        URI eObjectURI = declarationDescription.getEObjectURI();

        for (IEObjectDescription eobjectDescription : descriptions) {
            if (eobjectDescription.getName().equals(SkillQualifiedNameProvider.qualifiedName(declaration))) {
                if (!eobjectDescription.getEObjectURI().equals(eObjectURI)) {
                    if ((!services.isToolFile(eObjectURI)) && (!services.isToolFile(eobjectDescription.getEObjectURI()))) {
                        String errorMessage = helper.getDuplicateNameErrorMessage(declarationDescription,
                                declaration.eClass(), SkillPackage.Literals.DECLARATION__NAME);
                        error(errorMessage, declaration, SkillPackage.Literals.DECLARATION__NAME);
                        return false;
                    } else if ((!services.isToolFile(eObjectURI))
                            && (!services.isToolFile(eobjectDescription.getEObjectURI()))) {
                        String errorMessage = helper.getDuplicateNameErrorMessage(declarationDescription,
                                declaration.eClass(), SkillPackage.Literals.DECLARATION__NAME);
                        error(errorMessage, declaration, SkillPackage.Literals.DECLARATION__NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Collects all IEObjectDescriptions from the Declarations in the project in which the declaration is located.
     * 
     * @param declaration
     * @return
     */
    public Iterable<IEObjectDescription> getAllDeclarationDescriptions(Declaration declaration) {
        List<IEObjectDescription> allDescriptions = new ArrayList<>();
        IResourceDescriptions resourceDescriptions = resourceDescriptionsProvider
                .getResourceDescriptions(declaration.eResource());
        IResourceDescription resourceDescription = resourceDescriptions
                .getResourceDescription(declaration.eResource().getURI());
        List<IContainer> visiblecontainers = containermanager.getVisibleContainers(resourceDescription,
                resourceDescriptions);
        for (IContainer container : visiblecontainers) {
            for (IEObjectDescription eobjectDescription : container.getExportedObjects()) {
                EObject object = eobjectDescription.getEObjectOrProxy();
                if (object instanceof Declaration) {
                    allDescriptions.add(eobjectDescription);
                }
            }
        }
        return allDescriptions;
    }
}
