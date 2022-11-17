/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform independent trials on an n-by-n grid

    private int[] totalOpenSites;
    private int totalTrials;

    public PercolationStats(int n, int trials) {
        validate(n, trials);

        int openSite;
        totalTrials = trials;
        totalOpenSites = new int[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {

                int randomeRow = StdRandom.uniformInt(1, n + 1);

                int randomCol = StdRandom.uniformInt(1, n + 1);

                if (!perc.isOpen(randomeRow, randomCol)) perc.open(randomeRow, randomCol);

            }
            openSite = perc.numberOfOpenSites();
            totalOpenSites[i] = openSite;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(getTotalOpenSites());
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(getTotalOpenSites());

    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double stddev = stddev();
        double rootTrials = Math.sqrt(totalTrials);


        return mean - ((1.96 * stddev) / rootTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double stddev = stddev();
        double rootTrials = Math.sqrt(totalTrials);


        return mean + ((1.96 * stddev) / rootTrials);
    }

    // validate the inputs
    private void validate(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and trials must be greater than 0!");
        }
    }

    private int[] getTotalOpenSites() {
        return totalOpenSites;
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]),
                                                          Integer.parseInt(args[1]));

        System.out.println("mean = " + percStats.mean());

        System.out.println("stddev = " + percStats.stddev());

        double confidenceHi = percStats.confidenceHi();

        double confidenceLow = percStats.confidenceLo();

        double[] confidenceIntrvl = { confidenceLow, confidenceHi };

        System.out.println(
                "95% confidence interval = " + "[" + confidenceIntrvl[0] + ", "
                        + confidenceIntrvl[1]
                        + "]");


    }
}
