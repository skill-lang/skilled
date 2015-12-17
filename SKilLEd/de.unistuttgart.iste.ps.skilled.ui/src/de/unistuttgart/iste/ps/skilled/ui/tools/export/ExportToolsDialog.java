package de.unistuttgart.iste.ps.skilled.ui.tools.export;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ExportToolsDialog {
	Display d;
	String fName = "";
	String[] cSelectToolItems;
	int fNumberofTools;

	public void setNumberofTools(int number) {
		fNumberofTools = number;
	}

	public void setListofTools(List toolViewList) {
		// TODO
		toolViewList.add("", 0);
		cSelectToolItems = toolViewList.getItems();
	}

	/**
	 * Creates dialog window
	 * 
	 */
	public void run() {
		Shell shell = new Shell(d);
		shell.setText("Export Tool");
		shell.layout(true, true);
		createContents(shell);
		final Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		shell.setSize(newSize);
		shell.open();
	}

	/**
	 * Creates contents of the dialog window
	 * 
	 * @param shell
	 *            The window in which the content is created in
	 */
	public void createContents(final Shell shell) {
		shell.setLayout(new GridLayout(4, true));

		Label title = new Label(shell, SWT.NONE);
		title.setText("Export Tool");
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.CENTER;
		gridData.horizontalSpan = 4;
		title.setLayoutData(gridData);

		Label lSelectTool = new Label(shell, SWT.NONE);
		lSelectTool.setText("Select Tool:");
		GridData gridDataLabel = new GridData();
		gridDataLabel.horizontalAlignment = SWT.CENTER;
		gridDataLabel.horizontalSpan = 2;
		lSelectTool.setLayoutData(gridDataLabel);

		Combo cSelectTool = new Combo(shell, SWT.READ_ONLY);
		GridData gridDataWidgets = new GridData();
		gridDataWidgets.horizontalAlignment = SWT.FILL;
		gridDataWidgets.grabExcessHorizontalSpace = true;
		gridDataWidgets.horizontalSpan = 2;
		cSelectTool.setLayoutData(gridDataWidgets);
		cSelectTool.setItems(cSelectToolItems);
		// Default combofield value (blank)
		cSelectTool.select(0);
		cSelectTool.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO
				fName = cSelectTool.getText();
			}
		});

		Label lNameOfToolFile = new Label(shell, SWT.NONE);
		lNameOfToolFile.setText("Name of tool file:");
		lNameOfToolFile.setLayoutData(gridDataLabel);

		Text tNameOfToolFile = new Text(shell, SWT.BORDER | SWT.SINGLE);
		tNameOfToolFile.setText(fName);
		tNameOfToolFile.setLayoutData(gridDataWidgets);
		tNameOfToolFile.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textWidget = (Text) e.getSource();
				fName = textWidget.getText();
			}
		});

		// Fills grid cell with empty block
		Label lEmptyBlock = new Label(shell, SWT.NONE);
		lEmptyBlock.setLayoutData(new GridData());

		// Creates OK Button.
		Button OK = new Button(shell, SWT.PUSH);
		GridData gridDataButtons = new GridData();
		gridDataButtons.horizontalAlignment = SWT.FILL;
		gridDataButtons.verticalAlignment = SWT.FILL;
		OK.setLayoutData(gridDataButtons);
		OK.setText("OK");
		OK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				// TODO
				if (cSelectTool.getText().equals("")) {
					// Error message
				}
			}
		});

		// Cancel button. Closes dialog window.
		Button Cancel = new Button(shell, SWT.PUSH);
		Cancel.setLayoutData(gridDataButtons);
		Cancel.setText("Cancel");
		Cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				shell.dispose();
			}
		});
	}
}
