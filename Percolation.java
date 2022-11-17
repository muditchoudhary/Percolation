/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid;
    private int[] openBlock;
    private int rowColSize;
    private WeightedQuickUnionUF wQuf;
    private int virtualTop;
    private int virtualBottom;

    private int totalOpenSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be bigger than 0");
        }

        wQuf = new WeightedQuickUnionUF((n * n) + 2);

        virtualTop = n * n;

        virtualBottom = n * n + 1;

        rowColSize = n;

        grid = new int[n + 1][n + 1];
        openBlock = new int[n * n];

        int k = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grid[i][j] = k;
                k++;
            }
        }

    }

    private void openAdjacentBlocks(int block, int[] blockCordinates) {
        if (isOpen(blockCordinates[0], blockCordinates[1])) {
            wQuf.union(block, xyTo1D(blockCordinates[0], blockCordinates[1]));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);
        if (!isOpen(row, col)) {
            // Find which block to open
            int block = xyTo1D(row, col);
            int[] rightNeighbour = { row, col + 1 };
            int[] leftNeighbour = { row, col - 1 };
            int[] bottomNeighbour = { row + 1, col };
            int[] upNeighbour = { row - 1, col };
            // Open that blocks
            openBlock[block] = 1;
            totalOpenSites++;

            if (rowColSize == 1) {
                wQuf.union(currentVirtualTop(), block);
                wQuf.union(currentVirtualBottom(), block);
                return;
            }

            // if the block on the first row
            if (row == 1) {
                wQuf.union(currentVirtualTop(), block);
                // if the block on the first column
                if (col == 1) {
                    openAdjacentBlocks(block, rightNeighbour);
                    openAdjacentBlocks(block, bottomNeighbour);

                }
                // if the block on the last column
                else if (col == rowColSize) {
                    openAdjacentBlocks(block, leftNeighbour);
                    openAdjacentBlocks(block, bottomNeighbour);
                }
                // if the block is on the middle column of the first row
                else {
                    openAdjacentBlocks(block, leftNeighbour);
                    openAdjacentBlocks(block, rightNeighbour);
                    openAdjacentBlocks(block, bottomNeighbour);
                }

            }
            // if the block on the last row
            else if (row == rowColSize) {
                wQuf.union(currentVirtualBottom(), block);
                // if the block on the first column
                if (col == 1) {
                    openAdjacentBlocks(block, rightNeighbour);
                    openAdjacentBlocks(block, upNeighbour);
                }
                // if the block on the last column
                else if (col == rowColSize) {
                    openAdjacentBlocks(block, leftNeighbour);
                    openAdjacentBlocks(block, upNeighbour);
                }
                // if the block is on the middle column of the last row
                else {
                    openAdjacentBlocks(block, leftNeighbour);
                    openAdjacentBlocks(block, rightNeighbour);
                    openAdjacentBlocks(block, upNeighbour);
                }

            }
            // if the block is on the middle row
            else {
                // If the block on the first column
                if (col == 1) {
                    openAdjacentBlocks(block, rightNeighbour);
                    openAdjacentBlocks(block, upNeighbour);
                    openAdjacentBlocks(block, bottomNeighbour);

                }
                // If the block on the last column
                else if (col == rowColSize) {
                    openAdjacentBlocks(block, leftNeighbour);
                    openAdjacentBlocks(block, upNeighbour);
                    openAdjacentBlocks(block, bottomNeighbour);
                }
                // If the column is middle
                else {
                    openAdjacentBlocks(block, leftNeighbour);
                    openAdjacentBlocks(block, rightNeighbour);
                    openAdjacentBlocks(block, upNeighbour);
                    openAdjacentBlocks(block, bottomNeighbour);
                }
            }
        }

    }

    // Convert 2d coordinates to 1d coordinates
    private int xyTo1D(int x, int y) {
        int blockValue = grid[x][y];

        return blockValue;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        int blockValue = xyTo1D(row, col);
        if (openBlock[blockValue] == 1) {
            return true;
        }
        return false;
    }


    private boolean connected(int p, int q) {
        return wQuf.find(p) == wQuf.find(q);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        int blockValue = xyTo1D(row, col);
        if (connected(blockValue, currentVirtualTop())) {
            return true;
        }
        return false;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return totalOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (currentVirtualBottom() == currentVirtualTop()) {
            return true;
        }
        return false;
    }

    // Validate the indices
    private void validate(int row, int col) {
        if ((row < 1 || row > rowColSize) || (col < 1 || col > rowColSize)) {
            throw new IllegalArgumentException(
                    "Row & Column should be greater than 0 or less than N ");
        }
    }

    private int currentVirtualTop() {
        return wQuf.find(virtualTop);
    }

    private int currentVirtualBottom() {
        return wQuf.find(virtualBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(8);
        while (!perc.percolates()) {

            int randomeRow = StdRandom.uniformInt(1, 8 + 1);

            int randomCol = StdRandom.uniformInt(1, 8 + 1);

            if (!perc.isOpen(randomeRow, randomCol)) perc.open(randomeRow, randomCol);

        }
    }
}
