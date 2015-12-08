package de.unistuttgart.iste.ps.skilled.tests.validation.inheritance;

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
import de.unistuttgart.iste.ps.skilled.tests.utils.ErrorMessageComparator

/**
 * 
 * This Class tests Multiple Inheritence Validation from the InheritenceValidator.
 * @author Jan Berberich 
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestInheritenceValidator {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	//doesn't compile, but should be correct (?)
	@Test
	def void TransitiveInheritanceIsLegal() {
		assertTrue("A:B:C{} B:C{} C{}".parse.validate.isNullOrEmpty)		
	}
	
	@Test
	def void TwoInheritancesAreLegal() {
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

	@Test
	def void testInheritanceViaInterfaceNoError() {
		assertTrue("A:B{} interface B:C{} C{}".parse.validate.isNullOrEmpty);
	}

	@Test
	def void testInheritanceViaInterfacesNoError() {
		assertTrue("A:B:C{} interface B:D{} interface C:D{} D{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testInheritanceViaInterfacesAndTypeNoError() {
		assertTrue("interface A:D{} interface B:D{} C:A:B{} D:E{} E{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testInheritanceViaBothInterfacesAndTypeNoError() {
		assertTrue("interface A:D{} interface B:D{} C:A:B:D{} D:E{} E{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testInheritanceViaInterfaceAndTypeAndInterfaceNoError() {
		assertTrue("interface A:D{} interface B:E{} C:A:B{} D:E{} E{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testInheritanceViaMoreInterfacesAndTypeNoError() {
		assertTrue("interface A:D{} interface B:D{} interface C:D{} D:E{} E{} F:A:B:C{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testInheritanceViaTypeAndInterfaceNoError() {
		assertTrue("interface A:C{} B:A:C{} C{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError13() {
		assertTrue("H:A:B:C{} interface A:B{} interface B:C{} interface C:D{} G:C{} F:D{} D:E{} E{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError14() {
		assertTrue("interface A:B:C:D{} interface B:E{} interface C{} interface D{} E{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError15() {
		assertTrue("interface A:B:C:D{} interface B:E{} interface C:E{} interface D{} E{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError16() {
		assertTrue("E:A{} interface A:B:C:D{} interface B:G{} interface C:G{} interface D{} G{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoError17() {
		assertTrue("E:A{} interface A:B:C:D{} interface B:F{} interface C:F{} interface D:G{} F:G{} G{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testError() {
		val issues = "A:B:D{} B{} interface D:C{} C{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError2() {
		val issues = "A:D:E{} interface D:B{} B{} interface E:C{} C{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError3() {
		val issues = "E:A:B:C{} interface A:B{} interface B:F{} interface C:D{} interface D:G{} F{} G{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError4() {
		val issues = "C:IA:IB{} interface IA:D:E{} interface IB:D:E{} D{} E{}".parse.validate
		
		assertTrue(issues.size==3)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError5() {
		val issues = "A:E:F{} interface E:B:C{} interface F:B:C{} B:D{} C:D{} D{}".parse.validate
		
		assertTrue(issues.size==3)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError6() {
		val issues = "interface D:B:C{} B{} C{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError7() {
		val issues = "interface A:D:B{} B{} interface D:C{} C{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError9() {
		val issues = "A:E:F{} interface E:B{} interface F:C{} B:D{} C:D{} D{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testError10() {
		val issues = "A:E:F:G{} interface E:B{} interface F:C{} interface G:C{} B:D{} C:D{} D{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}

	@Test
	def void testNoErrorValid() {
		assertTrue("A:C{} B:A{} interface C{}".parse.validate.isNullOrEmpty)
	}

	@Test
	def void testErrorDirectInheritence() {
		val issues = "A:C:B{} B{} C{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_MULTIPLE
		));
	}
	
	@Test
	def void hallo() {
		val issues = "A:B:C{} B:A:C{} C:A:B{}".parse.validate
		
		assertTrue(issues.size==3)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_CYCLE
		));
	}
	
	@Test
	def void asdf() {
		val issues = "A:A:A{}".parse.validate
		
		assertTrue(issues.size==1)
		
		assertTrue(ErrorMessageComparator.containsMessage(
			issues, ErrorMessageComparator.ERROR_INHERITANCE_PARENT_IS_SELF
		));
	}
	
	
	
}