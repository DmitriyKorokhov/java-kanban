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
                lastNode.next = newNode;
                nodeMap.put(task.getTaskId(), newNode);
        }

        public void removeNode(Node node) {
            final Node next = node.next;
            final Node prev = node.prev;
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

        public List<Task> getTasks() throws InvalidValueException {
            List<Task> historyManager = new ArrayList<>();
            Node currentNode = first;
            if (currentNode == null) {
                throw new InvalidValueException("Список пустой");
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