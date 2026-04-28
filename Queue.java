public interface Queue<E> extends Iterable<E> {

    public void enqueue(E item);
    public E dequeue();

    public int size();
    public boolean isEmpty();
}
