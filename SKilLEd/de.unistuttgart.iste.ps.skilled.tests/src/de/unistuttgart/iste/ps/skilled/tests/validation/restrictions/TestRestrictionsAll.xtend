package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestRestrictionsAll {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;

	var static String specification = "";

	@BeforeClass
	def static void setup() {
		specification = FileLoader.loadFile("validation/restrictions/restrictionsAll");
	}

	@Test
	def void test() {
		var issueCount = 0
		// Look only for Errors, not Warnings
		for (issue : specification.parse.validate){
			if ("ERROR".equals(issue.severity.toString())) {
				issueCount++
			}
		}	
		Assert::assertTrue(issueCount == 0);
	}
}