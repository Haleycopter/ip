package howly.parser;

import howly.commands.*;
import howly.common.HowlyException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
/**
 * Handles the logic for interpreting user input strings and converting them into
 * actionable {@code Command} objects.
 * This class serves as the brain for deciphering the syntax of the Howly chatbot commands.
 */
public class Parser {

    /**
     * Parses the full user input and returns the corresponding Command object.
     * This method identifies the primary command word and delegates further
     * parsing to specific command types.
     *
     * @param fullCommand The raw input string entered by the user.
     * @return A {@code Command} object that matches the user's intent.
     * @throws HowlyException If the command word is unrecognized or the input is malformed.
     */
    public static Command parse(String fullCommand) throws HowlyException {
        String[] parts = fullCommand.split(" ", 2);
        CommandType type = CommandType.fromString(parts[0]);

        return switch (type) {
            case BYE -> new ExitCommand(fullCommand);
            case LIST -> new ListCommand(fullCommand);
            case DELETE -> new DeleteCommand(fullCommand);
            case MARK -> new MarkCommand(fullCommand, true);
            case UNMARK -> new MarkCommand(fullCommand, false);
            case FINDDATE -> new FindDateCommand(fullCommand);
            case TODO, DEADLINE, EVENT -> new AddCommand(fullCommand, type);
            default -> throw new HowlyException("I'm sorry, I don't know what that means.");
        };
    }

    /**
     * Extracts the description for a ToDo task from the user input.
     * Non-trivial as it uses regex to case-insensitively strip the command word.
     *
     * @param input The raw input string (e.g., "todo read book").
     * @return The trimmed description of the task.
     * @throws HowlyException If the description field is empty.
     */
    public static String parseTodo(String input) throws HowlyException {
        String desc = input.replaceFirst("(?i)todo", "").trim();
        if (desc.isEmpty()) {
            throw new HowlyException("You have to specify the task after 'todo'.");
        }
        return desc;
    }

    /**
     * Parses the input for a deadline task into its description and due date.
     * Expects the format: description /by yyyy-mm-dd.
     *
     * @param input The raw input string.
     * @return A {@code String} array where index 0 is the description and index 1 is the deadline date.
     * @throws HowlyException If the /by delimiter is missing or if fields are empty.
     */
    public static String[] parseDeadline(String input) throws HowlyException {
        String content = input.replaceFirst("(?i)deadline", "").trim();
        if (!content.contains("/by")) {
            throw new HowlyException("A deadline must include /by. Eg: deadline return book /by 2025-12-31");
        }
        String[] parts = content.split("/by", 2);
        if (parts[0].trim().isEmpty()) {
            throw new HowlyException("The description of a deadline cannot be empty.");
        }
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new HowlyException("The deadline date (/by) cannot be empty.");
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    /**
     * Parses the input for an event task into its description, start time, and end time.
     * Uses regex to split the string by both /from and /to markers.
     *
     * @param input The raw input string.
     * @return A {@code String} array where index 0 is description, index 1 is start, and index 2 is end.
     * @throws HowlyException If markers are missing or any field is empty.
     */
    public static String[] parseEvent(String input) throws HowlyException {
        String content = input.replaceFirst("(?i)event", "").trim();
        if (!content.contains("/from") || !content.contains("/to")) {
            throw new HowlyException("An event must include /from and /to. Eg: event meeting /from 2025-10-15 /to 2025-10-16");
        }

        // Split by /from and /to. Using regex to handle both.
        String[] parts = content.split("/from|/to");
        if (parts.length < 3 || parts[0].trim().isEmpty() ||
                parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new HowlyException("Event description, /from, and /to fields cannot be empty.");
        }
        return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
    }

    /**
     * Extracts and adjusts the task index from a user command.
     * This method converts the 1-based index used by users to a 0-based index used by {@code TaskList}.
     *
     * @param input The command string containing the index (e.g., "delete 3").
     * @return The 0-based integer index of the task.
     * @throws HowlyException If the index is missing or not a valid number.
     */
    public static int parseIndex(String input) throws HowlyException {
        try {
            String[] parts = input.split(" ");
            if (parts.length < 2) {
                throw new HowlyException("Please specify a task number.");
            }
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new HowlyException("Please provide a valid task number.");
        }
    }

    /**
     * Parses a date string from the user input into a {@code LocalDate} object.
     *
     * @param input The command string containing the date (e.g., "finddate 2026-01-23").
     * @return The parsed {@code LocalDate}.
     * @throws HowlyException If the date format is incorrect or missing.
     */
    public static LocalDate parseDate(String input) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new HowlyException("Please specify a date in yyyy-mm-dd format.");
        }
        try {
            return LocalDate.parse(parts[1]);
        } catch (DateTimeParseException e) {
            throw new HowlyException("Please use the date format yyyy-mm-dd.");
        }
    }
}
