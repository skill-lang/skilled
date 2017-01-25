package de.unistuttgart.iste.ps.skilled.ui.views;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.unistuttgart.iste.ps.skilled.tools.SIRCache;
import de.unistuttgart.iste.ps.skilled.tools.SIRHelper;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolConfigurationDialog;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;

/**
 * Context menu for the toolview.
 * 
 * @author Timm Felden
 * 
 * @category GUI
 */
final class ToolViewContextMenu {
    private final ToolView view;
    private final Menu menu;

    // no instances
    ToolViewContextMenu(ToolView view, final List toollist) {
        this.view = view;
        menu = new Menu(toollist);

        {
            MenuItem item = new MenuItem(menu, SWT.NONE);
            item.setText("Build");
            item.addListener(SWT.Selection, this::build);
        }

        {
            MenuItem item = new MenuItem(menu, SWT.NONE);
            item.setText("Configure");
            item.addListener(SWT.Selection, this::configure);
        }

        {
            MenuItem item = new MenuItem(menu, SWT.NONE);
            item.setText("Clone Tool");
            item.addListener(SWT.Selection, this::clone);
        }

        {
            MenuItem item = new MenuItem(menu, SWT.NONE);
            item.setText("Delete");
            item.addListener(SWT.Selection, this::delete);
        }

        toollist.setMenu(menu);
    }

    void build(Event e) {
        SKilLToolWizard newWizard = new SKilLToolWizard(WizardOption.RENAME, view.getActiveTool().getName());
        WizardDialog wizardDialog = new WizardDialog(view.getShell(), newWizard);

        if (wizardDialog.open() == Window.OK)
            ToolUtil.renameTool(view.getActiveTool().getName(), newWizard.getToolNewName(), view.getActiveProject());

        view.refresh();
    }

    void configure(Event e) {
        ToolConfigurationDialog dialog = new ToolConfigurationDialog(view.getShell(), view.getActiveTool(),
                view.getSkillFile());

        if (dialog.open() == Window.OK) {
            view.refresh();
        }
    }

    void clone(Event e) {
        SKilLToolWizard newWizard = new SKilLToolWizard(WizardOption.CLONE,
                SIRHelper.getTools(view.getActiveProject()));
        WizardDialog wizardDialog = new WizardDialog(view.getShell(), newWizard);

        if (wizardDialog.open() == Window.OK)
            ToolUtil.cloneTool(view.getActiveProject(), view.getActiveTool(), newWizard.getToolNewName(),
                    view.getSkillFile());

        view.refresh();
    }

    void delete(Event e) {
        SIRCache.deleteTool(view.getActiveProject(), view.getActiveTool());

        view.setActiveTool(null);
        view.refresh();
    }
}
