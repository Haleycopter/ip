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
    private final String arguments;
    private final CommandType type;

    /**
     * Constructs an AddCommand with the specified user input and task type.
     *
     * @param arguments The full raw command string provided by the user.
     * @param type The type of task to be added (TODO, DEADLINE, or EVENT).
     */
    public AddCommand(String arguments, CommandType type) {
        this.arguments = arguments;
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
            case TODO -> new ToDo(Parser.parseTodo(arguments)); // Logic moved to help methods
            case DEADLINE -> {
                String[] dParts = Parser.parseDeadline(arguments);
                yield new Deadline(dParts[0], dParts[1]);
            }
            case EVENT -> {
                String[] eParts = Parser.parseEvent(arguments);
                yield new Event(eParts[0], eParts[1], eParts[2]);
            }
            default -> throw new HowlyException("Unknown task type.");
            };

            tasks.add(newTask);
            storage.save(tasks.getTasks());
            return ui.formatMessages(
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            );
        } catch (java.time.format.DateTimeParseException e) {
            throw new HowlyException("Invalid date format. Use yyyy-mm-dd.");
        }
    }
}
