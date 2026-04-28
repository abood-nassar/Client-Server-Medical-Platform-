
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue_linkedlist<E> implements Queue<E>, Serializable {

    private class Node implements Serializable {

        private E item;
        private Node next;

        public Node(E item) {
            this.item = item;
            this.next = null;
        }
    }

    private Node head, tail;
    private int n;

    public Queue_linkedlist() {
        head = null;
        tail = null;
        n = 0;
    }

    @Override
    public void enqueue(E item) { // O(1)
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            head = new Node(item);
            tail = head;
        } else {
            Node oldtail = tail;
            tail = new Node(item);
            oldtail.next = tail;
        }
        n++;
    }

    @Override
    public E dequeue() { // O(1)
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E tmp = head.item;
        head = head.next;
        n--;
        if (head == null) {
            tail = null;
        }
        return tmp;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<E> {

        private Node x = head;

        @Override
        public boolean hasNext() {
            return x != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E item = x.item;
            x = x.next;
            return item;
        }
    }

    public E readAndRemove(int index) { // O(n)
        if (index < 0 || index >= n) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + n);
        }

        // Special case: Removing the head
        if (index == 0) {
            return dequeue(); // Dequeue already handles removing the head
        }

        // Traverse to the node at index - 1
        Node previous = null;
        Node current = head;
        for (int i = 0; i < index; i++) {
            previous = current;
            current = current.next;
        }

        // `current` is the target node
        // Remove it from the queue
        previous.next = current.next;

        // If the removed node is the tail, update the tail reference
        if (current == tail) {
            tail = previous;
        }

        n--; // Decrease the size of the queue
        return current.item;
    }
    public E get(int index) {
    if (index < 0 || index >= n) {
        throw new IndexOutOfBoundsException("Invalid index: " + index + ". Size of the queue is: " + n);
    }

    Node current = head;
    for (int i = 0; i < index; i++) {
        current = current.next;
    }

    return current.item;
}


}
