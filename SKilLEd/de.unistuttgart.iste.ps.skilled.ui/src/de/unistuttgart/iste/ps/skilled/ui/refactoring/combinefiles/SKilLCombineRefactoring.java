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

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

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
	String checker = null;
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
				checker = files[i].getName();
			} else {
				checker = checker + "|" + files[i].getName();
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
						line = line.replaceAll("\"[^\"]*(" + checker + ")\"", "");
						line = line.replaceAll("\\s+", " ");
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
				// checkSiblings();
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
	@SuppressWarnings("null")
	public void checkSiblings() {
		ArrayList<File> listofFiles = null;
		String fLineChecker = "";
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
		listFiles(fFullProjectPath, listofFiles);

		for (File f : listofFiles) {
			for (int z = 0; z < fCombines.length; z++) {
				// Path which the code should replace with
				String fReplacement = "";
				String fCurrentName = "\"" + f.getName() + "\"";
				String fCurrentFilePath = f.getAbsolutePath().substring(0,
						f.getAbsolutePath().lastIndexOf(File.separator + 1));
				String fCurrentFilePathWithoutWorkspace = fCurrentFilePath.substring(fWorkspaceLength + 1);
				String fCurrentFilePathWithoutProject = fCurrentFilePathWithoutWorkspace
						.substring(fCurrentFilePathWithoutWorkspace.indexOf(File.separator + 1));

				Pattern pattern = Pattern.compile(checker);
				Matcher matcher = pattern.matcher(fCurrentName);
				if (!matcher.find() && !f.getName().equals(fSaveName)) {
					boolean alreadyReplacedWith = false;
					boolean alreadyReplacedInclude = false;
					FileInputStream fis;
					int fDifferentFolder = 0;

					// Hierarchy level of the current file. Greater the number,
					// lower it is on the hierachy.
					int fCurrentFileHierarchyLevel = fCurrentFilePathWithoutProject.length()
							- fCurrentFilePathWithoutProject.replace(File.separator, "").length();
					// Hierarchy level of the combined file. Greater the number,
					// lower it is in the hierarchy
					int fSavedFileHierarchyLevel = fSaveNameWithFolderPathWithoutProjectFolder.length()
							- fSaveNameWithFolderPathWithoutProjectFolder.replace(File.separator, "").length();
					int fBigger;
					if (fCurrentFileHierarchyLevel >= fSavedFileHierarchyLevel) {
						fBigger = fCurrentFileHierarchyLevel;
					} else {
						fBigger = fSavedFileHierarchyLevel;
					}

					// If current file has the same parent as the combined file,
					// return name of the combined file (i.e. Save.skill)
					if (fCurrentFilePath.equals(fSavePath)) {
						fReplacement = fSaveName;
					}
					// Check if first folder in path of the current file is the
					// same as the first folder in path of the combined file
					// (maybe this if is unneeded? else{} enough?)
					else if (fCurrentFilePathWithoutProject
							.substring(0, fCurrentFilePathWithoutProject.indexOf(File.separator + 1))
							.equals(fSaveNameWithFolderPathWithoutProjectFolder.substring(0,
									fSaveNameWithFolderPathWithoutProjectFolder.indexOf(File.separator + 1)))) {
						for (int i = 0; i < fBigger; i++) {
							// If yes, child folder is the new first folder in
							// the path
							if (fCurrentFilePathWithoutProject
									.substring(0, fCurrentFilePathWithoutProject.indexOf(File.separator + 1))
									.equals(fSaveNameWithFolderPathWithoutProjectFolder.substring(0,
											fSaveNameWithFolderPathWithoutProjectFolder.indexOf(File.separator + 1)))) {
								fCurrentFilePathWithoutProject = fCurrentFilePathWithoutProject
										.substring(fCurrentFilePathWithoutProject.indexOf(File.separator) + 1);
								fSaveNameWithFolderPathWithoutProjectFolder = fSaveNameWithFolderPathWithoutProjectFolder
										.substring(fSaveNameWithFolderPathWithoutProjectFolder.indexOf(File.separator)
												+ 1);
							} else {
								// if the current file is lower in the hierarchy
								// as the combined file, return the path
								// starting from the difference in paths
								if (fBigger == fCurrentFileHierarchyLevel) {
									fReplacement = fSaveNameWithFolderPathWithoutProjectFolder;
									break;
								}
								// if the current file is higher in the
								// hierarchy as the combined file, find out how
								// many folders it has to backtrack in order to
								// return to the folder where the two folders
								// seperated
								else {
									fDifferentFolder = fDifferentFolder + 1;
									fCurrentFilePathWithoutProject = fCurrentFilePathWithoutProject
											.substring(fCurrentFilePathWithoutProject.indexOf(File.separator) + 1);
									fSaveNameWithFolderPathWithoutProjectFolder = fSaveNameWithFolderPathWithoutProjectFolder
											.substring(
													fSaveNameWithFolderPathWithoutProjectFolder.indexOf(File.separator)
															+ 1);
								}
								// For the number of backtracks write a "../"
								// and add the path (starting from the
								// difference) (i.e. if current file is in
								// Folder\Folder2\Current.skill and the combined
								// file is in Folder\Save.skill, fReplacement
								// would return "../Save.skill"
								for (int j = 0; j == fDifferentFolder; j++) {
									if (j != fDifferentFolder) {
										fReplacement += "../";
									} else {
										String fCurrentPathWithFowardSlash = fSaveNameWithFolderPathWithoutProjectFolder
												.replace("\\", "/");
										fReplacement += fCurrentPathWithFowardSlash;
									}
								}
								fDifferentFolder = 0;

							}
						}

					}
					// If first folder of the current folder is not the same as
					// the first folder of the combined file, return a "../" for
					// every folder in the current file path and add the path to
					// the save file to the end (i.e. current folder path
					// Folder\Current.skill and combined folder path
					// Folder2\Save.skill returns ../Folder2/Save.skill)
					else if (!fCurrentFilePathWithoutProject
							.substring(0, fCurrentFilePathWithoutProject.indexOf(File.separator + 1))
							.equals(fSaveNameWithFolderPathWithoutProjectFolder.substring(0,
									fSaveNameWithFolderPathWithoutProjectFolder.indexOf(File.separator + 1)))) {
						fDifferentFolder = fCurrentFileHierarchyLevel;
						for (int j = 0; j == fDifferentFolder; j++) {
							if (j != fDifferentFolder) {
								fReplacement += "../";
							} else {
								fReplacement += fSaveNameWithForwardSlash;
							}
						}
						fDifferentFolder = 0;
					}

					try {
						fis = new FileInputStream(f);
						BufferedReader br = new BufferedReader(new InputStreamReader(fis));
						String line;
						while ((line = br.readLine()) != null) {
							if (line.startsWith("include") || line.startsWith("with")) {
								// Deletes withs and includes that require the
								// use of the pre-combined files
								if (line.startsWith("with") && alreadyReplacedWith == false) {
									line = line.replaceFirst("\"[^\"]*(" + checker + ")\"", "\"" + fReplacement + "\"");
									line = line.replaceAll(checker, "");
									alreadyReplacedWith = true;
								} else if (line.startsWith("with") && alreadyReplacedWith == true) {
									line = line.replaceAll("\"[^\"]*(" + checker + ")\"", "");
								} else if (line.startsWith("include") && alreadyReplacedInclude == false) {
									line = line.replaceFirst(checker, "\"" + fReplacement + "\"");
									line = line.replaceAll("\"[^\"]*(" + checker + ")\"", "");
									alreadyReplacedInclude = true;
								} else if (line.startsWith("include") && alreadyReplacedInclude == true) {
									line = line.replaceAll("\"[^\"]*(" + checker + ")\"", "");
								}
								if (!line.matches("with\\s*") && !line.matches("include\\s*")) {
									fLineChecker += line + "\n";
								}
							} else {
								fLineChecker += line + "\n";
							}

						}
						FileWriter fw = new FileWriter(f, false);
						if (fLineChecker != null) {
							fw.write(fLineChecker);
							alreadyReplacedWith = false;
							alreadyReplacedInclude = false;
							fLineChecker = "";
						}
						fw.close();
						br.close();
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

							private static final long serialVersionUID = -7067159314180395545L;

							@Override
							public Dimension getPreferredSize() {
								return new Dimension(400, 300);
							}
						};
						JOptionPane.showMessageDialog(null, jsp, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
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
			} else if (file.isDirectory() && !file.getName().startsWith(".")) {
				listFiles(file.getAbsolutePath(), checkFiles);
			}
		}
	}
}