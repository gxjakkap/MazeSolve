package me.guntxjakka.MazeSolve.Algorithms.AStarAlgorithm;

import me.guntxjakka.MazeSolve.Utils.Coordinate;

public class AStarNode implements Comparable<AStarNode>{
    Coordinate coordinate;
    AStarNode parent;
    int f;
    int g;
    int h;

    public AStarNode(Coordinate coordinate, AStarNode parent, int g, int h){
        this.coordinate = coordinate;
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

    public int compareTo(AStarNode other){
        return Integer.compare(this.f, other.f);
    }
}