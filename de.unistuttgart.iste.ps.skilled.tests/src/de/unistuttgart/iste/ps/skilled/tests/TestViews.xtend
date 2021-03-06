package de.unistuttgart.iste.ps.skilled.tests;

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SkillInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.DeclarationReference
import de.unistuttgart.iste.ps.skilled.skill.File
import de.unistuttgart.iste.ps.skilled.skill.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.skill.Usertype
import de.unistuttgart.iste.ps.skilled.skill.View
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
 */
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestViews {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;

	var static String view1 = "";
	var static String view2 = "";
	var static String view3 = "";
	var static String view4 = "";
	var static String view5 = "";
	var static String view6 = "";
	var static String view7 = "";

	@BeforeClass
	def static void setup() {
		view1 = FileLoader.loadFile("views/TestView1");
		view2 = FileLoader.loadFile("views/TestView2");
		view3 = FileLoader.loadFile("views/TestView3");
		view4 = FileLoader.loadFile("views/TestView4");
		view5 = FileLoader.loadFile("views/TestView5");
		view6 = FileLoader.loadFile("views/TestView6");
		view7 = FileLoader.loadFile("views/TestView7");
	}

	@Test
	def void testView1() {
		val specification = view1.parse
		
		Assert::assertTrue(specification.validate.size == 0);
		
		val A = specification.declarations.get(0) as Usertype;
		val B = specification.declarations.get(1) as Usertype;
		val C = specification.declarations.get(2) as Usertype;
		val view = A.fields.get(0).fieldcontent as View;
		Assert::assertEquals("neuername", view.name.toLowerCase);
		Assert::assertEquals(C, (view.fieldtype as DeclarationReference).type);
		Assert::assertEquals(B.fields.get(0).fieldcontent, view.fieldcontent.fieldcontent)
	}

	@Test
	def void testView2() {
		val specification = view2.parse
		
		Assert::assertTrue(specification.validate.size == 0);
		
		val A = specification.declarations.get(0) as Usertype;
		val B = specification.declarations.get(1) as Usertype;
		val D = specification.declarations.get(3) as Usertype;
		val view = A.fields.get(0).fieldcontent as View;
		Assert::assertEquals("field", view.name.toLowerCase);
		Assert::assertEquals(D, (view.fieldtype as DeclarationReference).type);
		Assert::assertEquals(B.fields.get(0).fieldcontent, view.fieldcontent.fieldcontent)
	}

	@Test
	def void testView3() {
		val specification = view3.parse
		
		Assert::assertTrue(specification.validate.size == 0);
		
		val A = specification.declarations.get(0) as Usertype;
		val C = specification.declarations.get(2) as TypeDeclaration;
		val D = specification.declarations.get(3) as Usertype;
		val view = A.fields.get(0).fieldcontent as View;
		Assert::assertEquals("asdf", view.name.toLowerCase);
		Assert::assertEquals(D, (view.fieldtype as DeclarationReference).type);
		Assert::assertEquals(C.fields.get(0).fieldcontent, view.fieldcontent.fieldcontent)
	}

	@Test
	def void testView4() {
		val specification = view4.parse
		
		Assert::assertTrue(specification.validate.size == 0);
	}

	@Test
	def void testView5() {
		val specification = view5.parse
		
		Assert::assertTrue(specification.validate.size == 0);
		
		val A = specification.declarations.get(0) as Usertype;
		val B = specification.declarations.get(1) as Usertype;
		val C = specification.declarations.get(2) as Usertype;
		val E = specification.declarations.get(4) as Usertype;
		val F = specification.declarations.get(5) as TypeDeclaration;
		val view1 = A.fields.get(0).fieldcontent as View;
		val view2 = B.fields.get(0).fieldcontent as View;
		Assert::assertEquals("asdf", view1.name.toLowerCase);
		Assert::assertEquals("field", view2.name.toLowerCase);
		Assert::assertEquals(F, (view1.fieldtype as DeclarationReference).type);
		Assert::assertEquals(E, (view2.fieldtype as DeclarationReference).type);
		Assert::assertEquals(view2, view1.fieldcontent.fieldcontent);
		Assert::assertEquals(C.fields.get(0).fieldcontent, view2.fieldcontent.fieldcontent);
	}

	@Test
	def void testView6() {
		val specification = view6.parse
		
		Assert::assertTrue(specification.validate.size == 0);
		
		val A = specification.declarations.get(0) as TypeDeclaration;
		val B = specification.declarations.get(1) as TypeDeclaration;
		val D = specification.declarations.get(3) as Usertype;
		val view = A.fields.get(0).fieldcontent as View;
		Assert::assertEquals("field", view.name.toLowerCase);
		Assert::assertEquals(D, (view.fieldtype as DeclarationReference).type);
		Assert::assertEquals(B.fields.get(0).fieldcontent, view.fieldcontent.fieldcontent)
	}

	@Test
	def void testView7() {
		val specification = view7.parse
		
		Assert::assertTrue(specification.validate.size == 0);
		
		val A = specification.declarations.get(0) as TypeDeclaration;
		val B = specification.declarations.get(1) as TypeDeclaration;
		val C = specification.declarations.get(2) as Usertype;
		val view = A.fields.get(0).fieldcontent as View;
		Assert::assertEquals("field2", view.name.toLowerCase);
		Assert::assertEquals(C, (view.fieldtype as DeclarationReference).type);
		Assert::assertEquals(B.fields.get(0).fieldcontent, view.fieldcontent.fieldcontent)
	}
	
}