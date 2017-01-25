package de.unistuttgart.iste.ps.skilled.tests.edgecases;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SkillInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.Enumtype
import de.unistuttgart.iste.ps.skilled.skill.File
import de.unistuttgart.iste.ps.skilled.skill.impl.InterfacetypeImpl
import de.unistuttgart.iste.ps.skilled.skill.impl.UsertypeImpl
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Tobias Heck
 * 
 */
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestEdgeCases {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String testInheritageOfASingleTypeViaSeveralInterfaces = "";
	var static String testInheritageOfASingleTypeViaInterfaceAndType = "";
	var static String testInheritageOfASingleTypeTwiceViaOneInterface = "";
	var static String testDeclarationOfInheritedField = "";
	var static String testDuplicateFieldNames = "";
	var static String test = "";
	var static String testRestrictionCaseInsensitivity = "";
	var static String testDefault = "";
	
	@BeforeClass
	def static void setup() {
		testInheritageOfASingleTypeViaSeveralInterfaces = FileLoader.loadFile(
			"edgecases/TestInheritageOfASingleTypeViaSeveralInterfaces");
		testInheritageOfASingleTypeViaInterfaceAndType = FileLoader.loadFile(
			"edgecases/TestInheritageOfASingleTypeViaInterfaceAndType");
		testInheritageOfASingleTypeTwiceViaOneInterface = FileLoader.loadFile(
			"edgecases/TestInheritageOfASingleTypeTwiceViaOneInterface");
		testDeclarationOfInheritedField = FileLoader.loadFile(
			"edgecases/TestDeclarationOfInheritedField");
		testDuplicateFieldNames = FileLoader.loadFile("edgecases/TestDuplicateFieldNames");
		test = FileLoader.loadFile("edgecases/Test");
		testRestrictionCaseInsensitivity = FileLoader.loadFile(
			"edgecases/TestRestrictionCaseInsensitivity");
		testDefault = FileLoader.loadFile("edgecases/TestDefault");
	}
	
	@Test
	def void testInheritageOfASingleTypeViaSeveralInterfaces() {
		val specification = testInheritageOfASingleTypeViaSeveralInterfaces.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val d = specification.declarations.get(3) as UsertypeImpl;
		Assert::assertEquals(specification.declarations.get(1), d.supertypes.get(0).type);
		Assert::assertEquals(specification.declarations.get(2), d.supertypes.get(1).type);
		Assert::assertEquals(specification.declarations.get(0),
			d.supertypes.get(0).type.supertypes.get(0).type);
	}
	
	@Test
	def void testInheritageOfASingleTypeViaInterfaceAndType() {
		val specification = testInheritageOfASingleTypeViaInterfaceAndType.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val d = specification.declarations.get(3) as UsertypeImpl;
		Assert::assertEquals(specification.declarations.get(1), d.supertypes.get(0).type);
		Assert::assertEquals(specification.declarations.get(2), d.supertypes.get(1).type);
		Assert::assertEquals(specification.declarations.get(0),
			d.supertypes.get(0).type.supertypes.get(0).type);
	}
	
	@Test
	def void testInheritageOfASingleTypeTwiceViaOneInterface() {
		val specification = testInheritageOfASingleTypeTwiceViaOneInterface.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val c = specification.declarations.get(2) as UsertypeImpl;
		Assert::assertEquals(specification.declarations.get(1), c.supertypes.get(0).type);
		Assert::assertEquals(specification.declarations.get(0), c.supertypes.get(1).type);
	}
	
	@Test
	def void testDeclarationOfInheritedField() {
		val specification = testDeclarationOfInheritedField.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 1);
		val b = specification.declarations.get(1) as InterfacetypeImpl;
		val c = specification.declarations.get(2) as InterfacetypeImpl;
		val d = specification.declarations.get(3) as UsertypeImpl;
		Assert::assertEquals(c, d.supertypes.get(0).type);
		Assert::assertEquals("field", c.fields.get(0).fieldcontent.name);
		Assert::assertEquals(b, (c.fields.get(0).fieldcontent.fieldtype as DeclarationReference).type);
		Assert::assertEquals("field", d.fields.get(0).fieldcontent.name);
		Assert::assertEquals(b, (d.fields.get(0).fieldcontent.fieldtype as DeclarationReference).type);
	}
	
	@Test
	def void testDuplicateFieldNames() {
		val specification = testDuplicateFieldNames.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void test() {
		val specification = test.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testRestrictionCaseInsensitivity() {
		val specification = testRestrictionCaseInsensitivity.parse
		
		val issue = specification.validate.get(0);
		Assert::assertNotEquals("Unknown Restriction", issue.message);
	}
	
	@Test
	def void testDefault1() {
		val specification = testDefault.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val t = specification.declarations.get(0) as UsertypeImpl;
		val e = specification.declarations.get(1) as Enumtype;
		Assert::assertEquals(e.instances.get(0).name, t.fields.get(0).restrictions.
			get(0).restrictionArguments.get(0).enumInstance);
	}	
	
}