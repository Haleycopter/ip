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
