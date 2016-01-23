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
 * @author Marco Link
 * @author Daniel Ryan Degutis
 */
class SKilLServices {

	def dispatch Set<File> getAll(File file) {
		return file.eResource.getAll
	}

	def dispatch Set<File> getAll(Resource resource) {
		val ResourceSet rs = resource.getResourceSet
		val Iterable<File> files2 = rs.resources.map(r|r.allContents.toList.filter(typeof(File))).flatten

		var Set<File> files = new HashSet

		for (f : files2) {
			files.add(f)
		}
		return files
	}

	def List<URI> getAllURIs(Resource resource) {
		val String platformString = resource.getURI.toPlatformString(true)
		val IProject project = ResourcesPlugin.getWorkspace.getRoot.getFile(new Path(platformString)).getProject

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

}