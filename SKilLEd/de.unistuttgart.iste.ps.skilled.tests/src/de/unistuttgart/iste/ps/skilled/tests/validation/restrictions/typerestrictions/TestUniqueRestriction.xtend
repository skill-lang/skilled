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
 * @author Nikolay Fateev
 */
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestUniqueRestriction {
	
	@Inject extension ParseHelper<File> parser
	@Inject extension ValidationTestHelper
	
	var static String UniqueCorrect = "";
	var static String UniqueUsertypeErrorHasSubtype = "";
	var static String UniqueUsertypeErrorHasSupertype = "";	
	var static String UniqueUsertypeErrorWithArgs = "";
	var static String UniqueUsertypeWarningMultiple = "";
	var static String UniqueTypedefErrorHasSubtype = "";
	var static String UniqueTypedefErrorHasSupertype = "";
	var static String UniqueTypedefErrorWithArgs = "";
	var static String UniqueTypedefWarningMultiple = "";

	@BeforeClass
	def static void setup() {
		UniqueCorrect = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueCorrect");
		UniqueUsertypeErrorHasSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueUsertypeErrorHasSubtype");
		UniqueUsertypeErrorHasSupertype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueUsertypeErrorHasSupertype");
		UniqueUsertypeErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueUsertypeErrorWithArgs");
		UniqueUsertypeWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueUsertypeWarningMultiple");
		UniqueTypedefErrorHasSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueTypedefErrorHasSubtype");
		UniqueTypedefErrorHasSupertype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueTypedefErrorHasSupertype");
		UniqueTypedefErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueTypedefErrorWithArgs");
		UniqueTypedefWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueTypedefWarningMultiple");
	}
	
	@Test
	def void testUniqueCorrect() {
		assertTrue(UniqueCorrect.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testUniqueUsertypeErrorHasSubtype() {
		var issueCount = UniqueUsertypeErrorHasSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	
	@Test
	def void testUniqueUsertypeErrorHasSupertype() {
		var issueCount = UniqueUsertypeErrorHasSupertype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueUsertypeErrorWithArgs() {
		var issueCount = UniqueUsertypeErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueUsertypeWarningMultiple() {
		val issues = UniqueUsertypeWarningMultiple.parse.validate
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
	def void testUniqueTypedefErrorHasSupertype() {
		var issueCount = UniqueTypedefErrorHasSupertype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueTypedefErrorHasSubtype() {
		var issueCount = UniqueTypedefErrorHasSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueTypedefErrorWithArgs() {
		var issueCount = UniqueTypedefErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueTypedefWarningMultiple() {
		val issues = UniqueTypedefWarningMultiple.parse.validate
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