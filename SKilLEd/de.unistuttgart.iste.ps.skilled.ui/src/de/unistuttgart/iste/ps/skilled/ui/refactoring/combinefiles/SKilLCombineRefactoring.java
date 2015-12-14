package de.unistuttgart.iste.ps.skilled.ui.refactoring.combinefiles;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.eclipse.emf.common.util.URI;

/**
 * This class merges the .skill files selected in the
 * SKilLCombineRefactoringDialog and deletes the original .skill files.
 * 
 * @author Leslie Tso
 * 
 */

public class SKilLCombineRefactoring {

	int numberSelected;
	String[] fCombines;
	String fCombinedSave;
	File[] files;

	String fComment = "";
	String fHead = "";
	String fBody = "";
	String fPreCombinedLocation;
	int aFileHasHeadCommentCheck = 0;
	String namechecker = null;
	String pathchecker = null;
	String[] fPreCombinedWithFolderPath;
	String[] fPreCombinedWithoutProjectFolderPath;
	String[] fPreCombinedWithoutProjectFolderPathAndForwardSlash;
	File workspaceDirectory;

	int fWorkspaceLength;

	public SKilLCombineRefactoring() {
	}

	public static String getName() {
		return "Combine .skill files";
	}

	public void setNumberSelected(int number) {
		numberSelected = number;
	}

	public void setCombine(String[] text) {
		fCombines = new String[numberSelected];
		for (int i = 0; i < numberSelected; i++) {
			fCombines[i] = text[i];
		}
	}

	public void setCombinedSave(String text) {
		fCombinedSave = text;
	}

	public void setWorkSpaceDirectory(File file) {
		workspaceDirectory = file;
	}

	// get file paths of to be combined files and set file path of combined
	// file.
	public void startCombine() {
		files = new File[numberSelected];
		fPreCombinedWithFolderPath = new String[numberSelected];
		fPreCombinedWithoutProjectFolderPath = new String[numberSelected];
		fPreCombinedWithoutProjectFolderPathAndForwardSlash = new String[numberSelected];

		fWorkspaceLength = workspaceDirectory.getAbsolutePath().length();

		for (int i = 0; i < numberSelected; i++) {
			files[i] = new File(fCombines[i]);

			System.out.println("0 is " + fWorkspaceLength);
			fPreCombinedWithFolderPath[i] = fCombines[i].substring(fWorkspaceLength + 1);
			System.out.println("1 is " + fPreCombinedWithFolderPath[i]);
			fPreCombinedWithoutProjectFolderPath[i] = fPreCombinedWithFolderPath[i]
					.substring(fPreCombinedWithFolderPath[i].indexOf(File.separator) + 1);
			System.out.println("2 is " + fPreCombinedWithoutProjectFolderPath[i]);
			fPreCombinedWithoutProjectFolderPathAndForwardSlash[i] = fPreCombinedWithoutProjectFolderPath[i]
					.replace("\\", "/");
			System.out.println("3 is " + fPreCombinedWithoutProjectFolderPathAndForwardSlash[i]);
			// Creates regular expression with the names of
			// pre-combined files
			// if (i == 0) {
			// checker = "\"" +
			// fPreCombinedWithoutProjectFolderPathAndForwardSlash[i] + "\"";
			// }
			// else {
			// checker = checker + "|" + "\"" +
			// fPreCombinedWithoutProjectFolderPathAndForwardSlash[i] + "\"";
			// }
			if (i == 0) {
				namechecker = files[i].getName();
				pathchecker = files[i].getPath();
			} else {
				namechecker = namechecker + "|" + files[i].getName();
				pathchecker = pathchecker + "|" + files[i].getPath();
			}

		}
		organize(files);
	}

