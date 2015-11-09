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
class TestRestrictionsCore {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! restrictionsCore
			#
			#Test to check support for core restrictions at generator level; note that this
			# does not imply their correct or complete implementation.

			@default(System)
			Properties {
			}
			/* some properties of the target system */
			@singleton
			System : Properties {

			  @nonnull
			  string name;

			  @default(1.1)
			  f32 version;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}