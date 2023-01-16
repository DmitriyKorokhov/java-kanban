package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    /* обавил модификатор final, конечная переменная является ссылкой (ее нельзя будет
       повторно привязать к другому объекту), но на содержание объекта, на которое
       указывает эта ссылочная переменная, может быть изменено (удаление или добавление элементов)
    */
    private final HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();
    private final CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();

    private class CustomLinkedList<T> {
        transient Node<Task> first;
        transient Node<Task> last;

        // исправил имена параметров
        void linkLast(Task task) {
            final Node<Task> lastNode = last;
            final Node<Task> newNode = new Node<>(lastNode, task, null);
            last = newNode;
            if (lastNode == null)
                first = newNode;
            else
                lastNode.next = newNode;
                nodeMap.put(task.getTaskId(), newNode);
        }
        // метод removeNode() не должен ничего возвращать
        // в методах outputById... я учитываю, что Задача/ Эпик/ Подзадача может быть удален(а) или вообще не вводилась
        public void removeNode(Node<Task> node) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;
            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                node.prev = null;
            }
            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
            node.item = null;
        }

        public List<Task> getTasks() {
            List<Task> historyManager = new ArrayList<>();
            Node<Task> currentNode = first;
            if (currentNode == null) {
                System.out.println("Список пустой");
            } else {
                while (currentNode != null) {
                    if (historyManager.size() == 10) {
                        historyManager.remove(0);
                        historyManager.add(currentNode.item);
                    } else {
                        historyManager.add(currentNode.item);
                    }
                    historyManager.size();
                    currentNode = currentNode.next;
                }
            }
            return historyManager;
        }
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getTaskId())) {
            customLinkedList.removeNode(nodeMap.get((task.getTaskId())));
            customLinkedList.linkLast(task);
        } else {
            customLinkedList.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(nodeMap.get(id));
    }
}