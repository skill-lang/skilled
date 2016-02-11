package de.unistuttgart.iste.ps.skilled.ui.tools.imprt

import de.unistuttgart.iste.ps.skilled.sKilL.Declaration
import de.unistuttgart.iste.ps.skilled.sKilL.File
import de.unistuttgart.iste.ps.skilled.util.SKilLServices
import java.util.HashSet
import java.util.Set
import javax.swing.JOptionPane
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.xtext.EcoreUtil2

class ImportCombine {
	var String fProjectName = ""
	var String fSelectedToolPath = "";
	var Set<String> duplicateType = new HashSet
	var int counter = 0;
	var fSKilLServices = new SKilLServices()
	var String fDuplicateTypeName = ""
	var String fDuplicateName = ""	

	def void run() {
		if (ImportTools.getProjectName() != null) {
			fProjectName = ImportTools.getProjectName()
		}

		if (ImportTools.getSelectedToolPath() != null) {
			fSelectedToolPath = ImportTools.getSelectedToolPath();
		// TODO - Convert path to SKilL File
		}

		var project = ResourcesPlugin.getWorkspace.getRoot.getProject(fProjectName);
		var files = fSKilLServices.getAll(project, true);
		for (File f : files) {
			for (Declaration d : f.declarations) {
				System.out.println(EcoreUtil2.getURI(f));
				System.out.println(d.name);
				if (!duplicateType.contains(d.name)) {
					duplicateType.add(d.name);
				} else {
					if (counter == 0) {
						counter += 1;
						fDuplicateName = EcoreUtil2.getURI(f).toString;
						fDuplicateTypeName = d.name;
					} else {
						JOptionPane.showMessageDialog(null, "More than 1 duplicate type!", "Too Many Duplicate Types",
							JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
//				if(d instanceof Usertype) {
//					d.fields;
//				}
			}
		}
	// TODO
//		for (Declaration d : fSelectedToolPath.declarations) {
//			
//		}
	}
}