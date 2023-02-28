package taskmanager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InvalidValueException;
import service.TaskManager;
import service.TimeIntersectionException;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    void beforeEach() {
    }

    @Test
    public void saveAStandardTask() throws TimeIntersectionException {
        Task task = new Task("Testing the Task", "Test description");
        taskManager.saveTask(task);
        final int taskId = task.getTaskId();
        final Task savedTask = taskManager.getTaskTable().get(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(1, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(task, taskManager.getTaskTable().get(taskId), "Задачи не совпадают.");
    }

    @Test
    public void returnAnEmptyTaskList() {
        assertEquals("{}", taskManager.getTaskTable().toString());
    }

    @Test
    public void updateStatusWhenTaskIsCreatedOrUpdated() throws TimeIntersectionException, InvalidValueException {
        Task task = new Task("Testing the Task", "Test description");
        taskManager.saveTask(task);
        assertEquals(Status.NEW, task.getTaskStatus());
        task = new Task("Testing the Task", "Testing an update Task - 1");
        taskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, task.getTaskStatus());
        task = new Task("Testing the Task", "Testing an update Task - 2");
        taskManager.updateTask(task);
        assertEquals(Status.DONE, task.getTaskStatus());
    }

    @Test
    public void updateStatusWhenATaskListIsEmpty() {
        Task task = new Task("Test Task", "Testing an update Task");
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.updateTask(task));
        assertEquals("Данная задача или не существует, или пересекается по времени с другими задачами", ex.getMessage());
    }

    @Test
    public void clearTaskByIdWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Task taskOne = new Task("Testing the TaskOne", "Test description");
        taskManager.saveTask(taskOne);
        final int id = 0;
        Task taskTwo = new Task("Testing the TaskTwo", "Test description");
        taskManager.saveTask(taskTwo);
        taskManager.clearByIdTask(id);
        final Task savedTask = taskManager.getTaskTable().get(id);
        assertNull(savedTask, "Задача найдена.");
        assertEquals(1, taskManager.getTaskTable().size(), "Неверное количество задач.");
    }

    @Test
    public void clearTaskByIdWithAnEmptyList() {
        final int id = 0;
        final Task savedTask = taskManager.getTaskTable().get(id);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.clearByIdTask(id));
        assertEquals("Задача с данным id удалена или не вводилась", ex.getMessage());
        assertNull(savedTask, "Задача найдена.");
        assertEquals(0, taskManager.getTaskTable().size(), "Неверное количество задач.");
    }

    @Test
    public void clearANonexistentTaskId() throws TimeIntersectionException {
        Task task = new Task("Testing the Task", "Test description");
        taskManager.saveTask(task);
        final int id = 1;
        final Task savedTask = taskManager.getTaskTable().get(id);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.clearByIdTask(id));
        assertEquals("Задача с данным id удалена или не вводилась", ex.getMessage());
        assertNull(savedTask, "Задача найдена.");
        assertEquals(1, taskManager.getTaskTable().size(), "Неверное количество задач.");
    }

    @Test
    public void clearAllTasks() throws TimeIntersectionException {
        Task task = new Task("Testing the Task", "Test description");
        taskManager.saveTask(task);
        taskManager.clearAllTasks();
        assertEquals("{}", taskManager.getTaskTable().toString());
    }

    @Test
    public void outputByIdTaskWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Task taskOne = new Task("Testing the TaskOne", "Test description");
        taskManager.saveTask(taskOne);
        Task taskTwo = new Task("Testing the TaskTwo", "Test description");
        taskManager.saveTask(taskTwo);
        final int id = taskTwo.getTaskId();
        taskManager.outputByIdTask(id);
        assertEquals(1, taskManager.getListOfTasksIdForHistory().get(0));
    }

    @Test
    public void outputByIdTaskWithAnEmptyList() {
        final int id = 1;
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.outputByIdTask(id));
        assertEquals("Задача с данным id удалена или не вводилась", ex.getMessage());
        assertEquals("[]", taskManager.getListOfTasksIdForHistory().toString());
    }

    @Test
    public void outputByIdTaskWithAnInvalidId() throws TimeIntersectionException {
        Task task = new Task("Testing the Task", "Test description");
        taskManager.saveTask(task);
        final int id = 1;
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.outputByIdTask(id));
        assertEquals("Задача с данным id удалена или не вводилась", ex.getMessage());
        assertEquals("[]", taskManager.getListOfTasksIdForHistory().toString());
    }

    @Test
    public void saveAStandardEpic() {
        Epic epic = new Epic("Testing the Epic", "Epic description");
        taskManager.saveEpic(epic);
        final int epicId = epic.getTaskId();
        final Task savedEpic = taskManager.getEpicTable().get(epicId);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(1, taskManager.getEpicTable().size(), "Неверное количество Эпиков.");
        assertEquals(epic, taskManager.getEpicTable().get(epicId), "Эпики не совпадают.");
    }

    @Test
    public void returnAnEmptyEpicList() {
        assertEquals("{}", taskManager.getEpicTable().toString());
    }

    @Test
    public void updateStatusWhenEpicIsCreatedOrUpdated() throws InvalidValueException {
        Epic epic = new Epic("Testing the Epic", "Epic description");
        taskManager.saveEpic(epic);
        final int epicId = epic.getTaskId();
        epic = new Epic("Testing the Epic", "Testing an update Epic - 1");
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.getEpicTable().get(epicId));
        epic = new Epic("Testing the Epic", "Testing an update Epic - 2");
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.getEpicTable().get(epicId));
    }

    @Test
    public void updateStatusWhenAEpicListIsEmpty() {
        Epic epic = new Epic("Test Epic", "Testing an update Epic");
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.updateEpic(epic));
        assertEquals("Данный эпик или не существует, или пересекается по времени с другими задачами", ex.getMessage());
    }

    @Test
    public void clearEpicByIdWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Epic epicOne = new Epic("Testing the EpicOne", "Test description");
        taskManager.saveEpic(epicOne);
        final int epicId = epicOne.getTaskId();
        Subtask subtaskForEpicOne = new Subtask("Testing the EpicOne", "Test description", epicId);
        taskManager.saveSubtask(subtaskForEpicOne, epicOne, epicOne.getEpicListId());
        final int subtaskId = subtaskForEpicOne.getTaskId();
        Epic epicTwo = new Epic("Testing the EpicTwo", "Test description");
        taskManager.saveEpic(epicTwo);
        taskManager.clearByIdEpic(epicId);
        final Epic savedEpic = taskManager.getEpicTable().get(epicId);
        final Subtask savedSubtask = taskManager.getSubtaskTable().get(subtaskId);
        assertNull(savedEpic, "Эпик найден.");
        assertNull(savedSubtask, "Подзадача найдена.");
        assertEquals(1, taskManager.getEpicTable().size(), "Неверное количество эпиков.");
        assertEquals(0, taskManager.getSubtaskTable().size(), "Неверное количество подзадач.");
    }

    @Test
    public void clearEpicByIdWithAnEmptyList() {
        final int id = 0;
        final Epic savedEpic = taskManager.getEpicTable().get(id);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.clearByIdEpic(id));
        assertEquals("Эпик с данным id удалена или не вводилась", ex.getMessage());
        assertNull(savedEpic, "Задача найдена.");
        assertEquals(0, taskManager.getEpicTable().size(), "Неверное количество задач.");
    }

    @Test
    public void clearANonexistentEpicId(){
        Epic epic = new Epic("Testing the Epic", "Test description");
        taskManager.saveEpic(epic);
        final int id = 1;
        final Task savedEpic = taskManager.getEpicTable().get(id);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.clearByIdEpic(id));
        assertEquals("Эпик с данным id удалена или не вводилась", ex.getMessage());
        assertNull(savedEpic, "Задача найдена.");
        assertEquals(1, taskManager.getEpicTable().size(), "Неверное количество задач.");
    }

    @Test
    public void clearAllEpics() {
        Epic epic = new Epic("Testing the Epic", "Test description");
        taskManager.saveEpic(epic);
        taskManager.clearAllEpics();
        assertEquals("{}", taskManager.getEpicTable().toString());
    }

    @Test
    public void outputByIdEpicWithAFilledList() throws InvalidValueException {
        Epic epicOne = new Epic("Testing the EpicOne", "Test description");
        taskManager.saveEpic(epicOne);
        Epic epicTwo = new Epic("Testing the EpicTwo", "Test description");
        taskManager.saveEpic(epicTwo);
        final int id = epicTwo.getTaskId();
        taskManager.outputByIdEpic(id);
        assertEquals(1, taskManager.getListOfTasksIdForHistory().get(0));
    }

    @Test
    public void outputByIdEpicWithAnEmptyList() {
        final int id = 1;
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.outputByIdEpic(id));
        assertEquals("Эпик с данным id удален или не вводился", ex.getMessage());
        assertEquals("[]", taskManager.getListOfTasksIdForHistory().toString());
    }

    @Test
    public void outputByIdEpicWithAnInvalidId() {
        Epic epic = new Epic("Testing the Epic", "Test description");
        taskManager.saveEpic(epic);
        final int id = 1;
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.outputByIdEpic(id));
        assertEquals("Эпик с данным id удален или не вводился", ex.getMessage());
        assertEquals("[]", taskManager.getListOfTasksIdForHistory().toString());
    }

    @Test
    public void saveAStandardSubtask() throws TimeIntersectionException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        final int subtaskId = subtask.getTaskId();
        final Subtask savedSubtask = taskManager.getSubtaskTable().get(subtaskId);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
        assertEquals(1, taskManager.getSubtaskTable().size(), "Неверное количество подзадач.");
        assertEquals(subtask, taskManager.getSubtaskTable().get(subtaskId), "Подзадачи не совпадают.");
        assertEquals(subtaskId, epic.getEpicListId().get(0), "Подзадача не входит в список своего эпика.");
    }

    @Test
    public void returnAnEmptySubtaskList() {
        assertEquals("{}", taskManager.getSubtaskTable().toString());
    }

    @Test
    public void updateStatusWhenSubtaskIsCreatedOrUpdated() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        assertEquals(Status.NEW, subtask.getSubtaskStatus());
        subtask = new Subtask("Testing the Subtask", "Testing an update Subtask - 1", epic.getTaskId());
        taskManager.updateSubtask(subtask);
        assertEquals(Status.IN_PROGRESS, subtask.getSubtaskStatus());
        subtask = new Subtask("Testing the Subtask", "Testing an update Subtask - 2", epic.getTaskId());
        taskManager.updateSubtask(subtask);
        assertEquals(Status.DONE, subtask.getSubtaskStatus());
    }

    @Test
    public void updateStatusWhenASubtaskListIsEmpty() {
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", 0);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.updateSubtask(subtask));
        assertEquals("Данная подзадача или не существует, или пересекается по времени с другими задачами", ex.getMessage());

    }

    @Test
    public void clearSubtaskByIdWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtaskOne = new Subtask("Testing the SubtaskOne", "Test description - 1", epic.getTaskId());
        taskManager.saveSubtask(subtaskOne, epic, epic.getEpicListId());
        final int id = subtaskOne.getTaskId();
        Subtask subtaskTwo = new Subtask("Testing the SubtaskTwo", "Test description - 2", epic.getTaskId());
        taskManager.saveSubtask(subtaskTwo, epic, epic.getEpicListId());
        taskManager.clearByIdSubtasks(id);
        final Subtask savedSubtask = taskManager.getSubtaskTable().get(id);
        assertNull(savedSubtask, "Подзадача найдена.");
        assertEquals(1, taskManager.getSubtaskTable().size(), "Неверное количество подзадач.");
    }

    @Test
    public void clearSubtaskByIdWithAnEmptyList() {
        final int id = 0;
        final Subtask savedSubtask = taskManager.getSubtaskTable().get(id);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.clearByIdSubtasks(id));
        assertEquals("Подзадача с данным id удалена или не вводилась", ex.getMessage());
        assertNull(savedSubtask, "Подзадача найдена.");
        assertEquals(0, taskManager.getSubtaskTable().size(), "Неверное количество подзадач.");
    }

    @Test
    public void clearANonexistentSubtaskId() throws TimeIntersectionException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        final int id = 0;
        final Subtask savedSubtask = taskManager.getSubtaskTable().get(id);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.clearByIdSubtasks(id));
        assertEquals("Подзадача с данным id удалена или не вводилась", ex.getMessage());
        assertNull(savedSubtask, "Подзадача найдена.");
        assertEquals(1, taskManager.getSubtaskTable().size(), "Неверное количество подзадач.");
    }

    @Test
    public void clearAllSubtasks() throws TimeIntersectionException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        taskManager.clearAllSubtasks();
        assertEquals("{}", taskManager.getSubtaskTable().toString());
    }

    @Test
    public void outputByIdSubtaskWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtaskOne = new Subtask("Testing the SubtaskOne", "Test description - 1", epic.getTaskId());
        taskManager.saveSubtask(subtaskOne, epic, epic.getEpicListId());
        Subtask subtaskTwo = new Subtask("Testing the SubtaskTwo", "Test description - 2", epic.getTaskId());
        taskManager.saveSubtask(subtaskTwo, epic, epic.getEpicListId());
        final int id = subtaskTwo.getTaskId();
        taskManager.outputByIdSubtasks(id);
        assertEquals(id, taskManager.getListOfTasksIdForHistory().get(0));
    }

    @Test
    public void outputByIdSubtaskWithAnEmptyList() {
        final int id = 1;
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.outputByIdSubtasks(id));
        assertEquals("Подзадача с данным id удалена или не вводилась", ex.getMessage());
        assertEquals("[]", taskManager.getListOfTasksIdForHistory().toString());
    }

    @Test
    public void outputByIdSubtaskWithAnInvalidId() throws TimeIntersectionException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        final int id = 2;
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.outputByIdSubtasks(id));
        assertEquals("Подзадача с данным id удалена или не вводилась", ex.getMessage());
        assertEquals("[]", taskManager.getListOfTasksIdForHistory().toString());
    }

    @Test
    public void presenceOfATaskInASubtask() throws TimeIntersectionException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        final int idSubtask = subtask.getTaskId();
        assertTrue(epic.getEpicListId().contains(idSubtask), "Наличие эпика у подзадачи не обнаружено.");
    }

    @Test
    public void deletingASubtaskWhenDeletingAnEpic() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        final int epicId = epic.getTaskId();
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        final int idSubtask = subtask.getTaskId();
        taskManager.clearByIdEpic(epicId);
        assertFalse(taskManager.getSubtaskTable().containsKey(idSubtask), "Подзадача обнаружена.");
    }

    @Test
    public void findingOutTheEpicStatus() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic, epic.getEpicListId());
        assertEquals(Status.NEW, epic.getEpicStatus(), "Неверный статус у эпика");
        subtask = new Subtask("Testing the Subtask", "Test description - 1", epic.getTaskId());
        taskManager.updateSubtask(subtask);
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.IN_PROGRESS, epic.getEpicStatus());
        subtask = new Subtask("Testing the Subtask", "Test description - 2", epic.getTaskId());
        taskManager.updateSubtask(subtask);
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.DONE, epic.getEpicStatus());
    }
}