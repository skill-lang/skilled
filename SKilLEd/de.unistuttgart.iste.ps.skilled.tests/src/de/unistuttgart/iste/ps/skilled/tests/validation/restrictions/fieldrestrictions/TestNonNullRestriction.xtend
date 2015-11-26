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
class TestNonNullRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
//	@Test
//	def void test1() {
//		for(Issue i :'''
//			TypeA {
//				@nonnull 
//				i8 a;
//			}  
//		'''.parse.validate){	
//			val bla = i;
//		}
//	}

	/*
	 * Whitebox knowledge: the handling code is divided into 4 logical groups:
	 * maps; other compound types; built in types; user types.
	 */
	 
	//Map
	 
	@Test
	def void testNonNullMapNoArgs() {
		assertFalse('''
			TypeA {
				@nonNull 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullMapWithArgs() {
		assertFalse('''
			TypeA {
				@nonNull("") 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Compound types except Map
	
	@Test
	def void testNonNullSet() {
		assertFalse('''
			TypeA {
				@nonNull 
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullList() {
		assertFalse('''
			TypeA {
				@nonNull 
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullArray() {
		assertFalse('''
			TypeA {
				@nonNull 
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullArrayWithArgs() {
		assertFalse('''
			TypeA {
				@nonNull("")
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//User types
	
	@Test
	def void testNonNullUsertypeWithoutArgs() {
		assertTrue('''
			TypeB {
			}
			TypeA {
				@nonNull 
				TypeB a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullUsertypeWithArgs() {
		assertFalse('''
			TypeB {
			}
			TypeA {
				@nonNull("")
				TypeB a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Built in types
	
	@Test
	def void testNonNullString() {
		assertTrue('''
			TypeA {
				@nonNull 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testNonNullStringWithArguments() {
		assertFalse('''
			TypeA {
				@nonNull("") 
				string a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testNonNullI8() {
		assertFalse('''
			TypeA {
				@nonNull 
				i8 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullI16() {
		assertFalse('''
			TypeA {
				@nonNull 
				i16 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullI32() {
		assertFalse('''
			TypeA {
				@nonNull 
				i32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullI64() {
		assertFalse('''
			TypeA {
				@nonNull 
				i64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullV64() {
		assertFalse('''
			TypeA {
				@nonNull 
				v64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullF32() {
		assertFalse('''
			TypeA {
				@nonNull 
				f32 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullF64() {
		assertFalse('''
			TypeA {
				@nonNull 
				f64 a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullBoolean() {
		assertFalse('''
			TypeA {
				@nonNull 
				bool a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNonNullAnnotation() {
		assertFalse('''
			TypeA {
				@nonNull 
				annotation a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
}