package test;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;

import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider;
import de.unistuttgart.iste.ps.skilled.sKilL.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.BeforeClass

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestGarbage3 {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String garbage11 = "";
	var static String garbage12 = "";
	var static String garbage13 = "";
	
	@BeforeClass
	def static void setup() {
		garbage11 = FileLoader.loadFile("Garbage11");
		garbage12 = FileLoader.loadFile("Garbage12");
		garbage13 = FileLoader.loadFile("Garbage13");
	}
	
	@Test
	def void testGarbage11() {
		val issueCount = garbage11.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage12() {
		val issueCount = garbage12.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage13() {
		val issueCount = garbage13.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}

}