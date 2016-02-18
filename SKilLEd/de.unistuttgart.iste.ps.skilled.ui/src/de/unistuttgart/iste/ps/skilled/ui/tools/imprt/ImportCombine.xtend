package de.unistuttgart.iste.ps.skilled.ui.tools.imprt

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Enuminstance
import de.unistuttgart.iste.ps.skilled.sKilL.Enumtype
import de.unistuttgart.iste.ps.skilled.sKilL.Field
import de.unistuttgart.iste.ps.skilled.sKilL.Hint
import de.unistuttgart.iste.ps.skilled.sKilL.HintArgument
import de.unistuttgart.iste.ps.skilled.sKilL.Interfacetype
import de.unistuttgart.iste.ps.skilled.sKilL.TypeDeclaration
import de.unistuttgart.iste.ps.skilled.sKilL.Typedef
import de.unistuttgart.iste.ps.skilled.sKilL.Usertype
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import java.awt.EventQueue
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList
import javax.swing.JOptionPane
import org.apache.commons.io.FileUtils
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.EcoreUtil2
import de.unistuttgart.iste.ps.skilled.ui.tools.ToolUtil

/**
 * This class provides combine function for Import Tools. If there are 2 or more 
 * duplicate types when comparing the to-be-imported file with the files in the 
 * project folder then an error is given. If there is only one duplicate type, 
 * the fields from the duplicate type in the imported file will be transfered to
 * its identical type in one of the files in the project folder. 
 * 
 * @author Leslie
 * 
 */
class ImportCombine {
	var String fProjectName = ""
	var de.unistuttgart.iste.ps.skilled.sKilL.File fSelectedToolPath = null;
	var ArrayList<String> duplicateType = new ArrayList
	var ArrayList<String> duplicateTypeAddress = new ArrayList
	var int counter = 0;
	var fSKilLServices = new SKilLServices()
	var String fDuplicateTypeName = ""
	var int fDuplicateIndex = 0;
	var String fDuplicateFields = "";
	var String fProjectFilePath = "";
	var String fFieldTypes;
	var String fFieldNames;
	var String fDeclarationType;
	var String fEnumName;
	var String fEnumList = "";
	var String fComment = "";
	var String enums = "";
	var String fields = "";
	var String eline = "";

	var Boolean fPreviousLineWasUsertype = false;
	var Boolean fPreviousLineWasInterfacetype = false;
	var Boolean fPreviousLineWasEnumtype = false;

	var String fRestriction;
	var String fRestrictions = "";
	var String fIsBraceOpen = "";
	var String fIsBraceClosed = "";

	var String fResArgB = "";
	var String fResArgD = "";
	var String fResArgL = "";
	var String fResArgS = "";
	var String fResArgT = "";
	var String fResArgList = "";

	var String fHint

	var String fIsBraceOpenHints = "";

	var String fIsBraceClosedHints = "";

	var String fHints = "";
	var String fHArgD = "";
	var String fHArgL = "";
	var String fHArgS = "";
	var String fHArgT = "";
	var String fHArgList = "";

