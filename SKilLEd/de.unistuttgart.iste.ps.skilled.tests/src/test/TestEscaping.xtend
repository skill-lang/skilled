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
class TestEscaping {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! escaping
			# This test should help detect lack of proper escaping in most languages

			/** Stupid typename */
			Int {

			  /** A keyword in most languages */
			  Int if;

			  /** Another potential name clash */
			  if for;
			}

			/** Another stupid typename */
			if{}

			/**
			 * Representation of another type.
			 * @note Caused by a Bug in the C generator.
			 */
			Boolean {
			  /** reference to a boolean */
			  Boolean bool;

			  /** a flag */
			  bool boolean;
			}

			/* non-printable unicode characters */
			∀ {
			  ∀ €;
			 string ☢;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}