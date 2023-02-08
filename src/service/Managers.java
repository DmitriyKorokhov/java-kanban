package service;

public class Managers {

    private static String file = "file.csv";

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    // использую новую реализацию
    public static FileBackedTasksManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager(file);
    }
}
