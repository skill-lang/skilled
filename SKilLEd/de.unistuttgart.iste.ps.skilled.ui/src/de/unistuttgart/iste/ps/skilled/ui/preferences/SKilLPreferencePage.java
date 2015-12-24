package de.unistuttgart.iste.ps.skilled.ui.preferences;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;

import de.unistuttgart.iste.ps.skilled.ui.internal.SKilLActivator;

public class SKilLPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage, IWorkbenchPropertyPage {
	
	private DirectoryFieldEditor projectPathField;
	private RadioGroupFieldEditor generateOption;
	private FileFieldEditor generatorPathField;
	private ComboFieldEditor languageSelectionComboField;
	private ComboFieldEditor executionEnvironmentComboField;
	private StringFieldEditor moduleNameField;

	public SKilLPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		setPreferenceStore(SKilLActivator.getInstance().getPreferenceStore());
	}

	@Override
	protected void initialize() {
		super.initialize();
		getFieldEditorParent();
	}

	private void putSeperator() {
		Label l = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		l.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 2));
	}
	
	@Override
	protected void createFieldEditors() {
		noDefaultAndApplyButton();
		
		projectPathField = new DirectoryFieldEditor(SKilLConstants.TARGET_PROJECT_PATH, "Target project location:", getFieldEditorParent());
		addField(projectPathField);
		
		putSeperator();

		generateOption = new RadioGroupFieldEditor(SKilLConstants.GENERATE_OPTION, "Generation Mode", 2, new String[][] {{"Generate all tools (-ag)", "Generate all tools (-ag)"}, {"Generate lazy (-g)", "Generate lazy (-g)"}}, getFieldEditorParent());
		addField(generateOption);
		
		putSeperator();
		
		generatorPathField = new FileFieldEditor(SKilLConstants.GENERATOR_PATH, "Generator path:", true, 1, getFieldEditorParent());
		addField(generatorPathField);

		putSeperator();

		languageSelectionComboField = new ComboFieldEditor(SKilLConstants.LANGUAGE_SELECTION, "Select language:", new String[][] { { "Ada", "Ada" }, { "C", "C" }, { "Java", "Java" }, { "Scala", "Scala" } }, getFieldEditorParent());
		addField(languageSelectionComboField);

		putSeperator();

		executionEnvironmentComboField = new ComboFieldEditor(SKilLConstants.EXECUTION_ENVIRONMENT, "Execution environment:", new String[][] { { "Scala", "Scala" } }, getFieldEditorParent());
		addField(executionEnvironmentComboField);

		putSeperator();

		moduleNameField = new StringFieldEditor(SKilLConstants.MODULE_NAME, "Name of module:", getFieldEditorParent());
		addField(moduleNameField);
	}

	@Override
	public boolean performOk() {
		projectPathField.store();
		generateOption.store();
		generatorPathField.store();
		languageSelectionComboField.store();
		executionEnvironmentComboField.store();
		moduleNameField.store();
		return super.performOk();
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// Do nothing
	}

	@Override
	public IAdaptable getElement() {
		return null;
	}

	@Override
	public void setElement(IAdaptable element) {
		// Do nothing
	}
}
