package me.guntxjakka.MazeSolve.Utils;

public class MutableCoordinate extends Coordinate {
    public MutableCoordinate(int x, int y){
        super(x, y);
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setPos(Coordinate c){
        this.x = c.getX();
        this.y = c.getY();
    }
}
