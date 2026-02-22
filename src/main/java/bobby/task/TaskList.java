package bobby.task;

import java.util.ArrayList;
import java.util.List;

import bobby.exception.BobbyException;
import bobby.parser.Message;

/**
 * A container of user tasks.
 */
public class TaskList {
    private List<Task> tasks;

    /**
     * Initialises an empty list of tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes the task at the given index from the list.
     *
     * @param index Index of the task, with numbering beginning from 1.
     * @return The task that is deleted.
     * @throws BobbyException If the index is out of range.
     */
    public Task deleteTask(int index) throws BobbyException {
        throwExceptionIfIndexOutOfRange(index);
        return tasks.remove(index - 1);
    }

    /**
     * Finds all tasks whose descriptions contain the given keyword.
     *
     * @param keyword Keyword to search for.
     * @return A TaskList containing all found tasks.
     */
    public TaskList findTasks(String keyword) {
        TaskList foundTasks = new TaskList();

        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                foundTasks.addTask(task);
            }
        }

        return foundTasks;
    }

    /**
     * Gets the task at the given index from the list.
     *
     * @param index Index of the task, with numbering beginning from 1.
     * @return The task at the given index.
     * @throws BobbyException If the index is out of range.
     */
    public Task getTask(int index) throws BobbyException {
        throwExceptionIfIndexOutOfRange(index);
        return tasks.get(index - 1);
    }

    /**
     * Gets a List containing all tasks.
     */
    public List<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Gets the number of tasks in the list.
     *
     * @return The number of tasks in the list.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Outputs the contained tasks, one on each line, with numbering beginning from 1.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                sb.append("\n");
            }

            Task task = this.tasks.get(i);
            String line = String.format("%d.%s", i + 1, task.toString());
            sb.append(line);
        }

        return sb.toString();
    }

    private void throwExceptionIfIndexOutOfRange(int index) throws BobbyException {
        if (index < 1 || index > getSize()) {
            throw new BobbyException(String.format(Message.MESSAGE_INVALID_TASK_INDEX));
        }
    }
}
