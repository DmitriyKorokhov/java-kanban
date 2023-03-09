package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    Set<Task> getPrioritizedTasks();
    /* В методах я пробрасываю исключения с помощью throws, а обработку веду в блоке try catch в классе Main, поэтому
       прописываю проброс исключения у методов. Так я реализовал исключения
     */
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

    void saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId) throws TimeIntersectionException;

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
}
