package me.guntxjakka.MazeSolve;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmContext;
import me.guntxjakka.MazeSolve.Algorithms.AlgorithmResult;
import me.guntxjakka.MazeSolve.Algorithms.AStarAlgorithm.AStar;
import me.guntxjakka.MazeSolve.Algorithms.BFSAlgorithm.BFS;
import me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm.GeneticsAlgorithm;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.MazeFile.MazeFileReader;
import me.guntxjakka.MazeSolve.Utils.MazeMiscResults;
import me.guntxjakka.MazeSolve.Utils.MazeUtils;

public class App {
    public static void main(String[] args) {
        Path base = Paths.get(System.getProperty("user.dir"), "..", "maze");
        MazeFileReader mf = new MazeFileReader();
        String mfn = args[0];
        if (mfn.isBlank()){
            mfn = "m15_15";
        }
        System.out.println(String.format("Using maze file %s.txt", mfn));
        mf.setTargetPath(base.normalize().resolve(String.format("%s.txt", mfn)).toString());
        mf.readFile();
        MazeDimension md = mf.getDimension();
        ArrayList<List<Integer>> m = mf.getMaze();

        System.out.println(String.format("w: %d h: %d", md.getW(), md.getH()));

        AlgorithmContext alg = null;
        AlgorithmResult res = null;

        System.out.println("Genetics Algorithm Results: ");
        alg = new AlgorithmContext(new GeneticsAlgorithm());
        res = alg.execute(m, md);
        MazeUtils.printMaze(m, md, res.getPaths());
        MazeMiscResults.print(res);

        System.out.println("-------------------------");

        System.out.println("A* Algorithm Results: ");
        alg = new AlgorithmContext(new AStar());
        res = alg.execute(m, md);
        MazeUtils.printMaze(m, md, res.getPaths());
        MazeMiscResults.print(res);

        System.out.println("-------------------------");

        System.out.println("Dijkstra Algorithm Results: ");
        alg = new AlgorithmContext(new AStar());
        res = alg.execute(m, md);
        MazeUtils.printMaze(m, md, res.getPaths());
        MazeMiscResults.print(res);

        System.out.println("-------------------------");

        System.out.println("BFS Algorithm Results: ");
        alg = new AlgorithmContext(new BFS());
        res = alg.execute(m, md);
        MazeUtils.printMaze(m, md, res.getPaths());
        MazeMiscResults.print(res);
    }
}
