/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private int countOpen;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufFullness;
    private int len;
    private int top;
    private int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Given n <= 0");
        }
        grid = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufFullness = new WeightedQuickUnionUF(n * n + 1);
        top = n * n;
        bottom = n * n + 1;
        len = n;
        countOpen = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (checkBounds(row, col)) {
            if (!isOpen(row, col)) {
                grid[row - 1][col - 1] = true;
                countOpen++;
            }
            if (row == 1) {
                uf.union(col - 1, top);
                ufFullness.union(col - 1, top);
            }
            if (row == len) {
                uf.union((row - 1) * len + col - 1, bottom);
            }
            // above
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union((row - 1) * len + col - 1, (row - 2) * len + col - 1);
                ufFullness.union((row - 1) * len + col - 1, (row - 2) * len + col - 1);
            }
            // below
            if (row < len && isOpen(row + 1, col)) {
                uf.union((row - 1) * len + col - 1, row * len + col - 1);
                ufFullness.union((row - 1) * len + col - 1, row * len + col - 1);
            }
            // left
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union((row - 1) * len + col - 1, (row - 1) * len + col - 2);
                ufFullness.union((row - 1) * len + col - 1, (row - 1) * len + col - 2);
            }
            // right
            if (col < len && isOpen(row, col + 1)) {
                uf.union((row - 1) * len + col - 1, (row - 1) * len + col);
                ufFullness.union((row - 1) * len + col - 1, (row - 1) * len + col);
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (checkBounds(row, col)) {
            return grid[row - 1][col - 1];
        }
        throw new IllegalArgumentException();
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (checkBounds(row, col)) {
            return ufFullness.find((row - 1) * len + col - 1) == ufFullness.find(top);
        }
        throw new IllegalArgumentException();
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return countOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return (uf.find(top) == uf.find(bottom));
    }

    private boolean checkBounds(int i, int j) {
        return (i >= 1 && i <= len) && (j >= 1 && j <= len);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 2);
        p.open(2, 2);
        StdOut.println(p.percolates());
        p.open(3, 2);
        StdOut.println(p.percolates());
    }
}
