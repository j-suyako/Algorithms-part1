/******************************************************************************
 *  Author:         suyako
 *  Written:        9/22/2017
 *  Last updated:   2/13/2018
 *
 *  Compilation:    javac PercolationStats.java
 *  Execution:      java PercolationStats
 *  Dependencies:   StdOut.java StdRandom.java
 *
 *  Build a series of percolation calculation experiments
 *
 *  % java PercolationStats
 *  Build 100 200-by-200 Percolation models, print the average percolation
 *  threshold, the standard deviation of percolation threshold, low endpoint,
 *  high endpoint.
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

//    private Percolation perc;  // percolation model
    private double[] Times;    // matrix to store the outcome of each experiment
    private int size;          // number of trials
    private double mean;
    private double dev;

    /**
     * Initializes a series of computational percolation experiments
     * @param n size of percolation model
     * @param trials number of trials
     * @throws IllegalArgumentException if {@code n} or {@code trials} less than or equal to 0
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Row index n or trials less than or equal to 0.");
        size = trials;
        Times = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);  // create a new Percolation object each trial
            // open site randomly if perc is not percolated
            while (!perc.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (perc.isOpen(row, col))
                    continue;
                else
                    perc.open(row, col);
            }
            double temp = perc.numberOfOpenSites();
            Times[i] = temp / (n * n);
        }
        mean = StdStats.mean(Times);
        dev = StdStats.stddev(Times);
    }

    // calculate the average
    public double mean() {
        return mean;
    }

    // calculate the standard deviation
    public double stddev() {
        return dev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - 1.96 * dev / Math.sqrt(size);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + 1.96 * dev / Math.sqrt(size);
    }

    public static void main(String[] args) {
        PercolationStats percstate = new PercolationStats(200, 100);
        StdOut.println("mean                   " + " = "+ percstate.mean());
        StdOut.println("stddev                 " + " = " + percstate.stddev());
        StdOut.println("95% confidence interval" + " = " + "[" + percstate.confidenceLo() + ", " + percstate.confidenceHi() + "]");
    }
}
