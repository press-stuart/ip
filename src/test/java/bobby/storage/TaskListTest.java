package bobby.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import bobby.task.Task;
import bobby.task.TaskList;

public class TaskListTest {
    @Test
    public void toString_emptyTaskList_success() {
        TaskList taskList = new TaskList();
        assertEquals("", taskList.toString());
    }

    @Test
    public void toString_taskListWithTwoTodos_success() {
        TaskList taskList = new TaskList();
        taskList.addTask(new TodoStubOne());
        taskList.addTask(new TodoStubTwo());
        String actualString = taskList.toString();
        String expectedString = """
                1.[T][ ] todo one
                2.[T][X] todo two""";
        assertEquals(expectedString, actualString);
    }

    private static class TodoStubOne extends Task {
        public TodoStubOne() {
            super("todo one", false);
        }

        @Override
        public String toCommand() {
            return "todo todo one";
        }

        @Override
        public String toString() {
            return "[T][ ] todo one";
        }
    }

    private static class TodoStubTwo extends Task {
        public TodoStubTwo() {
            super("todo two", true);
        }

        @Override
        public String toCommand() {
            return "todo todo two /done";
        }

        @Override
        public String toString() {
            return "[T][X] todo two";
        }
    }
}
