package de.unistuttgart.iste.ps.skilled.tests.refactorings.combinefiles;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.unistuttgart.iste.ps.skilled.ui.refactoring.combinefiles.SKilLCombineRefactoring;
/**
 * Unit Tests for de.unistuttgart.iste.ps.skilled.ui.refactoring2.SKilLCombineRefactoring.java
 * @author Leslie
 *
 */
public class TestCombineSKilLFiles {
	String replaceWhiteSpaces(String input) {
		return input.replace("\r\n", "").replace("\n", "");
	}
	SKilLCombineRefactoring scr = new SKilLCombineRefactoring();
	int number;
	String directory = "resources" + File.separator + "refactorings" + File.separator + "combinefiles" + File.separator + "project" + File.separator;
	File workspaceDirectory = new File("resources" + File.separator + "refactorings" + File.separator + "combinefiles" + File.separator);
	String[] selectedFiles = new String[2];

	@Test
	public void DuplicateHeadCommentsAreRemoved() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "DuplicateHeadCommentsAreRemovedA.skill";
		selectedFiles[1] = directory + "DuplicateHeadCommentsAreRemovedB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "DuplicateHeadCommentsAreRemovedResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "DuplicateHeadCommentsAreRemovedC.skill");
		File Result = new File(saveLocation);
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(Result)));
	}
	
	@Test
	public void HashtagBetweenHeadCommentsFromDifferentFiles() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "HashtagBetweenHeadCommentsFromDifferentFilesA.skill";
		selectedFiles[1] = directory + "HashtagBetweenHeadCommentsFromDifferentFilesB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "HashtagBetweenHeadCommentsFromDifferentFilesResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "HashtagBetweenHeadCommentsFromDifferentFilesC.skill");
		File Result = new File(saveLocation);
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(Result)));
	}
	
	@Test
	public void RemoveDuplicateWithAndIncludeAddresses() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "RemoveCertainWithAndIncludeAddressesA.skill";
		selectedFiles[1] = directory + "RemoveCertainWithAndIncludeAddressesB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "RemoveCertainWithAndIncludeAddressesResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "RemoveCertainWithAndIncludeAddressesC.skill");
		File Result = new File(saveLocation);
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(Result)));
	}
	
	@Test
	public void RemoveWithAndIncludeAddressesThatRequireThePreCombinedFiles() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "RemoveWithAndIncludeAddressesThatRequireThePreCombinedFilesA.skill";
		selectedFiles[1] = directory + "RemoveWithAndIncludeAddressesThatRequireThePreCombinedFilesB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "RemoveWithAndIncludeAddressesThatRequireThePreCombinedFilesResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "RemoveWithAndIncludeAddressesThatRequireThePreCombinedFilesC.skill");
		File Result = new File(saveLocation);
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(Result)));		
	}
	
	@Test
	public void TestAllTheAboveAtTheSameTime() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "TestAllTheAboveAtTheSameTimeA.skill";
		selectedFiles[1] = directory + "TestAllTheAboveAtTheSameTimeB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "TestAllTheAboveAtTheSameTimeResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "TestAllTheAboveAtTheSameTimeC.skill");
		File Result = new File(saveLocation);
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(Result)));
	}
	
	@Test
	public void ReplacePreCombinedAddressesWithCombinedFileAddress() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "ReplacePreCombinedAddressesWithCombinedFileAddressA.skill";
		selectedFiles[1] = directory + "ReplacePreCombinedAddressesWithCombinedFileAddressB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "ReplacePreCombinedAddressesWithCombinedFileAddressResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "ReplacePreCombinedAddressesWithCombinedFileAddressC.skill");
		File D = new File(directory + "ReplacePreCombinedAddressesWithCombinedFileAddressD.skill");
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(D)));
	}
	@Test
	public void ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFolders() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersA.skill";
		selectedFiles[1] = directory + "LEVEL1A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "LEVEL1A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "LEVEL1A\\eplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersC.skill");
		File C1 = new File(directory + "LEVEL1A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersC1.skill");
		File D = new File(directory + "LEVEL1A\\LEVEL2A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersD.skill");
		File D1 = new File(directory + "LEVEL1A\\LEVEL2A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersD1.skill");
		File E = new File(directory + "ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersE.skill");
		File E1 = new File(directory + "ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSubFoldersE1.skill");
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(C1)));
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(D)), replaceWhiteSpaces(FileUtils.readFileToString(D1)));
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(E)), replaceWhiteSpaces(FileUtils.readFileToString(E1)));
	}
	
	@Test
	public void ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFolders() throws IOException {
		scr.setWorkSpaceDirectory(workspaceDirectory);
		number = 2;
		scr.setNumberSelected(number);
		selectedFiles[0] = directory + "LEVEL1A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersA.skill";
		selectedFiles[1] = directory + "LEVEL1A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersB.skill";
		scr.setCombine(selectedFiles);
		String saveLocation = directory + "LEVEL1A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersResult.skill";
		scr.setCombinedSave(saveLocation);
		scr.startCombine();
		File C = new File(directory + "LEVEL1B\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersC.skill");
		File C1 = new File(directory + "LEVEL1B\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersC1.skill");
		File D = new File(directory + "LEVEL1B\\LEVEL2A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersD.skill");
		File D1 = new File(directory + "LEVEL1B\\LEVEL2A\\ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersD1.skill");
		File E = new File(directory + "ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersE.skill");
		File E1 = new File(directory + "ReplacePreCombinedAddressesWithCombinedFileAddressForFilesInSiblingFoldersE1.skill");
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(C)), replaceWhiteSpaces(FileUtils.readFileToString(C1)));
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(D)), replaceWhiteSpaces(FileUtils.readFileToString(D1)));
		assertEquals("The files are the same.", replaceWhiteSpaces(FileUtils.readFileToString(E)), replaceWhiteSpaces(FileUtils.readFileToString(E1)));
	}
}

	