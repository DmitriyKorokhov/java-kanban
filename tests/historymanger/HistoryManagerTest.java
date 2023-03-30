package historymanger;

import model.Task;
import org.junit.jupiter.api.Test;
import service.exception.InvalidValueException;
import service.exception.TimeIntersectionException;
import service.managers.HistoryManager;
import service.managers.Managers;
import service.managers.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TaskManager taskManager = Managers.getDefaultFileBackedTasksManager();

    @Test
    public void addTaskToTheHistory() throws InvalidValueException {
        Task task = new Task("Task", "Test description");
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "История пустая.");
    }

    @Test
    public void emptyHistory() {
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> historyManager.getHistory());
        assertEquals("Список пустой", ex.getMessage());
    }

    @Test
    public void duplicationOfTasks() throws InvalidValueException {
        Task task = new Task("Task", "Test description");
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    public void deleteFromTheHistoryAtTheBeginning() throws TimeIntersectionException, InvalidValueException {
        Task taskOne = new Task("TaskOne", "Test description - 1");
        taskManager.saveTask(taskOne);
        final int taskOneId = taskOne.getTaskId();
        historyManager.add(taskOne);
        Task taskTwo = new Task("taskTwo", "Test description - 2");
        taskManager.saveTask(taskTwo);
        historyManager.add(taskTwo);
        Task taskThree = new Task("taskThree", "Test description - 3");
        taskManager.saveTask(taskThree);
        historyManager.add(taskThree);
        historyManager.remove(taskOneId);
        final List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(taskOne), "Задача не удалена");
        assertEquals(2, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    public void deleteFromTheHistoryAtTheMiddle() throws TimeIntersectionException, InvalidValueException {
        Task taskOne = new Task("TaskOne", "Test description - 1");
        taskManager.saveTask(taskOne);
        historyManager.add(taskOne);
        Task taskTwo = new Task("taskTwo", "Test description - 2");
        taskManager.saveTask(taskTwo);
        final int taskTwoId = taskTwo.getTaskId();
        historyManager.add(taskTwo);
        Task taskThree = new Task("taskThree", "Test description - 3");
        taskManager.saveTask(taskThree);
        historyManager.add(taskThree);
        historyManager.remove(taskTwoId);
        final List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(taskTwo), "Задача не удалена");
        assertEquals(2, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    public void deleteFromTheHistoryAtTheEnd() throws TimeIntersectionException, InvalidValueException {
        Task taskOne = new Task("TaskOne", "Test description - 1");
        taskManager.saveTask(taskOne);
        historyManager.add(taskOne);
        Task taskTwo = new Task("taskTwo", "Test description - 2");
        taskManager.saveTask(taskTwo);
        historyManager.add(taskTwo);
        Task taskThree = new Task("taskThree", "Test description - 3");
        taskManager.saveTask(taskThree);
        final int taskThreeId = taskThree.getTaskId();
        historyManager.add(taskThree);
        historyManager.remove(taskThreeId);
        final List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(taskThree), "Задача не удалена");
        assertEquals(2, history.size(), "Неверное количество элементов в истории.");
    }
}