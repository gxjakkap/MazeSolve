package me.guntxjakka.MazeSolve.Algorithms.DijkstraAlgorithm;

public class Node implements Comparable<Node>{
    private int x;
    private int y;
    private int cost;

    public Node(int x, int y, int cost){
        this.x = x;
        this.y = y;
        this.cost = cost;
    }

    public Integer getX(){
        return this.x;
    }

    public Integer getY(){
        return this.y;
    }

    public int getCost(){
        return this.cost;
    }

    @Override
    public int compareTo(Node other) {
        return this.cost - other.cost;
    }



}
