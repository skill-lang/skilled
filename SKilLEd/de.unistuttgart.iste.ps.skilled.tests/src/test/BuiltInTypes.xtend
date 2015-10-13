package test

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;

import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider;
import de.unistuttgart.iste.ps.skilled.sKilL.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import org.junit.Assert
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInType
import de.unistuttgart.iste.ps.skilled.sKilL.BuildInTypeReference
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ConstantImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.TypeDeclarationImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.UsertypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.MaptypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ListtypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DataImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DeclarationReferenceImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ArraytypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.SettypeImpl

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class BuiltInTypes {

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
  		val specification = '''
  			Rectangle {
  				i32 width;
  				i32 length;
  				auto i32 area;
  				}
  			}
  		'''.parse
  		
  		val int1 = specification.declarations.get(0) as UsertypeImpl;
  		val area = int1.fields.get(2);
  		Assert::assertEquals(true, (area.fieldcontent as DataImpl).isAuto)
  	}
  	
  	@Test
  	def void testCompoundTypes1() {
  		val specification = '''
  			Type {
  				/*comment*/
  				bool[] a;
  				
  				@nonnull
  				list<f64> b;
  				
  				!readOnly
  				map<i32,Type,string,bool> c;
  				
  				set<v64> d;
  				
  				Type[80] e;
  			}
  		'''.parse
  		
  		val type = specification.declarations.get(0) as UsertypeImpl;
  		val a = type.fields.get(0);
  		Assert::assertEquals(typeof(ArraytypeImpl), a.fieldcontent.fieldtype.class);
  		Assert::assertEquals(BuildInType.BOOLEAN, ((a.fieldcontent.fieldtype as ArraytypeImpl).basetype as BuildInTypeReference).type);
  		Assert::assertEquals("/*comment*/", a.comment);
  		val b = type.fields.get(1);
  		Assert::assertEquals(typeof(ListtypeImpl), b.fieldcontent.fieldtype.class);
  		Assert::assertEquals(BuildInType.F64, ((b.fieldcontent.fieldtype as ListtypeImpl).basetype as BuildInTypeReference).type);
  		Assert::assertEquals("nonnull", b.restrictions.get(0).restrictionName);
  	}
  	
  	@Test
  	def void testCompoundTypes2() {
  		val specification = '''
  			Type {
  				/*comment*/
  				bool[] a;
  				
  				@nonnull
  				list<f64> b;
  				
  				!readOnly
  				map<i32,Type,string,bool> c;
  				
  				set<v64> d;
  				
  				Type[80] e;
  			}
  		'''.parse
  		
  		val type = specification.declarations.get(0) as UsertypeImpl;
  		val c = type.fields.get(2);
  		Assert::assertEquals(typeof(MaptypeImpl), c.fieldcontent.fieldtype.class);
  		Assert::assertEquals(BuildInType.I32, ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(0) as BuildInTypeReference).type);
  		Assert::assertEquals(type, ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(1) as DeclarationReferenceImpl).type);
  		Assert::assertEquals(BuildInType.STRING, ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(2) as BuildInTypeReference).type);
  		Assert::assertEquals(BuildInType.BOOLEAN, ((c.fieldcontent.fieldtype as MaptypeImpl).basetypes.get(3) as BuildInTypeReference).type);
  		Assert::assertEquals("readOnly", c.hints.get(0).hintName);
  	}
  	
  	@Test
  	def void testCompoundTypes3() {
  		val specification = '''
  			Type {
  				/*comment*/
  				bool[] a;
  				
  				@nonnull
  				list<f64> b;
  				
  				!readOnly
  				map<i32,Type,string,bool> c;
  				
  				set<v64> d;
  				
  				Type[80] e;
  			}
  		'''.parse
  		
  		val type = specification.declarations.get(0) as UsertypeImpl;
  		val d = type.fields.get(3);
  		Assert::assertEquals(typeof(SettypeImpl), d.fieldcontent.fieldtype.class);
  		Assert::assertEquals(BuildInType.V64, ((d.fieldcontent.fieldtype as SettypeImpl).basetype as BuildInTypeReference).type);
  		val e = type.fields.get(4);
  		Assert::assertEquals(type, ((e.fieldcontent.fieldtype as ArraytypeImpl).basetype as DeclarationReferenceImpl).type);
  		//the boundary (80 in this case) seems to be not testable (yet?)
  	}
}