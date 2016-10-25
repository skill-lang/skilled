package de.unistuttgart.iste.ps.skilled.tests.validation

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
 * This class tests the KeywordWarning Validator.
 * @author Jan Berberich
 * 
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestKeywordWarning {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper

	@Test
	def void noWarningNormalTypenameNormalFieldname() {
		assertTrue("A{ i8 B;}".parse.validate.isNullOrEmpty)		
	}
	
	@Test
	def void warningForType() {
		assertFalse("for{}".parse.validate.isNullOrEmpty)		
	}
	
	@Test
	def void warningIntType() {
		assertFalse("int{}".parse.validate.isNullOrEmpty)		
	}
	
	@Test
	def void warningIntField() {
		assertFalse("A{ i8 int;}".parse.validate.isNullOrEmpty)		
	}
	
}