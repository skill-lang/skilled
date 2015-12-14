package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.fieldrestrictions;

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

/**
 * @author Nikolay Fateev
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestDefaultFieldRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Whitebox knowledge: the handling code is divided into 3 logical groups:
	 * maps; other compound types with user-types; built in types.
	 */
	 
	//Map
	
	@Test
	def void testDefaultMapNoArgs() {
		assertFalse("A {@default Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultMapWithArgs() {
		assertFalse("A {@default(1, 2) Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	//Compound types and user types except Map
	
	@Test
	def void testDefaultSet() {
		assertFalse("A {@default Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultList() {
		assertFalse("A {@default List<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultArray() {
		assertFalse("A {@default string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultArrayWithArgs() {
		assertFalse("TypeA {@default(\"\") string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultUsertypeWithoutArgs() {
		assertTrue("A {} @singleton B : A {} C {@default(B) A a;}".parse.validate.isNullOrEmpty)
	}
	
	//Built in types
	
	@Test
	def void testDefaultString() {
		assertTrue("A {@default(\"abc\") string a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringWithoutArgs() {
		assertFalse("A {@default string a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringIntegerArg() {
		assertFalse("A {@default(1) string a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringFloatArg() {
		assertFalse("A {@default(1.0) string a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringHexArg() {
		assertFalse("A {@default(0x123B) string a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringUserTypeArg() {
		assertFalse("B {} A {B a; @default(a) string b;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringMultipleArgs() {
		assertFalse("A {@default(\"abc\", \"def\") string a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultI8IntegerArg() {
		assertTrue("A {@default(1) i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI8HexArg() {
		assertTrue("A {@default(0x123B) i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI8FloatArg() {
		assertFalse("A {@default(1.0) i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI8StringArg() {
		assertFalse("A {@default(\"\") i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI16() {
		assertTrue("A {@default(1) i16 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI32() {
		assertTrue("A {@default(1) i32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI64() {
		assertTrue("A {@default(1) i64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultV64() {
		assertTrue("A {@default(1) v64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF32FloatArg() {
		assertTrue("A {@default(1.0) f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF32HexArg() {
		assertFalse("A {@default(0x123B) f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF32IntegerArg() {
		assertFalse("A {@default(1) f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF64() {
		assertTrue("A {@default(1.0) f64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultAnnotation() {
		assertTrue("@singleton A {} B {@default(A) annotation a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultBoolean() {
		assertFalse("A {@default(\"\") bool a;}".parse.validate.isNullOrEmpty)
	}
}