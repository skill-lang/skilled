package de.unistuttgart.iste.ps.skilled.ui.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import de.unistuttgart.iste.ps.skilled.sir.BuildInformation;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
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
        StringBuilder msg = new StringBuilder("The tool ");
        // build selected tool
        final Tool tool = view.getActiveTool();
        StringBuilder output = new StringBuilder();
        try {
            File jarLocation = new File(
                    FileLocator.resolve(FileLocator.find(Platform.getBundle("de.unistuttgart.iste.ps.skilled"),
                            new Path("lib"), Collections.emptyMap())).toURI());
            File projectLocation = new File(view.getActiveProject().getLocationURI());

            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "skill-0.3.jar",
                    projectLocation.getAbsolutePath() + "/.sir", "-t", tool.getName(), "-b");

            pb.directory(jarLocation);
            Process process = pb.start();
            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append(System.getProperty("line.separator"));
            }
        } catch (IOException | InterruptedException | URISyntaxException e1) {
            e1.printStackTrace();
            return;
        }

        // create message
        msg.append(tool.getName()).append(" was built for these targets:");
        for (BuildInformation build : tool.getBuildTargets()) {
            msg.append("\n  ").append(build.getLanguage()).append(": ");
            for (String s : build.getOutput().getParts()) {
                msg.append(s).append('/');
            }
        }

        if (output.length() > 0) {
            msg.append("\n\noutput:\n\n").append(output.toString());
        }

        // show success as some form of feedback
        MessageDialog.open(MessageDialog.INFORMATION, view.getShell(), "Build Tool", msg.toString(), 0);
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
