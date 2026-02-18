package bobby;

import bobby.commands.Command;
import bobby.parser.Message;
import bobby.parser.Parser;
import bobby.storage.Storage;
import bobby.task.TaskList;
import bobby.ui.Ui;

/**
 * Main class for Bobby the chatbot. Drives the initialization, user command loop
 * and cleanup on exit.
 */
public class Bobby {
    /** List of user tasks. */
    private TaskList taskList;

    /** Manager for the file storing the list of tasks. */
    private Storage storage;

    /** User interface. */
    private Ui ui;

    /**
     * Initializes Bobby.
     */
    public Bobby() {
        ui = new Ui();
        storage = new Storage();
    }

    /**
     * Loads tasks from storage, initializing an empty task list on failure.
     *
     * @return A message indicating the result of the loading process, or null if loading was successful.
     */
    public String loadTasks() {
        try {
            taskList = storage.load();
        } catch (Exception e) {
            taskList = new TaskList();
            return e.getMessage();
        }

        return null;
    }

    /**
     * Gets the introductory message displayed at startup.
     */
    public String getIntroMessage() {
        return Message.MESSAGE_INTRO;
    }

    /**
     * Executes a user command and returns the response.
     *
     * @param input The user command input.
     * @return The response after executing the command.
     */
    public String executeCommandAndGetResponse(String input) {
        String response;

        try {
            Command command = Parser.parse(input);
            response = command.executeAndRespond(taskList);
        } catch (Exception e) {
            response = e.getMessage();
        }

        return response;
    }

    /**
     * Attempts to save tasks to storage and closes the UI.
     */
    public void cleanUpAfterExit() {
        try {
            storage.save(taskList);
        } catch (Exception e) {
            ui.printMessage(e.getMessage());
        }
        
        ui.close();
    }
}
