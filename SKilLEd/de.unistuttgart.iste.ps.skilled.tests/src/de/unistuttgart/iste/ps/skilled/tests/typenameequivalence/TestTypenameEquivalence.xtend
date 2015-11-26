package de.unistuttgart.iste.ps.skilled.tests.typenameequivalence;

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

class TestTypenameEquivalence {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String nonEquivalent = "";
	var static String Equivalent1 = "";
	var static String Equivalent2 = "";
	var static String Equivalent3 = "";
	var static String Equivalent4 = "";

	@BeforeClass
	def static void setup() {
		nonEquivalent = FileLoader.loadFile("NonEquivalent");
		Equivalent1 = FileLoader.loadFile("Equivalent1");
		Equivalent2 = FileLoader.loadFile("Equivalent2");
		Equivalent3 = FileLoader.loadFile("Equivalent3");
		Equivalent4 = FileLoader.loadFile("Equivalent4");
	}
	
	@Test
	def void testNonEquivalentTypenames() {
		val issueCount = nonEquivalent.parse.validate.size
		
		Assert::assertTrue(issueCount == 0);
	}
	
	@Test
	def void testEquivalentTypenames1() {
		val issueCount = Equivalent1.parse.validate.size
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testEquivalentTypenames2() {
		val issueCount = Equivalent2.parse.validate.size
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testEquivalentTypenames3() {
		val issueCount = Equivalent3.parse.validate.size
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testEquivalentTypenames4() {
		val issueCount = Equivalent4.parse.validate.size
		
		Assert::assertTrue(issueCount > 0);
	}
	
	
}