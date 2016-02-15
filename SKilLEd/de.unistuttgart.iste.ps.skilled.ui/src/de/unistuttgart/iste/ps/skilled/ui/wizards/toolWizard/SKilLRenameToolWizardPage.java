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


public class SKilLRenameToolWizardPage extends WizardPage {

    private Text tbName;
    private Composite container;

    private String name;

    public SKilLRenameToolWizardPage(String name) {
        super("Rename Tool");
        setTitle("Rename Tool");
        setDescription("On this page you can rename the tool.");
        setControl(tbName);
        this.name = name;
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
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!tbName.getText().isEmpty()) {
                    setPageComplete(true);
                }
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
