package de.unistuttgart.iste.ps.skilled.tests.ui.util;

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

public class LoadTestfiles {
	
	/**
	 * Returns the performence testFile as a String.
	 * 
	 * @return TestFileSpecification.skill as String.
	 * 
	 */
	public static String loadPerformenceTestfile() {
		return loadFile("resources" + File.separator +"TestFileSpecification.skill");
	}
	
	/**
	 * Testfile for validating import test.
	 * @return Testfile as String
	 */
	public static String loadImportTestfile(){
		return loadFile("resources"+ File.separator + "testImport" + File.separator+ "age.skill");
	}
	
	/**
	 * 
	 * @return Array with content of file 1, content of file 2 and combined content
	 */
	public static String[] loadCombineTest(){
		String[] combine = loadArray("combine1", "combine2", "combine12");
		return combine;
	}
	
	
	private static String[] loadArray(String file1, String file2, String file3){
		String[] combine = new String[3];
		combine [0]= loadFile("resources" + File.separator +"testCombine"+File.separator+file1+".skill");
		combine [1]= loadFile("resources" + File.separator +"testCombine"+File.separator+file2+".skill");
		combine [2]= loadFile("resources" + File.separator +"testCombine"+File.separator+file3+".skill");
		return combine;
	}
	
	
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
