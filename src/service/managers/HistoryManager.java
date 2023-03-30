package service.managers;

import model.Task;
import service.exception.InvalidValueException;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory() throws InvalidValueException;

    void add(Task task);

    void remove(int id);
}
