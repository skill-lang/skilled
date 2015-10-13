package de.unistuttgart.iste.ps.skilled.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * 
 * @author Marco Link
 *
 */
public class SKilLNewProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage mainpage;

	public SKilLNewProjectWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	public void addPages() {
		mainpage = new WizardNewProjectCreationPage("basic new SKilL Project Page");
		mainpage.setDescription("Create a new fabulous SKilL Project!");
		mainpage.setTitle("SKilL Project Wizard");
		addPage(mainpage);
	}

	@Override
	public boolean performFinish() {
		boolean result = false;
		IProject project = mainpage.getProjectHandle();
		try {
			project.create(null);
			result = true;
		} catch (CoreException e) {
			e.printStackTrace();
		}

		IProjectDescription projectDesc = null;
		try {
			project.open(null);
			projectDesc = project.getDescription();
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
		String[] natureIds = projectDesc.getNatureIds();
		String[] newNatureIds = new String[natureIds.length + 1];
		System.arraycopy(natureIds, 0, newNatureIds, 0, natureIds.length);
		newNatureIds[natureIds.length] = "org.eclipse.xtext.ui.shared.xtextNature";
		projectDesc.setNatureIds(newNatureIds);
		try {
			project.setDescription(projectDesc, null);
		} catch (CoreException e2) {
			e2.printStackTrace();
		}

		return result;
	}
}
