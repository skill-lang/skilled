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
class TestMaxFieldRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Whitebox knowledge: the handling code is divided into 3 logical groups:
	 * maps; other compound types with user types; built in types.
	 */
	 
	//Map
	
	@Test
	def void testMaxMapNoArgs() {
		assertFalse("A {@max Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxMapWithArgs() {
		assertFalse("A {@max(1, 2) Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	//Compound types and user types except Map
	
	@Test
	def void testMaxSet() {
		assertFalse("A {@max Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxList() {
		assertFalse("A {@max List<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxArray() {
		assertFalse("A {@max string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxArrayWithArgs() {
		assertFalse("A {@max(1) string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxUsertypeWithoutArgs() {
		assertFalse("C {@max A a;}".parse.validate.isNullOrEmpty)
	}
	
	//Built in types
	
	@Test
	def void testMaxStringWithArgs() {
		assertFalse("A {@max(1, \"Inclusive\") string a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testMaxI8SingleIntegerArg() {
		assertTrue("A {@max(1) i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8IntegerAndStringArgs() {
		assertTrue("A {@max(1, \"Inclusive\") i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8With2IntegerAndStringArgs() {
		assertFalse("A {@max(1, 2, \"Inclusive\") i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8With2IntegerArgs() {
		assertFalse("A {@max(1, 2) i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8HexAndStringArgs() {
		assertTrue("A {@max(0x123B, \"Inclusive\") i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8FloatAndStringArgs() {
		assertFalse("A {@max(1.0, \"Inclusive\") i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI16() {
		assertTrue("A {@max(1, \"Inclusive\") i16 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI32() {
		assertTrue("A {@max(1, \"Inclusive\") i32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI64() {
		assertTrue("A {@max(1, \"Inclusive\") i64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxV64() {
		assertTrue("A {@max(1, \"Inclusive\") v64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32SingleFloatArg() {
		assertTrue("A {@max(1.0) f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32FloatAndStringArgs() {
		assertTrue("A {@max(1.0, \"Inclusive\") f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32With2FloatAndStringArgs() {
		assertFalse("A {@max(1.0, 2.0, \"Inclusive\") f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32HexArgs() {
		assertFalse("A {@max(0x123B, \"Inclusive\") f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32IntegerArgs() {
		assertFalse("A {@max(1, \"Inclusive\") f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF64() {
		assertTrue("A {@max(1.0) f64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxBoolean() {
		assertFalse("A {@max(1, \"Inclusive\") bool a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxAnnotation() {
		assertFalse("A {@max(1, \"Inclusive\") annotation a;}".parse.validate.isNullOrEmpty)
	}
}