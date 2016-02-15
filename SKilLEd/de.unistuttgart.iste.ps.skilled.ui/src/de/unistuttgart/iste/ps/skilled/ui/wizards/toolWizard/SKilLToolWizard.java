package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import de.unistuttgart.iste.ps.skillls.tools.Tool;


/**
 * creates the wizards used in SKilLEd
 * 
 * @@author Ken Singer
 *
 */
public class SKilLToolWizard extends Wizard {

    private WizardOption wizardOption;
    protected SKilLNewToolWizardPage pageNewTool;
    protected boolean addAllcheckboxState;
    protected SKilLCloneToolWizardPage cloneTool;
    protected SKilLRenameToolWizardPage pageRenameTool;
    private String name;
    private ArrayList<Tool> toolList;
    private String cloneToolName;

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

    public SKilLToolWizard(ArrayList<Tool> toolList) {
        super();
        this.toolList = toolList;
        this.wizardOption = WizardOption.CLONE;
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
                cloneTool = new SKilLCloneToolWizardPage(toolList);
                addPage(cloneTool);
                break;
        }
    }

    @Override
    public boolean performFinish() {
        switch (wizardOption) {
            case CREATE:
                this.name = pageNewTool.getTbNameText();
                this.addAllcheckboxState = pageNewTool.getAddAllCheckboxState();
                break;
            case RENAME:
                this.name = pageRenameTool.getTbNameText();
                break;
            case CLONE:
                this.name = cloneTool.getTbNameText();
                this.cloneToolName = cloneTool.getDropDownText();
                break;
        }
        return true;
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(getShell(), "Tool View", message);
    }

    public String getCloneToolName() {
        return this.cloneToolName;
    }

    public String getToolNewName() {
        return this.name;
    }

    public boolean getAddAllCheckboxState() {
        return this.addAllcheckboxState;
    }
}
