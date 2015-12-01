package de.unistuttgart.iste.ps.skilled.validation

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.Include
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.service.DependencyGraph
import de.unistuttgart.iste.ps.skilled.service.SKilLServices
import java.util.HashMap
import java.util.LinkedList
import java.util.List
import java.util.Map
import java.util.Set
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IncrementalProjectBuilder
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.EcoreUtil2
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
	private DependencyGraph dependencyGraphInUse;

	public static val NOT_INCLUDED_FILE = "notIncludedFile";
	public static val INDIRECT_OR_TRANSITIVE_INCLUDED_FILE = "indirectOrTransitiveIncludedFile";

	private Map<IProject, DependencyGraph> dependencyGraphs;
	private Map<IProject, List<File>> oldFiles;
	private Map<IProject, List<URI>> oldFilesURI;

	override register(EValidatorRegistrar registar) {
		dependencyGraphs = new HashMap<IProject, DependencyGraph>;
		oldFiles = new HashMap<IProject, List<File>>();
		oldFilesURI = new HashMap<IProject, List<URI>>();
	}

	@Check(CheckType.NORMAL)
	def checkFileDependencies(File file) {
		var IProject fileProject = services.getProject(file);

		if (dependencyGraphs.get(fileProject) != null) {
			dependencyGraphInUse = dependencyGraphs.get(services.getProject(file));
		} else {
			dependencyGraphInUse = new DependencyGraph();
			dependencyGraphs.put(fileProject, dependencyGraphInUse);
		}

		var Set<File> files = services.getAll(file);
		var List<File> copyOfAllFiles = new LinkedList<File>();
		var List<URI> allFilesUri = new LinkedList<URI>();
		for (File f : files) {
			copyOfAllFiles.add(EcoreUtil2.copy(f));
			allFilesUri.add(f.eResource.URI);
		}

		files = null;

		if (oldFiles.get(fileProject) == null) {
			dependencyGraphInUse.generate(copyOfAllFiles, allFilesUri);
			oldFiles.put(fileProject, copyOfAllFiles);
			oldFilesURI.put(fileProject, allFilesUri);
			rebuild(file);
		} else {
			var filesNew = copyOfAllFiles;
			var filesNewURI = allFilesUri;

			if (!equalsFileIncludes(oldFiles.get(fileProject), oldFilesURI.get(fileProject), filesNew, filesNewURI)) {
				dependencyGraphInUse.generate(copyOfAllFiles, allFilesUri);
				oldFiles.put(fileProject, copyOfAllFiles);
				oldFilesURI.put(fileProject, allFilesUri);
				rebuild(file);
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

		if ((dependencyGraphInUse.getDirectIncludesURI(fileObject.eResource).contains(linkedObjectURI)) ||
			(object.eResource.URI.equals(linkedObjectURI))) {
			// Everything is fine - Do nothing.
		} else if (dependencyGraphInUse.getAllIncludesURI(fileObject.eResource).contains(linkedObjectURI)) {

			if (reference != null) {
				warning("It is discouraged to use indirect or transitive includes", object, reference,
					INDIRECT_OR_TRANSITIVE_INCLUDED_FILE);
			}
		} else {
			if (reference != null) {
				error("Required file isn't included.", object, reference, NOT_INCLUDED_FILE);
			}
		}
	}

	/**
	 * Checks whether the given list of files have equals includes. The order of the includes does matter.
	 * @param files1 first list with files
	 * @param files1URI list with the URIs from files1
	 * @param files2 second list with files
	 * @param files2URI list with the URIs from files2
	 */
	def boolean equalsFileIncludes(List<File> files1, List<URI> files1URI, List<File> files2, List<URI> files2URI) {
		for (var int j = 0; j < files1.size(); j++) {
			var File file1 = files1.get(j);
			var boolean equal = false;
			for (var int i = 0; i < files2.size(); i++) {
				if (files1URI.get(j).path.equals(files2URI.get(i).path)) {
					if (equalIncludes(file1, files2.get(i), files1URI.get(j), files2URI.get(i))) {
						equal = true;
					} else {
						return false
					}
				} else {
					if ((i == files2.size() - 1) && (!equal)) {
						return false;
					}

				}
			}
		}
		return true;
	}

	/**
	 * Checks whether the given file have equals includes. The order of the includes does matter.
	 */
	def boolean equalIncludes(File file1, File file2, URI file1URI, URI file2URI) {
		var List<URI> importURI1 = new LinkedList<URI>();
		var List<URI> importURI2 = new LinkedList<URI>();
		for (Include i : file1.includes) {
			for (IncludeFile if1 : i.includeFiles) {
				importURI1.add(URI.createURI(if1.importURI).resolve(file1URI));
			}
		}
		for (Include i : file2.includes) {
			for (IncludeFile if1 : i.includeFiles) {
				importURI2.add(URI.createURI(if1.importURI).resolve(file2URI));
			}
		}
		if (!(importURI1.size() == importURI2.size)) {
			return false;
		}
		for (var int i = 0; i < importURI1.size(); i++) {
			if ((!importURI1.get(i).path.equals(importURI2.get(i).path))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Rebuilds the project of the given file.
	 * @param file
	 */
	def rebuild(File file) {
		var String platformString = file.eResource.getURI().toPlatformString(true);
		var IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
		var IProject project = ifile.getProject();
		if (project.getNature("org.eclipse.xtext.ui.shared.xtextNature") != null) {
			try {
				project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
				project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			} catch (Exception e) {
				e.printStackTrace
			}
		}
	}
}