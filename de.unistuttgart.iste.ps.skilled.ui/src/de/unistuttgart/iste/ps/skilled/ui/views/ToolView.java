package de.unistuttgart.iste.ps.skilled.ui.views;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.unistuttgart.iste.ps.skilled.sir.ClassType;
import de.unistuttgart.iste.ps.skilled.sir.FieldLike;
import de.unistuttgart.iste.ps.skilled.sir.Identifier;
import de.unistuttgart.iste.ps.skilled.sir.Tool;
import de.unistuttgart.iste.ps.skilled.sir.Type;
import de.unistuttgart.iste.ps.skilled.sir.UserdefinedType;
import de.unistuttgart.iste.ps.skilled.sir.api.SkillFile;
import de.unistuttgart.iste.ps.skilled.tools.SIRCache;

/**
 * This class creates the SKilL-Tool-Overview which enables the user to create
 * and organize {@link Tool tools} within a SKilLEd-Project. It can be used to
 * add, remove and rename tools, add and remove types/fields/hints.
 *
 * @author Timm Felden
 * 
 * @category GUI
 */
public final class ToolView extends ViewPart {

    public ToolView() {
        // listen to file changes
        new FileChangeAction(this);
    }

    // View Components
    private Composite content = null;
    private List tools = null;
    private Tree types = null;
    private Tree fields = null;

    // State of the View
    private IProject activeProject = null;
    private SkillFile skillFile = null;

    private Tool selectedTool = null;
    private ClassType selectedType = null;
    private FieldLike selectedField = null;

    @Override
    public void createPartControl(Composite parent) {
        final boolean hasProject = ensureActiveProjectandSIR();
        {
            content = parent;
            content.setLayout(new GridLayout(3, false));
            content.setLayoutData(new GridData(GridData.FILL_BOTH));
        }

        // create headline
        {
            new Label(content, 0).setText("Tools");
            new Label(content, 0).setText("Types");
            new Label(content, 0).setText("Fields");
        }

        // tool selection
        {
            tools = new List(content, SWT.SINGLE | SWT.H_SCROLL);
            tools.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

            if (hasProject)
                for (Tool t : skillFile.Tools())
                    tools.add((t).getName());

            ToolViewListener tvl = new ToolViewListener(this);
            tvl.initToolListListener(tools);
            tvl.initPartListener(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());

            new ToolViewContextMenu(this, tools);
        }

        // type selection
        {
            types = new Tree(content, SWT.CHECK | SWT.SINGLE);
            types.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));

            if (hasProject)
                updateTypes();

