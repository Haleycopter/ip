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

    /**
     * Constructs a {@code FindCommand} with the specified search keyword.
     *
     * @param keyword The string to search for within the task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by filtering the task list for matches.
     * The search is case-insensitive and returns an indexed list of matching tasks.
     * @param tasks The list of tasks to be operated on.
     * @param ui The user interface used to interact with the user.
     * @param storage The storage system used to save or load task data.
     * @return A string containing the indexed list of matching tasks or a "not found" message.
     * @throws HowlyException If an error occurs during execution.
     */
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
