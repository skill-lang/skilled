package test

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ConstantImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DeclarationReferenceImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.EnumtypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.InterfacetypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.ListtypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.TypeDeclarationImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.TypedefImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.UsertypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.Integer
import de.unistuttgart.iste.ps.skilled.sKilL.Float
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import de.unistuttgart.iste.ps.skilled.sKilL.Annotationtype
import de.unistuttgart.iste.ps.skilled.sKilL.Stringtype
import de.unistuttgart.iste.ps.skilled.sKilL.Integertype
import de.unistuttgart.iste.ps.skilled.sKilL.Floattype

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestInterfacesEnumsTypedefs {

	@Inject extension ParseHelper<File> parser;
	
	@Test
	def void testInterfaces1() {
  		val specification = '''
  			/*interface comment*/
  			interface Int1 {
  				annotation field1;
  				string field2;
  				const i32 field3 = 1234;
  				Type1 field4;
  				list<f64> field5;
  			}
  			
  			Type1 {
  				bool field6;
  			}
  			
  			Type2 with I:I extends Type1:I {
  				string[] field7;
  			}
  			
  			interface Int2:I:Type1 with I {
  				annotation field8;
  			}
  			
  			Type3:Type2 extends Int2 with Int3:Int3 {
  				f64 field9;
  			}
  			
  			interface Int3 {
  			}
  		'''.parse
  		
  		val int1 = specification.declarations.get(0) as InterfacetypeImpl;
  		Assert::assertEquals("/*interface comment*/", int1.comment);
  		Assert::assertEquals("Int1", int1.name);
  		val fields1 = int1.fields;
  		Assert::assertEquals("field1", fields1.get(0).fieldcontent.name);
  		Assert::assertEquals("field2", fields1.get(1).fieldcontent.name);
  		Assert::assertEquals("field3", fields1.get(2).fieldcontent.name);
  		Assert::assertEquals("field4", fields1.get(3).fieldcontent.name);
  		Assert::assertEquals("field5", fields1.get(4).fieldcontent.name);
  	}
  	
  	@Test
	def void testInterfaces2() {
  		val specification = '''
  			/*interface comment*/
  			interface Int1 {
  				annotation field1;
  				string field2;
  				const i32 field3 = 1234;
  				Type1 field4;
  				list<f64> field5;
  			}
  			
  			Type1 {
  				bool field6;
  			}
  			
  			Type2 with Int1:Int1 extends Type1:Int1 {
  				string[] field7;
  			}
  			
  			interface Int2:Int1:Type1 with Int1 {
  				annotation field8;
  			}
  			
  			Type3:Type2 extends Int2 with Int3:Int3 {
  				f64 field9;
  			}
  			
  			interface Int3 {
  			}
  		'''.parse
  		
  		val int1 = specification.declarations.get(0) as InterfacetypeImpl;
  		val fields1 = int1.fields;
  		val type1 = specification.declarations.get(1) as TypeDeclarationImpl;
  		
  		Assert::assertEquals("annotation", (fields1.get(0).fieldcontent.fieldtype as Annotationtype).type);
  		Assert::assertEquals("string", (fields1.get(1).fieldcontent.fieldtype as Stringtype).type);
  		Assert::assertEquals(Integer.I32, (fields1.get(2).fieldcontent.fieldtype as Integertype).type);
  		Assert::assertEquals(type1, (fields1.get(3).fieldcontent.fieldtype as DeclarationReferenceImpl).type);
  		Assert::assertEquals(ListtypeImpl, fields1.get(4).fieldcontent.fieldtype.class);
  		Assert::assertEquals(1234, (fields1.get(2).fieldcontent as ConstantImpl).value);
  		Assert::assertEquals(Float.F64, ((fields1.get(4).fieldcontent.fieldtype as ListtypeImpl).basetype as Floattype).type);
  	}
  	
  	@Test
	def void testInterfaces3() {
  		val specification = '''
  			/*interface comment*/
  			interface Int1 {
  				annotation field1;
  				string field2;
  				const i32 field3 = 1234;
  				Type1 field4;
  				list<f64> field5;
  			}
  			
  			Type1 {
  				bool field6;
  			}
  			
  			Type2 with Int1:Int1 extends Type1:Int1 {
  				string[] field7;
  			}
  			
  			interface Int2:Int1:Type1 with Int1 {
  				annotation field8;
  			}
  			
  			Type3:Type2 extends Int2 with Int3:Int3 {
  				f64 field9;
  			}
  			
  			interface Int3 {
  			}
  		'''.parse
  		
  		val int1 = specification.declarations.get(0) as InterfacetypeImpl;
  		val type1 = specification.declarations.get(1) as TypeDeclarationImpl;
  		val type2 = specification.declarations.get(2) as TypeDeclarationImpl;
  		val int2 = specification.declarations.get(3) as InterfacetypeImpl;
  		val type3 = specification.declarations.get(4) as TypeDeclarationImpl;
  		val int3 = specification.declarations.get(5);
  		
  		Assert::assertEquals(int1, type2.supertypes.get(0).type);
  		Assert::assertEquals(int1, type2.supertypes.get(1).type);
  		Assert::assertEquals(type1, type2.supertypes.get(2).type);
  		Assert::assertEquals(int1, int2.supertypes.get(0).type);
  		Assert::assertEquals(type1, int2.supertypes.get(1).type);
  		Assert::assertEquals(type2, type3.supertypes.get(0).type);
  		Assert::assertEquals(int2, type3.supertypes.get(1).type);
  		Assert::assertEquals(int3, type3.supertypes.get(2).type);
  	}
  	
  	@Test
  	def void testEnum() {
  		val specification = '''
  			/*enum description*/
  			enum Day {
  				Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
  				i8 day;
  				i8 month;
  			}
  			
  			Birthday {
  				i8 receivedPresents;
  				i8 invitedGuests;
  				Day day;
  			}
  		'''.parse
  		
  		val enum = specification.declarations.get(0) as EnumtypeImpl;
  		Assert::assertEquals("/*enum description*/", enum.comment);
  		Assert::assertEquals("Day", enum.name);
  		Assert::assertEquals("Monday", enum.instances.get(0));
  		Assert::assertEquals("Sunday", enum.instances.get(6));
  		Assert::assertEquals("day", enum.fields.get(0).fieldcontent.name);
  		Assert::assertEquals(Integer.I8, (enum.fields.get(0).fieldcontent.fieldtype as Integertype).type);
  		val day = (specification.declarations.get(1) as UsertypeImpl).fields.get(2);
  		Assert::assertEquals(enum, (day.fieldcontent.fieldtype as DeclarationReferenceImpl).type);
  	}
  	
  	@Test
  	def void testTypedef1() {
  		val specification = '''
  			/*typedef description*/
  			typedef Asdf @unique !removeUnknownRestrictions("unknown") @monotone A;

  			A {}
  			
  			B {}
  			
  			typedef Natural @min(0) i64;
  			
  			typedef AorB @oneOf(A, B) annotation;
  		'''.parse
  		
  		val asdf = specification.declarations.get(0) as TypedefImpl;
  		Assert::assertEquals("/*typedef description*/", asdf.comment);
  		Assert::assertEquals("Asdf", asdf.name);
  		Assert::assertEquals("unique", asdf.restrictions.get(0).restrictionName);
  		Assert::assertEquals("monotone", asdf.restrictions.get(1).restrictionName);
  		Assert::assertEquals("removeUnknownRestrictions", asdf.hints.get(0).hintName);
  		Assert::assertEquals("unknown", asdf.hints.get(0).hintArguments.get(0).valueString);
  	}
  	
  	@Test
  	def void testTypedef2() {
  		val specification = '''
  			/*typedef description*/
  			typedef Asdf @unique !removeUnknownRestrictions("unknown") @monotone A;

  			A {}
  			
  			B {}
  			
  			typedef Natural @min(0) i64;
  			
  			typedef AorB @oneOf(A, B) annotation;
  		'''.parse
  		
  		val asdf = specification.declarations.get(0) as TypedefImpl;
		val a = specification.declarations.get(1) as UsertypeImpl;
		val b = specification.declarations.get(2) as UsertypeImpl;
		val natural = specification.declarations.get(3) as TypedefImpl;
		val aORb = specification.declarations.get(4) as TypedefImpl;
		Assert::assertEquals(a, (asdf.fieldtype as DeclarationReferenceImpl).type);
		
		Assert::assertEquals("Natural", natural.name);
		Assert::assertEquals("min", natural.restrictions.get(0).restrictionName);
		Assert::assertEquals(0, natural.restrictions.get(0).restrictionArguments.get(0).valueLong);
		Assert::assertEquals(Integer.I64, (natural.fieldtype as Integertype).type);
		
		Assert::assertEquals("oneOf", aORb.restrictions.get(0).restrictionName);
		Assert::assertEquals(b, aORb.restrictions.get(0).restrictionArguments.get(1).valueType.type);
		Assert::assertEquals("AorB", aORb.name);
		Assert::assertEquals("annotation", (aORb.fieldtype as Annotationtype).type);
  	}
}