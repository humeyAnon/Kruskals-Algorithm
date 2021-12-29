
// Kruskals class - Class takes in the hotspots, edges and how many stations are needed in the constructor
//                  Then completes Kruskals to gather the MST cluster/s and returns the appropriate arraylist
//                  of clusters to the main class

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Kruskals {

    private ArrayList<HotSpot> hotspots;
    private ArrayList<Edge> edges;
    private ArrayList<ArrayList<HotSpot>> kruskalClusters = new ArrayList<>();
    private ArrayList<ArrayList<HotSpot>> tempClusters = new ArrayList<>();
    private int count = 0, stationsNeeded;

    public Kruskals(ArrayList<HotSpot> hotspots, ArrayList<Edge> edges, int stationsNeeded) {

        this.hotspots = hotspots;
        this.edges = edges;
        this.stationsNeeded = stationsNeeded;

    }

    // Makes each vertex its own tree - Sorts the edges arraylist from minimum weight to max
    // Runs Kruskals while the amount of clusters does not equal how many clusters the ArrayList has
    // If an edge destination is not in the cluster add it to the cluster with union
    // Once we have enough clusters it breaks the loop, sorts the clusters by order then returns back to main file
    public ArrayList<ArrayList<HotSpot>> MSTclusters() {

        if(stationsNeeded > hotspots.size() || stationsNeeded < 1){
            System.err.println(stationsNeeded + " stations is not viable, must be between 1 - number of hotspots");
            System.exit(0);
        }

        for(HotSpot v : hotspots){
            ArrayList<HotSpot> tree = makeSet(v);
            tempClusters.add(tree);
        }

        Collections.sort(edges);

        while (tempClusters.size() != stationsNeeded) {

            if (findSet(tempClusters, edges.get(count).getVert1()) != findSet(tempClusters, edges.get(count).getVert2())) {

                union(edges.get(count).getVert1(), edges.get(count).getVert2(), findCluster(edges.get(count).getVert2()));
            }
            count += 1;
        }

        sortClusters();
        return tempClusters;
    }

    // Method finds the index where HotSpot v2 is, needed for union to remove the
    // edge destination after merging it to another MST
    public int findCluster(HotSpot v2) {
        int count = 0;
        for(ArrayList<HotSpot> t : tempClusters) {
            if(t.contains(v2)) {
                return count;
            }
            count++;
        }
        return 0;
    }

    // Method finds the cluster where vertex one is located and adds the new vertex needed
    // Then removes the vertex from the original list as its not merged with another MST
    // If the list is empty then remove
    private ArrayList<HotSpot> union(HotSpot vert1, HotSpot vert2, int i) {
        
        ArrayList<HotSpot> q = findSet(tempClusters,vert1);

        q.add(vert2);

        tempClusters.get(i).remove(vert2);
        tempClusters.removeIf(ArrayList::isEmpty);

        return q;
    }

    // Method returns a new tree list with the hotspot given
    public ArrayList<HotSpot> makeSet(HotSpot v) {
        return new ArrayList<>(Arrays.asList(v));
    }

    // Returns the list where the hotspot is located
    public ArrayList<HotSpot> findSet(ArrayList<ArrayList<HotSpot>> clusters, HotSpot v) {
        for(ArrayList<HotSpot> t : clusters){
            if(t.contains(v)){
                return t;
            }
        }
        return null;

    }

    // Helper function to sort the clusters by order in case they are jumbled up
    public void sortClusters() {

        Collections.sort(kruskalClusters, Comparator.comparingInt(h -> h.get(0).getHotSpotID()));

    }
}
