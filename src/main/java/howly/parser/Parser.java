package howly.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import howly.commands.AddCommand;
import howly.commands.Command;
import howly.commands.CommandType;
import howly.commands.DeleteCommand;
import howly.commands.ExitCommand;
import howly.commands.FindCommand;
import howly.commands.FindDateCommand;
import howly.commands.ListCommand;
import howly.commands.MarkCommand;
import howly.common.HowlyException;

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
        String commandWord = parts[0];
        String arguments = parts.length > 1 ? parts[1].trim() : "";
        CommandType type = CommandType.fromString(commandWord);

        return switch (type) {
        case BYE -> prepareExit(arguments);
        case LIST -> prepareList(arguments);
        case DELETE -> prepareDelete(arguments);
        case MARK -> prepareMark(arguments, true);
        case UNMARK -> prepareMark(arguments, false);
        case FINDDATE -> prepareFindDate(arguments);
        case FIND -> prepareFind(arguments);
        case TODO, DEADLINE, EVENT -> prepareAdd(arguments, type);
        default -> throw new HowlyException("I'm sorry, I don't know what that means.");
        };
    }

    private static Command prepareAdd(String args, CommandType type) throws HowlyException {
        if (args.isEmpty()) {
            throw new HowlyException("The description of a " + type + " cannot be empty.");
        }
        return new AddCommand(args, type);
    }

    private static Command prepareExit(String args) throws HowlyException {
        if (!args.isEmpty()) {
            throw new HowlyException("The 'bye' command should not have arguments.");
        }
        return new ExitCommand();
    }

    private static Command prepareDelete(String args) throws HowlyException {
        if (args.isEmpty()) {
            throw new HowlyException("The 'delete' command requires an index.");
        }
        try {
            int index = Integer.parseInt(args) - 1;
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new HowlyException("Please provide a valid numeric index.");
        }
    }

    private static Command prepareList(String args) throws HowlyException {
        if (!args.isEmpty()) {
            throw new HowlyException("The 'list' command should not have arguments.");
        }
        return new ListCommand();
    }

    private static Command prepareMark(String args, boolean isMark) throws HowlyException {
        if (args.isEmpty()) {
            throw new HowlyException("Please specify a task index to mark/unmark.");
        }
        try {
            int index = Integer.parseInt(args) - 1;
            return new MarkCommand(index, isMark);
        } catch (NumberFormatException e) {
            throw new HowlyException("The task index must be a valid number.");
        }
    }

    private static Command prepareFind(String args) throws HowlyException {
        if (args.isEmpty()) {
            throw new HowlyException("The 'find' command requires a keyword.");
        }
        return new FindCommand(args);
    }

    private static Command prepareFindDate(String args) throws HowlyException {
        if (args.isEmpty()) {
            throw new HowlyException("The 'finddate' command requires a date. Eg: finddate 2026-01-23");
        }
        LocalDate date = parseDate(args.trim());
        return new FindDateCommand(date);
    }

    /**
     * Extracts the description for a ToDo task from the user input.
     * Non-trivial as it uses regex to case-insensitively strip the command word.
     *
     * @param args The raw input string (e.g., "todo read book").
     * @return The trimmed description of the task.
     * @throws HowlyException If the description field is empty.
     */
    public static String parseTodo(String args) throws HowlyException {
        if (args.trim().isEmpty()) {
            throw new HowlyException("You have to specify the task after 'todo'.");
        }
        return args.trim();
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
            throw new HowlyException("An event must include /from and /to. "
                    + "Eg: event meeting /from 2025-10-15 /to 2025-10-16");
        }

        // Split by /from and /to. Using regex to handle both.
        String[] parts = content.split("/from|/to");
        if (parts.length < 3 || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new HowlyException("Event description, /from, and /to fields cannot be empty.");
        }
        return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
    }

    /**
     * Parses a date string from the user input into a {@code LocalDate} object.
     *
     * @param dateString The command string containing the date (e.g., "finddate 2026-01-23").
     * @return The parsed {@code LocalDate}.
     * @throws HowlyException If the date format is incorrect or missing.
     */
    public static LocalDate parseDate(String dateString) throws HowlyException {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new HowlyException("Please specify a date in yyyy-mm-dd format.");
        }
    }
}
