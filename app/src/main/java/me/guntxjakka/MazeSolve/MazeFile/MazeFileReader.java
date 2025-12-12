package me.guntxjakka.MazeSolve.MazeFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MazeFileReader {
    private String targetPath;
    private MazeDimension dim;
    private ArrayList<List<Integer>> maze;

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
        this.maze = null;
    }

    public MazeDimension getDimension(){
        return new MazeDimension(dim.w(), dim.h());
    }

    public void readFile(){
        this.maze = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.targetPath))){
            String l;
            int w = 0, h = 0;
            while((l = br.readLine()) != null){
                if (w == 0) w = l.length();
                ArrayList<Integer> la = getMazeLine(l);
                this.maze.add(la);
                h++;
            }
            this.dim = new MazeDimension(w, h);
        }
        catch (FileNotFoundException e){
            System.err.printf("File %s not found!%n", this.targetPath);
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (IOException e){
            System.err.printf("Error while reading %s!%n", this.targetPath);
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static ArrayList<Integer> getMazeLine(String l) {
        ArrayList<Integer> la = new ArrayList<>();
        for (int i = 0; i < l.length(); i++){
            switch (l.charAt(i)) {
                case '#':
                    la.add(-1);
                    break;
                case 'S':
                    la.add(-2);
                    break;
                case 'G':
                    la.add(-3);
                    break;
                case '"':
                    StringBuilder sb = new StringBuilder();
                    if (l.charAt(i + 1) == '"') break;
                    while (l.charAt(++i) != '"'){
                        sb.append(l.charAt(i));
                    }
                    la.add(Integer.parseInt(sb.toString()));
                    break;
                default:
                    break;
            }
        }
        return la;
    }

    public ArrayList<List<Integer>> getMaze() {
        return new ArrayList<>(this.maze);
    }
}
