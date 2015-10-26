package test.restrictions.fieldrestrictions
import static org.junit.Assert.*;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;

import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider;
import de.unistuttgart.iste.ps.skilled.sKilL.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper

/**
 * @author Nikolay Fateev
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
		assertFalse('''
			TypeA {
				@coding 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingMapWithArgs() {
		assertFalse('''
			TypeA {
				@coding("zip")
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Other compound types and built in types and user types.
	
	@Test
	def void testCodingSet() {
		assertTrue('''
			TypeA {
				@coding("zip")
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetNoArgs() {
		assertFalse('''
			TypeA {
				@coding
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetIntegerArg() {
		assertFalse('''
			TypeA {
				@coding(1)
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetHexArg() {
		assertFalse('''
			TypeA {
				@coding(0x123B)
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetUserTypeArg() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				TypeA a;
				
				@coding(b)
				Set<string> b;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetFloatArg() {
		assertFalse('''
			TypeA {
				@coding(1.0)
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingSetWith2StringArgs() {
		assertFalse('''
			TypeA {
				@coding("zip", "zip")
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingList() {
		assertTrue('''
			TypeA {
				@coding("zip")
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingArray() {
		assertTrue('''
			TypeA {
				@coding("zip")
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingUsertype() {
		assertTrue('''
			TypeA {
				@coding("zip")
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingString() {
		assertTrue('''
			TypeA {
				@coding("zip")
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}		
	
	@Test
	def void testCodingI8() {
		assertTrue('''
			TypeA {
				@coding("zip") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingI16() {
		assertTrue('''
			TypeA {
				@coding("zip") 
				i16 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingI32() {
		assertTrue('''
			TypeA {
				@coding("zip")  
				i32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingI64() {
		assertTrue('''
			TypeA {
				@coding("zip")  
				i64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingV64() {
		assertTrue('''
			TypeA {
				@coding("zip")  
				v64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingF32() {
		assertTrue('''
			TypeA {
				@coding("zip")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingF64() {
		assertTrue('''
			TypeA {
				@coding("zip")
				f64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingBoolean() {
		assertTrue('''
			TypeA {
				@coding("zip") 
				bool a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testCodingAnnotation() {
		assertTrue('''
			TypeA {
				@coding("zip") 
				annotation a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
}