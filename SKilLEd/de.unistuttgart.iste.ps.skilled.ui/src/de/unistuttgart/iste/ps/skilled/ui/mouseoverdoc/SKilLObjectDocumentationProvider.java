package de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;

import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclarationReference;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;

/**
 * This Class defines the Object Documentation Provider for mouseover
 * documentation in SKilL and creates the object documentation string for the
 * documentation
 * 
 * @author Jan Berberich
 * @author Ken Singer
 */
public class SKilLObjectDocumentationProvider implements IEObjectDocumentationProvider {

	@Override
	public String getDocumentation(EObject object) {
		String returnString = "";
		if (object instanceof TypeDeclaration) {
			TypeDeclaration typeDeclaration = (TypeDeclaration) object;
			if (typeDeclaration.getComment() != null) {
				returnString = removeCommentMarkers(typeDeclaration.getComment());
			}
			if (typeDeclaration instanceof Usertype) {
				Usertype usertype = (Usertype) typeDeclaration;
				if (usertype.getRestrictions().size() > 0) {
					returnString = returnString + "</br>" + "<b>Restrictions: </b>";
				}
				boolean firstRestriction = true;
				for (Restriction restriction : usertype.getRestrictions()) {
					if (!firstRestriction) {
						returnString = returnString + ", ";
					} else {
						firstRestriction = false;
					}
					returnString = returnString + restriction.getRestrictionName();
				}

				if (usertype.getSupertypes().size() > 0) {
					returnString = returnString + "</br>" + "<b>Supertypes: </b>";
				}
				boolean firstSupertype = true;
				for (TypeDeclarationReference typeDeclarationReference : usertype.getSupertypes()) {
					if (!firstSupertype) {
						returnString = returnString + ", ";
					} else {
						firstSupertype = false;
					}
					returnString = returnString + typeDeclarationReference.getType().getName();
				}

			}
			return returnString;

		} else if (object instanceof Typedef) {
			Typedef typedef = (Typedef) object;
			if (typedef.getComment() != null) {
				returnString = removeCommentMarkers(typedef.getComment());
			}
			if (typedef instanceof Usertype) {
				Usertype usertype = (Usertype) typedef;
				if (usertype.getRestrictions() != null) {
					returnString = returnString + "</br>" + "<b>Restrictions: </b>";
				}
				boolean firstRestriction = true;
				for (Restriction restriction : usertype.getRestrictions()) {
					if (!firstRestriction) {
						returnString = returnString + " ";
					} else {
						firstRestriction = false;
					}
					returnString = returnString + restriction.getRestrictionName();
				}

			}
			return returnString;

		} else if (object instanceof Enumtype) {
			Enumtype enumtype = (Enumtype) object;
			if (enumtype.getComment() != null) {
				returnString = removeCommentMarkers(enumtype.getComment());
			}
		}
		return returnString;
	}

	/**
	 * Removes the comment markers from a comment
	 * 
	 * @param s
	 *            The comment
	 * @return The text of the comment
	 */
	private static String removeCommentMarkers(String commentToRemoveCommentsFrom) {
		String string = commentToRemoveCommentsFrom;
		string = string.substring(2);
		string = string.substring(0, string.length() - 2);
		for (int i = 0; i < string.length(); i++) {
			// Replace "*" with "\n"
			if (string.charAt(i) == '*') {
				if (i == 0)
					string = string.substring(1, string.length() - 1);
				else
					string = string.substring(0, i - 1) + "</br>" + string.substring(i + 1, string.length() - 1);
			}
		}
		return string + "</br>";
	}
}
