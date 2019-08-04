import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

/**
 *
 *
     ASSESSMENT SUMMARY

     Compilation:  PASSED
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          PASSED
     Checkstyle:   PASSED

     Correctness:  13/13 tests passed
     Memory:       3/3 tests passed
     Timing:       9/9 tests passed

     Aggregate score: 100.00%
 
 *
 * Created by yixu on 2019/7/31.
 */
public class BoggleSolver {

    private static final int R = 26;        // A-Z

    private final Node root = new Node();      // root of trie

    // R-way trie node
    private static class Node {
        private int val;
        private final Node[] next = new Node[R];
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        Node curr;
        for (String word : dictionary) {
            curr = root;
            int idx;
            for (int i = 0; i < word.length(); i++) {
                idx = word.charAt(i) - 'A';
                if (curr.next[idx] == null)
                    curr.next[idx] = new Node();
                curr = curr.next[idx];
            }
            curr.val = points(word);
        }
    }

    private int points(String s) {
        int len = s.length();

        if      (len < 3)  return 0;
        else if (len <= 4) return 1;
        else if (len <= 5) return 2;
        else if (len <= 6) return 3;
        else if (len <= 7) return 5;
        else               return 11;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> res = new HashSet<>();
        int m = board.rows(), n = board.cols();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(board, res, i, j, root, new boolean[m][n], new StringBuilder());
            }
        }
        return res;
    }

    private void dfs(BoggleBoard board, HashSet<String> res, int i, int j,
                     Node prev, boolean[][] visited, StringBuilder sb) {
        if (i < 0 || i == board.rows() || j < 0 || j == board.cols() || visited[i][j])
            return;

        boolean isQu = board.getLetter(i, j) == 'Q';

        // get curr node
        Node curr = prev.next[board.getLetter(i, j) - 'A'];
        if (curr == null)
            return;
        if (isQu) {
            curr = curr.next['U' - 'A'];
            if (curr == null)
                return;
        }

        if (isQu)
            sb.append("QU");
        else
            sb.append(board.getLetter(i, j));
        visited[i][j] = true;

        // add word
        if (curr.val != 0) {
            res.add(new String(sb));
        }

        dfs(board, res, i-1, j, curr, visited, sb);
        dfs(board, res, i+1, j, curr, visited, sb);
        dfs(board, res, i, j-1, curr, visited, sb);
        dfs(board, res, i, j+1, curr, visited, sb);
        dfs(board, res, i-1, j-1, curr, visited, sb);
        dfs(board, res, i+1, j-1, curr, visited, sb);
        dfs(board, res, i-1, j+1, curr, visited, sb);
        dfs(board, res, i+1, j+1, curr, visited, sb);

        if (isQu)
            sb.delete(sb.length() - 2, sb.length());
        else
            sb.deleteCharAt(sb.length() - 1);
        visited[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        Node curr = root;
        int idx;
        for (int i = 0; i < word.length(); i++) {
            idx = word.charAt(i) - 'A';
            if (curr.next[idx] == null)
                return 0;
            curr = curr.next[idx];
        }
        return curr.val;
    }


    public static void main(String[] args) {
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-q.txt");
//        BoggleBoard board = new BoggleBoard("board4x4.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word + " -- " + solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
