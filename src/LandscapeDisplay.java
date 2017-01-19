import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Displays a Landscape graphically using Swing.  The Landscape
 * can be displayed at any scale factor.
 * @author bseastwo
 */
public class LandscapeDisplay extends JFrame
{
    protected Landscape scape;
    private LandscapePanel canvas;
    private int gridScale; // width (and height) of each square in the grid

    /**
     * Initializes a display window for a Landscape.
     * @param scape the Landscape to display
     * @param scale controls the relative size of the display
     */
    public LandscapeDisplay(Landscape scape, int scale)
    {
        // setup the window
        super("Hunt the Wumpus");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.scape = scape;
        this.gridScale = scale;

        // create a panel in which to display the Landscape
        this.canvas = new LandscapePanel( (int) this.scape.getCols() * this.gridScale,
                                        (int) this.scape.getRows() * this.gridScale);

//        addKeyListener(this);

        Control control = new Control();  //Control class is class holding all the GUI interface commands


        /*
        Same general form is used for creating all the buttons
        first a Jbutton is created
        then an actional listener(Control object) is added to the button
        then the button is added to the canvas
         */

        JButton quit = new JButton("Quit");
        quit.addActionListener(control);
        canvas.add(quit);

        JButton switchmode = new JButton("Switch Num Players");
        switchmode.addActionListener(control);
        canvas.add(switchmode);

        JButton easier = new JButton("Easier");
        easier.addActionListener(control);
        canvas.add(easier);

        JButton harder = new JButton("Harder");
        harder.addActionListener(control);
        canvas.add(harder);

        JButton reset = new JButton("Replay");
        reset.addActionListener(control);
        canvas.add(reset);



//         add the panel to the window, layout, and display
        this.add(this.canvas, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);

        this.addKeyListener(control);
        this.setFocusable(true);
        this.requestFocus();
    }

    /**
     * Saves an image of the display contents to a file.  The supplied
     * filename should have an extension supported by javax.imageio, e.g.
     * "png" or "jpg".
     *
     * @param filename  the name of the file to save
     */
    public void saveImage(String filename)
    {
        // get the file extension from the filename
        String ext = filename.substring(filename.lastIndexOf('.') + 1, filename.length());

        // create an image buffer to save this component
        Component tosave = this.getRootPane();
        BufferedImage image = new BufferedImage(tosave.getWidth(), tosave.getHeight(), 
                                                BufferedImage.TYPE_INT_RGB);

        // paint the component to the image buffer
        Graphics g = image.createGraphics();
        tosave.paint(g);
        g.dispose();

        // save the image
        try
                {
                        ImageIO.write(image, ext, new File(filename));
                }
        catch (IOException ioe)
                {
                        System.out.println(ioe.getMessage());
                }
    }

    private class Control extends KeyAdapter implements ActionListener {

        public void keyTyped(KeyEvent e) {
            /*
            W/A/S/D keys are used for movement when hunter 1 is not shooting and when shooting mode is enabled it
            calls the follow method of landscape to see if the player shot correctly
             */
            if (scape.getPlayState() == Landscape.PlayState.PLAY) {
                if (e.getKeyChar() == 'w') {
                    if (!scape.getHunter().getHunting()) {
                        scape.move(scape.getHunter(), Vertex.Direction.NORTH);
                        scape.update();
                    } else {
                        scape.follow(scape.getHunter().getCurrentVertex(), Vertex.Direction.NORTH);
                        scape.update();
                    }
                }
                if (e.getKeyChar() == 'a') {
                    if (!scape.getHunter().getHunting()) {
                        scape.move(scape.getHunter(), Vertex.Direction.WEST);
                        scape.update();
                    } else {
                        scape.follow(scape.getHunter().getCurrentVertex(), Vertex.Direction.WEST);
                        scape.update();
                    }
                }
                if (e.getKeyChar() == 's') {
                    if (!scape.getHunter().getHunting()) {
                        scape.move(scape.getHunter(), Vertex.Direction.SOUTH);
                        scape.update();
                    } else {
                        scape.follow(scape.getHunter().getCurrentVertex(), Vertex.Direction.SOUTH);
                        scape.update();
                    }
                }
                if (e.getKeyChar() == 'd') {
                    if (!scape.getHunter().getHunting()) {
                        scape.move(scape.getHunter(), Vertex.Direction.EAST);
                        scape.update();
                    } else {
                        scape.follow(scape.getHunter().getCurrentVertex(), Vertex.Direction.EAST);
                        scape.update();
                    }
                }
                //Uses space key to toggle shooting mode for player 1
                if (e.getKeyChar() == ' ') {
                    if (scape.getHunter().getHunting()) {
                        System.out.println("Hunter 1 Shooting mode disabled");
                        scape.getHunter().setHunting(false);
                        scape.update();
                    } else {
                        System.out.println("Hunter 1 Shooting mode enabled");
                        scape.getHunter().setHunting(true);
                        scape.update();
                    }
                }
                /*
                i/j/k/l keys are used for movement when hunter 2 is not shooting and when shooting mode is enabled it
                calls the follow method of landscape to see if the player shot correctly
                 */

                if (scape.getNumPlayer()) {
                    if (e.getKeyChar() == 'i') {
                        if (!scape.getHunter().getHunting()) {
                            scape.move(scape.getHunter2(), Vertex.Direction.NORTH);
                            scape.update();
                        } else {
                            scape.follow(scape.getHunter2().getCurrentVertex(), Vertex.Direction.NORTH);
                            scape.update();
                        }
                    }
                    if (e.getKeyChar() == 'j') {
                        if (!scape.getHunter().getHunting()) {
                            scape.move(scape.getHunter2(), Vertex.Direction.WEST);
                            scape.update();
                        } else {
                            scape.follow(scape.getHunter2().getCurrentVertex(), Vertex.Direction.WEST);
                            scape.update();
                        }
                    }
                    if (e.getKeyChar() == 'k') {
                        if (!scape.getHunter().getHunting()) {
                            scape.move(scape.getHunter2(), Vertex.Direction.SOUTH);
                            scape.update();
                        } else {
                            scape.follow(scape.getHunter2().getCurrentVertex(), Vertex.Direction.SOUTH);
                            scape.update();
                        }
                    }
                    if (e.getKeyChar() == 'l') {
                        if (!scape.getHunter().getHunting()) {
                            scape.move(scape.getHunter2(), Vertex.Direction.EAST);
                            scape.update();
                        } else {
                            scape.follow(scape.getHunter2().getCurrentVertex(), Vertex.Direction.EAST);
                            scape.update();
                        }
                    }

                    //uses ',' to toggle multiplayer mode for player 2
                    if (e.getKeyChar() == ',') {
                        if (scape.getHunter().getHunting()) {
                            System.out.println("Hunter 2 Shooting mode disabled");
                            scape.getHunter().setHunting(false);
                            scape.update();
                        } else {
                            System.out.println("Hunter 2 Shooting mode enabled");
                            scape.getHunter().setHunting(true);
                            scape.update();
                        }
                    }
                }
            }
        }

