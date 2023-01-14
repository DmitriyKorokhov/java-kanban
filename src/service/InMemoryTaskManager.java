package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private final HashMap<Integer, Subtask> subtaskTable = new HashMap<>();
    private final HashMap<Integer, Epic> epicTable = new HashMap<>();
    private final HashMap<Integer, Task> taskTable = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private HashMap<Integer, ArrayList<Integer>> mapIdSubtaskByEpic = new HashMap<>();
    private HashMap<Integer, Status> mapStatusSubtask = new HashMap<>();

    @Override
    public int saveTask(Task task){
        task.setTaskId(taskId);
        taskTable.put(task.getTaskId(), task);
        this.taskId++;
        return taskId;
    }

    @Override
    public void updateTask(Task task){
        int id = taskId - 1;
        taskTable.put(id, task);
        task.setTaskId(id);
        if (task.getTaskStatus().equals(Status.NEW)){
            task.setTaskStatus(Status.IN_PROGRESS);
        } else if (task.getTaskStatus().equals(Status.IN_PROGRESS)){
            task.setTaskStatus(Status.DONE);
        }
    }

    @Override
    public void outputAllTasks(){
        for (Task value : taskTable.values()) {
            System.out.println(value);
        }
    }

    @Override
    public void clearAllTasks(){
        taskTable.clear();

    }

    @Override
    public Task outputByIdTask(Integer id){
        historyManager.add(taskTable.getOrDefault(id, new Task("Задача отсутствует",
                "Задача с данным id удалена или не вводилась")));
        return taskTable.getOrDefault(id, new Task("Задача отсутствует",
                "Задача с данным id удалена или не вводилась"));
    }

    @Override
    public Task clearByIdTask(Integer id){
        Task task = taskTable.remove(id);
        System.out.println("Задача с id = " + id + " удалена");
        return task;
    }

    @Override
    public int saveEpic(Epic epic){
        epic.setTaskId(taskId);
        epicTable.put(epic.getTaskId(), epic);
        this.taskId++;
        return taskId;
    }

    @Override
    public void updateEpic(Epic epic){
        int id = taskId - 1;
        epicTable.put(id, epic);
        epic.setTaskId(id);
    }

    @Override
    public void outputAllEpics() {
        for (Epic value : epicTable.values()) {
            System.out.println(value);
        }
    }

    @Override
    public void clearAllEpics(){
        epicTable.clear();
        clearAllSubtasks();
    }

    @Override
    public Epic outputByIdEpic(Integer id){
        historyManager.add(epicTable.getOrDefault(id, new Epic("Епик отсутствует",
                "Епик с данным id удален или не вводился")));
        return epicTable.getOrDefault(id, new Epic("Епик отсутствует",
                "Епик с данным id удален или не вводился"));
    }

    @Override
    public Epic clearByIdEpic(Integer id){
        Epic epic = epicTable.remove(id);
        System.out.println("Эпик с id = " + id + " удален");
        ArrayList<Integer> idSubtask = mapIdSubtaskByEpic.get(id);
        for (Integer integer : idSubtask) {
                   subtaskTable.remove(integer);
        }
        return epic;
    }

    @Override
    public int saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId){
        mapIdSubtaskByEpic.put(epic.getTaskId(), epicListId);
        mapStatusSubtask.put(taskId, subtask.subtaskStatus);
        subtask.setTaskId(taskId);
        epicListId.add(taskId);
        epic.setEpicListId(epicListId);
        subtaskTable.put(subtask.getTaskId(), subtask);
        this.taskId++;
        return taskId;
    }

    @Override
    public void updateSubtask(Subtask subtask){
        int id = taskId - 1;
        subtaskTable.put(id, subtask);
        subtask.setTaskId(id);
        if (subtask.getSubtaskStatus().equals(Status.NEW)){
            subtask.setSubtaskStatus(Status.IN_PROGRESS);
            mapStatusSubtask.put(id, Status.IN_PROGRESS);
        } else if (subtask.getSubtaskStatus().equals(Status.IN_PROGRESS)){
            subtask.setSubtaskStatus(Status.DONE);
            mapStatusSubtask.put(id, Status.DONE);
        }
    }

    @Override
    public void outputAllSubtasks() {
        for (Subtask value : subtaskTable.values()) {
            System.out.println(value);
        }
    }

    @Override
    public void clearAllSubtasks(){
        subtaskTable.clear();
    }

    @Override
    public Subtask outputByIdSubtasks(Integer id){
        historyManager.add(subtaskTable.getOrDefault(id, new Subtask("Подзадача отсутствует",
                "Подзадача с данным id удалена или не вводилась")));
        return subtaskTable.getOrDefault(id, new Subtask("Подзадача отсутствует",
                "Подзадача с данным id удалена или не вводилась"));
    }

    @Override
    public Subtask clearByIdSubtasks(Integer id){
        Subtask subtask = subtaskTable.remove(id);
        System.out.println("Подадача с id = " + id + " удалена");
        return subtask;
    }

    @Override
    public void SubtaskByEpic(ArrayList<Integer> epicListId){
        for (Integer integer : epicListId) {
            for (Integer key : subtaskTable.keySet()) {
                if(integer.equals(key)){
                    System.out.println(subtaskTable.get(key));
                }
            }
        }
    }

    @Override
    public void epicStatus(Epic epic , ArrayList<Integer> epicListId){
        int count = 0;
        int countDone = 0;
        for (Integer integer : epicListId) {
            for (Integer key : mapStatusSubtask.keySet()) {
                if(integer.equals(key)){
                    count++;
                    if(mapStatusSubtask.get(key).equals(Status.IN_PROGRESS)){
                        epic.setEpicStatus(Status.IN_PROGRESS);
                    } else if (mapStatusSubtask.get(key).equals(Status.DONE)){
                        countDone++;
                    }
                }
            }
        }
        if (countDone == count) {
            epic.setEpicStatus(Status.DONE);
        }
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public void removeHistoryById(int id){
        if (mapIdSubtaskByEpic.containsKey(id)){
            ArrayList<Integer> idSubtask = mapIdSubtaskByEpic.get(id);
            for (Integer integer : idSubtask) {
                historyManager.remove(integer);
            }
        }
        historyManager.remove(id);
    }
}
