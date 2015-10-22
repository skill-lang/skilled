package test;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;

import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider;
import de.unistuttgart.iste.ps.skilled.sKilL.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestGarbage2 {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	@Test
	def void testGarbage6() {
		val issueCount = '''
			Type {
				i32[][] twoDimensionalArray;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage7() {
		val issueCount = '''
			MathConstants {
				//approximately
				i32 pi = 3;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage8() {
		val issueCount = '''
			Type {}
			# HEADCOMMENT
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage9() {
		val issueCount = '''
			A {}
			interface abc : A;
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testGarbage10() {
		val issueCount = '''
			Type {
				@max((50)
				string name;
			}
		'''.parse.validate.size;
		
		Assert::assertTrue(issueCount > 0);
	}
	
}