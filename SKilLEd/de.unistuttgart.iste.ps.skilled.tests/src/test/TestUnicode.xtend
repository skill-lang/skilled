package test

import org.eclipse.xtext.junit4.InjectWith
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.XtextRunner
import com.google.inject.Inject
import org.eclipse.xtext.junit4.util.ParseHelper
import de.unistuttgart.iste.ps.skilled.sKilL.File
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.Assert

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestUnicode {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! unicode

			/** this test is used to check unicode handling inside of strings;
			    only one instance but no @singleton to keep things simple;
			    all fields contain one character. */
			Unicode {
			  /** contains "1", a one byte string */
			  string one;
			  /** contains "ö", a two byte string */
			  string two;
			  /** contains "☢", a three byte string */
			  string three;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}