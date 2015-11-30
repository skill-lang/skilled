package de.unistuttgart.iste.ps.skilled.tests.garbage;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestGarbage {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String garbage1 = "";
	var static String garbage2 = "";
	var static String garbage3 = "";
	var static String garbage4 = "";
	var static String garbage5 = "";
	
	@BeforeClass
	def static void setup() {
		garbage1 = FileLoader.loadFile("garbage/Garbage1");
		garbage2 = FileLoader.loadFile("garbage/Garbage2");
		garbage3 = FileLoader.loadFile("garbage/Garbage3");
		garbage4 = FileLoader.loadFile("garbage/Garbage4");
		garbage5 = FileLoader.loadFile("garbage/Garbage5");
	}
	
	@Test
	def void testGarbage1() {
		val issueCount = garbage1.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage2() {
		val issueCount = garbage2.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage3() {
		val issueCount = garbage3.parse.validate.size;
			
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage4() {
		val issueCount = garbage4.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage5() {
		val issueCount = garbage5.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
}