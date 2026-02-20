package bobby.commands;

import bobby.parser.Message;
import bobby.task.TaskList;

/**
 * Represents the command to find tasks containing a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Creates a FindCommand with the specified keyword.
     *
     * @param keyword The keyword to search for in the list of tasks.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    protected String performAction(TaskList taskList) {
        TaskList foundTasks = taskList.findTasks(keyword);
        return String.format(Message.MESSAGE_FIND_FORMAT, foundTasks);
    }
}
