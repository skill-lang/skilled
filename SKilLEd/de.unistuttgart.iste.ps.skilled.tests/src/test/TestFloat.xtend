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
class TestFloat {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! floats

			/** check some float values. */
			@singleton
			FloatTest {
			  f32 zero;
			  f32 minusZero;
			  f32 two;
			  f32 pi;
			  f32 NaN;
			}

			/** check some double values. */
			@singleton
			DoubleTest {
			  f64 zero;
			  f64 minusZero;
			  f64 two;
			  f64 pi;
			  f64 NaN;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}