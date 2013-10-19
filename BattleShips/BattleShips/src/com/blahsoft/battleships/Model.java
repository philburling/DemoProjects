package com.blahsoft.battleships;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.boardgame.Location;
import com.blahsoft.boardgame.Location2D;
import com.blahsoft.designpatterns.ObserverInterface;

public class Model implements ModelInterface {
	private static final int gridSize = 5;
	private static final int shipCount = 7;
	private ArrayList<WeakReference<ObserverInterface>> observerReferences;
	private Player player1;
	private Player player2; //In this implementation only player 2 can have an AI.
	private Player currentPlayer;
	private int phaseOfGame;
	private AIStrategy aiStrategy;
	private boolean player2AI;


/*-----------------------------
 * 
 * Model and game initialisation
 * 
 * ----------------------------
 */
	public Model(AIStrategy aiModule) {
		this.aiStrategy = aiModule;
		observerReferences = new ArrayList<WeakReference<ObserverInterface>>();
		player2AI = true; //Default. This could be overridden by calling setOpponentIsAI(false) before a game is started.
		initialiseGameState();
		notifyObservers();
		phaseOfGame = GAME_OVER;
	}
	
	@Override
	public void initialiseGameState() {
		player1 = new Player("Player 1", gridSize, shipCount, false);
		player2 = new Player("Player 2", gridSize, shipCount, player2AI);
		player1.setOpponent(player2);
		player2.setOpponent(player1);
		currentPlayer = player1;
		phaseOfGame = SETTING_UP;
		notifyObservers();
	}
	
	@Override
	public void setUpAIBoard(Player player) {
		aiStrategy.setUpBoard(player.getBoard(), player.getPiecesRemaining());
	}

/*----------------------------------
 *	
 * Accessors
 * 
 * ---------------------------------
 */	
	@Override
	public int getGridSize() {
		return gridSize;
	}

	@Override
	public int getPhaseOfGame() {
		return phaseOfGame;
	}

	@Override
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	@Override
	public String getAIStrategy() {
		return aiStrategy.toString();
	}
	
	@Override
	public boolean isOpponentAI() {
		if (player2.isAI()) {
			return true;
		} else {
			return false;
		}
	}

/*------------------------------------
 * 
 * Mutators
 * 
 * -----------------------------------
 */
	@Override
	public void nextPlayer() {
		if (currentPlayer == player1) {
			currentPlayer = player2;
		} else {
			currentPlayer = player1;
		}
		notifyObservers();
	}

	@Override
	public void setPhaseOfGame(int phaseOfGame) {
		this.phaseOfGame = phaseOfGame;
	}

	@Override
	public void setOpponentIsAI(boolean opponentIsAI) {
		if (opponentIsAI) {
			player2AI = true; //Necessary for when initialising a new game
			player2.setAI(true);
		} else {
			player2AI = false;
			player2.setAI(false);
		}
		notifyObservers();
	}
	
	@Override
	public void setAIStrategy(AIStrategy aiModule) {
		this.aiStrategy = aiModule;
		notifyObservers();
		
	}
	
	@Override
	/* This method places ship at a particular location on that user's board*/
	public void placePiece(Location location) {
		char[][] board = currentPlayer.getBoard();
		int x = ((Location2D)location).getX();
		int y = ((Location2D)location).getY();
		board[x][y] = SHIP;
		int pieces = currentPlayer.getPiecesToPlace();
		currentPlayer.setPiecesToPlace(pieces-1);
		notifyObservers();
	}
	
	@Override
	/*N.B. The case where a move has already been made in this location should be checked for by the class
	 * requesting the move and reported back to the user */
	public void requestMove(Location moveRequested) {
		addMove(currentPlayer, moveRequested);
	}
	
	@Override
	/* This method requests a move for the opponent of the player. The move is made according to 
	 * the current AI strategy held by the model */
	public void requestAIMove() {
		char[][] board = currentPlayer.getBoard();
		Location aiMove = aiStrategy.getMove(board);
		addMove(currentPlayer.getOpponent(), aiMove);
	}

	/* This is a helper method for requestMove() and requestAIMove() */
	private void addMove(Player player, Location location) {
		Player opponent = player.getOpponent(); 
		char[][] board = opponent.getBoard();
		int x = ((Location2D)location).getX();
		int y = ((Location2D)location).getY();
		
		if (board[x][y] == SHIP) {
			board[x][y] = HIT;
			int piecesRemaining = opponent.getPiecesRemaining();
			opponent.setPiecesRemaining(piecesRemaining-1);
		} else if (board[x][y] == UNMARKED){
			board[x][y] = MISS;
		}
		notifyObservers();
	}
	
/*-------------------------------------------------
 * 
 * ObserverInterface methods
 * 
 * ------------------------------------------------
 */
	@Override
	public void registerObserver(ObserverInterface observer) {
		WeakReference<ObserverInterface> weakReference = new WeakReference<ObserverInterface>(observer);
		observerReferences.add(weakReference);
	}
	
	@Override
	public void removeObserver(ObserverInterface observer) {
		for (WeakReference<ObserverInterface> weakReference : observerReferences) {
			if (weakReference.get() == observer) {
				observerReferences.remove(weakReference);
				return;
			}
		}
	}
	
	@Override
	public void notifyObservers() {
		for (WeakReference<ObserverInterface> weakReference : observerReferences) {
			ObserverInterface observer = weakReference.get();
			if (observer!=null) {
				observer.update();
			}
		}
	}

}
