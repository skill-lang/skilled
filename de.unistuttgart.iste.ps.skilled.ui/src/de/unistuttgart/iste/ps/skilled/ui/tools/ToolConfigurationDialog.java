package de.unistuttgart.iste.ps.skilled.ui.tools;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.unistuttgart.iste.ps.skilled.sir.BuildInformation;
import de.unistuttgart.iste.ps.skilled.sir.FilePath;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;

/**
 * Manages tool configuration. The configuration is automatically persisted if
 * [ok] is pressed and discarded otherwise.
 * 
 * @author Timm Felden
 */
final public class ToolConfigurationDialog extends Dialog {

    private static final class BuildData {
        final BuildInformation src;
        // true to signal that a preexisting build shall be deleted
        boolean isDeleted = false;

        String language;
        String output;
        String options;

        BuildData() {
            src = null;
            language = "";
            output = "";
            options = "";
        }

        BuildData(BuildInformation info) {
            this.src = info;
            this.language = info.getLanguage();
            this.output = toString(info.getOutput());
            this.options = toString(info.getOptions());
        }

        private String toString(ArrayList<String> options) {
            if (null == options)
                return "";

            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String s : options) {
                if (first)
                    first = false;
                else
                    sb.append(' ');
                sb.append(s);
            }
            return sb.toString();
        }

        private String toString(FilePath output) {
            if (null == output)
                return "";

            StringBuilder sb = new StringBuilder(output.getIsAbsolut() ? "/" : "");
            for (String s : output.getParts()) {
                sb.append(s).append('/');
            }
            return sb.toString();
        }
    }

    private static final class BuildConfigDialog extends Composite {
        private final BuildData build;

        private Text language;
        private Text output;
        private Text options;

        public BuildConfigDialog(Composite parent, BuildData b) {
            super(parent, SWT.NONE);
            this.build = b;

            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            setLayout(layout);
            setLayoutData(new GridData(GridData.FILL_BOTH));
            applyDialogFont(this);

            Label lblLanguage = new Label(this, SWT.NONE);
            lblLanguage.setText("Language:");

            language = new Text(this, SWT.BORDER);
            language.setText(build.language);
            language.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            language.addModifyListener(e -> {
                build.language = language.getText();
            });

            Label lblOutputPath = new Label(this, SWT.NONE);
            lblOutputPath.setText("Output Path:");

            output = new Text(this, SWT.BORDER);
            output.setText(build.output);
            output.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            output.addModifyListener(e -> {
                build.output = output.getText();
            });

            Label lblOptions = new Label(this, SWT.NONE);
            lblOptions.setText("Options:");

            options = new Text(this, SWT.BORDER);
            options.setText(build.options);
            options.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            options.addModifyListener(e -> {
                build.options = options.getText();
            });

            Button deleteButton = new Button(this, SWT.NONE);
            deleteButton.setText("Delete Buildtarget");
            deleteButton.addListener(SWT.Selection, e -> {
                build.isDeleted = true;
                Shell shell = getShell();
                this.dispose();

                shell.pack();
                shell.redraw();
            });
        }

    }

    final private SkillFile sf;
    final private Tool target;
    final private HashMap<BuildData, BuildConfigDialog> builds = new HashMap<>();

    private Text newNameField;

    public ToolConfigurationDialog(Shell parentShell, Tool target, SkillFile sf) {
        super(parentShell);
        this.target = target;
        this.sf = sf;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected Control createDialogArea(Composite parent) {

        // create a composite with standard margins and spacing
        Composite body = new Composite(parent, SWT.NONE);
        {
            body.setLayout(new GridLayout(1, false));
            body.setLayoutData(new GridData(GridData.FILL_BOTH));
        }

        {
            Composite head = new Composite(body, SWT.NONE);

            Label l = new Label(head, 0);
            l.setText("Configuration of tool " + target.getName());

            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
            layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
            layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
            layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
            head.setLayout(layout);
            head.setLayoutData(new GridData(GridData.FILL_BOTH));
            applyDialogFont(head);

            new Label(head, SWT.NONE);

            Label lblName = new Label(head, SWT.NONE);
            lblName.setText("Name:");

            newNameField = new Text(head, SWT.BORDER);
            newNameField.setText(target.getName());
            newNameField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            new Label(head, SWT.NONE);

            Button addNewTarget = new Button(head, SWT.NONE);
            addNewTarget.setText("Add new Buildtarget");
            addNewTarget.addListener(SWT.Selection, e -> {
                BuildData info = new BuildData();
                builds.put(info, new BuildConfigDialog(body, info));

                getShell().pack();
                getShell().redraw();
            });
        }

        for (BuildInformation b : target.getBuildTargets()) {
            BuildData info = new BuildData(b);
            builds.put(info, new BuildConfigDialog(body, info));
        }

        return body;
    }

    @Override
    protected void okPressed() {
        applyChanges();
        super.okPressed();
    }

    private void applyChanges() {
        // set the new name
        target.setName(newNameField.getText());

        // copy build info from dialog to target
        for (BuildData b : builds.keySet()) {
            if (b.isDeleted && null != b.src) {
                // delete
                sf.delete(b.src);
                target.getBuildTargets().remove(b.src);
            } else if (null != b.src) {
                // update
                b.src.setLanguage(b.language.toLowerCase());
                b.src.setOutput(mkFilePath(b.output));
                b.src.setOptions(mkOptions(b.options));
            } else {
                // create
                BuildInformation info = sf.BuildInformations().make(b.language.toLowerCase(), mkOptions(b.options),
                        mkFilePath(b.output));
                target.getBuildTargets().add(info);
            }
        }
        sf.flush();
    }

    private ArrayList<String> mkOptions(String options) {
        ArrayList<String> rval = new ArrayList<>();
        for (String s : options.split("\\s+")) {
            rval.add(s);
        }
        return rval;
    }

    private FilePath mkFilePath(String output) {
        File f = Paths.get(output).toFile();
        ArrayList<String> parts = new ArrayList<>();
        addParts(f, parts);
        return sf.FilePaths().make(f.isAbsolute(), parts);
    }

    /**
     * recursive reverse unpack of file structure
     */
    private void addParts(File f, ArrayList<String> parts) {
        if (null != f.getParentFile()) {
            addParts(f.getParentFile(), parts);
            parts.add(f.getName());
        } else if(!f.isAbsolute()){
            // add base part of a relative part
            parts.add(f.getName());
        }
    }
}
