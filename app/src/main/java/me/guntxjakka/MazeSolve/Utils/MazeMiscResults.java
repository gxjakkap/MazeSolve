package me.guntxjakka.MazeSolve.Utils;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmResult;

public class MazeMiscResults {
    public static void print(AlgorithmResult res){
        System.out.println("Results: ");
        System.out.println(String.format("Total Cost: %d, Time used: %d", res.getCost(), res.getElapsedTime()));
    }
}
