package de.unistuttgart.iste.ps.skillls.main;

/**
 * Class for normalizing names
 *
 * Replaces _ in the middle of words with empty strings, except there are
 * multiple.
 *
 * Created on 29.02.16.
 *
 * @author Armin Hüneburg
 */
public class NameUtil {
	/**
	 * normalizes a name in the skill context single _ in the middle of a word
	 * and multiple _ in the end are ignored capitalization is ignored
	 *
	 * @param name
	 *            the name, that should be normalized
	 * @return the normalized name
	 */
	public static String normalize(String name) {
		StringBuilder builder = new StringBuilder();
		String[] splits = name.split(" ");
		if (splits.length > 1) {
			for (String s : splits) {
				builder.append(normalize(s));
				builder.append(' ');
			}
			builder.setLength(builder.length() - 1);
			return builder.toString();
		}
		String converted = name.toLowerCase();
		int index = 0;
		while (converted.charAt(index) == '_') {
			index++;
			if (index == converted.length())
				return converted;
		}
		boolean wasUnderscore = true;
		while (index < converted.length()) {
			if (converted.charAt(index) != '_') {
				index++;
				wasUnderscore = false;
				continue;
			}
			if (!wasUnderscore) {
				wasUnderscore = true;
				converted = converted.substring(0, index) + converted.substring(index + 1);
				continue;
			}
			index++;
		}
		if (!converted.matches("_*")) {
			while (converted.charAt(converted.length() - 1) == '_')
				converted = converted.substring(0, converted.length() - 1);
		}
		return converted;
	}
}
