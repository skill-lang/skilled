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
class TestConstantLenghtPointerRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Whitebox knowledge: the handling code is divided into 4 logical groups:
	 * maps; other compound types; built in types; user types.
	 */
	 
	//Map
	
	@Test
	def void testConstantLenghtPointerMap() {
		assertFalse("A {@constantLenghtPointer Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerMapWithArgs() {
		assertFalse("A {@constantLenghtPointer(\"\") Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	//Compound types except Map
	
	@Test
	def void testConstantLenghtPointerSet() {
		assertFalse("A {@constantLenghtPointer Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerList() {
		assertFalse("A {@constantLenghtPointer List<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerArray() {
		assertFalse("A {@constantLenghtPointer string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerArrayWithArgs() {
		assertFalse("A {@constantLenghtPointer(\"\") string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	//User types
	
	@Test
	def void testConstantLenghtPointerUserType() {
		assertTrue("A {} B {@constantLenghtPointer A a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerUserTypeArgs() {
		assertFalse("A {} B {@constantLenghtPointer(\"\") A a;}".parse.validate.isNullOrEmpty)
	}
	
	//Built in types

	@Test
	def void testConstantLenghtPointerString() {
		assertTrue("A {@constantLenghtPointer string a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerWithArguments() {
		assertFalse("A {@constantLenghtPointer(\"\") string a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI8() {
		assertFalse("A {@constantLenghtPointer i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI16() {
		assertFalse("A {@constantLenghtPointern i16 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI32() {
		assertFalse("A {@constantLenghtPointer i32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI64() {
		assertFalse("A {@constantLenghtPointer i64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerV64() {
		assertFalse("A {@constantLenghtPointer v64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerF32() {
		assertFalse("A {@constantLenghtPointer f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerF64() {
		assertFalse("A {@constantLenghtPointer f64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerBoolean() {
		assertFalse("A {@constantLenghtPointer bool a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerAnnotation() {
		assertTrue("A {@constantLenghtPointer annotation a;}".parse.validate.isNullOrEmpty)
	}	
}