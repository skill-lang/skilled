package de.unistuttgart.iste.ps.skilled.ui.tools.export;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


/**
 * Activates ExportToolsDialog from the menu
 * 
 * @author Leslie Tso
 *
 */
public class ExportToolsHandler extends AbstractHandler {
    ExportTools dialog = new ExportTools();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        dialog.run();
        return null;
    }

}
