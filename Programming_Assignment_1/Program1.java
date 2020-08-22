/*
 * Name: Jiahan Zhang
 * EID: jz23745
 */

import java.util.*;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the Stable Marriage problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching marriage) {
        if(marriage.getEmployeeMatching() == null){
            return false;
        }
        //check if the number of employees matched to a location matches the number of slots
        List<Integer> numOpenings = new ArrayList<>(marriage.getLocationSlots());
        for(int s = 0; s < marriage.getEmployeeMatching().size(); s++){
            int h = marriage.getEmployeeMatching().get(s);
            if(h != -1) {
                numOpenings.set(h, numOpenings.get(h) - 1);
            }
        }
        for(int h = 0; h < numOpenings.size(); h++){
            if(numOpenings.get(h) != 0){
                return false;
            }
        }

        //check for instability
        for(int s = 0; s < marriage.getEmployeeMatching().size(); s++){
            //which h matched with
            int h = marriage.getEmployeeMatching().get(s);
            if(h == -1){
                continue;
            }
            for(int sPrime = 0; sPrime < marriage.getEmployeeMatching().size(); sPrime++){
                if(sPrime == s){
                    continue;
                }
                //which h' matched with
                int hPrime = marriage.getEmployeeMatching().get(sPrime);
                //h prefers s' to s
                if(marriage.getLocationPreference().get(h).indexOf(s) > marriage.getLocationPreference().get(h).indexOf(sPrime)){
                    //s' prefers h to h' OR s' is not assigned to a store
                    if(hPrime == -1){
                        return false;
                    }
                    if(marriage.getEmployeePreference().get(sPrime).indexOf(hPrime) > marriage.getEmployeePreference().get(sPrime).indexOf(h)){
                        //instability detected
                        //System.out.println(s + " " + h + " " + sPrime + " " + hPrime);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines a employee optimal solution to the Stable Marriage problem from the given input set.
     * Study the description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_employeeoptimal(Matching marriage) {
        ArrayList<Integer> employeeMatching = new ArrayList<>();
        int numEmployees = marriage.getEmployeeCount();
        int numLocations = marriage.getLocationCount();

        //Add all employees to queue
        List<Integer> queue = new ArrayList<>();
        //Get number of slots at each location
        List<Integer> numOpenings = new ArrayList<>(marriage.getLocationSlots());
        //Create preference list counter for each employee
        int[] counter = new int[numEmployees];
        //Create location openings counter for each employee
        int[] locationSlotCounter = new int[numEmployees];
        //Create to hold temp matchings
        List<List<Integer>> matchingTemp = new ArrayList<>();

        //Create a reverse preference list for locations as well as temporary matchings list
        ArrayList<ArrayList<Integer>> locationRPL = new ArrayList<>();
        for(int i = 0; i < numLocations; i++){
            matchingTemp.add(new ArrayList<>());
            locationRPL.add(new ArrayList<>());
            for(int j = 0; j < numEmployees; j++){
                //slots for each location
                matchingTemp.get(i).add(-1);
                locationRPL.get(i).add(0);
            }
            for(int j = 0; j < numEmployees; j++){
                locationRPL.get(i).set(marriage.getLocationPreference().get(i).get(j), j);
            }
        }

        for(int i = 0; i < numEmployees; i++){
            queue.add(i);
            counter[i] = 0;
            employeeMatching.add(-1);
        }

        marriage.setEmployeeMatching(employeeMatching);

        while(!queue.isEmpty()){
            int employee = (int) queue.get(0);
            int location = counter[employee];

            //remove currentEmployee from queue
            queue.remove(0);

            //check if employee has exhausted list
            if(location > numLocations - 1){
                continue;
            }

            location = marriage.getEmployeePreference().get(employee).get(location);
            int rank = locationRPL.get(location).get(employee);
            //only necessary if location is full
            int oldEmployee = matchingTemp.get(location).get(locationSlotCounter[employee]);
            int oldEmployeeRank;
            if(oldEmployee == -1){
                oldEmployeeRank = numEmployees;
            }
            else{
                oldEmployeeRank = locationRPL.get(location).get(oldEmployee);
            }

            if(oldEmployeeRank > rank){
                //match employee to location if higher than lowest rank (and replace)
                marriage.getEmployeeMatching().set(employee, location);
                matchingTemp.get(location).set(locationSlotCounter[employee], employee);

                if(oldEmployee != -1) {
                    marriage.getEmployeeMatching().set(oldEmployee, -1);
                    queue.add(oldEmployee);
                }
            }
            else{
                //add current employee back into queue
                queue.add(employee);
            }

            //update counters
            if(marriage.getLocationSlots().get(location) - 1 == locationSlotCounter[employee]){
                //reset slot and increment location
                locationSlotCounter[employee] = 0;
                counter[employee]++;
            }
            else{
                //next slot
                locationSlotCounter[employee]++;
            }
        }

        return marriage;
    }

    /**
     * Determines a location optimal solution to the Stable Marriage problem from the given input set.
     * Study the description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMarriageGaleShapley_locationoptimal(Matching marriage) {
        ArrayList<Integer> employeeMatching = new ArrayList<>();
        int numEmployees = marriage.getEmployeeCount();
        int numLocations = marriage.getLocationCount();

        //Add all locations to queue
        List<Integer> queue = new ArrayList<>();
        //Get number of slots at each location
        List<Integer> numOpenings = new ArrayList<>(marriage.getLocationSlots());
        //Create preference list counter for each location
        int[] counter = new int[numLocations];

        //Create a reverse preference list for employees and add -1 for each employee in employeematching
        ArrayList<ArrayList<Integer>> employeeRPL = new ArrayList<>();
        for(int i = 0; i < numEmployees; i++){
            employeeMatching.add(-1);
            employeeRPL.add(new ArrayList<>());

            for(int j = 0; j < numLocations; j++){
                employeeRPL.get(i).add(0);
            }
            for(int j = 0; j < numLocations; j++){
                employeeRPL.get(i).set(marriage.getEmployeePreference().get(i).get(j), j);
            }
        }

        for(int i = 0; i < numLocations; i++){
            queue.add(i);
            counter[i] = 0;
        }

        marriage.setEmployeeMatching(employeeMatching);

        while(!queue.isEmpty()){
            int location = queue.get(0);

            while(numOpenings.get(location) > 0){
                int employee = counter[location];

                //propose to employee
                employee =  marriage.getLocationPreference().get(location).get(employee);

                //obtain ranks of locations
                int currentRank = employeeRPL.get(employee).get(location);
                int oldLocation = marriage.getEmployeeMatching().get(employee);

                //test if not matched yet
                if(oldLocation == -1){
                    //decrement number of openings and match employee and location
                    numOpenings.set(location, numOpenings.get(location) - 1);
                    marriage.getEmployeeMatching().set(employee, location);
                }
                else {
                    //employee already matched get location rank
                    int oldRank = employeeRPL.get(employee).get(oldLocation);
                    if(oldRank > currentRank) {
                        //decrement number of openings and match employee and location
                        numOpenings.set(location, numOpenings.get(location) - 1);
                        marriage.getEmployeeMatching().set(employee, location);

                        numOpenings.set(oldLocation, numOpenings.get(oldLocation) + 1);
                        queue.add(oldLocation);
                    }
                }

                //increment counter
                counter[location]++;
            }

            //done with this location - remove from queue
            queue.remove(0);
        }

        return marriage;
    }
}
