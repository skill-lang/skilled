package de.unistuttgart.iste.ps.skilled.SWTbotTest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class LoadTestfile {

    /**
     * Returns the testFile as a String.
     * 
     * @return TestFileSpecification.skill as String.
     * 
     */
    public static String loadTestfile() {
        String fileContent = "";
        FileReader reader;
        try {
            reader = new FileReader("resources" + File.separator + "TestFileSpecification.skill");
            BufferedReader read = new BufferedReader(reader);
            String line = read.readLine();
            while (line != null) {
                fileContent = fileContent + String.format("%n") + line;
                line = read.readLine();
            }
            read.close();
        } catch (@SuppressWarnings("unused") Exception e) {
            return null;
        }
        return fileContent;
    }
}
