package de.unistuttgart.iste.ps.skilled.ui.refactoring.removeprojecthints;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


/**
 * Activates SKIlLCombineRefactoringDialog from the menu
 * 
 * @author Leslie Tso
 * @author Tobias Heck
 *
 */
public class SKilLRemoveProjectHintsHandler extends AbstractHandler {
    SKilLRemoveProjectHints fDialog = new SKilLRemoveProjectHints();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        fDialog.runFromMenu();
        return null;
    }

}
