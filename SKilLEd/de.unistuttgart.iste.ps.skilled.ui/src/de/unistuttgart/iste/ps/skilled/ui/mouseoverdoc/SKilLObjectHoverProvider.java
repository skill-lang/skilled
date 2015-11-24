package de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype;
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype;
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration;
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef;
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype;


/**
 * This Class defines the HoverProvider for mouseover documentation in SKilL and creates the title string for the
 * documentation
 * 
 * @author Jan Berberich
 */
public class SKilLObjectHoverProvider extends DefaultEObjectHoverProvider {
    @Override
    protected String getFirstLine(EObject o) {
        String comment = "";
        if (o instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) o;
            if (o instanceof Usertype) {
                comment = comment + "Type ";
            }
            if (o instanceof Interfacetype) {
                comment = comment + "Interface ";
            }
            comment = comment + td.getName();
        } else if (o instanceof Typedef) {
            Typedef td = (Typedef) o;
            comment = comment + "Typedef: " + td.getName();
        } else if (o instanceof Enumtype) {
            Enumtype en = (Enumtype) o;
            comment = comment + "Enum: " + en.getName();
            return comment;
        }
        return comment;
    }

}
