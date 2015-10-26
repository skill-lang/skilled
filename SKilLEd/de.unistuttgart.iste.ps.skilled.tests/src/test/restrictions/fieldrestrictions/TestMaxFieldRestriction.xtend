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
		assertFalse('''
			TypeA {
				@max 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxMapWithArgs() {
		assertFalse('''
			TypeA {
				@max(1, 2)
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Compound types and user types except Map
	
	@Test
	def void testMaxSet() {
		assertFalse('''
			TypeA {
				@max 
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxList() {
		assertFalse('''
			TypeA {
				@max 
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxArray() {
		assertFalse('''
			TypeA {
				@max 
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxArrayWithArgs() {
		assertFalse('''
			TypeA {
				@max(1)
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxUsertypeWithoutArgs() {
		assertFalse('''
			TypeC {
				@max
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Built in types
	
	@Test
	def void testMaxStringWithArgs() {
		assertFalse('''
			TypeA {
				@max(1, "Inclusive") 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testMaxI8SingleIntegerArg() {
		assertTrue('''
			TypeA {
				@max(1) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8IntegerAndStringArgs() {
		assertTrue('''
			TypeA {
				@max(1, "Inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8With2IntegerAndStringArgs() {
		assertFalse('''
			TypeA {
				@max(1, 2, "Inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8With2IntegerArgs() {
		assertFalse('''
			TypeA {
				@max(1, 2) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8HexAndStringArgs() {
		assertTrue('''
			TypeA {
				@max(0x123B, "Inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI8FloatAndStringArgs() {
		assertFalse('''
			TypeA {
				@max(1.0, "Inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI16() {
		assertTrue('''
			TypeA {
				@max(1, "Inclusive")  
				i16 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI32() {
		assertTrue('''
			TypeA {
				@max(1, "Inclusive")  
				i32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxI64() {
		assertTrue('''
			TypeA {
				@max(1, "Inclusive")   
				i64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxV64() {
		assertTrue('''
			TypeA {
				@max(1, "Inclusive")   
				v64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32SingleFloatArg() {
		assertTrue('''
			TypeA {
				@max(1.0)
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32FloatAndStringArgs() {
		assertTrue('''
			TypeA {
				@max(1.0, "Inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32With2FloatAndStringArgs() {
		assertFalse('''
			TypeA {
				@max(1.0, 2.0, "Inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32HexArgs() {
		assertFalse('''
			TypeA {
				@max(0x123B, "Inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF32IntegerArgs() {
		assertFalse('''
			TypeA {
				@max(1, "Inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxF64() {
		assertTrue('''
			TypeA {
				@max(1.0)
				f64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxBoolean() {
		assertFalse('''
			TypeA {
				@max(1, "Inclusive") 
				bool a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMaxAnnotation() {
		assertFalse('''
			TypeA {
				@max(1, "Inclusive") 
				annotation a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
}