/**
 * Represents a Deadline.
 */
public class Deadline extends Task {
    /** Deadline time. */
    protected String by;
    
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toCommand() {
        return String.format("deadline %s /by %s", description, by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

