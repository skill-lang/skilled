package de.unistuttgart.iste.ps.skilled.tests.validation.asciichar;

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
 * This Class tests ASCIICharValidator.xtend
 * @author Jan Berberich
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestNonASCIIWarning {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	@Test
	def void testErrorNonASCIITypename() {
		assertFalse("TypeÄ{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testErrorNonASCIIFieldname() {
		assertFalse("C{i8 ☢;}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testMultipleWrongTypenames() {
		assertTrue("interface IÄ{} Ü{} Ö{}".parse.validate.size>=3)
	}
	
	@Test
	def void testNoWarning() {
		assertTrue("A{} B{} C:B{}".parse.validate.isNullOrEmpty)
	}
}