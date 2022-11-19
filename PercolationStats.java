/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform independent trials on an n-by-n grid

    private double[] totalOpenSites;
    private int totalTrials;

    public PercolationStats(int n, int trials) {
        validate(n, trials);

        double openSite;
        totalTrials = trials;
        totalOpenSites = new double[trials];

        // In in = new In("input20.txt");      // input file
        // int num = in.readInt();         // n-by-n percolation system

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {

                int randomeRow = StdRandom.uniformInt(1, n + 1);

                int randomCol = StdRandom.uniformInt(1, n + 1);

                if (!perc.isOpen(randomeRow, randomCol)) perc.open(randomeRow, randomCol);

            }
            // while (!in.isEmpty() && !perc.percolates()) {
            //     int k = in.readInt();
            //     int j = in.readInt();
            //     perc.open(k, j);
            // }
            openSite = (double) perc.numberOfOpenSites() / (double) (n * n);
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

    private double[] getTotalOpenSites() {
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
