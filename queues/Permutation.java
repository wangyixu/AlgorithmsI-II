import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *
     Compilation:  PASSED (0 errors, 5 warnings)
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          PASSED
     Checkstyle:   PASSED

     Correctness:  43/43 tests passed
     Memory:       104/105 tests passed
     Timing:       193/193 tests passed

     Aggregate score: 99.90%

     Memory:
        Test 3 (bonus): check that maximum size of any or Deque or RandomizedQueue object
            created is equal to k
        Test 7: Worst-case constant memory allocated or deallocated per deque operation?
         - when current size of Deque was 16 objects;
         - the call to addFirst()
         - caused a change in memory of 128 bytes
         - any change of more than 96 bytes fails the test
 *
 * Created by yixu on 2017/1/7, edited on 2018/8/28.
 */
public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}

























