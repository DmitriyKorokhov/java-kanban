import model.Epic;
import model.Subtask;
import model.Task;
import service.*;

public class Main {

    public static void main(String[] args) {
        String taskTitleOne = "Переезд";
        String taskSpecificationOne = "Я перезжаю в новую квартиру, мне нужно спланировать, как я буду перевозить вещи";
        Task taskOne = new Task(taskTitleOne, taskSpecificationOne);

        String taskTitleTwo = "Переезд";
        String taskSpecificationTwo = "Продумал инструкции и начинаю подготовку";
        Task taskTwo = new Task(taskTitleTwo, taskSpecificationTwo);

        String epicTitleOne = "Сбор коробок";
        String epicSpecificationOne = "Мне нужно собрать много вещей и распределить по коробкам";
        Epic epicOne = new Epic(epicTitleOne, epicSpecificationOne);

        String subtaskTitleOne1 = "Распределение вещей";
        String subtaskSpecificationOne1 = "Вещи будут распределены по соответсвующим коробкам в определенном порядке";
        Subtask subtaskOne1 = new Subtask(subtaskTitleOne1, subtaskSpecificationOne1);

        String subtaskTitleOne2 = "Упаковка";
        String subtaskSpecificationOne2 = "Распределенные вещи нужно аккуратно упаковать";
        Subtask subtaskOne2 = new Subtask(subtaskTitleOne2, subtaskSpecificationOne2);

        String subtaskTitleOne3 = "Упаковка";
        String subtaskSpecificationOne3 = "Распределенные вещи нужно аккуратно упаковать";
        Subtask subtaskOne3 = new Subtask(subtaskTitleOne3, subtaskSpecificationOne3);

        String epicTitleTwo = "Погрузка, проверка и маркировка коробок в машину";
        String epicSpecificationTwo = "После сбора вещей в коробки, нужно вгрузить все в машину";
        Epic epicTwo = new Epic(epicTitleTwo, epicSpecificationTwo);

        TaskManager manager = Managers.getDefault();

        System.out.println("Добавление задачи 1");
        manager.saveTask(taskOne);
        System.out.println("Добавление задачи 2");
        manager.saveTask(taskTwo);
        System.out.println("Вывод всех задач");
        manager.outputAllTasks();
        System.out.println("Вывожу задачу с id = 0");
        System.out.println(manager.outputByIdTask(0));
        System.out.println("Вывожу задачу с id = 1");
        System.out.println(manager.outputByIdTask(1));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Добавление эпика 1");
        manager.saveEpic(epicOne);
        System.out.println("Добавление эпика 2");
        manager.saveEpic(epicTwo);
        System.out.println("Вывод всех эпиков");
        manager.outputAllEpics();
        System.out.println("Добавление подзадачи 1 для 1 эпика");
        manager.saveSubtask(subtaskOne1, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 2 для 1 эпика");
        manager.saveSubtask(subtaskOne2, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 3 для 1 эпика");
        manager.saveSubtask(subtaskOne3, epicOne, epicOne.getEpicListId());
        System.out.println("Вывод всех подзадач");
        manager.outputAllSubtasks();
        System.out.println("ранее выводилась задача с id = 0, повтор удалится (1 элемент в Списке) и перезапишется в конец");
        System.out.println("Вывожу задачу с id = 0");
        System.out.println(manager.outputByIdTask(0));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Вывожу подзадачу с id = 6");
        System.out.println(manager.outputByIdSubtasks(6));
        System.out.println("Вывожу подзадачу с id = 5");
        System.out.println(manager.outputByIdSubtasks(5));
        System.out.println("Вывожу подзадачу с id = 4");
        System.out.println(manager.outputByIdSubtasks(4));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("ранее выводился эпик с id = 6, повтор удалится и перезапишется в конец");
        System.out.println("Вывожу подзадачу с id = 6");
        System.out.println(manager.outputByIdSubtasks(6));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Вывожу эпик с id = 3");
        System.out.println(manager.outputByIdEpic(3));
        System.out.println("Вывожу эпик с id = 2");
        System.out.println(manager.outputByIdEpic(2));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("ранее выводилась родзадача с id = 3, повтор удалится и перезапишется в конец");
        System.out.println("Вывожу эпик с id = 3");
        System.out.println(manager.outputByIdEpic(3));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("ранее выводилась задача с id = 1, повтор удалится (1 элемент в Списке) и перезапишется в конец");
        System.out.println("Вывожу задачу с id = 1");
        System.out.println(manager.outputByIdTask(1));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Удаляю из истории элемент с id = 0");
        manager.removeHistoryById(0);
        System.out.println("Вывод истории (на 1 элемент с id = 0 меньше)");
        System.out.println(manager.getHistory());
        System.out.println("Удаляю из истории элемент с id = 2 (эпик с 3 подзадачами)");
        manager.removeHistoryById(2);
        System.out.println("Вывод истории (на 4 элемента меньше: Эпика с id = 2 и Подзадач с d = 4, 5, 6)");
        System.out.println(manager.getHistory());



    }
}
