package de.unistuttgart.iste.ps.skilled.ui.tools.export;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.iste.ps.skillls.tools.Tool;

public class SaveListofAllTools {
	List<String> fToolNameList = null;

	// Gets the names of the tools from
	// de.unistuttgart.iste.ps.skilled.ui.views.Toolview.java
	public void setListofAllTools(ArrayList<Tool> allToolList) {
		// TODO
		if (allToolList.size() > 0) {
			fToolNameList = new ArrayList<String>();
			for (int i = 0; i < allToolList.size(); i++) {
				fToolNameList.add(allToolList.get(i).getName());
			}
		}
		System.out.println("fToolNameList size is: " + fToolNameList.size());
	}
	
	public List<String> getToolNameList() {
		return fToolNameList;
		
	}

}
