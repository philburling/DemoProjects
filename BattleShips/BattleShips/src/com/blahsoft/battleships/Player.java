package com.blahsoft.battleships;

import java.lang.ref.WeakReference;

/* This class encapsulates the board, name, number of pieces, opponent and mode of control (human/ai) for 
 * a player in a game of BattleShips.
 */

public class Player {
	
	private String id;
	private char[][] board; 
	private int piecesToPlace;
	private int piecesRemaining;
	private boolean isAI;
	private WeakReference<Player> opponentReference;
	
	public Player(String id, int boardSize, int piecesToPlace, boolean isAI) {
		board = new char[boardSize][boardSize]; //A char representation of the board has been chosen for an intuitive representation and to allow for easy potential console representation in place of a GUI.
		this.id = id;
		this.isAI = isAI;
		this.piecesToPlace = piecesToPlace;
		this.piecesRemaining = piecesToPlace;
	}

	/* Changes to the board should be made only through addMove and Reset methods.*/
	public String getId() {
		return id;
	} 
	
	public char[][] getBoard() {
		return board; 
	}

	public int getPiecesToPlace() {
		return piecesToPlace;
	}
	
	public int getPiecesRemaining() {
		return piecesRemaining;
	}
	
	public boolean isAI() {
		return isAI;
	}

	public void setPiecesToPlace(int shipsToPlace) {
		this.piecesToPlace = shipsToPlace;
	}
	
	public void setPiecesRemaining(int piecesRemaining) {
		this.piecesRemaining = piecesRemaining;
	}
	
	public Player getOpponent() {
		return opponentReference.get();
	}
	
	public void setAI(boolean isAI) {
		this.isAI = isAI;
	}

	public void setOpponent(Player opponent) {
		opponentReference = new WeakReference<Player>(opponent);
	}
	
}