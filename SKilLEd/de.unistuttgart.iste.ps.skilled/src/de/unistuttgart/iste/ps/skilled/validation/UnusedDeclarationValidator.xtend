package de.unistuttgart.iste.ps.skilled.validation

import com.google.inject.Inject
import de.unistuttgart.iste.ps.skilled.skill.Declaration
import de.unistuttgart.iste.ps.skilled.skill.Field
import de.unistuttgart.iste.ps.skilled.skill.SkillPackage
import de.unistuttgart.iste.ps.skilled.tools.ToolInfo
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import org.eclipse.xtext.validation.Check

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
		val project = services.getProject(declaration.eResource)
		if (project == null) {
			return
		}
		val tools = ToolInfo.getTools(project)
		// do not warn if there are no tools at all
		if (tools.isEmpty) {
			return
		}

		if (services.isToolFile(declaration.eResource.URI)) {
			return
		}

		val name = declaration.name.toLowerCase

		for (tool : tools) {
			for (type : tool.selectedUserTypes) {
				if (type.name.equals(name)) {
					return
				}
			}
		}

		// we could not find the name but there are tools
		val node = NodeModelUtils.getNode(declaration);
		val offset = node.totalOffset;
		val length = node.totalLength;
		warning("Type is not used in Tool", declaration, SkillPackage.Literals.DECLARATION__NAME, UNUSED_TYPE,
			#[offset.toString, length.toString]);

	}

	/**
	 * Tests if a field is being used in a tool and gives a warning if not.
	 * @param field: The declared field that should be tested if it is used in a tool.
	 */
	@Check
	def checkField(Field field) {
		val project = services.getProject(field.eResource)
		if (project == null) {
			return
		}
		val tools = ToolInfo.getTools(project)
		// do not warn if there are no tools at all
		if (tools.isEmpty) {
			return
		}

		if (services.isToolFile(field.eResource.URI)) {
			return
		}

		val name = field.fieldcontent.name.toLowerCase

		for (tool : tools) {
			for (fields : tool.selectedFields.values) {
				if (fields.keySet.contains(name)) {
					return
				}
			}
		}

		val node = NodeModelUtils.getNode(field);
		val offset = node.totalOffset;
		val length = node.totalLength;
		warning("Field is not used in Tool", field.fieldcontent, SkillPackage.Literals.FIELDCONTENT__NAME, UNUSED_FIELD,
			#[offset.toString, length.toString])
	}

}
