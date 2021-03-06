package de.unistuttgart.iste.ps.skilled.validation

import de.unistuttgart.iste.ps.skilled.skill.IncludeFile
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.validation.Check

/**
 * This class contains validation rules for checking the includes in a skill files.
 * 
 * @author Marco Link
 */
class ImportURIValidator extends AbstractSKilLComposedValidatorPart {

  public static val TOOLSFOLDER = '.skillt';

  public static val INVALID_IMPORT_URI_PLATFORM = 'invalidImportURIPlatform';
  public static val INVALID_IMPORT_URI_OUTSIDE_SCOPE = 'invalidImportURIOutsideScope';

  /**
   * Checks whether the included URI is in the eclipse platform format (SKilL geneartor can't handle this) 
   * and raises an error if it is.
   */
  @Check
  def checkPlatformURI(IncludeFile includeFile) {
    var URI includeURI = URI.createURI(includeFile.importURI)
    if (includeURI.isPlatform()) {
      error("Platform uris aren't allowed", includeFile, SkillPackage.Literals.INCLUDE_FILE__IMPORT_URI,
        INVALID_IMPORT_URI_PLATFORM)
    }
  }

  /**
   * Checks whether the included file is a valid target. 
   * This means that if the including file is from the main specification, it cannot include files from tools. 
   * And if it is a tool file, it can only include files inside its tool folder.
   */
  @Check
  def checkIncludeOutsideScoping(IncludeFile includeFile) {
    var file = includeFile.eContainer;
    while (file.eContainer != null) {
      file = file.eContainer;
    }
    var URI fileURI = file?.eResource?.URI;
    if (fileURI != null) {
      var URI includeURI = URI.createURI(includeFile.importURI).resolve(fileURI)
      var fileURISegments = fileURI.segments
      var includeURISegements = includeURI.segments

      var boolean isFileInTools = (fileURISegments.size() > 2) && fileURISegments.get(2).equals(TOOLSFOLDER)
      var boolean isIncludeFileInTools = (includeURISegements.size() > 2) &&
        (includeURISegements.get(2).equals(TOOLSFOLDER))

      if (!isFileInTools && !isIncludeFileInTools) {
        // Everything is fine.
      } else if ((!isFileInTools && isIncludeFileInTools) || (isFileInTools && !isIncludeFileInTools)) {
        error("It is forbidden to reference from main specification to tools specification and vice versa.",
          includeFile, SkillPackage.Literals.INCLUDE_FILE__IMPORT_URI);
      } else if (!fileURISegments?.get(3).equals(includeURISegements?.get(3))) {
        error("It is forbidden to reference from one tool to another.", includeFile,
          SkillPackage.Literals.INCLUDE_FILE__IMPORT_URI);
      }

    }
  }
}