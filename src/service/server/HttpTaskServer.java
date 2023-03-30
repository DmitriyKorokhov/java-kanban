package service.server;

import com.sun.net.httpserver.HttpServer;
import model.TypesOfTasks;
import service.handler.*;
import service.managers.Managers;
import service.managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private final int PORT = 8090;
    private final HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

    public HttpTaskServer() throws IOException, InterruptedException {
        this(Managers.getDefaultHttpTaskManager());
    }

    public void startTaskServer() {
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
        this.httpServer.start();
    }

    public void stopTaskServer() {
        this.httpServer.stop(0);
        System.out.println("На "+ PORT + " порту HttpTaskServer сервер остановлен!");
    }

    public HttpTaskServer(TaskManager httpTaskManager) throws IOException {
        httpServer.createContext("/tasks/", new HandlerPrioritizedTasks(httpTaskManager, "/tasks/"));
        httpServer.createContext("/tasks/task/", new HandlerTask(httpTaskManager, "/tasks/task/", TypesOfTasks.TASK));
        httpServer.createContext("/tasks/epic/", new HandlerEpic(httpTaskManager, "/tasks/epic/", TypesOfTasks.EPIC));
        httpServer.createContext("/tasks/subtask/", new HandlerSubtask(httpTaskManager, "/tasks/subtask/", TypesOfTasks.SUBTASK));
        httpServer.createContext("/tasks/subtask/epic/", new HandlerSubtaskByEpicId(httpTaskManager, "/tasks/subtask/epic/"));
        httpServer.createContext("/tasks/history/", new HandlerHistory(httpTaskManager, "/tasks/history"));
    }
}
