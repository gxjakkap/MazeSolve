package me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm;

import java.util.ArrayDeque;
import java.util.Queue;

public class GAStandardDeviation {
    private static final int MAX_SIZE = 1000;
    private Queue<Double> fitQ = new ArrayDeque<>();

    public void addFiness(Double fitness) {
        this.fitQ.add(fitness);

        if (this.fitQ.size() > MAX_SIZE) {
            this.fitQ.poll();
        }
    }

    public double getSD() {
        if (this.fitQ.isEmpty())
            return 0.0;
        double sum = 0.0;
        for (double x : this.fitQ) {
            sum += x;
        }
        double xb = sum / (this.fitQ.size());
        double sd = 0.0;
        for (double x : this.fitQ) {
            sd += Math.pow((x - xb), 2);
        }

        sd = Math.sqrt(sd / (this.fitQ.size() - 1));
        // System.out.println(String.format("SD: %f", sd));
        return sd;
    }

    public boolean isConverged() {
        if (this.fitQ.size() < MAX_SIZE)
            return false;
        return (this.getSD() < 0.00001);
    }
}
