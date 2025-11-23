package me.guntxjakka.MazeSolve.Algorithms;

import java.util.List;

import me.guntxjakka.MazeSolve.Utils.Coordinate;

public class AlgorithmResult {
    private int cost;
    private List<Coordinate> paths;

    public AlgorithmResult(int cost, List<Coordinate> paths){
        this.cost = cost;
        this.paths = paths;
    }

    public int getCost() {
        return cost;
    }

    public List<Coordinate> getPaths() {
        return paths;
    }
}
