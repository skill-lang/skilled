package de.unistuttgart.iste.ps.skilled.tests.validation;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.validation.SKilLValidator
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.BeforeClass
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader

/**
 * TODO Comment everything
 * @author Marco Link
 * @author Armin Hüneburg
 * @author Moritz Platzer
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
public class TestSKilLValidator {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	var static String integerConstantsValid = "";
	var static String integerConstantsInvalidWithBuiltInType = "";
	var static String integerConstantsInvalidWithUserType = "";
	var static String nestedListInTypedef = "";
	var static String nestedListAsField = "";
	var static String nestedArrayInTypedef = "";
	var static String nestedArrayAsField = "";
	var static String nestedSetAsField = "";
	var static String nestedMapAsField = "";
	var static String allGood = "";
	
	@BeforeClass
	def static void setup() {
		integerConstantsValid = FileLoader.loadFile("validation/integerConstantsValid");
		integerConstantsInvalidWithBuiltInType = FileLoader.loadFile("validation/integerConstantsInvalidWithBuiltInType");
		integerConstantsInvalidWithUserType = FileLoader.loadFile("validation/integerConstantsInvalidWithUserType");
		nestedListInTypedef = FileLoader.loadFile("validation/nestedListInTypedef");
		nestedListAsField = FileLoader.loadFile("validation/nestedListAsField");
		nestedArrayInTypedef = FileLoader.loadFile("validation/nestedArrayInTypedef");
		nestedArrayAsField = FileLoader.loadFile("validation/nestedArrayAsField");
		nestedSetAsField = FileLoader.loadFile("validation/nestedSetAsField");
		nestedMapAsField = FileLoader.loadFile("validation/nestedMapAsField");
		allGood = FileLoader.loadFile("validation/allGood");
	}

	@Test
	def void testIntegerConstantsValid() {
		integerConstantsValid.parse.assertNoError("All's Good")
	}

	@Test
	def void testIntegerConstantsInvalidWithBuiltInType() {
		integerConstantsInvalidWithBuiltInType.parse.assertError(SKilLPackage::eINSTANCE.constant, SKilLValidator::INVALID_CONSTANT_TYPE,
			"Only an Integer can be constant.")
	}

	@Test
	def void testIntegerConstantsInvalidWithUserType() {
		integerConstantsInvalidWithUserType.parse.assertError(SKilLPackage::eINSTANCE.constant, SKilLValidator::INVALID_CONSTANT_TYPE,
			"Only an Integer can be constant.")
	}

	@Test
	def void testNestedListInTypedef() {
		nestedListInTypedef.parse.assertError(SKilLPackage::eINSTANCE.declarationReference, SKilLValidator::INVALID_NESTED_TYPEDEF,
			"It is forbidden to nest containers inside of other containers.")

	}

	@Test
	def void testNestedListAsField() {
		nestedListAsField.parse.assertError(SKilLPackage::eINSTANCE.declarationReference, SKilLValidator::INVALID_NESTED_TYPEDEF,
			"It is forbidden to nest containers inside of other containers.")

	}

	@Test
	def void testNestedArrayInTypedef() {
		nestedArrayInTypedef.parse.assertError(SKilLPackage::eINSTANCE.declarationReference, SKilLValidator::INVALID_NESTED_TYPEDEF,
			"It is forbidden to nest containers inside of other containers.")

	}

	@Test
	def void testNestedArrayAsField() {
		nestedArrayAsField.parse.assertError(SKilLPackage::eINSTANCE.declarationReference, SKilLValidator::INVALID_NESTED_TYPEDEF,
			"It is forbidden to nest containers inside of other containers.")

	}

	@Test
	def void testNestedSetAsField() {
		nestedSetAsField.parse.assertError(SKilLPackage::eINSTANCE.declarationReference, SKilLValidator::INVALID_NESTED_TYPEDEF,
			"It is forbidden to nest containers inside of other containers.")

	}

	@Test
	def void testNestedMapAsField() {
		nestedMapAsField.parse.assertError(SKilLPackage::eINSTANCE.declarationReference, SKilLValidator::INVALID_NESTED_TYPEDEF,
			"It is forbidden to nest containers inside of other containers.")

	}

	@Test
	def void testAllGood() {
		allGood.parse.assertNoError("All's good")

	}

}
