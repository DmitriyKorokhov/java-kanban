import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TypesOfTasks;
import service.adapters.DurationTypeAdapter;
import service.adapters.LocalDateTimeConverter;
import service.exception.InvalidValueException;
import service.exception.TimeIntersectionException;
import service.managers.HttpTaskManager;
import service.managers.Managers;
import service.server.HttpTaskServer;
import service.server.KVServer;
import service.server.KVTaskClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws TimeIntersectionException, IOException, InterruptedException, InvalidValueException {
        System.out.println("Это программа Трекер задач");
        KVServer kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        HttpTaskManager taskManager = Managers.getDefaultHttpTaskManager();
        new KVTaskClient("http://localhost:8078");

        Task taskOne = new Task("taskTitleOne", "taskSpecificationOne",
                LocalDateTime.of(2035, 11, 1, 21, 10), Duration.ofDays(1));
        taskManager.saveTask(taskOne);

        Task taskTwo = new Task("taskTitleTwo", "taskSpecificationTwo",
                LocalDateTime.of(2045, 1, 1, 11, 10), Duration.ofDays(1));
        taskManager.saveTask(taskTwo);

        Epic epicOne = new Epic("Сбор коробок", "Мне нужно собрать много вещей и распределить по коробкам", TypesOfTasks.EPIC);
        taskManager.saveEpic(epicOne);

        Subtask subtaskOne = new Subtask("Сбор коробок", "Собранные коробки нужно загрузить в машину", epicOne.getTaskId(),
                LocalDateTime.of(2047, 1, 5, 21, 19), Duration.ofDays(1));
        taskManager.saveSubtask(subtaskOne, epicOne.getTaskId());

        Subtask subtaskTwo = new Subtask("Сбор коробок", "Собранные коробки нужно загрузить в машину", epicOne.getTaskId(),
                LocalDateTime.of(2057, 12, 6, 1, 19), Duration.ofDays(1));
        taskManager.saveSubtask(subtaskTwo, epicOne.getTaskId());

        System.out.println("Получить task id = 0");
        try {
            taskManager.outputByIdTask(0);
        } catch (InvalidValueException e) {
           System.out.println(e.getMessage());
        }
        System.out.println("Получить список всех задач\n" + taskManager.getListSubtasks());
        System.out.println("Получить список всех эпиков\n" + taskManager.getListEpics());
        System.out.println("Получить список всех задач\n" + taskManager.getListTasks());
        System.out.println("====");
        HttpTaskServer taskServer = Managers.getDefaultHttpTaskServer(taskManager);
        taskServer.startTaskServer();
        System.out.println("Для завершения нажмите 0");
        if ((new Scanner(System.in)).nextInt() == 0) {
            taskServer.stopTaskServer();
            kvServer.stop();
        }
    }
}


