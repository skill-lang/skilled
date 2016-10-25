package de.unistuttgart.iste.ps.skilled.tests.validation.restrictions.fieldrestrictions;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.File
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static org.junit.Assert.*

/**
 * @author Nikolay Fateev
 * @author Tobias Heck
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
		assertFalse("A {@blabla Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void testBlaMapWithArgs() {
		assertFalse("A {@bla(\"\") Map<string, string> a;}".parse.validate.isNullOrEmpty)
	}
	
	//Other compound types and built in types and user types.
	
	@Test
	def void testBlaSetNoArgs() {
		assertFalse("A {@bla Set<string> a;}".parse.validate.isNullOrEmpty)
	}	
	
	@Test
	def void testBlaSetWithArgs() {
		assertFalse("A {@bla(\"\") Set<string> a;}".parse.validate.isNullOrEmpty)
	}
}