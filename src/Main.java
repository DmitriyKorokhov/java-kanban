import model.Epic;
import model.Subtask;
import model.Task;
import service.Manager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String taskTitleOne = "Переезд";
        String taskSpecificationOne = "Я перезжаю в новую квартиру, мне нужно спланировать, как я буду перевозить вещи";
        Task taskOne = new Task(taskTitleOne, taskSpecificationOne);

        String taskTitleTwo = "Переезд";
        String taskSpecificationTwo = "Продумал инструкции и нацинаю подготовку";
        Task taskTwo = new Task(taskTitleTwo, taskSpecificationTwo);

        String taskTitleThree = "Переезд";
        String taskSpecificationThree = "План выполнен в совершенстве";
        Task taskThree = new Task(taskTitleThree, taskSpecificationThree);

        String taskTitleFour = "Переезд";
        String taskSpecificationFour = "Забыл некоторые вещи в старой кваритре";
        Task taskFour = new Task(taskTitleFour, taskSpecificationFour);

        String epicTitleOne = "Сбор коробок";
        String epicSpecificationOne = "Мне нужно собрать много вещей и распределить по коробкам";
        Epic epicOne = new Epic(epicTitleOne, epicSpecificationOne);

        String subtaskTitleOne1 = "Распределение вещей";
        String subtaskSpecificationOne1 = "Вещи будут распределены по соответсвующим коробкам в определенном порядке";
        Subtask subtaskOne1 = new Subtask(subtaskTitleOne1, subtaskSpecificationOne1, epicOne.getTaskId());

        String subtaskTitleOne2 = "Упаковка";
        String subtaskSpecificationOne2 = "Распределенные вещи нужно аккуратно упаковать";
        Subtask subtaskOne2 = new Subtask(subtaskTitleOne2, subtaskSpecificationOne2, epicOne.getTaskId());

        String epicTitleTwo = "Погрузка, проверка и маркировка коробок в машину";
        String epicSpecificationTwo = "После сбора вещей в коробки, нужно вгрузить все в машину";
        Epic epicTwo = new Epic(epicTitleTwo, epicSpecificationTwo);

        String subtaskTitleTwo1 = "Подпись коробок";
        String subtaskSpecificationTwo1 = "Чтобы не перепутать, что лежит в коробках, их нужно подписать";
        Subtask subtaskTwo1 = new Subtask(subtaskTitleTwo1, subtaskSpecificationTwo1, epicOne.getTaskId());

        String subtaskTitleTwo2 = "Погрузка в машину";
        String subtaskSpecificationTwo2 = "Аккуратно складываем коробки в машину";
        Subtask subtaskTwo2 = new Subtask(subtaskTitleTwo2, subtaskSpecificationTwo2, epicOne.getTaskId());

        String subtaskTitleTwo3 = "Проверка количества коробок";
        String subtaskSpecificationTwo3 = "Сравниваем количество коробок при упаковке дома с теми, которые складываем в машину";
        Subtask subtaskTwo3 = new Subtask(subtaskTitleTwo3, subtaskSpecificationTwo3, epicOne.getTaskId());

        String epicTitleThree = "Проверка";
        String epicSpecificationThree = "Данный epic нужен для оценки работоспособности менеджера";
        Epic epicThree = new Epic(epicTitleThree, epicSpecificationThree);

        Manager manager = new Manager();

        manager.saveTask(taskOne);
        manager.saveTask(taskTwo);
        manager.outputAllTasks();
        manager.updateTask(taskFour);
        manager.saveTask(taskThree);
        //manager.clearAllTasks();
        manager.outputAllTasks();
        System.out.println("Введите id, чтобы вывести задачу по идентификатору");
        Integer outputIdTask = scanner.nextInt();
        System.out.println(manager.outputByIdTask(outputIdTask));
        System.out.println("Введите id, чтобы удалить задачу по идентификатору");
        Integer removeIdTask = scanner.nextInt();
        System.out.println(manager.clearByIdTask(removeIdTask));
        manager.outputAllTasks();
        manager.saveEpic(epicOne);
        manager.saveEpic(epicTwo);
        //manager.updateEpic(epicThree);
        manager.outputAllEpics();
        //manager.clearAllEpics();
        manager.saveSubtask(subtaskOne1, epicOne, epicOne.getEpicListId());
        manager.saveSubtask(subtaskOne2, epicOne, epicOne.getEpicListId());
        manager.saveSubtask(subtaskTwo1, epicTwo, epicTwo.getEpicListId());
        manager.updateSubtask(subtaskTwo3);
        manager.outputAllSubtasks();
        manager.saveSubtask(subtaskTwo2, epicTwo, epicTwo.getEpicListId());
        manager.outputAllSubtasks();
        manager.VVMap();
        manager.epicStatus(epicTwo, epicTwo.getEpicListId());
        manager.epicStatus(epicOne, epicOne.getEpicListId());
        manager.outputAllEpics();
        manager.SubtaskByEpic(epicTwo.getEpicListId());
        System.out.println("Введите id, чтобы вывести подзадачу по идентификатору");
        Integer outputIdSubtask = scanner.nextInt();
        System.out.println(manager.outputByIdSubtasks(outputIdSubtask));
        System.out.println("Введите id, чтобы удалить подзадачу по идентификатору");
        Integer removeIdSubtask = scanner.nextInt();
        System.out.println(manager.clearByIdSubtasks(removeIdSubtask));
        manager.outputAllSubtasks();
        System.out.println("Введите id, чтобы вывести эпик по идентификатору");
        Integer outputIdEpic = scanner.nextInt();
        System.out.println(manager.outputByIdEpic(outputIdEpic));
        System.out.println("Введите id, чтобы удалить эпик по идентификатору");
        Integer removeIdEpic = scanner.nextInt();
        System.out.println(manager.clearByIdEpic(removeIdEpic));
        manager.outputAllEpics();
    }
}
