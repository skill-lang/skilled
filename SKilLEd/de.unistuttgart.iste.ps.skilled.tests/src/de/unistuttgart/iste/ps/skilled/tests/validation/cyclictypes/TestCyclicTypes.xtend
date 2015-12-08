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
import de.unistuttgart.iste.ps.skilled.tests.utils.ErrorMessageComparator

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
	def void testNoError1() {
		assertTrue("A:B:C{} B:C{} C{}".parse.validate.isNullOrEmpty)		
	}
	
	@Test
	def void testNoError2() {
		assertTrue("A:B{} B{} C:B{}".parse.validate.isNullOrEmpty)
	}
	
	@Test
	def void errorCycle() {
		val issues = "A:B{} B:A{}".parse.validate;
	
		assertTrue(issues.size==2)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_CYCLE
		));
	}

	@Test
	def void errorTypeIsHisOwnParent() {
		val issues = "A:A{}".parse.validate;
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_PARENT_IS_SELF
		));
	}
	
	@Test
	def void errorInterfaceCycle() {
		val issues = "interface A:B{} interface B:A{}".parse.validate;
		
		assertTrue(issues.size==2)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_CYCLE
		));
	}
	
	@Test
	def void errorInterfaceIsHisOwnParent() {
		val issues = "interface A:A{}".parse.validate;
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_PARENT_IS_SELF
		));
	}
}