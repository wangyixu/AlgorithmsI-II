import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 *
     ASSESSMENT SUMMARY

     Compilation:  PASSED
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          FAILED (2 warnings)
     Checkstyle:   PASSED

     Correctness:  68/68 tests passed
     Memory:       10/10 tests passed
     Timing:       159/159 tests passed

     Aggregate score: 100.00%
 *
 * Created by yixu on 2019/8/3.
 */
public class CircularSuffixArray {
    private final char[] text;
    private final Integer[] index;
    private final int n;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        this.text = s.toCharArray();
        this.n = s.length();
        this.index = new Integer[n];
        for (int i = 0; i < n; i++)
            index[i] = i;

        Arrays.sort(index, (o1, o2) -> {
            int idx1 = o1, idx2 = o2, cmp;

            for (int i = 0; i < n; i++) {
                cmp = text[idx1] - text[idx2];
                if (cmp < 0) return -1;
                if (cmp > 0) return +1;

                if (++idx1 == n) idx1 = 0;
                if (++idx2 == n) idx2 = 0;
            }
            return 0;
        });
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n)
            throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        // 11 10 7 0 3 5 8 1 4 6 9 2
        CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < test.length(); i++) {
            StdOut.print(test.index(i) + " ");
        }
    }

}
