package de.unistuttgart.iste.ps.skilled.validation

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.service.DependencyGraph
import de.unistuttgart.iste.ps.skilled.service.SKilLServices
import java.util.HashMap
import java.util.Map
import java.util.Set
import org.eclipse.core.resources.IProject
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.CheckType
import org.eclipse.xtext.validation.EValidatorRegistrar

/**
 * Validation for the correct usage of linked declaration. It checks whether the needed skill file is included.
 * 
 * @author Marco Link
 */
class ScopingValidator extends AbstractSKilLValidator {

	@Inject private SKilLServices services;

	public static val NOT_INCLUDED_FILE = "notIncludedFile";

	// Represents the dependency graph for the active editor.
	private DependencyGraph dependencyGraphInUse;

	// Saves all dependency graphs for the projects.
	private Map<IProject, DependencyGraph> dependencyGraphs;

	override register(EValidatorRegistrar registar) {
		dependencyGraphs = new HashMap<IProject, DependencyGraph>;
	}

	/**
	 * Creates dependency graphs for the given file when the file will be validated.
	 */
	@Check(CheckType.NORMAL)
	def checkFileDependencies(File file) {
		// Get the project in which the file is located.
		var IProject fileProject = services.getProject(file);

		if (dependencyGraphs.get(fileProject) != null) {
			dependencyGraphInUse = dependencyGraphs.get(fileProject);
		} else {
			dependencyGraphInUse = new DependencyGraph();
			dependencyGraphs.put(fileProject, dependencyGraphInUse);
		}

		var Set<File> files = services.getAll(file);

		dependencyGraphInUse.generate(files);
	}

	@Check(CheckType.NORMAL)
	def validTypeDeclarationReference(TypeDeclarationReference typeDeclarationReference) {
		validReferences(typeDeclarationReference, typeDeclarationReference.type);
	}

	@Check(CheckType.NORMAL)
	def valideDeclarationReferences(DeclarationReference declarationReference) {
		validReferences(declarationReference, declarationReference.type);
	}

	/**
	 * Checks whether the file, in which the linkedObject is located, is visible. 
	 * Else it will show a warning for the use.
	 */
	def validReferences(EObject object, EObject linkedObject) {
		var fileObject = object.eContainer;
		var fileLinkedObject = linkedObject.eContainer;
		while (fileObject.eContainer != null) {
			fileObject = fileObject.eContainer;
		}
		while (fileLinkedObject.eContainer != null) {
			fileLinkedObject = fileLinkedObject.eContainer;
		}

		var EReference reference;
		if (object instanceof DeclarationReference) {
			reference = SKilLPackage.Literals.DECLARATION_REFERENCE__TYPE;
		} else if (object instanceof TypeDeclarationReference) {
			reference = SKilLPackage.Literals.TYPE_DECLARATION_REFERENCE__TYPE;
		}

		var URI linkedObjectURI = linkedObject.eResource.URI;

		if (dependencyGraphInUse != null) {

			if (dependencyGraphInUse.getIncludedURIs(fileObject.eResource).contains(linkedObjectURI)) {
				// Everything is fine - Do nothing.
			} else {
				if (reference != null) {
					var String missingFile = fileLinkedObject?.eResource?.URI?.segments.last;
					warning("Required file isn't included.", object, reference, NOT_INCLUDED_FILE, missingFile);
				}
			}
		}
	}
}