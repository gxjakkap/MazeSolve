package me.guntxjakka.MazeSolve.Algorithms.GeneticsAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population {
    private List<Individual> pops;

    public Population() {
        this.pops = new ArrayList<>();
    }

    public void addIndividual(Individual x) {
        this.pops.add(x);
    }

    public List<Individual> getPops() {
        return pops;
    }

    public void sortDescending() {
        Collections.sort(this.pops, Collections.reverseOrder());
    }

    public void populateFirstGen(int sz, int gLen) {
        for (int i = 0; i < sz; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < gLen; j++) {
                sb.append(GeneticsAlgorithm.MV[(int) Math.floor(Math.random() * GeneticsAlgorithm.MV.length)]);
            }
            this.pops.add(new Individual(sb.toString()));
        }
    }
}
