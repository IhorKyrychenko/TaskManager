package sample.model;

/**
 * Class to generate exceptions
 * @author Ihor Kyrychenko
 */
public class TaskException extends Exception {

    /**
     * Constructor with parameter
     * @param message - message
     */
    public TaskException(String message) {
        super(message);
    }

    public TaskException() {
        super();
    }
}
