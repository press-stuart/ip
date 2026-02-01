/**
 * Represents a Deadline.
 */
public class Deadline extends Task {
    /** Deadline time. */
    protected String by;

    public Deadline(String description, boolean isDone, String by) {
        super(description, isDone);
        this.by = by;
    }

    @Override
    public String toCommand() {
        return String.format("deadline %s /by %s", description, by)
                + (isDone ? " /done" : "");
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

