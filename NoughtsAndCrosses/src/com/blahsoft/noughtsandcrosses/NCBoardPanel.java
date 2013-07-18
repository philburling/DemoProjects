package com.blahsoft.noughtsandcrosses;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/* This class displays a board used for a game of Noughts & Crosses */

class NCBoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int BOARD_SIZE = 3;
	private char [][] board;
	  
	public NCBoardPanel() {
		this.board  = new char[BOARD_SIZE][BOARD_SIZE];
	}
	
	//Use this method to choose a new board for the panel to display.
	public void setBoard(char[][] board) {
		if (board.length>BOARD_SIZE) {
			throw new IllegalArgumentException("Input array is too large");
		}
		this.board = board;		 
	}

	// This method draws the board on the panel. It works independently of the size of the panel.
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Dimension panelSize = getSize();
	    int side = Math.min(panelSize.width, panelSize.height);
	    
	    int xBorder = panelSize.width - side;
	    int yBorder = panelSize.height - side;
	    
	    Graphics2D g2D = (Graphics2D)g; 
   		g2D.setStroke(new BasicStroke(10));

	    //Draw the play-grid
	    g2D.setColor(Color.WHITE);
    	g2D.drawLine( ((side/3) + (xBorder/2)), yBorder/2, ((side/3) + (xBorder/2)), (side - (yBorder/2)) );
    	g2D.drawLine( ((2*(side/3)) + (xBorder/2)), yBorder/2, ((2*(side/3)) - (xBorder/2)), (side - (yBorder/2)) );
    	g2D.drawLine( (xBorder/2), ((side/3) + (yBorder/2)) , (side - (xBorder/2)), (side/3) + (yBorder/2) );
    	g2D.drawLine( (xBorder/2), ((2*(side/3)) + (yBorder/2)) , (side - (xBorder/2)), ((2*(side/3)) + (yBorder/2)) );
    	
	    //Draw a player marker on the board, the position and size of which are dynamically determined size of the containing window.
	    
    	int offset = side/12;
    	int size = side/6;
    	for (int x = 0; x<board.length; x++) {
	    	for (int y = 0; y<board[x].length; y++) {
	    		
	    		int xCoordinate = (x*side/3) + xBorder/2;//xBorder/2;// + ((x+1)*(side/3)) - side/9;
	    		int yCoordinate = (y*side/3) + yBorder/2;;//yBorder/2;// + ((y+1)*(side/3));
	    		
	    		if (board[x][y] == 'X') {
	    			g2D.drawLine(xCoordinate + offset, yCoordinate + offset, xCoordinate + offset + size, yCoordinate + offset + size);
	    			g2D.drawLine(xCoordinate + offset + size, yCoordinate + offset, xCoordinate + offset, yCoordinate + offset + size);
	    		} else if (board[x][y] == 'O') {
	    			g2D.drawOval(xCoordinate + offset, yCoordinate + offset, size, size);
	    		}
	    	}
	    }
	}
}