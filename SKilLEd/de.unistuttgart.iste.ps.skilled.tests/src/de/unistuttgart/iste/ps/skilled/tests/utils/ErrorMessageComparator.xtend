package de.unistuttgart.iste.ps.skilled.tests.utils

import org.eclipse.xtext.validation.Issue
import java.util.List
import java.util.ArrayList

/**
 * @author Tobias Heck
 */
class ErrorMessageComparator {
	
	public static final val ERROR_INHERITANCE_CYCLE = "Error: can't use extend in a Cycle";
	public static final val ERROR_INHERITANCE_PARENT_IS_SELF = "Error: type can't be his own parent.";
	public static final val ERROR_INHERITANCE_MULTIPLE = "Error: Multiple Inheritence is not allowed.";

	public static def boolean containsMessage(List<Issue> issues, String s) {
		val List<String> errors = new ArrayList<String>(5);
		for (Issue issue : issues) {
			if (issue.message.equals(s)) return true;
			errors.add(issue.message);
		}
		println("The expected message wasn't found.");
		println("Instead, the following errors appeared:")
		for (String error : errors) {
			println(error);
		}
		return false;
	}
	
}