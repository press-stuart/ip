package bobby.commands;

import bobby.parser.Message;
import bobby.task.Task;
import bobby.task.TaskList;

/**
 * Represents the command to delete a task from the list.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a DeleteCommand with the index of the task to be deleted.
     *
     * @param index Index of the task to be deleted (1-based indexing).
     */
    public DeleteCommand(int index) {
        this.taskIndex = index;
    }

    @Override
    protected String performAction(TaskList taskList) {
        Task deletedTask = taskList.deleteTask(taskIndex);
        return String.format(
                Message.MESSAGE_DELETE_FORMAT,
                deletedTask.toString(), taskList.getSize());
    }
}
