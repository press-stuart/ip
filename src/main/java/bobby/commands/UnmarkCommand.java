package bobby.commands;

import bobby.parser.Message;
import bobby.task.Task;
import bobby.task.TaskList;

/**
 * Represents the command to mark a task as not done.
 */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    public UnmarkCommand(int index) {
        this.taskIndex = index;
    }

    @Override
    protected String performAction(TaskList taskList) {
        Task task = taskList.getTask(taskIndex);
        task.unmarkDone();
        return String.format(Message.MESSAGE_UNMARK_FORMAT, task);
    }
}
