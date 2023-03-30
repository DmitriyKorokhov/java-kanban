package service.handler;

import com.sun.net.httpserver.HttpExchange;
import service.managers.TaskManager;

public class HandlerPrioritizedTasks extends HandlerAbstractTask {
    public HandlerPrioritizedTasks(TaskManager taskManager, String requestPath) {
        super(taskManager);
        this.requestPath = requestPath;
    }

    public void handle(HttpExchange httpExchange) {
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());
        String query = httpExchange.getRequestURI().getQuery();
        if (method.equals("GET") && query == null && path.equals(requestPath)) {
            createResponse(httpExchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        } else if (method.equals("GET")) {
            createResponse(httpExchange, "Не найдено", 404);
        } else {
            createResponse(httpExchange, "Метод не задан", 405);
        }
    }
}
