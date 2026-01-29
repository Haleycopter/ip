package howly.common;

/**
 * Represents exceptions specific to the Howly chatbot app.
 * Used to handle user input errors and file processing issues.
 */
public class HowlyException extends Exception {
    public HowlyException(String userInput) {
        super(userInput);
    }
}
