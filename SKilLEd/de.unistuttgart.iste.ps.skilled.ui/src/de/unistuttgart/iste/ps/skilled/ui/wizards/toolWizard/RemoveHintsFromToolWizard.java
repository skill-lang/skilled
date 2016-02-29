package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.unistuttgart.iste.ps.skillls.tools.Tool;


/**
 * creates a wizardpage for removing hints from a {@link Tool tool}.
 * 
 * @author Nico Rusam
 *
 */
public class RemoveHintsFromToolWizard extends WizardPage {

    private Tree toolTree;
    private TreeItem toolItem;
    private ArrayList<Tool> tools;
    private Composite container;

    public RemoveHintsFromToolWizard(ArrayList<Tool> tools) {
        super("Select the Tool(s) which should be freed from Hints.");
        this.tools = tools;
        setTitle("Select the Tool(s) which should be freed from Hints.");
        setDescription("On this page you can select the tool(s) which should be freed from Hints.");
    }

    @Override
    public void createControl(Composite arg0) {
        container = new Composite(arg0, SWT.BORDER);
        container.setLayout(new FillLayout());
        toolTree = new Tree(container, SWT.CHECK);

        for (Tool tool : tools) {
            toolItem = new TreeItem(toolTree, 0);
            toolItem.setText(tool.getName());
            toolItem.setData(tool);
        }
        setControl(container);
    }

    /**
     * returns a list containing the selected tools
     * 
     * @return a {@link ArrayList} containing the selected tools
     */
    ArrayList<Tool> getSelectedTools() {
        ArrayList<Tool> listOfSelectedTools = new ArrayList<>();
        for (TreeItem item : toolTree.getItems()) {
            if (item.getChecked())
                listOfSelectedTools.add((Tool) item.getData());
        }
        return listOfSelectedTools;
    }
}
