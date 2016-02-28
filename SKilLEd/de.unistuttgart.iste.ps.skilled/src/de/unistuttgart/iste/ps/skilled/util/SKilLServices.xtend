package de.unistuttgart.iste.ps.skilled.util

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.validation.ImportURIValidator
import java.util.ArrayList
import java.util.HashSet
import java.util.LinkedList
import java.util.List
import java.util.Set
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.ui.util.ResourceUtil

/**
 * This class contains useful methods which can be used within SKilLEd.
 * 
 * @author Marco Link
 * @author Daniel Ryan Degutis
 */
class SKilLServices {

  /**
   * Searches for a SKilL file. Tool files will be ignored.
   * @param absolutePath The absolute path of the file.
   * @return The SKilL file.
   */
  def dispatch File getFile(String absolutePath) {
    return getFile(URI.createFileURI(absolutePath));
  }

  /**
   * Searches for a SKilL file. Tool files will be ignored.
   * @param uri The uri of the file.
   * @return The SKilL file.
   */
  def dispatch File getFile(URI uri) {
    var List<URI> uris = new ArrayList<URI>();
    uris.add(uri);
    var resourceSet = getResourceSet(uris);
    for (File f : getAll(resourceSet, true)) {
      return f;
    }
    return null;
  }

  /**
   * Searches for all uris of the SKilL files which are located in a project in which the resource is located. 
   * Tool files will be ignored.
   * @param resource It will be searched in the project in which the resource is located.
   * @return A list of all uris of SKilL files in the resource project.
   */
  def dispatch List<URI> getAllURIs(Resource resource) {
    val String platformString = resource.getURI.toPlatformString(true)
    val IProject project = ResourcesPlugin.getWorkspace.getRoot.getFile(new Path(platformString)).getProject

    return getAllURIs(project);
  }

  /**
   * Searches for all uris of the SKilL files which are located in a project. Tool files will be ignored.
   * @param project The project in which should be searched.
   * @return A list of all uris of SKilL files in the project.
   */
  def dispatch List<URI> getAllURIs(IProject project) {
    var List<URI> uris = new LinkedList

    for (ifile : processContainer(project)) {
      uris.add(URI.createPlatformResourceURI(ifile.getFullPath.toString, true))
    }
    return uris
  }

  /**
   * Searches recursively for all SKilL files within a container.
   * @param container The container in which should be searched
   * @return A list of all IFiles within the container which represents a SKilL file.
   */
  def List<IFile> processContainer(IContainer container) {
    val IResource [] members = container.members

    var files = new LinkedList

    for (member : members) {
      if (member instanceof IContainer) {
        files.addAll(processContainer(member))
      } else if (member instanceof IFile) {
        if (member.location.fileExtension != null) {
          if (member.location.fileExtension.equals("skill")) {
            files.add(member)
          }
        }
      }
    }
    return files
  }

  /**
   * Returns a set of all resources in a project.
   * @param file All resources of the project in which this file is located will be returned.
   * @return a ResourceSet
   */
  def dispatch ResourceSet getResourceSet(File file) {
    return file.eResource.getResourceSet
  }

  /**
   * Returns a set of all resources in a project.
   * @param resource All resource of the project in which this resource is located will be returned.
   * @return a ResourceSet
   */
  def dispatch ResourceSet getResourceSet(Resource resource) {
    val List<URI> uris = resource.getAllURIs

    return getResourceSet(uris);
  }

  /**
   * Returns a set of all resources in a project.
   * @param project All resource of this project will be returned.
   * @return a ResourceSet
   */
  def dispatch ResourceSet getResourceSet(IProject project) {
    val List<URI> uris = project.getAllURIs
    return getResourceSet(uris);
  }

  /**
   * Returns a set of all given resources uris.
   * @param uris All resources linked to this uris will be returned.
   * @return a ResourceSet
   */
  def dispatch ResourceSet getResourceSet(List<URI> uris) {
    var XtextResourceSet xtextResourceSet = new XtextResourceSet
    xtextResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE)

