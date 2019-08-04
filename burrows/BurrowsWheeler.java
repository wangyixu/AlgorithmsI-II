import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 *
 * Created by yixu on 2019/8/3.
 */
public class BurrowsWheeler {
    private static final int R = 256; // Radix

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
//        String s = "ABRACADABRA!";    // ARD!RCAAAABB

        CircularSuffixArray csa = new CircularSuffixArray(s);
        int first = 0, n = s.length();
        while (csa.index(first) != 0) {
            first++;
        }

        BinaryStdOut.write(first);
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) + n - 1) % n));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
//        int first = 3;
//        String t = "ARD!RCAAAABB";  // ABRACADABRA!

        int n = t.length();

        // constructing the next[] array from t[] and first
        int[] count = new int[R + 1], next = new int[n];
        for (int i = 0; i < n; i++)
            count[t.charAt(i) + 1]++;
        for (int i = 1; i < R + 1; i++)
            count[i] += count[i - 1];
        for (int i = 0; i < n; i++)
            next[count[t.charAt(i)]++] = i;

        // reconstruct the original input string from next[] and first
        int idx = first;
        for (int i = 0; i < n; i++) {
            idx = next[idx];
            BinaryStdOut.write(t.charAt(idx));
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
//        transform();
//        inverseTransform();
        String arg = args[0];
        if (arg.equals("-")) {
            transform();
        } else if (arg.equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException("Unknown argument: " + arg + "\n");
        }
    }
}
