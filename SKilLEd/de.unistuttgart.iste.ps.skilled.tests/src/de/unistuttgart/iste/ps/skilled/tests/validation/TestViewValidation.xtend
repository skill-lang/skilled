package de.unistuttgart.iste.ps.skilled.tests.validation

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
 * 
 * This Class tests View Validation from the ViewValidator.
 * @author Jan Berberich 
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestViewValidation {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper

	@Test
	def void testNoError() {
		assertTrue('''
			A{
				A x;
			}
			B:A{
				view A.x as	B x;
			}
		'''.parse.validate.isNullOrEmpty)

	}
	
	@Test
	def void testNoError2() {
		assertTrue('''
			X{
				
			}
			Y:X{
				
			}
			A{
				X x;
			}
			B:A{
				view A.x as	Y y;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNoError3() {
		assertTrue('''
			X{
				
			}
			Y:X{
				
			}
			Z:Y{
				
			}
			A{
				X x;
			}
			B:A{
				view A.x as	Z z;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
		
	@Test
	def void testNoError4() {
		assertTrue('''
			X{
				
			}
			Y:X{
				
			}
			Z:Y{
				
			}
			A{
				X x;
			}
			B:A{
				
			}
			C:B{
				view A.x as	Z z;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorTypeNotASupertype() {
		assertFalse('''
			X{
				
			}
			Y:X{
				
			}
			A{
				X x;
			}
			B{
				view A.x as	Y y;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorFieldNotASupertype() {
		assertFalse('''
			X{
				
			}
			Y{
				
			}
			A{
				X x;
			}
			B:A{
				view A.x as	Y y;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorVarInSupertypeNotExisting() {
		assertFalse('''
			X{
				
			}
			Y:X{
				
			}
			A{
			}
			B:A{
				view A.x as	Y y;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	@Test
	def void testErrorSupertypeNotExisting() {
		assertFalse('''
			X{
				
			}
			Y:X{
				
			}
			B{
				view A.x as	Y y;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testErrorVarInSameType() {
		assertFalse('''
			X{
				
			}
			Y:X{
				
			}
			B{
				X x;
				view B.x as	Y y;
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoErrorNotAUsertype() {
		assertFalse('''
			X{
				
			}
			Y:X{
				
			}
			A{
				X x;
			}
			B:A{
				view A.x as	i8 y;
			}
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testNoErrorNotAUsertype2() {
		assertFalse('''
			X{
				
			}
			Y:X{
				
			}
			A{
				i8 x;
			}
			B:A{
				view A.x as	Y y;
			}
		'''.parse.validate.isNullOrEmpty)
	}

}