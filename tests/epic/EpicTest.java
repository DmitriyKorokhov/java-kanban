package epic;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import service.exception.InvalidValueException;
import service.managers.Managers;
import service.managers.TaskManager;
import service.exception.TimeIntersectionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private final TaskManager taskManager = Managers.getDefaultFileBackedTasksManager();
    private final Epic epic = new Epic("Testing the Epic", "Test description");
    private Subtask subtask1 = new Subtask("Subtask1", "Test description - 1", epic.getTaskId());
    private Subtask subtask2 = new Subtask("Subtask2", "Test description - 2", epic.getTaskId());

    @Test
    public void returnNEWWhenTheSubtaskListIsEmpty() {
        taskManager.saveEpic(epic);
        assertEquals(Status.NEW, epic.getEpicStatus());
    }

    @Test
    public void returnNEWWhenAllSubtaskAreNew() throws TimeIntersectionException {
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(subtask1, epic.getTaskId());
        taskManager.saveSubtask(subtask2, epic.getTaskId());
        assertEquals(Status.NEW, epic.getEpicStatus());
    }

    @Test
    public void returnDONEWhenAllSubtaskAreDone() throws TimeIntersectionException, InvalidValueException {
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(subtask1, epic.getTaskId());
        subtask1 = new Subtask(1, "Subtask1", "Test description - 1 - 1");
        taskManager.updateSubtask(subtask1);
        subtask1 = new Subtask(1, "Subtask1", "Test description - 1 - 2");
        taskManager.updateSubtask(subtask1);
        taskManager.saveSubtask(subtask2, epic.getTaskId());
        subtask2 = new Subtask(2, "Subtask2", "Test description - 2 - 1");
        taskManager.updateSubtask(subtask2);
        subtask2 = new Subtask(2, "Subtask2", "Test description - 2 - 2");
        taskManager.updateSubtask(subtask2);
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.DONE, epic.getEpicStatus());
    }

}