package howly.commands;
/**
 * Represents the various types of commands that the Howly chatbot can execute.
 * This enum is used by the Parser to categorize user input and determine
 * which Command object to instantiate.
 */
public enum CommandType {
    TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, UNKNOWN, BYE, FINDDATE;

    /**
     * Converts a string to a CommandType safely.
     * @param userInput The command word from user input
     * @return The matching Enum or UNKNOWN
     */
    public static CommandType fromString(String userInput) {
        try {
            return CommandType.valueOf(userInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
