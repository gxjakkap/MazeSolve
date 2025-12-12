package me.guntxjakka.MazeSolve.Algorithms.DijkstraAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmConstant;
import me.guntxjakka.MazeSolve.Algorithms.AlgorithmStrategy;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.Utils.Coordinate;

public class Dijkstra implements AlgorithmStrategy{
    private int cost = 0;
    private List<Coordinate> bestPath = null;
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

    private long elapsed = 0;

    // get position of specific value
    private Coordinate getStart(List<List<Integer>> maze){
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(i).size(); j++) {
                if (maze.get(i).get(j).equals(-2)) {
                    return new Coordinate(i, j);
                }
            }
        }
        return null;
    }

    private Coordinate getEnd(List<List<Integer>> maze){
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(i).size(); j++) {
                if (maze.get(i).get(j).equals(-3)) {
                    return new Coordinate(i, j);
                }
            }
        }
        return null;
    }

    // dijkstra algorithm
    public void findPath(List<List<Integer>> maze, MazeDimension dimension){
        System.out.println("dijkstra Method running..");
        long startTime = System.currentTimeMillis();
        int row = dimension.h();
        int col = dimension.w();
        List<List<Integer>> dist = new ArrayList<>(); //collect cost
        NodeDijkstra[][] parent = new NodeDijkstra[row][col]; //collect path
        /* initiate dist with max val */
        for(int i = 0; i < row;i++){
            List<Integer> d = new ArrayList<>();
            for(int j = 0;j < col;j++){
                d.add(Integer.MAX_VALUE);
            }
            dist.add(d);
        }
        Coordinate startPoint = getStart(maze);
        Coordinate endPoint = getEnd(maze);

        if (startPoint == null || endPoint == null) {
            System.out.println("Start or End not found");
            this.elapsed = System.currentTimeMillis() - startTime;
            return;
        }


        //set start point cost = 0
        dist.get(startPoint.getX()).set(startPoint.getY(), 0); 
        PriorityQueue<NodeDijkstra> pq = new PriorityQueue<>();
        pq.add(new NodeDijkstra(startPoint.getX(), startPoint.getY(), 0));
        parent[startPoint.getX()][startPoint.getY()] = null;
        while(!pq.isEmpty()){
            this.elapsed = System.currentTimeMillis() - startTime;
            if (this.elapsed >= AlgorithmConstant.TIME_LIMIT_MS){
                System.out.printf("Terminated! Time limit of %d reached. (%d elapsed)%n", AlgorithmConstant.TIME_LIMIT_MS, this.elapsed);
                this.elapsed = System.currentTimeMillis() - startTime;
                return;
            }
            NodeDijkstra cur = pq.poll();
            if(cur.getX() == endPoint.getX() && cur.getY() == endPoint.getY()){
                break;
            }
            for(int i = 0; i < 4;i++){
                int nx = cur.getX() + dx[i];
                int ny = cur.getY() + dy[i];
                // limited area
                if(nx < 0 || ny < 0 || nx >= row || ny >= col){
                    continue;
                }
                // wall
                if(maze.get(nx).get(ny) == -1){
                    continue;
                }
                int weight = maze.get(nx).get(ny);
                // exclude the cost of start/end point
                if(weight == -2 || weight == -3){
                    weight = 0;
                }
                int newCost = cur.getCost() + weight;
                if(newCost < dist.get(nx).get(ny)){
                    dist.get(nx).set(ny, newCost);
                    parent[nx][ny] = cur;
                    pq.add(new NodeDijkstra(nx, ny, newCost));
                }
            }
        }

        this.elapsed = System.currentTimeMillis() - startTime;

        // find best path
        List<Coordinate> path = new ArrayList<>();
        if(dist.get(endPoint.getX()).get(endPoint.getY()) == Integer.MAX_VALUE){
            System.out.println("cant find path");
        }
        NodeDijkstra c = new NodeDijkstra(endPoint.getX(), endPoint.getY(), dist.get(endPoint.getX()).get(endPoint.getY()));
        while (c != null){
            this.elapsed = System.currentTimeMillis() - startTime;
            if (this.elapsed >= AlgorithmConstant.TIME_LIMIT_MS){
                System.out.println(String.format("Terminated! Time limit of %d reached. (%d elapsed)", AlgorithmConstant.TIME_LIMIT_MS, this.elapsed));
                this.elapsed = System.currentTimeMillis() - startTime;
                return;
            }
            path.add(new Coordinate(c.getX(), c.getY()));
            c = parent[c.getX()][c.getY()];
        }
        Collections.reverse(path);
        bestPath = new ArrayList<>(path);
        cost = dist.get(endPoint.getX()).get(endPoint.getY());
        // test
        // System.out.println(dist);

        this.elapsed = System.currentTimeMillis() - startTime;
        return;
    }

    //execute total cost from path that we walk
    public int getCost(){
        return this.cost;
    }

    //execute path answer
    public List<Coordinate> getPath(){
        return new ArrayList<>(this.bestPath);
    }

    public long getElapsedTime(){
        return this.elapsed;
    }
}
