import java.awt.*;
import java.util.HashMap;
import java.util.Collection;

/**
 * Created by prashant on 11/29/16.
 */
public class Vertex extends Agent {

    public enum Direction {NORTH, EAST, WEST, SOUTH,NONE};
    private HashMap<Direction, Vertex> edges;
    private int cost;
    private boolean marked;
    private String label;
    private boolean visible;
    private boolean hunterVisible;

    public static Direction opposite (Direction d) {
        //Returns the opposite direction of parameter
        if (d == Direction.NORTH) {
            return Direction.SOUTH;
        }
        if (d == Direction.SOUTH) {
            return Direction.NORTH;
        }
        if (d == Direction.EAST) {
            return Direction.WEST;
        }
        if (d == Direction.WEST) {
            return Direction.EAST;
        }
        if (d == Direction.NONE) {
            return Direction.NONE;
        }
        return null;
    }

    public Vertex(int row, int col) {
        //Constructor
        super(row,col);
        this.edges = new HashMap<Direction,Vertex>();
        this.cost = 0;
        this.marked = false;
        this.label = "";
        this.visible = false;
    }

    public Vertex(int row, int col, String label) {
        //Constructor for vertexes with label
        super(row,col);
        this.edges = new HashMap<Direction,Vertex>();
        this.cost = 0;
        this.marked = false;
        this.label = label;
        this.visible = false;
    }

    //Next four methods are accessor functions for cost and marked

    public void setCost (int c) {
        this.cost = c;
    }

    public int getCost() {
        return this.cost;
    }

    public void setMarked( boolean tf) {
        this.marked = tf;
    }

    public boolean getMarked() {
        return this.marked;
    }

    public void setVisible(boolean tf) {this.visible = tf;}

    public void setHunterVisited(boolean tf) {this.hunterVisible = tf;}

    public boolean getHunterVisited() {return this.hunterVisible;}

    public void connect(Vertex other, Direction dir) {
        //Connect a new vertex in a specific direction to the current vertex
        edges.put(dir, other);
    }

    public Vertex getNeighbor(Direction dir) {
        //Returns the neighbor in a specific direction
        return edges.get(dir);
    }

    public Collection<Vertex> getNeighbors() {
        //return all the neighbors
        return this.edges.values();
    }

    public void draw(Graphics g, int gridScale) {
        if (!this.visible) {
            return;
        }
        int xpos = (int)this.getCol()*gridScale;
        int ypos = (int)this.getRow()*gridScale;
        int border = 2;
        int half = gridScale / 2;
        int eighth = gridScale / 8;
        int sixteenth = gridScale / 16;

        g.setColor(Color.WHITE);
        if (this.edges.containsKey(Direction.NORTH)) {
            g.fillRect(xpos + 12*gridScale/7, ypos - 7*gridScale/7, 5*gridScale/7, 10*gridScale/7);
        }
        if (this.edges.containsKey(Direction.SOUTH)){
            g.fillRect(xpos + 12*gridScale/7, ypos + 27*gridScale/7, 5*gridScale/7, 10*gridScale/7);
        }
        if (this.edges.containsKey(Direction.WEST))
            g.fillRect(xpos - 7*gridScale/7, ypos + 11*gridScale/7, 20*gridScale/7,5*gridScale/7);
        if (this.edges.containsKey(Direction.EAST))   //How does this work even without the {} ?
            g.fillRect(xpos+ 22*gridScale/7, ypos + 11*gridScale/7, 20*gridScale/7,5*gridScale/7);

        g.setColor(Color.lightGray);
        g.fillRect(xpos,ypos, (int)(gridScale*2)*border,(int)(gridScale*2)* border);
        // draw rectangle for the walls of the cave
        if (this.cost <= 2) {
            // wumpus is nearby
            g.setColor(Color.RED);
        }
        else {
            // wumpus is not nearby
            g.setColor(Color.black);
        }

        g.drawRect(xpos, ypos, (int)(gridScale*2)*border,(int)(gridScale*2)* border);

    }

    public String toString () {
        String retval = "Num of Neighbors : " + edges.size() + " Cost : " + this.cost + " Marked : " + this.marked +
                "\n Row = " + this.getRow() + " Col = " + this.getCol();

        return retval;
    }

    public static void main(String[] args) {
        Vertex V1 = new Vertex(1,1);
        Vertex V2 = new Vertex(1,2);

        VertexComparator VC = new VertexComparator();

        System.out.println(V1);
        V1.connect(V2, Direction.NORTH);
        V1.setCost(4);
        System.out.println(V1.opposite(Direction.NORTH));
        System.out.println(V1.opposite(Direction.SOUTH));
        System.out.println(V1.opposite(Direction.EAST));
        System.out.println(V1.opposite(Direction.WEST));
        System.out.println(V1);
        System.out.println(VC.compare(V1,V2));
    }
}

class VertexComparator implements java.util.Comparator<Vertex> {
    public int compare( Vertex v1, Vertex v2 ) {
        // returns negative 1 if Vertex 2 cost is smaller than vertex 1 cost
        // 0 if same
        // 1 if V1 > V2
        int i1 = v1.getCost();
        int i2 = v2.getCost();
        if (i2 > i1) {
            return -1;
        }
        else if (i1 == i2) {
            return 0;
        }
        else {
            return 1;
        }
    }
}