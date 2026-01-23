package howly.commands;

public enum CommandType {
    TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, UNKNOWN, BYE, FINDDATE, FIND;

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
