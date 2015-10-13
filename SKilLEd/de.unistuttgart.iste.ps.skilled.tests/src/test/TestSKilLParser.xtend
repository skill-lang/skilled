package test

import static org.junit.Assert.*;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;

import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider;
import de.unistuttgart.iste.ps.skilled.sKilL.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.validation.SKilLValidator
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import org.junit.Assert
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.sKilL.impl.FieldtypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInType
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInTypeReference
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ConstantImpl
import org.eclipse.emf.ecore.util.EDataTypeEList

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

		val usertype = specification.declarations.get(0) as Usertype;
		Assert::assertEquals("UserType", usertype.name);
		val fields = usertype.fields;
		val field1 = fields.get(0);
		Assert::assertEquals("int1", field1.fieldcontent.name);
		Assert::assertEquals(BuildInType.I8, (field1.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field2 = fields.get(1);
		Assert::assertEquals("int2", field2.fieldcontent.name);
		Assert::assertEquals(BuildInType.I16, (field2.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field3 = fields.get(2);
		Assert::assertEquals("int3", field3.fieldcontent.name);
		Assert::assertEquals(BuildInType.I32, (field3.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field4 = fields.get(3);
		Assert::assertEquals("int4", field4.fieldcontent.name);
		Assert::assertEquals(BuildInType.I64, (field4.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field5 = fields.get(4);
		Assert::assertEquals("int5", field5.fieldcontent.name);
		Assert::assertEquals(BuildInType.V64, (field5.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field6 = fields.get(5);
		Assert::assertEquals("float1", field6.fieldcontent.name);
		Assert::assertEquals(BuildInType.F32, (field6.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field7 = fields.get(6);
		Assert::assertEquals("float2", field7.fieldcontent.name);
		Assert::assertEquals(BuildInType.F64, (field7.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field8 = fields.get(7);
		Assert::assertEquals("str", field8.fieldcontent.name);
		Assert::assertEquals(BuildInType.STRING, (field8.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field9 = fields.get(8);
		Assert::assertEquals("b", field9.fieldcontent.name);
		Assert::assertEquals(BuildInType.BOOLEAN, (field9.fieldcontent.fieldtype as BuildInTypeReference).type)
		val field10 = fields.get(9);
		Assert::assertEquals("a", field10.fieldcontent.name);
		Assert::assertEquals(BuildInType.ANNOTATION, (field10.fieldcontent.fieldtype as BuildInTypeReference).type)
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

		val usertype = specification.declarations.get(0) as Usertype;
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
				!auto
				i32 field;
			}
		'''.parse

		val usertype = specification.declarations.get(0) as Usertype;
		Assert::assertEquals("removeUnknownRestrictions", usertype.hints.get(0).hintName);
		Assert::assertEquals("unique",(usertype.hints.get(0).hintArguments.get(0).valueString));
		Assert::assertEquals("monotone",(usertype.hints.get(0).hintArguments.get(1).valueString));
		Assert::assertEquals("mixin", usertype.hints.get(1).hintName);
		Assert::assertEquals("unique", usertype.hints.get(2).hintName);
		Assert::assertEquals("pure", usertype.hints.get(3).hintName);
		Assert::assertEquals("monotone", usertype.hints.get(4).hintName);
		Assert::assertEquals("readOnly", usertype.hints.get(5).hintName);
		val fields = usertype.fields;
		val field1 = fields.get(0);
		Assert::assertEquals("constantMutator", field1.hints.get(0).hintName);
		Assert::assertEquals(-17, field1.hints.get(0).hintArguments.get(0).valueLong);
		Assert::assertEquals(3000000000L, field1.hints.get(0).hintArguments.get(1).valueLong);
		val field2 = fields.get(1);
		Assert::assertEquals("distributed", field2.hints.get(0).hintName);
		Assert::assertEquals("onDemand", field2.hints.get(1).hintName);
		val field3 = fields.get(2);
		Assert::assertEquals("ignore", field3.hints.get(0).hintName);
		val field4 = fields.get(3);
		Assert::assertEquals("hide", field4.hints.get(0).hintName);

		val usertype2 = specification.declarations.get(1) as Usertype;
		Assert::assertEquals("flat", usertype2.hints.get(0).hintName);
		val field = usertype2.fields.get(0);
		Assert::assertEquals("auto", field.hints.get(0).hintName);
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
			usertype.comment);
		val field = usertype.fields.get(0);
		Assert::assertEquals("/*field comment" + System.getProperty("line.separator") + "  second line*/",
			field.comment);
	}
/* TODO WIP
 * @Test
 * def void testRestrictions() {
 * 	val specification =
 * 	@unique
 * 	@default(Type2)
 * 	Type1 {
 * 		@default(-3000000000)
 * 		i64 field1;
 * 		@default("Akemi")
 * 		string field2;
 * 		@default(-123.456)
 * 		f64 field3;
 * 	}
 * 	
 * 	@singleton
 * 	@monotone
 * 	Type2 {
 * 		@nonnull
 * 		Type4 field4;
 * 	}
 * 	
 * 	@abstract
 * 	Type3 {
 * 		
 * 	}
 * 	
 * 	Type4 {
 * 		@min(-3000000000)
 * 		@min(-0xABCD, "inclusive")
 * 		@max(-3000000000)
 * 		@max(-0xABCD, "exclusive")
 * 		@range
 * 		@range
 * 	}
 } */
}