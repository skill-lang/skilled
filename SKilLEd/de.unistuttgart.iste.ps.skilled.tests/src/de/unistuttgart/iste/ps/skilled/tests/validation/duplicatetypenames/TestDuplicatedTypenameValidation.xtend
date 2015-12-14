package de.unistuttgart.iste.ps.skilled.tests.validation.duplicatetypenames;

import static org.junit.Assert.*;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;

import de.unistuttgart.iste.ps.skilled.SKilLInjectorProvider;
import de.unistuttgart.iste.ps.skilled.sKilL.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.BeforeClass
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader

/**
 * This Class tests the DuplicatedTypenameValidation class.
 * @author Jan Berberich
 * @author Tobias Heck
 */
@InjectWith(SKilLInjectorProvider)
@RunWith(XtextRunner)
class TestDuplicatedTypenameValidation {
	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper
	
	var static String test1 = "";
	var static String test2 = "";
	var static String test3 = "";
	var static String test4 = "";
	
	@BeforeClass
	def static void setup() {
		test1 = FileLoader.loadFile("validation/duplicatetypenames/DuplicateNames1");
		test2 = FileLoader.loadFile("validation/duplicatetypenames/DuplicateNames2");
		test3 = FileLoader.loadFile("validation/duplicatetypenames/DuplicateNames3");
		test4 = FileLoader.loadFile("validation/duplicatetypenames/DuplicateNames4");
	}

	@Test
	def void testNoErrorValid() {
		assertTrue('''
			TypeA {
				const i8 a = 1;
			}    	
			TypeB :TypeA{
				const i8 a2 = 2;
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testNoErrorView() {
		assertTrue('''
			A{
				A a;
			}
			B : A {
				view A.a as
				B a;
			}
		'''.parse.validate.isNullOrEmpty)
	}

	@Test
	def void testErrorDuplicatedFieldname() {
		assertTrue('''
			TypeA {
				const i8 a = 1;
			}    	
			TypeB :TypeA{
				const i8 a = 2;
			}
		'''.parse.validate.size == 1)
	}

	@Test
	def void testErrorDuplicatedFieldname2() {
		assertTrue('''
			TypeA {
				i16 a;
			}    	
			TypeB :TypeA{
				const i8 b = 2;
			}
			TypeC:TypeB{
				i8 a;
			}
		'''.parse.validate.size == 1)
	}

}