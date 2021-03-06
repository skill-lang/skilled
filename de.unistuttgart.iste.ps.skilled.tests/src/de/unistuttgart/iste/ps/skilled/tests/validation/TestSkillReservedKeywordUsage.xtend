package de.unistuttgart.iste.ps.skilled.tests.validation

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.SkillInjectorProvider
import de.unistuttgart.iste.ps.skilled.skill.File
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.tests.utils.FileLoader
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Armin Hüneburg
 */
@InjectWith(SkillInjectorProvider)
@RunWith(XtextRunner)
class TestSkillReservedKeywordUsage {

	@Inject extension ParseHelper<File> parser;
	@Inject extension ValidationTestHelper;
	
	val static String noErrorFile = FileLoader.loadFile("reservedKeyword/noError");
	val static String fieldApi = FileLoader.loadFile("reservedKeyword/field/api");
	val static String fieldInternal = FileLoader.loadFile("reservedKeyword/field/internal");
	val static String fieldSkillid = FileLoader.loadFile("reservedKeyword/field/skillid");
	val static String typeApi = FileLoader.loadFile("reservedKeyword/type/api");
	val static String typeInternal = FileLoader.loadFile("reservedKeyword/type/internal");
	val static String typeSkillid = FileLoader.loadFile("reservedKeyword/type/skillid");
	
	@Test
	def void testNoError() {
		noErrorFile.parse.assertNoIssues;
	}
	
	@Test
	def void testFieldApi() {
		fieldApi.parse.assertWarning(SkillPackage::eINSTANCE.fieldcontent, null, "Usage of skillid, api or internal is discouraged");
	}
	
	@Test
	def void testFieldInternal() {
		fieldInternal.parse.assertWarning(SkillPackage::eINSTANCE.fieldcontent, null, "Usage of skillid, api or internal is discouraged");
	}
	
	@Test
	def void testFieldSkillid() {
		fieldSkillid.parse.assertWarning(SkillPackage::eINSTANCE.fieldcontent, null, "Usage of skillid, api or internal is discouraged");
	}
	
	@Test
	def void testTypeApi() {
		typeApi.parse.assertWarning(SkillPackage::eINSTANCE.declaration, null, "Usage of skillid, api or internal is discouraged");
	}
	
	@Test
	def void testTypeInternal() {
		typeInternal.parse.assertWarning(SkillPackage::eINSTANCE.declaration, null, "Usage of skillid, api or internal is discouraged");
	}
	
	@Test
	def void testTypeSkillid() {
		typeSkillid.parse.assertWarning(SkillPackage::eINSTANCE.declaration, null, "Usage of skillid, api or internal is discouraged");
	}
	
}