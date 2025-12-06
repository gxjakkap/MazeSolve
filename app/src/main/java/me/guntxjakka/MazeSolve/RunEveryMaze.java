package me.guntxjakka.MazeSolve;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmContext;
import me.guntxjakka.MazeSolve.Algorithms.AlgorithmResult;
import me.guntxjakka.MazeSolve.Algorithms.AStarAlgorithm.AStar;
import me.guntxjakka.MazeSolve.Algorithms.BFSAlgorithm.BFS;
import me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm.GeneticsAlgorithm;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.MazeFile.MazeFileReader;
import me.guntxjakka.MazeSolve.Utils.MazeMiscResults;
//import me.guntxjakka.MazeSolve.Utils.MazeUtils;

public class RunEveryMaze {
    public static void main(String[] args) {
        Path base = Paths.get(System.getProperty("user.dir"), "..", "maze");

        File mazeFolder = new File(base.normalize().toString());

        if (!mazeFolder.exists() || !mazeFolder.isDirectory()){
            System.out.println("Maze folder not found!");
            System.exit(1);
        }

        File[] mazeFiles = mazeFolder.listFiles();

        if (mazeFiles != null){
            for (File mFile : mazeFiles){
                MazeFileReader mf = new MazeFileReader();
                String mfn = mFile.getName();
                System.out.println(String.format("Using maze file %s", mfn));
                mf.setTargetPath(base.normalize().resolve(String.format("%s", mfn)).toString());
                mf.readFile();
                MazeDimension md = mf.getDimension();
                ArrayList<List<Integer>> m = mf.getMaze();
    
                System.out.println(String.format("w: %d h: %d", md.getW(), md.getH()));
    
                AlgorithmContext alg = null;
                AlgorithmResult res = null;
    
                Map<String, AlgorithmResult> resmap = new HashMap<>();
    
                System.out.println("Genetics Algorithm Results: ");
                alg = new AlgorithmContext(new GeneticsAlgorithm());
                res = alg.execute(m, md);
                //MazeUtils.printMaze(m, md, res.getPaths());
                MazeMiscResults.print(res);
                resmap.put("ga", res);
    
                System.out.println("-------------------------");
    
                System.out.println("A* Algorithm Results: ");
                alg = new AlgorithmContext(new AStar());
                res = alg.execute(m, md);
                //MazeUtils.printMaze(m, md, res.getPaths());
                MazeMiscResults.print(res);
                resmap.put("astar", res);
    
                System.out.println("-------------------------");
    
                System.out.println("Dijkstra Algorithm Results: ");
                alg = new AlgorithmContext(new AStar());
                res = alg.execute(m, md);
                //MazeUtils.printMaze(m, md, res.getPaths());
                MazeMiscResults.print(res);
                resmap.put("dijkstra", res);
    
                System.out.println("-------------------------");
    
                System.out.println("BFS Algorithm Results: ");
                alg = new AlgorithmContext(new BFS());
                res = alg.execute(m, md);
                //MazeUtils.printMaze(m, md, MazeUtils.flipCoordinates(res.getPaths()));
                MazeMiscResults.print(res);
                resmap.put("bfs", res);
    
                System.out.println("-------------------------");
                System.out.println();
                System.out.println(String.format("Conclusion for %s", mfn));
                MazeMiscResults.printTable(resmap);
            }
        }
        else {
            System.out.println("Maze folder is empty!");
            System.exit(1);
        }
    }
}
