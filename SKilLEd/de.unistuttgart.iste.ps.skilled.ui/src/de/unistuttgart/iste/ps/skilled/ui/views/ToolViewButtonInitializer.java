package de.unistuttgart.iste.ps.skilled.ui.views;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionBars;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;
import de.unistuttgart.iste.ps.skillls.tools.Field;
import de.unistuttgart.iste.ps.skillls.tools.Tool;
import de.unistuttgart.iste.ps.skillls.tools.Type;


/**
 * this class is used to initialize the buttons displayed in the {@link ToolView toolview}
 * 
 * @author Ken Singer
 * @category GUI
 */
public class ToolViewButtonInitializer {

    private Action createToolAction;
    private Action cloneToolAction;
    private Action removeHintAction;

    private ToolView toolview;

    public ToolViewButtonInitializer(ToolView toolview) {
        this.toolview = toolview;
    }

    /**
     * Actions for the buttons in the {@link ToolView toolview}.
     * 
     * @category GUI
     */
    private void makeActions() {
        // Action for create tool button
        createToolAction = new Action() {
            @Override
            public void run() {
                createToolDialog();
            }
        };
        createToolAction.setText("Create Tool");
        createToolAction.setToolTipText("opens a wizard to create a new tool");
        // Action for clone tool button
        cloneToolAction = new Action() {
            @Override
            public void run() {
                cloneToolDialog();
            }
        };
        cloneToolAction.setText("Clone Tool");
        cloneToolAction.setToolTipText("opens a wizard to clone an existing tool.");
        // Action for remove hints in tools button
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
     * Creates a new {@link Tool tool}.
     * 
     * @category Dialog
     */
    private void createToolDialog() {
        final SKilLToolWizard sKilLToolWizard = new SKilLToolWizard(WizardOption.CREATE, toolview.getAllToolList());
        WizardDialog wizardDialog = new WizardDialog(toolview.getShell(), sKilLToolWizard);
        if (wizardDialog.open() == Window.OK) {
            String newToolName = sKilLToolWizard.getToolNewName();
            if (newToolName == null)
                return;
            if (!ToolUtil.createTool(newToolName, toolview.getActiveProject()))
                toolview.showMessage("Could not create tool.");

            toolview.readToolBinaryFile();
            if (sKilLToolWizard.getAddAllCheckboxState()) {
                try {
                    Tool tool = toolview.getSkillFile().Tools().stream().filter(t -> t.getName().equals(newToolName))
                            .findFirst().get();
                    ToolUtil.addAllToTool(toolview.getSkillFile(), toolview.getActiveProject(), tool);
                } catch (NoSuchElementException e) {
                    return;
                }
            }
            toolview.refresh();
        }
    }

    /**
     * opens the dialog to clone a existing {@link Tool tool}
     * 
     * @category Dialog
     */
    private void cloneToolDialog() {
        final SKilLToolWizard skillToolWizard = new SKilLToolWizard(WizardOption.CLONE, toolview.getAllToolList());
        WizardDialog wizardDialog = new WizardDialog(toolview.getShell(), skillToolWizard);
        if (wizardDialog.open() == Window.OK) {
            String newToolName = skillToolWizard.getToolNewName();
            Tool cloneTool;
            try {
                cloneTool = toolview.getAllToolList().stream()
                        .filter(t -> t.getName().equals(skillToolWizard.getCloneToolName())).findFirst().get();
            } catch (NoSuchElementException e) {
                toolview.showMessage("Could not create tool.");
                return;
            }
            if (newToolName == null)
                return;
            if (!ToolUtil.cloneTool(toolview.getActiveProject(), cloneTool, newToolName, toolview.getSkillFile())) {
                toolview.showMessage("Could not create tool.");
                return;
            }
            toolview.refresh();
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
        if (wizardDialog.open() == Window.OK) {
            ArrayList<Tool> removeHints = skillToolWizard.getRemoveHintsFromTools();
            for (Tool tempTool : removeHints) {
                for (Type tempType : tempTool.getTypes()) {
                    for (Field tempField : tempType.getFields()) {
                        if (tempField.getHints().size() > 0)
                            ToolUtil.removeAllFieldHints(this.toolview.getActiveProject(), tempTool, tempType, tempField);
                    }
                    if (tempType.getHints().size() > 0)
                        ToolUtil.removeAllTypeHints(this.toolview.getActiveProject(), tempTool, tempType);
                }
            }
            toolview.refresh();
        }
    }

    /**
     * Add the buttons to the {@link ToolView}.
     * 
     * @category GUI
     */
    private void fillLocalToolBar() {
        IActionBars bars = toolview.getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        manager.add(createToolAction);
        manager.add(cloneToolAction);
        manager.add(removeHintAction);
    }

    /**
     * initializes the buttons
     */
    public void initialize() {
        makeActions();
        fillLocalToolBar();
    }
}
