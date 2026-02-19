package bobby.commands;

import bobby.exception.BobbyException;
import bobby.parser.Message;
import bobby.task.TaskList;
import bobby.task.TodoTask;

/**
 * Represents a command to add a Todo task.
 */
public class TodoCommand extends Command {
    private final String description;
    private final boolean isDone;

    public TodoCommand(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    @Override
    protected String performAction(TaskList taskList) throws BobbyException {
        TodoTask todo = new TodoTask(description, isDone);
        taskList.addTask(todo);
        return String.format(Message.MESSAGE_ADD_FORMAT, todo, taskList.getSize());
    }
}
