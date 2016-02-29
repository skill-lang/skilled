package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.typerestrictions

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
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
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestMonotoneRestriction {
	
	@Inject extension ParseHelper<File> parser
	@Inject extension ValidationTestHelper
	
	var static String MonotoneCorrect = "";
	var static String MonotoneUsertypeErrorHasSupertype = "";	
	var static String MonotoneUsertypeErrorWithArgs = "";
	var static String MonotoneUsertypeWarningMultiple = "";
	var static String MonotoneTypedefErrorHasSupertype = "";
	var static String MonotoneTypedefErrorWithArgs = "";
	var static String MonotoneTypedefWarningMultiple = "";

	@BeforeClass
	def static void setup() {
		MonotoneCorrect = FileLoader.loadFile("validation/restrictions/TypeRestrictions/MonotoneCorrect");
		MonotoneUsertypeErrorHasSupertype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/MonotoneUsertypeErrorHasSupertype");
		MonotoneUsertypeErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/MonotoneUsertypeErrorWithArgs");
		MonotoneUsertypeWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/MonotoneUsertypeWarningMultiple");
		MonotoneTypedefErrorHasSupertype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/MonotoneTypedefErrorHasSupertype");
		MonotoneTypedefErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/MonotoneTypedefErrorWithArgs");
		MonotoneTypedefWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/MonotoneTypedefWarningMultiple");
	}
	
	@Test
	def void testMonotoneCorrect() {
		assertTrue(MonotoneCorrect.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testMonotoneUsertypeErrorHasSupertype() {
		var issueCount = MonotoneUsertypeErrorHasSupertype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testMonotoneUsertypeErrorWithArgs() {
		var issueCount = MonotoneUsertypeErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testMonotoneUsertypeWarningMultiple() {
		val issues = MonotoneUsertypeWarningMultiple.parse.validate
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
	def void testMonotoneTypedefErrorHasSupertype() {
		var issueCount = MonotoneTypedefErrorHasSupertype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testMonotoneTypedefErrorWithArgs() {
		var issueCount = MonotoneTypedefErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testMonotoneTypedefWarningMultiple() {
		val issues = MonotoneTypedefWarningMultiple.parse.validate
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