package wroldmap;

import java.io.File;
import java.io.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import javafx.scene.paint.Color;

public class Dijkstra {

    static ArrayList<Country> countrys = new ArrayList<Country>();
    static HashMap<String, Country> allNodes = new HashMap<>();
    private Country source;
    private Country destination;
    private PriorityQueue<Country> Queue;   // all adj 

    public Dijkstra() {

    }

    public Dijkstra(ArrayList<Country> countrys, Country source, Country destination) { // table
        Queue = new PriorityQueue<>(countrys.size());
        this.destination = destination;
        this.countrys = countrys;
        for (Country country : countrys) {
            country.resetTemps();
            if (country == source) {
                country.setTempCost(0);
            }
//            System.out.println("country.getFullName();" + country.getFullName());
//           System.out.println("country.getTempCost()" + country.getTempCost());
            Queue.add(country);
        }
        //printQueue();
    }

    void printQueue() {
        System.out.println("Priority Queue contents:");
        for (Country country : Queue) {
            System.out.println(country.getFullName() + " : " + country.getTempCost());
        }
    }

    public void generateDijkstra() {
        // While there are still nodes to process in the priority queue
        while (!Queue.isEmpty()) {
            // Remove and return the country with the smallest temporary cost (minUnknown)
            Country minUnknown = Queue.poll();
                System.out.println("minUnknown"+ minUnknown);
                System.out.println(minUnknown.getAdjacentsList());
                System.out.println("-0-------------------------");
            // Get the list of adjacent countries and distances (edges) from minUnknown
            LinkedList<Edge> adjacentsList = minUnknown.getAdjacentsList();

            // For each edge in the adjacency list
            for (Edge edge : adjacentsList) {
                // Get the adjacent country connected by this edge
                Country adjacentCity = edge.getAdjacentCity();
                // System.out.println("++++++++++++" + edge + "----->" + adjacentCity.getAdjacentsList());

                // If the path through minUnknown to adjacentCity is shorter than the previously known path
                if ((minUnknown.getTempCost() + edge.getDistance()) < adjacentCity.getTempCost()) {

//                    System.out.println("minUnknown.getTempCost()" + minUnknown.getTempCost() + " edge.getDistance())" + edge.getDistance() + " < adjacentCity.getTempCost() --------> "
//                            + adjacentCity.getTempCost());
                    // Remove the adjacent city from the priority queue (to update its cost)
                    Queue.remove(adjacentCity);

                    // Update the cost to reach adjacentCity through minUnknown
                    adjacentCity.setTempCost(minUnknown.getTempCost() + edge.getDistance());

                    // Update the previous country on the shortest path to adjacentCity
                    adjacentCity.setTempPrev(minUnknown);

                    // Re-add the adjacent city to the priority queue with the updated cost
                    Queue.add(adjacentCity);
                }
            }
        }
    }

    private String pathString;
    String distanceString;

    public Country[] pathTo(Country destination) {
        LinkedList<Country> countries = new LinkedList<>();
        Country iterator = destination;                 //notFreq
        distanceString = String.format("%.2f", destination.getTempCost());
        while (iterator != source) {
            countries.addFirst(iterator);
            pathString = iterator.getFullName() + " : " + String.format("%.2f", iterator.getTempCost()) + "  KM" + "\n"
                    + "\t\t,  " + pathString;
            iterator = iterator.getTempPrev();
        }

        return countries.toArray(new Country[0]);
    }

    public static ArrayList<Country> readFile() throws FileNotFoundException {
        
        String line = null;
        int numberOfCountryes, numberOfEdges;
        File file = new File("data.txt");
        Scanner scan = new Scanner(file);
        line = scan.nextLine();
        String[] str = line.split(" ");

        numberOfCountryes = Integer.parseInt(str[0]);
        numberOfEdges = Integer.parseInt(str[1]);

        for (int i = 0; i < numberOfCountryes; i++) {
            float x, y;
            line = scan.nextLine();
            String[] strN = line.split(" ");
            x = (float) Double.parseDouble(strN[1]);
            y = (float) Double.parseDouble(strN[2]);
       
            Country newCountry = new Country(strN[0], x, y);
            countrys.add(newCountry);
            allNodes.putIfAbsent(strN[0], newCountry);
            
        }
        for (int i = 0; i < numberOfEdges; i++) {
            line = scan.nextLine();
            String[] strN = line.split(" ");
            String fromCityName = strN[0], toCityName = strN[1];
            Country fromCity = allNodes.get(fromCityName), toCity = allNodes.get(toCityName);
            double distance = distance(fromCity.x, fromCity.y, toCity.x, toCity.y);
            fromCity.addAdjacentCity(toCity, distance);
        }

//        System.out.println("Contents of allNodes hash map:");
//        for (Map.Entry<String, Country> entry : allNodes.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//        }
        return countrys;
    }


    public static double distance(double x1, double y1, double x2, double y2) {
        return 6378.8 * Math.acos((Math.sin(Math.toRadians(y1)) * Math.sin(Math.toRadians(y2))) + Math.cos(Math.toRadians(y1))
                * Math.cos(Math.toRadians(y2)) * Math.cos(Math.toRadians(x1) - Math.toRadians(x2)));
    }
    
    
    
    
    
    
    
    
    
    
    
    
     public String getPathString() {
        if (countOccurrences(pathString, '\n') <= 1) {
            pathString = "No Path";
            distanceString = "\t\t\t------------------";
            return pathString;
        }

        pathString = "\t" + pathString;

        int truncateIndex = pathString.lastIndexOf('\n');
        pathString = pathString.substring(0, truncateIndex);

        return pathString;
    }

    private static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }
}
//
