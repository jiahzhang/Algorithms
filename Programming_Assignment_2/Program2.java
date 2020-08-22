import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.*;

public class Program2 {
    private ArrayList<City> cities;     //this is a list of all cities, populated by Driver class.
    private Heap minHeap;

    // feel free to add any fields you'd like, but don't delete anything that's already here

    public Program2(int numCities) {
        minHeap = new Heap();
        cities = new ArrayList<City>();
    }

    /**
     * findCheapestPathPrice(City start, City dest)
     *
     * @param start - the starting city.
     * @param dest  - the end (destination) city.
     * @return the minimum cost possible to get from start to dest.
     * If no path exists, return Integer.MAX_VALUE
     */
    public int findCheapestPathPrice(City start, City dest) {
        // use Dijkstra's algorithm
        //build minHeap
        minHeap = new Heap();
        for(int i = 0; i < cities.size(); i++){
            cities.get(i).setMinCost(Integer.MAX_VALUE);
            cities.get(i).setPi(null);
        }
        minHeap.buildHeap(cities);
        minHeap.changeKey(start, 0);
        while (minHeap.toArrayList().size() > 0) {
            City temp = minHeap.extractMin();

            //relaxation on an edge
            for (int i = 0; i < temp.getNeighbors().size(); i++) {
                int newWeight = temp.getMinCost() + temp.getWeights().get(i);
                if(temp.getMinCost() == Integer.MAX_VALUE){
                    newWeight = Integer.MAX_VALUE;
                }
                //ties don't matter here
                if (temp.getNeighbors().get(i).getMinCost() > newWeight) {
                    minHeap.changeKey(temp.getNeighbors().get(i), newWeight);
                }
            }
        }

        //will return Integer.MAX_VALUE if no path exists
        return dest.getMinCost();
    }

    /**
     * findCheapestPath(City start, City dest)
     *
     * @param start - the starting city.
     * @param dest  - the end (destination) city.
     * @return an ArrayList of nodes representing a minimum-cost path on the graph from start to dest.
     * If no path exists, return null
     */
    public ArrayList<City> findCheapestPath(City start, City dest) {
        // use Dijkstra's algorithm
        //build minHeap
        minHeap = new Heap();
        for(int i = 0; i < cities.size(); i++){
            cities.get(i).setMinCost(Integer.MAX_VALUE);
            cities.get(i).setPi(null);
        }
        minHeap.buildHeap(cities);
        minHeap.changeKey(start, 0);
        while (minHeap.toArrayList().size() > 0) {
            City temp = minHeap.extractMin();
            //relaxation on an edge
            for (int i = 0; i < temp.getNeighbors().size(); i++) {
                int newWeight = temp.getMinCost() + temp.getWeights().get(i);
                if(temp.getMinCost() == Integer.MAX_VALUE){
                    newWeight = Integer.MAX_VALUE;
                }
                //ties don't matter here
                if (temp.getNeighbors().get(i).getMinCost() > newWeight) {
                    temp.getNeighbors().get(i).setPi(temp);
                    minHeap.changeKey(temp.getNeighbors().get(i), newWeight);
                }
            }
        }

        //test if path exists
        if(dest.getMinCost() == Integer.MAX_VALUE){
            return null;
        }

        //construct reversed path
        ArrayList<City> reversedPath = new ArrayList<City>();
        City temp = dest;
        reversedPath.add(temp);
        while(temp != start){
            reversedPath.add(temp.getPi());
            temp = temp.getPi();
        }

        //reverse path
        ArrayList<City> path = new ArrayList<City>();
        for(int i = reversedPath.size() - 1; i >= 0; i--){
            path.add(reversedPath.get(i));
        }

        //will return Integer.MAX_VALUE if no path exists
        return path;
    }

    /**
     * findLowestTotalCost()
     *
     * @return The sum of all edge weights in a minimum spanning tree for the given graph.
     * Assume the given graph is always connected.
     * The government wants to shut down as many tracks as possible to minimize costs.
     * However, they can't shut down a track such that the cities don't remain connected.
     * The tracks you're leaving open cost some money (aka the edge weights) to maintain. Minimize the overall cost.
     */
    public int findLowestTotalCost() {
        // use prim's algorithm
        minHeap = new Heap();
        for(int i = 0; i < cities.size(); i++){
            cities.get(i).setMinCost(Integer.MAX_VALUE);
            cities.get(i).setPi(null);
        }
        minHeap.buildHeap(cities);
        minHeap.changeKey(cities.get(0), 0);

        //if value = 1, then city is still in heap
        int[] existence = new int[cities.size()];
        Arrays.fill(existence, 1);

        while(minHeap.toArrayList().size() > 0){
            City temp = minHeap.extractMin();
            existence[temp.getCityName()] = 0;

            for(int i = 0; i < temp.getNeighbors().size(); i++){
                City adj = temp.getNeighbors().get(i);
                int weight = temp.getWeights().get(i);
                if(existence[adj.getCityName()] == 1  && weight < adj.getMinCost()){
                    minHeap.changeKey(adj, weight);
                }
            }
        }

        int total = 0;
        //sum weights to obtain total cost
        for(City c : cities){
            total += c.getMinCost();
        }

        return total;
    }

    //returns edges and weights in a string.
    public String toString() {
        String o = "";
        for (City v : cities) {
            boolean first = true;
            o += "City ";
            o += v.getCityName();
            o += " has neighbors: ";
            ArrayList<City> ngbr = v.getNeighbors();
            for (City n : ngbr) {
                o += first ? n.getCityName() : ", " + n.getCityName();
                first = false;
            }
            first = true;
            o += " with weights ";
            ArrayList<Integer> wght = v.getWeights();
            for (Integer i : wght) {
                o += first ? i : ", " + i;
                first = false;
            }
            o += System.getProperty("line.separator");

        }

        return o;
    }

///////////////////////////////////////////////////////////////////////////////
//                           DANGER ZONE                                     //
//                everything below is used for grading                       //
//                      please do not change :)                              //
///////////////////////////////////////////////////////////////////////////////

    public Heap getHeap() {
        return minHeap;
    }

    public ArrayList<City> getAllCities() {
        return cities;
    }

    //used by Driver class to populate each Node with correct neighbors and corresponding weights
    public void setEdge(City curr, City neighbor, Integer weight) {
        curr.setNeighborAndWeight(neighbor, weight);
    }

    //This is used by Driver.java and sets vertices to reference an ArrayList of all nodes.
    public void setAllNodesArray(ArrayList<City> x) {
        cities = x;
    }
}
