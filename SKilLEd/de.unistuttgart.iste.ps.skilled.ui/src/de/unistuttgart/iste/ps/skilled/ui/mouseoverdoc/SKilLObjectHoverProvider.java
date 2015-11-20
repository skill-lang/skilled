package de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.hover.html.DefaultEObjectHoverProvider;

public class SKilLObjectHoverProvider extends DefaultEObjectHoverProvider {
    @Override
    protected String getFirstLine(EObject o) {
        return o.toString();
    }
 
}