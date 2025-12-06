package me.guntxjakka.MazeSolve;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.MazeFile.MazeFileReader;
import me.guntxjakka.MazeSolve.Utils.MazeUtils;

public class PrintMaze {
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

        MazeUtils.printBlankMaze(m, md);
    }
}
