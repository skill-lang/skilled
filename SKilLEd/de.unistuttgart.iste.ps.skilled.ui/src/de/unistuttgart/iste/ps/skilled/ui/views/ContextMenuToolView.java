package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.File;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;


/**
 * Extracted class to create the context menu for the toolview.
 * 
 * @category GUI
 * @author Nico Rusam
 *
 */
public class ContextMenuToolView {

    ToolView toolView = null;

    public ContextMenuToolView(ToolView toolView) {
        this.toolView = toolView;
    }

    public void initContextMenu(List toollist, final Menu menu) {

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
                        ToolUtil.cloneTool(toolView.getActiveProject(), toolView.getActiveTool(), toolView.getSkillFile());
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
                        ToolView.deleteDirectoryRecursivly(toDelete);
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
                        SKilLToolWizard newWizard = new SKilLToolWizard(WizardOption.RENAME, toollist.getItem(selected));
                        WizardDialog wizardDialog = new WizardDialog(toolView.getShell(), newWizard);

                        if (wizardDialog.open() == org.eclipse.jface.window.Window.OK) {
                            System.out.println("Ok pressed");
                            ToolUtil.renameTool(toolView.getActiveTool().getName(), newWizard.getToolNewName(),
                                    toolView.getActiveProject());
                            toolView.refresh();

                        } else
                            System.out.println("Cancel pressed");
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
