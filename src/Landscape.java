/**
 * Created by prashant on 03/12/16
 */

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.layout.Background;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Landscape {
    //Contains row and cols fields
    //Contains two arraylists to help with drawing in order and putting hunter/wumpus objects on top of the vertex graph
    //Also has 2 hunter/1 wumpus/ 1 bullet
    //Boolean to change drawing depending on number of players
    private int rows = 0;
    private int cols = 0;
    private Random gen;
    private LinkedList<Agent> foreground;
    private LinkedList<Agent> background;
    private Graph caves;
    private Vertex[][] mesh;
    private Hunter hunter;
    private Hunter hunter2;
    private Wumpus wumpus;
    private Bullet bullet;
    private int numVertices;
    private boolean twoPlayer;

    enum PlayState { PLAY, STOP1, STOP}
    enum PlayLevel {EASY, MEDIUM, HARD}

    private PlayState stateOfGame;
    private PlayLevel gameLevel;

    public Landscape(int rows, int cols, boolean multiplayer, PlayLevel g) {
        //The landscape holds all the information required to play the game
        //Follow the comments carefully to understand what happens
        this.rows = rows;
        this.cols = cols;
        gen = new Random();
        foreground = new LinkedList<Agent>();
        background = new LinkedList<Agent>();
        mesh = new Vertex[rows][cols];          //First it Creates a 2D array of size row and col
        caves = new Graph();
        stateOfGame = PlayState.PLAY;
        numVertices = 20;
        gameLevel = g;
        twoPlayer = multiplayer;

        //First
        for (int i = rows/10; i < 7*rows/8; i+=5) {   //57% chance that a vertex will be created -- Meaning that there are different pockets of vertices
            for (int j=cols/6; j< 4*cols/5; j+=5) {
                if (gen.nextDouble() < 0.57) {
                    Vertex v = new Vertex(i, j);
                    mesh[i][j] = v;
                    this.addAgentB(v);
                }
            }
        }

        //Connecting all the vertices together
        for (int i = 0; i < rows; i++) {   //Connects any neighboring vertices together
            for (int j = 0; j < cols; j ++) {
                if (mesh[i][j]!= null && mesh[i-5][j]!= null) {
                    caves.addEdge(mesh[i][j], Vertex.Direction.NORTH,mesh[i-5][j]);
                }
                if (mesh[i][j]!= null && mesh[i+5][j]!= null) {
                    caves.addEdge(mesh[i][j], Vertex.Direction.SOUTH,mesh[i+5][j]);
                }
                if (mesh[i][j]!= null && mesh[i][j-5]!= null) {
                    caves.addEdge(mesh[i][j-5], Vertex.Direction.EAST,mesh[i][j]);
                }
                if (mesh[i][j]!= null && mesh[i][j+5]!= null) {
                    caves.addEdge(mesh[i][j], Vertex.Direction.EAST,mesh[i][j+5]);
                }
            }
        }
        //effectively has created multiple pockets of connected vertices

        Vertex wumpusVertex = this.getRandomVertex();  //randomly gets a vertex

        while (this.winneable(wumpusVertex) ==false) {   //Checks to see if the vertex is winneable (look at winneable method for more)
            wumpusVertex = this.getRandomVertex();  // if vertex is not winneable then it assigns a new one
        }

        ArrayList<Vertex> connectedVertices = caves.getConnectedVertices(wumpusVertex);  //gets all the connected vertices of wumpus vertex

        int intex = gen.nextInt(connectedVertices.size());
        int intex2 = gen.nextInt(connectedVertices.size());

        Vertex hunterVertex = connectedVertices.get(intex);  //randomly picks a new vertex from the connected vertices so that hunter can actually hunt the wumpus
        Vertex hunterVertex2 = connectedVertices.get(intex2);

        hunterVertex.setVisible(true);

        wumpus = new Wumpus(10,10,wumpusVertex);  //creates a wumpus object
        this.addAgentF(wumpus);

        hunter = new Hunter(10,10,hunterVertex, false);  //creates a hunter object
        this.addAgentF(hunter);

        if (this.twoPlayer == true) {
            hunter2 = new Hunter(10, 10, hunterVertex2, true);  //creates second hunter if multiplayer mode is enabled
            this.addAgentF(hunter2);
        }

        bullet = new Bullet(0,0,null);  //Needs to be created in the constructor since the key event listener does not allow IOExceptions
    }
    //Accessor functions below-- name is self explanatory
    public PlayLevel getGameLevel() {
        return this.gameLevel;
    }
    public Boolean getMultiplayerMode() {
        return this.twoPlayer;
    }
    public Boolean getOppositePlayerMode() {
        //Return the oppostie of the current player mode (Since it is restricted to a two player game)
        if(this.twoPlayer) {
            return false;
        }
        return true;
    }
    public boolean getNumPlayer() {return this.twoPlayer;}
    public void addAgentF(Agent a) {
        foreground.add(a);
    }
    public void addAgentB(Agent a) {
        background.add(a);
    }
    public Hunter getHunter() {
        return this.hunter;
    }
    public Hunter getHunter2() {
        return this.hunter2;
    }
    public void setAllNonHunted(boolean tf) {
        caves.setAllNonHunterVisited(tf);
    }
    public PlayState getPlayState() {
        return this.stateOfGame;
    }


    public int getRows() {
        //Returns the number of rows
        return this.rows;
    }

    public int getCols() {
        // Returns the number of Columns
        return this.cols;
    }

    public PlayLevel incrementLevel(PlayLevel p) {
        //Increments the hardness level
        if (p == PlayLevel.HARD) {
            System.out.println("Hardest Level");
            return PlayLevel.HARD;
        }
        else if (p == PlayLevel.MEDIUM) {
            return PlayLevel.HARD;
        }
        else if (p == PlayLevel.EASY) {
            return PlayLevel.MEDIUM;
        }
        return null;
    }

    public PlayLevel decrementLevel(PlayLevel p) {
        //Decrements the hardness level
        if (p == PlayLevel.EASY) {
            System.out.println("Easiest Level");
            return PlayLevel.EASY;
        }
        else if (p == PlayLevel.MEDIUM) {
            return PlayLevel.EASY;
        }
        else if (p == PlayLevel.HARD) {
            return PlayLevel.MEDIUM;
        }
        return null;
    }

    public void update() {
        //checks to see if hunter accidentally walked into the wumpus or the bullet misfired and should be killed
        if (hunter.getRow() == wumpus.getRow() && hunter.getCol() == wumpus.getCol() ) {
            wumpus.updateLifeStatus(true);
            wumpus.updateVisibility(true);
            hunter.setLifeStatus(false);
            System.out.println("Sorry you got killed by the wumpus");
            this.stateOfGame = PlayState.STOP1;
        }
        else if (hunter.getRow() == bullet.getRow() && hunter.getCol() == bullet.getCol()) {
            System.out.println("Sorry you got killed by your own bullet");
            hunter.setLifeStatus(false);
            this.stateOfGame = PlayState.STOP1;
        }
    }


    public boolean winneable(Vertex wV) {
        //Determines if the game is winneable if the wumpus is placed here.
        //Does so by making sure there are enough connected vertices for the hunter to find and kill the wumpus
        caves.shortestPath(wV);

        if (this.gameLevel == PlayLevel.EASY) {
            if (caves.getLCost() > 7) {   //Atleast 7 connected vertices for easy level
                return true;
            }
        }
        else if (this.gameLevel == PlayLevel.MEDIUM) {
            if (caves.getLCost() > 12) {  //Atleast 12 connected vertices for medium level
                return true;
            }
        }
        else if (this.gameLevel == PlayLevel.HARD) {
            if (caves.getLCost() > 17) {  //Atleast 17 connected vertices for the hardest level
                return true;
            }
        }
        return false;
    }

    public void move (Hunter h, Vertex.Direction d) {
        //Move the Hunter as defined by Direction parameter
        if (caves.getDirection(h.getCurrentVertex(), d)!= null) {
            h.updateCur(caves.getDirection(h.getCurrentVertex(), d));
            h.getCurrentVertex().setVisible(true);
            h.getCurrentVertex().setHunterVisited(true);
        }
        else {
            System.out.println("Invalid Movement");
        }
    }

    public Vertex getRandomVertex() {
        //gets a random vertex from the connected graph
        Random gen = new Random();
        int idex = gen.nextInt(background.size());
        Vertex v = (Vertex) background.get(idex);  //Not entirly sure why I need to convert to a vertex, since isnt vertex already agent extended
        return v;
    }

    public void follow(Vertex v, Vertex.Direction d) {
        //Makes the bullet vertex move along with the direction specified in the parameter
        if (this.hunter.getAmmoVal() != 0) {  //Means the hunter has bullets to fire
            this.hunter.decrementAmmoVal();
            bullet.updateCur(v);
            while (v.getNeighbor(d)!= null) {
                bullet.updateCur(v.getNeighbor(d));
                if (bullet.getRow() == wumpus.getRow() && bullet.getCol() == wumpus.getCol()) {  //If bullet hits the wumpus
                    wumpus.updateVisibility(true);   //Wumpus is made visible
                    wumpus.updateLifeStatus(false);  //Updates the life status of the wumpus
                    bullet.updateCur(null);   //Reset the bullet
                    return;
                }
                v = v.getNeighbor(d);
            }

            bullet.updateCur(this.hunter.getCurrentVertex());
        }

        if (this.hunter2.getAmmoVal() != 0) {  //Means the hunter has bullets to fire
            this.hunter2.decrementAmmoVal();
            bullet.updateCur(v);
            while (v.getNeighbor(d)!= null) {
                bullet.updateCur(v.getNeighbor(d));
                if (bullet.getRow() == wumpus.getRow() && bullet.getCol() == wumpus.getCol()) {  //If bullet hits the wumpus
                    wumpus.updateVisibility(true);   //Wumpus is made visible
                    wumpus.updateLifeStatus(false);  //Updates the life status of the wumpus
                    bullet.updateCur(null);   //Reset the bullet
                    return;
                }
                v = v.getNeighbor(d);
            }

            bullet.updateCur(this.hunter.getCurrentVertex());
        }


    }

    public void draw(Graphics g, int gridScale) {
        if (this.stateOfGame == PlayState.PLAY) {
            //draws the various components of the landscape
            g.setColor(Color.black);
            g.fillRect(0, 0, this.cols * gridScale, this.rows * gridScale);

            for (Agent a : background) {  //First draw all the vertices
                a.draw(g, gridScale);
            }
            for (Agent a : foreground) {  //Then draw agent objects lik wumpus and hunter
                a.draw(g, gridScale);
            }

            if (this.getHunter().getHunting()) {  //if hunting is enabled then a sign in the top right corner has an image showing shooting mode has been enabled
                Image crossHair = null;
                try {
                    crossHair = ImageIO.read(new File("sniper.png"));
                } catch (IOException e) {}

                ImageObserver imageO = new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                };
                g.drawImage(crossHair, this.cols*gridScale -50*gridScale/8, 10,gridScale*2*2 - gridScale/3, 2*gridScale*2 - gridScale/3,imageO);
            }

            if (this.gameLevel == PlayLevel.EASY) {  //Draws a button 1 on the top left corner if level is 1
                Image level1 = null;
                try {
                    level1 = ImageIO.read(new File("label1.png"));
                } catch (IOException e) {}

                ImageObserver imageO = new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                };
                g.drawImage(level1, 5, 10,gridScale*2*2 - gridScale/3, 2*gridScale*2 - gridScale/3,imageO);
            }
            else if (this.gameLevel == PlayLevel.MEDIUM) {  //Draws a button 2 on the top left corner if level is 2
                Image level2 = null;
                try {
                    level2 = ImageIO.read(new File("label2.png"));
                } catch (IOException e) {}

                ImageObserver imageO = new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                };
                g.drawImage(level2, 20, 10,gridScale*2*2 - gridScale/3, 2*gridScale*2 - gridScale/3,imageO);
            }
            else if (this.gameLevel == PlayLevel.HARD) { //Draws a button 3 on the top left corner if level is 3
                Image level3 = null;
                try {
                    level3 = ImageIO.read(new File("label3.png"));
                } catch (IOException e) {}

                ImageObserver imageO = new ImageObserver() {
                    @Override
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                };
                g.drawImage(level3, 40, 10,gridScale*2*2 - gridScale/3, 2*gridScale*2 - gridScale/3,imageO);
            }
            if (this.bullet.getCurVer() != null) {
                bullet.draw(g, gridScale);
            }
        }
        else if (this.stateOfGame == PlayState.STOP1) {  //if game is in stop 1 it draws all the elements just once but then moves to state stop
            //draws the various components of the landscape
            g.setColor(Color.black);
            g.fillRect(0, 0, this.cols * gridScale, this.rows * gridScale);
            for (Agent a : background) {
                a.draw(g, gridScale);
            }
            for (Agent a : foreground) {
                a.draw(g, gridScale);
            }
            this.stateOfGame = PlayState.STOP;
            return;
        }
        else if (this.stateOfGame == PlayState.STOP) { //Within state stop it only draws the background and makes sure not to draw over wumpus
            g.setColor(Color.black);
            g.fillRect(0, 0, this.cols * gridScale, this.rows * gridScale);
            for (Agent a : background) {
                if (wumpus.getCurVer() == a) {
                    continue;
                }
                else {
                    a.draw(g, gridScale);
                }
            }
            wumpus.draw(g,gridScale);
            Image RIP = null;
            try {
                RIP = ImageIO.read(new File("RIP.png"));
            } catch (IOException e) {}

            ImageObserver imageO = new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            };
            g.drawImage(RIP, 10, 10,20*gridScale, 25*gridScale,imageO);
        }
    }

    public String toString() {
        //Returns a string of the size of the grid
        String tempString = "Size of grid = " + this.rows + " x "+this.cols;
        return tempString;
    }

    public static void main(String[] args) throws IOException {
        //test the methods of the landscape
        Landscape scape = new Landscape(10,10,false,PlayLevel.EASY);
        Vertex v1 = new Vertex(10,10);

        scape.addAgentF(v1);
        System.out.println("Hi");
        System.out.println(scape.getRows());
        System.out.println(scape.getCols());
        System.out.println(scape.toString());
    }
}


