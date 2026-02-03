package bobby.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import bobby.task.Deadline;

public class DeadlineTest {
    @Test
    public void toCommand_deadlineIsDone_success() {
        LocalDate date = LocalDate.of(2026, 4, 30);
        Deadline deadline = new Deadline("return book", true, date);
        String actualCommand = deadline.toCommand();
        String expectedCommand = "deadline return book /by 2026-04-30 /done";
        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void toCommand_deadlineIsNotDone_success() {
        LocalDate date = LocalDate.of(2026, 12, 30);
        Deadline deadline = new Deadline("eat book", false, date);
        String actualCommand = deadline.toCommand();
        String expectedCommand = "deadline eat book /by 2026-12-30";
        assertEquals(expectedCommand, actualCommand);
    }
}
