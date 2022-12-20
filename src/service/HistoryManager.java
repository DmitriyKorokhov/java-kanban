package service;

import model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();
    // К Вашему комментарию: Понял, что незачем возвращать List<Task>
    void add(Task task);

}
