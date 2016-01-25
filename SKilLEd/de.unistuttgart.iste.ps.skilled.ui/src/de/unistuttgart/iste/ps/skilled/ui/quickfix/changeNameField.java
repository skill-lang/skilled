package de.unistuttgart.iste.ps.skilled.ui.quickfix;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * @author Jan Berberich
 * 
 *         This field is used in the Quickfix for nonASCIICharWarnings. It opens a dialog to enter a new name for the Field
 *         or TypeDeclarationand changes the name to the entered name if the user clicks on the OK Button.
 *
 */
public class changeNameField {
    private String oldName = ""; // The old name of the element.
    private setName name; // The class used to change the name.

    /**
     * Opens the chanceNameField window.
     * 
     */
    public void open() {
        // create Shell
        Display d = Display.getDefault();
        final Shell shell = new Shell(d);
        shell.setText("Enter new Name");
        // create textfield
        final Text text1 = new Text(shell, SWT.NONE);
        text1.setText(oldName);
        // create Button
        Button continueButton = new Button(shell, SWT.NONE);
        continueButton.setText("OK");
        continueButton.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event e) {
                switch (e.type) {
                    case SWT.Selection:
                        String newName = text1.getText();
                        if (!newName.equals("")) {
                            name.changeName(newName);
                            shell.close();
                        }
                }
            }
        });
        // set Layout
        shell.setLayout(new GridLayout(1, true));
        GridData gridData = new GridData();
        text1.setLayoutData(gridData);
        continueButton.setLayoutData(gridData);
        Point newSize = shell.computeSize(400, 200, true);
        shell.setSize(newSize);
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        shell.open();
    }

    /**
     * Sets the old name of the Element.
     * 
     * @param s
     *            old Name of the Element
     */
    public void setOldName(String s) {
        oldName = s;
    }

    public changeNameField(setName name) {
        this.name = name;
    }

}
