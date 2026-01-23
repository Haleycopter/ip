package howly.tasks;

import java.time.LocalDate;

/**
 * Represents a generic task in the Howly application.
 * This class serves as a base for specific task types like Todo, Deadline, and Event,
 * providing common functionality for descriptions and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not yet completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns an icon representing the completion status of the task.
     * "X" indicates completed, while a space indicates not done.
     *
     * @return A status icon string.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the machine-readable format of the task's base data for storage.
     * The format used is "[status] | [description]", where 1 is done and 0 is not done.
     *
     * @return A string representation of the task's status and description.
     */
    public String toFileFormat() {
        return (isDone? "1" : "0") + " | " + description;
    }

    /**
     * Checks if this task occurs on a specific date.
     * The base implementation always returns {@code false};
     * subclasses should override this if they have date-related properties.
     *
     * @param date The date to check against.
     * @return {@code false} by default for generic tasks.
     */
    public boolean isOnDate(LocalDate date) {
        return false;
    }

    /**
     * Returns a string representation of the task, showing its status icon
     * and description.
     *
     * @return A user-friendly string representation of the task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}
