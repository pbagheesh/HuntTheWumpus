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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

import java.util.*;

/**
 * Creates a window with two text fields, two buttons, and a large
 * display area. The app then tracks the mouse over the display area
 * and follows the mouse with a circle.
 **/
public class Basic extends JFrame {

    /** holds the drawing canvas **/
    private BasicPanel canvas;

    /** height of the main drawing window **/
    int height;
    /** width of the main drawing window **/
    int width;

    /** Label field 1, displays the X location of the mouse **/
    JLabel fieldX;
    /** Label field 2, displays the Y location of the mouse **/
    JLabel fieldY;

    Color curColor;
    Color prevColor;

    Point curPoint;

    // controls whether the simulation is playing or exiting
    private enum PlayState { PLAY, STOP }
    private PlayState state= PlayState.PLAY;

    /**
     * Creates the main window
     * @param height the height of the window in pixels
     * @param width the width of the window in pixels
     **/
    public Basic( int height, int width ) {
        super("Basic Display");
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);

        this.canvas = new BasicPanel( height, width );
        this.add( this.canvas, BorderLayout.CENTER );
        this.pack();
        this.setVisible( true );

        this.fieldX = new JLabel("X");
        this.fieldY = new JLabel("Y");
        JButton color = new JButton("Color");
        JButton quit = new JButton("Quit");
        JPanel panel = new JPanel( new FlowLayout(FlowLayout.RIGHT));
        panel.add( this.fieldX );
        panel.add( this.fieldY );
        panel.add(color);
        panel.add(quit);

        this.add( panel, BorderLayout.SOUTH);
        this.pack();

        Control control = new Control();
        this.addKeyListener(control);
        this.setFocusable(true);
        this.requestFocus();

        color.addActionListener( control );
        quit.addActionListener( control );

        MouseControl mc = new MouseControl();
        this.addMouseListener( mc );
        this.addMouseMotionListener( mc );

        this.width = width;
        this.height = height;

        this.curColor = Color.blue;
        this.prevColor = Color.blue;

        this.curPoint = new Point( this.width/2, this.height/2 );
    }


    private class BasicPanel extends JPanel {

        /**
         * Creates the drawing canvas
         * @param height the height of the panel in pixels
         * @param width the width of the panel in pixels
         **/
        public BasicPanel(int height, int width) {
            super();
            this.setPreferredSize( new Dimension( width, height ) );
            this.setBackground(Color.white);
        }

        /**
         * Method overridden from JComponent that is responsible for
         * drawing components on the screen.  The supplied Graphics
         * object is used to draw.
         *
         * @param g		the Graphics object used for drawing
         */
        public void paintComponent(Graphics g) {
            Random gen = new Random();
            super.paintComponent(g);

            // do whatever drawing is necessary
            g.setColor( curColor );
            if( curPoint != null )
                g.fillOval( curPoint.x, curPoint.y-10, 20, 20 );
            else
                g.fillOval( width/2, height/2, 20, 20 );

        }
    } // end class BasicPanel

    private class MouseControl extends MouseInputAdapter {
        public void mouseMoved(MouseEvent e) {
            fieldX.setText( "" + e.getPoint().x );
            fieldY.setText( "" + e.getPoint().y );
            if( e.getPoint().x > 0 && e.getPoint().x < width &&
                    e.getPoint().y > 0 && e.getPoint().y < height ) {
                curPoint.x += (e.getPoint().x - curPoint.x) * 0.1;
                curPoint.y += (e.getPoint().y - curPoint.y) * 0.1;
            }
        }

        public void mouseDragged(MouseEvent e) {
            fieldX.setText( "" + e.getPoint().x );
            fieldY.setText( "" + e.getPoint().y );
            if( e.getPoint().x > 0 && e.getPoint().x < width &&
                    e.getPoint().y > 0 && e.getPoint().y < height ) {
                curPoint.x += (e.getPoint().x - curPoint.x) * 0.1;
                curPoint.y += (e.getPoint().y - curPoint.y) * 0.1;
            }
        }

        public void mousePressed(MouseEvent e) {
            System.out.println( "Pressed: " + e.getClickCount() );
            prevColor = curColor;
            curColor = Color.red;
        }

        public void mouseReleased(MouseEvent e) {
            System.out.println( "Released: " + e.getClickCount());
            curColor = prevColor;
        }

        public void mouseEntered(MouseEvent e) {
            System.out.println( "Entered: " + e.getPoint() );
            curColor = prevColor;
        }

        public void mouseExited(MouseEvent e) {
            System.out.println( "Exited: " + e.getPoint() );
            prevColor = curColor;
            curColor = Color.yellow;
        }

        public void mouseClicked(MouseEvent e) {
            System.out.println( "Clicked: " + e.getClickCount() );
        }
    } // end class MouseControul

    private class Control extends KeyAdapter implements ActionListener {

        public void keyTyped(KeyEvent e) {
            System.out.println( "Key Pressed: " + e.getKeyChar() );
            if( ("" + e.getKeyChar()).equalsIgnoreCase("q") ) {
                state = PlayState.STOP;
            }
        }

        public void actionPerformed(ActionEvent event) {
            if( event.getActionCommand().equalsIgnoreCase("Color") ) {
                // change the color of the ball
                Random gen = new Random();
                prevColor = new Color( gen.nextFloat(),
                        gen.nextFloat(),
                        gen.nextFloat() );
            }
            else if( event.getActionCommand().equalsIgnoreCase("Quit") ) {
                state = PlayState.STOP;
            }
        }
    } // end class Control


    public static void main(String[] argv) throws InterruptedException {
        Basic w = new Basic( 640, 480 );
        while(w.state == PlayState.PLAY) {
            w.repaint();
            Thread.sleep(33);
        }
        w.dispose();
    }

} // end class Basic