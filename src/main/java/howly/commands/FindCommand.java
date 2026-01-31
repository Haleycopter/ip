package howly.commands;

import howly.common.HowlyException;
import howly.common.TaskList;
import howly.storage.Storage;
import howly.tasks.Task;
import howly.ui.Ui;

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
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        String[] parts = input.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new HowlyException("The 'find' command requires a keyword. Eg: find book");
        }
        String keyword = parts[1].trim();
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");

        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.toString().contains(keyword)) {
                count++;
                sb.append(count).append(".").append(task).append("\n");
            }
        }

        return count == 0 ? "No matching tasks found." : sb.toString().trim();
    }
}
