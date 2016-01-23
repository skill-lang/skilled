package de.unistuttgart.iste.ps.skilled.util;

/**
 * @author Jan Berberich
 * @author Daniel Ryan Degutis
 */
public class CheckASCII {
    public static boolean isPureAscii(String checkString) {
        return checkString.chars().allMatch(c -> (c & 0x7f) == c);
    }
}
