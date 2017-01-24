package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.typerestrictions

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SkillInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.File
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

/**
 * 
 * @author Nikolay Fateev
 */
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestSingletonRestriction {
	
	@Inject extension ParseHelper<File> parser
	@Inject extension ValidationTestHelper
	
	var static String SingletonCorrect = "";
	var static String SingletonUsertypeErrorHasSubtype = "";
	var static String SingletonUsertypeErrorWithArgs = "";
	var static String SingletonUsertypeWarningMultiple = "";
	var static String SingletonTypedefErrorHasSubtype = "";
	var static String SingletonTypedefErrorWithArgs = "";
	var static String SingletonTypedefWarningMultiple = "";

	@BeforeClass
	def static void setup() {
		SingletonCorrect = FileLoader.loadFile("validation/restrictions/TypeRestrictions/SingletonCorrect");
		SingletonUsertypeErrorHasSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/SingletonUsertypeErrorHasSubtype");
		SingletonUsertypeErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/SingletonUsertypeErrorWithArgs");
		SingletonUsertypeWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/SingletonUsertypeWarningMultiple");
		SingletonTypedefErrorHasSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/SingletonTypedefErrorHasSubtype");
		SingletonTypedefErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/SingletonTypedefErrorWithArgs");
		SingletonTypedefWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/SingletonTypedefWarningMultiple");
	}
	
	@Test
	def void testSingletonCorrect() {
		assertTrue(SingletonCorrect.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testSingletonUsertypeErrorHasSubtype() {
		var issueCount = SingletonUsertypeErrorHasSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testSingletonUsertypeErrorWithArgs() {
		var issueCount = SingletonUsertypeErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testSingletonUsertypeWarningMultiple() {
		val issues = SingletonUsertypeWarningMultiple.parse.validate
		val issueCount = issues.size
		var errorCount = 0
		// Count the number of Errors, not Warnings
		for (issue : issues){
			if ("ERROR".equals(issue.severity.toString())) {
				errorCount++
			}
		}	
		assertTrue(errorCount == 0)
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testSingletonTypedefErrorHasSubtype() {
		var issueCount = SingletonTypedefErrorHasSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testSingletonTypedefErrorWithArgs() {
		var issueCount = SingletonTypedefErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testSingletonTypedefWarningMultiple() {
		val issues = SingletonTypedefWarningMultiple.parse.validate
		val issueCount = issues.size
		var errorCount = 0
		// Count the number of Errors, not Warnings
		for (issue : issues){
			if ("ERROR".equals(issue.severity.toString())) {
				errorCount++
			}
		}	
		assertTrue(errorCount == 0)
		assertTrue(issueCount > 0)
	}
}