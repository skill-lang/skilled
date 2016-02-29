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
 * 
 * @author Nikolay Fateev
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestDefaultRestriction {
	
	@Inject extension ParseHelper<File> parser
	@Inject extension ValidationTestHelper
	
	var static String DefaultCorrect = "";
	var static String DefaultUsertypeErrorArgUsertypeNotSubtype = "";
	var static String DefaultUsertypeErrorArgUsertypeNotSingletonSubtype = "";
	var static String DefaultUsertypeErrorArgTypedefNotSubtype = "";
	var static String DefaultUsertypeErrorArgTypedefNotSingletonSubtype = "";
	var static String DefaultUsertypeErrorArgEnum = "";
	var static String DefaultUsertypeErrorArgInterface = "";
	var static String DefaultUsertypeErrorNoArgs = "";
	var static String DefaultUsertypeErrorMultiple = "";
	var static String DefaultTypedefErrorArgUsertypeNotSubtype = "";
	var static String DefaultTypedefErrorArgUsertypeNotSingletonSubtype = "";
	var static String DefaultTypedefErrorArgTypedefNotSubtype = "";
	var static String DefaultTypedefErrorArgTypedefNotSingletonSubtype = "";
	var static String DefaultTypedefErrorArgEnum = "";
	var static String DefaultTypedefErrorArgInterface = "";
	var static String DefaultTypedefErrorNoArgs = "";
	var static String DefaultTypedefErrorMultiple = "";

	@BeforeClass
	def static void setup() {
		DefaultCorrect = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultCorrect");
		DefaultUsertypeErrorArgUsertypeNotSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorArgUsertypeNotSubtype");
		DefaultUsertypeErrorArgUsertypeNotSingletonSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorArgUsertypeNotSingletonSubtype");
		DefaultUsertypeErrorArgTypedefNotSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorArgTypedefNotSubtype");
		DefaultUsertypeErrorArgTypedefNotSingletonSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorArgTypedefNotSingletonSubtype");
		DefaultUsertypeErrorArgEnum = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorArgEnum");
		DefaultUsertypeErrorArgInterface = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorArgInterface");
		DefaultUsertypeErrorNoArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorMultiple");
		DefaultUsertypeErrorMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultUsertypeErrorMultiple");
		DefaultTypedefErrorArgUsertypeNotSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorArgUsertypeNotSubtype");
		DefaultTypedefErrorArgUsertypeNotSingletonSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorArgUsertypeNotSingletonSubtype");
		DefaultTypedefErrorArgTypedefNotSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorArgTypedefNotSubtype");
		DefaultTypedefErrorArgTypedefNotSingletonSubtype = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorArgTypedefNotSingletonSubtype");
		DefaultTypedefErrorArgEnum = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorArgEnum");
		DefaultTypedefErrorArgInterface = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorArgInterface");
		DefaultTypedefErrorNoArgs = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorNoArgs");
		DefaultTypedefErrorMultiple = FileLoader.loadFile("validation/restrictions/TypeRestrictions/DefaultTypedefErrorMultiple");
	}
	
	@Test
	def void testDefaultCorrect() {
		assertTrue(DefaultCorrect.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultUsertypeErrorArgUsertypeNotSubtype() {
		var issueCount = DefaultUsertypeErrorArgUsertypeNotSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultUsertypeErrorArgUsertypeNotSingletonSubtype() {
		var issueCount = DefaultUsertypeErrorArgUsertypeNotSingletonSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultUsertypeErrorArgTypedefNotSubtype() {
		var issueCount = DefaultUsertypeErrorArgTypedefNotSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultUsertypeErrorArgTypedefNotSingletonSubtype() {
		var issueCount = DefaultUsertypeErrorArgTypedefNotSingletonSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	// White box knowledge: the outer if() accepts Enums
	@Test
	def void testDefaultUsertypeErrorArgEnum() {
		var issueCount = DefaultUsertypeErrorArgEnum.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	// White box knowledge: the outer if() accepts Interfaces
	@Test
	def void testDefaultUsertypeErrorArgInterface() {
		var issueCount = DefaultUsertypeErrorArgInterface.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultUsertypeErrorNoArgs() {
		var issueCount = DefaultUsertypeErrorNoArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultUsertypeErrorMultiple() {
		val issueCount = DefaultUsertypeErrorMultiple.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultTypedefErrorArgUsertypeNotSubtype() {
		var issueCount = DefaultTypedefErrorArgUsertypeNotSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultTypedefErrorArgUsertypeNotSingletonSubtype() {
		var issueCount = DefaultTypedefErrorArgUsertypeNotSingletonSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultTypedefErrorArgTypedefNotSubtype() {
		var issueCount = DefaultTypedefErrorArgTypedefNotSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultTypedefErrorArgTypedefNotSingletonSubtype() {
		var issueCount = DefaultTypedefErrorArgTypedefNotSingletonSubtype.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultTypedefErrorArgEnum() {
		var issueCount = DefaultTypedefErrorArgEnum.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultTypedefErrorArgInterface() {
		var issueCount = DefaultTypedefErrorArgInterface.parse.validate.size
		assertTrue(issueCount > 0)
	}
	@Test
	def void testDefaultTypedefErrorNoArgs() {
		var issueCount = DefaultTypedefErrorNoArgs.parse.validate.size
		assertTrue(issueCount > 0)
	}
	
	@Test
	def void testDefaultTypedefErrorMultiple() {
		val issueCount = DefaultTypedefErrorMultiple.parse.validate.size
		assertTrue(issueCount > 0)
	}
}