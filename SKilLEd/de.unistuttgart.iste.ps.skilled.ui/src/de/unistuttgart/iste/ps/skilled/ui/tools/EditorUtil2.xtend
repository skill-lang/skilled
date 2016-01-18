package de.unistuttgart.iste.ps.skilled.ui.tools

import de.unistuttgart.iste.ps.skilled.sKilL.File
import java.util.HashSet
import java.util.LinkedList
import java.util.List
import java.util.Set
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet

/**
 * @author Marco Link
 */
class EditorUtil2 {

	def Set<File> getAll(IProject project) {
		var Set<File> files = new HashSet();
		var Iterable<File> files2;
		var ResourceSet rs = getResourceSet(project);

		files2 = rs.resources.map(r|r.allContents.toList.filter(typeof(File))).flatten;
		for (File f : files2) {
			files.add(f);
		}
		return files;
	}

	def List<URI> getAllURIs(IProject project) {
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

	def ResourceSet getResourceSet(IProject project) {
		var List<URI> uris = getAllURIs(project);
		var XtextResourceSet xtextResourceSet = new XtextResourceSet();
		xtextResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		for (URI uri : uris) {
			xtextResourceSet.getResource(uri, true);
		}
		return xtextResourceSet;
	}

	def File getFileFromURI(URI uri) {
		var XtextResourceSet xtextResourceSet = new XtextResourceSet();
		xtextResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		xtextResourceSet.getResource(uri, true);
		var files2 = xtextResourceSet.resources.map(r|r.allContents.toList.filter(typeof(File))).flatten;

		return files2.get(0);
	}

}