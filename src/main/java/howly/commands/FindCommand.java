package howly.commands;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws HowlyException {
        // Legacy parsing needed because this branch doesn't have SLAP refactor yet
        String[] parts = keyword.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new HowlyException("The 'find' command requires a keyword.");
        }
        String keyword = parts[1].trim();

        // Stream logic
        List<Task> matchingTasks = tasks.getTasks().stream()
                .filter(t -> t.toString().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

        if (matchingTasks.isEmpty()) {
            return "No matching tasks found for: " + keyword;
        }

        String results = IntStream.range(0, matchingTasks.size())
                .mapToObj(i -> (i + 1) + "." + matchingTasks.get(i))
                .collect(Collectors.joining("\n"));

        return "Here are the matching tasks in your list:\n" + results;
    }
}
