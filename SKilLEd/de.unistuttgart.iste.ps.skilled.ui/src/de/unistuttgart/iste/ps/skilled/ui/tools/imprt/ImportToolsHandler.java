package de.unistuttgart.iste.ps.skilled.ui.tools.imprt;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


/**
 * Activates ExportToolsDialog from the menu
 * 
 * @author Leslie Tso
 *
 */
public class ImportToolsHandler extends AbstractHandler {
    ImportTools fDialog = new ImportTools();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        fDialog.run();
        return null;
    }

}
