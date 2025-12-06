package me.guntxjakka.MazeSolve.Utils;

import java.util.Map;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmResult;

public class MazeMiscResults {
    public static void print(AlgorithmResult res){
        System.out.println("Results: ");
        System.out.println(String.format("Total Cost: %d, Time used: %dms", res.getCost(), res.getElapsedTime()));
    }

    public static void printTable(Map<String, AlgorithmResult> resmap) {
        System.out.printf("----------------------------------%n");
        System.out.printf("| %-10s | %-6s | %8s |%n", "Algorithm", "Cost", "Time");
        System.out.printf("----------------------------------%n");
        System.out.printf("| %-10s | %-6s | %6sms |%n", "Genetics", resmap.get("ga").getCost(), resmap.get("ga").getElapsedTime());
        System.out.printf("| %-10s | %-6s | %6sms |%n", "A*", resmap.get("astar").getCost(), resmap.get("astar").getElapsedTime());
        System.out.printf("| %-10s | %-6s | %6sms |%n", "Dijkstra", resmap.get("dijkstra").getCost(), resmap.get("dijkstra").getElapsedTime());
        System.out.printf("| %-10s | %-6s | %6sms |%n", "BFS", resmap.get("bfs").getCost(), resmap.get("bfs").getElapsedTime());
        System.out.printf("----------------------------------%n");
    }
}
