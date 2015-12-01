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
 * This Class tests Multiple Inheritence Validation from the InheritenceValidator.
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

}