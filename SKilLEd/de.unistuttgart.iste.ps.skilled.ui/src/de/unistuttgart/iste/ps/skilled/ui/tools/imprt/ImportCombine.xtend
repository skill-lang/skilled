package de.unistuttgart.iste.ps.skilled.ui.tools.imprt

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.Field
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

	def void start() {
		//Gets project name from ImportTools
		if (ImportTools.getProjectName() != null) {
			fProjectName = ImportTools.getProjectName()
		}
		
		//Checks if the selected tool file is empty
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
				// add type to list of types if it is not in the list of types
				if (!duplicateType.contains(d.name)) {
					duplicateType.add(d.name);
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
			System.out.println(EcoreUtil2.getURI(fSelectedToolPath));
			System.out.println(d2.name);
			if (!duplicateType.contains(d2.name)) {
				duplicateType.add(d2.name);
			} else {
				if (counter == 0) {
					counter += 1;
					fDuplicateTypeName = d2.name;
					fDuplicateIndex = duplicateType.indexOf(d2.name);
					
					//Get field type and name information from the duplicate type
					if (d2 instanceof Usertype) {
						for (Field field : d2.fields) {
							//Get field type
							fFieldTypes = field.fieldcontent.fieldtype.toString.substring(
								field.fieldcontent.fieldtype.toString.indexOf("type: ") + 6,
								field.fieldcontent.fieldtype.toString.length - 1);
							//Get field name
							fFieldNames = field.fieldcontent.name.toString.substring(
								field.fieldcontent.name.toString.indexOf("name: ") + 1);
							
							//Forms complete field content (fieldtype fieldname;)
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
			System.out.println("fDuplicateIndex: " + fDuplicateIndex);

			var fFindDuplicatedType = duplicateType.get(fDuplicateIndex);
			var fFindDuplicatedPath = duplicateTypeAddress.get(fDuplicateIndex);

			var File fOpenFileToMerge = new File(fFindDuplicatedPath);

			var BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fOpenFileToMerge)));
			var FileWriter fw = new FileWriter(fWriteToTemp);
			var String line;
			var String fText = "";
			while ((line = br.readLine()) != null) {
				// Write fields from duplicate type to its identical type in the project folder
				if (line.startsWith(fFindDuplicatedType) && !line.endsWith(";")) {
					if (!line.endsWith("}")) {
						fText += line + "\n" + "	" + fDuplicateFields + "\n";
					} else {
						line.substring(0, line.length - 1);
						fText += line + "\n" + "	" + fDuplicateFields + "\n" + "}";
					}
				} else {
					fText += line + "\n";
				}
			}
			fw.write(fText);
			fw.close;
			br.close;

			// Deletes original file with the merged types
			fOpenFileToMerge.delete;
			// Renames Temp file to the name of the file that has merged types
			fWriteToTemp.renameTo(fOpenFileToMerge);

			// Delete type from original file
			var String fOriginalFileText = new String(Files.readAllBytes(Paths.get(ImportTools.getSelectedToolPath())));
			fOriginalFileText = fOriginalFileText.replaceAll("\n+", "#nextline#");
			fOriginalFileText = fOriginalFileText.replaceAll(fFindDuplicatedType + "( (.| )*?)? ?\\{.*?\\}", "");
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

	}

	def ShowMessage(String string, String string2) {
		EventQueue.invokeLater(new Runnable() {
			override run() {
				JOptionPane.showMessageDialog(null, string, string2, JOptionPane.ERROR_MESSAGE);
			}
		});
	}

}