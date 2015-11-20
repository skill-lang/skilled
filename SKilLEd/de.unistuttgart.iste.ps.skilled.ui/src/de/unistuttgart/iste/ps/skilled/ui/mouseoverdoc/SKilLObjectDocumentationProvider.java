package de.unistuttgart.iste.ps.skilled.ui.mouseoverdoc;
 
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.documentation.IEObjectDocumentationProvider;

public class SKilLObjectDocumentationProvider implements IEObjectDocumentationProvider {
 
    @Override
    public String getDocumentation(EObject o) {
        return "Test2" + o.toString();
    }
 
}