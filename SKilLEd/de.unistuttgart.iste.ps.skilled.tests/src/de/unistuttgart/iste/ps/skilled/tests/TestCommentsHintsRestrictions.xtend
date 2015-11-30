package de.unistuttgart.iste.ps.skilled.tests;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype
import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype
import de.unistuttgart.iste.ps.skilled.sKilL.Data
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.Float
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Integer
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ConstantImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DataImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DeclarationImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DeclarationReferenceImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.HintImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.InterfacetypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ListtypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.TypeDeclarationImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.TypedefImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.UsertypeImpl
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestCommentsHintsRestrictions {

	@Inject extension ParseHelper<File> parser;

	var static String testHints = "";
	var static String testComments = "";
	var static String testHexint = "";
	var static String testRestrictions = "";
	var static String testInterfaces = "";
	var static String testAuto = "";

	@BeforeClass
	def static void setup() {
		testHints = FileLoader.loadFile("hints/TestHints");
		testComments = FileLoader.loadFile("comments/TestComments");
		testHexint = FileLoader.loadFile("hexint/TestHexint");
		testRestrictions = FileLoader.loadFile("validation/restrictions/TestRestrictions");
		testInterfaces = FileLoader.loadFile("interfaces/TestInterfaces");
		testAuto = FileLoader.loadFile("builtintypes/TestAuto");
	}

	@Test
	def void testHints1() {
		val specification = testHints.parse

		val usertype = specification.declarations.get(0) as UsertypeImpl;
		Assert::assertEquals("removeunknownrestrictions", usertype.hints.get(0).hintName);
		Assert::assertEquals("unique",((usertype.hints.get(0) as HintImpl).hintArguments.get(0).valueString));
		Assert::assertEquals("monotone",((usertype.hints.get(0) as HintImpl).hintArguments.get(1).valueString));
		Assert::assertEquals("mixin", usertype.hints.get(1).hintName);
		Assert::assertEquals("unique", usertype.hints.get(2).hintName);
		Assert::assertEquals("pure", usertype.hints.get(3).hintName);
		Assert::assertEquals("monotone", usertype.hints.get(4).hintName);
		Assert::assertEquals("readonly", usertype.hints.get(5).hintName);
	}

	@Test
	def void testHints2() {
		val specification = testHints.parse

		val usertype = specification.declarations.get(0) as UsertypeImpl;
		val fields = usertype.fields;
		val field1 = fields.get(0);
		Assert::assertEquals("constantmutator", field1.hints.get(0).hintName);
		Assert::assertEquals(-17, (field1.hints.get(0) as HintImpl).hintArguments.get(0).valueLong);
		Assert::assertEquals(3000000000L, (field1.hints.get(0) as HintImpl).hintArguments.get(1).valueLong);
		val field2 = fields.get(1);
		Assert::assertEquals("distributed", field2.hints.get(0).hintName);
		Assert::assertEquals("ondemand", field2.hints.get(1).hintName);
		val field3 = fields.get(2);
		Assert::assertEquals("ignore", field3.hints.get(0).hintName);
		val field4 = fields.get(3);
		Assert::assertEquals("hide", field4.hints.get(0).hintName);
	}

	@Test
	def void testHints3() {
		val specification = testHints.parse

		val usertype2 = specification.declarations.get(1) as UsertypeImpl;
		Assert::assertEquals("flat", usertype2.hints.get(0).hintName);
	}

	@Test
	def void testComments() {
		val specification = testComments.parse

		Assert::assertEquals("#head comment" + System.getProperty("line.separator"), specification.headComments.get(0));
		Assert::assertEquals("#head comment 2" + System.getProperty("line.separator"),
			specification.headComments.get(1));
		val usertype = specification.declarations.get(0) as Usertype;
		Assert::assertEquals("/*usertype comment" + System.getProperty("line.separator") + "second line*/",
			(usertype as DeclarationImpl).comment);
		val field = (usertype as TypeDeclarationImpl).fields.get(0);
		Assert::assertEquals("/*field comment" + System.getProperty("line.separator") + "  second line*/",
			field.comment);
	}

	@Test
	def void testHexint() {
		val specification = testHexint.parse

		val foo = specification.declarations.get(0) as Usertype;
		val bar = foo.fields.get(0);
		val barArray = (bar.fieldcontent as Data).fieldtype as Arraytype
		Assert::assertEquals(-0xCAB, barArray.length);
		Assert::assertEquals(-3243, barArray.length);
	}

	@Test
	def void testRestrictions1() {
		val specification = testRestrictions.parse

		val usertype1 = specification.declarations.get(0) as UsertypeImpl;
		val usertype2 = specification.declarations.get(1) as UsertypeImpl;
		Assert::assertEquals("unique", usertype1.restrictions.get(0).restrictionName);
		Assert::assertEquals("default", usertype1.restrictions.get(1).restrictionName);
		Assert::assertEquals(usertype2 as TypeDeclaration,
			usertype1.restrictions.get(1).restrictionArguments.get(0).valueType.type);
	}

	@Test
	def void testRestrictions2() {
		val specification = testRestrictions.parse

		val usertype1 = specification.declarations.get(0) as UsertypeImpl;
		val fields1 = usertype1.fields;
		Assert::assertEquals("default", fields1.get(0).restrictions.get(0).restrictionName);
		Assert::assertEquals(-3000000000L, fields1.get(0).restrictions.get(0).restrictionArguments.get(0).valueLong);
		Assert::assertEquals("default", fields1.get(1).restrictions.get(0).restrictionName);
		Assert::assertEquals("Akemi", fields1.get(1).restrictions.get(0).restrictionArguments.get(0).valueString);
		Assert::assertEquals("default", fields1.get(2).restrictions.get(0).restrictionName);
		Assert::assertEquals(-123.456, fields1.get(2).restrictions.get(0).restrictionArguments.get(0).valueDouble,
			0.0001);
	}

	@Test
	def void testRestrictions3() {
		val specification = testRestrictions.parse

		val usertype2 = specification.declarations.get(1) as UsertypeImpl;
		Assert::assertEquals("singleton", usertype2.restrictions.get(0).restrictionName);
		Assert::assertEquals("monotone", usertype2.restrictions.get(1).restrictionName);
	}

	@Test
	def void testRestrictions4() {
		val specification = testRestrictions.parse

		val usertype2 = specification.declarations.get(1) as UsertypeImpl;
		val fields2 = usertype2.fields;
		Assert::assertEquals("nonnull", fields2.get(0).restrictions.get(0).restrictionName);
		Assert::assertEquals("constantlengthpointer", fields2.get(0).restrictions.get(1).restrictionName);
		Assert::assertEquals("constantlengthpointer", fields2.get(1).restrictions.get(0).restrictionName);
		Assert::assertEquals("constantlengthpointer", fields2.get(2).restrictions.get(0).restrictionName);
		Assert::assertEquals("oneof", fields2.get(2).restrictions.get(1).restrictionName);
	}

	@Test
	def void testRestrictions5() {
		val specification = testRestrictions.parse

		val usertype1 = specification.declarations.get(0) as UsertypeImpl;
		val usertype2 = specification.declarations.get(1) as UsertypeImpl;
		val usertype3 = specification.declarations.get(2) as UsertypeImpl;
		val usertype4 = specification.declarations.get(3) as UsertypeImpl;
		val fields2 = usertype2.fields;
		Assert::assertEquals(usertype1 as TypeDeclaration,
			fields2.get(2).restrictions.get(1).restrictionArguments.get(0).valueType.type);
		Assert::assertEquals(usertype2 as TypeDeclaration,
			fields2.get(2).restrictions.get(1).restrictionArguments.get(1).valueType.type);
		Assert::assertEquals(usertype3 as TypeDeclaration,
			fields2.get(2).restrictions.get(1).restrictionArguments.get(2).valueType.type);
		Assert::assertEquals(usertype4 as TypeDeclaration,
			fields2.get(2).restrictions.get(1).restrictionArguments.get(3).valueType.type);
	}

	@Test
	def void testRestrictions6() {
		val specification = testRestrictions.parse

		val usertype3 = specification.declarations.get(2) as UsertypeImpl;
		Assert::assertEquals("abstract", usertype3.restrictions.get(0).restrictionName);
		val fields3 = usertype3.fields;
		Assert::assertEquals("coding", fields3.get(0).restrictions.get(0).restrictionName);
		Assert::assertEquals("zip", fields3.get(0).restrictions.get(0).restrictionArguments.get(0).valueString);
	}

	@Test
	def void testRestrictions7() {
		val specification = testRestrictions.parse

		val usertype4 = specification.declarations.get(3) as UsertypeImpl;
		val fields4 = usertype4.fields;
		Assert::assertEquals("min", fields4.get(0).restrictions.get(0).restrictionName);
		Assert::assertEquals(0xABCD.operator_minus(),
			fields4.get(0).restrictions.get(0).restrictionArguments.get(0).valueLong);
		Assert::assertEquals("min", fields4.get(1).restrictions.get(0).restrictionName);
		Assert::assertEquals(12345.6789, fields4.get(1).restrictions.get(0).restrictionArguments.get(0).valueDouble,
			1e-5);
		Assert::assertEquals("inclusive", fields4.get(1).restrictions.get(0).restrictionArguments.get(1).valueString);
		Assert::assertEquals("max", fields4.get(2).restrictions.get(0).restrictionName);
		Assert::assertEquals(-3000000000L, fields4.get(2).restrictions.get(0).restrictionArguments.get(0).valueLong);
		Assert::assertEquals("max", fields4.get(3).restrictions.get(0).restrictionName);
		Assert::assertEquals(-1.1, fields4.get(3).restrictions.get(0).restrictionArguments.get(0).valueDouble, 1e-5);
		Assert::assertEquals("exclusive", fields4.get(3).restrictions.get(0).restrictionArguments.get(1).valueString);
	}

	@Test
	def void testRestrictions8() {
		val specification = testRestrictions.parse

		val usertype4 = specification.declarations.get(3) as UsertypeImpl;
		val fields4 = usertype4.fields;
		Assert::assertEquals("range", fields4.get(4).restrictions.get(0).restrictionName);
		Assert::assertEquals(-10, fields4.get(4).restrictions.get(0).restrictionArguments.get(0).valueLong);
		Assert::assertEquals(10, fields4.get(4).restrictions.get(0).restrictionArguments.get(1).valueLong);
		Assert::assertEquals("range", fields4.get(5).restrictions.get(0).restrictionName);
		Assert::assertEquals(-10.7, fields4.get(5).restrictions.get(0).restrictionArguments.get(0).valueDouble, 0.01);
		Assert::assertEquals(10.2, fields4.get(5).restrictions.get(0).restrictionArguments.get(1).valueDouble, 0.01);
		Assert::assertEquals("exclusive, inclusive",
			fields4.get(5).restrictions.get(0).restrictionArguments.get(2).valueString);
	}

	@Test
	def void testRestrictions9() {
		val specification = testRestrictions.parse

		val usertype1 = specification.declarations.get(0) as UsertypeImpl;
		val usertype2 = specification.declarations.get(1) as UsertypeImpl;
		val typedef = specification.declarations.get(4) as TypedefImpl;
		Assert::assertEquals("oneof", typedef.restrictions.get(0).restrictionName);
		Assert::assertEquals(usertype1 as TypeDeclaration,
			typedef.restrictions.get(0).restrictionArguments.get(0).valueType.type);
		Assert::assertEquals(usertype2 as TypeDeclaration,
			typedef.restrictions.get(0).restrictionArguments.get(1).valueType.type);
	}

	@Test
	def void testInterfaces() {
		val specification = testInterfaces.parse

		val I = specification.declarations.get(0) as InterfacetypeImpl;
		Assert::assertEquals("/*interface comment*/", I.comment);
		Assert::assertEquals("i", I.name);

		val fields1 = I.fields;
		val type1 = specification.declarations.get(1) as TypeDeclarationImpl;
		Assert::assertEquals("field1", fields1.get(0).fieldcontent.name);
		Assert::assertEquals("field2", fields1.get(1).fieldcontent.name);
		Assert::assertEquals("field3", fields1.get(2).fieldcontent.name);
		Assert::assertEquals("field4", fields1.get(3).fieldcontent.name);
		Assert::assertEquals("field5", fields1.get(4).fieldcontent.name);
		Assert::assertEquals("annotation", (fields1.get(0).fieldcontent.fieldtype as Annotationtype).type);
		Assert::assertEquals("string", (fields1.get(1).fieldcontent.fieldtype as Stringtype).type);
		Assert::assertEquals(Integer.I32, (fields1.get(2).fieldcontent.fieldtype as Integertype).type);
		Assert::assertEquals(type1, (fields1.get(3).fieldcontent.fieldtype as DeclarationReferenceImpl).type);
		Assert::assertEquals(ListtypeImpl, fields1.get(4).fieldcontent.fieldtype.class);
		Assert::assertEquals(1234, (fields1.get(2).fieldcontent as ConstantImpl).value);
		Assert::assertEquals(Float.F64,
			((fields1.get(4).fieldcontent.fieldtype as ListtypeImpl).basetype as Floattype).type);

		val type2 = specification.declarations.get(2) as TypeDeclarationImpl;
		val int2 = specification.declarations.get(3) as InterfacetypeImpl;
		val type3 = specification.declarations.get(4) as TypeDeclarationImpl;
		val int3 = specification.declarations.get(5);
		Assert::assertEquals(I, type2.supertypes.get(0).type);
		Assert::assertEquals(type1, type2.supertypes.get(2).type);
		Assert::assertEquals(I, int2.supertypes.get(0).type);
		Assert::assertEquals(type1, int2.supertypes.get(1).type);
		Assert::assertEquals(type2, type3.supertypes.get(0).type);
		Assert::assertEquals(int2, type3.supertypes.get(1).type);
		Assert::assertEquals(int3, type3.supertypes.get(2).type);
	}

	@Test
	def void testAuto() {
		val specification = testAuto.parse

		val int1 = specification.declarations.get(0) as UsertypeImpl;
		val area = int1.fields.get(2);
		Assert::assertEquals(true, (area.fieldcontent as DataImpl).isAuto)
	}
	
}