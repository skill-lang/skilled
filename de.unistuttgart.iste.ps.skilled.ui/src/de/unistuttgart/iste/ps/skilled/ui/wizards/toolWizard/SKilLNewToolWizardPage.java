package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skilled.sir.Tool;

/**
 * creates a wizardpage for creating a new {@link Tool tool}
 * 
 * @author Nico Rusam
 * @author Ken Singer
 * @author Timm Felden
 *
 */
public class SKilLNewToolWizardPage extends WizardPage {

    private Text tbName;
    private Composite container;
    private Button checkbox;
    private Iterable<Tool> tools;

    public SKilLNewToolWizardPage(Iterable<Tool> allToolList) {
        super("Create a new tool");
        setTitle("Create a new tool");
        setDescription("Add a new tool to your project.");
        this.tools = allToolList;
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        container.setLayout(layout);
        Label label1 = new Label(container, SWT.NONE);
        label1.setText("Specify a name:");

        this.tbName = new Text(container, SWT.BORDER | SWT.SINGLE);
        this.tbName.setText("");
        this.tbName.addModifyListener(e -> {
            if (tbName.getText().isEmpty()) {
                setPageComplete(false);
            } else {
                final String name = tbName.getText().toLowerCase();
                for (Tool t : tools) {
                    if (t.getName().toLowerCase().equals(name)) {
                        setPageComplete(false);
                        return;
                    }
                }
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
