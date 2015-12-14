package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.fieldrestrictions;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import static org.junit.Assert.*
import java.util.List
import org.eclipse.xtext.validation.Issue
import org.junit.runner.RunWith
import org.junit.Test

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
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")
				Set<string> a;
			}   
		'''.parse.validate))
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
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")
				List<string> a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingArray() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")
				string[] a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingUsertype() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")
				TypeA a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingString() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")
				string a;
			}   
		'''.parse.validate))
	}		
	
	@Test
	def void testCodingI8() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip") 
				i8 a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingI16() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip") 
				i16 a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingI32() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")  
				i32 a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingI64() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")  
				i64 a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingV64() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")  
				v64 a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingF32() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")  
				f32 a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingF64() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip")
				f64 a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingBoolean() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip") 
				bool a;
			}   
		'''.parse.validate))
	}
	
	@Test
	def void testCodingAnnotation() {
		assertTrue(listHasNoErrors('''
			TypeA {
				@coding("zip") 
				annotation a;
			}   
		'''.parse.validate))
	}
	
	/**
	 * The result of field restriction validation there can be Errors and Warnings.
	 * This method checks if as a result of the validation there are any errors, while 
	 * ignoring warnings.
	 */
	def boolean listHasNoErrors(List<Issue> issues){
		for (issue : issues){
			if ("ERROR".equals(issue.severity.toString())) {
				return false;
			}
		}
		return true;
	}
}