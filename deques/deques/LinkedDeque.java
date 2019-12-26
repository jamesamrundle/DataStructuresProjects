package deques;

public class LinkedDeque<T> implements Deque<T> {
    private int size;
    private Node head;
    private Node tail;
    public LinkedDeque() {
        head = tail = null;
        size = 0;
    }

    private class Node {
        T value;
        Node prev;
        Node next;

        Node(T value) {
            this.value = value;
            this.prev = null;
            this.next = null;

        }
    }

    public void addFirst(T item) {
        if (size == 0){
            head = tail = new Node(item);
        }
        else {
            Node newTemp = new Node(item);
            newTemp.next = head;
            head.prev = newTemp;
            head = newTemp;

        }
        size += 1;
        }

    public void addLast(T item) {
        if (size == 0){
            head = tail = new Node(item);
        }
        else {
            Node newTemp = new Node(item);
            newTemp.prev = tail;
            tail.next = newTemp;
            tail = newTemp;

        }
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node toBeRemoved = head;
        if (size == 1){
            head = tail = null;
            toBeRemoved.next = toBeRemoved.prev = null;
        }
        else {

            head = head.next;
            head.prev = null;
            toBeRemoved.next = toBeRemoved.prev = null;


        }
        size -= 1;
        return toBeRemoved.value;

        }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node toBeRemoved = tail;
        if (size == 1){
            head = tail = null;
            toBeRemoved.next = toBeRemoved.prev = null;
        }
        else {

            tail = tail.prev;
            tail.next = null;
            toBeRemoved.prev = toBeRemoved.next = null;


        }
        size -= 1;
        return toBeRemoved.value;
    }

    public T get(int index) {
        if ((index > size) || (index < 0)) {
            return null;
        }
        Node getNode = null;
        for (int i = 0; i <= index; i++){
            if (i == 0){
                getNode = head;
            }
            else {
                getNode = getNode.next;
            }
        }
        return getNode.value;
    }

    public int size() {
        return size;
    }
}
