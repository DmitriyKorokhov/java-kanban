package service.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

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
        String subtask = gson.toJson(getListSubtasks());
        client.put("subtasks", subtask);
        String epic = gson.toJson(getListEpics());
        client.put("epics", epic);
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

    private void loadFromHttp() throws IOException, InterruptedException {
        JsonElement jsonElementForTask = JsonParser.parseString(client.load("tasks"));
        if (jsonElementForTask != null) {
            JsonArray arrayTask = jsonElementForTask.getAsJsonArray();
            for (JsonElement jsonTsk : arrayTask) {
                Task task = gson.fromJson(jsonTsk, Task.class);
                taskTable.put(task.getTaskId(), task);
                prioritizedTasks.add(task);
            }
        }
        JsonElement jsonElementForEpic = JsonParser.parseString(client.load("epics"));
        if (jsonElementForEpic != null) {
            JsonArray arrayEpic = jsonElementForEpic.getAsJsonArray();
            for (JsonElement jsonTsk : arrayEpic) {
                Epic epic = gson.fromJson(jsonTsk, Epic.class);
                epicTable.put(epic.getTaskId(), epic);
            }
        }
        JsonElement jsonElementForSubtask = JsonParser.parseString(client.load("subtasks"));
        if (jsonElementForSubtask != null) {
            JsonArray arraySubtask = jsonElementForSubtask.getAsJsonArray();
            for (JsonElement jsonTsk : arraySubtask) {
                Subtask subtask = gson.fromJson(jsonTsk, Subtask.class);
                subtaskTable.put(subtask.getTaskId(), subtask);
                prioritizedTasks.add(subtask);
            }
        }
        JsonElement jsonHistoryList = JsonParser.parseString(this.client.load("history"));
        if (jsonHistoryList != null) {
            JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
            for (JsonElement jsonElement : jsonHistoryArray) {
                JsonElement jsonTaskId = jsonElement;
                int taskId = jsonTaskId.getAsInt();
                if (this.subtaskTable.containsKey(taskId)) {
                    this.getHistoryManager().add(subtaskTable.get(taskId));
                } else if (this.epicTable.containsKey(taskId)) {
                    this.getHistoryManager().add(epicTable.get(taskId));
                } else if (this.taskTable.containsKey(taskId)) {
                    this.getHistoryManager().add(taskTable.get(taskId));
                }
            }
        }
    }
}
