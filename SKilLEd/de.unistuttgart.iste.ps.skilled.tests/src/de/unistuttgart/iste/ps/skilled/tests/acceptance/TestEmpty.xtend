package de.unistuttgart.iste.ps.skilled.tests.acceptance;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SkillInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.File
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
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestEmpty {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String specification = "";
	
	@BeforeClass
	def static void setup() {
		specification = FileLoader.loadFile("acceptance/empty");
	}
	
	@Test
	def void test() {
		val issueCount = specification.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}