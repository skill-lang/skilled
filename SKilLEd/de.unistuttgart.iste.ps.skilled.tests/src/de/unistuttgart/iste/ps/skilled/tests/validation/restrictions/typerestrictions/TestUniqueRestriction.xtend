package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.typerestrictions

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*
import org.junit.BeforeClass
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader

/**
 * @author Nikolay Fateev
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestUniqueRestriction {
	
	@Inject extension ParseHelper<File> parser
	@Inject extension ValidationTestHelper
	
	var static String UniqueRestrictionCorrect = "";
	var static String UniqueRestrictionErrorHasSubtype = "";
	var static String UniqueRestrictionErrorHasSupertype = "";
	var static String UniqueRestrictionErrorWithArgs = "";
	var static String UniqueRestrictionWarningMultiple = "";

	@BeforeClass
	def static void setup() {
		UniqueRestrictionCorrect = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueRestrictionCorrect");
		UniqueRestrictionErrorHasSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueRestrictionErrorHasSubtype");
		UniqueRestrictionErrorHasSupertype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueRestrictionErrorHasSupertype");
		UniqueRestrictionErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueRestrictionErrorWithArgs");
		UniqueRestrictionWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/UniqueRestrictionWarningMultiple");
	}
	
	@Test
	def void testUniqueRestrictionCorrect() {
		assertTrue(UniqueRestrictionCorrect.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testUniqueRestrictionErrorHasSubtype() {
		var issueCount = UniqueRestrictionErrorHasSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueRestrictionErrorHasSupertype() {
		var issueCount = UniqueRestrictionErrorHasSupertype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueRestrictionErrorWithArgs() {
		var issueCount = UniqueRestrictionErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testUniqueRestrictionWarningMultiple() {
		val issues = UniqueRestrictionWarningMultiple.parse.validate
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