package de.unistuttgart.iste.ps.skilled.validation

import org.eclipse.xtext.validation.ImportUriValidator
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.CheckType
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.emf.common.util.URI
import de.unistuttgart.iste.ps.skilled.sKilL.IncludeFile

class ImportValidator extends ImportUriValidator {

	@Check(value=CheckType.FAST)
	override public void checkImportUriIsValid(EObject object) {
		var String importURI = getResolver().resolve(object);
		if (importURI != null && !EcoreUtil2.isValidUri(object, URI.createURI(importURI))) {
			var URI uri = object.eResource.URI;
			if (uri.isHierarchical() && !uri.isRelative() && (uri.isRelative() && !uri.isEmpty())) {
				uri = uri.resolve(uri);
			}
			var IncludeFile include = null
			if (object instanceof IncludeFile) {
				include = object
			}
			var path = URI.createURI(uri.segment(1)).appendSegments(include.importURI.replace("\\", "/").split("/")).toString
			error("Imported resource could not be found.", getResolver().getAttribute(object),
				"Imported resource could not be found.", path);
		}
	}

}