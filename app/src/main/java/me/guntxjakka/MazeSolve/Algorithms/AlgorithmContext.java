package me.guntxjakka.MazeSolve.Algorithms;

import java.util.List;

import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;

public class AlgorithmContext {
    private AlgorithmStrategy strategy;

    public AlgorithmContext(AlgorithmStrategy strategy){
        this.strategy = strategy;
    }

    public void setStrategy(AlgorithmStrategy strategy){
        this.strategy = strategy;
    }

    public AlgorithmResult execute(List<List<Integer>> maze, MazeDimension dimension){
        this.strategy.findPath(maze, dimension);
        return new AlgorithmResult(this.strategy.getCost(), this.strategy.getPath());
    }
    
}
