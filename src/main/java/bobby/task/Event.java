package bobby.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event.
 */
public class Event extends Task {
    protected static final DateTimeFormatter PRINT_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy");
    protected static final DateTimeFormatter COMMAND_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected LocalDate from;
    protected LocalDate to;
    
    public Event(String description, boolean isDone, LocalDate from, LocalDate to) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toCommand() {
        String f = from.format(COMMAND_FORMAT);
        String t = to.format(COMMAND_FORMAT);
        return String.format("event %s /from %s /to %s", description, f, t)
                + (isDone ? " /done" : "");
    }

    @Override
    public String toString() {
        String f = from.format(PRINT_FORMAT);
        String t = to.format(PRINT_FORMAT);
        return String.format("[E]%s (from: %s to: %s)", super.toString(), f, t);
    }
}
