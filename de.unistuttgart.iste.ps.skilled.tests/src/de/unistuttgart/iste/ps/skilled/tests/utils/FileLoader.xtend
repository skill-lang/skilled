package de.unistuttgart.iste.ps.skilled.tests.utils;

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * @author Tobias Heck
 */
class FileLoader {
	
	public def static String loadFile(String s) {
		var String fileContent = "";
		val reader = new FileReader("resources" + File.separator +
			s + ".skill")
		val read = new BufferedReader(reader);
		var boolean eof = false;
		while (!eof) {
			val line = read.readLine;
			if (line == null) {
				eof = true;
			} else {
				 fileContent = fileContent + String.format("%n") + line;
			}
		}
		return fileContent;
	}
	
}