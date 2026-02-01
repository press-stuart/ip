public class Deadline extends Task {

    protected String by;
    
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toCommand() {
        return String.format("deadline %s /by %s", this.description, this.by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

