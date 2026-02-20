package bobby.commands;

import java.time.LocalDate;

import bobby.parser.Message;
import bobby.task.DoAfterTask;
import bobby.task.TaskList;

/**
 * Represents a command to add a DoAfter task.
 */
public class DoAfterCommand extends Command {
    private final String description;
    private final boolean isDone;
    private final LocalDate afterDate;

    /**
     * Creates a DoAfterCommand with the given description, completion status, and do-after date.
     *
     * @param description Description of the DoAfterTask.
     * @param isDone Completion status of the DoAfterTask.
     * @param afterDate Date after which the DoAfterTask should be done.
     */
    public DoAfterCommand(String description, boolean isDone, LocalDate afterDate) {
        this.description = description;
        this.isDone = isDone;
        this.afterDate = afterDate;
    }

    @Override
    protected String performAction(TaskList taskList) {
        DoAfterTask doAfter = new DoAfterTask(description, isDone, afterDate);
        taskList.addTask(doAfter);
        return String.format(Message.MESSAGE_ADD_FORMAT, doAfter, taskList.getSize());
    }
}
