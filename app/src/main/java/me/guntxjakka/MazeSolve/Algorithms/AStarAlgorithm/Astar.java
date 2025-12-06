package me.guntxjakka.MazeSolve.Algorithms.AStarAlgorithm;

import java.util.*;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmConstant;
import me.guntxjakka.MazeSolve.Algorithms.AlgorithmStrategy;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.Utils.*;

class Node{
    Coordinate coordinate;
    Node parent;
    int f;
    int g;
    int h;

    public Node(Coordinate coordinate, Node parent, int g, int h){
        this.coordinate = coordinate;
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }

    public int compareTo(Node other){
        return Integer.compare(this.f, other.f);
    }
}

public class AStar implements AlgorithmStrategy{
    private int cost = -1;
    private List<Coordinate> path = new ArrayList<>();

    private int[][] directions = {{-1,0}, {1,0}, {0, -1}, {0, 1}};

    private long elapsed = 0;

    public void findPath(List<List<Integer>> maze, MazeDimension dimension){
        long startTime = System.currentTimeMillis();

        Coordinate start = null;
        Coordinate goal = null;
        int H = dimension.getH();
        int W = dimension.getW();

        for(int y = 0; y < H; y++){
            for(int x = 0; x < W; x++){
                int value = maze.get(y).get(x);
                if(value == -2){
                    start = new Coordinate(x, y);
                }
                else if(value == -3){
                    goal = new Coordinate(x, y);
                }
            }
        }
        if(start == null || goal == null){
            System.out.println("Not found Start or Goal kub ;-;");
            return;
        }

        PriorityQueue<Node> nodeList = new PriorityQueue<>();
        HashMap<String, Node> allNodes = new HashMap<>();

        int hStart = calculateManDis(start, goal);    
        Node startNode = new Node(start, null, 0, hStart);
        nodeList.add(startNode);
        allNodes.put(getKey(start), startNode);
        Node curNode;
        
        while(!nodeList.isEmpty()){
            curNode = nodeList.poll(); //ดึง f น้อยสุดดด
            Coordinate curCoor = curNode.getCoordinate();
            this.elapsed = System.currentTimeMillis() - startTime;

            if(curCoor.getX() == goal.getX() && curCoor.getY() == goal.getY()){
                reconstructPath(curNode);
                this.cost = curNode.g;
                this.elapsed = System.currentTimeMillis() - startTime;
                return;
            }

            if (this.elapsed >= AlgorithmConstant.TIME_LIMIT_MS){
                System.out.println(String.format("Terminated! Time limit of %d reached. (%d elapsed)", AlgorithmConstant.TIME_LIMIT_MS, this.elapsed));
                this.elapsed = System.currentTimeMillis() - startTime;
                return;
            }

            for(int[] dir : directions){ //check neighbor
                int newX = curCoor.getX() + dir[1];
                int newY = curCoor.getY() + dir[0];

                Coordinate neighCoor = new Coordinate(newX, newY);
                String neighKey = getKey(neighCoor);
                //Check wall
                if(newX >= 0 && newX < W && newY >= 0 && newY < H && maze.get(newY).get(newX) != -1){
                    int stepCost = 1;
                    int sqValue = maze.get(newY).get(newX);
                    if(sqValue > 0){
                        stepCost = sqValue;
                    }

                    int newG = curNode.g + stepCost;
                    Node neighNode = allNodes.get(neighKey);
                    if(neighNode == null || newG < neighNode.g){ //New node or better g
                        int hNeighbor = calculateManDis(neighCoor, goal);

                        if(neighNode == null){
                            neighNode = new Node(neighCoor, curNode, newG, hNeighbor);
                            allNodes.put(neighKey, neighNode);
                            nodeList.add(neighNode);
                        }
                        else{
                            neighNode.g =  newG;
                            neighNode.f = newG + neighNode.h;
                            neighNode.parent = curNode;

                            nodeList.remove(neighNode); //ลบแล้วเพิ่มใหม่ เรียงค่า f
                            nodeList.add(neighNode);
                        }
                    }
                }
            }
        }
        this.cost = -1;
        this.path = Collections.emptyList();
        this.elapsed = System.currentTimeMillis() - startTime;
    }

    private int calculateManDis(Coordinate cur, Coordinate goal){
        return Math.abs(cur.getX() - goal.getX()) + Math.abs(cur.getY() - goal.getY());
    }


    private void reconstructPath(Node endNode){
        List<Coordinate> tempPath = new ArrayList<>();
        Node current = endNode;
        while(current != null){
            tempPath.add(current.getCoordinate());
            current = current.parent;
        }
        Collections.reverse(tempPath);
        this.path = tempPath;
    }

    private String getKey(Coordinate c){
        return c.getX() + "," + c.getY();
    }

    public int getCost(){
        return cost;
    }

    public List<Coordinate> getPath(){
        return path;
    }

    public long getElapsedTime(){
        return this.elapsed;
    }
    
}

