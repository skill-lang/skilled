package de.unistuttgart.iste.ps.skilled.ui.preferences;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;

import de.unistuttgart.iste.ps.skilled.ui.internal.SKilLActivator;


// Preference and Property page for SKilLls
public class SKilLPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage, IWorkbenchPropertyPage {
    // BooleanFieldEditor t;
    BooleanFieldEditor a;
    BooleanFieldEditor g;
    FileFieldEditor gp;
    BooleanFieldEditor l;
    ComboFieldEditor lp;
    BooleanFieldEditor o;
    DirectoryFieldEditor op;
    BooleanFieldEditor x;
    ComboFieldEditor xp;
    BooleanFieldEditor ls;
    BooleanFieldEditor m;
    StringFieldEditor mp;
    // String tool = "";
    String all = "";
    String generator = "";
    String generatorpath = "";
    String language = "";
    Object languagepath;
    String output = "";
    String outputpath = "";
    String execenv = "";
    Object execenvpath;
    String list = "";
    String module = "";
    String modulepath = "";

    public SKilLPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        IPreferenceStore store = SKilLActivator.getInstance().getPreferenceStore();
        store.setValue(SKilLConstants.LIST_TOOLS, false);
        store.setValue(SKilLConstants.GENERATOR, false);
        store.setValue(SKilLConstants.GENERATOR_PATH, "");
        store.setValue(SKilLConstants.LANGUAGE, false);
        store.setValue(SKilLConstants.LANGUAGE_PATH, 1);
        store.setValue(SKilLConstants.OUTPUT, false);
        store.setValue(SKilLConstants.OUTPUT_PATH, "");
        store.setValue(SKilLConstants.EXECUTION_ENVIRONMENT, false);
        store.setValue(SKilLConstants.EXECUTION_ENVIRONMENT_PATH, 1);
        store.setValue(SKilLConstants.LIST_OR_GENERATE, false);
        store.setValue(SKilLConstants.MODULE, false);
        store.setValue(SKilLConstants.MODULE_PATH, "");
        setPreferenceStore(store);
        setDescription("SKilLls");
    }

    @Override
    protected void initialize() {
        super.initialize();

        // deactivate all FieldEditors except SKilLConstants.IS_TOOL and SKilLConstants.LIST_TOOLS by default on first time
        // initalization
        // g.setEnabled(false, getFieldEditorParent());
        // l.setEnabled(false, getFieldEditorParent());
        // o.setEnabled(false, getFieldEditorParent());
        // x.setEnabled(false, getFieldEditorParent());
        // ls.setEnabled(false, getFieldEditorParent());
        // m.setEnabled(false, getFieldEditorParent());
        gp.setEnabled(false, getFieldEditorParent());
        lp.setEnabled(false, getFieldEditorParent());
        op.setEnabled(false, getFieldEditorParent());
        xp.setEnabled(false, getFieldEditorParent());
        mp.setEnabled(false, getFieldEditorParent());

        // deactivates or activates certain fields depending on whether SKilLConstants.IS_TOOL is check-marked or not.
        // t.setPropertyChangeListener(new IPropertyChangeListener() {
        // public void propertyChange(PropertyChangeEvent event) {
        // boolean hidePref = ((Boolean)event.getNewValue()).booleanValue();
        // g.setEnabled(hidePref, getFieldEditorParent());
        // l.setEnabled(hidePref, getFieldEditorParent());
        // o.setEnabled(hidePref, getFieldEditorParent());
        // x.setEnabled(hidePref, getFieldEditorParent());
        // ls.setEnabled(hidePref, getFieldEditorParent());
        // m.setEnabled(hidePref, getFieldEditorParent());
        // checkState();
        //
        // if (hidePref == false){
        // gp.setEnabled(hidePref, getFieldEditorParent());
        // lp.setEnabled(hidePref, getFieldEditorParent());
        // op.setEnabled(hidePref, getFieldEditorParent());
        // xp.setEnabled(hidePref, getFieldEditorParent());
        // mp.setEnabled(hidePref, getFieldEditorParent());
        // checkState();
        // }
        // }
        // });
        // activates SKilLConstants.GENERATOR_PATH when SKilLConstants.GENERATOR is check-marked.
        g.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                boolean hidePref = ((Boolean) event.getNewValue()).booleanValue();
                gp.setEnabled(hidePref, getFieldEditorParent());
                checkState();
            }
        });
        // activates SKilLConstants.LANGUAGE_PATH when SKilLConstants.LANGUAGE is check-marked.
        l.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                boolean hidePref = ((Boolean) event.getNewValue()).booleanValue();
                lp.setEnabled(hidePref, getFieldEditorParent());
                checkState();
            }
        });
        // gets current value of SKilLConstants.LANGUAGE_PATH
        lp.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                languagepath = event.getNewValue();
            }
        });
        // activates SKilLConstants.OUTPUT_PATH when SKilLConstants.OUTPUT is check-marked.
        o.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                boolean hidePref = ((Boolean) event.getNewValue()).booleanValue();
                op.setEnabled(hidePref, getFieldEditorParent());
                checkState();
            }
        });
        // activates SKilLConstants.EXECUTION_ENVIRONMENT_PATH when SKilLConstants.EXECUTION_ENVIRONMENT is check-marked.
        x.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                boolean hidePref = ((Boolean) event.getNewValue()).booleanValue();
                xp.setEnabled(hidePref, getFieldEditorParent());
                checkState();
            }
        });
        // gets current value of SKilLConstants.EXECUTION_ENVIRONMENT
        xp.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                execenvpath = event.getNewValue();
            }
        });
        // activates SKilLConstants.MODULE_PATH when SKilLConstants.MODULE is check-marked.
        m.setPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                boolean hidePref = ((Boolean) event.getNewValue()).booleanValue();
                mp.setEnabled(hidePref, getFieldEditorParent());
                checkState();
            }
        });
    }

    // creates fields so that users can select the SKilLls commands they want.
    @Override
    protected void createFieldEditors() {
        noDefaultAndApplyButton();

        // t = new BooleanFieldEditor(SKilLConstants.IS_TOOL, "Tools", getFieldEditorParent());
        // //t.setPreferenceStore(SKilLActivator.getDefault().getPreferenceStore());
        // addField(t);
        //
        //
        // //Seperator
        // Label e = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
        // e.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        a = new BooleanFieldEditor(SKilLConstants.LIST_TOOLS, "List/Generate all tools (-a/--all)", getFieldEditorParent());
        addField(a);

        // Seperator
        Label e1 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
        e1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        g = new BooleanFieldEditor(SKilLConstants.GENERATOR, "Generate Bindings(-g/--generator)", getFieldEditorParent());
        addField(g);
        gp = new FileFieldEditor(SKilLConstants.GENERATOR_PATH, "Generator Path:", true, 1, getFieldEditorParent());
        // gp.isEmptyStringAllowed();
        addField(gp);

        // Seperator
        Label e2 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
        e2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        l = new BooleanFieldEditor(SKilLConstants.LANGUAGE, "Language (-l/--lang)", getFieldEditorParent());
        addField(l);
        lp = new ComboFieldEditor(SKilLConstants.LANGUAGE_PATH, "Select Language:", new String[][] {},
                getFieldEditorParent());
        addField(lp);

        // Seperator
        Label e3 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
        e3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        o = new BooleanFieldEditor(SKilLConstants.OUTPUT, "Specify Output (-o/--output)", getFieldEditorParent());
        addField(o);
        op = new DirectoryFieldEditor(SKilLConstants.OUTPUT_PATH, "Output Location:", getFieldEditorParent());
        addField(op);

        // Seperator
        Label e4 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
        e4.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        x = new BooleanFieldEditor(SKilLConstants.EXECUTION_ENVIRONMENT, "Specify Execution Environment (-x/--exec)",
                getFieldEditorParent());
        addField(x);
        xp = new ComboFieldEditor(SKilLConstants.EXECUTION_ENVIRONMENT_PATH, "Execution Environment:", new String[][] {},
                getFieldEditorParent());
        addField(xp);

        // Seperator
        Label e5 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
        e5.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        ls = new BooleanFieldEditor(SKilLConstants.LIST_OR_GENERATE, "List instead of generate tools (-ls/--list)",
                getFieldEditorParent());
        addField(ls);

        // Seperator
        Label e6 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
        e6.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

        m = new BooleanFieldEditor(SKilLConstants.MODULE, "Specify Module (-m/--module)", getFieldEditorParent());
        addField(m);
        mp = new StringFieldEditor(SKilLConstants.MODULE_PATH, "Name of Module:", getFieldEditorParent());
        addField(mp);
    }

    @Override
    public void init(IWorkbench workbench) {
        // DO SOMETHING or nothing?
    }

    @Override
    public boolean performOk() {

        if (a.getBooleanValue() == true) {
            all = "a";
        } else {
            all = "";
        }
        if (g.getBooleanValue() == true) {
            generator = "g";
            generatorpath = gp.getStringValue();
        } else {
            generator = "";
            generatorpath = "";
        }
        if (l.getBooleanValue() == true) {
            language = "l";
        } else {
            language = "";
            languagepath = "";
        }
        if (o.getBooleanValue() == true) {
            output = "o";
            outputpath = op.getStringValue();
        } else {
            output = "";
            outputpath = "";
        }
        if (x.getBooleanValue() == true) {
            execenv = "x";
        } else {
            execenv = "";
            execenvpath = "";
        }
        if (ls.getBooleanValue() == true) {
            list = "ls";
        } else {
            list = "";
        }
        if (m.getBooleanValue() == true) {
            module = "m";
            modulepath = mp.getStringValue();
        } else {
            module = "";
            modulepath = "";
        }
        try {
            // Escape characters for spaces depending on OS.
            if (System.getProperty("os.name").startsWith("Windows")) {
                generatorpath = generatorpath.replaceAll("\\s+", "^ ");
                outputpath = outputpath.replaceAll("\\s+", "^ ");
                modulepath = modulepath.replaceAll("\\s+", "^ ");
            } else if (System.getProperty("os.name").startsWith("Linux")
                    || System.getProperty("os.name").startsWith("Mac")) {
                generatorpath = generatorpath.replaceAll("\\s+", "\\ ");
                outputpath = outputpath.replaceAll("\\s+", "\\ ");
                modulepath = modulepath.replaceAll("\\s+", "\\ ");
            }

            // For testing purposes
            System.out.println(System.getProperty("os.name"));
            System.out.println("-" + all + generator + language + output + execenv + list + module + " " + generatorpath
                    + " " + languagepath + " " + outputpath + " " + modulepath);

            // Runs SKilLls Generator with the commands selected in the preference page.
            @SuppressWarnings("unused")
            Process process = new ProcessBuilder("C:\\\\skillls.jar", "-" + all + generator + language + output + execenv
                    + list + module + " " + generatorpath + " " + languagepath + " " + outputpath + " " + modulepath)
                            .start();
            return super.performOk();
        }
        // For testing purposes
        // finally {}

        // Error pop-ups if necessary fields are unused.
        catch (IOException e) {
            if (g.getBooleanValue() == true && gp.getStringValue() == "") {
                JOptionPane.showMessageDialog(new JFrame(), "Generator path missing!", all, JOptionPane.ERROR_MESSAGE);
            }
            if (o.getBooleanValue() == true && op.getStringValue() == "") {
                JOptionPane.showMessageDialog(new JFrame(), "Output path missing!", all, JOptionPane.ERROR_MESSAGE);
            }
            if (m.getBooleanValue() == true && mp.getStringValue() == "") {
                JOptionPane.showMessageDialog(new JFrame(), "Module path missing!", all, JOptionPane.ERROR_MESSAGE);
            }
            if (a.getBooleanValue() == false && g.getBooleanValue() == false && l.getBooleanValue() == false
                    && o.getBooleanValue() == false && x.getBooleanValue() == false && ls.getBooleanValue() == false
                    && m.getBooleanValue() == false) {
                JOptionPane.showMessageDialog(new JFrame(), "Nothing selected!", all, JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IAdaptable getElement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setElement(IAdaptable element) {
        // TODO Auto-generated method stub

    }

}
