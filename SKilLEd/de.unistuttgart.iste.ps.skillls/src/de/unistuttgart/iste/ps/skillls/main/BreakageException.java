package de.unistuttgart.iste.ps.skillls.main;

import de.unistuttgart.iste.ps.skillls.tools.Tool;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that indicates that at least one tool is broken.
 *
 * Created on 23.02.16.
 *
 * @author Armin Hüneburg
 */
public class BreakageException extends RuntimeException {
	private final ArrayList<Tool> brokenTools = new ArrayList<>();

	/**
	 * Constructor.
	 *
	 * adds all tools to the exception.
	 * 
	 * @param tools
	 *            tools that are broken
	 */
	public BreakageException(Collection<Tool> tools) {
		super();
		brokenTools.addAll(tools);
	}

	/**
	 * Returns the detail message string of this throwable.
	 * 
	 * @return returns the detail message string of this exception
	 */
	@Override
	public String getMessage() {
		super.getMessage();
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

	/**
	 *
	 * @return Returns the broken tools.
	 */
	public Tool[] getTools() {
		Tool[] tools = new Tool[brokenTools.size()];
		tools = brokenTools.toArray(tools);
		return tools;
	}
}
