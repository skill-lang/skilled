package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


/**
 * creates a wizardpage for creating a new tool
 * 
 * @author Nico Rusam
 * @author Ken Singer
 *
 */
public class SKilLNewToolWizardPage extends WizardPage {

    private Text tbName;
    private Composite container;
    private Button checkbox;

    public SKilLNewToolWizardPage() {
        super("Create a new tool");
        setTitle("Create a new tool");
        setDescription("In this page you can insert a name for the new tool.");
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        container.setLayout(layout);
        Label label1 = new Label(container, SWT.NONE);
        label1.setText("Put a value here.");

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
        checkbox = new Button(container, SWT.CHECK);
        checkbox.setSelection(false);
        checkbox.setText("Add all Types to Tool:");
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        tbName.setLayoutData(gd);
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);
    }

    public String getTbNameText() {
        return this.tbName.getText();
    }

    public boolean getAddAllCheckboxState() {
        return checkbox.getSelection();
    }
}
