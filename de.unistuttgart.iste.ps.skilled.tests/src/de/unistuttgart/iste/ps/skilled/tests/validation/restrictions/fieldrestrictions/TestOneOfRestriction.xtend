package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.fieldrestrictions;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SkillInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.File
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

/**
 * @author Nikolay Fateev
 * @author Tobias Heck
 */
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestOneOfRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Whitebox knowledge: the handling code is divided into 4 logical groups:
	 * maps; other compound types; built in types; user types.
	 */
	 
	//Map
	
	@Test
	def void testOneOfMap() {
		assertFalse("A {} B {@oneOf(A) Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfMapNoArgs() {
		assertFalse("A {@oneOf Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	//Compound types except Map
	
	@Test
	def void testOneOfSet() {
		assertFalse("A {} B {@oneOf(A) Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfList() {
		assertFalse("A {} B {@oneOf(A) List<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfArray() {
		assertFalse("A {} B {@oneOf(A) string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfArrayNoArgs() {
		assertFalse("A {@oneOf string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	//User types
	
	@Test
	def void testOneOfUsertypeCorrectArgument() {
		assertTrue("A {} B : A {} C {@oneOf(B) A a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfUsertypeMultipleCorrectArguments() {
		assertTrue("A {} B : A {} D : A {} C {@oneOf(B, D) A a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfUsertypeCorrectAndIncorrectArguments() {
		assertFalse("A {} B : A {} D : A {} C {@oneOf(B, 1) A a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfUsertypeIncorrectArgument() {
		assertFalse("C {@oneOf(1) A a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfNoArgument() {
		assertFalse("C {@oneOf A a;}".parse.validate.isNullOrEmpty)
	}
	
	//Built in types

	@Test
	def void testOneOfI8() {
		assertFalse("A {} B {@oneOf(A) i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfI16() {
		assertFalse("A {} B {@oneOf(A) i16 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfI32() {
		assertFalse("A {} B {@oneOf(A) i32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfI64() {
		assertFalse("A {} B {@oneOf(A) i64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfV64() {
		assertFalse("A {} B {@oneOf(A) v64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfF32() {
		assertFalse("A {} B {@oneOf(A) f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfF64() {
		assertFalse("A {} B {@oneOf(A) f64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfString() {
		assertFalse("A {} B {@oneOf(A) string a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfBoolean() {
		assertFalse("A {} B {@oneOf(A) bool a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfAnnotation() {
		assertTrue("A {} B {@oneOf(A) annotation a;}".parse.validate.isNullOrEmpty)
	}
	
}