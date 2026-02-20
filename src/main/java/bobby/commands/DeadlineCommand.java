package bobby.commands;

import java.time.LocalDate;

import bobby.parser.Message;
import bobby.task.DeadlineTask;
import bobby.task.TaskList;

/**
 * Represents a command to add a Deadline task.
 */
public class DeadlineCommand extends Command {
    private final String description;
    private final boolean isDone;
    private final LocalDate byDate;

    /**
     * Creates a DeadlineCommand with the given description, completion status, and deadline date.
     *
     * @param description Description of the Deadline task.
     * @param isDone Completion status of the Deadline task.
     * @param byDate Deadline date of the Deadline task.
     */
    public DeadlineCommand(String description, boolean isDone, LocalDate byDate) {
        this.description = description;
        this.isDone = isDone;
        this.byDate = byDate;
    }

    @Override
    protected String performAction(TaskList taskList) {
        DeadlineTask deadline = new DeadlineTask(description, isDone, byDate);
        taskList.addTask(deadline);
        return String.format(Message.MESSAGE_ADD_FORMAT, deadline, taskList.getSize());
    }
}
