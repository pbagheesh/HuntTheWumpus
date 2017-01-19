import java.io.IOException;
import java.util.Random;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

import java.util.*;


/**
 * Created by prashant on 12/3/16.
 */
public class HuntTheWumpus extends JFrame {
    // Hunt The Wumpus creates a landscape and landscape display
    Landscape scape;
    LandscapeDisplay display;

    /** height of the main drawing window **/

    // controls whether the simulation is playing or exiting
    private enum GameState { PLAY, STOP, Quit }
    private GameState state= GameState.PLAY;

    public HuntTheWumpus(boolean multiplayer) {
        //Constructor creates a landscape and randomly
        scape = new Landscape(100,100, multiplayer, Landscape.PlayLevel.EASY);
        display = new LandscapeDisplay(scape,8);
    }

    public GameState getGameState() {
        //Returns the state of the game
        return this.state;
    }

    public void update() {
        //updates the state of the HuntTheWumpus so that if the landscape quits the hunt the wumpus class also quits
        if (this.scape.getPlayState() == Landscape.PlayState.STOP) {
            this.state = GameState.STOP;
        }
        display.update();   //Still updates the display so that GUI elements can work
    }

    public LandscapeDisplay getDisplay () {
        return this.display;
    }

    public static void main(String[] args) throws InterruptedException {
        //Creates the game and constantly updates the screen until user hits quit button
        HuntTheWumpus HTW = new HuntTheWumpus(false);
        int counter = 0;

        while (HTW.getGameState() != GameState.Quit) {
            HTW.getDisplay().repaint();
            Thread.sleep(30);
            HTW.update();

            if (counter == 0 && HTW.getGameState() == GameState.STOP) {  //if game is stop then UI elements should still work
                HTW.getDisplay().repaint();
                Thread.sleep(30);
                HTW.update();
                counter++;
            }
        }
    }
}
