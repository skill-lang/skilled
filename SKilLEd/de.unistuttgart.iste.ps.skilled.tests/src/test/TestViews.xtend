package test

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
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
 * TODO test more than just the absence of errors
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestViews {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String testView1 = "";
	var static String testView2 = "";
	var static String testView3 = "";
	var static String testView4 = "";
	var static String testView5 = "";
	var static String testView6 = "";
	var static String testView7 = "";
	
	@BeforeClass
	def static void setup() {
		testView1 = FileLoader.loadFile("TestView1");
		testView2 = FileLoader.loadFile("TestView2");
		testView3 = FileLoader.loadFile("TestView3");
		testView4 = FileLoader.loadFile("TestView4");
		testView5 = FileLoader.loadFile("TestView5");
		testView6 = FileLoader.loadFile("TestView6");
		testView7 = FileLoader.loadFile("TestView7");
	}
	
	@Test
	def void testView1() {
		val specification = testView1.parse;
		
		Assert::assertTrue(specification.validate.size == 0);
	}
	
	@Test
	def void testView2() {
		val specification = testView2.parse;
		
		Assert::assertTrue(specification.validate.size == 0);
	}
	
	@Test
	def void testView3() {
		val specification = testView3.parse;
		
		Assert::assertTrue(specification.validate.size == 0);
	}
	
	@Test
	def void testView4() {
		val specification = testView4.parse;
		
		Assert::assertTrue(specification.validate.size == 0);
	}
	
	@Test
	def void testView5() {
		val specification = testView5.parse;
		
		Assert::assertTrue(specification.validate.size == 0);
	}
	
	@Test
	def void testView6() {
		val specification = testView6.parse;
		
		Assert::assertTrue(specification.validate.size > 0);
	}
	
	@Test
	def void testView7() {
		val specification = testView7.parse;
		
		Assert::assertTrue(specification.validate.size > 0);
	}
}