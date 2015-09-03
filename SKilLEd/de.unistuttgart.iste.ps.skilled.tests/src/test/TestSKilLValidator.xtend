package test;

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
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.validation.SKilLValidator

/**
 * TODO Comment everything
 * @author Marco Link
 * @author Armin Hüneburg
 * @author Moritz Platzer
 */
@InjectWith (SKilLInjectorProvider)
@RunWith (XtextRunner)
public class TestSKilLValidator {

    @Inject extension ParseHelper<File> parser;
    @Inject extension ValidationTestHelper
    
    @Test
    def void testIntegerConstantsValid(){
    	'''
        TypeA {
        	const i8 a = 1;
        	const i16 b = 2;
        	const i32 c = 3;
        	const i64 d = 4;
        	const v64 e = 5;
        }    	
    	'''.parse.assertNoError("All's Good")
    }
    
     @Test
    def void testIntegerConstantsInValidWithBuildInType(){
    	'''
        TypeA {
        	const string a = 1;
        }    	
    	'''.parse.assertError(SKilLPackage::eINSTANCE.constant, SKilLValidator::INVALID_CONSTANT_TYPE, "Only an Integer can be const.")
    }
    
         @Test
    def void testIntegerConstantsInValidWithUserType(){
    	'''
        TypeA {
        	const TypeA a = 1;
        }    	
    	'''.parse.assertError(SKilLPackage::eINSTANCE.constant, SKilLValidator::INVALID_CONSTANT_TYPE, "Only an Integer can be const.")
    }
    
	@Test
	def void testNestedListInTypedef() {
		'''
		TypeA {
			
		}
		Typedef abc list<TypeA>;
		Typedef abcd list<abc>;
		'''.parse.assertError(SKilLPackage::eINSTANCE.listtype, SKilLValidator::INVALID_NESTED_TYPEDEF, "It is forbidden to nest containers inside of other containers.")
				
	}
	
	
	@Test
	def void testNestedListAsField() {
		'''
		TypeA {
			list<abc> abcd;
		}
		Typedef abc list<i8>;
		'''.parse.assertError(SKilLPackage::eINSTANCE.listtype, SKilLValidator::INVALID_NESTED_TYPEDEF, "It is forbidden to nest containers inside of other containers.")
			
	}
	
	@Test
	def void testNestedArrayInTypedef() {
		'''
		TypeA {
			
		}
		Typedef abc TypeA[];
		Typedef abcd abc[];
		'''.parse.assertError(SKilLPackage::eINSTANCE.arraytype, SKilLValidator::INVALID_NESTED_TYPEDEF, "It is forbidden to nest containers inside of other containers.")
				
	}
	
	
	@Test
	def void testNestedArrayAsField() {
		'''
		TypeA {
			abc[] abcd;
		}
		Typedef abc i8[];
		'''.parse.assertError(SKilLPackage::eINSTANCE.arraytype, SKilLValidator::INVALID_NESTED_TYPEDEF, "It is forbidden to nest containers inside of other containers.")
			
	}
	
	@Test
	def void testNestedSetAsField() {
		'''
		TypeA {
			set<abc> abcd;
		}
		Typedef abc i8[];
		'''.parse.assertError(SKilLPackage::eINSTANCE.settype, SKilLValidator::INVALID_NESTED_TYPEDEF, "It is forbidden to nest containers inside of other containers.")
			
	}
	
	@Test
	def void testNestedMapAsField() {
		'''
		TypeA {
			map<i8, abc> abcd;
		}
		Typedef abc i8[];
		'''.parse.assertError(SKilLPackage::eINSTANCE.maptype, SKilLValidator::INVALID_NESTED_TYPEDEF, "It is forbidden to nest containers inside of other containers.")
			
	}
	
	@Test
	def void testAllGood() {
		'''
		TypeA {
			list<a> aa;
			b[] bb;
			c[2] cc;
			set<d> dd;
			map<i8, e> ee;
		}
		Typedef a i8;
		Typedef b i16;
		Typedef c i32;
		Typedef d i64;
		Typedef e v64;
		'''.parse.assertNoError("All's good")
		
	}

}
