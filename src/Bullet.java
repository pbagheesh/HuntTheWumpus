/**
 * Created by prashant on 12/5/16.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Bullet extends Agent {
    private Vertex cur;
    private Image bulletImg;
    private ImageObserver imageO;

    public Bullet (int row, int col, Vertex v) {
        //Bullet Object that is an extension of the agent class and is used to check if the wumpus is
        //in the path of the bullet
        super(row, col);
        cur = v;
        try {
            bulletImg = ImageIO.read(new File("bullet.png"));
        } catch (IOException e) {}

        imageO = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        };
    }

    public Vertex getCurVer() {
        //Return the current vertex
        return this.cur;
    }

    public void updateCur(Vertex c) {
        //Update the current Vertex
        cur = c;
        if (c != null) {
            this.setRow((int)c.getRow());   //Makes sure that field row and col are same as vertex row and col
            this.setCol((int)c.getCol());
        }
    }

    public void draw(Graphics g, int gridScale) {
        //Draws the Bullet when called.
        if (cur != null) {
            int xpos = (int)cur.getCol()*gridScale;
            int ypos = (int)cur.getRow()*gridScale;
            int border = 2;
            g.drawImage(bulletImg, xpos+ gridScale/3, ypos+border,gridScale*border*2 - gridScale/3, 2*gridScale*border - gridScale/3,imageO);
        }
    }
}
