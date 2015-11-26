package de.unistuttgart.iste.ps.skilled.tests.validation.cyclictypes;

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
 * This Class tests Cyclic Types Validation from the InheritenceValidator.
 * @author Jan Berberich
 */

@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestCyclicTypes {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
		
	@Test
	def void testNoErrorValid() {
		assertTrue('''
			TypeA {
				const i8 a = 1;
			}    	
			TypeB :TypeA{
				
			}
			TypeC{
				
			}
		'''.parse.validate.isNullOrEmpty)
		
	}
	
	@Test
	def void testNoErrorValid2() {
		assertTrue('''
			TypeA {
				const i8 a = 1;
			}    	
			TypeB :TypeA{
				
			}
			TypeC{
				
			}
			TypeD:TypeA{
				
			}
		'''.parse.validate.isNullOrEmpty)
		
	}
	
	
	@Test
	def void errorCycle() {
		assertFalse('''
			TypeA :TypeC {
				const i8 a = 1;
			}   
			TypeB :TypeA{
				
			}
			TypeC :TypeB{
				
			} 	
		'''.parse.validate.isNullOrEmpty)
	
		assertTrue('''
			TypeA :TypeC {
				const i8 a = 1;
			}   
			TypeB :TypeA{
				
			}
			TypeC :TypeB{
				
			} 	
		'''.parse.validate.size==3)	
	}

	@Test
	def void errorTypeIsHisOwnParent() {
		assertFalse('''
			TypeA :TypeA {
				const i16 a = 1;
			}   
			TypeB :TypeA{
				
			}
			TypeC :TypeB{
				
			} 	
		'''.parse.validate.isNullOrEmpty )
		
		assertFalse('''
			TypeA :TypeA {
				const i16 a = 1;
			}   
			TypeB :TypeA{
				
			}
			TypeC :TypeB{
				
			} 	
		'''.parse.validate.isNullOrEmpty)
		
	}
	
	@Test
	def void errorInterfaceCycle() {
		assertFalse('''
			Interface TypeA : TypeB {
				const i8 a = 1;
			}    	
			Interface TypeB : TypA{
				
			}
		'''.parse.validate.isNullOrEmpty )
	}
}