package test

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.Arraytype
import de.unistuttgart.iste.ps.skilled.sKilL.Data
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ConstantImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DeclarationImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.HintImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.TypeDeclarationImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.UsertypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.Integer
import de.unistuttgart.iste.ps.skilled.sKilL.Float
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.Booleantype
import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestSKilLParser {

	@Inject extension ParseHelper<File> parser;

	@Test
	def void testBasicFieldTypes() {
		val specification = '''
			UserType {
				i8 int1;
				i16 int2;
				i32 int3;
				i64 int4;
				v64 int5;
				f32 float1;
				f64 float2;
				string str;
				bool b;
				annotation a;
			}    	
		'''.parse

		val usertype = specification.declarations.get(0) as TypeDeclarationImpl;
		Assert::assertEquals("UserType", usertype.name);
		val fields = usertype.fields;
		val field1 = fields.get(0);
		Assert::assertEquals("int1", field1.fieldcontent.name);
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
		Assert::assertEquals(Float.F32, (field6.fieldcontent.fieldtype as Floattype).type)
		val field7 = fields.get(6);
		Assert::assertEquals("float2", field7.fieldcontent.name);
		Assert::assertEquals(Float.F64, (field7.fieldcontent.fieldtype as Floattype).type)
		val field8 = fields.get(7);
		Assert::assertEquals("str", field8.fieldcontent.name);
		Assert::assertEquals("string", (field8.fieldcontent.fieldtype as Stringtype).type)
		val field9 = fields.get(8);
		Assert::assertEquals("b", field9.fieldcontent.name);
		Assert::assertEquals("bool", (field9.fieldcontent.fieldtype as Booleantype).type)
		val field10 = fields.get(9);
		Assert::assertEquals("a", field10.fieldcontent.name);
		Assert::assertEquals("annotation", (field10.fieldcontent.fieldtype as Annotationtype).type)
	}

	@Test
	def void testConstants() {
		val specification = '''
			UserType {
				const i32 field1 = -17;
				const i64 field2 = 3000000000;
				const i16 field3 = 0xABCD;
			}
		'''.parse

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
	def void testHints() {
		val specification = '''
			!removeUnknownRestrictions("unique", "monotone")
			!mixin
			!unique
			!pure
			!monotone
			!readOnly
			UserType1 {
				!constantMutator(-17, 3000000000)
				const i64 field1 = 2500000000;
				!distributed
				!onDemand
				i32 field2;
				!ignore
				i32 field3;
				!hide
				i32 field4;
			}
			
			!flat
			UserType2 {
				!pragma(tree)
				i32 field;
			}
		'''.parse

		val usertype = specification.declarations.get(0) as UsertypeImpl;
		Assert::assertEquals("removeUnknownRestrictions", usertype.hints.get(0).hintName);
		Assert::assertEquals("unique",((usertype.hints.get(0) as HintImpl).hintArguments.get(0).valueString));
		Assert::assertEquals("monotone",((usertype.hints.get(0) as HintImpl).hintArguments.get(1).valueString));
		Assert::assertEquals("mixin", usertype.hints.get(1).hintName);
		Assert::assertEquals("unique", usertype.hints.get(2).hintName);
		Assert::assertEquals("pure", usertype.hints.get(3).hintName);
		Assert::assertEquals("monotone", usertype.hints.get(4).hintName);
		Assert::assertEquals("readOnly", usertype.hints.get(5).hintName);
		val fields = usertype.fields;
		val field1 = fields.get(0);
		Assert::assertEquals("constantMutator", field1.hints.get(0).hintName);
		Assert::assertEquals(-17, (field1.hints.get(0) as HintImpl).hintArguments.get(0).valueLong);
		Assert::assertEquals(3000000000L, (field1.hints.get(0) as HintImpl).hintArguments.get(1).valueLong);
		val field2 = fields.get(1);
		Assert::assertEquals("distributed", field2.hints.get(0).hintName);
		Assert::assertEquals("onDemand", field2.hints.get(1).hintName);
		val field3 = fields.get(2);
		Assert::assertEquals("ignore", field3.hints.get(0).hintName);
		val field4 = fields.get(3);
		Assert::assertEquals("hide", field4.hints.get(0).hintName);

		val usertype2 = specification.declarations.get(1) as UsertypeImpl;
		Assert::assertEquals("flat", usertype2.hints.get(0).hintName);
		val field = usertype2.fields.get(0);
		Assert::assertEquals("pragma", field.hints.get(0).hintName);
		Assert::assertEquals("tree", field.hints.get(0).hintArguments.get(0).valueIdentifier);
	}

	@Test
	def void testComments() {
		val specification = '''
#head comment
#head comment 2
		
/*usertype comment
second line*/
!unique
Usertype {
  /*field comment
  second line*/
  !auto
  i32 field;
}
		'''.parse

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
	def void testRestrictions() {
		val specification = '''
			@unique
			@default(Type2)
			Type1 {
				@default(-3000000000)
				i64 field1;
				@default("Akemi")
				string field2;
				@default(-123.456)
				 	f64 field3;
			}
			
			@singleton
			@monotone
			Type2 {
				@nonnull
				@constantLengthPointer
				Type4 field4;
				@constantLengthPointer
				string s;
				@constantLengthPointer
				@oneOf(Type1, Type2, Type3, Type4)
				annotation a;
			}
			
				@abstract
				Type3 {
				@coding("zip")
				i32 field5;
			}
			
				Type4 {
				@min(-3000000000)
				i32 field6;
				@min(-0xABCD.3Dp-5, "inclusive")
				f32 field7;
				@max(-3000000000)
				i32 field8;
				@max(-0xABCD.3Dp-5, "exclusive")
				f32 field9;
				@range(-10, 10)
				i32 field10;
				@range(-10.7, 10.2, "exclusive, inclusive")
				f32 field11;
			}
			
			@oneOf(Type1, Type2)
			typedef annotation Type5;
		'''.parse
	}

	@Test
	def void hexint() {
		val specification = '''
			foo{
			string[-0xCAB] bar;
			}
		'''.parse

		val foo = specification.declarations.get(0) as Usertype;
		val bar = foo.fields.get(0);
		val barArray = (bar.fieldcontent as Data).fieldtype as Arraytype
		Assert::assertEquals(-0xCAB, barArray.length);
		Assert::assertEquals(-3243, barArray.length);

	}
}