        /*
        Pressing h key shows the entire map as long as button is held down and reverts back to regular mode when button is released
         */

    public void keyPressed(KeyEvent e) {
        //Hint button which reveals all the map
        if (e.getKeyChar() == 'h') {
            scape.setAllNonHunted(true);
            scape.update();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'h') {
            scape.setAllNonHunted(false);
            scape.update();
        }
    }

        public void actionPerformed(ActionEvent event) {
            if( event.getActionCommand().equalsIgnoreCase("Quit") ) {
                //Simply quits out of the game
                System.exit(0);
            }
            else if( event.getActionCommand().equalsIgnoreCase("Replay") ) {
                //Creates a new landscape with same rules as the last one
                scape = new Landscape(scape.getRows(),scape.getCols(),scape.getMultiplayerMode(), scape.getGameLevel());
                LandscapeDisplay.this.setFocusable(true);
                LandscapeDisplay.this.requestFocus();
            }

            else if( event.getActionCommand().equalsIgnoreCase("Switch Num Players") ) {
                //changes between single and multiplayer mode by creating new landscapes
                scape = new Landscape(scape.getRows(),scape.getCols(),scape.getOppositePlayerMode(), scape.getGameLevel());
                LandscapeDisplay.this.setFocusable(true);
                LandscapeDisplay.this.requestFocus();
            }

            else if( event.getActionCommand().equalsIgnoreCase("Easier") ) {
                    //makes the game easier by creating a new map
                    scape = new Landscape(scape.getRows(), scape.getCols(),scape.getMultiplayerMode(), scape.decrementLevel(scape.getGameLevel()));
                    LandscapeDisplay.this.setFocusable(true);
                    LandscapeDisplay.this.requestFocus();
            }

            else if( event.getActionCommand().equalsIgnoreCase("Harder") ) {
                //Makes the game harder by creating a harder map
                    scape = new Landscape(scape.getRows(), scape.getCols(),scape.getMultiplayerMode(), scape.incrementLevel(scape.getGameLevel()));
                    LandscapeDisplay.this.setFocusable(true);
                    LandscapeDisplay.this.requestFocus();
            }
        }
    }

    /**
     * This inner class provides the panel on which Landscape elements
     * are drawn.
     */
    private class LandscapePanel extends JPanel
    {
        /**
         * Creates the panel.
         * @param width     the width of the panel in pixels
         * @param height        the height of the panel in pixels
         */
        public LandscapePanel(int width, int height)
        {
                super();
                this.setPreferredSize(new Dimension(width, height));
                this.setBackground(Color.white);
        }

        /**
         * Method overridden from JComponent that is responsible for
         * drawing components on the screen.  The supplied Graphics
         * object is used to draw.
         * 
         * @param g     the Graphics object used for drawing
         */
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            scape.draw( g, gridScale );    
        } // end paintComponent
        
    } // end LandscapePanel

    public void update() {
        this.repaint(250);
        this.requestFocus();
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        Landscape scape = new Landscape(100,100,false, Landscape.PlayLevel.EASY);
        Random gen = new Random();
        double density = 0.08;

        Vertex v1 = new Vertex(10,10);
        Vertex v2 = new Vertex(10,15);
        Vertex v3 = new Vertex(15,10);
        Vertex v4 = new Vertex(15,15);

        v3.connect(v2, Vertex.Direction.SOUTH);
        v3.connect(v1, Vertex.Direction.NORTH);
        v3.connect(v4, Vertex.Direction.EAST);


        Hunter h1 = new Hunter(10,10,v1, false);
        Wumpus w1 = new Wumpus(10,10,v2);

        scape.addAgentF(h1);
        scape.addAgentF(w1);
        v1.setVisible(true);
        v2.setVisible(true);
        v3.setVisible(true);
        v4.setVisible(true);

        scape.addAgentB(v1);
        scape.addAgentB(v2);
        scape.addAgentB(v3);
        scape.addAgentB(v4);

        LandscapeDisplay display = new LandscapeDisplay(scape, 7);
        display.repaint();
        }
}
