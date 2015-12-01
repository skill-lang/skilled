package de.unistuttgart.iste.ps.skilled.ui.refactoring.removehints;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.utils.EditorUtils;

public class SKilLRemoveHints {

	public void run() {
		String editor = EditorUtils.getActiveXtextEditor().toString();
		File f = new File(editor);
		try {
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			FileInputStream fis = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			while ((line = br.readLine()) != null) {
				//TODO
				if (!line.startsWith("!")) {
					bw.write(line);
					bw.newLine();
				}
			}
			br.close();
			bw.close();
		} catch (IOException e) {
			// TODO
		}
	}

}
