package de.unistuttgart.iste.ps.skilled.ui.wizards;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;


/**
 * 
 * This is a new wizard. Its role is to create a new SKilL Project. It will also add the required nature, for editing skill
 * files, to the project.
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

    /**
     * Adding the page to the wizard.
     */
    @Override
    public void addPages() {
        mainpage = new WizardNewProjectCreationPage("basic new SKilL Project Page");
        mainpage.setDescription("Create a new SKilL Project!");
        mainpage.setTitle("SKilL Project Wizard");
        addPage(mainpage);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We will create a new project with the given
     * informations and add the nature.
     * 
     * @return true if everything went fine - else false.
     */
    @Override
    public boolean performFinish() {
        IProgressMonitor progressMonitor = new NullProgressMonitor();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject project = null;
        IProjectDescription projectDesc = null;
        // If default path is not set change to the inserted location.
        if (!mainpage.useDefaults()) {
            project = root.getProject(mainpage.getProjectName());
            IWorkspace w = ResourcesPlugin.getWorkspace();
            projectDesc = w.newProjectDescription(project.getName());
            IPath projectLocation = mainpage.getLocationPath().append(mainpage.getProjectName());
            projectDesc.setLocation(projectLocation);
            try {
                project.create(projectDesc, progressMonitor);
            } catch (CoreException e) {
                e.printStackTrace();
                return false;
            }

        } else {
            project = mainpage.getProjectHandle();
            try {
                project.create(progressMonitor);
            } catch (CoreException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            project.open(null);

            // Create new .skills file for the tool support.
            String toolsFileName = project.getFullPath().toString() + "/.skillls";
            IFile toolsFile = root.getFile(new Path(toolsFileName));
            if (!toolsFile.exists()) {
                toolsFile.create(new ByteArrayInputStream(new byte[0]), false, null);
            }

            projectDesc = project.getDescription();

            String[] natureIds = projectDesc.getNatureIds();
            String[] newNatureIds = new String[natureIds.length + 1];
            System.arraycopy(natureIds, 0, newNatureIds, 0, natureIds.length);
            newNatureIds[natureIds.length] = "org.eclipse.xtext.ui.shared.xtextNature";
            projectDesc.setNatureIds(newNatureIds);
            try {
                project.setDescription(projectDesc, null);
            } catch (CoreException e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (CoreException e1) {
            e1.printStackTrace();
            return false;
        }

        return true;
    }
}
