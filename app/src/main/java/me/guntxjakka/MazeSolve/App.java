package me.guntxjakka.MazeSolve;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.MazeFile.MazeFileReader;
import me.guntxjakka.MazeSolve.Utils.TwoDArray;

public class App {
    public static void main(String[] args) {
        Path base = Paths.get(System.getProperty("user.dir"), "..", "maze");
        MazeFileReader mf = new MazeFileReader();
        mf.setTargetPath(base.normalize().resolve("m15_15.txt").toString());
        mf.readFile();
        MazeDimension md = mf.getDimension();
        ArrayList<List<Integer>> m = mf.getMaze();

        System.out.println(String.format("w: %d h: %d", md.getW(), md.getH()));
        Integer[][] miarr = new Integer[md.getH()][md.getW()];
        for(int i = 0; i < m.size(); i++){
            for(int j = 0; j < m.get(i).size(); j++){
                miarr[i][j] = m.get(i).get(j);
            }
        }
        TwoDArray.printIntTwoDArray(miarr);
    }
}
