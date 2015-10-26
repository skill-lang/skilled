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
class TestDummyBlaRestriction {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	/*
	 * Testing a input of an unexisting restriction.
	 * 
	 * Whitebox knowledge: the handling code is divided into 2 logical groups:
	 * maps; other compound types and built in types and user types.
	 */
	 
	//Map
	
	//Map
	
	@Test
	def void testBlaMapNoArgs() {
		assertFalse('''
			TypeA {
				@blabla
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testBlaMapWithArgs() {
		assertFalse('''
			TypeA {
				@bla("")
				Map<string, string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
	
	//Other compound types and built in types and user types.
	
	@Test
	def void testBlaSetNoArgs() {
		assertFalse('''
			TypeA {
				@bla
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testBlaSetWithArgs() {
		assertFalse('''
			TypeA {
				@bla("")
				Set<string> a;
			}   
		'''.parse.validate.isNullOrEmpty)
	}
}