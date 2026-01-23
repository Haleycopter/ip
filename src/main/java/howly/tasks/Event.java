package howly.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs within a specific time frame.
 * An <code>Event</code> object contains a description, a start date,
 * and an end date.
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDate.parse(from);
        this.to = LocalDate.parse(to);
    }

    /**
     * Returns the machine-readable data format of the event task for storage.
     * The format used is "E | [status] | [description] | [from] | [to]".
     * * @return A string formatted for saving to a file.
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }

    /**
     * Checks if the event occurs on or spans across a specific date.
     * * @param date The date to check against the event's duration.
     * @return <code>true</code> if the date is the start, end, or between the range;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate date) {
        // Returns true if the search date is the start date, end date, or anywhere in between
        return (date.isEqual(from) || date.isEqual(to)) || (date.isAfter(from) && date.isBefore(to));
    }

    /**
     * Returns a string representation of the event task, including its type icon,
     * status icon, description, and formatted date range.
     * * @return A user-friendly string representation of the event.
     */
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d yyyy");
        return "[E]" + super.toString() + " (from: " + from.format(fmt) + " to: " + to.format(fmt) +")";
    }
}
