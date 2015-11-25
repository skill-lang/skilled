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
 * This Class tests ASCIICharValidator.xtend
 * @author Jan Berberich 
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestNonASCIIWarning {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	@Test
	def void testErrorNonASCIITypename() {
		assertFalse('''
			TypeÄ{
				
			}  
		'''.parse.validate.isNullOrEmpty)

	}

	@Test
	def void testErrorNonASCIIFieldname() {
		assertFalse('''
			C{
				i8 ü;
			}
		'''.parse.validate.isNullOrEmpty)

	}

	@Test
	def void testMultipleWrongTypenames() {
		assertTrue('''
			interface IÄ{
				const i8 a = 1;
			}    	
			Ü{
				
			}
			Ö{
				
			}
		'''.parse.validate.size==3)
	}
	
	@Test
	def void testNoWarning() {
		assertTrue('''
			A{
				
			}
			B{
				i16 var;
			}
			C:B{
				i8 i;
			}
		'''.parse.validate.isNullOrEmpty)
	}

	
	
	
}