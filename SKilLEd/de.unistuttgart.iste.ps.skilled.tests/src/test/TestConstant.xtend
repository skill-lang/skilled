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
class TestConstant {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! constants

			/**
			 * Check for constant integerers.
			 *
			 * @author Dennis Przytarski
			 */
			Constant {
				const i8 a = 8;
				const i16 b = 16;
				const i32 c = 32;
				const i64 d = 64;
				const v64 e = 46;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}