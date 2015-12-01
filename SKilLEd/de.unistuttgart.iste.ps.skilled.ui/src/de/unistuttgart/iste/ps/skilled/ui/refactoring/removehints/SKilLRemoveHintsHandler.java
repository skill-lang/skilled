package de.unistuttgart.iste.ps.skilled.ui.refactoring.removehints;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


/**
 * Activates SKIlLCombineRefactoringDialog from the menu
 * 
 * @author Leslie Tso
 *
 */
public class SKilLRemoveHintsHandler extends AbstractHandler {
    SKilLRemoveHints fDialog = new SKilLRemoveHints();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        fDialog.run();
        return null;
    }

}
