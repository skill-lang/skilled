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
import java.util.ArrayList
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage

/**
 * @author Marco Link
 * @author Daniel Ryan Degutis
 */
class SKilLServices {

  def dispatch Set<File> getAll(File file) {
    return file.eResource.getAll
  }

  def dispatch Set<File> getAll(Resource resource) {
    val ResourceSet rs = getResourceSet(resource);
    return getAll(rs);
  }

  def dispatch Set<File> getAll(IProject project) {
    val ResourceSet rs = getResourceSet(project);
    return getAll(rs);
  }

  def dispatch Set<File> getAll(ResourceSet rs) {
    val Iterable<File> files2 = rs.resources.map(r|r.allContents.toList.filter(typeof(File))).flatten
    var Set<File> files = new HashSet
    for (f : files2) {
      files.add(f)
    }
    return files
  }

  def File getFile(String absolutePath) {
    var List<URI> uri = new ArrayList<URI>();
    uri.add(URI.createFileURI(absolutePath));
    var resourceSet = getResourceSet(uri);
    for (File f : getAll(resourceSet)) {
      return f;
    }
    return null;
  }

  def dispatch List<URI> getAllURIs(Resource resource) {
    val String platformString = resource.getURI.toPlatformString(true)
    val IProject project = ResourcesPlugin.getWorkspace.getRoot.getFile(new Path(platformString)).getProject

    return getAllURIs(project);
  }

  def dispatch List<URI> getAllURIs(IProject project) {
    var List<URI> uris = new LinkedList

    for (ifile : processContainer(project)) {
      uris.add(URI.createPlatformResourceURI(ifile.getFullPath.toString, true))
    }
    return uris
  }

  def List<IFile> processContainer(IContainer container) {
    val IResource [] members = container.members

    var files = new LinkedList

    for (member : members) {
      if (member instanceof IContainer) {
        files.addAll(processContainer(member))
      } else if (member instanceof IFile) {
        if (member.location.fileExtension.equals("skill")) {
          files.add(member)
        }
      }
    }
    return files
  }

  def dispatch ResourceSet getResourceSet(File file) {
    return file.eResource.getResourceSet
  }

  def dispatch ResourceSet getResourceSet(Resource resource) {
    val List<URI> uris = resource.getAllURIs

    return getResourceSet(uris);
  }

  def dispatch ResourceSet getResourceSet(IProject project) {
    val List<URI> uris = project.getAllURIs
    return getResourceSet(uris);
  }

  def dispatch ResourceSet getResourceSet(List<URI> uris) {
    var XtextResourceSet xtextResourceSet = new XtextResourceSet
    xtextResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE)

    for (uri : uris) {
      xtextResourceSet.getResource(uri, true)
    }
    return xtextResourceSet
  }

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

  def IFile getIFile(File file) {
    var project = getProject(file);
    var u = EcoreUtil2.getNormalizedResourceURI(file);
    var path = new Path(u.toPlatformString(false));
    var p = path.removeFirstSegments(1);
    var IFile4 = project.getFile(p);
    return IFile4;
  }

  def URI createAbsoluteURIFromRelative(String relativePath, URI relativeTo) {
    return URI.createURI(relativePath).resolve(relativeTo);
  }

  def File getMainFile(File file) {
    var Set<File> files = getAll(file);
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


  def dispatch Set<File> getAll(File file, boolean ignoreToolFiles) {

    return getAll(file.eResource, ignoreToolFiles);

  }

  def dispatch Set<File> getAll(Resource resource, boolean ignoreToolFiles) {

    val ResourceSet rs = resource.getResourceSet

    return getAll(rs, ignoreToolFiles);

  }

  def dispatch Set<File> getAll(IProject project, boolean ignoreToolFiles) {

    val ResourceSet rs = project.getResourceSet

    return getAll(rs, ignoreToolFiles);

  }

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

  def dispatch File getFile(String absolutePath) {

    return getFile(URI.createPlatformResourceURI(absolutePath, true));

  }

  def dispatch File getFile(URI uri) {

    var List<URI> uris = new ArrayList<URI>();

    uris.add(uri);

    var resourceSet = getResourceSet(uris);

    for (File f : getAll(resourceSet, false)) {

      return f;

    }

    return null;

  }
  
  def boolean isToolFile(URI uri) {

    var uriSegments = uri.segments;

    if (uriSegments.size() > 3) {

      if (uriSegments.get(2).equals(ImportURIValidator::TOOLSFOLDER)) {

        return true;

      }

    }

    return false;

  }

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

}