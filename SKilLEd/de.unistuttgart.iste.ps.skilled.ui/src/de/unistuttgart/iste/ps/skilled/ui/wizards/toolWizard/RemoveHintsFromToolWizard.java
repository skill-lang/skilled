package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.unistuttgart.iste.ps.skillls.tools.Tool;


public class RemoveHintsFromToolWizard extends WizardPage {

    private Tree toolTree;
    private TreeItem toolItem;
    private ArrayList<Tool> tools;
    private Composite container;

    public RemoveHintsFromToolWizard(ArrayList<Tool> tools) {
        super("Select Tool(s) where Hints should be deleted.");
        this.tools = tools;
        setTitle("Select Tool(s) where Hints should be deleted.");
        setDescription("In this page you can select the tool(s) where the Hints should be deleted.");
    }

    @Override
    public void createControl(Composite arg0) {
        container = new Composite(arg0, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        toolTree = new Tree(container, SWT.MULTI | SWT.CHECK | SWT.SINGLE);

        for (Tool tool : tools) {
            toolItem = new TreeItem(toolTree, 0);
            toolItem.setText(tool.getName());
        }

        setControl(container);
        setPageComplete(false);
    }
}
