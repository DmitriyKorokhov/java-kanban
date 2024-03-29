package service.managers;

import service.server.HttpTaskServer;
import service.server.KVServer;

import java.io.IOException;

public class Managers {
    private static final String URI = "http://localhost:8078";

    public static HttpTaskManager getDefaultHttpTaskManager() throws IOException, InterruptedException {
        return new HttpTaskManager(URI);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }

    public static KVServer getDefaultKVServer() throws IOException {
        return new KVServer();
    }

    public static HttpTaskServer getDefaultHttpTaskServer(HttpTaskManager taskManager) throws IOException, InterruptedException {
        return new HttpTaskServer(taskManager);
    }

}
