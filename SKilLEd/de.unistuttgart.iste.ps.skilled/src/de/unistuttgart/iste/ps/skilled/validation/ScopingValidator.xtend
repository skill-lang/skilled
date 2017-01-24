package de.unistuttgart.iste.ps.skilled.validation

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.File
import de.unistuttgart.iste.ps.skilled.skill.IncludeFile
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.util.DependencyGraph.DependencyGraph
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import java.util.Set
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.validation.Check
import de.unistuttgart.iste.ps.skilled.formatting2.SkillImportOrganizer

/**
 * Validation for the correct usage of linked declaration. It checks whether the needed skill file is included, 
 * or if there are missing, duplicated or unused includes within a skill file.
 * 
 * @author Marco Link
 */
class ScopingValidator extends AbstractSKilLComposedValidatorPart {

	@Inject private SKilLServices services;

	public static val NOT_INCLUDED_FILE = "notIncludedFile";
	public static val DUPLICATED_INCLUDED_FILE = "duplicatedIncludedFile";
	public static val UNUSED_INCLUDED_FILE = "unusedIncludedFile";

	// Represents the dependency graph for the active editor.
	private DependencyGraph dependencyGraph;

	/**
	 * Creates dependency graphs for the given file when the file will be validated.
	 */
	@Check
	def checkFileDependencies(File file) {
		if (services.getProject(file) != null) {
			var Set<File> files = services.getAll(file, true);
			dependencyGraph = new DependencyGraph();
			dependencyGraph.generateIgnoreOrigin(file, files);
		}
	}

	/**
	 * Raises a warning if there a unused or duplicated includes in a skill file.
	 */
	@Check
	def checkForUnsuedAndDuplicateIncludes(File file) {
		if (services.getProject(file) == null) {
			return
		}
		if (dependencyGraph == null) {
			return;
		}

		if (services.isToolFile(EcoreUtil2.getURI(file))) {
			return;
		}
		var mainFile = services.getMainFile(file);
		if (mainFile != null) {
			if (EcoreUtil2.equals(file, mainFile)) {
				return
			}
		}

		var imp = new SkillImportOrganizer(dependencyGraph);
		var String mainFileRelative = null;
		if (mainFile != null) {
			mainFileRelative = EcoreUtil2.getURI(mainFile).deresolve(EcoreUtil2.getURI(file)).path;
		}

		var duplicate = SkillImportOrganizer.getDuplicateIncludes(file);
		for (IncludeFile f : duplicate) {
			if (!f.importURI.equals(mainFileRelative)) {
				warning("Duplicate include.", f, SkillPackage.Literals.INCLUDE_FILE__IMPORT_URI,
					DUPLICATED_INCLUDED_FILE);
			}
		}
		var unused = imp.getUnusedImports(file);
		for (IncludeFile f : unused) {
			if (!f.importURI.equals(mainFileRelative)) {
				warning("Unused include.", f, SkillPackage.Literals.INCLUDE_FILE__IMPORT_URI, UNUSED_INCLUDED_FILE);
			}
		}
	}

	@Check
	def validTypeDeclarationReference(TypeDeclarationReference typeDeclarationReference) {
		validReferences(typeDeclarationReference, typeDeclarationReference.type);
	}

	@Check
	def valideDeclarationReferences(DeclarationReference declarationReference) {
		validReferences(declarationReference, declarationReference.type);
	}

	/**
	 * Checks whether the file, in which the linkedObject is located, is visible. 
	 * Else it will show a warning for the use.
	 */
	def validReferences(EObject object, EObject linkedObject) {
		if (dependencyGraph == null) {
			return;
		}

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
			reference = SkillPackage.Literals.DECLARATION_REFERENCE__TYPE;
		} else if (object instanceof TypeDeclarationReference) {
			reference = SkillPackage.Literals.TYPE_DECLARATION_REFERENCE__TYPE;
		}

		var URI linkedObjectURI = EcoreUtil2.getNormalizedResourceURI(linkedObject);

		if (dependencyGraph.getIncludedURIs(fileObject.eResource).contains(linkedObjectURI)) {
			// Everything is fine - Do nothing.
		} else {
			if (reference != null) {
				var fileLinkedObjectURI = EcoreUtil2.getURI(fileLinkedObject);
				if (fileLinkedObjectURI != null) {
					var String missingFile = fileLinkedObjectURI.segments.last;
					warning("Required file isn't included.", object, reference, NOT_INCLUDED_FILE, missingFile);

				}
			}
		}
	}
}
