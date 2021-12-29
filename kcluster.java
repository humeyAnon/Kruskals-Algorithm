
// Main class - Reads in input file with regex and gets stations needed
//              If input file matches any "BAD" groups from the REGEX string it will terminate
//              And tell the user what needs to be deleted, if there are any empty line breaks the program will again error
//              Creates the weighted edges, Runs kruskals to get an ArrayList of clusters, creates the stations and interDistance
//              Then prints the output.

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class kcluster {

    public static String inputFile, programOutput;
    private static int stationsNeeded;
    private static long lineCount;
    public static ArrayList<HotSpot> hotspots = new ArrayList<>();
    public static ArrayList<Edge> edges = new ArrayList<>();
    public static ArrayList<ArrayList<HotSpot>> krustalClusters;
    private static ArrayList<Station> stations = new ArrayList<>();
    private static double minInterDistance = Double.MAX_VALUE;
    private static Kruskals kruskalAlgo;

    public static void main(String[] args) throws IOException {

        if(args.length != 2){

            System.err.println("Wrong input provided");
            System.exit(0);

        }

        Path path = Paths.get(args[0]);
        lineCount = Files.lines(path).count();

        readInFile(args[0]);
        stationsNeeded = Integer.parseInt(args[1]);

        runAlgorithm();
    }

    public static void readInFile(String path) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(path));
        inputFile = new String(encoded, StandardCharsets.UTF_8);

        String regexPattern = "(?<ID>[\\d])[,](?<XCOORD>[+]?[0-9]*\\.?[0-9]*)[,](?<YCOORD>[+]?[0-9]*\\.?[0-9]*)(?<BAD>[A-z\\p{P}\\p{S}]*)";
        Pattern inputPattern = Pattern.compile(regexPattern);
        Matcher inputMatcher = inputPattern.matcher(inputFile);


        int i = 0;
        while(inputMatcher.find()){

            hotspots.add(new HotSpot(Integer.parseInt(inputMatcher.group("ID")),
                             Float.parseFloat(inputMatcher.group("XCOORD")),
                             Float.parseFloat(inputMatcher.group("YCOORD"))));
            i++;
            String badInput = inputMatcher.group("BAD") ;

            if(!badInput.isEmpty()) {
                System.err.println("Input file is wrong, please get rid of: " + badInput + " - on line: " + i);
                System.exit(0);
            }
        }

        if(hotspots.size() != lineCount){
            System.err.println("Input file is incorrect");
            System.exit(0);
        }
    }

    public static void runAlgorithm() {

        programOutput = "Hello and Welcome to Kruskal's Clustering! \n\n" +
                        "The weighted graph of hotspots: \n\n";

        createWeightedGraph(hotspots);

        kruskalAlgo = new Kruskals(hotspots, edges, stationsNeeded);
        krustalClusters = kruskalAlgo.MSTclusters();

        createStations();
        interDistance();

        System.out.println(programOutput);
    }

    // Creates the weighted edges and adds the source and destination
    // Then adds it to the ArrayList of edges - concats each edge onto the output string
    private static void createWeightedGraph(ArrayList<HotSpot> hotspots) {

        for (int i = 0; i < hotspots.size(); i++) {
            for (HotSpot hotspot : hotspots) {
                // Adding the edge from vertex i - j
                edges.add(new Edge(hotspots.get(i), hotspot));
            }
        }

        int hotSpotNo = 0;
        for(int i = 0; i < edges.size(); i++) {

            if(hotSpotNo < hotspots.size()) {
                programOutput = programOutput.concat((edges.get(i).toString() + "   "));
                hotSpotNo++;
            }
            else {
                hotSpotNo = 1;
                programOutput = programOutput.concat("\n" + edges.get(i).toString() + "   ");
            }

        }

        programOutput = programOutput.concat("\n\nThere are " + hotspots.size() + " hotspots. \n" +
                                            "You have requested " + stationsNeeded + " temporary fire stations. \n\n");
    }

    // Find the centroid of the cluster to position the station
    // Adds to the ArrayList of station and concats onto output string
    public static void createStations() {

        int i = 0;
        for(ArrayList<HotSpot> clusters : krustalClusters) {

                ArrayList<HotSpot> servicedHotspots = new ArrayList<>();
                float avgX = 0;
                float avgY = 0;

                for (HotSpot h : clusters) {

                    avgX += h.getX();
                    avgY += h.getY();

                    servicedHotspots.add(h);
                }

                avgX = avgX / clusters.size();
                avgY = avgY / clusters.size();

                stations.add(new Station(avgX, avgY, servicedHotspots, i));

                programOutput = programOutput.concat("Station " + (i + 1) + ": \n" + "Coordinates: (" + String.format("%.2f", avgX) +
                    ", " + String.format("%.2f", avgY) + ") \n" + "Hotspots: (" + stations.get(i).getHotSpots() + ") \n\n");

                i++;
        }
    }

    // Calculate the interDistance for each cluster
    // concats onto the output string - If there is only 1 cluster
    // Dont calculate the inter distance as there is no other cluster to get from
    public static void interDistance() {
        if(stationsNeeded != 1) {
            for (ArrayList<HotSpot> clusters : krustalClusters) {
                // Get first cluster
                for (Edge e : edges) {
                    //Go through edges
                    if (clusters.contains(e.getVert1()) && !clusters.contains(e.getVert2())) {
                        double smallestDistance = Point2D.distance(e.getVert1().getX(), e.getVert1().getY(), e.getVert2().getX(), e.getVert2().getY());
                        if (minInterDistance > smallestDistance) {
                            minInterDistance = smallestDistance;
                        }
                    }
                }
            }
            programOutput = programOutput.concat(String.format("Intercluster distance: %.2f", minInterDistance));
        }
        else {
        programOutput = programOutput.concat("InterCluster distance: Only 1 cluster");
        }
    }

    // Helper function to read input file and return the string
    static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);

    }
    
}
