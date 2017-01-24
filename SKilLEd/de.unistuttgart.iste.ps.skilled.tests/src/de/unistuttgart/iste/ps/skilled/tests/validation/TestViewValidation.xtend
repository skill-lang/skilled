package de.unistuttgart.iste.ps.skilled.tests.validation

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
 * This Class tests View Validation from the ViewValidator.
 * @author Jan Berberich 
 */
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestViewValidation {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	var static String noError1 = "";
	var static String noError2 = "";
	var static String noError3 = "";
	var static String noError4 = "";
	var static String errorTypeNotASupertype = "";
	var static String errorFieldNotASupertype = "";
	var static String errorVarInSupertypeNotExisting = "";
	var static String errorSupertypeNotExisting = "";
	var static String errorVarInSameType = "";
	var static String errorNotAUsertype = "";
	var static String errorNotAUsertype2 = "";
	
	@BeforeClass
	def static void setup() {
		noError1 = FileLoader.loadFile("views/noError1");
		noError2 = FileLoader.loadFile("views/noError2");
		noError3 = FileLoader.loadFile("views/noError3");
		noError4 = FileLoader.loadFile("views/noError4");
		errorTypeNotASupertype = FileLoader.loadFile("views/errorTypeNotASupertype");
		errorFieldNotASupertype = FileLoader.loadFile("views/errorFieldNotASupertype");
		errorVarInSupertypeNotExisting = FileLoader.loadFile("views/errorVarInSupertypeNotExisting");
		errorSupertypeNotExisting = FileLoader.loadFile("views/errorSupertypeNotExisting");
		errorVarInSameType = FileLoader.loadFile("views/errorVarInSameType");
		errorNotAUsertype = FileLoader.loadFile("views/errorNotAUsertype");
		errorNotAUsertype2 = FileLoader.loadFile("views/errorNotAUsertype2");
	}

	@Test
	def void testNoError1() {
		assertTrue(noError1.parse.validate.isNullOrEmpty)

	}
	
	@Test
	def void testNoError2() {
		assertTrue(noError2.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNoError3() {
		assertTrue(noError3.parse.validate.isNullOrEmpty)
	}
	
		
	@Test
	def void testNoError4() {
		assertTrue(noError4.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorTypeNotASupertype() {
		assertFalse(errorTypeNotASupertype.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorFieldNotASupertype() {
		assertFalse(errorFieldNotASupertype.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorVarInSupertypeNotExisting() {
		assertFalse(errorVarInSupertypeNotExisting.parse.validate.isNullOrEmpty)
	}
	@Test
	def void testErrorSupertypeNotExisting() {
		assertFalse(errorSupertypeNotExisting.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorVarInSameType() {
		assertFalse(errorVarInSameType.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testErrorNotAUsertype() {
		assertFalse(errorNotAUsertype.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorNotAUsertype2() {
		assertFalse(errorNotAUsertype2.parse.validate.isNullOrEmpty)
	}

}