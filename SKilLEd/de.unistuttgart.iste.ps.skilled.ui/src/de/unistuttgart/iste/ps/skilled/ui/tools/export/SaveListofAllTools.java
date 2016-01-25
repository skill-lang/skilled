package de.unistuttgart.iste.ps.skilled.ui.tools.export;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.iste.ps.skillls.tools.Tool;

/**
 * This class receives and saves tool name and path information from
 * de.unistuttgart.iste.ps.skilled.ui.views.Toolview.java and sends it to
 * ExportTools.java
 * 
 * @author Leslie Tso
 *
 */
public class SaveListofAllTools {
	static List<String> fToolNameList = null;
	static List<String> fToolPathList = null;

	// Ensures that SaveListofAllTools is a singleton class
	private static SaveListofAllTools instance = null;

	public SaveListofAllTools() {
	}

	public static SaveListofAllTools getInstance() {
		if (instance == null) {
			instance = new SaveListofAllTools();
		}
		return instance;
	}

	// Gets the names of the tools from
	// de.unistuttgart.iste.ps.skilled.ui.views.Toolview.java
	public void setListofAllTools(ArrayList<Tool> allToolList) {
		// TODO
		if (allToolList.size() > 0) {
			fToolNameList = new ArrayList<String>();
			for (int i = 0; i < allToolList.size(); i++) {
				fToolNameList.add(allToolList.get(i).getName());

			}
			System.out.println("fToolNameList: " + fToolNameList.toString());
		}
	}

	// Gets the paths of the tools from
	// de.unistuttgart.iste.ps.skilled.ui.views.Toolview.java
	public void setPathofAllTools(ArrayList<String> pathList) {
		if (pathList.size() > 0) {
			fToolPathList = new ArrayList<String>();
			for (int i = 0; i < pathList.size(); i++) {
				fToolPathList.add(pathList.get(i));
			}
			System.out.println("fToolPathList: " + fToolPathList.toString());
		}
	}

	public static List<String> getToolNameList() {
		return fToolNameList;
	}

	public static List<String> getToolPathList() {
		return fToolPathList;
	}

}
