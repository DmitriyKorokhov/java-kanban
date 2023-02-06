package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    void saveTask(Task task);

    void updateTask(Task task);

    void outputAllTasks();

    void clearAllTasks();

    void outputByIdTask(Integer id);

    void clearByIdTask(Integer id);

    void saveEpic(Epic epic);

    void updateEpic(Epic epic);

    void outputAllEpics();

    void clearAllEpics();

    void outputByIdEpic(Integer id);

    void clearByIdEpic(Integer id);

    void saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId);

    void updateSubtask(Subtask subtask);

    void outputAllSubtasks();

    void clearAllSubtasks();

    void outputByIdSubtasks(Integer id);

    void clearByIdSubtasks(Integer id);

    void SubtaskByEpic(ArrayList<Integer> epicListId);

    void epicStatus(Epic epic , ArrayList<Integer> epicListId);

    List<Task> getHistory();

    void removeHistoryById(int id);

    HashMap<Integer, Subtask> getSubtaskTable();

    HashMap<Integer, Epic> getEpicTable();

    HashMap<Integer, Task> getTaskTable();

    HashMap<Integer, ArrayList<Integer>> getMapIdSubtaskByEpic();

    ArrayList<Integer> getListOfTasksIdForHistory();

    HistoryManager getHistoryManager();
}
