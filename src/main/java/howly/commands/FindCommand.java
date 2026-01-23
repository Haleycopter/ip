package howly.commands;

import howly.tasks.Task;
import howly.common.HowlyException;
import howly.common.TaskList;
import howly.ui.Ui;
import howly.storage.Storage;

/**
 * Represents a command to search for tasks that contain a specific keyword.
 * This class filters the task list and displays matches to the user.
 */
public class FindCommand extends Command {
    private final String input;

    public FindCommand(String input) {
        this.input = input;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new HowlyException("The 'find' command requires a keyword. Eg: find book");
        }
        String keyword = parts[1].trim();

        ui.showLine();
        System.out.println(" Here are the matching tasks in your list:");

        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            // Check if the task description contains the keyword
            if (task.toString().contains(keyword)) {
                count++;
                System.out.println(" " + count + "." + task);
            }
        }

        if (count == 0) {
            System.out.println(" No matching tasks found.");
        }
        ui.showLine();
    }
}