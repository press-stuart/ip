package bobby.task;

/**
 * Represents a Task.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task.
     *
     * @param description Description of the Task.
     * @param isDone Completion status of the Task.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public Task(String description) {
        this(description, false);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void unmarkDone() {
        isDone = false;
    }

    /**
     * Checks if the task is done.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the task's status icon depending on whether the task is done.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns a user command that produces this task.
     */
    public abstract String toCommand();

    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }
}
