package de.unistuttgart.iste.ps.skilled.validation

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.formatting2.SKilLImportOrganizer
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.Include
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference
import de.unistuttgart.iste.ps.skilled.util.DependencyGraph.DependencyGraph
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import java.util.ArrayList
import java.util.HashMap
import java.util.Map
import java.util.Set
import org.eclipse.core.resources.IProject
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.EValidatorRegistrar
import java.util.HashSet

/**
 * Validation for the correct usage of linked declaration. It checks whether the needed skill file is included.
 * 
 * @author Marco Link
 */
class ScopingValidator extends AbstractSKilLValidator {

  @Inject private SKilLServices services;

  public static val NOT_INCLUDED_FILE = "notIncludedFile";
  public static val DUPLICATED_INCLUDED_FILE = "duplicatedIncludedFile";
  public static val UNUSED_INCLUDED_FILE = "unusedIncludedFile";

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
  @Check
  def checkFileDependencies(File file) {
    // Get the project in which the file is located.
    var IProject fileProject = services.getProject(file);

    if (fileProject != null) {
      if (dependencyGraphs.get(fileProject) != null) {
        dependencyGraphInUse = dependencyGraphs.get(fileProject);
      } else {
        dependencyGraphInUse = new DependencyGraph();
        dependencyGraphs.put(fileProject, dependencyGraphInUse);
      }

      var Set<File> files = services.getAll(file);

      dependencyGraphInUse.generateIgnoreOrigin(file, files);
    }
  }

  @Check
  def TEST(File file) {
    var mainFile = services.getMainFile(file);
    if (mainFile != null) {
      if (mainFile.equals(file)) {
        return
      }
    }
    var imp = new SKilLImportOrganizer(dependencyGraphInUse);
//    var map = new HashMap<Integer, IncludeFile>();
//    var map2 = new HashMap<URI, IncludeFile>();
//    var uris = new ArrayList<URI>();
//
//    var uris2 = new HashSet<URI>();
//
//    var includes = file.includes;
//    var count = 0;
//    for (Include inc : includes) {
//      for (IncludeFile i : inc.includeFiles) {
//        uris.add(services.createAbsoluteURIFromRelative(i.importURI, file.eResource.URI));
//        uris2.add(services.createAbsoluteURIFromRelative(i.importURI, file.eResource.URI));
//        map2.put(services.createAbsoluteURIFromRelative(i.importURI, file.eResource.URI), i);
//        map.put(count, i);
//        count++;
//      }
//    }
//    var index = imp.getIndexOfDuplicateImports(uris);
//
//    for (var i = 0; i < index.size(); i++) {
//      warning("Duplicate include.", map.get(index.get(i)), SKilLPackage.Literals.INCLUDE_FILE__IMPORT_URI, DUPLICATED_INCLUDED_FILE);
//    }
//
//    var unused = imp.getUnusedImports(file.eResource.getURI(), uris2, dependencyGraphInUse.getNeededIncludes(file.eResource));
//    for (URI u : unused) {
//      if (map2.get(u) != null) {
//        warning("Unused include.", map2.get(u), SKilLPackage.Literals.INCLUDE_FILE__IMPORT_URI, UNUSED_INCLUDED_FILE);
//      }
//    }
    var duplicate = imp.a(file);
    for (IncludeFile f : duplicate) {
      warning("Duplicate include.", f, SKilLPackage.Literals.INCLUDE_FILE__IMPORT_URI, DUPLICATED_INCLUDED_FILE);
    }
    var unused = imp.getUnusedImports2(file);
    for (IncludeFile f : unused) {
      warning("Unused include.", f, SKilLPackage.Literals.INCLUDE_FILE__IMPORT_URI, UNUSED_INCLUDED_FILE);
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