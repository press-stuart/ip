package bobby.commands;

import bobby.exception.BobbyException;
import bobby.parser.Message;
import bobby.task.Task;
import bobby.task.TaskList;

/**
 * Represents the command to mark a task as not done.
 */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates an UnmarkCommand to mark the task at the specified index as not done.
     *
     * @param index Index of the task to be marked as not done (1-based indexing).
     */
    public UnmarkCommand(int index) {
        this.taskIndex = index;
    }

    @Override
    protected String performAction(TaskList taskList) throws BobbyException {
        Task task = taskList.getTask(taskIndex);
        task.unmarkDone();
        return String.format(Message.MESSAGE_UNMARK_FORMAT, task);
    }
}
