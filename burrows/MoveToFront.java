import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 *
 * Created by yixu on 2019/8/3.
 */
public class MoveToFront {
    private static final int R = 256;  // length of EXTENDED_ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chars = createEASCIIArray();
        char c, i;
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();

            // move-to-front
            char prev = chars[0], curr;
            for (i = 0; chars[i] != c; i++) {
                curr = chars[i];
                chars[i] = prev;
                prev = curr;
            }
            BinaryStdOut.write(i);
            chars[i] = prev;
            chars[0] = c;
        }
        BinaryStdOut.close();
    }

    // create extended ASCII characters (0x00 to 0xFF)
    private static char[] createEASCIIArray() {
        char[] chars = new char[R];
        for (int i = 0; i < R; i++) {
            chars[i] = (char) i;
        }
        return chars;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = createEASCIIArray();
        char i, c;
        while (!BinaryStdIn.isEmpty()) {
            i = BinaryStdIn.readChar();
            c = chars[i];
            BinaryStdOut.write(c);

            // move-to-front
            for (; i > 0; i--) {
                chars[i] = chars[i - 1];
            }
            chars[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        String arg = args[0];
        if (arg.equals("-")) {
            encode();
        } else if (arg.equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("Unknown argument: " + arg + "\n");
        }
    }

}