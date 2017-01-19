import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

/**
 * Created by prashant on 12/3/16.
 */
public class Wumpus extends Agent {
    private Vertex cur;
    private Image wumpusWin;
    private Image wumpusLoose;
    private ImageObserver imageO;
    private boolean lifeStatus;
    private boolean visibility;

    public Wumpus (int row, int col, Vertex v) {
        super(row,col);
        cur = v;
        this.setRow((int)cur.getRow());
        this.setCol((int)cur.getCol());
        imageO = new ImageObserver() {
            @Override
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        };
        lifeStatus = true;

        try {
            wumpusWin = ImageIO.read(new File("wumpus.png"));
            wumpusLoose = ImageIO.read(new File("wumpusL.jpg"));
        } catch (IOException e) {}
    }

    public Vertex getCurVer() {
        return cur;
    }

    public void updateCur(Vertex c) {
        cur = c;
        this.setRow((int)cur.getRow());
        this.setCol((int)cur.getCol());
    }

    public void updateLifeStatus(boolean tf) {
        this.lifeStatus = tf;
    }

    public void updateVisibility(boolean tf) {
        this.visibility = tf;
    }

    public void draw(Graphics g, int gridScale) {
        int xpos = (int)cur.getCol()*gridScale;
        int ypos = (int)cur.getRow()*gridScale;
        int border = 2;

        if (visibility) {
            if (lifeStatus) {
                g.drawImage(wumpusWin, xpos + gridScale / 6, ypos + border - gridScale / 6, gridScale * border * 2 - gridScale / 6, 2 * gridScale * border - gridScale / 6, imageO);
            } else {
                g.drawImage(wumpusLoose, xpos + gridScale / 3, ypos + border, gridScale * border * 2 - gridScale / 3, 2 * gridScale * border - gridScale / 3, imageO);
                Image level1 = null;

                try {
                    level1 = ImageIO.read(new File("youwin.jpg"));
                } catch (IOException e) {}

                ImageObserver imageO = new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                };
                g.drawImage(level1, 90*gridScale -50*gridScale/8, 10, 8*gridScale - gridScale/3, 8*gridScale - gridScale/3,imageO);
            }
        }
    }
}
