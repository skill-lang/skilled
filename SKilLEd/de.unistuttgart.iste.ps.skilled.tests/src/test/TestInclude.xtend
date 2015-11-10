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
import de.unistuttgart.iste.ps.skilled.sKilL.impl.UsertypeImpl
import de.unistuttgart.iste.ps.skilled.sKilL.impl.DeclarationReferenceImpl
import org.junit.BeforeClass

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestInclude {

	@Inject extension ParseHelper<File> parser;
	
	var static String testInclude1 = "";
	var static String testInclude2 = "";
	
	@BeforeClass
	def static void setup() {
		testInclude1 = FileLoader.loadFile("TestInclude1C");
		testInclude2 = FileLoader.loadFile("TestInclude2A");
	}

  	@Test
  	def void testInclude1() {	
  			val specification = testInclude1.parse
  		
  			Assert::assertEquals("testInclude1A.skill", specification.includes.get(0).includeFiles.get(0).importURI);
  			val field1 = (specification.declarations.get(0) as UsertypeImpl).fields.get(0);
  			Assert::assertEquals("/*first imported type*/", (field1.fieldcontent.fieldtype as DeclarationReferenceImpl).type.comment);
  			val field2 = (specification.declarations.get(0) as UsertypeImpl).fields.get(1);
  			Assert::assertEquals("/*second imported type*/", (field2.fieldcontent.fieldtype as DeclarationReferenceImpl).type.comment);	
  	}
  	
  	@Test
  	def void testInclude2() {
  			val specification = testInclude2.parse

  			val field = (specification.declarations.get(0) as UsertypeImpl).fields.get(0);
  			Assert::assertEquals("/*imported type*/", (field.fieldcontent.fieldtype as DeclarationReferenceImpl).type.comment);
  	}
}