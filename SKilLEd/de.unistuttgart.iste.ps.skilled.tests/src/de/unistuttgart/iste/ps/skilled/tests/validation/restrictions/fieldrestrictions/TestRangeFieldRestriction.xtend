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
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestRangeFieldRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Whitebox knowledge: the handling code is divided into 3 logical groups:
	 * maps; other compound types with user types; built in types.
	 */
	 
	//Map
	
	@Test
	def void testRangeMapNoArgs() {
		assertFalse('''
			TypeA {
				@range 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeMapWithArgs() {
		assertFalse('''
			TypeA {
				@range(1, 2)
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Compound types and user types except Map
	
	@Test
	def void testRangeSet() {
		assertFalse('''
			TypeA {
				@range 
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeList() {
		assertFalse('''
			TypeA {
				@range 
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeArray() {
		assertFalse('''
			TypeA {
				@range 
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeArrayWithArgs() {
		assertFalse('''
			TypeA {
				@range(1, 2)
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeUsertypeWithoutArgs() {
		assertFalse('''
			TypeC {
				@range
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Built in types
	
	@Test
	def void testRangeStringWithArgs() {
		assertFalse('''
			TypeA {
				@range(1, 2) 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testRangeI8WithTwoIntegerArgs() {
		assertTrue('''
			TypeA {
				@range(1, 2) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8WithIntegerAndStringArgs() {
		assertFalse('''
			TypeA {
				@range(1, "") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8With2IntegerAnd1StringArgs() {
		assertFalse('''
			TypeA {
				@range(1, 2, "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8With2IntegerAnd2StringArgs() {
		assertTrue('''
			TypeA {
				@range(1, 2, "inclusive", "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8With3IntegerArgs() {
		assertFalse('''
			TypeA {
				@range(1, 2, 3) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8With2HexArgs() {
		assertTrue('''
			TypeA {
				@range(0x123B, 0x123F) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8With2HexAnd1StringArgs() {
		assertFalse('''
			TypeA {
				@range(0x123B, 0x123F, "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8With2FloatArgs() {
		assertFalse('''
			TypeA {
				@range(1.0, 2.0) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI8With2FloatAndStringArgs() {
		assertFalse('''
			TypeA {
				@range(1.0, 2.0, "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI16() {
		assertTrue('''
			TypeA {
				@range(1, 2) 
				i16 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI32() {
		assertTrue('''
			TypeA {
				@range(1, 2)  
				i32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeI64() {
		assertTrue('''
			TypeA {
				@range(1, 2)  
				i64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeV64() {
		assertTrue('''
			TypeA {
				@range(1, 2)  
				v64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF32FloatArgs() {
		assertTrue('''
			TypeA {
				@range(1.0, 2.0)  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF32With2FloatAnd1StringArgs() {
		assertFalse('''
			TypeA {
				@range(1.0, 2.0, "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF32With2FloatAnd2StringArgs() {
		assertTrue('''
			TypeA {
				@range(1.0, 2.0, "inclusive", "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF32HexArgs() {
		assertFalse('''
			TypeA {
				@range(0x123B, 0x123F)  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF32IntegerArgs() {
		assertFalse('''
			TypeA {
				@range(1, 2)  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF32IntegerAndStringArgs() {
		assertFalse('''
			TypeA {
				@range(1, "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF32FloatAndStringArgs() {
		assertFalse('''
			TypeA {
				@range(1.0, "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeF64() {
		assertTrue('''
			TypeA {
				@range(1.0, 2.0)
				f64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeBoolean() {
		assertFalse('''
			TypeA {
				@range(1, 2) 
				bool a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testRangeAnnotation() {
		assertFalse('''
			TypeA {
				@range(1, 2) 
				annotation a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
}