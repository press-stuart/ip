package bobby.commands;

import bobby.parser.Message;
import bobby.task.TaskList;

/**
 * Represents the command to list all tasks in the task list.
 */
public class ListCommand extends Command {
    @Override
    protected String performAction(TaskList taskList) {
        if (taskList.isEmpty()) {
            return Message.MESSAGE_LIST_EMPTY;
        }
        return String.format(Message.MESSAGE_LIST_FORMAT, taskList);
    }
}
