package bobby.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a DoAfter task.
 */
public class DoAfterTask extends Task {
    protected static final DateTimeFormatter PRINT_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy");
    protected static final DateTimeFormatter COMMAND_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected LocalDate after;

    /**
     * Constructs a DoAfterTask.
     *
     * @param description Description of the DoAfterTask.
     * @param isDone Completion status of the DoAfterTask.
     * @param after Date after which the DoAfterTask should be done.
     */
    public DoAfterTask(String description, boolean isDone, LocalDate after) {
        super(description, isDone);
        this.after = after;
    }

    @Override
    public String toCommand() {
        String b = after.format(COMMAND_FORMAT);
        return String.format("doafter %s /after %s", description, b)
                + (isDone ? " /done" : "");
    }

    @Override
    public String toString() {
        String b = after.format(PRINT_FORMAT);
        return "[A]" + super.toString() + " (after: " + b + ")";
    }
}

