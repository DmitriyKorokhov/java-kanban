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
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Subtask> subtaskTable = new HashMap<>();
    private final HashMap<Integer, Epic> epicTable = new HashMap<>();
    private final HashMap<Integer, Task> taskTable = new HashMap<>();
    private final HashMap<Integer, ArrayList<Integer>> mapIdSubtaskByEpic = new HashMap<>();
    private final HashMap<Integer, Status> mapStatusSubtask = new HashMap<>();
    private final ArrayList<Integer> listOfTasksIdForHistory = new ArrayList<>();

    @Override
    public void saveTask(Task task) {
        task.setTaskId(taskId);
        taskTable.put(task.getTaskId(), task);
        this.taskId++;
    }

    @Override
    public void updateTask(Task task) {
        int id = taskId - 1;
        taskTable.put(id, task);
        task.setTaskId(id);
        if (task.getTaskStatus().equals(Status.NEW)) {
            task.setTaskStatus(Status.IN_PROGRESS);
        } else if (task.getTaskStatus().equals(Status.IN_PROGRESS)) {
            task.setTaskStatus(Status.DONE);
        }
    }

    @Override
    public void outputAllTasks() {
        for (Task value : taskTable.values()) {
            System.out.println(value);
        }
    }

    @Override
    public void clearAllTasks() {
        taskTable.clear();
    }

    @Override
    public void outputByIdTask(Integer id) {
        if (!taskTable.containsKey(id)) {
            System.out.println("Задача с данным id удалена или не вводилась");
        } else {
            historyManager.add(taskTable.get(id));
            listOfTasksIdForHistory.add(id);
            System.out.println(taskTable.get(id));
        }
    }

    @Override
    public void clearByIdTask(Integer id) {
        taskTable.remove(id);
        System.out.println("Задача с id = " + id + " удалена");
    }

    @Override
    public void saveEpic(Epic epic) {
        epic.setTaskId(taskId);
        epicTable.put(epic.getTaskId(), epic);
        this.taskId++;
    }

    @Override
    public void updateEpic(Epic epic) {
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
    public void clearAllEpics() {
        epicTable.clear();
        clearAllSubtasks();
    }

    @Override
    public void outputByIdEpic(Integer id) {
        if (!epicTable.containsKey(id)) {
            System.out.println("Епик с данным id удален или не вводился");
        } else {
            historyManager.add(epicTable.get(id));
            listOfTasksIdForHistory.add(id);
            System.out.println(epicTable.get(id));
        }
    }

    @Override
    public void clearByIdEpic(Integer id) {
        epicTable.remove(id);
        System.out.println("Эпик с id = " + id + " удален");
        ArrayList<Integer> listOfIdSubtask = mapIdSubtaskByEpic.get(id);
        for (Integer idSubtask : listOfIdSubtask) {
                   subtaskTable.remove(idSubtask);
        }
    }

    @Override
    public void saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId) {
        mapIdSubtaskByEpic.put(epic.getTaskId(), epicListId);
        mapStatusSubtask.put(taskId, subtask.getSubtaskStatus());
        subtask.setTaskId(taskId);
        epicListId.add(taskId);
        epic.setEpicListId(epicListId);
        subtaskTable.put(subtask.getTaskId(), subtask);
        subtask.setIdEpic(epic.getTaskId());
        this.taskId++;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = taskId - 1;
        subtaskTable.put(id, subtask);
        subtask.setTaskId(id);
        if (subtask.getSubtaskStatus().equals(Status.NEW)) {
            subtask.setSubtaskStatus(Status.IN_PROGRESS);
            mapStatusSubtask.put(id, Status.IN_PROGRESS);
        } else if (subtask.getSubtaskStatus().equals(Status.IN_PROGRESS)) {
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
    public void clearAllSubtasks() {
        subtaskTable.clear();
    }

    @Override
    public void outputByIdSubtasks(Integer id) {
        if (!subtaskTable.containsKey(id)) {
            System.out.println("Подзадача с данным id удалена или не вводилась");
        } else {
            historyManager.add(subtaskTable.get(id));
            listOfTasksIdForHistory.add(id);
            System.out.println(subtaskTable.get(id));
        }
    }

    @Override
    public void clearByIdSubtasks(Integer id) {
        subtaskTable.remove(id);
        System.out.println("Подадача с id = " + id + " удалена");
    }

    @Override
    public void SubtaskByEpic(ArrayList<Integer> epicListId) {
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
            ArrayList<Integer> listOfIdSubtask = mapIdSubtaskByEpic.get(id);
            for (Integer idSubtask : listOfIdSubtask) {
                historyManager.remove(idSubtask);
            }
        }
        historyManager.remove(id);
    }

    public HashMap<Integer, Subtask> getSubtaskTable() {
        return subtaskTable;
    }

    public HashMap<Integer, Epic> getEpicTable() {
        return epicTable;
    }

    public HashMap<Integer, Task> getTaskTable() {
        return taskTable;
    }

    public HashMap<Integer, ArrayList<Integer>> getMapIdSubtaskByEpic() {
        return mapIdSubtaskByEpic;
    }

    public ArrayList<Integer> getListOfTasksIdForHistory() {
        return listOfTasksIdForHistory;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
