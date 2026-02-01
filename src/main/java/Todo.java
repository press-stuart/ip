/**
 * Represents a Todo.
 */
public class Todo extends Task {
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toCommand() {
        return String.format("todo %s", description)
                + (isDone ? " /done" : "");
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
