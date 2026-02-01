/**
 * Represents an Event.
 */
public class Event extends Task {
    /** Start time of the event. */
    protected String from;

    /** End time of the event. */
    protected String to;
    
    public Event(String description, boolean isDone, String from, String to) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toCommand() {
        return String.format("event %s /from %s /to %s", description, from, to)
                + (isDone ? " /done" : "");
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), from, to);
    }
}
