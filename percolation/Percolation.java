import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final boolean OPEN = true;
    private boolean[][] open;
    private int openCount;
    private final int n;
    private final WeightedQuickUnionUF weightedUF;   // initialized only in the declaration or constructor, use final
    private final WeightedQuickUnionUF backwashUF;   // for backwash

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        open = new boolean[n + 1][n + 1];  // easier to use
        openCount = 0;
        weightedUF = new WeightedQuickUnionUF(n * n + 2);
        backwashUF = new WeightedQuickUnionUF(n * n + 1);
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();

        if (open[row][col] != OPEN) {
            open[row][col] = OPEN;
            openCount++;

            int idx = xyTo1D(row, col);
            if (row > 1 && isOpen(row-1, col)) union(idx, idx - n);
            if (row < n && isOpen(row+1, col)) union(idx, idx + n);
            if (col > 1 && isOpen(row, col-1)) union(idx, idx - 1);
            if (col < n && isOpen(row, col+1)) union(idx, idx + 1);

            if (row == 1) union(idx, 0);
            if (row == n) weightedUF.union(idx, n * n + 1);
        }
    }

    // map from 2D to 1D indices
    private int xyTo1D(int row, int col) {
        return (row - 1) * n + col;
    }

    private void union(int p, int q) {
        weightedUF.union(p, q);
        backwashUF.union(p, q);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return open[row][col];
    }

    // is site (row, col) full? (connected to top)
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return open[row][col] && backwashUF.connected(xyTo1D(row, col), 0);
    }

    // number of board sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedUF.connected(n * n + 1, 0);
    }

    public static void main(String[] args) {
        // test client (optional)
    }
}
































