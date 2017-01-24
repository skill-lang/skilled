package de.unistuttgart.iste.ps.skilled.ui.quickfix;

import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.ui.editor.model.edit.DefaultTextEditComposer;


public class SkillTextEditComposer extends DefaultTextEditComposer {

    @Override
    protected SaveOptions getSaveOptions() {
        // Activates the formatter after quickfix.
        return SaveOptions.newBuilder().format().getOptions();
    }
}
