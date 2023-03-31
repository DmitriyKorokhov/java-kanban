package taskmanager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.exception.InvalidValueException;
import service.managers.TaskManager;
import service.exception.TimeIntersectionException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
public abstract class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

    @BeforeEach
    protected void beforeEach() throws IOException, InterruptedException {
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
        assertTrue(taskManager.getPrioritizedTasks().contains(task));
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
    }

    @Test
    public void saveATaskNotOverlapInTime() throws TimeIntersectionException {
        LocalDateTime task1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration task1Duration = Duration.ofHours(10);
        Task taskOne = new Task("Testing the Task - 1", "Test description", task1StartTime, task1Duration);
        taskManager.saveTask(taskOne);
        final int taskOneId = taskOne.getTaskId();
        final Task savedTaskOne = taskManager.getTaskTable().get(taskOneId);
        LocalDateTime task2StartTime = LocalDateTime.of(2023, Month.DECEMBER, 3, 11, 11);
        Duration task2Duration = Duration.ofHours(20);
        Task taskTwo = new Task("Testing the Task - 2", "Test description", task2StartTime, task2Duration);
        taskManager.saveTask(taskTwo);
        final int taskTwoId = taskTwo.getTaskId();
        final Task savedTaskTwo = taskManager.getTaskTable().get(taskTwoId);
        assertNotNull(savedTaskOne, "Задача 1 не найдена.");
        assertNotNull(savedTaskTwo, "Задача 2 не найдена.");
        assertEquals(taskOne, savedTaskOne, "Задачи 1 не совпадают.");
        assertEquals(taskTwo, savedTaskTwo, "Задачи 2 не совпадают.");
        assertEquals(2, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
    }

    @Test
    public void saveATaskOverlapInTime() throws TimeIntersectionException {
        LocalDateTime task1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration task1Duration = Duration.ofDays(10);
        Task taskOne = new Task("Testing the Task - 1", "Test description", task1StartTime, task1Duration);
        taskManager.saveTask(taskOne);
        final int taskOneId = taskOne.getTaskId();
        final Task savedTaskOne = taskManager.getTaskTable().get(taskOneId);
        LocalDateTime task2StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 6, 2, 19);
        Duration task2Duration = Duration.ofHours(20);
        Task taskTwo = new Task("Testing the Task - 2", "Test description", task2StartTime, task2Duration);
        TimeIntersectionException ex = assertThrows(TimeIntersectionException.class, () -> taskManager.saveTask(taskTwo));
        assertEquals("Задача пересекается по времени с другими задачами", ex.getMessage(), "Задачи не пересекаются");
        final int taskTwoId = taskTwo.getTaskId();
        final Task savedTaskTwo = taskManager.getTaskTable().get(taskTwoId);
        assertNotNull(savedTaskOne, "Задача 1 не найдена.");
        assertEquals(taskOne, savedTaskOne, "Задачи 1 не совпадают.");
        assertNotEquals(taskTwo, savedTaskTwo, "Задача 2 совпадает.");
        assertEquals(1, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
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
        LocalDateTime task1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration task1Duration = Duration.ofDays(10);
        task = new Task("Testing the Task", "Testing an update Task - 1", task1StartTime, task1Duration);
        taskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, task.getTaskStatus());
        assertEquals(task1StartTime, task.getTaskStartTime(), "Неверная дата");
        assertEquals(task1Duration, task.getTaskDuration(), "Неверная продолжительность");
        LocalDateTime task2StartTime = LocalDateTime.of(2025, Month.DECEMBER, 9, 11, 19);
        Duration task2Duration = Duration.ofDays(10);
        task = new Task("Testing the Task", "Testing an update Task - 2", task2StartTime, task2Duration);
        taskManager.updateTask(task);
        assertEquals(Status.DONE, task.getTaskStatus());
        assertEquals(task2StartTime, task.getTaskStartTime(), "Неверная дата");
        assertEquals(task2Duration, task.getTaskDuration(), "Неверная продолжительность");
    }

    @Test
    public void updateStatusWhenATaskListIsEmpty() {
        Task task = new Task("Test Task", "Testing an update Task");
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.updateTask(task));
        assertEquals("Данной задачи не существует", ex.getMessage());
    }

    @Test
    public void updateStatusATaskNotOverlapInTime() throws TimeIntersectionException, InvalidValueException {
        LocalDateTime task1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration task1Duration = Duration.ofHours(10);
        Task taskOne = new Task("Testing the Task - 1", "Test description", task1StartTime, task1Duration);
        taskManager.saveTask(taskOne);
        final int oldTaskOneId = taskOne.getTaskId();
        final Task oldSavedTaskOne = taskManager.getTaskTable().get(oldTaskOneId);
        LocalDateTime task2StartTime = LocalDateTime.of(2023, Month.DECEMBER, 3, 11, 11);
        Duration task2Duration = Duration.ofHours(20);
        Task taskTwo = new Task("Testing the Task - 2", "Test description", task2StartTime, task2Duration);
        taskManager.saveTask(taskTwo);
        LocalDateTime newTask1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration newTask1Duration = Duration.ofHours(10);
        taskOne = new Task("Testing the Task - 1", "Testing an update Task - 1", newTask1StartTime, newTask1Duration);
        taskManager.updateTask(taskOne);
        final int taskOneId = taskOne.getTaskId();
        final Task savedTaskOne = taskManager.getTaskTable().get(taskOneId);
        assertNotEquals(oldSavedTaskOne, savedTaskOne, "Старая и новая задачи 1 совпадают.");
        assertEquals(2, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(Status.IN_PROGRESS, taskOne.getTaskStatus());
    }

    @Test
    public void updateStatusATaskOverlapInTime() throws TimeIntersectionException{
        LocalDateTime task1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration task1Duration = Duration.ofHours(10);
        Task taskOne = new Task("Testing the Task - 1", "Test description", task1StartTime, task1Duration);
        taskManager.saveTask(taskOne);
        LocalDateTime task2StartTime = LocalDateTime.of(2023, Month.DECEMBER, 3, 11, 11);
        Duration task2Duration = Duration.ofHours(20);
        Task taskTwo = new Task("Testing the Task - 2", "Test description", task2StartTime, task2Duration);
        taskManager.saveTask(taskTwo);
        LocalDateTime newTask1StartTime = LocalDateTime.of(2023, Month.DECEMBER, 3, 2, 19);
        Duration newTask1Duration = Duration.ofDays(20);
        taskOne = new Task("Testing the Task - 1", "Testing an update Task - 1", newTask1StartTime, newTask1Duration);
        Task finalTaskOne = taskOne;
        TimeIntersectionException ex = assertThrows(TimeIntersectionException.class, () -> taskManager.updateTask(finalTaskOne));
        assertEquals("Задача пересекается по времени с другими задачами", ex.getMessage());
        assertEquals(2, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(Status.NEW, taskOne.getTaskStatus());
    }

    @Test
    public void clearTaskByIdWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Task taskOne = new Task("Testing the TaskOne", "Test description");
        taskManager.saveTask(taskOne);
        final int id = 0;
        Task taskTwo = new Task("Testing the TaskTwo", "Test description");
        taskManager.saveTask(taskTwo);
        assertEquals(2, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
        taskManager.clearByIdTask(id);
        final Task savedTask = taskManager.getTaskTable().get(id);
        assertNull(savedTask, "Задача найдена.");
        assertEquals(1, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
    }

    @Test
    public void clearTaskByIdWithAnEmptyList() {
        final int id = 0;
        final Task savedTask = taskManager.getTaskTable().get(id);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.clearByIdTask(id));
        assertEquals("Задача с данным id удалена или не вводилась", ex.getMessage());
        assertNull(savedTask, "Задача найдена.");
        assertEquals(0, taskManager.getTaskTable().size(), "Неверное количество задач.");
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
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
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
    }

    @Test
    public void clearAllTasks() throws TimeIntersectionException {
        Task task = new Task("Testing the Task", "Test description");
        taskManager.saveTask(task);
        taskManager.clearAllTasks();
        assertEquals("{}", taskManager.getTaskTable().toString());
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
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
    public void updateStatusWhenEpicIsCreatedOrUpdated() throws InvalidValueException, TimeIntersectionException {
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
        assertEquals("Данного эпика не существует", ex.getMessage());
    }

    @Test
    public void clearEpicByIdWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Epic epicOne = new Epic("Testing the EpicOne", "Test description");
        taskManager.saveEpic(epicOne);
        final int epicId = epicOne.getTaskId();
        Subtask subtaskForEpicOne = new Subtask("Testing the EpicOne", "Test description", epicId);
        taskManager.saveSubtask(subtaskForEpicOne, epicOne.getTaskId());
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
        taskManager.saveSubtask(subtask, epic.getTaskId());
        final int subtaskId = subtask.getTaskId();
        final Subtask savedSubtask = taskManager.getSubtaskTable().get(subtaskId);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
        assertEquals(1, taskManager.getSubtaskTable().size(), "Неверное количество подзадач.");
        assertEquals(subtask, taskManager.getSubtaskTable().get(subtaskId), "Подзадачи не совпадают.");
        assertEquals(subtaskId, epic.getEpicListId().get(0), "Подзадача не входит в список своего эпика.");
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
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
        taskManager.saveSubtask(subtask, epic.getTaskId());
        assertEquals(Status.NEW, subtask.getSubtaskStatus());
        LocalDateTime subtask1StartTime = LocalDateTime.of(2023, Month.DECEMBER, 3, 11, 11);
        Duration subtask1Duration = Duration.ofHours(20);
        subtask = new Subtask(1, "Testing the Subtask", "Testing an update Subtask - 1",
                subtask1StartTime, subtask1Duration);
        taskManager.updateSubtask(subtask);
        assertEquals(Status.IN_PROGRESS, subtask.getSubtaskStatus());
        assertEquals(subtask1StartTime, subtask.getTaskStartTime(), "Неверное время начала подзадачи");
        assertEquals(subtask1Duration, subtask.getTaskDuration(), "Неверная продолжительность подзадачи");
        assertEquals(subtask1StartTime, epic.getTaskStartTime(), "Неверное время начала у эпика");
        assertEquals(subtask.getTaskEndTime(), epic.getTaskEndTime(), "Неверная продолжительность эпика");
        LocalDateTime subtask2StartTime = LocalDateTime.of(2025, Month.MARCH, 6, 19, 22);
        Duration subtask2Duration = Duration.ofHours(10);
        subtask = new Subtask(1, "Testing the Subtask", "Testing an update Subtask - 2", subtask2StartTime, subtask2Duration);
        taskManager.updateSubtask(subtask);
        assertEquals(subtask2StartTime, subtask.getTaskStartTime(), "Неверное время начала подзадачи");
        assertEquals(subtask2Duration, subtask.getTaskDuration(), "Неверная продолжительность подзадачи");
        assertEquals(subtask2StartTime, epic.getTaskStartTime(), "Неверное время начала у эпика");
        assertEquals(subtask.getTaskEndTime(), epic.getTaskEndTime(), "Неверная продолжительность эпика");
        assertEquals(Status.DONE, subtask.getSubtaskStatus());
    }

    @Test
    public void updateStatusWhenASubtaskListIsEmpty() {
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", 0);
        InvalidValueException ex = assertThrows(InvalidValueException.class, () -> taskManager.updateSubtask(subtask));
        assertEquals("Данной подзадачи не существует", ex.getMessage());

    }

    @Test
    public void clearSubtaskByIdWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtaskOne = new Subtask("Testing the SubtaskOne", "Test description - 1", epic.getTaskId());
        taskManager.saveSubtask(subtaskOne, epic.getTaskId());
        final int id = subtaskOne.getTaskId();
        Subtask subtaskTwo = new Subtask("Testing the SubtaskTwo", "Test description - 2", epic.getTaskId());
        taskManager.saveSubtask(subtaskTwo, epic.getTaskId());
        taskManager.clearByIdSubtasks(subtaskOne.getTaskId());
        final Subtask savedSubtask = taskManager.getSubtaskTable().get(id);
        assertNull(savedSubtask, "Подзадача найдена.");
        assertEquals(1, taskManager.getSubtaskTable().size(), "Неверное количество подзадач.");
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "Неверное количество отсортированных по времени задач.");
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
        taskManager.saveSubtask(subtask, epic.getTaskId());
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
        taskManager.saveSubtask(subtask, epic.getTaskId());
        taskManager.clearAllSubtasks();
        assertEquals("{}", taskManager.getSubtaskTable().toString());
    }

    @Test
    public void outputByIdSubtaskWithAFilledList() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtaskOne = new Subtask("Testing the SubtaskOne", "Test description - 1", epic.getTaskId());
        taskManager.saveSubtask(subtaskOne, epic.getTaskId());
        Subtask subtaskTwo = new Subtask("Testing the SubtaskTwo", "Test description - 2", epic.getTaskId());
        taskManager.saveSubtask(subtaskTwo, epic.getTaskId());
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
        taskManager.saveSubtask(subtask, epic.getTaskId());
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
        taskManager.saveSubtask(subtask, epic.getTaskId());
        final int idSubtask = subtask.getTaskId();
        assertTrue(epic.getEpicListId().contains(idSubtask), "Наличие эпика у подзадачи не обнаружено.");
    }

    @Test
    public void deletingASubtaskWhenDeletingAnEpic() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        final int epicId = epic.getTaskId();
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic.getTaskId());
        final int idSubtask = subtask.getTaskId();
        taskManager.clearByIdEpic(epicId);
        assertFalse(taskManager.getSubtaskTable().containsKey(idSubtask), "Подзадача обнаружена.");
    }

    @Test
    public void findingOutTheEpicStatus() throws TimeIntersectionException, InvalidValueException {
        Epic epic = new Epic("Epic", "Test description");
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        taskManager.saveSubtask(subtask, epic.getTaskId());
        int subtaskId = subtask.getTaskId();
        assertEquals(Status.NEW, epic.getEpicStatus(), "Неверный статус у эпика");
        subtask = new Subtask(subtaskId, "Testing the Subtask", "Test description - 1");
        taskManager.updateSubtask(subtask);
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.IN_PROGRESS, epic.getEpicStatus());
        subtask = new Subtask(subtaskId, "Testing the Subtask", "Test description - 2");
        taskManager.updateSubtask(subtask);
        taskManager.epicStatus(epic, epic.getEpicListId());
        assertEquals(Status.DONE, epic.getEpicStatus());
    }
}