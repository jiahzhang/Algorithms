import java.util.ArrayList;

public class Heap {
    private ArrayList<City> minHeap;
    private ArrayList<Integer> position;

    public Heap() {
        minHeap = new ArrayList<City>();
        position = new ArrayList<Integer>();
    }

    /**
     * buildHeap(ArrayList<City> cities)
     * Given an ArrayList of Cities, build a min-heap keyed on each City's minCost
     * Time Complexity - O(n)
     *
     * @param cities
     */
    public void buildHeap(ArrayList<City> cities) {
        minHeap.clear();
        position.clear();

        for(int i = 0; i < cities.size(); i++){
            position.add(-1);
        }

        for (City city : cities) {
            insertNode(city);
        }
    }

    /**
     * heapifyUp(int index)
     * Moves damaged part upward if needed
     * Time Complexity - O(log(n))
     */
    private void heapifyUp(int index){
        if(index > minHeap.size() - 1){
            return;
        }

        if(index > 0){
            //get parent index
            int j = (index - 1)/2;
            //compare keys
            if(minHeap.get(index).getMinCost() < minHeap.get(j).getMinCost()){
                //swap entries
                City temp = minHeap.get(index);

                position.set(minHeap.get(index).getCityName(), j);
                position.set(minHeap.get(j).getCityName(), index);

                minHeap.set(index, minHeap.get(j));
                minHeap.set(j, temp);
                heapifyUp(j);
            }
            //break ties
            else if((minHeap.get(index).getMinCost() == minHeap.get(j).getMinCost()) && (minHeap.get(index).getCityName() < minHeap.get(j).getCityName())){
                //swap entries
                City temp = minHeap.get(index);

                position.set(minHeap.get(index).getCityName(), j);
                position.set(minHeap.get(j).getCityName(), index);

                minHeap.set(index, minHeap.get(j));
                minHeap.set(j, temp);
                heapifyUp(j);
            }
        }
    }

    /**
     * insertNode(City in)
     * Insert a City into the heap.
     * Time Complexity - O(log(n))
     *
     * @param in - the City to insert.
     */
    public void insertNode(City in) {
        //insert into last position
        minHeap.add(in);
        position.set(in.getCityName(), minHeap.size() - 1);

        //fix heap with heapify-up
        heapifyUp(minHeap.size() - 1);
    }

    /**
     * findMin()
     *
     * @return the minimum element of the heap. Must run in constant time.
     */
    public City findMin() {
        return minHeap.get(0);
    }

    /**
     * extractMin()
     * Time Complexity - O(log(n))
     *
     * @return the minimum element of the heap, AND removes the element from said heap.
     */
    public City extractMin() {
        City temp = findMin();
        delete(0);
        return temp;
    }

    /**
     * heapifyUp(int index)
     * Moves damaged part downward if needed
     * Time Complexity - O(log(n))
     * @param index
     */
    private void heapifyDown(int index){
        int n = minHeap.size() - 1;
        int j = 0;
        //no children then do nothing
        if(2 * index + 1 > n){
            return;
        }
        //two children
        else if(2*index + 1 < n){
            City left = minHeap.get(2*index + 1);
            City right = minHeap.get(2*index + 2);
            if(left.getMinCost() < right.getMinCost()){
                j = 2*index + 1;
            }
            else if(left.getMinCost() > right.getMinCost()){
                j = 2*index + 2;
            }
            else if ((left.getMinCost() == right.getMinCost()) && (left.getCityName() < right.getCityName())){
                j = 2*index + 1;
            }
            else{
                j = 2*index + 2;
            }
        }
        //one child
        else if(2*index + 1 == n){
            j = 2*index + 1;
        }
        //swap if key is less
        if(minHeap.get(j).getMinCost() < minHeap.get(index).getMinCost()){
            City temp = minHeap.get(index);

            position.set(minHeap.get(index).getCityName(), j);
            position.set(minHeap.get(j).getCityName(), index);

            minHeap.set(index, minHeap.get(j));
            minHeap.set(j, temp);
            heapifyDown(j);
        }
        else if((minHeap.get(j).getMinCost() == minHeap.get(index).getMinCost()) && (minHeap.get(j).getCityName() < minHeap.get(index).getCityName())){
            City temp = minHeap.get(index);

            position.set(minHeap.get(index).getCityName(), j);
            position.set(minHeap.get(j).getCityName(), index);

            minHeap.set(index, minHeap.get(j));
            minHeap.set(j, temp);
            heapifyDown(j);
        }
    }

    /**
     * delete(int index)
     * Deletes an element in the min-heap given an index to delete at.
     * Time Complexity - O(log(n))
     *
     * @param index - the index of the item to be deleted in the min-heap.
     */
    public void delete(int index) {
        //move last element to index and delete last element
        int last = minHeap.get(index).getCityName();

        minHeap.set(index, minHeap.get(minHeap.size() - 1));
        position.set(minHeap.get(minHeap.size() - 1).getCityName(), index);

        position.set(last, -1);
        minHeap.remove(minHeap.size() - 1);

        //key is smaller than parent - call heapifyUp
        heapifyUp(index);
        //key is larger than children - call heapifyDown
        heapifyDown(index);
    }


    /**
     * changeKey(City c, int newCost)
     * Updates and rebalances a heap for City c.
     * Time Complexity - O(log(n))
     *
     * @param c       - the city in the heap that needs to be updated.
     * @param newCost - the new cost of city c in the heap (note that the heap is keyed on the values of minCost)
     */
    public void changeKey(City c, int newCost) {
        // find index of city in heap
        //int index = position.get(c.getCityName());
        int index = position.get(c.getCityName());
        c.setMinCost(newCost);
        if(index == -1){
            return;
        }
        delete(index);
        insertNode(c);
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < minHeap.size(); i++) {
            output += minHeap.get(i).getCityName() + " ";
        }
        return output;
    }

///////////////////////////////////////////////////////////////////////////////
//                           DANGER ZONE                                     //
//                everything below is used for grading                       //
//                      please do not change :)                              //
///////////////////////////////////////////////////////////////////////////////

    public ArrayList<City> toArrayList() {
        return minHeap;
    }
}
