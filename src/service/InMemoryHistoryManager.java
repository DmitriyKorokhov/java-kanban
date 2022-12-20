package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task> historyManager = new ArrayList<>();

    @Override
    public List<Task> getHistory(){
        return historyManager;
    }

    @Override
    public void add(Task task){
        if (historyManager.size() == 10){
            historyManager.remove(0);
        }
        historyManager.add(task);
    }
}
