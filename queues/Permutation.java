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

























