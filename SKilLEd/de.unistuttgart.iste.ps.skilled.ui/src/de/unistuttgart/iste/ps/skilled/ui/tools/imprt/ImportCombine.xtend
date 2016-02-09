package de.unistuttgart.iste.ps.skilled.ui.tools.imprt

import java.io.File
import java.util.ArrayList
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.Path

class ImportCombine {
	var String fImportedFile = ""
	var IProject fProject

	def void run() {
		if (ImportTools.getImportLocation() != null) {
			fImportedFile = ImportTools.getImportLocation()
		}
	}
}