package com.blahsoft.noughtsandcrosses;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.blahsoft.ai.AIStrategy;
import com.blahsoft.boardgame.Model;
import com.blahsoft.boardgame.GameStateModelInterface;
import com.blahsoft.boardgame.Move;
import com.blahsoft.designpatterns.ObserverInterface;


public class NCViewModel implements Model {
	ArrayList<WeakReference<ObserverInterface>> observerReferences;
	private int opponentMode;
	private boolean crossesStarts;
	private boolean gameOver;
	private GameStateModelInterface gameState;
	AIStrategy aiModule;

	public NCViewModel(AIStrategy aiModule) {
		this.aiModule = aiModule;
		observerReferences = new ArrayList<WeakReference<ObserverInterface>>();
		opponentMode = AI_PLAYER;
		crossesStarts = true;
		gameOver = false;
		gameState = new NCGameState();
		gameState.setPlayerTurn('X'); //Initialise crosses to start.
		notifyObservers();
	}
	
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
	
	@Override
	public int getOpponentMode() {
		return opponentMode;
	}

	@Override
	public String getAIStrategy() {
		return aiModule.toString();
	}
	
	@Override
	public char getStartingPlayer() {
		if (crossesStarts) {
			return 'X';
		} else {
			return 'O';
		}
	}
		
	public boolean gameIsOver() {
		return gameOver;
	}
	
	public GameStateModelInterface getGameState() {
		return gameState;
	}
	
	@Override
	public void setOpponentMode(int mode) throws IllegalArgumentException {
		if ((mode == AI_PLAYER) || (mode == HUMAN_PLAYER)) {
			opponentMode = mode;
			notifyObservers();
		} else {
			throw new IllegalArgumentException("Unexpected value passed to setOpponentMode");
		}
	}
	
	@Override
	public void setAIStrategy(AIStrategy aiModule) {
		this.aiModule = aiModule;
		notifyObservers();
		
	}
	
	@Override
	public void setStartingPlayer(char player) throws IllegalArgumentException {
		if (player == 'X' || player == 'x') {
			crossesStarts = true;
		} else if (player == 'O' || player == 'o') {
			crossesStarts = false;
		} else {
			throw new IllegalArgumentException("Unexpected char value passed to setStartingPlayer method in NCModel");
		}
		notifyObservers();
	}
	
	@Override
	public void resetBoard() {
			gameState = new NCGameState();
			notifyObservers();
	}
	
	@Override
	public void startGame() {
		gameOver = false;
		if (crossesStarts) {
			gameState.setPlayerTurn('X');
		} else {
			gameState.setPlayerTurn('O');
			// Take the first turn if 'O' is AI
			if (opponentMode == AI_PLAYER) {
				Move move = aiModule.getMove(gameState, 'O', 'X');
				gameState.addMove(move, 'O');
				gameState.nextTurn();
				notifyObservers();
			}
		}
	}
	
	@Override
	public void requestMove(Move moveRequested) {

		if (gameOver || (gameState.getPlayerTurn()=='O' && opponentMode == AI_PLAYER)) {
			return;
		} else if (gameState.getAvailableMoves().contains(moveRequested)) {
			if (gameState.getPlayerTurn() == 'X') {
				gameState.addMove(moveRequested, 'X');
			} else {
				gameState.addMove(moveRequested, 'O');
			}
			notifyObservers();
			checkForWin();
			gameState.nextTurn();
			notifyObservers();
			if (!gameOver && (opponentMode == AI_PLAYER)) {
				gameState.addMove(aiModule.getMove(gameState, 'O', 'X'), 'O');
				checkForWin();
				gameState.nextTurn();
				notifyObservers();
			}
		}
	}	
	
	private void checkForWin() {
		int utility = gameState.getUtility('X', 'O');
		if (utility == GameStateModelInterface.WIN) {
			gameOver = true;
		} else if (utility == GameStateModelInterface.LOSS) {
			gameOver = true;
		} else if (utility == GameStateModelInterface.DRAWN) {
			gameOver = true;
		} else if (utility == GameStateModelInterface.GAME_UNFINISHED) {
			return;
		}
		
	}
}
