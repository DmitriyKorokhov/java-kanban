package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    /* К Вашему комментарию: Исправил модификатор доступа.
    Использование ArrayList<>() меня навело то, что в ТЗ прописано: метод getHistory() использует список.
     */
    private List<Task> historyManager = new ArrayList<>();

    /* К Вашему комментарию про null: В class InMemoryTaskManager прописал в методах по вызову Задчи, Подзадачи, Эпика по id:
       что выводится, к примеру, "Задачи по данному id не существует" (в истории это тоже отобразится).
     */
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
