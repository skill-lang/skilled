package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.fieldrestrictions;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
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
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestMinFieldRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Whitebox knowledge: the handling code is divided into 3 logical groups:
	 * maps; other compound types with user types; built in types.
	 */
	 
	//Map
	
	@Test
	def void testMinMapNoArgs() {
		assertFalse('''
			TypeA {
				@min 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinMapWithArgs() {
		assertFalse('''
			TypeA {
				@min(1)
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Compound types and user types except Map
	
	@Test
	def void testMinSet() {
		assertFalse('''
			TypeA {
				@min 
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinList() {
		assertFalse('''
			TypeA {
				@min 
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinArray() {
		assertFalse('''
			TypeA {
				@min 
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinArrayWithArgs() {
		assertFalse('''
			TypeA {
				@min(1)
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinUsertypeWithoutArgs() {
		assertFalse('''
			TypeC {
				@min
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Built in types
	
	@Test
	def void testMinStringWithArgs() {
		assertFalse('''
			TypeA {
				@min(1, "inclusive") 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testMinI8SingleIntegerArg() {
		assertTrue('''
			TypeA {
				@min(1) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI8IntegerAndStringArgs() {
		assertTrue('''
			TypeA {
				@min(1, "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI8With2IntegerAndStringArgs() {
		assertFalse('''
			TypeA {
				@min(1, 2, "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI8With2IntegerArgs() {
		assertFalse('''
			TypeA {
				@min(1, 2) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI8HexAndStringArgs() {
		assertTrue('''
			TypeA {
				@min(0x1B, "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI8FloatAndStringArgs() {
		assertFalse('''
			TypeA {
				@min(1.0, "inclusive") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI16() {
		assertTrue('''
			TypeA {
				@min(1, "inclusive")  
				i16 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI32() {
		assertTrue('''
			TypeA {
				@min(1, "inclusive")  
				i32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinI64() {
		assertTrue('''
			TypeA {
				@min(1, "inclusive")   
				i64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinV64() {
		assertTrue('''
			TypeA {
				@min(1, "inclusive")   
				v64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinF32SingleFloatArg() {
		assertTrue('''
			TypeA {
				@min(1.0)
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinF32FloatAndStringArgs() {
		assertTrue('''
			TypeA {
				@min(1.0, "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinF32With2FloatAndStringArgs() {
		assertFalse('''
			TypeA {
				@min(1.0, 2.0, "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinF32HexArgs() {
		assertFalse('''
			TypeA {
				@min(0x123B, "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinF32IntegerArgs() {
		assertFalse('''
			TypeA {
				@min(1, "inclusive")  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinF64() {
		assertTrue('''
			TypeA {
				@min(1.0)
				f64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinBoolean() {
		assertFalse('''
			TypeA {
				@min(1, "inclusive") 
				bool a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testMinAnnotation() {
		assertFalse('''
			TypeA {
				@min(1, "inclusive") 
				annotation a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
}