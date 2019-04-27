import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int n;            // number of elements on queue

    // construct an empty queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;

        // alternative implementation
        // a = java.util.Arrays.copyOf(a, capacity);
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2*a.length);
        a[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) throw new NoSuchElementException();
        int idx = StdRandom.uniform(n);
        Item item = a[idx];
        if (idx != n-1) a[idx] = a[n-1];
        a[--n] = null;
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
    }

    // return a random item
    public Item sample() {
        if (n == 0) throw new NoSuchElementException();
        int r = StdRandom.uniform(n);
        return a[r];
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private final Item[] ite;

        public RandomizedQueueIterator() {
            ite = (Item[]) new Object[n];
            System.arraycopy(a, 0, ite, 0, n);
            StdRandom.shuffle(ite);
        }
        public boolean hasNext()  { return i < n;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return ite[i++];
        }
    }
}






































