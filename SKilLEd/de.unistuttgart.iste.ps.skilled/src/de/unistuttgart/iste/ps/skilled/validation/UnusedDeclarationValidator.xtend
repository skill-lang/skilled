package de.unistuttgart.iste.ps.skilled.validation

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.SKilLPackage
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import de.unistuttgart.iste.ps.skillls.tools.Tool
import de.unistuttgart.iste.ps.skillls.tools.Type
import de.unistuttgart.iste.ps.skillls.tools.api.SkillFile
import java.io.File
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.validation.Check

import static de.unistuttgart.iste.ps.skilled.validation.SkillFileOpener.*

class UnusedDeclarationValidator extends AbstractSKilLComposedValidatorPart {

  @Inject SKilLServices services;

  public static val UNUSED_TYPE = "unusedType";
  public static val UNUSED_FIELD = "unusedField";

  /**
   * Tests if a type is being used in a tool and gives a warning if not.
   * @param declaration: The type declaration that should be tested if it is used in a tool.
   */
  @Check
  def checkType(Declaration declaration) {
    if (services.getProject(declaration.eResource) == null) {
      return
    }
    if (services.isToolFile(declaration.eResource.URI)) {
      return
    }
    val platformString = declaration.eResource.URI.toPlatformString(true);
    val myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString))
    val project = myFile.project
    val file = project.getFile(File.separator + ".skills")
    var SkillFile skillFile = null
    try {
      var a = file.rawLocation.toString();
      SkillFileOpener.setPath(a);
    } catch (Throwable e) {
      e.printStackTrace
    }
    skillFile = SkillFileOpener.getFile()
    var name = declaration.name
    var found = false
    for (Tool tool : skillFile.Tools) {
      for (Type type : tool.types) {
        if (type.name.startsWith(name)) {
          found = true
        }
      }
    }
    if (!found) {
      var node = NodeModelUtils.getNode(declaration);
      var offset = node.totalOffset;
      var length = node.totalLength;
      warning("Type is not used in Tool", declaration, SKilLPackage.Literals.DECLARATION__NAME, UNUSED_TYPE,
        #[offset.toString, length.toString]);
    }
  }

  /**
   * Tests if a field is being used in a tool and gives a warning if not.
   * @param field: The declared field that should be tested if it is used in a tool.
   */
  @Check
  def checkField(Field field) {
    if (services.getProject(field.eResource) == null) {
      return
    }
    if (services.isToolFile(field.eResource.URI)) {
      return
    }
    val platformString = field.eResource.URI.toPlatformString(true);
    val myFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(platformString))
    val project = myFile.project
    val file = project.getFile(File.separator + ".skills")
    var SkillFile skillFile = null
    try {
      SkillFileOpener.path = file.rawLocation.toString()
    } catch (Throwable e) {
      e.printStackTrace
    }
    skillFile = SkillFileOpener.file
    val content = field.fieldcontent
    val name = content.name
    var found = false
    for (Tool tool : skillFile.Tools) {
      for (Type type : tool.types) {
        for (de.unistuttgart.iste.ps.skillls.tools.Field f : type.fields) {
          if (f.name.toLowerCase.substring(f.name.lastIndexOf(' ') + 1).equals(name)) {
            found = true
          }
        }
      }
    }
    if (!found) {
      var node = NodeModelUtils.getNode(field);
      var offset = node.totalOffset;
      var length = node.totalLength;
      warning("Field is not used in Tool", field.fieldcontent, SKilLPackage.Literals.FIELDCONTENT__NAME, UNUSED_FIELD,
        #[offset.toString, length.toString])
    }
  }

}
