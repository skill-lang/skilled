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
class TestAuto {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! auto
			# Auto compilation and structural correctness test

			/**
			 * All fields of this type are auto.
			 *
			 * @author Timm Felden
			 */
			NoSerializedData {
			  auto v64 age;
			  auto string name;
			  auto NoSerializedData someReference;
			  auto map<string, string> someMap;
			  auto list<i32> someIntegersInAList;
			  auto bool seen;
			}

			/**
			 * Check subtyping; use single fields only,
			 * because otherwise field IDs are underspecified
			 */
			A {
			  auto A a;
			}

			B : A {
			  auto B b;
			}

			C : B {
			  auto C c;
			}

			D : B {
			  auto D d;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}