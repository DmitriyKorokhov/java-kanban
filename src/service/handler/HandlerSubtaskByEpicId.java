package service.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import model.TypesOfTasks;
import service.exception.InvalidValueException;
import service.managers.TaskManager;

import java.util.List;

public class HandlerSubtaskByEpicId extends HandlerAbstractTask {
    public HandlerSubtaskByEpicId(TaskManager taskManager, String requestPath) {
        super(taskManager);
        this.requestPath = requestPath;
    }

    public void handle(HttpExchange httpExchange) {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            String query = httpExchange.getRequestURI().getQuery();
            if (query != null) {
                try {
                    int id = Integer.parseInt(query.substring(3));
                    List<Subtask> subtasks = taskManager.getSubTasksByEpicId(id);
                    if (checkTypeOfMapById(id, TypesOfTasks.EPIC)) {
                        createResponse(httpExchange, gson.toJson(subtasks), 200);
                    } else {
                        createResponse(httpExchange, "Неверный запрос", 404);
                    }
                } catch (StringIndexOutOfBoundsException | NumberFormatException var9) {
                    createResponse(httpExchange, "Неверный запрос", 404);
                } catch (InvalidValueException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                createResponse(httpExchange, "Неверный запрос", 404);
            }
        } else {
            createResponse(httpExchange, "Метод не допустим", 405);
        }
    }
}
