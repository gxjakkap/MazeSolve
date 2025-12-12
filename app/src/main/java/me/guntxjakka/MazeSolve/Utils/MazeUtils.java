package me.guntxjakka.MazeSolve.Utils;

import java.util.ArrayList;
import java.util.List;

import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;

public class MazeUtils {
    // Helper to strip ANSI escape codes and pad to a fixed width for alignment
    private static String pad(String s) {
        // Remove ANSI escape sequences to measure visible length
        String raw = s.replaceAll("\\u001B\\[[;\\d]*m", "");
        int padding = 4 - raw.length();
        if (padding > 0) {
            // Prepend spaces to the original string (which may contain ANSI codes)
            return " ".repeat(padding) + s;
        }
        return s;
    }

    public static List<Coordinate> flipCoordinates(List<Coordinate> p){
        List<Coordinate> l = new ArrayList<>();

        for (Coordinate n : p){
            l.add(new Coordinate(n.getY(), n.getX()));
        }

        return new ArrayList<>(l);
    }

    public static void printMaze(List<List<Integer>> maze, MazeDimension dim, List<Coordinate> path) {
        if (path == null)
            return;
        String[][] displayGrid = new String[dim.h()][dim.w()];

        for (int y = 0; y < dim.h(); y++) {
            for (int x = 0; x < dim.w(); x++) {
                int val = maze.get(y).get(x);
                if (val == -1) {
                    displayGrid[y][x] = "##";
                } else if (val == -2) {
                    displayGrid[y][x] = "\u001B[41mST\u001B[0m";
                } else if (val == -3) {
                    displayGrid[y][x] = "\u001B[41mED\u001B[0m";
                } else {
                    // print the cost
                    displayGrid[y][x] = String.valueOf(val);
                }
            }
        }

        for (int i = 0; i < path.size() - 1; i++) {
            Coordinate curr = path.get(i);
            Coordinate next = path.get(i + 1);

            int cellVal = maze.get(curr.getY()).get(curr.getX());
            if (cellVal == -2 || cellVal == -3)
                continue;

            String symbol = "*";
            if (next.getX() > curr.getX())
                symbol = "\u001B[42m>\u001B[0m";
            else if (next.getX() < curr.getX())
                symbol = "\u001B[42m<\u001B[0m";
            else if (next.getY() > curr.getY())
                symbol = "\u001B[42mv\u001B[0m";
            else if (next.getY() < curr.getY())
                symbol = "\u001B[42m^\u001B[0m";

            displayGrid[curr.getY()][curr.getX()] = symbol;
        }

        for (int y = 0; y < dim.h(); y++) {
            for (int x = 0; x < dim.w(); x++) {
                // Use pad() to ensure alignment even with ANSI colors
                System.out.print(pad(displayGrid[y][x]));
            }
            System.out.println();
        }
    }
    

    public static void printBlankMaze(List<List<Integer>> maze, MazeDimension dim) {
        String[][] displayGrid = new String[dim.h()][dim.w()];

        for (int y = 0; y < dim.h(); y++) {
            for (int x = 0; x < dim.w(); x++) {
                int val = maze.get(y).get(x);
                if (val == -1) {
                    displayGrid[y][x] = "##";
                } else if (val == -2) {
                    displayGrid[y][x] = "\u001B[41mST\u001B[0m";
                } else if (val == -3) {
                    displayGrid[y][x] = "\u001B[41mED\u001B[0m";
                } else {
                    // print the cost
                    displayGrid[y][x] = String.valueOf(val);
                }
            }
        }
        for (int y = 0; y < dim.h(); y++) {
            for (int x = 0; x < dim.w(); x++) {
                // Use pad() to ensure alignment even with ANSI colors
                System.out.print(pad(displayGrid[y][x]));
            }
            System.out.println();
        }
    }


    public static Coordinate getPosition(List<List<Integer>> maze, int val) {
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(i).size(); j++) {
                if (maze.get(i).get(j).equals(val)) {
                    return new Coordinate(i, j);
                }
            }
        }

        return null;
    }
}
