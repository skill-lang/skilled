package de.unistuttgart.iste.ps.skilled.ui.views;

import java.util.NoSuchElementException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IActionBars;

import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.tools.SIRHelper;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;

/**
 * this class is used to initialize the buttons displayed in the {@link ToolView
 * toolview}
 * 
 * @author Ken Singer
 * @author Marco Link
 * @category GUI
 */
public class ToolViewButtonInitializer {

    private Action createToolAction;
    private Action cloneToolAction;
    private Action removeHintAction;

    private ToolView toolView;

    public ToolViewButtonInitializer(ToolView toolview) {
        this.toolView = toolview;
    }

    /**
     * make the Actions for the buttons in the {@link ToolView toolview}.
     * 
     * @category GUI
     */
    private void makeActions() {
        // Action for create tool button
        createToolAction = new Action() {
            @Override
            public void run() {
                runCreateToolDialog();
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
    private void runCreateToolDialog() {
        final SKilLToolWizard sKilLToolWizard = new SKilLToolWizard(WizardOption.CREATE,
                SIRHelper.getTools(toolView.getActiveProject()));
        WizardDialog wizardDialog = new WizardDialog(toolView.getShell(), sKilLToolWizard);
        if (wizardDialog.open() == Window.OK) {
            String newToolName = sKilLToolWizard.getToolNewName();
            if (newToolName == null)
                return;

            Tool t = ToolUtil.createNewTool(toolView.getActiveProject(), newToolName,
                    sKilLToolWizard.getAddAllCheckboxState());

            toolView.refresh();
            toolView.setActiveTool(t);
        }
    }

    /**
     * opens the dialog to clone a existing {@link Tool tool}
     * 
     * @category Dialog
     */
    private void cloneToolDialog() {
        final SKilLToolWizard skillToolWizard = new SKilLToolWizard(WizardOption.CLONE,
                toolView.getSkillFile().Tools());
        WizardDialog wizardDialog = new WizardDialog(toolView.getShell(), skillToolWizard);
        if (wizardDialog.open() == Window.OK) {
            String newToolName = skillToolWizard.getToolNewName();
            Tool cloneTool;
            try {
                cloneTool = toolView.getSkillFile().Tools().stream()
                        .filter(t -> t.getName().equals(skillToolWizard.getCloneToolName())).findFirst().get();
            } catch (NoSuchElementException e) {
                toolView.showMessage("Could not create tool.");
                return;
            }
            if (newToolName == null)
                return;
            if (!ToolUtil.cloneTool(toolView.getActiveProject(), cloneTool, newToolName, toolView.getSkillFile())) {
                toolView.showMessage("Could not create tool.");
                return;
            }
            toolView.refresh();
        }
    }

    /**
     * Open a wizard to select tools where hints should be deleted.
     * 
     * @category GUI
     */
    private void removeHintsFromTools() {
        throw new NoSuchMethodError();
        // final SKilLToolWizard skillToolWizard = new
        // SKilLToolWizard(WizardOption.REMOVEHINTS,
        // toolview.getSkillFile().Tools());
        // WizardDialog wizardDialog = new WizardDialog(toolview.getShell(),
        // skillToolWizard);
        // if (wizardDialog.open() == Window.OK) {
        // ArrayList<Tool> removeHints =
        // skillToolWizard.getRemoveHintsFromTools();
        // for (Tool tempTool : removeHints) {
        // for (Type tempType : tempTool.getTypes()) {
        // for (Field tempField : tempType.getFields()) {
        // if (tempField.getHints().size() > 0)
        // ToolUtil.removeAllFieldHints(this.toolview.getActiveProject(),
        // tempTool, tempType,
        // tempField);
        // }
        // if (tempType.getHints().size() > 0)
        // ToolUtil.removeAllTypeHints(this.toolview.getActiveProject(),
        // tempTool, tempType);
        // }
        // }
        // toolview.refresh();
        // }
    }

    /**
     * Add the buttons to the {@link ToolView}.
     * 
     * @category GUI
     */
    private void fillLocalToolBar() {
        IActionBars bars = toolView.getViewSite().getActionBars();
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
