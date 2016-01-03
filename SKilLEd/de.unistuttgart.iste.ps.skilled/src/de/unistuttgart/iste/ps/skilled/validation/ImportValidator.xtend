package de.unistuttgart.iste.ps.skilled.validation

import org.eclipse.xtext.validation.ImportUriValidator
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.validation.CheckType
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.emf.common.util.URI

class ImportValidator extends ImportUriValidator {

	@Check(value=CheckType.FAST)
	override public void checkImportUriIsValid(EObject object) {
		var String importURI = getResolver().resolve(object);
		if (importURI != null && !EcoreUtil2.isValidUri(object, URI.createURI(importURI))) {
			var URI uri = object.eResource.URI;
			if (uri.isHierarchical() && !uri.isRelative() && (uri.isRelative() && !uri.isEmpty())) {
				uri = uri.resolve(uri);
			}
			var path = uri.path
			while (path.substring(0, 1).equals("/") || path.substring(0, 1).equals("\\")) path = path.substring(1)
			while (!path.substring(0, 1).equals("/") && !path.substring(0, 1).equals("\\")) path = path.substring(1)
			while (path.substring(0, 1).equals("/") || path.substring(0, 1).equals("\\")) path = path.substring(1)
			error("Imported resource could not be found.", getResolver().getAttribute(object),
				"Imported resource could not be found.", path);
		}
	}

}