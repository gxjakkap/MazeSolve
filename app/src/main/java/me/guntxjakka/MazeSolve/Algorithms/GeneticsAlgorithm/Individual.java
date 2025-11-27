package me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm;

public class Individual implements Comparable<Individual> {
    private String moves;
    private double fitness;
    private boolean finished;
    private int cost;

    public Individual(String moves) {
        this.moves = moves;
    }

    @Override
    public int compareTo(Individual o) {
        return Double.compare(this.cost, o.getCost());
    }

    public String getMoves() {
        return this.moves;
    }

    public double getFitness() {
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void copyFrom(Individual src) {
        this.fitness = src.getFitness();
        this.moves = src.getMoves();
        this.finished = src.isFinished();
        this.cost = src.getCost();
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void mutate(double rate) {
        char[] mvarr = this.moves.toCharArray();
        for (int i = 0; i < mvarr.length; i++) {
            if (Math.random() < rate)
                mvarr[i] = GeneticsAlgorithm.MV[(int) (Math.random() * GeneticsAlgorithm.MV.length)];
        }
        this.moves = new String(mvarr);
    }
}