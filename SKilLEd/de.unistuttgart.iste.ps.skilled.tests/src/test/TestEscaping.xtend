package test

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
import org.eclipse.xtext.validation.Issue

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestEscaping {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String specification = "";
	
	@BeforeClass
	def static void setup() {
		specification = FileLoader.loadFile("escaping");	
	}
	
	@Test
	def void test() {
		
		var boolean warningASCIIExists = false;
		for (Issue i: specification.parse.validate) {
			if (i.message.contains("Warning") && i.message.contains("non-ASCII-Chars")) {
				warningASCIIExists = true;
			}
		}
		
		Assert::assertTrue(warningASCIIExists);
	}
}