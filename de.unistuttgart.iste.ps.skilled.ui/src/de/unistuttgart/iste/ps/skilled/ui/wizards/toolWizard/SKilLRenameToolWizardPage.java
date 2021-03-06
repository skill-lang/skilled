package de.unistuttgart.iste.ps.skilled.ui.wizards.toolWizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skilled.sir.Tool;



/**
 * creates a wizardpage for renaming a {@link Tool tool}
 * 
 * @author Nico Rusam
 * @author Ken Singer
 *
 */
public class SKilLRenameToolWizardPage extends WizardPage {

    private Text tbName;
    private Composite container;
    private Iterable<Tool> tools;

    private String name;

    public SKilLRenameToolWizardPage(Iterable<Tool> toollist, String name) {
        super("Rename Tool");
        setTitle("Rename Tool");
        setDescription("On this page you can rename the selected tool.");
        setControl(tbName);
        this.name = name;
        this.tools = toollist;
    }

    @Override
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        Label label1 = new Label(container, SWT.NONE);
        label1.setText("New name");

        tbName = new Text(container, SWT.BORDER | SWT.SINGLE);
        tbName.setText(this.name);
        tbName.selectAll();
        tbName.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                // not used
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setPageComplete(false);
                boolean match = false;
                for (Tool t : tools) {
                    if (t.getName().toLowerCase().equals(tbName.getText().toLowerCase())) {
                        match = true;
                        break;
                    }
                }
                if (!match)
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
        return tbName.getText();
    }
}
