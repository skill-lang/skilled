package test

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.DeclarationReference
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.impl.InterfacetypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.UsertypeImpl
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.BeforeClass

/**
 * @author Tobias Heck
 * 
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestEdgeCases {
	
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	var static String testBracketsNotNecessary = "";
	var static String testInheritageOfASingleTypeViaSeveralInterfaces = "";
	var static String testInheritageOfASingleTypeViaInterfaceAndType = "";
	var static String testInheritageOfASingleTypeTwiceViaOneInterface = "";
	var static String testDeclarationOfInheritedField = "";
	var static String testDuplicateFieldNames = "";
	var static String test = "";
	var static String testRestrictionCaseInsensitive = "";
	var static String testDefault1 = "";
	var static String testDefault2 = "";
	
	@BeforeClass
	def static void setup() {
		testBracketsNotNecessary = FileLoader.loadFile("TestBracketsNotNecessary");
		testInheritageOfASingleTypeViaSeveralInterfaces = FileLoader.loadFile(
			"TestInheritageOfASingleTypeViaSeveralInterfaces");
		testInheritageOfASingleTypeViaInterfaceAndType = FileLoader.loadFile(
			"testInheritageOfASingleTypeViaInterfaceAndType");
		testInheritageOfASingleTypeTwiceViaOneInterface = FileLoader.loadFile(
			"testInheritageOfASingleTypeTwiceViaOneInterface");
		testDeclarationOfInheritedField = FileLoader.loadFile(
			"testDeclarationOfInheritedField");
		testDuplicateFieldNames = FileLoader.loadFile("testDuplicateFieldNames");
		test = FileLoader.loadFile("test");
		testRestrictionCaseInsensitive = FileLoader.loadFile(
			"testRestrictionCaseInsensitive");
		testDefault1 = FileLoader.loadFile(
			"testDefault1");
		testDefault2 = FileLoader.loadFile(
			"testDefault2");
	}
	
	@Test
	def void testBracketsNotNecessary() {
		val specification = testBracketsNotNecessary.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val b = specification.declarations.get(1) as UsertypeImpl;
		val a = b.supertypes.get(0).type;
		Assert::assertEquals(specification.declarations.get(0), a);
	}
	
	@Test
	def void testInheritageOfASingleTypeViaSeveralInterfaces() {
		val specification = '''
			A {}
			interface B : A
			interface C : A
			D : B : C
		'''.parse
		
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
		val specification = '''
			A {}
			interface B : A
			C : A
			D : B : C
		'''.parse
		
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
		val specification = '''
			A {}
			interface B : A
			C : B : A
		'''.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val c = specification.declarations.get(2) as UsertypeImpl;
		Assert::assertEquals(specification.declarations.get(1), c.supertypes.get(0).type);
		Assert::assertEquals(specification.declarations.get(0), c.supertypes.get(1).type);
	}
	
	@Test
	def void testDeclarationOfInheritedField() {
		val specification = '''
			A {}
			interface B : A
			interface C : A {
				B field;
			}
			D : C {
				B field;
			}
		'''.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val b = specification.declarations.get(1) as InterfacetypeImpl;
		val c = specification.declarations.get(2) as InterfacetypeImpl;
		val d = specification.declarations.get(3) as UsertypeImpl;
		Assert::assertEquals(c, d.supertypes.get(0).type);
		Assert::assertEquals("field", c.fields.get(0).fieldcontent.name);
		Assert::assertEquals(b, (c.fields.get(0).fieldcontent.fieldtype as DeclarationReference).type);
		Assert::assertEquals("field", d.fields.get(0));
		Assert::assertEquals(b, (d.fields.get(0).fieldcontent.fieldtype as DeclarationReference).type);
	}
	
	@Test
	def void testDuplicateFieldNames() {
		val specification = '''
			A {
				v64 field;
			}
			interface B : A {
				C field;
			}
			interface C : A {
				B field;
			}
			D : C {
				B field;
			}
		'''.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount > 0);
	}
	
	//TODO what is the expected behavior in the following situation?
	@Test
	def void test() {
		val specification = '''
			A {}
			interface B : A {
				C field;
			}
			interface C : A {
				B field;
			}
			D : C {
				B field;
			}
		'''.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount > 0);
	}
	
	@Test
	def void testRestrictionCaseInsensitivity() {
		val specification = '''
			T {
				@nonnull
				i8 x;
			}
		'''.parse
		
		val issue = specification.validate.get(0);
		Assert::assertNotEquals("Unknown Restriction", issue.message);
	}
	
	@Test
	def void testDefault1() {
		val specification = '''
			T {
				@default(foo)
				string x;	
			}

			enum E {
				foo, bar;
			}
		'''.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val t = specification.declarations.get(0) as UsertypeImpl;
		val e = specification.declarations.get(1) as Enumtype;
		Assert::assertEquals(e.instances.get(0), t.fields.get(0).restrictions.
			get(0).restrictionArguments.get(0).enumInstance);
	}
	
	@Test
	def void testDefault2() {
		val specification = '''
			T {
				@default(foo)
				E x;	
			}

			enum E {
				foo, bar;
			}
		'''.parse
		
		val issueCount = specification.validate.size;
		Assert::assertTrue(issueCount == 0);
		val t = specification.declarations.get(0) as UsertypeImpl;
		val e = specification.declarations.get(1) as Enumtype;
		Assert::assertEquals(e.instances.get(0), t.fields.get(0).restrictions.
			get(0).restrictionArguments.get(0).enumInstance);
	}
	
	
}