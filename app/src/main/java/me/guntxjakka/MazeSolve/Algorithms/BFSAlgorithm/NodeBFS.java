package me.guntxjakka.MazeSolve.Algorithms.BFSAlgorithm;

public class NodeBFS {
    private int x;
    private int y;
    private int cost;
    private NodeBFS parent;

    public NodeBFS(int x, int y, int cost, NodeBFS parent){
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.parent = parent;
    }

    public Integer getX(){
        return this.x;
    }

    public Integer getY(){
        return this.y;
    }

    public Integer getCost(){
        return this.cost;
    }

    public NodeBFS getParent(){
        return this.parent;
    }

}
