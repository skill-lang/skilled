package de.unistuttgart.iste.ps.skillls.main;

/**
 * Class for handling program arguments.
 *
 * @author Armin Hüneburg
 */
public class ArgumentEvaluation {
	private final int INDEX;
	private final String ARGUMENT;
	private final String NAME;

	/**
	 * Constructor. Only way to set the attributes.
	 *
	 * @param index
	 *            the last index this argument is concerned with.
	 * @param argument
	 *            if the argument has a second parameter, this is it.
	 * @param name
	 *            the name of the argument.
	 */
	public ArgumentEvaluation(int index, String argument, String name) {
		this.INDEX = index;
		this.ARGUMENT = argument;
		this.NAME = name;
	}

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return INDEX;
	}

	/**
	 * @return returns the parameter to the argument.
	 */
	public String getArgument() {
		return ARGUMENT;
	}

	/**
	 * @return returns the name of the argument.
	 */
	public String getName() {
		return NAME;
	}
}
