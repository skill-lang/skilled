package de.unistuttgart.iste.ps.skilled.ui.tools.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * Create a new tool. The configuration is automatically persisted if [ok] is
 * pressed and discarded otherwise.
 * 
 * @author Timm Felden
 */
final public class ToolCreationDialog extends Dialog {

    final private SkillFile sf;
    private Text name;
    private Button addAll;

    public ToolCreationDialog(Shell parentShell, SkillFile sf) {
        super(parentShell);
        this.sf = sf;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite body = new Composite(parent, SWT.NONE);
        {
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
            layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
            layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
            layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
            body.setLayout(layout);
            body.setLayoutData(new GridData(GridData.FILL_BOTH));
            applyDialogFont(body);
        }

        {
            Label lblName = new Label(body, SWT.NONE);
            lblName.setText("Name:");
        }

        {
            name = new Text(body, SWT.BORDER);
            name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

            final String[] toolNames = sf.Tools().stream().map(t -> t.getName().toLowerCase()).toArray(String[]::new);

            name.addModifyListener(e -> {
                final String content = name.getText().toLowerCase();
                if (content.isEmpty()) {
                    canFinish(false);
                } else {
                    for (String n : toolNames) {
                        if (n.equals(content)) {
                            canFinish(false);
                            return;
                        }
                    }
                    canFinish(true);
                }
            });
        }

        {
            Label lblAddAllTypes = new Label(body, SWT.NONE);
            lblAddAllTypes.setText("Add all Types?");
        }

        {
            addAll = new Button(body, SWT.CHECK);
            addAll.setToolTipText("Create tool with all types and fields selected.");
            addAll.setSelection(true);
            addAll.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        }

        return body;
    }

    void canFinish(boolean status) {
        getButton(IDialogConstants.OK_ID).setEnabled(status);
    }

    @Override
    protected void okPressed() {
        applyChanges();
        super.okPressed();
    }

    private void applyChanges() {
        Tool tool = sf.Tools().make();
        tool.setName(name.getText());

        if (addAll.getSelection()) {
            // add all types and their fields
            throw null;
        }
        sf.flush();
    }
}
