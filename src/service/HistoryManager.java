package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory() throws InvalidValueException;

    void add(Task task);

    void remove(int id);
}
