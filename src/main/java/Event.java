import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Event extends Task {
    private final LocalDate from;
    private final LocalDate to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDate.parse(from);
        this.to = LocalDate.parse(to);
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        // Returns true if the search date is the start date, end date, or anywhere in between
        return (date.isEqual(from) || date.isEqual(to)) || (date.isAfter(from) && date.isBefore(to));
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d yyyy");
        return "[E]" + super.toString() + " (from: " + from.format(fmt) + " to: " + to.format(fmt) +")";
    }
}
