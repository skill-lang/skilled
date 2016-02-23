package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.tools.Tool;

import java.util.ArrayList;

/**
 * Created on 23.02.16.
 *
 * @author Armin Hüneburg
 */
public class BreakageException extends RuntimeException {
    private final ArrayList<Tool> brokenTools = new ArrayList<>();

    @Override
    public String getMessage() {
        if (brokenTools.size() == 0) {
            return "";
        } else {
            final StringBuilder builder = new StringBuilder();
            builder.append("The following tools are broken: \n - ");
            for (Tool brokenTool : brokenTools) {
                builder.append(brokenTool.getName());
                builder.append("\n - ");
            }
            builder.setLength(builder.length() - 4);
            return builder.toString();
        }
    }

    public void addTool(Tool tool) {
        brokenTools.add(tool);
    }
}
