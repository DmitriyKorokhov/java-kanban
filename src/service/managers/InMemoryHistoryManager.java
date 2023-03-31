package service.managers;

import model.Task;
import service.exception.InvalidValueException;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private final CustomLinkedList customLinkedList = new CustomLinkedList();

    private class CustomLinkedList {
        transient Node first;
        transient Node last;

        void linkLast(Task task) {
            final Node lastNode = last;
            final Node newNode = new Node(lastNode, task, null);
            last = newNode;
            if (lastNode == null)
                first = newNode;
            else
                lastNode.setNext(newNode);
                nodeMap.put(task.getTaskId(), newNode);
        }

        public void removeNode(Node node) {
            final Node next = node.getNext();
            final Node prev = node.getPrev();
            if (prev == null) {
                first = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }
            if (next == null) {
                last = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }
            node.setItem(null);
        }

        public List<Task> getTasks() throws InvalidValueException {
            List<Task> historyManager = new ArrayList<>();
            Node currentNode = first;
            if (currentNode == null) {
                throw new InvalidValueException("Список пустой");
            } else {
                while (currentNode != null) {
                    if (historyManager.size() == 10) {
                        historyManager.remove(0);
                        historyManager.add(currentNode.getItem());
                    } else {
                        historyManager.add(currentNode.getItem());
                    }
                    historyManager.size();
                    currentNode = currentNode.getNext();
                }
            }
            return historyManager;
        }
    }

    @Override
    public List<Task> getHistory() throws InvalidValueException {
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