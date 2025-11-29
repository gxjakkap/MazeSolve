package me.guntxjakka.MazeSolve.Algorithms.BFSAlgorithm;

import java.util.ArrayList;
import java.util.List;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmStrategy;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.Utils.Coordinate;

public class BFS implements AlgorithmStrategy{

    private int cost = 0;
    private List<Coordinate> bestPath = null;
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    // bfs algorithm
    public void findPath(List<List<Integer>> maze, MazeDimension dimension){

    }

    //execute total cost from path that we walk
    public int getCost(){
        return this.cost;
    }

    //execute path answer
    public List<Coordinate> getPath(){
        return new ArrayList<>(this.bestPath);
    }
}
