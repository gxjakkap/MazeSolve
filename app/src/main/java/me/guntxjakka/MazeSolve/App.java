package me.guntxjakka.MazeSolve;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmContext;
import me.guntxjakka.MazeSolve.Algorithms.AlgorithmResult;
import me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm.GeneticsAlgorithm;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.MazeFile.MazeFileReader;
import me.guntxjakka.MazeSolve.Utils.MazeUtils;
import me.guntxjakka.MazeSolve.Utils.TwoDArray;

public class App {
    public static void main(String[] args) {
        Path base = Paths.get(System.getProperty("user.dir"), "..", "maze");
        MazeFileReader mf = new MazeFileReader();
        System.out.println(args[0]);
        mf.setTargetPath(base.normalize().resolve(String.format("%s.txt", args[0])).toString());
        mf.readFile();
        MazeDimension md = mf.getDimension();
        ArrayList<List<Integer>> m = mf.getMaze();

        System.out.println(String.format("w: %d h: %d", md.getW(), md.getH()));
        /* Integer[][] miarr = new Integer[md.getH()][md.getW()];
        for(int i = 0; i < m.size(); i++){
            for(int j = 0; j < m.get(i).size(); j++){
                miarr[i][j] = m.get(i).get(j);
            }
        }
        TwoDArray.printIntTwoDArray(miarr); */

        AlgorithmContext alg = new AlgorithmContext(new GeneticsAlgorithm());
        AlgorithmResult res = alg.execute(m, md);

        MazeUtils.printMaze(m, md, res.getPaths());
    }
}
