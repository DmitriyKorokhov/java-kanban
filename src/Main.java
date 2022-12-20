import model.Epic;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        String taskTitleOne = "Переезд";
        String taskSpecificationOne = "Я перезжаю в новую квартиру, мне нужно спланировать, как я буду перевозить вещи";
        Task taskOne = new Task(taskTitleOne, taskSpecificationOne);

        String taskTitleTwo = "Переезд";
        String taskSpecificationTwo = "Продумал инструкции и нацинаю подготовку";
        Task taskTwo = new Task(taskTitleTwo, taskSpecificationTwo);

        String taskTitleThree = "Переезд";
        String taskSpecificationThree = "План выполнен в совершенстве";
        Task taskThree = new Task(taskTitleThree, taskSpecificationThree);

        String taskTitleNew = "Переезд";
        String taskSpecificationNew = "Забыл некоторые вещи в старой кваритре";
        Task taskNew = new Task(taskTitleNew, taskSpecificationNew);

        String taskTitleFour = "Переезд - 4";
        String taskSpecificationFour = "Снова забыл некоторые вещи в старой кваритре";
        Task taskFour = new Task(taskTitleFour, taskSpecificationFour);

        String taskTitleFive = "Переезд - 5";
        String taskSpecificationFive = "Еще рваз забыл некоторые вещи в старой кваритре";
        Task taskFive = new Task(taskTitleFive, taskSpecificationFive);

        String taskTitleSix = "Переезд - 6";
        String taskSpecificationSix = "И вновь я забыл некоторые вещи в старой кваритре";
        Task taskSix = new Task(taskTitleSix, taskSpecificationSix);

        String taskTitleSeven = "Переезд - 7";
        String taskSpecificationSeven = "Повторно забыл некоторые вещи в старой кваритре";
        Task taskSeven = new Task(taskTitleSeven, taskSpecificationSeven);

        String taskTitleEight = "Переезд - 8";
        String taskSpecificationEight = "В который раз забыл некоторые вещи в старой кваритре";
        Task taskEight = new Task(taskTitleEight, taskSpecificationEight);

        String taskTitleNine = "Переезд - 9";
        String taskSpecificationNine = "В очередной раз я забыл некоторые вещи в старой кваритре";
        Task taskNine = new Task(taskTitleNine, taskSpecificationNine);

        String taskTitleTen = "Переезд - 10";
        String taskSpecificationTen = "Опять-таки я забыл некоторые вещи в старой кваритре";
        Task taskTen = new Task(taskTitleTen, taskSpecificationTen);

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

        TaskManager manager = Managers.getDefault();

        System.out.println("Добавление задачи 1");
        manager.saveTask(taskOne);
        System.out.println("Добавление задачи 2");
        manager.saveTask(taskTwo);
        System.out.println("Вывод всех задач");
        manager.outputAllTasks();
        System.out.println("Обновление задачи 2");
        manager.updateTask(taskNew);
        System.out.println("Вывод всех задач");
        manager.outputAllTasks();
        System.out.println("Добавление задачи 3");
        manager.saveTask(taskThree);
        System.out.println("Вывод всех задач");
        manager.outputAllTasks();
        System.out.println("Вывожу задачу с id = 1");
        System.out.println(manager.outputByIdTask(1));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Удаляю задачу с id = 0");
        System.out.println(manager.clearByIdTask(0));
        System.out.println("Вывод всех задач");
        manager.outputAllTasks();
        System.out.println("Добавление эпика 1");
        manager.saveEpic(epicOne);
        System.out.println("Вывод всех эпиков");
        manager.outputAllEpics();
        System.out.println("Обновление эпика 1");
        manager.updateEpic(epicThree);
        System.out.println("Вывод всех эпиков");
        manager.outputAllEpics();
        System.out.println("Добавление эпика 2");
        manager.saveEpic(epicTwo);
        System.out.println("Вывод всех эпиков");
        manager.outputAllEpics();
        System.out.println("Добавление подзадачи 1 для 1 эпика");
        manager.saveSubtask(subtaskOne1, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 2 для 1 эпика");
        manager.saveSubtask(subtaskOne2, epicOne, epicOne.getEpicListId());
        System.out.println("Добавление подзадачи 1 для 2 эпика");
        manager.saveSubtask(subtaskTwo1, epicTwo, epicTwo.getEpicListId());
        System.out.println("Вывод всех подзадач");
        manager.outputAllSubtasks();
        System.out.println("Обновление подзадачи 1 (2 эпика)");
        manager.updateSubtask(subtaskTwo3);
        System.out.println("Вывод всех подзадач");
        manager.outputAllSubtasks();
        System.out.println("Добавление подзадачи 2 для 2 эпика");
        manager.saveSubtask(subtaskTwo2, epicTwo, epicTwo.getEpicListId());
        System.out.println("Вывод всех подзадач");
        manager.outputAllSubtasks();
        System.out.println("Определение статуса 2 эпика");
        manager.epicStatus(epicTwo, epicTwo.getEpicListId());
        System.out.println("Определение статуса 1 эпика");
        manager.epicStatus(epicOne, epicOne.getEpicListId());
        System.out.println("Вывод всех эпиков");
        manager.outputAllEpics();
        System.out.println("Вывод подзадач по 2 эпику");
        manager.SubtaskByEpic(epicTwo.getEpicListId());
        System.out.println("Удаляю эпик с  id = 0");
        System.out.println("Удаление эпика и его подзадач");
        manager.clearByIdEpic(0);
        System.out.println("Вывод всех эпиков");
        manager.outputAllEpics();
        System.out.println("Вывод всех подзадач");
        manager.outputAllSubtasks();
        System.out.println("Вывожу подзадачу с id = 7. Данной подзадачи не существует. В выводе подзадачи и истории это отображается");
        System.out.println(manager.outputByIdSubtasks(7));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Удаляю подзадачу с id = 2");
        System.out.println(manager.clearByIdSubtasks(2));
        System.out.println("Вывод всех подзадач");
        manager.outputAllSubtasks();
        System.out.println("Ранее эпик с id = 0 был удален. В выводе эпика и истории это отображается");
        System.out.println("Вывожу эпик с id = 0");
        System.out.println(manager.outputByIdEpic(0));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Проверка на работу ограничителя истории в 10 элементов. Сейчас в истории 3 элемента");
        System.out.println("Добавление задачи 4");
        manager.saveTask(taskFour);
        System.out.println("Добавление задачи 5");
        manager.saveTask(taskFive);
        System.out.println("Добавление задачи 6");
        manager.saveTask(taskSix);
        System.out.println("Добавление задачи 7");
        manager.saveTask(taskSeven);
        System.out.println("Добавление задачи 8");
        manager.saveTask(taskEight);
        System.out.println("Добавление задачи 9");
        manager.saveTask(taskNine);
        System.out.println("Добавление задачи 10");
        manager.saveTask(taskTen);
        System.out.println("Вывод всех задач");
        manager.outputAllTasks();
        System.out.println("Вывожу задачу с id = 1");
        System.out.println(manager.outputByIdTask(1));
        System.out.println("Вывожу задачу с id = 2");
        System.out.println(manager.outputByIdTask(2));
        System.out.println("Вывожу задачу с id = 4");
        System.out.println(manager.outputByIdTask(4));
        System.out.println("Вывожу задачу с id = 5");
        System.out.println(manager.outputByIdTask(5));
        System.out.println("Вывожу задачу с id = 6");
        System.out.println(manager.outputByIdTask(6));
        System.out.println("Вывожу задачу с id = 7");
        System.out.println(manager.outputByIdTask(7));
        System.out.println("Вывожу задачу с id = 8 - 10 элемент");
        System.out.println(manager.outputByIdTask(8));
        System.out.println("Вывод истории");
        System.out.println(manager.getHistory());
        System.out.println("Вывожу задачу с id = 9 - 11 элемент");
        System.out.println(manager.outputByIdTask(9));
        System.out.println("Вывод истории, т.к. должно быть 10 элементов: первый элемент был удален");
        System.out.println(manager.getHistory());
        System.out.println("Удаление всех эпиков");
        manager.clearAllEpics();
        System.out.println("Вывод всех эпиков и соответственно всех подзадач");
        manager.outputAllEpics();
        System.out.println("Удаление всех задач");
        manager.clearAllTasks();
        System.out.println("Вывод всех задач");
        manager.outputAllTasks();
        System.out.println("Вывод всех подзадач");
        manager.outputAllSubtasks();
    }
}
