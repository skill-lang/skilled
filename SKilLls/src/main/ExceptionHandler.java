package main;

/**
 * @Author Armin Hüneburg
 * @since 25.08.15.
 */
public class ExceptionHandler {

    /**
     * Logs the exception.
     *
     * @param e
     *            Exception to log.
     */
    public static void handle(Exception e) {
        // TODO
    }

    /**
     * Logs the exception.
     *
     * @param e
     *            Exception to log.
     * @param message
     *            Message that should be shown to the user.
     */
    @SuppressWarnings("unused")
    public static void handle(Exception e, @SuppressWarnings("SameParameterValue") String message) {
        System.out.println(message);
        e.printStackTrace();
    }
}
