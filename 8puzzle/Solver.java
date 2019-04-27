import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 *
     Compilation:  PASSED
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          PASSED
     Checkstyle:   PASSED

     Correctness:  49/49 tests passed
     Memory:       22/22 tests passed
     Timing:       116/125 tests passed

     Aggregate score: 98.56%
 *
 * Created by yixu on 2017/1/11, edited on 2018/9/3
 * */
public class Solver {
    private SearchNode node;
    private final boolean isSolvable;
    private final int     solveMoves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        MinPQ<SearchNode> minPQ = new MinPQ<>();
        minPQ.insert(new SearchNode(null, initial,  0,  false));
        minPQ.insert(new SearchNode(null, initial.twin(), 0, true));    // add twin board to get solvable

        while (true) {  // minPQ will not be empty
            node = minPQ.delMin();
            if (node.board.isGoal())
                break;

            for (Board board : node.board.neighbors()) {
                if (node.prev == null || !board.equals(node.prev.board)) {
                    minPQ.insert(new SearchNode(node, board, node.moves + 1, node.isTwin));
                }
            }
        }

        isSolvable = !node.isTwin;
        solveMoves = node.isTwin ? -1 : node.moves;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private  final SearchNode prev;
        private  final Board board;
        private  final int moves;
        private  final int priority;
        private  final boolean isTwin;

        public SearchNode(SearchNode prev, Board board, int moves, boolean isTwin) {
            this.prev = prev;
            this.board = board;  
            this.moves = moves;  
            this.priority = board.manhattan() + moves;
            this.isTwin = isTwin; 
        }

        public int compareTo(SearchNode that) {
            /*
            if (this.priority > that.priority) return 1;  
            if (this.priority < that.priority) return -1;  
            return 0;
            */
            return this.priority - that.priority;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solveMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        Stack<Board> stack = new Stack<>();
        SearchNode curr = node;
        while (curr != null) {
            stack.push(curr.board);
            curr = curr.prev;
        }
        return stack;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
 
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }    
    }
}