package howly.common;

public class HowlyException extends Exception {
    public HowlyException(String userInput) {
        super(userInput);
    }
}
