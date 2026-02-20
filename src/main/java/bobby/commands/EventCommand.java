package bobby.commands;

import java.time.LocalDate;

import bobby.parser.Message;
import bobby.task.EventTask;
import bobby.task.TaskList;

/**
 * Represents a command to add an Event task.
 */
public class EventCommand extends Command {
    private final String description;
    private final boolean isDone;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    /**
     * Creates an EventCommand with the given description, completion status, and event dates.
     *
     * @param description Description of the event task.
     * @param isDone Completion status of the event task.
     * @param fromDate Start date of the event.
     * @param toDate End date of the event.
     */
    public EventCommand(String description, boolean isDone, LocalDate fromDate, LocalDate toDate) {
        this.description = description;
        this.isDone = isDone;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected String performAction(TaskList taskList) {
        EventTask event = new EventTask(description, isDone, fromDate, toDate);
        taskList.addTask(event);
        return String.format(Message.MESSAGE_ADD_FORMAT, event, taskList.getSize());
    }
}
