import java.util.LinkedList;
import java.util.Queue;

public class Board {
    private final int n;
    private int zeroRow;
    private int zeroCol;
    private final int[][] board;

    // construct a board from an n-by-n array of blocks
    public Board(int[][] blocks) {
        n = blocks.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
                board[i][j] = blocks[i][j];
            }
        }
    }

    // board dimension n
    public int dimension()  {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int hamm = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 0 && board[i][j] != i * n + j+1)
                    hamm++;
            }
        }
        return hamm;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manh = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)    {
                int value = board[i][j];
                if (value != 0) {
                    int x = (value - 1) / n;
                    int y = (value - 1) %  n;
                    manh += Math.abs(i-x) + Math.abs(j-y);
                }
            }
        }
        return manh;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0 || this.manhattan() == 0;
    }

    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int row = 0;
        if (zeroRow == 0) row = 1;

        return swapToGetNewBoard(board, row, 0, row, 1);
    }

    private Board swapToGetNewBoard(int[][] a, int x1, int y1, int x2, int y2) {
        swap(a, x1, y1, x2, y2);
        Board newBoard = new Board(a);
        swap(a, x1, y1, x2, y2);
        return newBoard;
    }

    private void swap(int[][] matrix, int x1, int y1, int x2, int y2) {
        int temp       = matrix[x1][y1];
        matrix[x1][y1] = matrix[x2][y2];
        matrix[x2][y2] = temp;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        if (that.n != this.n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != that.board[i][j])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new LinkedList<>();

        int[][] dxy = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dxy) {
            int x = zeroRow + d[0];
            int y = zeroCol + d[1];
            if (x < 0 || x >= n || y < 0 || y >= n)
                continue;

            Board neighbor = swapToGetNewBoard(board, zeroRow, zeroCol, x, y);
            neighbors.add(neighbor);
        }

        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}