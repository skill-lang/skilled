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
		assertFalse('''
			TypeA {
				@default 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultMapWithArgs() {
		assertFalse('''
			TypeA {
				@default(1, 2)
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Compound types and user types except Map
	
	@Test
	def void testDefaultSet() {
		assertFalse('''
			TypeA {
				@default 
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultList() {
		assertFalse('''
			TypeA {
				@default 
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultArray() {
		assertFalse('''
			TypeA {
				@default 
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultArrayWithArgs() {
		assertFalse('''
			TypeA {
				@default("")
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultUsertypeWithoutArgs() {
		assertFalse('''
			TypeC {
				@default
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Built in types
	
	@Test
	def void testDefaultString() {
		assertTrue('''
			TypeA {
				@default("abc") 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringWithoutArgs() {
		assertFalse('''
			TypeA {
				@default
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringIntegerArg() {
		assertFalse('''
			TypeA {
				@default(1)
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringFloatArg() {
		assertFalse('''
			TypeA {
				@default(1.0)
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringHexArg() {
		assertFalse('''
			TypeA {
				@default(0x123B)
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringUserTypeArg() {
		assertFalse('''
			TypeB {
			}
			TypeA {
				TypeB a;
				
				@default(a)
				string b;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultStringMultipleArgs() {
		assertFalse('''
			TypeA {
				@default("abc", "def") 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testDefaultI8IntegerArg() {
		assertTrue('''
			TypeA {
				@default(1) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI8HexArg() {
		assertTrue('''
			TypeA {
				@default(0x123B) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI8FloatArg() {
		assertFalse('''
			TypeA {
				@default(1.0) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI8StringArg() {
		assertFalse('''
			TypeA {
				@default("") 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI16() {
		assertTrue('''
			TypeA {
				@default(1) 
				i16 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI32() {
		assertTrue('''
			TypeA {
				@default(1)  
				i32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultI64() {
		assertTrue('''
			TypeA {
				@default(1)  
				i64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultV64() {
		assertTrue('''
			TypeA {
				@default(1)  
				v64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF32FloatArg() {
		assertTrue('''
			TypeA {
				@default(1.0)  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF32HexArg() {
		assertFalse('''
			TypeA {
				@default(0x123B)  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF32IntegerArg() {
		assertFalse('''
			TypeA {
				@default(1)  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultF64() {
		assertTrue('''
			TypeA {
				@default(1.0)
				f64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultAnnotation() {
		assertTrue('''
			@singleton
			TypeA {
			}
			 
			TypeB {
				@default(TypeA) 
				annotation a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testDefaultBoolean() {
		assertFalse('''
			TypeA {
				@default("") 
				bool a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
}