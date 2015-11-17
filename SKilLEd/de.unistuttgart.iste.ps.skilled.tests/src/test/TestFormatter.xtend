package test

import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import com.google.inject.Inject
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.formatting.INodeModelFormatter
import org.eclipse.xtext.resource.XtextResource

import static extension org.junit.Assert.*
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import org.junit.Test
import org.junit.BeforeClass
import de.unistuttgart.iste.ps.skilled.sKilL.File

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(SKilLInjectorProvider))
class TestFormatter {

	@Inject extension ParseHelper<File>
	@Inject extension INodeModelFormatter
	
	var static String formatted = "";
	var static String unformatted = "";
	
	@BeforeClass
	def static void setup() {
		formatted = FileLoader.loadFile("Formatted");
		unformatted = FileLoader.loadFile("Formatted");
	}
	
	def void assertFormattedAs(CharSequence input, CharSequence expected) {
		println(input.parse)
		expected.toString.assertEquals(
		(input.parse.eResource as XtextResource).parseResult.rootNode.
		format(0, input.length).formattedText)
	}
	
	@Test
	def void testEntities() {
		assertFormattedAs(unformatted, formatted)
	}

}