package de.unistuttgart.iste.ps.skilled.validation;


/**
 * ASCII-Check for the ASCIICharValidator.
 * 
 * @author Jan Berberich
 */
public class checkASCII {
	/**
	 * This method checks a String for non-ASCII-chars
	 * @param checkString The String to be checked
	 * @return True if checkString has no non-ASCII-Chars; else false 
	 */
	public static boolean isPureAscii(String checkString) {
        return checkString.chars().allMatch(c -> (c &0x7f)==c);
	}
}
