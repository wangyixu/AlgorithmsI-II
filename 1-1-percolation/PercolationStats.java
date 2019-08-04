import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *
     ASSESSMENT SUMMARY

     Compilation:  PASSED
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          PASSED
     Checkstyle:   PASSED

     Correctness:  30/30 tests passed
     Memory:       8/8 tests passed
     Timing:       20/20 tests passed

     Aggregate score: 100.00%
 *
 *  Created by yixu on 2017/1/7, edited on 2018/8/27.
 *
 * */
public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final double mean;
    private final double stddev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.trials = trials;

        double[] percolates = new double[trials];
        Percolation p;
        for (int i = 0; i < trials; i++) {
            p = new Percolation(n);

            int row, col;
            while (!p.percolates()) {
                do {
                    row = 1 + StdRandom.uniform(n);
                    col = 1 + StdRandom.uniform(n);
                } while (p.isOpen(row, col));

                p.open(row, col); // open a blocked site.
            }
            percolates[i] = (double) p.numberOfOpenSites() / (n * n);
        }

        mean = StdStats.mean(percolates);
        stddev = StdStats.stddev(percolates);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + test.mean());
        StdOut.println("stddev                  = " + test.stddev());
        StdOut.println("95% confidence interval = " + test.confidenceLo() +", "+ test.confidenceHi());
    }
}