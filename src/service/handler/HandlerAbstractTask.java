package service.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import model.TypesOfTasks;
import service.adapters.DurationTypeAdapter;
import service.adapters.LocalDateTimeConverter;
import service.managers.TaskManager;

public abstract class HandlerAbstractTask implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager taskManager;
    protected final Gson gson = new GsonBuilder().registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter()).create();
    protected String requestPath;
    protected TypesOfTasks taskType;

    public HandlerAbstractTask(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange httpExchange) {
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());
        String query = httpExchange.getRequestURI().getQuery();
        switch (method) {
            case "GET": {
                getTask(httpExchange, path, taskType, query);
                break;
            }
            case "POST": {
                postTask(httpExchange, taskType);
                break;
            }
            case "DELETE": {
                deleteTask(httpExchange, path, taskType, query);
                break;
            }
            default:
                createResponse(httpExchange, "Такого эндпоинта не существует", 404);
        }
    }

    public boolean checkTypeOfMapById(int id, TypesOfTasks taskType) {
        if (taskManager.getTaskTable().containsKey(id) && taskType.equals(TypesOfTasks.TASK)) {
            return true;
        } else if (taskManager.getEpicTable().containsKey(id) && taskType.equals(TypesOfTasks.EPIC)) {
            return true;
        } else {
            return taskManager.getSubtaskTable().containsKey(id) && taskType.equals(TypesOfTasks.SUBTASK);
        }
    }

    public String bodyRequest(HttpExchange httpExchange) {
        try {
            return new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createResponse(HttpExchange httpExchange, String response, int code) {
        try {
            httpExchange.sendResponseHeaders(code, 0);
            OutputStream outputStream = httpExchange.getResponseBody();
            try {
                outputStream.write(response.getBytes());
            } catch (Throwable throwable_1) {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable throwable_2) {
                        throwable_1.addSuppressed(throwable_2);
                    }
                }
                throw throwable_1;
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void getTask(HttpExchange httpExchange, String path, TypesOfTasks taskType, String query) {
    }

    protected void postTask(HttpExchange httpExchange, TypesOfTasks taskType) {
    }

    protected void deleteTask(HttpExchange httpExchange, String path, TypesOfTasks taskType, String query) {
    }
}
