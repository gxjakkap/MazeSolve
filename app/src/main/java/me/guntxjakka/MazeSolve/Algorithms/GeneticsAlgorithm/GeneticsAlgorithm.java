package me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.guntxjakka.MazeSolve.Algorithms.AlgorithmConstant;
import me.guntxjakka.MazeSolve.Algorithms.AlgorithmStrategy;
import me.guntxjakka.MazeSolve.MazeFile.MazeDimension;
import me.guntxjakka.MazeSolve.Utils.Coordinate;
import me.guntxjakka.MazeSolve.Utils.MazeUtils;
import me.guntxjakka.MazeSolve.Utils.MutableCoordinate;

public class GeneticsAlgorithm implements AlgorithmStrategy {
    // Constant
    public static final char[] MV = { 'u', 'd', 'l', 'r' };

    // Population
    private static final int POP_SIZE = 2000;

    // Generation
    private static final int MAX_GEN = 50_000;
    private static final int STAGNATION_LIMIT = 500;
    private static final double MAX_MOVES_MULTIPLIER = 1.5;

    // Evolution
    private static final double MUT_RATE = 0.02;
    private static final double MUT_DECAY_AFTER = 0.25;
    private static final int TOURNAMENT_SIZE = 7;
    private static final double ELITISM_RATE = 0.05;
    private static final double RANDOM_IMMIGRANT_RATE = 0.02;
    private static final double CROSSOVER_RATE = 0.9;

    private static final boolean ENABLE_DEBUG_PRINT = false;

    private int cost = 0;
    private List<Coordinate> bestPath = null;
    private long elapsed = 0;

    private Population evolvePops(Population cur, int gc, int maxMoves) {
        Population newPops = new Population();

        int ec = (int) (cur.getPops().size() * ELITISM_RATE);
        List<Individual> elites = cur.getPops().subList(0, ec);
        elites.forEach(x -> {
            newPops.addIndividual(new Individual(x.getMoves()));
        });

        // Evolve
        while (newPops.getPops().size() < (cur.getPops().size() * (1.0 - RANDOM_IMMIGRANT_RATE))) {
            Individual p1 = tournamentSelection(cur);
            Individual child;

            if (Math.random() < CROSSOVER_RATE) {
                Individual p2 = tournamentSelection(cur);
                child = crossover(p1, p2);
            } else {
                child = new Individual(p1.getMoves());
            }

            child.mutate(this.adaptiveMut(gc));
            newPops.addIndividual(child);
        }

        newPops.fillPopsWithRandomImmigrant(cur.getPops().size(), maxMoves);

        return newPops;
    }

    private Individual tournamentSelection(Population pop) {
        Individual best = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomId = (int) (Math.random() * pop.getPops().size());
            Individual cand = pop.getPops().get(randomId);
            if (best == null || cand.getFitness() > best.getFitness()) {
                best = cand;
            }
        }

        if (best == null)
            throw new NullPointerException();

