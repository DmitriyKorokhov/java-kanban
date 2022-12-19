package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int saveTask(Task task);

    void updateTask(Task task);

    void outputAllTasks();

    void clearAllTasks();

    Task outputByIdTask(Integer id);

    Task clearByIdTask(Integer id);

    int saveEpic(Epic epic);

    void updateEpic(Epic epic);

    void outputAllEpics();

    void clearAllEpics();

    Epic outputByIdEpic(Integer id);

    Epic clearByIdEpic(Integer id);

    int saveSubtask(Subtask subtask, Epic epic, ArrayList<Integer> epicListId);

    void updateSubtask(Subtask subtask);

    void outputAllSubtasks();

    void clearAllSubtasks();

    Subtask outputByIdSubtasks(Integer id);

    Subtask clearByIdSubtasks(Integer id);

    void SubtaskByEpic(ArrayList<Integer> epicListId);

    void epicStatus(Epic epic , ArrayList<Integer> epicListId);

    List<Task> getHistory();
}
