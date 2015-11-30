package de.unistuttgart.iste.ps.skilled.tests.acceptance;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestConstant {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String specification = "";
	
	@BeforeClass
	def static void setup() {
		specification = FileLoader.loadFile("acceptance/constant");
	}
	
	@Test
	def void test() {
		val issueCount = specification.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}