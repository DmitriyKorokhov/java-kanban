package epic;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;
import service.InvalidValueException;
import service.Managers;
import service.TaskManager;
import service.TimeIntersectionException;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private final TaskManager taskManager = Managers.getDefault();
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
        taskManager.saveSubtask(subtask1,epic, epic.getEpicListId());
        taskManager.saveSubtask(subtask2,epic, epic.getEpicListId());
        assertEquals(Status.NEW, epic.getEpicStatus());
    }

    @Test
    public void returnDONEWhenAllSubtaskAreDone() throws TimeIntersectionException, InvalidValueException {
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(subtask1,epic, epic.getEpicListId());
        subtask1 = new Subtask("Subtask1", "Test description - 1 - 1", epic.getTaskId());
        taskManager.updateSubtask(subtask1);
        subtask1 = new Subtask("Subtask1", "Test description - 1 - 2", epic.getTaskId());
        taskManager.updateSubtask(subtask1);
        taskManager.saveSubtask(subtask2,epic, epic.getEpicListId());
        subtask2 = new Subtask("Subtask2", "Test description - 2 - 1", epic.getTaskId());
        taskManager.updateSubtask(subtask2);
        subtask2 = new Subtask("Subtask2", "Test description - 2 - 2", epic.getTaskId());
        taskManager.updateSubtask(subtask2);
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.DONE, epic.getEpicStatus());
    }

    @Test
    public void returnIN_PROGRESSWhenAllSubtaskAreDoneAndNew() throws TimeIntersectionException, InvalidValueException {
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(subtask1,epic, epic.getEpicListId());
        taskManager.saveSubtask(subtask2,epic, epic.getEpicListId());
        subtask2 = new Subtask("Subtask2", "Test description - 2 - 1", epic.getTaskId());
        taskManager.updateSubtask(subtask2);
        subtask2 = new Subtask("Subtask2", "Test description - 2 - 2", epic.getTaskId());
        taskManager.updateSubtask(subtask2);
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.IN_PROGRESS, epic.getEpicStatus());
    }

    @Test
    public void returnIN_PROGRESSWhenAllSubtaskAreInProgress() throws TimeIntersectionException, InvalidValueException {
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(subtask1,epic, epic.getEpicListId());
        subtask1 = new Subtask("Subtask1", "Test description - 1 - 1", epic.getTaskId());
        taskManager.updateSubtask(subtask1);
        taskManager.saveSubtask(subtask2,epic, epic.getEpicListId());
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.IN_PROGRESS, epic.getEpicStatus());
    }
}