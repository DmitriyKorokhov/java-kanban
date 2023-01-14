package service;

import model.Task;

public class Node<E> {
    Task item;
    Node<Task> next;
    Node<Task> prev;

    Node(Node<Task> prev, Task element, Node<Task> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}