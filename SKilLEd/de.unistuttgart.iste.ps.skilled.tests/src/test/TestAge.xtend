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
class TestAge {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! age
			# Specification for TR15§8.6

			/**
			 * The age of a person.
			 * @author Timm Felden
			 */
			Age {

			 /**
			  * People have a small positive age, but maybe they
			  * will start to live longer in the future, who knows
			  */
			  @min (0)
			  v64 age;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}