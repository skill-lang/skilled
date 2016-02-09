package de.unistuttgart.iste.ps.skilled.ui.tools.imprt

import java.io.File
import java.util.ArrayList

class ImportCombine {
	var String fImportedFile = ""
	var ArrayList<File> fListofFiles = null
	
	def void run() {
		if (ImportTools.getListofFiles() != null) {
			fListofFiles = ImportTools.getListofFiles()
		}
		if (ImportTools.getImportLocation() != null) {
			fImportedFile = ImportTools.getImportLocation()
		}
	}
	
}