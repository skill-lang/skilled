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
class TestEnum {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! enums

			/**
			 * Test of mapping of enums.
			 *
			 * @author Timm Felden
			 */
			enum TestEnum {
			  default, second, third, last;

			  /**
			   * an application may store a name for each enum value
			   */
			  auto string name;

			  /**
			   * a real data field to test (compilable) mapping of fields in the generated code
			   */
			  TestEnum next;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}