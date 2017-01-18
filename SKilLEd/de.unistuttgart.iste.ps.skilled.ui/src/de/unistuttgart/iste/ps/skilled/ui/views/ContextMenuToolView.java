package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.unistuttgart.iste.ps.skilled.tools.SIRHelper;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;

/**
 * Extracted class to create the context menu for the toolview.
 * 
 * @category GUI
 * @author Nico Rusam
 * @author Ken Singer
 */
public class ContextMenuToolView {

    private final ToolView toolView;

    public ContextMenuToolView(ToolView toolView) {
        this.toolView = toolView;
    }

    /**
     * 
     * @param toollist
     *            - a {@link List list} containing the tools
     * @param menu
     *            - {@link Menu}
     */
    void initContextMenu(List toollist, final Menu menu) {

        toollist.setMenu(menu);
        menu.addMenuListener(new MenuAdapter() {
            @Override
            public void menuShown(MenuEvent e) {
                int selected = toollist.getSelectionIndex();

                if (selected < 0 || selected >= toollist.getItemCount())
                    return;

                for (MenuItem mI : menu.getItems())
                    mI.dispose();

                // Create contextmenu for 'Clone Tool'.
                MenuItem cloneToolItem = new MenuItem(menu, SWT.NONE);
                cloneToolItem.setText("Clone Tool");
                cloneToolItem.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        SKilLToolWizard newWizard = new SKilLToolWizard(WizardOption.CLONE,
                                SIRHelper.getTools(toolView.getActiveProject()));
                        WizardDialog wizardDialog = new WizardDialog(toolView.getShell(), newWizard);

                        if (wizardDialog.open() == Window.OK)
                            ToolUtil.cloneTool(toolView.getActiveProject(), toolView.getActiveTool(),
                                    newWizard.getToolNewName(), toolView.getSkillFile());

                        toolView.refresh();
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });

                // Create contextmenu for 'Delete Tool'.
                // Delete is currently not implemented in skill (08.10.2015).
                MenuItem deleteToolItem = new MenuItem(menu, SWT.NONE);
                deleteToolItem.setText("Delete Tool");
                deleteToolItem.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        ToolUtil.cleanUpAfterDeletion(toolView.getActiveProject(), toolView.getActiveTool());
                        ToolUtil.removeTool(toolView.getActiveProject(), toolView.getActiveTool().getName());
                        File toDelete = new File(toolView.getActiveProject().getLocationURI().getPath().toString()
                                + File.separator + ".skillt" + File.separator + toolView.getActiveTool().getName());
                        toolView.deleteDirectoryRecursivly(toDelete);
                        toolView.refresh();
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });

                // Create contextmenu for 'Rename Tool'.
                MenuItem renameToolItem = new MenuItem(menu, SWT.NONE);
                renameToolItem.setText("Rename Tool");
                renameToolItem.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        SKilLToolWizard newWizard = new SKilLToolWizard(WizardOption.RENAME,
                                toollist.getItem(selected));
                        WizardDialog wizardDialog = new WizardDialog(toolView.getShell(), newWizard);

                        if (wizardDialog.open() == Window.OK)
                            ToolUtil.renameTool(toolView.getActiveTool().getName(), newWizard.getToolNewName(),
                                    toolView.getActiveProject());

                        toolView.refresh();
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent arg0) {
                        // no default
                    }
                });
            }
        });
    }
}
