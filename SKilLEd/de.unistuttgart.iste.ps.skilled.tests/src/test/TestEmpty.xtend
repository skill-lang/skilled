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
class TestEmpty {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! empty
			# This test contains no specification and can be used to check the reflective API
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}