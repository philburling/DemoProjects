package com.blahsoft.battleships;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/* This class displays a board used for a game of BattleShips */

class BoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private char [][] board;
	private boolean undiscoveredShipsDisplayed;
	/* The variable below controls whether any markers (misses, hits or undiscovered ships) are displayed at all */
	private boolean markersVisible;

	public BoardPanel(char[][] board) {
		this.board  = board;
		undiscoveredShipsDisplayed = false;
		markersVisible = true;
	}
	
	public boolean areUndiscoveredShipsDisplayed() {
		return undiscoveredShipsDisplayed;
	}

	public void setBoard(char[][] board) {
		this.board = board;		 
	}
	
	public void setUndiscoveredShipsDisplayed(boolean undiscoveredShipsDisplayed) {
		this.undiscoveredShipsDisplayed = undiscoveredShipsDisplayed;
	}

	public void setMarkersVisible(boolean visibility) {
		this.markersVisible = visibility;
	}
	
	/* This method draws the board on the panel. It works independently of the size of the panel.
	 * N.B. It assumes the panel is square-shaped */
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Dimension panelSize = getSize();
	    int side = Math.min(panelSize.width, panelSize.height);
	    
	    int xBorder = 0;
	    int yBorder = 0;
	    
	    Graphics2D g2D = (Graphics2D)g; 
   		g2D.setStroke(new BasicStroke(1));

	    /* Draw the play-grid */
	    g2D.setColor(Color.WHITE);
	    for (int i=1; i<board.length; i++) {
	    	g2D.drawLine( (i*(side/board.length)), 0, i*(side/board.length), side);
	    	g2D.drawLine( 0, ((i*(side/board.length)) + (yBorder/2)) , (side - (xBorder/2)), ((i*(side/board.length)) + (yBorder/2)) );
	    }
    	
	    /* Draw markers on the board */
	    if (markersVisible) {
	    	int offset = side/(board.length*4);
	    	int size = side/(board.length*2);
	    	for (int x = 0; x<board.length; x++) {
	    		for (int y = 0; y<board[x].length; y++) {
	    			
	    			int xCoordinate = (x*side/board.length);
	    			int yCoordinate = (y*side/board.length);
	    			// Draw ships that are hit...
	    			if (board[x][y] == Model.HIT) {
	    				g2D.setColor(Color.RED);
	    				g2D.fillOval(xCoordinate + offset, yCoordinate + offset, size, size);
	    			// Draw positions that have been selected by a player but where no ship was positioned...
	    			} else if (board[x][y] == Model.MISS) {
	    				g2D.setColor(Color.WHITE);
		    			g2D.drawLine(xCoordinate + offset, yCoordinate + offset, xCoordinate + offset + size, yCoordinate + offset + size);
		    			g2D.drawLine(xCoordinate + offset + size, yCoordinate + offset, xCoordinate + offset, yCoordinate + offset + size);
	    			} else if (undiscoveredShipsDisplayed) {
	    				// Draw undiscovered ships. This should only happen for the player that owns them.
	    				if (board[x][y] == Model.SHIP) {
	    					g2D.setColor(Color.WHITE);
	    					g2D.drawOval(xCoordinate + offset, yCoordinate + offset, size, size);
	    				}
	    			}
	    		}
	    	}
	    }
	}
}