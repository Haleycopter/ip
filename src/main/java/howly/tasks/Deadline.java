package howly.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a specific deadline.
 * A <code>Deadline</code> object contains a description and a date by which
 * the task must be completed.
 */
public class Deadline extends Task {
    private final LocalDate by;

    public Deadline(String description, String by) {
        super(description);
        // Assume input dates in the format yyyy-mm-dd
        this.by = LocalDate.parse(by);
    }

    /**
     * Returns the machine-readable data format of the deadline task for storage.
     * The format used is "D | [status] | [description] | [date]".
     * * @return A string formatted for saving to a file.
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }

    /**
     * Checks if the deadline of the task occurs on a specific date.
     * * @param date The date to compare against the deadline.
     * @return <code>true</code> if the deadline matches the specified date, <code>false</code> otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate date) {
        return this.by.equals(date);
    }

    /**
     * Returns a string representation of the deadline task, including its type icon,
     * status icon, description, and formatted deadline date.
     * * @return A user-friendly string representation of the deadline.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " +
                by.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }
}
