package me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmStrategy;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.Utils.Coordinate;
import me.guntxjakka.MazeSolve.Utils.MutableCoordinate;

public class GeneticsAlgorithm implements AlgorithmStrategy {
    private static final int POP_SIZE = 200;
    private static final int MAX_GEN = 2000;
    private static final int MAX_MOVES = 255;
    private static final double MUT_RATE = 0.05;
    public static final char[] MV = { 'u', 'd', 'l', 'r' };

    private int cost = 0;
    private List<Coordinate> bestPath = null;

    private Population evolvePops(Population cur) {
        Population newPops = new Population();

        int e = cur.getPops().size() / 10; // top 10%

        for (int i = 0; i < e; i++) {
            newPops.addIndividual(new Individual(cur.getPops().get(i).getMoves()));
        }

        // Evolve
        while (newPops.getPops().size() < cur.getPops().size()) {
            Individual p1 = tournamentSelection(cur);
            Individual p2 = tournamentSelection(cur);
            Individual child = crossover(p1, p2);
            child.mutate(MUT_RATE); // 5% mutation rate
            newPops.addIndividual(child);
        }

        return newPops;
    }

    private Individual tournamentSelection(Population pop) {
        int tournamentSize = 5;
        Population tournament = new Population();
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.getPops().size());
            tournament.addIndividual(pop.getPops().get(randomId));
        }
        Collections.sort(tournament.getPops());
        return tournament.getPops().get(0);
    }

    private Individual crossover(Individual p1, Individual p2) {
        String m1 = p1.getMoves();
        String m2 = p2.getMoves();
        int len = m1.length();
        int split = (int) (Math.random() * len);

        String childMoves = m1.substring(0, split) + m2.substring(split);
        return new Individual(childMoves);
    }

    private Coordinate getPosition(List<List<Integer>> maze, int val) {
        for (int i = 0; i < maze.size(); i++) {
            for (int j = 0; j < maze.get(i).size(); j++) {
                if (maze.get(i).get(j).equals(val)) {
                    return new Coordinate(i, j);
                }
            }
        }

        return null;
    }

    private boolean isIllegalMove(List<List<Integer>> maze, MazeDimension dim, Coordinate next) {
        if (next.getX() < 0 || next.getY() < 0)
            return true; // list index can't be negative
        if (next.getX() >= dim.getW() || next.getY() >= dim.getH())
            return true; // check out of bound

        Integer nextPosVal = maze.get(next.getY()).get(next.getX());

        if (nextPosVal.equals(-1))
            return true; // if walked into walls
        if (nextPosVal.equals(-2))
            return true; // don't allow walking back to start to prevent loop

        return false;
    }

    private double calcFitness(int cst, Coordinate cur, boolean fnd, Coordinate goal) {
        if (fnd)
            return 10000.0 + (1.0 / cst);
        else {
            double dis = Math.abs(cur.getX() - goal.getX()) + Math.abs(cur.getY() - goal.getY());

            // add 1 to avoid divide by zero
            return 1.0 / (dis + 1);
        }
    }

    private void evaluate(List<List<Integer>> maze, MazeDimension dim, Individual ind, Coordinate start) {
        int cst = 0;
        boolean fnd = false;

        MutableCoordinate pos = new MutableCoordinate(start.getX(), start.getY());

        for (char m : ind.getMoves().toCharArray()) {
            Coordinate next;

            switch (m) {
                case 'u':
                    next = new Coordinate(pos.getX(), pos.getY() - 1);
                    break;
                case 'd':
                    next = new Coordinate(pos.getX(), pos.getY() + 1);
                    break;
                case 'l':
                    next = new Coordinate(pos.getX() - 1, pos.getY());
                    break;
                default: // only case left is r
                    next = new Coordinate(pos.getX() + 1, pos.getY());
                    break;
            }

            if (!isIllegalMove(maze, dim, next)) {
                pos.setPos(next);
                if (maze.get(pos.getY()).get(pos.getX()).equals(-3)) {
                    // goal reached
                    fnd = true;
                    ind.setFinished(true);
                    break;
                }
                cst += maze.get(next.getY()).get(next.getX());
            }

        }

        ind.setFitness(calcFitness(cst, pos, fnd, getPosition(maze, -3)));
        ind.setCost(cst);
    }

    private void recordBestPath(List<List<Integer>> maze, Individual ind, Coordinate start) {
        MutableCoordinate pos = new MutableCoordinate(start.getX(), start.getY());
        List<Coordinate> path = new ArrayList<>();
        for (char m : ind.getMoves().toCharArray()) {
            switch (m) {
                case 'u':
                    path.add(new Coordinate(pos.getX(), pos.getY() - 1));
                    break;
                case 'd':
                    path.add(new Coordinate(pos.getX(), pos.getY() + 1));
                    break;
                case 'l':
                    path.add(new Coordinate(pos.getX() - 1, pos.getY()));
                    break;
                default: // only case left is r
                    path.add(new Coordinate(pos.getX() + 1, pos.getY()));
                    break;
            }
        }
        this.bestPath = path;
    }

    public void findPath(List<List<Integer>> maze, MazeDimension dimension) {
        Coordinate start = getPosition(maze, -2);
        Coordinate end = getPosition(maze, -3);

        if (start == null || end == null) {
            System.out.println("Error: Start or End point not found!");
            return;
        }

        // init
        Population pop = new Population();
        pop.populateFirstGen(POP_SIZE, MAX_MOVES);

        Individual champOfTheChamp = null;
        int gc = 0;

        while (gc < MAX_GEN) {
            for (Individual ind : pop.getPops()) {
                evaluate(maze, dimension, ind, start);
            }

            pop.sortDescending();
            Individual champCandidate = pop.getPops().get(0);

            if (champOfTheChamp == null || champCandidate.compareTo(champOfTheChamp) > 0) {
                champOfTheChamp = new Individual(null);
                champOfTheChamp.copyFrom(champCandidate);

                System.out.println(
                        String.format("[Gen #%d] new ChampOfTheChamp! fit: %.3f", gc, champOfTheChamp.getFitness()));
            }

            if (champOfTheChamp.isFinished()) {
                this.cost = champOfTheChamp.getCost();
                recordBestPath(maze, champCandidate, start);
                System.out.println(String.format("[Gen #%d] Solution found!", gc));
                // TODO: print solution
                break;
            }

            pop = evolvePops(pop);
            gc++;
        }
    }

    public int getCost() {
        return this.cost;
    }

    public List<Coordinate> getPath() {
        return new ArrayList<>(this.bestPath);
    }
}
