package test

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype
import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.Float
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Integer
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ArraytypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ConstantImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DataImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DeclarationReferenceImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ListtypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.MaptypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.SettypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.TypeDeclarationImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.UsertypeImpl
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
class TestBuiltInTypes {

	@Inject extension ParseHelper<File> parser;
	
	var static String testBasicFieldTypes = "";
	var static String testConstants = "";
	var static String testAuto = "";
	var static String testCompoundTypes = "";
	
	@BeforeClass
	def static void setup() {
		testBasicFieldTypes = FileLoader.loadFile("TestBasicFieldTypes");
		testConstants = FileLoader.loadFile("TestConstants");
		testAuto = FileLoader.loadFile("TestAuto");
		testCompoundTypes = FileLoader.loadFile("TestCompoundTypes");
	}

	@Test
	def void testBasicFieldTypes() {
		val specification = testBasicFieldTypes.parse

		val usertype = specification.declarations.get(0) as TypeDeclarationImpl;
		Assert::assertEquals("UserType", usertype.name);
		val fields = usertype.fields;
		val field1 = fields.get(0);
		Assert::assertEquals("int1", field1.fieldcontent.name);
		//EXAMPLE FOR NEW STRUCTURE FOR INT
		Assert::assertEquals(Integer.I8, (field1.fieldcontent.fieldtype as Integertype).type)	
		val field2 = fields.get(1);
		Assert::assertEquals("int2", field2.fieldcontent.name);
		Assert::assertEquals(Integer.I16, (field2.fieldcontent.fieldtype as Integertype).type)
		val field3 = fields.get(2);
		Assert::assertEquals("int3", field3.fieldcontent.name);
		Assert::assertEquals(Integer.I32, (field3.fieldcontent.fieldtype as Integertype).type)
		val field4 = fields.get(3);
		Assert::assertEquals("int4", field4.fieldcontent.name);
		Assert::assertEquals(Integer.I64, (field4.fieldcontent.fieldtype as Integertype).type)
		val field5 = fields.get(4);
		Assert::assertEquals("int5", field5.fieldcontent.name);
		Assert::assertEquals(Integer.V64, (field5.fieldcontent.fieldtype as Integertype).type)
		val field6 = fields.get(5);
		Assert::assertEquals("float1", field6.fieldcontent.name);
		//EXAMPLE FOR NEW STRUCTURE FOR FLOAT
		Assert::assertEquals(Float.F32, (field6.fieldcontent.fieldtype as Floattype).type)
		val field7 = fields.get(6);
		Assert::assertEquals("float2", field7.fieldcontent.name);
		Assert::assertEquals(Float.F64, (field7.fieldcontent.fieldtype as Floattype).type)
		val field8 = fields.get(7);
		Assert::assertEquals("str", field8.fieldcontent.name);
		//EXAMPLE FOR NEW STRUCTURE FOR STRING
		Assert::assertEquals("string", (field8.fieldcontent.fieldtype as Stringtype).type)
		val field9 = fields.get(8);
		Assert::assertEquals("b", field9.fieldcontent.name);
		//EXAMPLE FOR NEW STRUCTURE FOR BOOLEAN
		Assert::assertEquals("bool", (field9.fieldcontent.fieldtype as Booleantype).type)
		val field10 = fields.get(9);
		Assert::assertEquals("a", field10.fieldcontent.name);
		//EXAMPLE FOR NEW STRUCTURE FOR ANNOTATION
		Assert::assertEquals("annotation", (field10.fieldcontent.fieldtype as Annotationtype).type)
	}

	@Test
	def void testConstants() {
		val specification = testConstants.parse

		val usertype = specification.declarations.get(0) as TypeDeclarationImpl;
		val fields = usertype.fields;
		val field1 = fields.get(0);
		Assert::assertEquals(ConstantImpl, field1.fieldcontent.class);
		Assert::assertEquals(-17, (field1.fieldcontent as ConstantImpl).value);
		val field2 = fields.get(1);
		Assert::assertEquals(ConstantImpl, field2.fieldcontent.class);
		Assert::assertEquals(3000000000L, (field2.fieldcontent as ConstantImpl).value);
		val field3 = fields.get(2);
		Assert::assertEquals(ConstantImpl, field3.fieldcontent.class);
		Assert::assertEquals(0xABCD, (field3.fieldcontent as ConstantImpl).value);
	}

  	@Test
  	def void testAuto() {
  		val specification = testAuto.parse
  		
  		val int1 = specification.declarations.get(0) as UsertypeImpl;
  		val area = int1.fields.get(2);
  		Assert::assertEquals(true, (area.fieldcontent as DataImpl).isAuto)
  	}
  	
  	@Test
  	def void testCompoundTypes1() {
  		val specification = testCompoundTypes.parse
  		
  		val type = specification.declarations.get(0) as UsertypeImpl;
  		val a = type.fields.get(0);
  		Assert::assertEquals(typeof(ArraytypeImpl), a.fieldcontent.fieldtype.class);
  		// How to solve this?
  		Assert::assertEquals("bool", ((a.fieldcontent.fieldtype as ArraytypeImpl).basetype as Booleantype).type);
  		Assert::assertEquals("/*comment*/", a.comment);
  		val b = type.fields.get(1);
  		Assert::assertEquals(typeof(ListtypeImpl), b.fieldcontent.fieldtype.class);
  		Assert::assertEquals(Float.F64, ((b.fieldcontent.fieldtype as ListtypeImpl).basetype as Floattype).type);
  		Assert::assertEquals("nonnull", b.restrictions.get(0).restrictionName);
  	}
  	
  	@Test
  	def void testCompoundTypes2() {
  		val specification = testCompoundTypes.parse
  		
  		val type = specification.declarations.get(0) as UsertypeImpl;
  		val c = type.fields.get(2);
  		Assert::assertEquals(typeof(MaptypeImpl), c.fieldcontent.fieldtype.class);
  		Assert::assertEquals(Integer.I32, ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(0) as Integertype).type);
  		Assert::assertEquals(type, ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(1) as DeclarationReferenceImpl).type);
  		Assert::assertEquals("string", ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(2) as Stringtype).type);
  		Assert::assertEquals("bool", ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(3) as Booleantype).type);
  		Assert::assertEquals("readOnly", c.hints.get(0).hintName);
  	}
  	
  	@Test
  	def void testCompoundTypes3() {
  		val specification = testCompoundTypes.parse
  		
  		val type = specification.declarations.get(0) as UsertypeImpl;
  		val d = type.fields.get(3);
  		Assert::assertEquals(typeof(SettypeImpl), d.fieldcontent.fieldtype.class);
  		Assert::assertEquals(Integer.V64, ((d.fieldcontent.fieldtype as SettypeImpl).basetype as Integertype).type);
  		val e = type.fields.get(4);
  		Assert::assertEquals(type, ((e.fieldcontent.fieldtype as ArraytypeImpl).basetype as DeclarationReferenceImpl).type);
  		//the boundary (80 in this case) seems to be not testable (yet?)
  	}
}