package bobby.commands;

import bobby.exception.BobbyException;
import bobby.task.TaskList;

/**
 * Represents a command with the ability to be executed.
 */
public abstract class Command {
    /**
     * Performs the action associated with this command and returns the response message.
     *
     * @param taskList The TaskList to perform the action on.
     * @return The response message after performing the action.
     * @throws BobbyException If an error occurs during the action.
     */
    protected abstract String performAction(TaskList taskList) throws BobbyException;

    /**
     * Executes the command, performing the action associated with it.
     *
     * @param taskList The TaskList to perform the action on.
     * @throws BobbyException If an error occurs during command execution.
     */
    public final void execute(TaskList taskList) throws BobbyException {
        performAction(taskList);
    }

    /**
     * Executes the command and returns the response message.
     *
     * @param taskList The TaskList to perform the action on.
     * @return The response message after executing the command.
     * @throws BobbyException If an error occurs during command execution.
     */
    public final String executeAndRespond(TaskList taskList) throws BobbyException {
        return performAction(taskList);
    }
}
