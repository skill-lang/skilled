package de.unistuttgart.iste.ps.skilled.tests;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.eclipse.xtext.junit4.validation.ValidationTestHelper

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestIncorrectViews {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;

	var static String view1 = "";
	var static String view2 = "";
	var static String view3 = "";
	var static String view4 = "";
	var static String view5 = "";
	var static String view6 = "";
	var static String view7 = "";

	@BeforeClass
	def static void setup() {
		view1 = FileLoader.loadFile("views/TestIncorrectView1");
		view2 = FileLoader.loadFile("views/TestIncorrectView2");
		view3 = FileLoader.loadFile("views/TestIncorrectView3");
		view4 = FileLoader.loadFile("views/TestIncorrectView4");
		view5 = FileLoader.loadFile("views/TestIncorrectView5");
		view6 = FileLoader.loadFile("views/TestIncorrectView6");
		view7 = FileLoader.loadFile("views/TestIncorrectView7");
	}

	@Test
	def void testIncorrectView1() {
		val specification = view1.parse
		
		Assert::assertTrue(specification.validate.size > 0);
	}

	@Test
	def void testIncorrectView2() {
		val specification = view2.parse
		
		Assert::assertTrue(specification.validate.size > 0);
	}

	@Test
	def void testIncorrectView3() {
		val specification = view3.parse
		
		Assert::assertTrue(specification.validate.size > 0);
	}

	@Test
	def void testIncorrectView4() {
		val specification = view4.parse
		
		Assert::assertTrue(specification.validate.size > 0);
	}

	@Test
	def void testIncorrectView5() {
		val specification = view5.parse
		
		Assert::assertTrue(specification.validate.size > 0);
	}

	@Test
	def void testIncorrectView6() {
		val specification = view6.parse
		
		Assert::assertTrue(specification.validate.size > 0);
	}

	@Test
	def void testIncorrectView7() {
		val specification = view7.parse
		
		Assert::assertTrue(specification.validate.size > 0);
	}
	
}