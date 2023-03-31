package service.managers;

import model.Epic;
import model.Subtask;
import model.Task;
import service.exception.InvalidValueException;
import service.exception.TimeIntersectionException;

import java.util.*;

public interface TaskManager {
    Set<Task> getPrioritizedTasks();
    
    void saveTask(Task task) throws TimeIntersectionException;

    void updateTask(Task task) throws InvalidValueException, TimeIntersectionException;

    void outputAllTasks() throws InvalidValueException;

    void clearAllTasks();

    void outputByIdTask(Integer id) throws InvalidValueException;

    void clearByIdTask(Integer id) throws InvalidValueException;

    void saveEpic(Epic epic);

    void updateEpic(Epic epic) throws InvalidValueException, TimeIntersectionException;

    void outputAllEpics() throws InvalidValueException;

    void clearAllEpics();

    void outputByIdEpic(Integer id) throws InvalidValueException;

    void clearByIdEpic(Integer id) throws InvalidValueException;

    void saveSubtask(Subtask subtask, int epicId) throws TimeIntersectionException;

    void updateSubtask(Subtask subtask) throws InvalidValueException, TimeIntersectionException;

    void outputAllSubtasks() throws InvalidValueException;

    void clearAllSubtasks();

    void outputByIdSubtasks(Integer id) throws InvalidValueException;

    void clearByIdSubtasks(Integer id) throws InvalidValueException;

    void epicStatus(Epic epic , ArrayList<Integer> epicListId);

    List<Task> getHistory() throws InvalidValueException;

    void removeHistoryById(int id);

    HashMap<Integer, Subtask> getSubtaskTable();

    HashMap<Integer, Epic> getEpicTable();

    HashMap<Integer, Task> getTaskTable();

    HashMap<Integer, ArrayList<Integer>> getMapIdSubtaskByEpic();

    ArrayList<Integer> getListOfTasksIdForHistory();

    HistoryManager getHistoryManager();

    Collection<Task> getListTasks();

    Collection<Epic> getListEpics();

    Collection<Subtask> getListSubtasks();

    List<Subtask> getSubTasksByEpicId(int id) throws InvalidValueException;
}
