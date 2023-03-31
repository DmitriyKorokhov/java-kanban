package service.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;

import model.Epic;
import model.Subtask;
import model.Task;
import service.server.KVTaskClient;
import service.adapters.DurationTypeAdapter;
import service.adapters.LocalDateTimeConverter;
import service.exception.InvalidValueException;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter()).create();

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        this(url, false);
    }

    public HttpTaskManager(String url, boolean upload) throws IOException, InterruptedException {
        client = new KVTaskClient(url);
        if (upload) loadFromHttp();
    }

    public void save() {
        try {
        String task = gson.toJson(getListTasks());
        client.put("tasks", task);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        try {
        String subtask = gson.toJson(getListSubtasks());
        client.put("subtasks", subtask);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        try {
        String epic = gson.toJson(getListEpics());
        client.put("epics", epic);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        try {
        String history =  gson.toJson(getHistory());
        client.put("history", history);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (InvalidValueException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("HttpTaskManager: задачи сохранены на KVTaskClient");
    }

    public void loadFromHttp() throws IOException, InterruptedException {
        JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
        JsonElement jsonHistoryList;
        if (!jsonTasks.isJsonNull()) {
            JsonArray jsonTasksArray = jsonTasks.getAsJsonArray();
            Iterator iterator = jsonTasksArray.iterator();
            while(iterator.hasNext()) {
                jsonHistoryList = (JsonElement)iterator.next();
                Task task = (Task)gson.fromJson(jsonHistoryList, Task.class);
                int taskID = task.getTaskId();
                this.taskTable.put(taskID, task);
                this.prioritizedTasks.add(task);
            }
        }
        JsonElement jsonEpics = JsonParser.parseString(client.load("epics"));
        if (!jsonEpics.isJsonNull()) {
            JsonArray jsonEpicsArray = jsonEpics.getAsJsonArray();
            Iterator iterator = jsonEpicsArray.iterator();
            while(iterator.hasNext()) {
                JsonElement jsonEpic = (JsonElement)iterator.next();
                Epic task = (Epic)gson.fromJson(jsonEpic, Epic.class);
                int taskID = task.getTaskId();
                epicTable.put(taskID, task);
            }
        }
        JsonElement jsonSubtasks = JsonParser.parseString(client.load("subtasks"));
        if (!jsonSubtasks.isJsonNull()) {
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            Iterator iterator = jsonSubtasksArray.iterator();
            while(iterator.hasNext()) {
                JsonElement jsonSubtask = (JsonElement)iterator.next();
                Subtask task = (Subtask)gson.fromJson(jsonSubtask, Subtask.class);
                subtaskTable.put(task.getTaskId(), task);
                prioritizedTasks.add(task);
            }
        }
        jsonHistoryList = JsonParser.parseString(client.load("history"));
        if (!jsonHistoryList.isJsonNull()) {
            JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
            Iterator iterator = jsonHistoryArray.iterator();
            while(iterator.hasNext()) {
                JsonElement jsonTaskId = (JsonElement)iterator.next();
                int taskId = jsonTaskId.getAsInt();
                if (taskTable.containsKey(taskId)) {
                    getHistoryManager().add(taskTable.get(taskId));
                } else if (epicTable.containsKey(taskId)) {
                    getHistoryManager().add(epicTable.get(taskId));
                } else if (subtaskTable.containsKey(taskId)) {
                    getHistoryManager().add(subtaskTable.get(subtaskTable.get(taskId)));
                }
            }
        }
    }
}
