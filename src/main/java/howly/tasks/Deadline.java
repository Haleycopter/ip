import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Deadline extends Task {
    private final LocalDate by;

    public Deadline(String description, String by) {
        super(description);
        // Assume input dates in the format yyyy-mm-dd
        this.by = LocalDate.parse(by);
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        return this.by.equals(date);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " +
                by.format(DateTimeFormatter.ofPattern("MMM d yyyy")) +")";
    }
}
