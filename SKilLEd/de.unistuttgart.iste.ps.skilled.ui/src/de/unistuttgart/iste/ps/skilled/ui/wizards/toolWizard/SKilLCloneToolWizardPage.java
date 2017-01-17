package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.internal.ToolAccess;

/**
 * creates the wizardpage to clone a tool from an existing {@link Tool tool}
 * 
 * @author Ken Singer
 *
 */
public class SKilLCloneToolWizardPage extends WizardPage {

    private Composite container;
    private CCombo dropdown;
    private ToolAccess tools;
    private Text tbName;

    public SKilLCloneToolWizardPage(ToolAccess toolList) {
        super("Clone an existing tool");
        setTitle("Clone an existing tool");
        setDescription("On this page you can select a tool and create a clone out of it");
        this.tools = toolList;
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        layout.numColumns = 2;
        container.setLayout(layout);
        Label label1 = new Label(container, SWT.NONE);
        label1.setText("Select a tool to clone from");

        dropdown = new CCombo(container, SWT.DROP_DOWN | SWT.H_SCROLL | SWT.READ_ONLY);
        String[] toolnames = new String[tools.size()];
        {
            int i = 0;
            for (Tool t : tools)
                toolnames[i++] = t.getName();
        }
        dropdown.setItems(toolnames);
        dropdown.setLayoutData(gd);
        Label label2 = new Label(container, SWT.NONE);
        label2.setText("Set the name for the new Tool");
        this.tbName = new Text(container, SWT.BORDER | SWT.SINGLE);
        this.tbName.setText("");
        this.tbName.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // not used
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!tbName.getText().isEmpty())
                    setPageComplete(true);
            }
        });
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
