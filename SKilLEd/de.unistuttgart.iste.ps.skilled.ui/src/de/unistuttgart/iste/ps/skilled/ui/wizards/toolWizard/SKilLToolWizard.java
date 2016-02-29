package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.Wizard;

import de.unistuttgart.iste.ps.skillls.tools.Tool;


/**
 * creates the wizards used in SKilLEd to handle {@link Tool tools}
 * 
 * @author Nico Rusam
 * @author Ken Singer
 *
 */
public class SKilLToolWizard extends Wizard {

    private WizardOption wizardOption;
    protected SKilLNewToolWizardPage pageNewTool;
    protected boolean addAllcheckboxState;
    protected SKilLCloneToolWizardPage cloneTool;
    protected SKilLRenameToolWizardPage pageRenameTool;
    protected RemoveHintsFromToolWizard removeHints;
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

    /**
     * Constructor which is used if the desired operation is to <b>clone</b> a tool or to <b>remove hints</b> from tools
     * 
     * @param option
     * @param toolList
     */
    public SKilLToolWizard(WizardOption option, ArrayList<Tool> toolList) {
        this();
        this.toolList = toolList;
        this.wizardOption = option;
    }

    @Override
    public String getWindowTitle() {
        return "SKilLEd Tool Wizard";
    }

    @Override
    public void addPages() {
        switch (wizardOption) {
            case CREATE:
                pageNewTool = new SKilLNewToolWizardPage(toolList);
                addPage(pageNewTool);
                break;
            case RENAME:
                pageRenameTool = new SKilLRenameToolWizardPage(toolList, this.name);
                addPage(pageRenameTool);
                break;
            case CLONE:
                cloneTool = new SKilLCloneToolWizardPage(toolList);
                addPage(cloneTool);
                break;
            case REMOVEHINTS:
                removeHints = new RemoveHintsFromToolWizard(toolList);
                addPage(removeHints);
                break;
            default:
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
            case REMOVEHINTS:
                this.toolList = removeHints.getSelectedTools();
                break;
            default:
                break;
        }
        return true;
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

    public ArrayList<Tool> getRemoveHintsFromTools() {
        return this.toolList;
    }
}
