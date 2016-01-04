package de.unistuttgart.iste.ps.skillls.main;

/**
 * Class for handling exceptions.
 *
 * @author Armin Hüneburg
 * @since 25.08.15.
 */
class ExceptionHandler {

    /**
     * Logs the exception.
     *
     * @param e
     *            Exception to log.
     */
    public static void handle(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }

    /**
     * Logs the exception.
     *
     * @param e
     *            Error to log.
     */
    public static void handle(Error e) {
        System.out.println("Following error occurred: " + e.getMessage());
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
}
