package howly.tasks;

/**
 * Represents a basic task without any specific date or time constraints.
 * A <code>ToDo</code> object corresponds to a task that only requires a description.
 */
public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns the machine-readable data format of the todo task for storage.
     * The format used is "T | [status] | [description]".
     *
     * @return A string formatted for saving to a file.
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }

    /**
     * Returns a string representation of the todo task, including its type icon,
     * status icon, and description.
     *
     * @return A user-friendly string representation of the todo.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}