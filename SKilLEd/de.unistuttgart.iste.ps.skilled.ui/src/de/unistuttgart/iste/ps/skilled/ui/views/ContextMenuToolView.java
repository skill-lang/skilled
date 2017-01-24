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

import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import de.unistuttgart.iste.ps.skilled.tools.SIRCache;
import de.unistuttgart.iste.ps.skilled.tools.SIRHelper;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolConfigurationDialog;
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.SKilLToolWizard;
import de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard.WizardOption;

/**
 * Extracted class to create the context menu for the toolview.
 * 
 * @category GUI
 * @author Nico Rusam
 * @author Ken Singer
 * @author Timm Felden
 */
final class ContextMenuToolView {

    // no instances
    private ContextMenuToolView() {
    }

    /**
     * 
     * @param toollist
     *            - a {@link List list} containing the tools
     * @param menu
     *            - {@link Menu}
     */
    static void make(ToolView toolView, List toollist, final Menu menu) {

        toollist.setMenu(menu);
        menu.addMenuListener(new MenuAdapter() {
            @Override
            public void menuShown(MenuEvent e) {
                int selected = toollist.getSelectionIndex();

                if (selected < 0 || selected >= toollist.getItemCount())
                    return;

                for (MenuItem mI : menu.getItems())
                    mI.dispose();

                // Create contextmenu for 'Build'.
                {
                    MenuItem item = new MenuItem(menu, SWT.NONE);
                    item.setText("Build");
                    item.addSelectionListener(new SelectionListener() {
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

                // Create contextmenu for 'configure'.
                {
                    MenuItem item = new MenuItem(menu, SWT.NONE);
                    item.setText("Configure");
                    item.addSelectionListener(new SelectionListener() {
                        @Override
                        public void widgetSelected(SelectionEvent e) {

                            ToolConfigurationDialog dialog = new ToolConfigurationDialog(toolView.getShell(),
                                    toolView.getActiveTool(), toolView.getSkillFile());

                            if (dialog.open() == Window.OK) {
                                SIRCache.ensureFile(toolView.getActiveProject()).flush();

                                toolView.refresh();
                            }

                        }

                        @Override
                        public void widgetDefaultSelected(SelectionEvent arg0) {
                            // no default
                        }
                    });
                }

                // Create contextmenu for 'Clone Tool'.
                {
                    MenuItem item = new MenuItem(menu, SWT.NONE);
                    item.setText("Clone Tool");
                    item.addSelectionListener(new SelectionListener() {
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
                }

                // Create contextmenu for 'Delete Tool'.
                // Delete is currently not implemented in skill (08.10.2015).
                {
                    MenuItem item = new MenuItem(menu, SWT.NONE);
                    item.setText("Delete");
                    item.addSelectionListener(new SelectionListener() {
                        @Override
                        public void widgetSelected(SelectionEvent arg0) {
                            SIRCache.deleteTool(toolView.getActiveProject(), toolView.getActiveTool());

                            toolView.setActiveTool(null);
                            toolView.refresh();
                        }

                        @Override
                        public void widgetDefaultSelected(SelectionEvent arg0) {
                            // no default
                        }
                    });
                }
            }
        });
    }
}
