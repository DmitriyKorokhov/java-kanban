import model.Epic;
import model.Subtask;
import model.Task;
import service.*;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws TimeIntersectionException, InvalidValueException {
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultFileBackedTasksManager();

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

        // исправил ошибки со временем
        /*
        Следует обернуть в try-catch все вызываемые методы Менеджера, но раз нет необходимости создавать
        интерфейс(меню), то, как мне кажется, оборачивать в try-catch не стоит, т.к. упадет читабельность кода
         */
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
        System.out.println("Добавление подзадачи 3 для 1 эпика");
        fileBackedTasksManager.saveSubtask(subtaskOne3, epicOne, epicOne.getEpicListId());
        System.out.println("Отсортированные по времени задачи");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        taskOne = new Task(0, taskTitleOne, taskSpecificationOne,
                LocalDateTime.of(2035, Month.NOVEMBER, 1, 21, 10),  Duration.ofDays(1));
        System.out.println("Обновление задачи 1");
        fileBackedTasksManager.updateTask(taskOne);
        System.out.println("Вывод всех задач");
        fileBackedTasksManager.outputAllTasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();
        System.out.println("Вывод всех subtask");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Обновление подзадачи с id = 5");
        subtaskOne3 = new Subtask(5,"Сбор коробок", "Собранные коробки нужно загрузить в машину",
                LocalDateTime.of(2047, Month.APRIL, 5, 21, 19), Duration.ofDays(1));
        fileBackedTasksManager.updateSubtask(subtaskOne3);
        fileBackedTasksManager.epicStatus(epicOne, epicOne.getEpicListId());
        System.out.println("Вывод подзадачи с id = 5");
        fileBackedTasksManager.outputByIdSubtasks(5);
        System.out.println("Вывод эпика с id = 2");
        fileBackedTasksManager.outputByIdEpic(2);
        System.out.println("Отсортированные по времени задачи");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        System.out.println("Удаление всех подзадач");
        fileBackedTasksManager.clearAllSubtasks();
        fileBackedTasksManager.epicStatus(epicOne, epicOne.getEpicListId());
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();
        try {
            fileBackedTasksManager.outputAllSubtasks();
        } catch (InvalidValueException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Отсортированные по времени задачи");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());

        System.out.println("Добавление подзадачи 4 для 1 эпика");
        Subtask subtaskOne4 = new Subtask("subtaskTitleOne4", "subtaskSpecification4", epicOne.getTaskId(),
                LocalDateTime.of(2023, Month.APRIL, 5, 21, 19), Duration.ofHours(10));
        fileBackedTasksManager.saveSubtask(subtaskOne4, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 5 для 1 эпика");
        try {
            Subtask subtaskOne5 = new Subtask("subtaskTitle5", "subtaskSpecification5", epicTwo.getTaskId(),
                    LocalDateTime.of(2023, Month.APRIL, 5, 21, 19), Duration.ofHours(10));
            fileBackedTasksManager.saveSubtask(subtaskOne5, epicTwo, epicTwo.getEpicListId());
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Добавление задачи 3");
        try {
            Task task0 = new Task("subtaskTitleOne2", "subtaskSpecificationOne2",
                    LocalDateTime.of(2023, Month.APRIL, 1, 23, 1), Duration.ofDays(10));
            fileBackedTasksManager.saveTask(task0);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Добавление подзадачи 6 для 1 эпика");
        Subtask subtaskOne6 = new Subtask("subtaskTitleOne6", "subtaskSpecificationOne6", epicOne.getTaskId(),
                LocalDateTime.of(2023, Month.APRIL, 5, 7, 19), Duration.ofHours(10));
        fileBackedTasksManager.saveSubtask(subtaskOne6, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 1 для 2 эпика");
        Subtask subtaskTwo1 = new Subtask("subtaskTitleTwo1", "subtaskSpecificationTwo1", epicTwo.getTaskId(),
                LocalDateTime.of(2020, Month.APRIL, 10, 7, 19), Duration.ofHours(10));
        fileBackedTasksManager.saveSubtask(subtaskTwo1, epicTwo, epicTwo.getEpicListId());
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Отсортированные по времени задачи");
        System.out.println(fileBackedTasksManager.getPrioritizedTasks());
        try {
            subtaskTwo1 = new Subtask(8,"subtaskTitleOne2", "subtaskSpecificationOne2",
                    LocalDateTime.of(2019, Month.APRIL, 1, 1, 1), Duration.ofHours(20));
            fileBackedTasksManager.updateSubtask(subtaskTwo1);
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Обновление подзадачи с id = 6");
        subtaskOne4 = new Subtask(6, "subtaskTitleTwo1", "subtaskSpecificationTwo1",
                LocalDateTime.of(2012, Month.NOVEMBER, 1, 9, 39), Duration.ofHours(15));
        fileBackedTasksManager.updateSubtask(subtaskOne4);
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();

        System.out.println("Удаление subtask id = 8");
        fileBackedTasksManager.clearByIdSubtasks(8);
        fileBackedTasksManager.epicStatus(epicTwo, epicTwo.getEpicListId());
        System.out.println("Удаление subtask id = 6");
        fileBackedTasksManager.clearByIdSubtasks(6);
        fileBackedTasksManager.epicStatus(epicOne, epicOne.getEpicListId());
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();

        System.out.println("Добавление подзадачи 3 для 2 эпика");
        Subtask subtaskTwo3 = new Subtask("subtaskTitleTwo3", "subtaskSpecificationTwo3", epicTwo.getTaskId(),
                LocalDateTime.of(2058, Month.NOVEMBER, 10, 7, 19), Duration.ofDays(100));
        fileBackedTasksManager.saveSubtask(subtaskTwo3, epicTwo, epicTwo.getEpicListId());
        System.out.println("Добавление подзадачи 4 для 2 эпика");
        Subtask subtaskTwo4 = new Subtask("subtaskTitleTwo4", "subtaskSpecificationTwo4", epicTwo.getTaskId(),
                LocalDateTime.of(2049, Month.DECEMBER, 10, 7, 19), Duration.ofHours(1));
        fileBackedTasksManager.saveSubtask(subtaskTwo4, epicTwo, epicTwo.getEpicListId());
        System.out.println("Вывод эпика с id = 3");
        fileBackedTasksManager.outputByIdEpic(3);

        System.out.println("Вывод всех subtask");
        fileBackedTasksManager.outputAllSubtasks();

        System.out.println("Обновление подзадачи с id = 10");
        subtaskTwo4  = new Subtask(10, "subtaskTitleTwo4 new", "subtaskSpecificationTwo4 new",
                LocalDateTime.of(2034, Month.MARCH, 1, 10, 39), Duration.ofHours(11));
        fileBackedTasksManager.updateSubtask(subtaskTwo4);
        fileBackedTasksManager.epicStatus(epicTwo, epicTwo.getEpicListId());

        System.out.println("Обновление подзадачи с id = 9");
        subtaskTwo3  = new Subtask(9, "subtaskTitleTwo3 new", "subtaskSpecificationTwo3 new",
                LocalDateTime.of(2099, Month.OCTOBER, 5, 10, 39), Duration.ofHours(1));
        fileBackedTasksManager.updateSubtask(subtaskTwo3);
        fileBackedTasksManager.epicStatus(epicTwo, epicTwo.getEpicListId());
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();

        System.out.println("Добавление подзадачи 5 для 2 эпика");
        Subtask subtaskOne5 = new Subtask("subtaskTitleTwo5", "subtaskSpecificationTwo5", epicTwo.getTaskId(),
                LocalDateTime.of(2030, Month.APRIL, 12, 6, 19), Duration.ofDays(1));
        fileBackedTasksManager.saveSubtask(subtaskOne5, epicOne, epicOne.getEpicListId());
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();

        System.out.println("Удаление подзадачи id = 9");
        fileBackedTasksManager.clearByIdSubtasks(9);
        fileBackedTasksManager.epicStatus(epicTwo, epicTwo.getEpicListId());
        System.out.println("Вывод всех подзадач");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();

        System.out.println("Удаление подзадачи id = 19");
        fileBackedTasksManager.clearByIdSubtasks(10);
        fileBackedTasksManager.epicStatus(epicTwo, epicTwo.getEpicListId());
        System.out.println("Вывод всех subtask");
        fileBackedTasksManager.outputAllSubtasks();
        System.out.println("Вывод всех эпиков");
        fileBackedTasksManager.outputAllEpics();

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
    }
}