    for (uri : uris) {
      xtextResourceSet.getResource(uri, true)
    }
    return xtextResourceSet
  }

  /**
   * Returns the project of a SKilL file. Or null if something went wrong, e.g. project is closed.
   * @param file
   * @return the project of the file.
   */
  def dispatch IProject getProject(File file) {
    return file.eResource.getProject
  }

  def dispatch IProject getProject(Resource resource) {
    try {
      val String platformString = resource.getURI.toPlatformString(false)
      return ResourcesPlugin.getWorkspace.getRoot.getFile(new Path(platformString)).getProject
    } catch (IllegalStateException e) {
      return null
    }
  }

  /**
   * Returns the workspace related IFile of the SKilL file.
   * @param file
   * @return IFile
   */
  def IFile getIFile(File file) {
    if (file != null) {
      return ResourceUtil.getFile(file.eResource);
    }
    return null;
  }

  /**
   * Creates a relative uri.
   * @param relativePath The path which should be converted to a relative one.
   * @param relativeTo The path to which the uri should be relative to.
   * @return A relative URI.
   */
  def URI createAbsoluteURIFromRelative(String relativePath, URI relativeTo) {
    return URI.createURI(relativePath).resolve(relativeTo);
  }

  /** 
   * Searches for a main file in a project.
   * The main file have the tag "@main" in the headcomments or it has the name of the project an ends with "-all".
   * @param file It will be searched in the project in which the file is located.
   * @return The main SKilL file or null if a main file doesn't exist.
   */
  def File getMainFile(File file) {
    var Set<File> files = getAll(file, true);
    for (File file1 : files) {
      for (String headcomment : file1.headComments) {
        if (headcomment.toLowerCase.contains("@main")) {
          return file1;
        }
      }
    }
    var project = getProject(file);
    var mainFileName = project.getName() + "-all" + ".skill";
    var f = project.getFile(mainFileName);
    if (f.exists) {
      return getFile(f.getFullPath().toPortableString());
    }
    return null;
  }

  /**
   * Checks wheter the given uri belongs to a SKilL file which is located in the Toolfolder within the project.
   * @param The uri to check.
   * @return true if it belongs to a toolfile, false otherwise
   */
  def boolean isToolFile(URI uri) {
    var uriSegments = uri.segments;
    if (uriSegments.size() > 3) {
      if (uriSegments.get(2).equals(ImportURIValidator::TOOLSFOLDER)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Searches for all Tool SKilL files of a specific Tool. 
   * The SKilL files of the main specification will be ignored.
   * @param toolname The name of the Tool which specifications should be searched.
   * @param project The project in which the Tool is located.
   * @return A set of all SKilL files from the Tool.
   */
  def Set<File> getToolFiles(String toolname, IProject project) {
    var Set<File> toolFiles = new HashSet<File>();
    var Set<File> files = getAll(project, false);
    for (File f : files) {
      var fileURI = EcoreUtil2.getURI(f);
      if (isToolFile(fileURI)) {
        var String[] segments = f.eResource().getURI().segments();
        if (segments.size() > 3) {
          if (segments.get(3).equals(toolname)) {
            toolFiles.add(f);
          }
        }

      }
    }
    return toolFiles;
  }

  def List<Field> getFields(Declaration declaration) {
    if (declaration != null) {
    }
    if (declaration instanceof TypeDeclaration) {
      return declaration.fields;
    } else if (declaration instanceof Enumtype) {
      return declaration.fields;
    }
    return null;
  }

  /**
   * Searches for all SKilL files in a project. 
   * @param file It will be searched in the project in which the file is located.
   * @param ignoreToolFiles Whether Tool files should be ignored or not.
   * @return A set of SKilL files.
   */
  def dispatch Set<File> getAll(File file, boolean ignoreToolFiles) {
    return getAll(file.eResource, ignoreToolFiles);
  }

  /**
   * Searches for all SKilL files in a project. 
   * @param resource It will be searched in the project in which the resource is located.
   * @param ignoreToolFiles Whether Tool files should be ignored or not.
   * @return A set of SKilL files.
   */
  def dispatch Set<File> getAll(Resource resource, boolean ignoreToolFiles) {
    val ResourceSet rs = getResourceSet(resource);
    return getAll(rs, ignoreToolFiles);
  }

  /**
   * Searches for all SKilL files in a project. 
   * @param project The project in which should be searched.
   * @param ignoreToolFiles Whether Tool files should be ignored or not.
   * @return A set of SKilL files.
   */
  def dispatch Set<File> getAll(IProject project, boolean ignoreToolFiles) {
    val ResourceSet rs = getResourceSet(project);
    return getAll(rs, ignoreToolFiles);
  }

  /**
   * Searches for all SKilL files in a project. 
   * @param rs The resourceSet.
   * @param ignoreToolFiles Whether Tool files should be ignored or not.
   * @return A set of SKilL files.
   */
  def dispatch Set<File> getAll(ResourceSet rs, boolean ignoreToolFiles) {
    val Iterable<File> files2 = rs.resources.map(r|r.allContents.toList.filter(typeof(File))).flatten
    var Set<File> files = new HashSet
    for (f : files2) {
      if (ignoreToolFiles) {
        if (!isToolFile(f.eResource.getURI())) {
          files.add(f);
        }
      } else {
        files.add(f)
      }
    }
    return files
  }
}