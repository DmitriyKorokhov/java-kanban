package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected int subId;
    protected int epicId;
    protected int taskId;
    protected HashMap<Integer, Subtask> subtaskTable;
    protected HashMap<Integer, Epic> epicTable;
    protected HashMap<Integer, Task> taskTable;
    public Manager() {
        subId = 0;
        epicId = 0;
        taskId = 0;
        taskTable = new HashMap<>();
        epicTable = new HashMap<>();
        subtaskTable = new HashMap<>();
    }

    public int saveTask(Task task){
        task.setTaskId(taskId);
        taskTable.put(task.getTaskId(), task);
        this.taskId++;
        return taskId;
    }

    public void updateTask(Task task){
        int id = taskId - 1;
        taskTable.put(id, task);
        task.setTaskId(id);
        if (task.getTaskStatus().equals("NEW")){
            String newStatus = "IN_PROGRESS";
            task.setTaskStatus(newStatus);
        } else if (task.getTaskStatus().equals("IN_PROGRESS")){
            String newStatus = "DONE";
            task.setTaskStatus(newStatus);
        }
    }

    public void outputAllTasks(){
        System.out.println("Список всех задач: ");
        for (Task value : taskTable.values()) {
            System.out.println(value);
        }
    }

    public void clearAllTasks(){
        taskTable.clear();
        System.out.println(taskTable.values());
    }

    public Task outputByIdTask(Integer id){
        Task task = taskTable.get(id);
        return task;
    }

    public Task clearByIdTask(Integer id){
        Task task = taskTable.remove(id);
        System.out.println("Задача с id = " + id + " удалена");
        return task;
    }

    public int saveEpic(Epic epic){
        epic.setTaskId(epicId);
        epicTable.put(epic.getTaskId(), epic);
        this.epicId++;
        return epicId;
    }

    public void updateEpic(Epic epic){
        int id = epicId - 1;
        epicTable.put(id, epic);
        epic.setTaskId(id);
    }

    public void outputAllEpics() {
        System.out.println("Список всех Epic задач: ");
        for (Epic value : epicTable.values()) {
            System.out.println(value);
        }
    }

    public void clearAllEpics(){
        epicTable.clear();
        System.out.println(epicTable.values());
    }

    public Epic outputByIdEpic(Integer id){
        Epic epic = epicTable.get(id);
        return epic;
    }

    public Epic clearByIdEpic(Integer id){
        Epic epic = epicTable.remove(id);
        System.out.println("Эпик с id = " + id + " удалена");
        return epic;
    }

    public HashMap<Integer, String> mapStatusSubtask = new HashMap<>();

    public int saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId){
        mapStatusSubtask.put(subId, subtask.subtaskStatus);
        subtask.setEpicId(subId);
        epicListId.add(subId);
        epic.setEpicListId(epicListId);
        subtaskTable.put(subtask.getEpicId(), subtask);
        this.subId++;
        return subId;
    }

    public void VVMap(){
        System.out.println(mapStatusSubtask);
    }

    public void updateSubtask(Subtask subtask){
        int id = subId - 1;
        subtaskTable.put(id, subtask);
        subtask.setEpicId(id);
        if (subtask.getSubtaskStatus().equals("NEW")){
            String newStatus = "IN_PROGRESS";
            subtask.setSubtaskStatus(newStatus);
            mapStatusSubtask.put(id, newStatus);
        } else if (subtask.getSubtaskStatus().equals("IN_PROGRESS")){
            String newStatus = "DONE";
            mapStatusSubtask.put(id, newStatus);
            subtask.setSubtaskStatus(newStatus);
        }
    }

    public void outputAllSubtasks() {
        System.out.println("Список всех подзадач: ");
        for (Subtask value : subtaskTable.values()) {
            System.out.println(value);
        }
    }

    public void clearAllSubtasks(){
        subtaskTable.clear();
        System.out.println(subtaskTable.values());
    }

    public Task outputByIdSubtasks(Integer id){
        Subtask subtask = subtaskTable.get(id);
        return subtask;
    }

    public Task clearByIdSubtasks(Integer id){
        Subtask subtask = subtaskTable.remove(id);
        System.out.println("Подадача с id = " + id + " удалена");
        return subtask;
    }

    public void SubtaskByEpic(ArrayList<Integer> epicListId){
        System.out.println("Подзадачи определенного эпика");
        for (Integer integer : epicListId) {
            for (Integer key : subtaskTable.keySet()) {
                if(integer.equals(key)){
                    System.out.println(subtaskTable.get(key));
                }
            }
        }
    }

    public void epicStatus(Epic epic , ArrayList<Integer> epicListId){
        int count = 0;
        int countDone = 0;
        for (Integer integer : epicListId) {
            for (Integer key : mapStatusSubtask.keySet()) {
                if(integer.equals(key)){
                    count++;
                    if(mapStatusSubtask.get(key).equals("IN_PROGRESS")){
                        epic.setEpicStatus("IN_PROGRESS");
                    } else if (mapStatusSubtask.get(key).equals("DONE")){
                        countDone++;
                    }
                }
            }
        }
        if (countDone == count) {
            epic.setEpicStatus("DONE");
        }
    }
}
