package bobby.commands;

import bobby.parser.Message;
import bobby.task.Task;
import bobby.task.TaskList;

/**
 * Represents the command to mark a task as done.
 */
public class MarkCommand extends Command {
    private final int taskIndex;

    public MarkCommand(int index) {
        this.taskIndex = index;
    }

    @Override
    protected String performAction(TaskList taskList) {
        Task task = taskList.getTask(taskIndex);
        task.markDone();
        return String.format(Message.MESSAGE_MARK_FORMAT, task);
    }
}
