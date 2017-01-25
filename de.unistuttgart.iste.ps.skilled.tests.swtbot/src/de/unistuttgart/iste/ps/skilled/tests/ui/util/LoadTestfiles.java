package de.unistuttgart.iste.ps.skilled.tests.ui.util;

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

/*
 * @author Jan Berberich
 */
public class LoadTestfiles {
	
	/**
	 * Returns the performence testFile as a String.
	 * @return TestFileSpecification.skill as String.
	 * 
	 */
	public static String loadPerformenceTestfile() {
		return loadFile("resources" + File.separator +"TestFileSpecification.skill");
	}
	
	/**
	 * Testfile for validating import binary test.
	 * @return Testfile as String
	 */
	public static String loadImportTestfile(){
		return loadFile("resources"+ File.separator + "testImport" + File.separator+ "age.skill");
	}
	
	/**
	 * Load test files for the combine skill files refactoring.
	 * @return Array with content of file 1, content of file 2 and combined content of both files
	 */
	public static String[] loadCombineTest(){
		String[] combine = loadArray("combine1", "combine2", "combine12");
		return combine;
	}
	
	/**
	 * Load String array with the content of three files
	 * @return The array
	 */
	private static String[] loadArray(String file1, String file2, String file3){
		String[] combine = new String[3];
		combine [0]= loadFile("resources" + File.separator +"testCombine"+File.separator+file1+".skill");
		combine [1]= loadFile("resources" + File.separator +"testCombine"+File.separator+file2+".skill");
		combine [2]= loadFile("resources" + File.separator +"testCombine"+File.separator+file3+".skill");
		return combine;
	}
	
	/**
	 * Loads a file.
	 * @param path Path of the file
	 * @return Content of the file
	 */
	private static String loadFile(String path){
		String fileContent = "";
		FileReader reader;
		try {
			reader = new FileReader(path);
			BufferedReader read = new BufferedReader(reader);
			String line= read.readLine();
			while (line!= null) {
				fileContent = fileContent + String.format("%n") + line;
				line = read.readLine();
			}
			read.close();
		} catch (Exception e) {
			return null;
		}
		return fileContent;
	}
	
	
}
