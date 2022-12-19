package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected int subId;
    protected int epicId;
    protected int taskId;
    protected HashMap<Integer, Subtask> subtaskTable;
    protected HashMap<Integer, Epic> epicTable;
    protected HashMap<Integer, Task> taskTable;

    public InMemoryTaskManager() {
        subId = 0;
        epicId = 0;
        taskId = 0;
        taskTable = new HashMap<>();
        epicTable = new HashMap<>();
        subtaskTable = new HashMap<>();
    }

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
        historyManager.add(taskTable.get(id));
        return taskTable.get(id);
    }

    @Override
    public Task clearByIdTask(Integer id){
        Task task = taskTable.remove(id);
        System.out.println("Задача с id = " + id + " удалена");
        return task;
    }

    @Override
    public int saveEpic(Epic epic){
        epic.setTaskId(epicId);
        epicTable.put(epic.getTaskId(), epic);
        this.epicId++;
        return epicId;
    }

    @Override
    public void updateEpic(Epic epic){
        int id = epicId - 1;
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
        historyManager.add(epicTable.get(id));
        return epicTable.get(id);
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

    public HashMap<Integer, ArrayList<Integer>> mapIdSubtaskByEpic = new HashMap<>();

    public HashMap<Integer, Status> mapStatusSubtask = new HashMap<>();

    @Override
    public int saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId){
        mapIdSubtaskByEpic.put(epic.getTaskId(), epicListId);
        mapStatusSubtask.put(subId, subtask.subtaskStatus);
        subtask.setEpicId(subId);
        epicListId.add(subId);
        epic.setEpicListId(epicListId);
        subtaskTable.put(subtask.getEpicId(), subtask);
        this.subId++;
        return subId;
    }

    @Override
    public void updateSubtask(Subtask subtask){
        int id = subId - 1;
        subtaskTable.put(id, subtask);
        subtask.setEpicId(id);
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
        historyManager.add(subtaskTable.get(id));
        //historyTasks.add(subtaskTable.get(id));
        return subtaskTable.get(id);
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

    HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}