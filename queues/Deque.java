import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] a;       // queue elements
    private int n;          // number of elements on queue
    private int first;      // index of first element of queue
    private int last;       // index of next available slot

    // construct an empty deque
    public Deque() {
        a = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[(first + i) % a.length];
        }
        a = temp;
        first = 0;
        last  = n;
    }

    // insert the item at the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2*a.length);
        if (first == 0) first = a.length;           // wrap-around
        a[--first] = item;
        n++;
    }

    // insert the item at the end
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == a.length) resize(2*a.length);  // double size of array if necessary and recopy to front of array
        if (n == 0) first = last;   // first should reference to first element
        a[last++] = item;
        if (last == a.length) last = 0;           // wrap-around
        n++;
    }

    // delete and return the item at the front
    public Item removeFirst() {
        if (n == 0) throw new NoSuchElementException();
        Item item = a[first];
        a[first++] = null;
        n--;
        if (first == a.length) first = 0;           // wrap-around
        if (n > 0 && n == a.length/4) resize(a.length/2);   // shrink size of array if necessary
        return item;
    }

    // delete and return the item at the end
    public Item removeLast() {
        if (n == 0) throw new NoSuchElementException();
        if (last == 0) last = a.length;           // wrap-around
        Item item = a[--last];
        a[last] = null;
        n--;
        if (n > 0 && n == a.length/4) resize(a.length/2);
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()  { return i < n;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = a[(i + first) % a.length];
            i++;
            return item;
        }
    }
}



















