package me.guntxjakka.MazeSolve.Algorithms;

import java.util.List;

import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.Utils.Coordinate;

public interface AlgorithmStrategy {
    public void findPath(List<List<Integer>> maze, MazeDimension dimension);
    public int getCost();
    public long getElapsedTime();
    public List<Coordinate> getPath();
}
