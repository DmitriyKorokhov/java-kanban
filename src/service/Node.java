package service;

import model.Task;

public class Node {
    Task item;
    Node next;
    Node prev;
    // из Вашего комментария понял, что нет смысла использовть дженерики
    Node(Node prev, Task element, Node next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}