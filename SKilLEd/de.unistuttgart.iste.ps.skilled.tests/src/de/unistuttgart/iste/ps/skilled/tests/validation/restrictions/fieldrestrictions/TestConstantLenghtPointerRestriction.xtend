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
		assertFalse('''
			TypeA {
				@constantLengthPointer 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerMapWithArgs() {
		assertFalse('''
			TypeA {
				@constantLengthPointer("") 
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Compound types except Map
	
	@Test
	def void testConstantLenghtPointerSet() {
		assertFalse('''
			TypeA {
				@constantLengthPointer 
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerList() {
		assertFalse('''
			TypeA {
				@constantLengthPointer 
				List<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerArray() {
		assertFalse('''
			TypeA {
				@constantLengthPointer 
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerArrayWithArgs() {
		assertFalse('''
			TypeA {
				@constantLengthPointer("")
				string[] a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//User types
	
	@Test
	def void testConstantLenghtPointerUserType() {
		assertTrue('''
			TypeA {
			}
			TypeB {
				@constantLengthPointer
				TypeA a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerUserTypeArgs() {
		assertFalse('''
			TypeA {
			}
			TypeB {
				@constantLengthPointer("")
				TypeA a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Built in types

	@Test
	def void testConstantLenghtPointerString() {
		assertTrue('''
			TypeA {
				@constantLengthPointer
				string a;
			}
		'''.parse.validate.isNullOrEmpty)	
	}
	
	@Test
	def void testConstantLenghtPointerWithArguments() {
		assertFalse('''
			TypeA {
				@constantLengthPointer("")
				string a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI8() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				i8 a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI16() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				i16 a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI32() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				i32 a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerI64() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				i64 a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerV64() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				v64 a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerF32() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				f32 a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerF64() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				f64 a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerBoolean() {
		assertFalse('''
			TypeA {
				@constantLengthPointer
				bool a;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testConstantLenghtPointerAnnotation() {
		assertTrue('''
			TypeA {
				@constantLengthPointer
				annotation a;
			}
		'''.parse.validate.isNullOrEmpty)
	}	
}