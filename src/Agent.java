/**
 CS 231 Fall 2016
 Prashant
 Mushroom Project
 */

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;


public class Agent {
    //Parent class for Fertile Ground, Gatherer, Mushroom and Path Vertex
    // Holds the objects row and column and accessor functions for those methods
        private int row;
        private int col;

        public Agent(int r, int c) {
            //Constructor method sets parameter to field of Agent Object
            this.row = r;
            this.col = c;
        }

        public double getRow() {
            //return the rows position
            return this.row;
        }

        public double getCol() {
            //returns the cols
            return this.col;
        }
        public void setRow (int newRow) {
            //sets the row
            this.row = newRow;
        }
        public void setCol( int newCol ) {
            //sets the cols
            this.col = newCol;
        }


        public String toString() {
            // Returns a string with the values stored in x0 and y0
            String tempString;
            tempString = (this.row + ", " + this.col);
            return tempString;
        }

        public void draw(Graphics g, int gridScale) {
            //Do Nothing
        }

        public static void main(String[] args){
            //tests the functinos of the class
            Agent A1 = new Agent(3,3);
            System.out.println(A1.toString());
            A1.setRow(2);
            A1.setCol(2);
            System.out.println(A1.toString());
        }
    }
