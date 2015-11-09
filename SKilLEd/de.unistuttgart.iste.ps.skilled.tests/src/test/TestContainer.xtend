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
class TestContainer {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! container

			Container { 
			  v64[3] arr;
			  v64[] varr;
			  list<v64> l;
			  set<v64> s;
			  map<string, v64, v64> f;

			  set<SomethingElse> someSet;
			}

			/** no instance of this is required */
			SomethingElse{
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}