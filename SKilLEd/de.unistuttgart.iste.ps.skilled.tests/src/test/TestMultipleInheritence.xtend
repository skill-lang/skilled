package test

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
 * 
 * This Class tests Multiple Inheritence Validation from the InheritenceValidator.
 * @author Jan Berberich 
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestMultipleInheritence {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper

	@Test
	def void testNoError() {
		assertTrue('''
			TypeA :TypeC{
				const i8 a = 1;
			}    	
			TypeB :TypeA{
				
			}
			TypeC{
				
			}
		'''.parse.validate.isNullOrEmpty)

	}

	@Test
	def void testNoError2() {
		assertTrue('''
			interface IA :C{
				const i8 a = 1;
			}    	
			B :IA{
				
			}
			C{
				
			}
			
		'''.parse.validate.isNullOrEmpty)

	}

	@Test
	def void testNoError3() {
		assertTrue('''
			interface IA:D{
				const i8 a = 1;
			}    	
			interface IB:D{
				
			}
			C:IA:IB{
				
			}
			D{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError5() {
		assertTrue('''
			interface IA:D{
				const i8 a = 1;
			}    	
			interface IB:D{
				
			}
			C:IA:IB{
				
			}
			D:E{
				
			}
			E{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError6() {
		assertTrue('''
			interface IA:D{
				const i8 a = 1;
			}    	
			interface IB:D{
				
			}
			C:IA:IB:D{
				
			}
			D:E{
				
			}
			E{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError7() {
		assertTrue('''
			interface IA:D{
				const i8 a = 1;
			}    	
			interface IB:E{
				
			}
			C:IA:IB{
				
			}
			D:E{
				
			}
			E{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError8() {
		assertTrue('''
			interface IA:D{
				const i8 a = 1;
			}    	
			interface IB:D{
				
			}
			interface IC:D{
				
			}
			C:IA:IB:IC{
				
			}
			D:E{
				
			}
			E{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError9() {
		assertTrue('''
			interface IA :C{
				const i8 a = 1;
			}    	
			B :IA:C{
				
			}
			C{
				
			}
			
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError13() {
		assertTrue('''
			A:IA:IB:IC{
				
			}
			interface IA:B{
				
			}
			interface IB:C{
				
			}
			interface IC:D{
				
			}
			B:C{
				
			}
			C:D{
				
			}
			D:E{
				
			}
			E{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError14() {
		assertTrue('''
			interface IA:IB:IC:ID{
				
			}
			interface IB:C{
				
			}
			interface IC{
				
			}
			interface ID{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError15() {
		assertTrue('''
			interface IA:IB:IC:ID{
				
			}
			interface IB:C{
				
			}
			interface IC:C{
				
			}
			interface ID{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError16() {
		assertTrue('''
			A:IA{
				
			}
			interface IA:IB:IC:ID{
				
			}
			interface IB:C{
				
			}
			interface IC:C{
				
			}
			interface ID{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError17() {
		assertTrue('''
			A:IA{
				
			}
			interface IA:IB:IC:ID{
				
			}
			interface IB:C{
				
			}
			interface IC:C{
				
			}
			interface ID:D{
				
			}
			C:D{
				
			}
			D{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError() {
		assertFalse('''
			A:B:IA{
				
			}
			B{
				
			}
			interface IA:C{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError2() {
		assertFalse('''
			A:IB:IA{
				
			}
			interface IB:B{
				
			}
			B{
				
			}
			interface IA:C{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError3() {
		assertFalse('''
			A:IA:IB:IC{
				
			}
			interface IA:IB{
				
			}
			interface IB:B{
				
			}
			interface IC:ID{
				
			}
			interface ID:C{
				
			}
			B{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError4() {
		assertFalse('''
			A:IA:IB{
				
			}
			interface IA:B:C{
				
			}
			interface IB:B:C{
				
			}
			B{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError5() {
		assertFalse('''
			A:IA:IB{
				
			}
			interface IA:B:C{
				
			}
			interface IB:B:C{
				
			}
			B:D{
				
			}
			C:D{
				
			}
			D{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError6() {
		assertFalse('''
			interface IA:B:C{
				
			}
			B{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError7() {
		assertFalse('''
			interface IA:IB:B{
				
			}
			B{
				
			}
			interface IB:C{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError8() {
		assertFalse('''
			A:IA:IB{
				
			}
			interface IA:B{
				
			}
			interface IB:C{
				
			}
			B{
				
			}
			C{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError9() {
		assertFalse('''
			A:IA:IB{
				
			}
			interface IA:B{
				
			}
			interface IB:C{
				
			}
			B:D{
				
			}
			C:D{
				
			}
			D{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError10() {
		assertFalse('''
			A:IA:IB:IC{
				
			}
			interface IA:B{
				
			}
			interface IB:C{
				
			}
			interface IC:C{
				
			}
			B:D{
				
			}
			C:D{
				
			}
			D{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoErrorValid() {
		assertTrue('''
			TypeA :TypeC{
				const i8 a = 1;
			}    	
			TypeB :TypeA{
				
			}
			interface TypeC{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testErrorDirectInheritence() {
		assertFalse('''
			TypeA :TypeC:TypeB{
				const i8 a = 1;
			}    	
			TypeB{
				
			}
			TypeC{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testErrorMultipleInheritence() {
		assertFalse('''
			TypeA :TypeC:TypeD{
				const i8 a = 1;
			}    	
			Interface TypeD:TypeB{
				
			}
			TypeB{
				
			}
			TypeC{
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoErrorInterfaces() {
		assertTrue('''
			S {
				
			}
			interface E : S {
				
			}
			interface C : S {
				
			}
			Y : C : E {
				
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoErrorInheritence() {
		assertTrue('''
			S {
				
			}
			interface A : S {
				
			}
			Y : A : S {
				
			}
		'''.parse.validate.isNullOrEmpty)
	}
}