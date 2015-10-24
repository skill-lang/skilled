package de.unistuttgart.iste.ps.skilled.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SKilLNewToolWizardPage extends WizardPage {

	private Text tbName;
	private Composite container;

	public SKilLNewToolWizardPage() {
		super("Create a new tool");
		setTitle("Create a new tool");
		setDescription("In this page you can insert a name for the new tool.");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
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
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		tbName.setLayoutData(gd);
		// required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	public String getTbNameText() {
		return this.tbName.getText();
	}
}
