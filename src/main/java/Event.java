/**
 * Represents an Event.
 */
public class Event extends Task {
    /** Start time of the event. */
    protected String from;

    /** End time of the event. */
    protected String to;
    
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toCommand() {
        return String.format("event %s /from %s /to %s", description, from, to);
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), from, to);
    }
}
