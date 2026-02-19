package bobby.commands;

import javafx.application.Platform;

import bobby.parser.Message;
import bobby.task.TaskList;

/**
 * Represents the command to exit the program and initiate clean-up.
 */
public class ByeCommand extends Command {
    @Override
    protected String performAction(TaskList taskList) {
        Platform.exit();
        return Message.MESSAGE_EXIT;
    }
}
