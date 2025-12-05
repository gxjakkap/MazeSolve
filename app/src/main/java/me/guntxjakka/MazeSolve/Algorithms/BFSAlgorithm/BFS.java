package me.guntxjakka.MazeSolve.Algorithms.BFSAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmStrategy;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.Utils.Coordinate;

public class BFS implements AlgorithmStrategy{

    private int cost = 0;
    private List<Coordinate> bestPath = null;
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

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
    
    // bfs algorithm
    public void findPath(List<List<Integer>> maze, MazeDimension dimension){
        System.out.println("BFS Method running..");
        int row = dimension.getH();
        int col = dimension.getW();
        boolean[][] visited = new boolean[row][col];
        Queue<NodeBFS> q = new LinkedList<>();
        Coordinate startPoint = getStart(maze);
        Coordinate endPoint = getEnd(maze);
        q.add(new NodeBFS(startPoint.getX(), startPoint.getY(), 0, null));
        visited[startPoint.getX()][startPoint.getY()] = true;
        NodeBFS endNode = null;

        while(!q.isEmpty()){
            NodeBFS cur = q.poll();
            //reach endPoint
            if(cur.getX() == endPoint.getX() && cur.getY() == endPoint.getY()){
                endNode = cur;
                cost = cur.getCost();
                break;
            }
            for(int i = 0; i < 4;i++){
                int nx = cur.getX() + dx[i];
                int ny = cur.getY() + dy[i];
                if(nx >= 0 && ny >= 0 && nx < row && ny < col && !visited[nx][ny] && maze.get(nx).get(ny) != -1){
                    visited[nx][ny] = true;
                    int weight = maze.get(nx).get(ny);
                    if(weight == -3 || weight == -2){
                        weight = 0;
                    }
                    q.add(new NodeBFS(nx, ny, cur.getCost()+weight, cur));
                }
            }
        }

        // no path found
        if(endNode == null){
            System.out.println("can't find path");
            return;
        }
        // find best path
        List<Coordinate> path = new ArrayList<>();
        NodeBFS cur = endNode;
        while(cur != null){
            path.add(new Coordinate(cur.getX(), cur.getY()));
            cur = cur.getParent();
        }
        Collections.reverse(path);
        bestPath = new ArrayList<>(path);
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
}