        return new Individual(best.getMoves());
    }

    private double adaptiveMut(int gc) {
        return (gc > MAX_GEN * MUT_DECAY_AFTER) ? MUT_RATE * 20 : MUT_RATE;
    }

    private Individual crossover(Individual p1, Individual p2) {
        String m1 = p1.getMoves();
        String m2 = p2.getMoves();
        int len = m1.length();
        int split = (int) (Math.random() * len);

        String childMoves = m1.substring(0, split) + m2.substring(split);
        return new Individual(childMoves);
    }

    private boolean isLegalMove(List<List<Integer>> maze, MazeDimension dim, Coordinate next) {
        if (next.getX() < 0 || next.getY() < 0)
            return false; // list index can't be negative
        if (next.getX() >= dim.getW() || next.getY() >= dim.getH())
            return false; // check out of bound

        Integer nextPosVal = maze.get(next.getY()).get(next.getX());

        return !nextPosVal.equals(-1) && !nextPosVal.equals(-2); // if walked into walls
    }

    private boolean isFacingGoal(Coordinate pos, char lastMove, Coordinate goal) {
        int dx = goal.getX() - pos.getX();
        int dy = goal.getY() - pos.getY();

        switch (lastMove) {
            case 'u':
                return dy < 0; // goal is above
            case 'd':
                return dy > 0; // goal is below
            case 'l':
                return dx < 0; // goal is left
            case 'r':
                return dx > 0; // goal is right
        }
        return false;
    }

    private double calcFitness(int cst, Coordinate cur, boolean fnd, Coordinate goal, MazeDimension dim,
            char lastMove) {
        double dis = Math.abs(cur.getX() - goal.getX()) + Math.abs(cur.getY() - goal.getY());

        if (fnd) {
            // goal found, optimize cost
            return 10000000.0 - (cst * 100);
        } else {
            // reward for getting closer
            double maxDist = dim.getW() + dim.getH() - 2;
            double proximityScore = 1000.0 * (1.0 - (dis / maxDist));
            // double progressBonus = Math.min(cst * 0.5, 500.0);

            if (isFacingGoal(cur, lastMove, goal)) {
                proximityScore += 150;
            }

            return proximityScore;
        }
    }

    private void evaluate(List<List<Integer>> maze, MazeDimension dim, Individual ind, Coordinate start) {
        int cst = 0;
        boolean fnd = false;

        MutableCoordinate pos = new MutableCoordinate(start.getX(), start.getY());

        for (char m : ind.getMoves().toCharArray()) {
            Coordinate next = switch (m) {
                case 'u' -> new Coordinate(pos.getX(), pos.getY() - 1);
                case 'd' -> new Coordinate(pos.getX(), pos.getY() + 1);
                case 'l' -> new Coordinate(pos.getX() - 1, pos.getY());
                default -> // only case left is r
                        new Coordinate(pos.getX() + 1, pos.getY());
            };

            if (isLegalMove(maze, dim, next)) {
                pos.setPos(next);
                if (maze.get(pos.getY()).get(pos.getX()).equals(-3)) {
                    // goal reached
                    fnd = true;
                    ind.setFinished(true);
                    break;
                }
                int nxcst = maze.get(next.getY()).get(next.getX());
                if (nxcst > 0) {
                    cst += nxcst;
                }

            }

        }

        String m = ind.getMoves();
        char lm = m.charAt(m.length() - 1);
        ind.setFitness(calcFitness(cst, pos, fnd, Objects.requireNonNull(MazeUtils.getPosition(maze, -3)), dim, lm));
        ind.setCost(cst);
    }

    private List<Coordinate> getPathFromIndividual(List<List<Integer>> maze, MazeDimension dim, Individual ind,
            Coordinate start) {
        MutableCoordinate pos = new MutableCoordinate(start.getX(), start.getY());
        List<Coordinate> path = new ArrayList<>();
        path.add(new Coordinate(start.getX(), start.getY()));

        for (char m : ind.getMoves().toCharArray()) {
            Coordinate next = switch (m) {
                case 'u' -> new Coordinate(pos.getX(), pos.getY() - 1);
                case 'd' -> new Coordinate(pos.getX(), pos.getY() + 1);
                case 'l' -> new Coordinate(pos.getX() - 1, pos.getY());
                default -> // r
                        new Coordinate(pos.getX() + 1, pos.getY());
            };

            if (isLegalMove(maze, dim, next)) {
                pos.setPos(next);
                path.add(next);
                if (maze.get(pos.getY()).get(pos.getX()).equals(-3)) {
                    break;
                }
            }
        }
        return path;
    }

    public void findPath(List<List<Integer>> maze, MazeDimension dimension) {
        Coordinate start = MazeUtils.getPosition(maze, -2);
        Coordinate end = MazeUtils.getPosition(maze, -3);

        int maxMoves = (int) (MAX_MOVES_MULTIPLIER * dimension.getH() * dimension.getW());

        long startTime = System.currentTimeMillis();

        if (start == null || end == null) {
            System.out.println("Error: Start or End point not found!");
            return;
        }

        // init
        Population pop = new Population();
        pop.populateFirstGen(POP_SIZE, maxMoves);

        Individual champOfTheChamp = null;
        GAStandardDeviation csd = new GAStandardDeviation();
        int gc = 0;
        double prevBestFitness = Double.NEGATIVE_INFINITY;
        int stagnationCounter = 0; // counts generations without improvement
        int champGen = 0;

        while (gc < MAX_GEN) {
            for (Individual ind : pop.getPops()) {
                evaluate(maze, dimension, ind, start);
            }

            pop.sortDescending();
            Individual champCandidate = pop.getPops().getFirst();

            // Track stagnation: if best fitness hasn't improved, increment counter
            if (champCandidate.getFitness() > prevBestFitness) {
                prevBestFitness = champCandidate.getFitness();
                stagnationCounter = 0;
            } else {
                stagnationCounter++;
            }

            csd.addFitness(champCandidate.getFitness());

            if (champOfTheChamp == null || champCandidate.compareTo(champOfTheChamp) > 0) {
                champOfTheChamp = new Individual(null);
                champOfTheChamp.copyFrom(champCandidate);
                champGen = gc;
                this.cost = champOfTheChamp.getCost();
                this.bestPath = getPathFromIndividual(maze, dimension, champOfTheChamp, start);
                if (ENABLE_DEBUG_PRINT) {
                    System.out.printf("[Gen #%d] new ChampOfTheChamp! fit: %.3f cost: %d%n", champGen,
                            champOfTheChamp.getFitness(), champOfTheChamp.getCost());
                    MazeUtils.printMaze(maze, dimension,
                            getPathFromIndividual(maze, dimension, champOfTheChamp, start));
                }
            }

            if (champOfTheChamp.isFinished()) {
                if (ENABLE_DEBUG_PRINT) {
                    if (champGen == gc)
                        System.out.printf("[Gen #%d] Solution found! cost: %d%n", gc, this.cost);
                }

                if (csd.isConverged()) {
                    System.out.printf("[Gen #%d] Terminated! Fitness converged (sd = %f)%n", gc, csd.getSD());
                    break;
                }
                // Additional early termination based on stagnation
                if (stagnationCounter >= STAGNATION_LIMIT) {
                    System.out.printf("[Gen #%d] Terminated! No fitness improvement for %d generations.%n",
                            gc, STAGNATION_LIMIT);
                    break;
                }
            }
            this.elapsed = System.currentTimeMillis() - startTime;
            if (this.elapsed >= AlgorithmConstant.TIME_LIMIT_MS) {
                System.out.printf("[Gen #%d] Terminated! Time limit of %d reached. (%d elapsed)%n", gc,
                        AlgorithmConstant.TIME_LIMIT_MS, this.elapsed);
                break;
            }

            pop = evolvePops(pop, gc, maxMoves);
            gc++;
        }

        System.out.printf("GA Conclusion: Ran %d gens, Best at #%d (%s)%n", gc, champGen,
                champOfTheChamp.isFinished() ? "finished" : "unfinished");
        this.elapsed = System.currentTimeMillis() - startTime;
    }

    public int getCost() {
        return this.cost;
    }

    public long getElapsedTime() {
        return this.elapsed;
    }

    public List<Coordinate> getPath() {
        return this.bestPath;
    }
}