	/**
	 * Splits includes and withs, head comments and body for all files
	 * 
	 * @param files
	 *            The pre-combined files
	 */
	public void organize(File[] files) {
		Set<String> duplicateComment = new HashSet<>();
		Set<String> duplicateWithAddress = new HashSet<>();
		Set<String> duplicateIncludeAddress = new HashSet<>();

		// Path of combined file here
		File combinedFile = new File(fCombinedSave);

		for (File f : files) {
			FileInputStream fis;
			int thisFileHasHeadCommentCheck = 0;
			try {

				fis = new FileInputStream(f);
				BufferedReader br1 = new BufferedReader(new InputStreamReader(fis));
				String address;
				String line;
				while ((line = br1.readLine()) != null) {
					// write everything that is not include, with or head
					// comment to fBody
					if (!line.startsWith("include") && !line.startsWith("with") && !line.startsWith("#")) {
						fBody += line + "\n";
					}
					// All includes and withs saved in fHead
					else if (line.startsWith("include") || line.startsWith("with")) {
						// Deletes withs and includes that require the use of
						// the pre-combined files
						Pattern namePattern = Pattern.compile("\"[^\"]*(" + namechecker + ")\"");
						Matcher nameMatcher = namePattern.matcher(line);
						while (nameMatcher.find()) {
							String writtenPath = nameMatcher.group();
							String removeQuotes = writtenPath.substring(1, writtenPath.length() - 1);
							String currentFolder = f.getAbsolutePath().substring(0,
									f.getAbsolutePath().lastIndexOf(File.separator));
							String canonPath = currentFolder + File.separator + removeQuotes;
							String fixSeperator = canonPath.replace("\\", "/");
							File testPath = new File(fixSeperator);
							Pattern pathPattern = Pattern.compile("(" + pathchecker.replace("\\", "\\\\") + ")");
							Matcher pathMatcher = pathPattern.matcher(testPath.getCanonicalPath());
							while (pathMatcher.find()) {
								line = line.replace(writtenPath, "");
							}
						}
						// If with or includes contains no addresses,
						// skip line
						if (!line.matches("with\\s*") && !line.matches("include\\s*")) {
							// If with or includes contains addresses,
							// check for duplicate addresses
							Pattern addressPattern = Pattern.compile("\"o*\"");
							Matcher addressMatcher = addressPattern.matcher(line);
							// Check for duplicates includes and withs
							while (addressMatcher.find()) {
								address = addressMatcher.group();
								if (line.startsWith("with")) {
									if (!duplicateWithAddress.contains(address)) {
										duplicateWithAddress.add(address);
									} else {
										line = line.replace(address + " ", "");
									}
								} else if (line.startsWith("include")) {
									if (!duplicateIncludeAddress.contains(address)) {
										duplicateIncludeAddress.add(address);
									} else {
										line = line.replace(address + " ", "");
									}
								}

							}
							if (!line.matches("with\\s*") && !line.matches("include\\s*")) {
								fHead += line + "\n";
							}
						}

					}
					// All head comments save in fComment
					else if (line.startsWith("#")) {
						String commentChecker = line.substring(1);
						commentChecker.replaceAll("\\s+", "");
						if (!commentChecker.isEmpty()) {
							// Checks for duplicate head comments
							if (!duplicateComment.contains(line)) {
								// There is a head comment in THIS file
								thisFileHasHeadCommentCheck = 1;
								// One of the pre-combined files has a head
								// comment
								aFileHasHeadCommentCheck = 1;
								fComment += line + "\n";
								duplicateComment.add(line);
							}
						}
					}
				}
				// Adds # after the head comment(s) of a file
				if (thisFileHasHeadCommentCheck == 1) {
					fComment += "#\n";
					thisFileHasHeadCommentCheck = 0;
				}
				br1.close();
			} catch (IOException e) {
				StringBuilder sb = new StringBuilder("Error: ");
				sb.append(e.getMessage());
				sb.append("\n");
				for (StackTraceElement ste : e.getStackTrace()) {
					sb.append(ste.toString());
					sb.append("\n");
				}
				JTextArea jta = new JTextArea(sb.toString());
				JScrollPane jsp = new JScrollPane(jta) {
					/**
					 * 
					 */
					private static final long serialVersionUID = -7067159314180395545L;

					@Override
					public Dimension getPreferredSize() {
						return new Dimension(400, 300);
					}
				};
				JOptionPane.showMessageDialog(null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
			}

		}

		if (aFileHasHeadCommentCheck == 1) {
			// Deletes last # in comment.txt
			fComment = fComment.substring(0, fComment.length() - 2);
		}

		combine(combinedFile);

	}

	/**
	 * Writes fComment, fHead and fBody into a single file
	 * 
	 * @param combinedFile
	 *            Creates a file with the combined content of the pre-combined
	 *            files
	 */

	public void combine(File combinedFile) {
		try {
			FileWriter fw = new FileWriter(combinedFile, true);

			if (fComment != null) {
				fw.write(fComment);
			}
			if (fHead != null) {
				fw.write(fHead);
			}
			if (fBody != null) {
				fw.write(fBody);
			}
			fw.close();

			for (int x = 0; x < numberSelected; x++) {
				fPreCombinedLocation = fCombines[x].substring(0, fCombines[x].lastIndexOf(File.separator));
				checkSiblings();
			}

			// Empty strings to remove append errors
			fComment = "";
			fHead = "";
			fBody = "";
			aFileHasHeadCommentCheck = 0;

			// Deletes the original pre-combined files
			for (int i = 0; i < numberSelected; i++) {
				files[i].delete();
			}
		} catch (IOException e) {
			StringBuilder sb = new StringBuilder("Error: ");
			sb.append(e.getMessage());
			sb.append("\n");
			for (StackTraceElement ste : e.getStackTrace()) {
				sb.append(ste.toString());
				sb.append("\n");
			}
			JTextArea jta = new JTextArea(sb.toString());
			JScrollPane jsp = new JScrollPane(jta) {

				private static final long serialVersionUID = 1L;

				@Override
				public Dimension getPreferredSize() {
					return new Dimension(400, 300);
				}
			};
			JOptionPane.showMessageDialog(null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Replace with and includes that require the precombined files with the
	 * combined file.
	 *
	 */
	public void checkSiblings() {
		// Returns the name of the combined file (i.e. combined_file.skill)
		String fSaveName = fCombinedSave.substring(fCombinedSave.lastIndexOf(File.separator) + 1);
		// Returns path of the combined file without the name (i.e.
		// C:\Workspace\Project\Folder instead of
		// C:\Workspace\Project\Folder\Save.skill)
		String fSavePath = fCombinedSave.substring(0, fCombinedSave.lastIndexOf(File.separator));
		// Returns path of the combined file starting from the project folder
		// (i.e. Project\Folder\Save.skill)
		String fSaveNameWithFolderPath = fCombinedSave.substring(fWorkspaceLength + 1);
		// Returns path of the combined file starting from its subfolder (i.e.
		// Save.skill if it is in the project folder, else Folder\Save.skill)
		String fSaveNameWithFolderPathWithoutProjectFolder = fSaveNameWithFolderPath
				.substring(fSaveNameWithFolderPath.indexOf(File.separator) + 1);
		// Replaces all backslashes with forward slashes (for Windows computers)
		String fSaveNameWithForwardSlash = fSaveNameWithFolderPathWithoutProjectFolder.replace("\\", "/");

		// Length of the combined file path
		int fSaveTotalLength = fCombinedSave.length();
		// Length of the path starting from its subfolder
		int fFolderLength = fSaveNameWithForwardSlash.length();
		// Length of the project name
		int ProjectPathLength = fSaveTotalLength - fFolderLength;
		// Returns path of the project folder (i.e. C:\Workspace\Project)
		String fFullProjectPath = fCombinedSave.substring(0, ProjectPathLength);
		System.out.println(fFullProjectPath);
		
		ArrayList<File> listofFiles = new ArrayList<File>();
		listFiles(fFullProjectPath, listofFiles);
		for (File f : listofFiles) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line;
				boolean alreadyReplacedWith = false;
				boolean alreadyReplacedInclude = false;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("include") || line.startsWith("with")) {
						
					}
				}
			} catch (IOException e) { e.printStackTrace();}
			
		}
	}

	/**
	 * Finds all .skill files in the project folder and its subdirectories
	 * 
	 * @param directoryName
	 * @param checkFiles
	 */
	public void listFiles(String directoryName, ArrayList<File> checkFiles) {
		File fProjectDirectory = new File(directoryName);
		File[] listofFiles = fProjectDirectory.listFiles();
		for (File file : listofFiles) {
			if (file.isFile() && file.getName().endsWith(".skill")) {
				checkFiles.add(file);
				System.out.println("File: " + file.getAbsolutePath());
			} else if (file.isDirectory() && !file.getName().startsWith(".")) {
				System.out.println("Directory: " + file.getAbsolutePath());
				listFiles(file.getAbsolutePath(), checkFiles);
			}
		}
	}
}