            TypeTreeListener tvl = new TypeTreeListener(this);
            tvl.initTypeTreeListener(types);
        }

        // field selection
        {
            fields = new Tree(content, SWT.CHECK);
            fields.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

            // we do not add fields on construction, they will be added as a
            // reaction to type selection

            FieldTreeListener ftl = new FieldTreeListener(this);
            ftl.initFieldTreeListener(fields);
        }

        setFocus();
    }

    @Override
    public void setFocus() {
        content.setFocus();
    }

    /**
     * refreshs the data shown in the {@link ToolView toolview} and sets the
     * {@link CTabFolder tooltab} as the selected tab of the {@link CTabFolder
     * tabfolder}.
     * 
     * @category GUI
     */
    public void refresh() {
        if (!content.isDisposed()) {
            refreshTools();
        }
    }

    /**
     * Ensures that the .sir file used by this dialog is connected to
     * <code>{@link IProject activeProject}</code>.
     * 
     * @category Data Handling
     */
    boolean ensureActiveProjectandSIR() {
        try {
            IFileEditorInput file = (IFileEditorInput) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getActivePage().getActiveEditor().getEditorInput();

            activeProject = file.getFile().getProject();
            if (null == activeProject)
                return false;
        } catch (NullPointerException e) {
            // some of the components is not yet available
            return false;
        }

        skillFile = SIRCache.ensureFile(activeProject);
        return true;
    }

    /**
     * Builds up the {@link CTabItem tooltab} inside the {@link ToolView
     * toolview} and a {@link List list} of all {@link Tools tools}.
     * 
     * @return a {@link List} containing all the tools in the .skills file of
     *         the <code>{@link IProject activeProject}</code>
     * @category Data Handling
     */
    void refreshTools() {
        boolean hasProject = ensureActiveProjectandSIR();

        tools.setItems(new String[0]);

        boolean sawSelectedTool = false;

        if (hasProject)
            for (Tool t : skillFile.Tools()) {
                tools.add((t).getName());
                if (t == selectedTool) {
                    sawSelectedTool = true;
                    tools.select(tools.getItemCount() - 1);
                }
            }

        if (!sawSelectedTool)
            selectedTool = null;
        updateTypes();
    }

    /**
     * updates the contents and state of the types list
     */
    private void updateTypes() {
        if (null == types)
            return;

        types.removeAll();

        // add all types to the tree
        boolean sawSelectedType = false;
        for (UserdefinedType type : skillFile.UserdefinedTypes()) {
            TreeItem item = new TreeItem(types, 0);
            item.setText(nameToText(type.getName()));
            item.setData(type);

            if (null != selectedTool && selectedTool.getSelectedUserTypes().contains(type)) {
                item.setChecked(true);
            }

            if (selectedType == type) {
                sawSelectedType = true;
                types.select(item);
            }
        }

        if (!sawSelectedType) {
            selectedType = null;
            selectedField = null;
        }
        updateFields();
    }

    /**
     * add all fields of the selected type to a view
     */
    private void updateFields() {
        if (null == fields)
            return;

        fields.removeAll();

        if (null != selectedType) {
            for (FieldLike f : selectedType.getFields()) {
                TreeItem item = new TreeItem(fields, 0);
                item.setText(nameToText(f.getName()));
                item.setData(f);

                if (null != selectedTool) {
                    HashMap<String, FieldLike> map = selectedTool.getSelectedFields().get(selectedType);
                    item.setChecked(null != map && map.containsKey(f.getName().getSkillname()));

                    if (f == selectedField) {
                        fields.select(item);
                    }
                }
            }
        }
    }

    private String nameToText(Identifier name) {
        StringBuilder sb = new StringBuilder();
        for (String n : name.getParts())
            sb.append(n);
        return sb.toString();
    }

    /**
     * Return the selected .
     * 
     * @category Getter
     * @return the selected {@link FieldLike field}.
     */
    FieldLike getSelectedField() {
        return selectedField;
    }

    /**
     * Set the selected {@link FieldLike field}.
     * 
     * @category Setter
     * @param selectedField
     *            the selected {@link FieldLike field}.
     */
    void setSelectedField(FieldLike selectedField) {
        this.selectedField = selectedField;
        // TODO update???
    }

    /**
     * Get the selected {@link Type type}.
     * 
     * @category Getter
     * @return the selected {@link Type type}
     */
    ClassType getSelectedType() {
        return selectedType;
    }

    /**
     * Set the selected {@link Type type}.
     * 
     * @category Setter
     * @param selectedType
     *            the selected {@link Type type}.
     */
    void setSelectedType(ClassType selectedType) {
        if (this.selectedType != selectedType) {
            this.selectedType = selectedType;
            this.selectedField = null;

            updateFields();
        }
    }

    /**
     * Get the active {@link Tool tool}.
     * 
     * @category Getter
     * @return the active {@link Tool tool}.
     */
    Tool getActiveTool() {
        return selectedTool;
    }

    /**
     * Set the active {@link Tool tool}.
     * 
     * @category Setter
     * @param activeTool
     *            the active {@link Tool tool}.
     */
    void setActiveTool(Tool activeTool) {
        this.selectedTool = activeTool;
        updateTypes();
    }

    /**
     * Get the active {@link IProject project}.
     * 
     * @category Getter
     * @return the active {@link IProject project}.
     */
    IProject getActiveProject() {
        return activeProject;
    }

    /**
     * Get the shell.
     * 
     * @category Getter
     * @return {@link Shell shell}
     */
    public Shell getShell() {
        return content.getShell();
    }

    /**
     * Get skill file.
     * 
     * @category Getter
     * @return {@link SkillFile skillfile}
     */
    public SkillFile getSkillFile() {
        return skillFile;
    }
}
