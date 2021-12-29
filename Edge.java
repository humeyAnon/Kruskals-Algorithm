
// Edge class - Stores all relevant information about the edges
//              Implements comparable to sort the edge by minimum weights
//              Uses java geom lib to find out the distance between 2 points

import java.awt.geom.Point2D;

public class Edge implements Comparable<Edge>{

    private double weight;
    private HotSpot vert1, vert2;

    public Edge(HotSpot vert1, HotSpot vert2){
        this.vert1 = vert1;
        this.vert2 = vert2;
        this.weight = Point2D.distance(vert1.getX(), vert1.getY(), vert2.getX(), vert2.getY());
    }

    public double getWeight() {
        return weight;
    }

    public int compareTo(Edge edge) {
        Edge e = edge;
        if(e.weight == this.weight){
            return 0;
        }
        return e.weight < this.weight ? 1 : -1;
    }

    public HotSpot getVert1() {
        return vert1;
    }

    public HotSpot getVert2() {
        return vert2;
    }

    public String toString(){

        String output = String.format("%.2f", this.weight);
        output = output.contains(".") ? output.replaceAll("0*$","").replaceAll("\\.$","   ") : output;

        return output;
    }
}
