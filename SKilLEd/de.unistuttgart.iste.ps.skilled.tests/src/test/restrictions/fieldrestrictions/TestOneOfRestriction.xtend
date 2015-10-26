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
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfMapNoArgs() {
		assertFalse('''
			TypeA {
				@oneOf
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Compound types except Map
	
	@Test
	def void testOneOfSet() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfList() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfArray() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA) 
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfArrayNoArgs() {
		assertFalse('''
			TypeA {
				@oneOf
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//User types
	
	//TODO
	
	@Test
	def void testOneOfUsertypeCorrectArgument() {
		assertTrue('''
			TypeA {
			}
			TypeB : TypeA {
			}
			TypeC {
				@oneOf(TypeB) 
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfUsertypeMultipleCorrectArguments() {
		assertTrue('''
			TypeA {
			}
			TypeB : TypeA {
			}
			TypeD : TypeA {
			}
			TypeC {
				@oneOf(TypeB, TypeD) 
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfUsertypeCorrectAndIncorrectArguments() {
		assertFalse('''
			TypeA {
			}
			TypeB : TypeA {
			}
			TypeD : TypeA {
			}
			TypeC {
				@oneOf(TypeB, 1) 
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfUsertypeIncorrectArgument() {
		assertFalse('''
			TypeC {
				@oneOf(1) 
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfNoArgument() {
		assertFalse('''
			TypeC {
				@oneOf 
				TypeA a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Built in types

	@Test
	def void testOneOfI8() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA) 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfI16() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)  
				i16 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfI32() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA) 
				i32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfI64() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA) 
				i64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfV64() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)  
				v64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfF32() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)  
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfF64() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)  
				f64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfString() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA) 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testOneOfBoolean() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA)  
				bool a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//TODO
	
	@Test
	def void testOneOfAnnotation() {
		assertTrue('''
			TypeA {
			}
			TypeB {
				@oneOf(TypeA) 
				annotation a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
}