package bobby.commands;

import bobby.parser.Message;
import bobby.task.TaskList;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    protected String performAction(TaskList taskList) {
        TaskList foundTasks = taskList.findTasks(keyword);
        return String.format(Message.MESSAGE_FIND_FORMAT, foundTasks);
    }
}
