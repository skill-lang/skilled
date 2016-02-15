package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skillls.tools.Tool;


/**
 * creates the wizardpage to clone a tool from an existing tool
 * 
 * @author Ken Singer
 *
 */
public class SKilLCloneToolWizardPage extends WizardPage {

    private Composite container;
    private Combo dropdown;
    private ArrayList<Tool> toolList;
    private Text tbName;

    public SKilLCloneToolWizardPage(ArrayList<Tool> toolList) {
        super("Clone an existing tool");
        setTitle("Clone an existing tool");
        setDescription("In this page you can select a tool and create a clone out of it.");
        this.toolList = toolList;
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        container.setLayout(layout);
        Label label1 = new Label(container, SWT.NONE);
        label1.setText("Select a tool to clone from.");

        dropdown = new Combo(container, SWT.DROP_DOWN);
        String[] toolnames = new String[toolList.size()];
        for (int i = 0; i < toolList.size(); i++) {
            toolnames[i] = toolList.get(i).getName();
        }
        dropdown.setItems(toolnames);
        Label label2 = new Label(container, SWT.NONE);
        label2.setText("Set the name for the new Tool");
        this.tbName = new Text(container, SWT.BORDER | SWT.SINGLE);
        this.tbName.setText("");
        this.tbName.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // nothing
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!tbName.getText().isEmpty())
                    setPageComplete(true);
            }
        });
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        tbName.setLayoutData(gd);
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);
    }

    public String getTbNameText() {
        return this.tbName.getText();
    }

    public String getDropDownText() {
        return this.dropdown.getText();
    }
}
