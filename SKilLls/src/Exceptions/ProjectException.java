package Exceptions;

/**
 * Exception thrown when something is not right with the SKilLEd-project.
 *
 * @author Armin Hüneburg
 * @since 18.08.15.
 */
public class ProjectException extends Exception {

    /**
     *
     * @param message The message that is shown with the exception.
     */
    public ProjectException(String message) {
        super(message);
    }
}
