package howly.parser;

import howly.commands.*;
import howly.common.HowlyException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parser deals with making sense of the user command.
 */
public class Parser {

    /**
     * Parses the full user input and returns the corresponding Command object.
     *
     * @param fullCommand input entered by the user.
     * @return A Command object that can be executed.
     * @throws HowlyException If the command is unknown or arguments are invalid.
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

    // Helper Parsing Methods, called by the specific Command classes during execution.

    // Extract description for a ToDo task
    public static String parseTodo(String input) throws HowlyException {
        String desc = input.replaceFirst("(?i)todo", "").trim();
        if (desc.isEmpty()) {
            throw new HowlyException("You have to specify the task after 'todo'.");
        }
        return desc;
    }

    /**
     * Parses the input for a deadline task.
     * @return String array: [description, by]
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
     * Parses the input for an event task.
     * @return String array: [description, from, to]
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

    // Extract index (as an int) from mark, unmark and delete commands
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

    // Parses date for finddate command
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
