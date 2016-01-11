package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;


public class SKilLToolWizard extends Wizard {

    private WizardOption wizardOption;
    protected SKilLNewToolWizardPage pageNewTool;
    protected SKilLRenameToolWizardPage pageRenameTool;
    private String name;

    /**
     * Default Wizard with create new tool option.
     */
    public SKilLToolWizard() {
        super();
        setNeedsProgressMonitor(true);
        this.wizardOption = WizardOption.CREATE;
    }

    public SKilLToolWizard(WizardOption option, String name) {
        this();
        this.wizardOption = option;
        this.name = name;
    }

    @Override
    public String getWindowTitle() {
        return "SKilLEd Tool Wizard";
    }

    @Override
    public void addPages() {
        switch (wizardOption) {
            case CREATE:
                pageNewTool = new SKilLNewToolWizardPage();
                addPage(pageNewTool);
                break;
            case RENAME:
                pageRenameTool = new SKilLRenameToolWizardPage(this.name);
                addPage(pageRenameTool);
                break;
            case CLONE:
                // TODO: Clone Tool
                break;
        }
    }

    @Override
    public boolean performFinish() {
        switch (wizardOption) {
            case CREATE:
                this.name = pageNewTool.getTbNameText();
                IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getActivePage().getActiveEditor().getEditorInput();
                IProject project = file.getFile().getProject();
                if (!ToolUtil.createTool(name, project)) {
                    showMessage("EVERYTHING BROKEN AAAAAAAAAAAAAAAAAAAA!!!!!!!!!!!A");
                }
                return true;
            case RENAME:
                this.name = pageRenameTool.getTbNameText();
                break;
            case CLONE:
                // TODO: Clone Tool
                break;
        }
        return true;
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(getShell(), "Tool View", message);
    }

    public String getToolNewName() {
        return this.name;
    }
}
