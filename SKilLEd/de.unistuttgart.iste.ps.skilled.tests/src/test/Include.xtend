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
import java.io.PrintWriter

/**
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class Include {

	@Inject extension ParseHelper<File> parser;

  	@Test
  	def void testInclude1() {
  		try {
  			val PrintWriter writer1 = new PrintWriter("testInclude1.skill", "UTF-8");
  			writer1.println("include \"testInclude2.skill\"");
  			writer1.println("/*first imported type*/");
  			writer1.println("Type1 {");
  			writer1.println("i32 field;");
  			writer1.println("}");
  			writer1.close();
  			
  			val PrintWriter writer2 = new PrintWriter("testInclude2.skill", "UTF-8");
  			writer2.println("/*second imported type*/");
  			writer2.println("Type2 {");
  			writer2.println("string field;");
  			writer2.println("}");
  			writer2.close();
  			
  			val specification = '''
				include "testInclude1.skill"

				Type {
					Type1 field1;
					Type2 field2;
				}
  			'''.parse
  		
  			Assert::assertEquals("testInclude1.skill", specification.includes.get(0).includeFiles.get(0).importURI);
  			val field1 = (specification.declarations.get(0) as UsertypeImpl).fields.get(0);
  			Assert::assertEquals("/*first imported type*/", (field1.fieldcontent.fieldtype as DeclarationReferenceImpl).type.comment);
  			val field2 = (specification.declarations.get(0) as UsertypeImpl).fields.get(1);
  			Assert::assertEquals("/*second imported type*/", (field2.fieldcontent.fieldtype as DeclarationReferenceImpl).type.comment);
  		
  		} finally {
  			new java.io.File("testInclude1.skill").delete();
  			new java.io.File("testInclude2.skill").delete();
  		}
  			
  	}
  	
  	@Test
  	def void testInclude2() {
  		try {
  			//__synthetic0.skill is the name of the (virtual) file created when parsing- ...I think
  			//(if this test case fails even though it's supposed to work I'll rewrite it)
  			val PrintWriter writer = new PrintWriter("__synthetic0.skill", "UTF-8");
  			writer.println("Type {");
  			writer.println("Type1 field1;");
  			writer.println("}");
  			writer.close();
  			  			  			  			
  			val PrintWriter writer1 = new PrintWriter("main.skill", "UTF-8");
  			writer1.println("include \"testInclude.skill\"");
  			writer1.println("include \"__synthetic0.skill\"");
  			writer1.close();
  			
  			val PrintWriter writer2 = new PrintWriter("testInclude.skill", "UTF-8");
  			writer2.println("/imported type*/");
  			writer2.println("Type1 {");
  			writer2.println("string field;");
  			writer2.println("}");
  			writer2.close();
  			
  			val specification = '''
				Type {
				Type1 field1;
				}
  			'''.parse

			Assert::assertEquals("__synthetic0.skill", specification.eResource.URI.path);
  			val field = (specification.declarations.get(0) as UsertypeImpl).fields.get(0);
  			Assert::assertEquals("/*imported type*/", (field.fieldcontent.fieldtype as DeclarationReferenceImpl).type.comment);
  		
  		} finally {
  			new java.io.File("testInclude.skill").delete();
  			new java.io.File("main.skill").delete();
  			new java.io.File("__synthetic0.skill").delete();
  		}
  			
  	}
}