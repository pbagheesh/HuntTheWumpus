import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by prashant on 11/29/16.
 */
public class Graph {
    private ArrayList<Vertex> caves;

    public Graph () {
        //Constructor
        caves = new ArrayList<Vertex>();
    }

    public int vertexCount() {
        //Returns the number of vertices on the graph
        return this.caves.size();
    }

    public ArrayList<Vertex> getConnectedVertices (Vertex v) {
        //returns the connected Vertices in the graph
        ArrayList<Vertex> retVal = new ArrayList<Vertex>();
        for (Vertex v1: caves) {
            if (v1.getCost() > 0 && v1.getCost() != Integer.MAX_VALUE) {
                retVal.add(v1);
            }
        }
        return retVal;
    }

    public void setAllVisibility(boolean tf) {
        //Makes all the vertices on the graph visible - used for the hint key
        if (tf) {
            for (Vertex v: caves) {
                v.setVisible(true);
            }
        }
        else {
            for (Vertex v: caves) {
                v.setVisible(false);
            }
        }
    }

    public void setAllNonHunterVisited(boolean tf) {
        //Makes all the vertices that have not been visited by the hunter as visible so that you
        // will not loose all the progress you have made exploring the caves
        if (tf) {
            for (Vertex v: caves) {
                if (!v.getHunterVisited()) {
                    v.setVisible(true);
                }
            }
        }
        else {
            for (Vertex v: caves) {
                if (!v.getHunterVisited()) {
                v.setVisible(false);
                }
            }
        }    }

    public Vertex getDirection(Vertex v0, Vertex.Direction d) {
        //gets the vertex in the direction as stated by parameter
        for (Vertex v1 : v0.getNeighbors()) {
            if (d == Vertex.Direction.NORTH) {
                if (v1.getRow() == v0.getRow()-5) {
                    return v1;
                }
            }
            else if (d == Vertex.Direction.SOUTH) {
                if (v1.getRow() == v0.getRow()+5) {
                    return v1;
                }
            }
            else if (d == Vertex.Direction.EAST) {
                if (v1.getCol() == v0.getCol()+5) {
                    return v1;
                }
            }
            else if (d == Vertex.Direction.WEST) {
                if (v1.getCol() == v0.getCol()-5) {
                    return v1;
                }
            }
        }
        return null;
    }

    public int getLCost() {
        //gets the largest cost in the caves -- used to put a wumpus in an appropriate vertex
        int largestCost =0;
        for (Vertex v: caves) {
            if (v.getCost() > largestCost && v.getCost() != Integer.MAX_VALUE) {
                largestCost = v.getCost();
            }
        }
        return largestCost;
    }

    public void addEdge(Vertex v1, Vertex.Direction dir, Vertex v2) {
        //Connect Vertex 2 to vertex 1
        if (!caves.contains(v1)) { this.caves.add(v1); }
        if (!caves.contains(v2)) { this.caves.add(v2); }
        v1.connect(v2, dir);
        v2.connect(v1, v1.opposite(dir));
    }

    public void shortestPath(Vertex v0) {
        //Shortest path algorith implementation of Djirksta algortihm
        VertexComparator VC = new VertexComparator();
        PQHeap<Vertex> q = new PQHeap(VC);

        for (Vertex v : caves) {
            v.setCost(Integer.MAX_VALUE);
            v.setMarked(false);
        }

        v0.setCost(0);
        q.add(v0);

        while (q.size() > 0) {
            Vertex v = q.remove();
            v.setMarked(true);
            for (Vertex w : v.getNeighbors()) {
                if (w.getMarked()!= true && v.getCost()+1 < w.getCost()) {
                    w.setCost(v.getCost() +1);
                    q.add(w);
                }
            }
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph();
        Vertex v1 = new Vertex(1,1);
        System.out.println(v1.toString());
        Vertex v2 = new Vertex(1,2);
        Vertex v3 = new Vertex(1,3);
        Vertex v4 = new Vertex(1,4);
        Vertex v5 = new Vertex(1,5);

        g1.addEdge(v1, Vertex.Direction.SOUTH,v2);
        g1.addEdge(v2, Vertex.Direction.SOUTH,v3);
        g1.addEdge(v3, Vertex.Direction.SOUTH,v4);
        g1.addEdge(v4, Vertex.Direction.SOUTH,v5);

        g1.shortestPath(v1);
        System.out.println(v1.toString());
        System.out.println(v2.toString());
        System.out.println(v3.toString());
        System.out.println(v4.toString());
        System.out.println(v5.toString());
    }


}

