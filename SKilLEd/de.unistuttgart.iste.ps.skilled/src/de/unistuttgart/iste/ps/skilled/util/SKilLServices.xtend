package de.unistuttgart.iste.ps.skilled.util

import de.unistuttgart.iste.ps.skilled.sKilL.File
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
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet

/**
 * This class contains usefull functions for the daily use. e.g get all skill files of a project.
 * 
 * @author Marco Link
 */
class SKilLServices {

	def dispatch Set<File> getAll(Resource resource) {
		var Set<File> files = new HashSet();
		var Iterable<File> files2;
		var ResourceSet rs = getResourceSet(resource);

		files2 = rs.resources.map(r|r.allContents.toList.filter(typeof(File))).flatten;
		for (File f : files2) {
			files.add(f);
		}
		return files;
	}

	def dispatch Set<File> getAll(File file) {
		return getAll(file.eResource);
	}

	def List<URI> getAllURIs(Resource resource) {
		var String platformString = resource.getURI().toPlatformString(true);
		var IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
		var IProject project = ifile.getProject();

		var List<IFile> ifiles = processContainer(project);
		var List<URI> uris = new LinkedList<URI>();
		for (IFile ifileee : ifiles) {
			uris.add(URI.createPlatformResourceURI(ifileee.getFullPath().toString(), true));
		}
		return uris;
	}

	def List<IFile> processContainer(IContainer container) {
		var files = new LinkedList<IFile>();
		var IResource [] members = container.members();

		for (IResource member : members) {
			if (member instanceof IContainer) {
				files.addAll(processContainer(member));
			} else if (member instanceof IFile) {
				if (member.location.fileExtension.equals("skill")) {
					files.add(member);
				}
			}
		}
		return files
	}

	def dispatch ResourceSet getResourceSet(Resource resource) {
		var List<URI> uris = getAllURIs(resource);
		var XtextResourceSet xtextResourceSet = new XtextResourceSet();
		xtextResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		for (URI uri : uris) {
			xtextResourceSet.getResource(uri, true);
		}
		return xtextResourceSet;
	}

	def dispatch ResourceSet getResourceSet(File file) {
		return getResourceSet(file.eResource);
	}

	def dispatch IProject getProject(Resource resource) {
		var String platformString = resource.getURI().toPlatformString(false);
		var IFile ifile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString));
		return ifile.getProject();
	}

	def dispatch IProject getProject(File file) {
		return getProject(file.eResource);
	}
}