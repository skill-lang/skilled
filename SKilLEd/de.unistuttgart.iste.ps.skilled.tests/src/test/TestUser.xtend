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
class TestUser {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! user
			# The standard user example as commonly used by the SKilL specification.

			/** A user has a name and an age. */
			User {

			  /**
			   * Full name of the user ignoring regional specifics like give or family
			   * names.
			   */
			  string name;

			  /** The current age of the user. */
			  v64 age;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}