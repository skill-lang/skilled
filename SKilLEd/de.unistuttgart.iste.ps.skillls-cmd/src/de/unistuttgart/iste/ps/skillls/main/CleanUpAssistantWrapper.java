package de.unistuttgart.iste.ps.skillls.main;

/**
 * Class for wrapping an instance of {@link CleanUpAssistant}
 *
 * Created on 28.02.16.
 *
 * @author Armin Hüneburg
 */
public class CleanUpAssistantWrapper {
	private CleanUpAssistant instance;

	/**
	 *
	 * @param instance
	 *            the instance that should be returned
	 */
	public void set(CleanUpAssistant instance) {
		this.instance = instance;
	}

	/**
	 *
	 * @return returns the instance
	 */
	public CleanUpAssistant get() {
		return instance;
	}
}
