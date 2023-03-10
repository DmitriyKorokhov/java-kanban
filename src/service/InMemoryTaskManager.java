package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskId = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final HashMap<Integer, Subtask> subtaskTable = new HashMap<>();
    private final HashMap<Integer, Epic> epicTable = new HashMap<>();
    private final HashMap<Integer, Task> taskTable = new HashMap<>();
    private final HashMap<Integer, ArrayList<Integer>> mapIdSubtaskByEpic = new HashMap<>();
    private final HashMap<Integer, Status> mapStatusSubtask = new HashMap<>();
    private final ArrayList<Integer> listOfTasksIdForHistory = new ArrayList<>();
    Comparator<Task> comparator = (task1, task2) -> {
        if (task1.equals(task2)){
            return 0;
        } else if (task1.getTaskStartTime() != null && task2.getTaskStartTime() != null ) {
            return task1.getTaskStartTime().compareTo(task2.getTaskStartTime());
        } else if (task1.getTaskStartTime() == null && task2.getTaskStartTime() != null) {
            return 1;
        } else {
            return -1;
        }
    };
    private final Set<Task> prioritizedTasks = new TreeSet<>(comparator);

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void checkIntersections(Task task) throws TimeIntersectionException {
        for (Task sortedTask : prioritizedTasks) {
            if ( sortedTask.getTaskStartTime() != null && task.getTaskStartTime() != null &&
                    ( sortedTask.getTaskStartTime().isAfter(task.getTaskStartTime()) &&
                      sortedTask.getTaskEndTime().isBefore(task.getTaskEndTime()) ||
                      sortedTask.getTaskStartTime().isBefore(task.getTaskStartTime()) &&
                      task.getTaskStartTime().isBefore(sortedTask.getTaskEndTime()) ||
                      sortedTask.getTaskStartTime().isAfter(task.getTaskStartTime()) &&
                      sortedTask.getTaskStartTime().isBefore(task.getTaskEndTime()) ||
                      sortedTask.getTaskStartTime() == task.getTaskStartTime() ||
                      sortedTask.getTaskEndTime() == task.getTaskEndTime())
            ) {
                throw new TimeIntersectionException("Задача пересекается по времени с другими задачами");
            }
        }
    }

    @Override
    public void saveTask(Task task) throws TimeIntersectionException {
        checkIntersections(task);
        task.setTaskId(taskId);
        taskTable.put(task.getTaskId(), task);
        prioritizedTasks.add(task);
        this.taskId++;
    }

    @Override
    public void updateTask(Task task) throws InvalidValueException, TimeIntersectionException {
        checkIntersections(task);
        for (Map.Entry<Integer, Task> taskEntry : taskTable.entrySet()) {
            if (taskEntry.getValue().getTaskTitle().equals(task.getTaskTitle())) {
                int id = taskEntry.getKey();
                prioritizedTasks.remove(taskTable.get(id));
                task.setTaskStatus(taskEntry.getValue().getTaskStatus());
                task.setTaskId(id);
                taskTable.put(id, task);
                if (task.getTaskStatus().equals(Status.NEW)) {
                    task.setTaskStatus(Status.IN_PROGRESS);
                } else if (task.getTaskStatus().equals(Status.IN_PROGRESS)) {
                    task.setTaskStatus(Status.DONE);
                }
                prioritizedTasks.add(task);
                return;
            }
        }
        throw new InvalidValueException("Данной задачи не существует");
    }

    @Override
    public void outputAllTasks() throws InvalidValueException {
        if (!taskTable.isEmpty()) {
            for (Task value : taskTable.values()) {
                System.out.println(value);
            }
            return;
        }
        throw new InvalidValueException("Список задач пуст");
    }

    @Override
    public void clearAllTasks() {
        for (Integer id : taskTable.keySet()) {
            if (prioritizedTasks.contains(taskTable.get(id))) {
                prioritizedTasks.remove(taskTable.get(id));
            }
            if (listOfTasksIdForHistory.contains(id)) {
                listOfTasksIdForHistory.remove(id);
            }
        }
        taskTable.clear();
    }

    @Override
    public void outputByIdTask(Integer id) throws InvalidValueException {
        if (taskTable.containsKey(id)) {
            historyManager.add(taskTable.get(id));
            if (listOfTasksIdForHistory.contains(id)) {
                listOfTasksIdForHistory.remove(id);
                listOfTasksIdForHistory.add(id);
            } else {
                listOfTasksIdForHistory.add(id);
            }
            System.out.println(taskTable.get(id));
            return;
        }
        throw new InvalidValueException("Задача с данным id удалена или не вводилась");
    }

    @Override
    public void clearByIdTask(Integer id) throws InvalidValueException {
        if (taskTable.containsKey(id)) {
            prioritizedTasks.remove(taskTable.get(id));
            taskTable.remove(id);
            listOfTasksIdForHistory.remove(id);
            System.out.println("Задача с id = " + id + " удалена");
            return;
        }
        throw new InvalidValueException("Задача с данным id удалена или не вводилась");
    }

    @Override
    public void saveEpic(Epic epic) {
        epic.setTaskId(taskId);
        epicTable.put(epic.getTaskId(), epic);
        this.taskId++;
    }

    @Override
    public void updateEpic(Epic epic) throws InvalidValueException, TimeIntersectionException {
        checkIntersections(epic);
        for (Map.Entry<Integer, Epic> epicEntry : epicTable.entrySet()) {
            if (epicEntry.getValue().getTaskTitle().equals(epic.getTaskTitle())) {
                int id = epicEntry.getKey();
                epic.setEpicStatus(epicEntry.getValue().getEpicStatus());
                epic.setTaskId(id);
                epicTable.put(id, epic);
                return;
            }
        }
        throw new InvalidValueException("Данного эпика не существует");
    }

    @Override
    public void outputAllEpics() throws InvalidValueException {
        if (!epicTable.isEmpty()) {
            for (Epic value : epicTable.values()) {
                System.out.println(value);
            }
            return;
        }
        throw new InvalidValueException("Список эпиков пуст");
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
        if (epicTable.containsKey(id)) {
            historyManager.add(epicTable.get(id));
            if (listOfTasksIdForHistory.contains(id)){
                listOfTasksIdForHistory.remove(id);
                listOfTasksIdForHistory.add(id);
            } else {
                listOfTasksIdForHistory.add(id);
            }
            System.out.println(epicTable.get(id));
            return;
        }
        throw new InvalidValueException("Эпик с данным id удален или не вводился");
    }

    @Override
    public void clearByIdEpic(Integer id) throws InvalidValueException {
        if (epicTable.containsKey(id)) {
            epicTable.remove(id);
            listOfTasksIdForHistory.remove(id);
            System.out.println("Эпик с id = " + id + " удален");
            if (mapIdSubtaskByEpic.containsKey(id)) {
                ArrayList<Integer> listOfIdSubtask = mapIdSubtaskByEpic.get(id);
                for (Integer idSubtask : listOfIdSubtask) {
                    subtaskTable.remove(idSubtask);
                }
            }
            return;
        }
        throw new InvalidValueException("Эпик с данным id удалена или не вводилась");
    }

    @Override
    public void saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId) throws TimeIntersectionException {
        checkIntersections(subtask);
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
    public void updateSubtask(Subtask subtask) throws InvalidValueException, TimeIntersectionException {
        int epicId = 0;
        checkIntersections(subtask);
        for (Map.Entry<Integer, Subtask> subtaskEntry : subtaskTable.entrySet()) {
            if (subtaskEntry.getValue().getTaskTitle().equals(subtask.getTaskTitle())) {
                int id = subtaskEntry.getKey();
                prioritizedTasks.remove(subtaskTable.get(id));
                for (Map.Entry<Integer, ArrayList<Integer>> entry : mapIdSubtaskByEpic.entrySet()) {
                    ArrayList<Integer> subtasksListId = entry.getValue();
                    for (Integer subtaskId : subtasksListId) {
                        if (subtaskId.equals(id)) {
                            epicId = entry.getKey();
                        }
                    }
                }
                Epic epic = epicTable.get(epicId);
                subtask.setSubtaskStatus(subtaskEntry.getValue().getSubtaskStatus());
                subtask.setTaskId(id);
                subtaskTable.put(id, subtask);
                if (subtask.getSubtaskStatus().equals(Status.NEW)) {
                    subtask.setSubtaskStatus(Status.IN_PROGRESS);
                    mapStatusSubtask.put(id, Status.IN_PROGRESS);
                } else if (subtask.getSubtaskStatus().equals(Status.IN_PROGRESS)) {
                    subtask.setSubtaskStatus(Status.DONE);
                    mapStatusSubtask.put(id, Status.DONE);
                }
                prioritizedTasks.add(subtask);
                LocalDateTime startTime = LocalDateTime.MAX;
                LocalDateTime endTime = LocalDateTime.MIN;
                for (Map.Entry<Integer, Subtask> subtaskEntryForTime : subtaskTable.entrySet()) {
                    if (subtaskEntryForTime.getValue().getTaskStartTime() != null) {
                        if (subtaskEntryForTime.getValue().getTaskStartTime().isBefore(startTime)) {
                            startTime = subtaskEntryForTime.getValue().getTaskStartTime();
                            epic.setTaskStartTime(startTime);
                        }
                        if (subtaskEntryForTime.getValue().getTaskEndTime().isAfter(endTime)) {
                            endTime = subtaskEntryForTime.getValue().getTaskEndTime();
                            epic.setTaskEndTime(endTime);
                        }
                    }
                }
                epicTable.put(epicId, epic);
                return;
            }
        }
        throw new InvalidValueException("Данной подзадачи не существует");
    }

    @Override
    public void outputAllSubtasks() throws InvalidValueException {
        if (!subtaskTable.isEmpty()) {
            for (Subtask value : subtaskTable.values()) {
                System.out.println(value);
            }
            return;
        }
        throw new InvalidValueException("Список подзадач пуст или не вводился");
    }

    @Override
    public void clearAllSubtasks() {
        for (Integer id : subtaskTable.keySet()) {
            if (listOfTasksIdForHistory.contains(id)) {
                listOfTasksIdForHistory.remove(id);
            }
            if (prioritizedTasks.contains(subtaskTable.get(id))) {
                prioritizedTasks.remove(subtaskTable.get(id));
            }
        }
        subtaskTable.clear();
    }

    @Override
    public void outputByIdSubtasks(Integer id) throws InvalidValueException {
        if (subtaskTable.containsKey(id)) {
            historyManager.add(subtaskTable.get(id));
            if (listOfTasksIdForHistory.contains(id)){
                listOfTasksIdForHistory.remove(id);
                listOfTasksIdForHistory.add(id);
            } else {
                listOfTasksIdForHistory.add(id);
            }
            System.out.println(subtaskTable.get(id));
            return;
        }
        throw new InvalidValueException("Подзадача с данным id удалена или не вводилась");
    }

    @Override
    public void clearByIdSubtasks(Integer id) throws InvalidValueException {
        if (subtaskTable.containsKey(id)) {
            prioritizedTasks.remove(subtaskTable.get(id));
            subtaskTable.remove(id);
            listOfTasksIdForHistory.remove(id);
            System.out.println("Подзадача с id = " + id + " удалена");
            return;
        }
        throw new InvalidValueException("Подзадача с данным id удалена или не вводилась");
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
    public List<Task> getHistory() throws InvalidValueException{
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
