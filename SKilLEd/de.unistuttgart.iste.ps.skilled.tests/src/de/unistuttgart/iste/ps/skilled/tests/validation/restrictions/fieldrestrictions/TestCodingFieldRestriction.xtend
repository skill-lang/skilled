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
class TestCodingFieldRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Whitebox knowledge: the handling code is divided into 2 logical groups:
	 * maps; other compound types and built in types and user types.
	 */
	 
	//Map
	
	@Test
	def void testCodingMapNoArgs() {
		assertFalse("A {@coding Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingMapWithArgs() {
		assertFalse("A {@coding(\"zip\") Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	//Other compound types and built in types and user types.
	
	@Test
	def void testCodingSet() {
		assertTrue("A {@coding(\"zip\") Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetNoArgs() {
		assertFalse("A {@coding Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetIntegerArg() {
		assertFalse("A {@coding(1) Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetHexArg() {
		assertFalse("A {@coding(0x123B) Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetUserTypeArg() {
		assertFalse("A {} B {A a; @coding(b) Set<string> b;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetFloatArg() {
		assertFalse("A {@coding(1.0) Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetWith2StringArgs() {
		assertFalse("A {@coding(\"zip\", \"zip\") Set<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingList() {
		assertTrue("A {@coding(\"zip\") List<string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingArray() {
		assertTrue("A {@coding(\"zip\") string[] a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingUsertype() {
		assertTrue("A {@coding(\"zip\") A a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingString() {
		assertTrue("A {@coding(\"zip\") string a;}".parse.validate.isNullOrEmpty)
	}		
	
	@Test
	def void testCodingI8() {
		assertTrue("A {@coding(\"zip\") i8 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingI16() {
		assertTrue("A {@coding(\"zip\") i16 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingI32() {
		assertTrue("A {@coding(\"zip\") i32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingI64() {
		assertTrue("A {@coding(\"zip\") i64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingV64() {
		assertTrue("A {@coding(\"zip\") v64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingF32() {
		assertTrue("A {@coding(\"zip\") f32 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingF64() {
		assertTrue("A {@coding(\"zip\") f64 a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingBoolean() {
		assertTrue("A {@coding(\"zip\") bool a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingAnnotation() {
		assertTrue("A {@coding(\"zip\") annotation a;}".parse.validate.isNullOrEmpty)
	}
}