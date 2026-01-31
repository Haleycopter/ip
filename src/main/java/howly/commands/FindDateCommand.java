package howly.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.parser.Parser;
import howly.storage.Storage;
import howly.tasks.Task;
import howly.ui.Ui;

/**
 * Represents a command to search for and display tasks that occur on a specific date.
 * This class handles date parsing and iterates through the task list to find matches.
 */
public class FindDateCommand extends Command {
    private final String input;

    public FindDateCommand(String input) {
        this.input = input;
    }

    /**
     * Executes the search command by parsing the target date and printing all tasks
     * that match that date.
     * <p>
     * This method performs strict argument validation and uses the {@link Task#isOnDate(LocalDate)}
     * method of each task to determine if it should be displayed.
     * </p>
     *
     * @param tasks The list of tasks to search through.
     * @param ui The user interface used to display the search results.
     * @param storage The storage system (unused by this command).
     * @throws HowlyException If the input format is invalid or the date cannot be parsed.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        String[] parts = input.split(" ");
        if (parts.length > 2) {
            throw new HowlyException("The 'finddate' command should only contain a date. Eg: finddate 2026-01-23");
        }
        LocalDate searchDate = Parser.parseDate(input);
        ui.showLine();
        System.out.println(" Here are the tasks occurring on "
                + searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isOnDate(searchDate)) {
                count++;
                System.out.println(" " + count + "." + tasks.get(i));
            }
        }
        if (count == 0) {
            System.out.println(" No tasks found for this date.");
        }
        ui.showLine();
    }
}
