package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager  {
    private int taskId = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Subtask> subtaskTable = new HashMap<>();
    private final HashMap<Integer, Epic> epicTable = new HashMap<>();
    private final HashMap<Integer, Task> taskTable = new HashMap<>();
    private final HashMap<Integer, ArrayList<Integer>> mapIdSubtaskByEpic = new HashMap<>();
    private final HashMap<Integer, Status> mapStatusSubtask = new HashMap<>();
    private final ArrayList<Integer> listOfTasksIdForHistory = new ArrayList<>();

    private final Set<Task> prioritizedTasks = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.getTaskStartTime() != null && task2.getTaskStartTime() != null) {
                return task1.getTaskStartTime().compareTo(task2.getTaskStartTime());
            } else if (task1.getTaskStartTime() == null && task2.getTaskStartTime() != null) {
                return 1;
            } else {
                return -1;
            }
        }
    });

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void saveTask(Task task) {
        task.setTaskId(taskId);
        taskTable.put(task.getTaskId(), task);
        prioritizedTasks.add(task);
        this.taskId++;
    }

    @Override
    public void updateTask(Task task) throws InvalidValueException {
        boolean check = false;
        for (Map.Entry<Integer, Task> taskEntry : taskTable.entrySet()) {
            if (taskEntry.getValue().getTaskTitle().equals(task.getTaskTitle())) {
                int id = taskEntry.getKey();
                task.setTaskStatus(taskEntry.getValue().getTaskStatus());
                task.setTaskId(id);
                check = true;
                taskTable.put(id, task);
                if (task.getTaskStatus().equals(Status.NEW)) {
                    task.setTaskStatus(Status.IN_PROGRESS);
                } else if (task.getTaskStatus().equals(Status.IN_PROGRESS)) {
                    task.setTaskStatus(Status.DONE);
                }
            }
        }
        if (!check) {
            throw new InvalidValueException("Задача удалена или не вводилась");
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
        for (Integer id : taskTable.keySet()) {
            if (listOfTasksIdForHistory.contains(id)) {
                listOfTasksIdForHistory.remove(id);
            }
        }
        taskTable.clear();
    }

    @Override
    public void outputByIdTask(Integer id) throws InvalidValueException {
        if (!taskTable.containsKey(id)) {
            throw new InvalidValueException("Задача с данным id удалена или не вводилась");
        } else {
            historyManager.add(taskTable.get(id));
            if (listOfTasksIdForHistory.contains(id)){
                listOfTasksIdForHistory.remove(id);
                listOfTasksIdForHistory.add(id);
            } else {
                listOfTasksIdForHistory.add(id);
            }
            System.out.println(taskTable.get(id));
        }
    }

    @Override
    public void clearByIdTask(Integer id) throws InvalidValueException {
        if (taskTable.containsKey(id)) {
            taskTable.remove(id);
            listOfTasksIdForHistory.remove(id);
            System.out.println("Задача с id = " + id + " удалена");
        } else {
            throw new InvalidValueException("Задача с данным id удалена или не вводилась");
        }
    }

    @Override
    public void saveEpic(Epic epic) {
        epic.setTaskId(taskId);
        epicTable.put(epic.getTaskId(), epic);
        this.taskId++;
    }

    @Override
    public void updateEpic(Epic epic) throws InvalidValueException {
        boolean check = false;
        for (Map.Entry<Integer, Epic> epicEntry : epicTable.entrySet()) {
            if (epicEntry.getValue().getTaskTitle().equals(epic.getTaskTitle())) {
                int id = epicEntry.getKey();
                epic.setEpicStatus(epicEntry.getValue().getEpicStatus());
                epic.setTaskId(id);
                check = true;
                epicTable.put(id, epic);
            }
        }
        if (!check) {
            throw new InvalidValueException("Задача удалена или не вводилась");
        }
    }

    @Override
    public void outputAllEpics() {
        for (Epic value : epicTable.values()) {
            System.out.println(value);
        }
    }

    @Override
    public void clearAllEpics() {
        for (Integer id : epicTable.keySet()) {
            if (listOfTasksIdForHistory.contains(id)) {
                listOfTasksIdForHistory.remove(id);
            }
        }
        epicTable.clear();
        clearAllSubtasks();
    }

    @Override
    public void outputByIdEpic(Integer id) throws InvalidValueException {
        if (!epicTable.containsKey(id)) {
            throw new InvalidValueException("Эпик с данным id удален или не вводился");
        } else {
            historyManager.add(epicTable.get(id));
            if (listOfTasksIdForHistory.contains(id)){
                listOfTasksIdForHistory.remove(id);
                listOfTasksIdForHistory.add(id);
            } else {
                listOfTasksIdForHistory.add(id);
            }
            System.out.println(epicTable.get(id));
        }
    }

    @Override
    public void clearByIdEpic(Integer id) throws InvalidValueException {
        if (epicTable.containsKey(id)) {
            epicTable.remove(id);
            listOfTasksIdForHistory.remove(id);
            System.out.println("Эпик с id = " + id + " удален");
            if (!mapIdSubtaskByEpic.isEmpty()) {
                ArrayList<Integer> listOfIdSubtask = mapIdSubtaskByEpic.get(id);
                for (Integer idSubtask : listOfIdSubtask) {
                    subtaskTable.remove(idSubtask);
                }
            }
        } else {
            throw new InvalidValueException("Эпик с данным id удалена или не вводилась");
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
        epic.setEpicStartTime(subtask);
        epic.setEpicEndTime(subtask);
        prioritizedTasks.add(subtask);
        this.taskId++;
    }

    @Override
    public void updateSubtask(Subtask subtask) throws InvalidValueException {
        boolean check = false;
        for (Map.Entry<Integer, Subtask> subtaskEntry : subtaskTable.entrySet()) {
            if (subtaskEntry.getValue().getTaskTitle().equals(subtask.getTaskTitle())) {
                int id = subtaskEntry.getKey();
                subtask.setSubtaskStatus(subtaskEntry.getValue().getSubtaskStatus());
                subtask.setTaskId(id);
                check = true;
                subtaskTable.put(id, subtask);
                if (subtask.getSubtaskStatus().equals(Status.NEW)) {
                    subtask.setSubtaskStatus(Status.IN_PROGRESS);
                    mapStatusSubtask.put(id, Status.IN_PROGRESS);
                } else if (subtask.getSubtaskStatus().equals(Status.IN_PROGRESS)) {
                    subtask.setSubtaskStatus(Status.DONE);
                    mapStatusSubtask.put(id, Status.DONE);
                }
            }
        }
        if (!check) {
            throw new InvalidValueException("Подзадача удалена или не вводилась");
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
        for (Integer id : subtaskTable.keySet()) {
            if (listOfTasksIdForHistory.contains(id)) {
                listOfTasksIdForHistory.remove(id);
            }
        }
        subtaskTable.clear();
    }

    @Override
    public void outputByIdSubtasks(Integer id) throws InvalidValueException {
        if (!subtaskTable.containsKey(id)) {
            throw new InvalidValueException("Подзадача с данным id удалена или не вводилась");
        } else {
            historyManager.add(subtaskTable.get(id));
            if (listOfTasksIdForHistory.contains(id)){
                listOfTasksIdForHistory.remove(id);
                listOfTasksIdForHistory.add(id);
            } else {
                listOfTasksIdForHistory.add(id);
            }
            System.out.println(subtaskTable.get(id));
        }
    }

    @Override
    public void clearByIdSubtasks(Integer id) throws InvalidValueException {
        if (subtaskTable.containsKey(id)) {
            subtaskTable.remove(id);
            listOfTasksIdForHistory.remove(id);
            System.out.println("Подзадача с id = " + id + " удалена");
        } else {
            throw new InvalidValueException("Подзадача с данным id удалена или не вводилась");
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
                    if(mapStatusSubtask.get(key).equals(Status.IN_PROGRESS) || mapStatusSubtask.get(key).equals(Status.DONE)) {
                        epic.setEpicStatus(Status.IN_PROGRESS);
                        if (mapStatusSubtask.get(key).equals(Status.DONE)) {
                            countDone++;
                        }
                    }
                }
            }
        }
        if (countDone == count) {
            epic.setEpicStatus(Status.DONE);
        }
    }



    @Override
    public List<Task> getHistory() throws InvalidValueException {
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
