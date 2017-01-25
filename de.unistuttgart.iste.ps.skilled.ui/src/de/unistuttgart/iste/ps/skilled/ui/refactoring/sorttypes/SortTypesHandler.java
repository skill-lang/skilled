package de.unistuttgart.iste.ps.skilled.ui.refactoring.sorttypes;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


/**
 * Activates SortTypes-routine
 * 
 * @author Tobias Heck
 *
 */
public class SortTypesHandler extends AbstractHandler {
    SortTypes sorter = new SortTypes();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        sorter.run();
        return null;
    }
}
