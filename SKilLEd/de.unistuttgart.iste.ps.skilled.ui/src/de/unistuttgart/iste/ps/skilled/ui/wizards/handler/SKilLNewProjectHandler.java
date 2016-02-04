package de.unistuttgart.iste.ps.skilled.ui.wizards.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import de.unistuttgart.iste.ps.skilled.ui.wizards.SKilLNewProjectWizard;

/**
 * Small Handler for open a new SKilL Project Wizard. It is needed for making a
 * contribution to the right click of the project explorer.
 * 
 * @author Marco Link
 */
public class SKilLNewProjectHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// Not needed

	}

	@Override
	public void dispose() {
		// Not needed

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell activeShell = HandlerUtil.getActiveShell(event);

		IWizard wizard = new SKilLNewProjectWizard();

		WizardDialog dialog = new WizardDialog(activeShell, wizard);

		dialog.open();
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// Not needed
	}

}
