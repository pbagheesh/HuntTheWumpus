import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import javax.imageio.*;

/**
 * Created by prashant on 12/3/16.
 */
public class Hunter extends Agent {
    private Vertex cur;
    private ImageObserver imageO;
    private Image hunterImg;
    private Boolean hunting;
    private Boolean lifeStatus;
    private Boolean secondHunter;
    private int ammo;


    public Hunter (int row, int col, Vertex v, Boolean second) {
        //Hunter constructor that creates a hunter object on a vertex
        // Boolean second changes the image of the hunter accordingly to help players distinguish between the two
        super(row,col);
        cur = v;
        this.setRow((int)v.getRow());  //Makes sure that the field row and col are the same as specified vertex
        this.setCol((int)v.getCol());
        this.lifeStatus = true;
        this.secondHunter = second;
        this.ammo = 1;

        imageO = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        };

        try {
            if (!secondHunter) {
                hunterImg = ImageIO.read(new File("hunter1.jpg"));   //Cretaes different pictures different images if it is the first/second hunter
                hunting = false;
            }
            else {
                hunterImg = ImageIO.read(new File("secondHunter.jpg"));
                hunting = false;
            }
        }
        catch (IOException e) {}

    }

    //Accessor functions for hunting/lifestatus

    public void setHunting(boolean tf) {
        this.hunting = tf;
    }

    public int getAmmoVal() {return this.ammo;}

    public void decrementAmmoVal() {this.ammo--;}

    public boolean getHunting() {
        return this.hunting;
    }

    public boolean getLifeStatus() {return this.lifeStatus;}

    public void setLifeStatus(boolean tf) {this.lifeStatus = tf; }

    public void updateCur(Vertex c) {
        //Updates both the vertex object field but also the row and col fields of the object
        cur = c;
        this.setRow((int)c.getRow());   //Makes sure that field row and col are same as vertex row and col
        this.setCol((int)c.getCol());
    }

    public Vertex getCurrentVertex() {
        return cur;
    }

    public void draw(Graphics g, int gridScale) {
        //Draws the hunter image within the vertex
        if (this.lifeStatus) {
            this.cur.setVisible(true);
            int xpos = (int)cur.getCol()*gridScale;
            int ypos = (int)cur.getRow()*gridScale;
            int border = 2;
            g.drawImage(hunterImg, xpos+ gridScale/3, ypos+border,gridScale*border*2 - gridScale/3, 2*gridScale*border - gridScale/3,imageO);
        }
        else {
            System.out.println("You are Dead !!!");
        }

    }

}
