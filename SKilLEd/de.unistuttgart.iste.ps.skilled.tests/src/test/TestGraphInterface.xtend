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
class TestGraphInterface {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void test() {
		val issueCount = '''
			#! graphInterface
			#
			# This test is mostly equivalent to graph, but checks interfacing and subtyping
			# capabilities of a generated binding.

			@abstract
			AbstractNode {
			  set<Node> edges;
			}

			/** The property of being a colored Node */
			interface ColoredNode : AbstractNode with Colored {
			}

			/** a graph of colored nodes */
			Node : ColoredNode {
			}

			/** anything that has a color is colored */
			interface Colored {
			  string color;
			}

			/** check that abstract colors are in fact annotations */
			ColorHolder {
			  Colored anAnnotation;
			  ColoredNode anAbstractNode;
			}		
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount == 0);
	}
}