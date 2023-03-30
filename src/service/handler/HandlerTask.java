package service.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TypesOfTasks;
import service.exception.InvalidValueException;
import service.managers.TaskManager;
import service.exception.TimeIntersectionException;


public class HandlerTask extends HandlerAbstractTask {
    public HandlerTask(TaskManager manager, String requestPath, TypesOfTasks taskType) {
        super(manager);
        this.requestPath = requestPath;
        this.taskType = taskType;
    }

    protected void getTask(HttpExchange httpExchange, String path, TypesOfTasks taskType, String query) {
        try {
            if (query == null && path.equals(requestPath)) {
                String jsonString = gson.toJson(taskManager.getListTasks());
                createResponse(httpExchange, gson.toJson(jsonString), 200);
            } else if (query != null) {
                int id = Integer.parseInt(query.substring(3));
                Task task = taskManager.getTaskTable().get(id);
                boolean isQueryStartCorrect = query.equals("id=");
                if (checkTypeOfMapById(id, taskType) && isQueryStartCorrect) {
                    createResponse(httpExchange, gson.toJson(task), 200);
                } else {
                    createResponse(httpExchange, "Не найдено", 404);
                }
            } else {
                createResponse(httpExchange, "Не найдено", 404);
            }
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            createResponse(httpExchange, "Неверный запрос", 404);
        }
    }

    protected void postTask(HttpExchange httpExchange, TypesOfTasks taskType) {
        try {
            String bodyRequest = bodyRequest(httpExchange);
            if (bodyRequest.isEmpty()) {
                createResponse(httpExchange, "Неверный запрос", 400);
                return;
            }
            Task task = gson.fromJson(bodyRequest, Task.class);
            int taskId = task.getTaskId();
            if (checkTypeOfMapById(taskId, taskType)) {
                taskManager.updateTask(task);
                createResponse(httpExchange, "Задача с id = " + taskId + " обновлена", 201);
            } else {
                switch (taskType) {
                    case TASK: {
                        taskManager.saveTask(task);
                        break;
                    }
                    case SUBTASK: {
                        taskManager.saveSubtask((Subtask) task, task.getTaskId());
                        break;
                    }
                    case EPIC: {
                        taskManager.saveEpic((Epic) task);
                    }
                }
                int newTaskId = task.getTaskId();
                createResponse(httpExchange, "Задача с id = " + newTaskId + " создана", 201);
            }
        } catch (JsonSyntaxException e) {
            createResponse(httpExchange, "Недопустимый синтаксис Json в запросе", 404);
        } catch (InvalidValueException e) {
            System.out.println(e.getMessage());
        } catch (TimeIntersectionException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void deleteTask(HttpExchange httpExchange, String path, TypesOfTasks taskType, String query) {
        try {
            if (query == null && path.equals(requestPath)) {
                taskManager.clearAllTasks();
                createResponse(httpExchange, "Все задачи были удалены", 200);
            } else if (query != null) {
                int taskId = Integer.parseInt(query.substring(3));
                taskManager.clearByIdTask(taskId);
                createResponse(httpExchange, "Задача с id = " + taskId + " удалена", 200);
            } else {
                createResponse(httpExchange, "Неверный запрос", 404);
            }
        } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            createResponse(httpExchange, "Неверный запрос", 400);
        } catch (InvalidValueException e) {
            System.out.println(e.getMessage());
        }
    }
}
