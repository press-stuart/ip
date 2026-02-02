package bobby.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline.
 */
public class Deadline extends Task {
    protected static final DateTimeFormatter PRINT_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy");
    protected static final DateTimeFormatter COMMAND_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    protected LocalDate by;

    public Deadline(String description, boolean isDone, LocalDate by) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String toCommand() {
        String b = by.format(COMMAND_FORMAT);
        return String.format("deadline %s /by %s", description, b)
                + (isDone ? " /done" : "");
    }

    @Override
    public String toString() {
        String b = by.format(PRINT_FORMAT);
        return "[D]" + super.toString() + " (by: " + b + ")";
    }
}

