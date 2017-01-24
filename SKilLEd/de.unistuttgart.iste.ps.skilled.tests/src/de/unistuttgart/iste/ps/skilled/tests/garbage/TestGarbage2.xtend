package de.unistuttgart.iste.ps.skilled.tests.garbage;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SkillInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.File
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
@InjectWith(SkillInjectorProvider)
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
		garbage6 = FileLoader.loadFile("garbage/Garbage6");
		garbage7 = FileLoader.loadFile("garbage/Garbage7");
		garbage8 = FileLoader.loadFile("garbage/Garbage8");
		garbage9 = FileLoader.loadFile("garbage/Garbage9");
		garbage10 = FileLoader.loadFile("garbage/Garbage10");
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