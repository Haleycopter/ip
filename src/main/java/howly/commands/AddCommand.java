package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.parser.Parser;
import howly.storage.Storage;
import howly.tasks.Deadline;
import howly.tasks.Event;
import howly.tasks.Task;
import howly.tasks.ToDo;
import howly.ui.Ui;

/**
 * Represents a command to add a task to the task list.
 * This class handles the creation of Todo, Deadline, and Event tasks based on user input.
 */
public class AddCommand extends Command {
    private final String input;
    private final CommandType type;

    /**
     * Constructs an AddCommand with the specified user input and task type.
     *
     * @param input The full raw command string provided by the user.
     * @param type The type of task to be added (TODO, DEADLINE, or EVENT).
     */
    public AddCommand(String input, CommandType type) {
        this.input = input;
        this.type = type;
    }

    /**
     * Executes the add command by parsing the input, creating the appropriate task,
     * adding it to the task list, and saving the updated list to storage.
     *
     * @param tasks The list of tasks where the new task will be added.
     * @param ui The user interface used to display feedback to the user.
     * @param storage The storage handler used to persist the updated task list.
     * @throws HowlyException If the user input is invalid or if date parsing fails.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        try {
            Task newTask = switch (type) {
            case TODO -> new ToDo(Parser.parseTodo(input));
            case DEADLINE -> {
                String[] dParts = Parser.parseDeadline(input);
                yield new Deadline(dParts[0], dParts[1]);
            }
            case EVENT -> {
                String[] eParts = Parser.parseEvent(input);
                yield new Event(eParts[0], eParts[1], eParts[2]);
            }
            default -> throw new HowlyException("Please specify an appropriate command: todo, deadline, event");
            };

            tasks.add(newTask);
            storage.save(tasks.getTasks());
            return ui.formatMessages(
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            );
        } catch (java.time.format.DateTimeParseException e) {
            throw new HowlyException("Please use the date format yyyy-mm-dd (Eg: 2025-12-31).");
        }
    }
}
