package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * The handler which is needed to add the {@link ToolView toolview} to the SKilLEd menu
 * 
 * @author Ken Singer
 * @category GUI
 */
public class ToolViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage()
                    .showView("de.unistuttgart.iste.ps.skilled.ui.views.ToolView");
        } catch (PartInitException e) {
            return null;
        }
        return null;
    }
}
