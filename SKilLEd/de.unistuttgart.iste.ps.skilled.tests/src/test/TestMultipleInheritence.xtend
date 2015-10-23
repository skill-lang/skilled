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
 * @author Jan Berberich
 * 
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
		
		
}