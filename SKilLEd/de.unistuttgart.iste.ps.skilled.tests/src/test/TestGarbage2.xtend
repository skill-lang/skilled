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
class TestGarbage2 {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String garbage6 = "";
	var static String garbage7 = "";
	var static String garbage8 = "";
	var static String garbage9 = "";
	var static String garbage10 = "";
	
	@BeforeClass
	def static void setup() {
		garbage6 = FileLoader.loadFile("Garbage6");
		garbage7 = FileLoader.loadFile("Garbage7");
		garbage8 = FileLoader.loadFile("Garbage8");
		garbage9 = FileLoader.loadFile("Garbage9");
		garbage10 = FileLoader.loadFile("Garbage10");
	}
	
	@Test
	def void testGarbage6() {
		val issueCount = garbage6.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage7() {
		val issueCount = garbage7.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage8() {
		val issueCount = garbage8.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage9() {
		val issueCount = garbage9.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage10() {
		val issueCount = garbage10.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
}