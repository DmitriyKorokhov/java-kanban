import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.*;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Main {
    public static void main(String[] args) throws TimeIntersectionException, InvalidValueException {
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultFileBackedTasksManager();
/*
        String taskTitleOne = "Переезд 1";
        String taskSpecificationOne = "Я переезжаю в новую квартиру";
        LocalDateTime taskStartTimeOne = LocalDateTime.now();
        Duration taskDurationOne = Duration.ofDays(1);
        Task taskOne = new Task(taskTitleOne, taskSpecificationOne, taskStartTimeOne, taskDurationOne);

        String taskTitleTwo = "Переезд 2";
        String taskSpecificationTwo = "Продумал инструкции и начинаю подготовку к переезду";
        Task taskTwo = new Task(taskTitleTwo, taskSpecificationTwo);

        String epicTitleOne = "Сбор коробок";
        String epicSpecificationOne = "Мне нужно собрать много вещей и распределить по коробкам";
        Epic epicOne = new Epic(epicTitleOne, epicSpecificationOne);

        String epicTitleTwo = "Проверка и маркировка коробок в машину";
        String epicSpecificationTwo = "Сбор и погрузка всех вещей в машину";
        Epic epicTwo = new Epic(epicTitleTwo, epicSpecificationTwo);

        String subtaskTitleOne1 = "Распределение вещей";
        String subtaskSpecificationOne1 = "Вещи будут распределены по соответствующим коробкам в определенном порядке";
        LocalDateTime subtask1StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 12, 25);
        Duration subtask1Duration = Duration.ofHours(10);
        Subtask subtaskOne1 = new Subtask(subtaskTitleOne1, subtaskSpecificationOne1, epicOne.getTaskId(), subtask1StartTime, subtask1Duration);

        String subtaskTitleOne2 = "Упаковка";
        String subtaskSpecificationOne2 = "Распределенные вещи нужно аккуратно упаковать";
        LocalDateTime subtask2StartTime = LocalDateTime.of(2023, Month.NOVEMBER, 2, 11, 11);
        Duration subtask2Duration = Duration.ofHours(2);
        Subtask subtaskOne2 = new Subtask(subtaskTitleOne2, subtaskSpecificationOne2, epicOne.getTaskId(), subtask2StartTime, subtask2Duration);

        String subtaskTitleOne3 = "Сбор коробок";
        String subtaskSpecificationOne3 = "Коробки нужно собрать вместе";
        LocalDateTime subtask3StartTime = LocalDateTime.of(2018, Month.NOVEMBER, 2, 11, 11);
        Duration subtask3Duration = Duration.ofHours(2);
        Subtask subtaskOne3 = new Subtask(subtaskTitleOne3, subtaskSpecificationOne3, epicOne.getTaskId(), subtask3StartTime, subtask3Duration);


 */
        // исправил ошибку, когда время начала или конца подзадачи при обновлении некорректно обновлялись у эпика
        System.out.println("Добавление эпика 1");
        Epic epic = new Epic("Epic", "Test description");
        fileBackedTasksManager.saveEpic(epic);
        System.out.println("Добавление подзадачи 1 для 1 эпика");
        Subtask subtask = new Subtask("Testing the Subtask", "Test description", epic.getTaskId());
        fileBackedTasksManager.saveSubtask(subtask, epic, epic.getEpicListId());
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();
        System.out.println("Обновление subtask 1");
        LocalDateTime subtask1StartTime = LocalDateTime.of(2023, Month.DECEMBER, 3, 11, 11);
        Duration subtask1Duration = Duration.ofHours(20);
        subtask = new Subtask("Testing the Subtask", "Testing an update Subtask - 1", epic.getTaskId(),
                subtask1StartTime, subtask1Duration);
        fileBackedTasksManager.updateSubtask(subtask);
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();
        System.out.println("Обновление subtask 1 - 2");
        LocalDateTime subtask2StartTime = LocalDateTime.of(2025, Month.MARCH, 6, 19, 22);
        Duration subtask2Duration = Duration.ofHours(10);
        subtask = new Subtask("Testing the Subtask", "Testing an update Subtask - 2", epic.getTaskId(), subtask2StartTime, subtask2Duration);
        fileBackedTasksManager.updateSubtask(subtask);
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();

        /*
        System.out.println("Добавление задачи 1");
        fileBackedTasksManager.saveTask(taskOne);
        System.out.println("Добавление задачи 2");
        fileBackedTasksManager.saveTask(taskTwo);
        System.out.println("Добавление эпика 1");
        fileBackedTasksManager.saveEpic(epicOne);
        System.out.println("Добавление эпика 2");
        fileBackedTasksManager.saveEpic(epicTwo);
        System.out.println("Добавление подзадачи 1 для 1 эпика");
        fileBackedTasksManager.saveSubtask(subtaskOne1, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 2 для 1 эпика");
        System.out.println("Добавление подзадачи 3 для 1 эпика");
        fileBackedTasksManager.saveSubtask(subtaskOne3, epicOne, epicOne.getEpicListId());
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        LocalDateTime newTaskStartTimeOne = LocalDateTime.of(2035, Month.NOVEMBER, 1, 21, 10);;
        Duration newTaskDurationOne = Duration.ofDays(1);
        taskOne = new Task(taskTitleOne, taskSpecificationOne, newTaskStartTimeOne, newTaskDurationOne);
        System.out.println("Обновление задачи 1");
        fileBackedTasksManager.updateTask(taskOne);
        System.out.println("Вывод всех задач");
        fileBackedTasksManager.outputAllTasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        LocalDateTime newTaskStartTime3 = LocalDateTime.of(2047, Month.APRIL, 5, 21, 19);;
        Duration newTaskDuration3 = Duration.ofDays(1);
        String newSubtaskTitleOne3 = "Сбор коробок";
        String newSubtaskSpecificationOne3 = "Собранные коробки нужно загрузить в машину";
        subtaskOne3 = new Subtask(newSubtaskTitleOne3, newSubtaskSpecificationOne3, epicOne.getTaskId(), newTaskStartTime3, newTaskDuration3);
        System.out.println("Обновление подзадачи с id = 5");
        fileBackedTasksManager.updateSubtask(subtaskOne3);
        System.out.println("Вывод подзадачи с id = 5");
        fileBackedTasksManager.outputByIdSubtasks(5);
        System.out.println("Вывод эпика с id = 2");
        fileBackedTasksManager.outputByIdEpic(2);
        System.out.println("Отсортированные по времени задачи");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        System.out.println("Удаление всех подзадач");
        fileBackedTasksManager.clearAllSubtasks();
        System.out.println("Вывод всех подзадач");
        try {
            fileBackedTasksManager.outputAllSubtasks();
        } catch (InvalidValueException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Отсортированные по времени задачи");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());

        FileBackedTasksManager newFileBackedTasksManager = fileBackedTasksManager.loadFromFile("file.csv");
        System.out.println("Вывод всех задач");
        newFileBackedTasksManager.outputAllTasks();
        System.out.println("Вывод всех эпиков");
        newFileBackedTasksManager.outputAllEpics();
        System.out.println("Вывод всех подзадач");
        try {
            newFileBackedTasksManager.outputAllSubtasks();
        } catch (InvalidValueException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Вывод истории");
        System.out.println(newFileBackedTasksManager.getHistory());

         */
    }
}
