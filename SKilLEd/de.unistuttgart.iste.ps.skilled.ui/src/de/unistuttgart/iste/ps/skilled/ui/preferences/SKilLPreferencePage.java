package de.unistuttgart.iste.ps.skilled.ui.preferences;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

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
		
		// gets current value of SKilLConstants.LANGUAGE_PATH
		lp.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				languagepath = event.getNewValue();
			}
		});
		
		// gets current value of SKilLConstants.EXECUTION_ENVIRONMENT
		xp.setPropertyChangeListener(new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				execenvpath = event.getNewValue();
			}
		});
	}

	// creates fields so that users can select the SKilLls commands they want.
	@Override
	protected void createFieldEditors() {
		noDefaultAndApplyButton();

		a = new BooleanFieldEditor(SKilLConstants.LIST_TOOLS, "List/Generate all tools (-a/--all)",
				getFieldEditorParent());
		addField(a);

		// Seperator
		Label e1 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		e1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

		gp = new FileFieldEditor(SKilLConstants.GENERATOR_PATH, "Generator Path:", true, 1, getFieldEditorParent());
		// gp.isEmptyStringAllowed();
		addField(gp);

		// Seperator
		Label e2 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		e2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

		lp = new ComboFieldEditor(SKilLConstants.LANGUAGE_PATH, "Select Language:",
				new String[][] { { "Ada", "Ada" }, { "C", "C" }, { "Java", "Java" }, { "Scala", "Scala" } },
				getFieldEditorParent());
		addField(lp);

		// Seperator
		Label e3 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		e3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

		op = new DirectoryFieldEditor(SKilLConstants.OUTPUT_PATH, "Output Location:", getFieldEditorParent());
		addField(op);

		// Seperator
		Label e4 = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		e4.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));

		xp = new ComboFieldEditor(SKilLConstants.EXECUTION_ENVIRONMENT_PATH, "Execution Environment:",
				new String[][] {}, getFieldEditorParent());
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

		mp = new StringFieldEditor(SKilLConstants.MODULE_PATH, "Name of Module:", getFieldEditorParent());
		addField(mp);
	}

	@Override
	public void init(IWorkbench workbench) {
		// Do nothing
	}

	@Override
	public boolean performOk() {
		generatorpath = gp.getStringValue();
		outputpath = op.getStringValue();
		modulepath = mp.getStringValue();
		
		File generatorFileTest = new File(generatorpath);
		File outputFileTest = new File(outputpath);
		
		// Error pop-ups if necessary fields are unused.
		if (!generatorFileTest.isFile() | !generatorFileTest.exists()) {
			JOptionPane.showMessageDialog(new JFrame(), "Invalid generator path!", all, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!outputFileTest.isDirectory() | !outputFileTest.exists()) {
			JOptionPane.showMessageDialog(new JFrame(), "Invalid output path!", all, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (modulepath == null| modulepath.equals("") | modulepath.isEmpty()) {
			JOptionPane.showMessageDialog(new JFrame(), "Name of module missing!", all, JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (a.getBooleanValue() == true) {
			all = "a";
		} else {
			all = "";
		}
		if (ls.getBooleanValue() == true) {
			list = "ls";
		} else {
			list = "";
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
			
			// Finds path of SKilLls.jar
			String fClassPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String fSKilLlsPath = fClassPath + "/../de.unistuttgart.iste.ps.skilled/lib/SKilLls.jar";
			File f1 = new File(fSKilLlsPath.substring(1));
			String fJavaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe";
			String fParameters = "-gloxm" + all + list + " " + generatorpath + " " + languagepath + " " + outputpath + " " + execenvpath + " "+ modulepath;
			
			// Runs SKilLls Generator with the commands selected in the
			// preference page.
			List<String> commands = new ArrayList<String>();
			commands.add("java");
			commands.add("-jar");
			commands.add(f1.getCanonicalPath());
			commands.add (fParameters);
			
			System.out.println(commands);

			ProcessBuilder processBuilder = new ProcessBuilder(commands);
			processBuilder.redirectOutput(Redirect.INHERIT);
			processBuilder.redirectError(Redirect.INHERIT);
			Process process = processBuilder.start();
			
			java.io.InputStream err = process.getErrorStream();	
			for (int i = 0; i < err.available(); i++) {
				System.out.println(err.read() + "\n");
			}
			
			java.io.InputStream in = process.getInputStream();	
			for (int i = 0; i < in.available(); i++) {
				System.out.println(in.read() + "\n");
			}

			return super.performOk();
		}

		catch (IOException e) {
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