package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    public List<Task> historyManager = new ArrayList<>();

    @Override
    public List<Task> getHistory(){
        return historyManager;
    }

    @Override
    public List<Task> add(Task task){
        if (historyManager.size() == 10){
            historyManager.remove(0);
        }
        historyManager.add(task);
        return historyManager;
    }
}
