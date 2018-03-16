/******************************************************************************
 *  Author:         suyako
 *  Written:        9/22/2017
 *  Last updated:   2/13/2018
 *
 *  Compilation:    javac Percolation.java
 *  Execution:      java Percolation
 *
 *  Build a percolation class
 *
 *  % java Percolation
 *  Build a 10-by-10 Percolation class, and open site(1, 5), site(2, 6), then
 *  return the number of open sites.
 ******************************************************************************/

import java.lang.IllegalArgumentException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF UF1;  //  weighted quick-union find data structure used in judge Full
    private WeightedQuickUnionUF UF2;  //  weighted quick-union find data structure used in judge percolated
    private int opened;                //  number of opened sites
    private int size;                  //  size of grid
    private int[] condition;           //  matrix store the status of all sites, where 0 is block, 1 is open

    /**
     * Initializes an percolation model which has {@code N}-by-{@code N} blocked grid and one opened virtual site
     * @param N the number of sites
     * @throws IllegalArgumentException if {@code N} <= 0
     */
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N should be more than 0.");  // at least one site required
        UF1 = new WeightedQuickUnionUF(N*N+1);  // additional one block represents virtual site at top
        UF2 = new WeightedQuickUnionUF(N*N+2);  // additional two blocks represent virtual site at top and at bottom
        condition = new int[N*N+2];
        opened = 0;
        size = N;
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++)
                condition[xyTo1D(i, j)] = 0;  // any site in grid is closed
        }
        condition[N*N] = 1;  //  the virtual site is opened
        condition[N*N+1] = 1;
    }

    // expand to one-dimensional vector
    private int xyTo1D(int i, int j) {
        if (i < 1 || i > size || j < 1 || j > size)
            throw new IllegalArgumentException("Argument out bound.");
        return (i - 1) * size + (j - 1);
    }

    // find the site near by the target site
    private int[] nearby(int flag, int i, int j) {
        int[] res = new int[4];

        // res[0] is the left of target site, duplicate itself if it doesn't exist
        if (j > 1)
            res[0] = xyTo1D(i, j-1);
        else
            res[0] = xyTo1D(i, j);

        // res[1] is the right of target site, duplicate itself if it doesn't exist
        if (j < size)
            res[1] = xyTo1D(i, j+1);
        else
            res[1] = xyTo1D(i, j);

        // res[2] is the top of target site, duplicate itself if it doesn't exist
        if (i > 1)
            res[2] = xyTo1D(i-1, j);
        else
            res[2] = size * size;

        // res[3] is the bottom of target site, duplicate itself if it doesn't exist
        if (i < size)
            res[3] = xyTo1D(i+1, j);
        else {
            if (flag == 1)       // UF1
                res[3] = xyTo1D(i, j);
            else if (flag == 2)  // UF2
                res[3] = size * size + 1;
        }
        return res;
    }

    // open site (row, col) if it is not opened
    public void open(int i, int j) {
        if (isOpen(i, j)) return;
        int location = xyTo1D(i, j);
        condition[location] = 1;
        opened++;
        int[] nearby1 = nearby(1, i, j);
        int[] nearby2 = nearby(2, i, j);
        for (int k : nearby1) {
            if (condition[k]==0) continue;                  //  if nearby site is closed, continue
            else if (UF1.connected(location, k)) continue;  //  if has connected, continue
            else UF1.union(location, k);                    //  if nearby site is opened, then connect them
        }
        for (int k : nearby2) {
            if (condition[k]==0) continue;
            else if (UF2.connected(location, k)) continue;
            else UF2.union(location, k);
        }
    }

    // is site (row, col) full?
    public boolean isFull(int i, int j) {
        return UF1.connected(xyTo1D(i, j), size*size);  //full site is an open site that can be connected to the top virtual site
    }

    // is site (row, col) open?
    public boolean isOpen(int i, int j) {
        int location = xyTo1D(i, j);
        return condition[location]==1;
    }

    // does the system percolate?
    public boolean percolates() {
        return UF2.connected(size*size, size*size+1);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return opened;
    }

    public static void main(String[] args) {
        Percolation perc = new Percolation(10);
        perc.open(1, 5);
        perc.open(2, 6);
        StdOut.println(perc.numberOfOpenSites());
    }
}
