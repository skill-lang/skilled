package de.unistuttgart.iste.ps.skilled.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;


/**
 * Creates the initial SKilL Perspective Layout. It adds the Project Explorer, Toolview, Problem View and Outline View.
 * Additionally the shortcuts for opening the views are added in the menus and also the shortcuts for creating a SKilL
 * Project and File.
 * 
 * @author Marco Link
 */
public class SKilLPerspectiveFactory implements IPerspectiveFactory {

    @Override
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();

        IFolderLayout folder = layout.createFolder("left", IPageLayout.LEFT, (float) 0.25, editorArea);
        folder.addView(IPageLayout.ID_PROJECT_EXPLORER);

        IFolderLayout problemViewfolder = layout.createFolder("bottom", IPageLayout.BOTTOM, (float) 0.75, editorArea);
        problemViewfolder.addView(IPageLayout.ID_PROBLEM_VIEW);

        IFolderLayout toolViewFolder = layout.createFolder("toolView", IPageLayout.RIGHT, (float) 0.75, editorArea);
        toolViewFolder.addView("de.unistuttgart.iste.ps.skilled.ui.views.ToolView");

        IFolderLayout outlineFolder = layout.createFolder("right", IPageLayout.BOTTOM, (float) 0.5, "left");
        outlineFolder.addView(IPageLayout.ID_OUTLINE);

        // views - standard workbench
        layout.addShowViewShortcut("de.unistuttgart.iste.ps.skilled.ui.views.ToolView");
        layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
        layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);

        // new actions - SKilL project creation wizard
        layout.addNewWizardShortcut("de.unistuttgart.iste.ps.skilled.ui.wizard.new.SKilL");
        layout.addNewWizardShortcut("de.unistuttgart.iste.ps.skilled.ui.wizards.SKilLNewFileWizard");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");

    }

    /**
     * This will open the SKilL Perspective. It will be only opened, if it isn't already active and it won't be resetted.
     */
    public static void openSKilLPerspective() {
        try {
            PlatformUI.getWorkbench().showPerspective("de.unistuttgart.iste.ps.skilled.ui.SKilLPerspective",
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        } catch (WorkbenchException e) {
            e.printStackTrace();
        }
    }

}
