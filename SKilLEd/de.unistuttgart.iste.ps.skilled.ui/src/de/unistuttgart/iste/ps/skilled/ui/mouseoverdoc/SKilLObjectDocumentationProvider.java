package de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;

import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Restriction;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;


/**
 * This Class defines the Object Documentation Provider for mouseover documentation in SKilL and creates the object
 * documentation string for the documentation
 * 
 * @author Jan Berberich
 */
public class SKilLObjectDocumentationProvider implements IEObjectDocumentationProvider {

    @Override
    public String getDocumentation(EObject o) {
        String returnString = "";
        if (o instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) o;
            if (td.getComment() != null) {
                returnString = removeCommentMarkers(td.getComment());
            }
            if (td instanceof Usertype) {
                Usertype ut = (Usertype) td;
                if (ut.getRestrictions() != null) {
                    returnString = returnString + "\n" + "<b>Restrictions: </b>";
                }
                boolean firstRestriction = true;
                for (Restriction r : ut.getRestrictions()) {
                    if (!firstRestriction) {
                        returnString = returnString + " ";
                    } else {
                        firstRestriction = false;
                    }
                    returnString = returnString + r.getRestrictionName();
                }

            }
            return returnString;
        } else if (o instanceof Typedef) {
            Typedef td = (Typedef) o;
            if (td.getComment() != null) {
                returnString = removeCommentMarkers(td.getComment());
            }
            if (td instanceof Usertype) {
                Usertype ut = (Usertype) td;
                if (ut.getRestrictions() != null) {
                    returnString = returnString + "\n" + "<b>Restrictions: </b>";
                }
                boolean firstRestriction = true;
                for (Restriction r : ut.getRestrictions()) {
                    if (!firstRestriction) {
                        returnString = returnString + " ";
                    } else {
                        firstRestriction = false;
                    }
                    returnString = returnString + r.getRestrictionName();
                }

            }
            return returnString;
        } else if (o instanceof Enumtype) {
            Enumtype en = (Enumtype) o;
            if (en.getComment() != null) {
                returnString = removeCommentMarkers(en.getComment());
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
    private String removeCommentMarkers(String s) {
        s = s.substring(2);
        s = s.substring(0, s.length() - 2);
        for (int i = 0; i < s.length(); i++) {
            // Replace "*" with "\n"
            if (s.substring(i, i + 1).equals("*")) {
                s = s.substring(0, i) + "" + s.substring(i + 1, s.length());
            }
        }
        return s;
    }
}