	def void start() {
		// Gets project name from ImportTools
		if (ImportTools.getProjectName() != null) {
			fProjectName = ImportTools.getProjectName()
		}

		// Checks if the selected tool file is empty
		var String fCheckSelectedTool = new String(Files.readAllBytes(Paths.get(ImportTools.getSelectedToolPath())));
		fCheckSelectedTool = fCheckSelectedTool.replaceAll("\\s+", "");

		if (ImportTools.getSelectedToolPath() != null || fCheckSelectedTool != null || fCheckSelectedTool != "") {
			// Gets selected tool from ImportTools and loads file as a skill file
			fSelectedToolPath = fSKilLServices.getFile(ImportTools.getSelectedToolPath());
		} else {
			ShowMessage("File to import is empty!", "Empty Import File");
			return;
		}

		var project = ResourcesPlugin.getWorkspace.getRoot.getProject(fProjectName);
		var files = fSKilLServices.getAll(project, true);
		// Check types in all files in the project folder
		for (de.unistuttgart.iste.ps.skilled.sKilL.File f : files) {
			for (Declaration d : f.declarations) {
				if (d instanceof Usertype) {
					fDeclarationType = "User"
				} else if (d instanceof Typedef) {
					fDeclarationType = "Typedef";

				} else if (d instanceof Enumtype) {
					fDeclarationType = "Enum";

				} else if (d instanceof Interfacetype) {
					fDeclarationType = "Interface";
				}
				// add type to list of types if it is not in the list of types
				if (!duplicateType.contains(fDeclarationType + " " + d.name)) {
					duplicateType.add(fDeclarationType + " " + d.name);
					var uri = EcoreUtil2.getNormalizedResourceURI(f);
					uri = URI.createFileURI(uri.path);
					if (uri.toFileString.startsWith(File.separator)) {
						var String fRemoveFirstSeparator = uri.toFileString.substring(1);
						fProjectFilePath = fRemoveFirstSeparator.substring(
							fRemoveFirstSeparator.indexOf(File.separator));
					} else {
						fProjectFilePath = uri.toFileString.substring(uri.toFileString.indexOf(File.separator));
					}
					// Create absolute path of the file
					duplicateTypeAddress.add(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() +
						fProjectFilePath);
				} else {
					JOptionPane.showMessageDialog(null, "Duplicate types in project!", "Duplicate Types",
						JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}

		// Check types in the selected tool file
		for (Declaration d2 : fSelectedToolPath.declarations) {
			if (d2 instanceof Usertype) {
				fDeclarationType = "User"
			} else if (d2 instanceof Typedef) {
				fDeclarationType = "Typedef";

			} else if (d2 instanceof Enumtype) {
				fDeclarationType = "Enum";

			} else if (d2 instanceof Interfacetype) {
				fDeclarationType = "Interface";
			}

			if (!duplicateType.contains(fDeclarationType + " " + d2.name)) {
				duplicateType.add(fDeclarationType + " " + d2.name);
			} else {
				if (counter == 0) {
					counter += 1;
					fDuplicateTypeName = d2.name;
					fDuplicateIndex = duplicateType.indexOf(fDeclarationType + " " + d2.name);

					if (d2.comment != null) {
						fComment = d2.comment + "\n";
					}
					// Get field type and name information from the duplicate type
					if (d2 instanceof TypeDeclaration) {
						for (Field field : d2.fields) {
							// Get field type
							fFieldTypes = field.fieldcontent.fieldtype.toString.substring(
								field.fieldcontent.fieldtype.toString.indexOf("type: ") + 6,
								field.fieldcontent.fieldtype.toString.length - 1);
							// Get field name
							fFieldNames = field.fieldcontent.name.toString.substring(
								field.fieldcontent.name.toString.indexOf("name: ") + 1);

							// Forms complete field content (fieldtype fieldname;)
							fDuplicateFields += fFieldTypes + " " + fFieldNames + ";" + "\n";
						}
						if (d2 instanceof Usertype) {
							for (restriction : d2.restrictions) {
								fRestriction = restriction.restrictionName
								if (restriction.isIsBraceOpen) {
									fIsBraceOpen = "(";
								}
								if (restriction.isIsBraceClose) {
									fIsBraceClosed = ")"
								}
								for (ra : restriction.restrictionArguments) {
									if (ra.valueBoolean.value == 0) {
										fResArgB = false.toString;
									} else {
										fResArgB = true.toString;
									}
									fResArgD = ra.valueDouble.toString + ", ";
									fResArgL = ra.valueLong.toString + ", ";
									fResArgS = ra.valueString + ", ";
									fResArgT = ra.valueType.type.toString + ", ";
									fResArgList += fResArgB + fResArgD + fResArgL + fResArgS + fResArgT;
									fResArgB = "";
									fResArgD = "";
									fResArgL = "";
									fResArgS = "";
									fResArgT = "";
								}
								if (fResArgList.length > 2) {
									fResArgList = fResArgList.substring(0, fResArgList.length - 2);
								}
								fRestrictions = fRestriction + fIsBraceOpen + fResArgList + fIsBraceClosed;
								if (fRestrictions.length > 0) {
									fRestrictions = "@" + fRestrictions + "\n";
								}
							}
							for (Hint hint : d2.hints) {
								fHint = hint.hintName;
								if (hint.isIsBraceOpen) {
									fIsBraceOpenHints = "(";
								}
								if (hint.isIsBraceClose) {
									fIsBraceClosedHints = ")";
								}
								for (HintArgument ha : hint.hintArguments) {
									fHArgD = ha.valueDouble.toString + ", ";
									fHArgL = ha.valueLong.toString + ", ";
									fHArgS = ha.valueString + ", ";
									fHArgT = ha.valueType.type.toString + ", ";
									fHArgList += fHArgD + fHArgL + fHArgS + fHArgT;
									fHArgD = "";
									fHArgL = "";
									fHArgS = "";
									fHArgT = "";
								}
								if (fHArgList.length > 2) {
									fHArgList = fHArgList.substring(0, fHArgList.length - 2);
								}
								fHints = fHint + fIsBraceOpenHints + fHArgList + fIsBraceClosedHints;
								if (fHints.length > 0) {
									fHints = "!" + fHints + "\n";
								}

							}

						}
					} else if (d2 instanceof Enumtype) {
						for (Enuminstance enum : d2.instances) {
							fEnumName = enum.name.toString.substring(enum.name.toString.indexOf("name: ") + 1);
							fEnumList += fEnumName + ",";
						}
						for (Field field : d2.fields) {
							// Get field type
							fFieldTypes = field.fieldcontent.fieldtype.toString.substring(
								field.fieldcontent.fieldtype.toString.indexOf("type: ") + 6,
								field.fieldcontent.fieldtype.toString.length - 1);
							// Get field name
							fFieldNames = field.fieldcontent.name.toString.substring(
								field.fieldcontent.name.toString.indexOf("name: ") + 1);

							// Forms complete field content (fieldtype fieldname;)
							fDuplicateFields += fFieldTypes + " " + fFieldNames + ";" + "\n";
						}

					}
				} else {
					ShowMessage("More than 1 duplicate type between imported file and project files!",
						"Too Many Duplicate Types");
					return;
				}
			}
		}

		var File fSource = new File(ImportTools.getSelectedToolPath());
		var File fDestination = new File(
			ImportTools.getSaveDestination() + File.separator + ImportTools.getFileName() + ".skill");
		// A alternate name in case name of the file already exists in the project folder
		var File fDestinationRenamed = new File(
			ImportTools.getSaveDestination + File.separator + ImportTools.getFileName() + "_ImportedBySKilLEd.skill");

		var String fProjectPath = ImportTools.getSaveDestination().substring(0,
			ImportTools.getSaveDestination().lastIndexOf(File.separator) + 1);
		var File fWriteToTemp = new File(fProjectPath + "TempFileBySKilLEd.skill")

		if (counter == 0) {
			System.out.println("No duplicates found, moving file");
			// Move to project folder
			if (!fDestination.exists) {
				FileUtils.moveFile(fSource, fDestination);
			} else {
				FileUtils.moveFile(fSource, fDestinationRenamed);
			}
		} else {
			System.out.println("Only 1 duplicate found, starting merge");
			// If type name already exists, merge
			var fFindDuplicatedType = duplicateType.get(fDuplicateIndex);
			var fFindDuplicatedPath = duplicateTypeAddress.get(fDuplicateIndex);

			if (fDeclarationType.equals("User")) {
				fFindDuplicatedType = fFindDuplicatedType.substring(5);
			}

			var File fOpenFileToMerge = new File(fFindDuplicatedPath);

			// Restructure file to remove any empty lines
			var String fRestructureFile = new String(Files.readAllBytes(Paths.get(fFindDuplicatedPath)));
			fRestructureFile = fRestructureFile.replaceAll("\n+", "\n");

			fOpenFileToMerge.delete;
			var FileWriter fw = new FileWriter(fOpenFileToMerge);
			if (fRestructureFile != null) {
				// Remake file with the deleted empty lines
				fw.write(fRestructureFile);
				fw.close;

			}

			var BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fOpenFileToMerge)));
			var FileWriter fw1 = new FileWriter(fWriteToTemp);
			var String line;
			var String fText = "";
			while ((line = br.readLine()) != null) {
				var Boolean isUsertype = fDeclarationType.equals("User") &&
					line.toLowerCase.startsWith(fFindDuplicatedType.toLowerCase) && line.contains(";");
				var Boolean isInterfacetype = fDeclarationType.equals("Interface") &&
					line.toLowerCase.startsWith(fFindDuplicatedType.toLowerCase) && line.contains(";");
				var Boolean isEnumtype = fDeclarationType.equals("Enum") &&
					line.toLowerCase.startsWith(fFindDuplicatedType.toLowerCase) && line.contains(";");

				if (line.contains("}")) {
					eline = line.substring(0, line.length - 1);
				} else {
					eline = line;
				}

				var String fRemoveColon = line.replaceAll(";", "");
				var int fCheckForFields = line.length - fRemoveColon.length;
				if (fDeclarationType.equals("Enum") && line.contains(";")) {
					enums = eline.substring(0, eline.indexOf(";"));
				} else {
					enums = "";
				}
				if (fCheckForFields > 1) {
					fields = eline.substring(eline.lastIndexOf(";") + 1);
				} else {
					fields = "";
				}
				var String fWriteEnums = enums + ", " + fEnumList.substring(0, fEnumList.length - 1) + ";" + "\n";
				var String fWriteFields = fields + "\n" + fDuplicateFields;

				if (!fPreviousLineWasUsertype && fDeclarationType.equals("User") &&
					line.toLowerCase.startsWith(fFindDuplicatedType.toLowerCase) && !line.contains(";")) {
					fText += fComment + fRestrictions + fHints + line + "\n";
					fPreviousLineWasUsertype = true;
				} else if (fPreviousLineWasUsertype && !line.contains("}")) {
					fText += line + "\n" + "	" + fDuplicateFields;
					fPreviousLineWasUsertype = false;
				} else if (isUsertype && !line.contains("}")) {
					fText += fComment + fRestrictions + fHints + line + "\n" + "	" + fDuplicateFields;
					fPreviousLineWasUsertype = false;
				} else if (fPreviousLineWasUsertype && line.contains("}")) {
					line = line.substring(0, line.length - 1);
					fText += line + "\n" + "	" + fDuplicateFields + "}" + "\n";
					fPreviousLineWasUsertype = false;
				} else if (isUsertype && line.contains("}")) {
					line = line.substring(0, line.length - 1);
					fText += fComment + fRestrictions + fHints + line + "\n" + "	" + fDuplicateFields + "}" + "\n";
					fPreviousLineWasUsertype = false;
				} else if (!fPreviousLineWasInterfacetype && fDeclarationType.equals("Interface") &&
					line.toLowerCase.startsWith(fFindDuplicatedType.toLowerCase) && !line.contains(";")) {
					fText += line + "\n";
					fPreviousLineWasInterfacetype = true;
				} else if (fPreviousLineWasInterfacetype && !line.contains("}")) {
					fText += line + "\n" + "	" + fDuplicateFields;
					fPreviousLineWasInterfacetype = false;
				} else if (isInterfacetype && !line.contains("}")) {
					fText += fComment + line + "\n" + "	" + fDuplicateFields;
					fPreviousLineWasInterfacetype = false;
				} else if (fPreviousLineWasInterfacetype && line.contains("}")) {
					line = line.substring(0, line.length - 1);
					fText += line + "\n" + "	" + fDuplicateFields + "}" + "\n";
					fPreviousLineWasInterfacetype = false;
				} else if (isInterfacetype && line.contains("}")) {
					line = line.substring(0, line.length - 1);
					fText += fComment + line + "\n" + "	" + fDuplicateFields + "}" + "\n";
					fPreviousLineWasInterfacetype = false;
				} 
				// i.e. enum A {
				else if (!fPreviousLineWasEnumtype && fDeclarationType.equals("Enum") &&
					line.toLowerCase.startsWith(fFindDuplicatedType.toLowerCase) && !line.contains(";")) {
					fText += line + "\n";
					fPreviousLineWasEnumtype = true;
				} // i.e. {e1, e2;}
				else if (fPreviousLineWasEnumtype && line.contains("}")) {
					fText += fWriteEnums + fWriteFields + "}";
					fPreviousLineWasEnumtype = false;
				} // i.e. enum A { e1, e2; }
				else if (isEnumtype && line.contains("}")) {
					fText += fComment + fRestrictions + fHints + fWriteEnums + fWriteFields + "}";
					fPreviousLineWasEnumtype = false;
				} // i.e. { e1, e2; or enum A { e1, e2;
				else if ((fPreviousLineWasEnumtype || isEnumtype) && !line.contains("}")) {
					fText += fWriteEnums + fWriteFields;
					fPreviousLineWasEnumtype = false;
				} else {
					fText += line + "\n";
				}
			}

			fw1.write(fText);
			fw1.close;
			br.close;

			// Deletes original file with the merged types
			fOpenFileToMerge.delete;
			// Renames Temp file to the name of the file that has merged types
			fWriteToTemp.renameTo(fOpenFileToMerge);

			// Delete type from original file
			var String fOriginalFileText = new String(Files.readAllBytes(Paths.get(ImportTools.getSelectedToolPath())));
			fOriginalFileText = fOriginalFileText.replaceAll("\n+", " #nextline#");
			fOriginalFileText = fOriginalFileText.toLowerCase.replaceAll(fFindDuplicatedType.toLowerCase + "( (.| )*?)? ?\\{.*?\\}",
				"");
			fOriginalFileText = fOriginalFileText.replaceAll("#nextline#", "\n");
			// Remove empty lines again to remove empty lines made by the first replace
			fOriginalFileText = fOriginalFileText.replaceAll("\n+", "#nextline#");
			fOriginalFileText = fOriginalFileText.replaceAll("#nextline#", "\n");

			// Delete original to be imported file
			fSource.delete;
			var FileWriter fw2 = new FileWriter(fSource);
			if (fOriginalFileText != null) {
				// Remake to be imported file with the deleted duplicate types 
				fw2.write(fOriginalFileText);
				fw2.close;

				// Move file to the import location
				if (!fDestination.exists) {
					FileUtils.moveFile(fSource, fDestination);

				} else {
					FileUtils.moveFile(fSource, fDestinationRenamed);
				}

			}

		}
		// Create tool
		ToolUtil.createTool(ImportTools.getFileName(), project);
	}

	def ShowMessage(
		String string,
		String string2
	) {
		EventQueue.invokeLater(new Runnable() {
			override run() {
				JOptionPane.showMessageDialog(null, string, string2, JOptionPane.ERROR_MESSAGE);
			}
		});
	}

}