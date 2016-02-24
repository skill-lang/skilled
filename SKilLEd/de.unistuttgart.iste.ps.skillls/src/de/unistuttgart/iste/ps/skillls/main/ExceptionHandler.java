package de.unistuttgart.iste.ps.skillls.main;

/**
 * Class for handling exceptions.
 *
 * @author Armin Hüneburg
 * @since 25.08.15.
 */
public class ExceptionHandler {

    private static boolean rethrow = true;

    /**
     * Logs the exception.
     *
     * @param e
     *            Exception to log.
     */
    public static void handle(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
        if (rethrow) {
            throw new Error(e);
        }
    }

    /**
     * Logs the exception.
     *
     * @param e
     *            Error to log.
     */
    public static void handle(Error e) {
        System.out.println("Following error occurred: " + e.getMessage());
        if (rethrow) {
            throw new Error(e);
        }
    }

    /**
     * Logs the exception.
     *
     * @param e
     *            Exception to log.
     * @param message
     *            Message to log.
     */
    public static void handle(Exception e, String message) {
        System.out.println(message + "\n" + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Handles tool breaking exceptions.
     * @param e the tool breaking exception
     * @param message the message to print
     */
    public static void handle(BreakageException e, String message) {
        System.out.println(message + "\n" + e.getMessage());
        if (rethrow) {
            throw e;
        }
    }

    /**
     * Handles tool breaking exceptions.
     * @param e the tool breaking exception
     */
    public static void handle(BreakageException e) {
        if (rethrow) {
            throw e;
        }
    }

    /**
     * decides whether exceptions should be rethrown.
     * @param rethrow
     */
    public static void setRethrow(boolean rethrow) {
        ExceptionHandler.rethrow = rethrow;
    }
}
