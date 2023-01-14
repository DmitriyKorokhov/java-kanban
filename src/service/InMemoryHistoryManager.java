package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private HashMap<Integer, Node<Task>> nodeMap = new HashMap<>();
    CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();
    private class CustomLinkedList<T> {
        transient Node<Task> first;
        transient Node<Task> last;

        void linkLast(Task task) {
            final Node<Task> l = last;
            final Node<Task> newNode = new Node<>(l, task, null);
            last = newNode;
            if (l == null)
                first = newNode;
            else
                l.next = newNode;
                nodeMap.put(task.getTaskId(), newNode);
        }

        public Task removeNode(Node<Task> x) {
            final Task element = x.item;
            final Node<Task> next = x.next;
            final Node<Task> prev = x.prev;
            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                x.prev = null;
            }
            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }
            x.item = null;
            return element;
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
    public List<Task> getHistory(){
        return customLinkedList.getTasks();
    }

    @Override
    public void add(Task task){
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