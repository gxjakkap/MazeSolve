package me.guntxjakka.MazeSolve.Algorithms.DijkstraAlgorithm;

public class NodeDijkstra implements Comparable<NodeDijkstra>{
    private int x;
    private int y;
    private int cost;

    public NodeDijkstra(int x, int y, int cost){
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
    public int compareTo(NodeDijkstra other) {
        return this.cost - other.cost;
    }



}
