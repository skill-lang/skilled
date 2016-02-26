package de.unistuttgart.iste.ps.skilled.ui.refactoring.extractspecification;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Activates ExtractSpecification routine
 * 
 * @author Tobias Heck
 *
 */
public class ExtractSpecificationHandler extends AbstractHandler {
    ExtractSpecificationDialog dialog = new ExtractSpecificationDialog();
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        dialog.run();
        return null;
    }
}
