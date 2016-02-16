package de.unistuttgart.iste.ps.skilled.ui.views;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionBars;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;


public class ToolViewButtonInitializer {

    private Action createToolAction;
    private Action cloneToolAction;
    private Action removeHintAction;

    private ToolView toolview;

    public ToolViewButtonInitializer(ToolView toolview) {
        this.toolview = toolview;
    }

    /**
     * Actions for the buttons in the toolview.
     * 
     * @category GUI
     */
    public void makeActions() {
        createToolAction = new Action() {
            @Override
            public void run() {
                createToolDialog();
            }
        };
        createToolAction.setText("Create Tool");
        createToolAction.setToolTipText("opens a wizard to create a new tool");

        cloneToolAction = new Action() {
            @Override
            public void run() {
                cloneToolDialog();
            }
        };
        cloneToolAction.setText("Clone Tool");
        cloneToolAction.setToolTipText("opens a wizard to clone an existing tool.");

        removeHintAction = new Action() {
            @Override
            public void run() {
                removeHintsFromTools();
            }
        };
        removeHintAction.setText("Remove Hints");
        removeHintAction.setToolTipText("opens a wizard to remove hint from tools.");

    }

    /**
     * Creates a new tool.
     * 
     * @category Dialog
     */
    private void createToolDialog() {
        final SKilLToolWizard sKilLToolWizard = new SKilLToolWizard();
        WizardDialog wizardDialog = new WizardDialog(toolview.getShell(), sKilLToolWizard);
        if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
            String newToolName = sKilLToolWizard.getToolNewName();
            if (newToolName != null) {
                if (!ToolUtil.createTool(newToolName, toolview.getActiveProject()))
                    toolview.showMessage("Could not create tool.");
                toolview.readToolBinaryFile();
                if (sKilLToolWizard.getAddAllCheckboxState()) {
                    Tool tool = toolview.getSkillFile().Tools().stream().filter(t -> t.getName().equals(newToolName))
                            .findFirst().get();
                    ToolUtil.addAllToTool(toolview.getSkillFile(), toolview.getActiveProject(), tool);
                }
                toolview.refresh();
            }
        }
    }

    /**
     * opens the dialog to clone a existing tool
     */
    private void cloneToolDialog() {
        final SKilLToolWizard skillToolWizard = new SKilLToolWizard(WizardOption.CLONE, toolview.getAllToolList());
        WizardDialog wizardDialog = new WizardDialog(toolview.getShell(), skillToolWizard);
        if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
            String newToolName = skillToolWizard.getToolNewName();
            Tool cloneTool;
            try {
                cloneTool = toolview.getAllToolList().stream()
                        .filter(t -> t.getName().equals(skillToolWizard.getCloneToolName())).findFirst().get();
            } catch (NoSuchElementException e) {
                toolview.showMessage("Could not create tool.");
                return;
            }
            if (newToolName != null) {
                if (!ToolUtil.cloneTool(toolview.getActiveProject(), cloneTool, newToolName, toolview.getSkillFile())) {
                    toolview.showMessage("Could not create tool.");
                    return;
                }
                toolview.refresh();
            }
        }
    }

    /**
     * Open a wizard to select tools where hints should be deleted.
     * 
     * @category GUI
     */
    private void removeHintsFromTools() {
        final SKilLToolWizard skillToolWizard = new SKilLToolWizard(WizardOption.REMOVEHINTS, toolview.getAllToolList());
        WizardDialog wizardDialog = new WizardDialog(toolview.getShell(), skillToolWizard);
        if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
            ArrayList<Tool> removeHints = skillToolWizard.getRemoveHintsFromTools();
            for (Tool tempTool : removeHints) {
                System.out.println(tempTool.getName());
                for (Type tempType : tempTool.getTypes()) {
                    System.out.println(tempType.getName());
                    for (Field tempField : tempType.getFields()) {
                        System.out.println(tempField.getName());
                        if (tempField.getFieldHints().size() > 0)
                            System.out.println(ToolUtil.removeAllFieldHints(this.toolview.getActiveProject(), tempTool,
                                    tempType, tempField));
                    }
                    if (tempType.getTypeHints().size() > 0)
                        System.out
                                .println(ToolUtil.removeAllTypeHints(this.toolview.getActiveProject(), tempTool, tempType));
                }
            }
            toolview.refresh();
        }
    }

    /**
     * Add the buttons to the toolview.
     * 
     * @category GUI
     * @param manager
     */
    public void fillLocalToolBar() {
        IActionBars bars = toolview.getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        manager.add(createToolAction);
        manager.add(cloneToolAction);
        manager.add(removeHintAction);
    }

    public void initialize() {
        makeActions();
        fillLocalToolBar();
    }
}
