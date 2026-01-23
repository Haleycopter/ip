package howly.common;

import howly.tasks.Task;
import java.util.ArrayList;
/**
 * Encapsulates the list of tasks and provides operations to manipulate the list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list using the specified task.
     * @param t The task to be added to the task list.
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Deletes a task from the list at the specified index.
     * @param index The zero-based index of the task to be deleted.
     * @return The task that was removed from the list.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     * @param index The zero-based index of the task to retrieve.
     * @return The task at the given index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * @return The number of tasks in the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * @return The list of tasks in the task list.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
