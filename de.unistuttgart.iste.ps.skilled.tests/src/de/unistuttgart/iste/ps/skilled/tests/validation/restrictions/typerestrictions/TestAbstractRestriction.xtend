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
class TestAbstractRestriction {
	
	@Inject extension ParseHelper<File> parser
	@Inject extension ValidationTestHelper
	
	var static String AbstractCorrect = "";
	var static String AbstractUsertypeErrorWithArgs = "";
	var static String AbstractUsertypeWarningMultiple = "";
	var static String AbstractTypedefErrorUsedOnMap = "";
	var static String AbstractTypedefErrorWithArgs = "";
	var static String AbstractTypedefWarningMultiple = "";

	@BeforeClass
	def static void setup() {
		AbstractCorrect = FileLoader.loadFile("validation/restrictions/TypeRestrictions/AbstractCorrect");
		AbstractUsertypeErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/AbstractUsertypeErrorWithArgs");
		AbstractUsertypeWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/AbstractUsertypeWarningMultiple");
		AbstractTypedefErrorUsedOnMap = FileLoader.loadFile("validation/restrictions/TypeRestrictions/AbstractTypedefErrorUsedOnMap");
		AbstractTypedefErrorWithArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/AbstractTypedefErrorWithArgs");
		AbstractTypedefWarningMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/AbstractTypedefWarningMultiple");
	}
	
	@Test
	def void testAbstractCorrect() {
		assertTrue(AbstractCorrect.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testAbstractUsertypeErrorWithArgs() {
		var issueCount = AbstractUsertypeErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testAbstractUsertypeWarningMultiple() {
		val issues = AbstractUsertypeWarningMultiple.parse.validate
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
	def void testAbstractTypedefErrorUsedOnMap() {
		var issueCount = AbstractTypedefErrorUsedOnMap.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testAbstractTypedefErrorWithArgs() {
		var issueCount = AbstractTypedefErrorWithArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testAbstractTypedefWarningMultiple() {
		val issues = AbstractTypedefWarningMultiple.parse.validate
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