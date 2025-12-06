package me.guntxjakka.MazeSolve.Algorithms;

import java.util.List;

import me.guntxjakka.MazeSolve.Utils.Coordinate;

public class AlgorithmResult {
    private int cost;
    private List<Coordinate> paths;
    private long elapsedTime;

    public AlgorithmResult(int cost, List<Coordinate> paths, long elapsedTime){
        this.cost = cost;
        this.paths = paths;
        this.elapsedTime = elapsedTime;
    }

    public int getCost() {
        return cost;
    }

    public long getElapsedTime(){
        return this.elapsedTime;
    }

    public List<Coordinate> getPaths() {
        return paths;
    }
}
