// feel free to add things to this file. Just don't remove anything

import java.util.*;

public class City {
    private int minCost;
    private int cityName;
    private City pi;
    private ArrayList<City> neighbors;
    private ArrayList<Integer> weights;

    public City(int x) {
        cityName = x;
        minCost = Integer.MAX_VALUE;
        pi = null;
        neighbors = new ArrayList<City>();
        weights = new ArrayList<Integer>();
    }

    public void setNeighborAndWeight(City n, Integer w) {
        neighbors.add(n);
        weights.add(w);
    }

    public ArrayList<City> getNeighbors() {
        return neighbors;
    }

    public ArrayList<Integer> getWeights() {
        return weights;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setMinCost(int x) {
        minCost = x;
    }

    public void resetMinCost() {
        minCost = Integer.MAX_VALUE;
    }

    public int getCityName() {
        return cityName;
    }

    public City getPi(){ return pi; }

    public void setPi(City pi) { this.pi = pi; }
}
