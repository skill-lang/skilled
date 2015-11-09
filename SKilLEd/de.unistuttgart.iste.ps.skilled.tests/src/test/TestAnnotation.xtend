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
class TestAnnotation {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! annotation
			# checks for correct implementation of annotation field types.

			/** Test the implementation of annotations. */
			Test {

			  /** can point to anything, there are binary files exlpoiting this property. */
			  annotation f;
			}

			/** A simple date test with known Translation */
			Date {

			  /** seconds since 1.1.1970 UTC */
			  v64 date;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}