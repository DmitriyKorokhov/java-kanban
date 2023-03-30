package service.handler;

import com.sun.net.httpserver.HttpExchange;
import service.exception.InvalidValueException;
import service.managers.TaskManager;

public class HandlerHistory extends HandlerAbstractTask {
    public HandlerHistory(TaskManager taskManager, String requestPath) {
        super(taskManager);
        this.requestPath = requestPath;
    }

    public void handle(HttpExchange httpExchange) {
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());
        try {
            String query = httpExchange.getRequestURI().getQuery();
            if (method.equals("GET") && query == null && path.equals(requestPath)) {
                createResponse(httpExchange, gson.toJson(taskManager.getHistory()), 200);
            } else if (method.equals("GET")) {
                createResponse(httpExchange, "Неверный запрос", 404);
            } else {
                createResponse(httpExchange, "Метод недопустим", 405);
            }
        } catch (InvalidValueException e) {
            System.out.println(e.getMessage());
        }
    }
}
